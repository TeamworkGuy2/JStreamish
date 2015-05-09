package streamUtils;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.RandomAccess;
import java.util.Set;
import java.util.Spliterators;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/** Utility methods for {@link Stream Streams} and stream related activities, such as mapping,
 * and reducing data sets, which are not provided by {@link StreamSupport}
 * @author TeamworkGuy2
 * @since 2014-11-2
 */
public class StreamUtil {

	public static final <T, R> List<R> map(T[] values, Function<? super T, R> convert) {
		ArrayList<R> res = new ArrayList<R>();

		for(T value : values) {
			res.add(convert.apply(value));
		}

		return res;
	}


	public static final <T, R> List<R> map(Collection<? extends T> values, Function<? super T, R> convert) {
		ArrayList<R> res = new ArrayList<R>();

		if(values instanceof List && values instanceof RandomAccess) {
			List<? extends T> valuesList = (List<? extends T>)values;
			for(int i = 0, size = values.size(); i < size; i++) {
				res.add(convert.apply(valuesList.get(i)));
			}
		}
		else {
			for(T value : values) {
				res.add(convert.apply(value));
			}
		}

		return res;
	}


	/** Returns a transformed map where all of the new keys are ensured to be unique (i.e. the size of the original and returned maps will be the same).
	 * An error is thrown if two new keys collide. The map's values are transfered without modification to the new map
	 * @param values the map of values
	 * @param keyMapper the function to transform keys
	 * @return the map of transformed keys to original values
	 * @see #mapCheckNewKeyUniqueness(Map, Function, Function, boolean, boolean)
	 */
	public static final <K, V, R> Map<R, V> mapRequireUnique(Map<K, V> values, Function<? super K, R> keyMapper) {
		return mapCheckNewKeyUniqueness(values, keyMapper, Function.identity(), false, true);
	}


	/** Returns a transformed map where all of the new keys are ensured to be unique (i.e. the size of the original and returned maps will be the same).
	 * An error is thrown if two new keys collide
	 * @param values the map of values
	 * @param keyMapper the function to transform keys
	 * @param valueMapper the function to transform values
	 * @return the map of transformed keys to original values
	 * @see #mapCheckNewKeyUniqueness(Map, Function, Function, boolean, boolean)
	 */
	public static final <K, V, R, S> Map<R, S> mapRequireUnique(Map<K, V> values, Function<? super K, R> keyMapper, Function<? super V, S> valueMapper) {
		return mapCheckNewKeyUniqueness(values, keyMapper, valueMapper, false, true);
	}



	/** Returns a map of transformed keys and values.
	 * @param values the map of values
	 * @param keyMapper the function to transform keys
	 * @param valueMapper the function to transform values
	 * @param ifDuplicateKeyKeepFirst true to keep the first key if two new keys are duplicates, false to overwrite new keys with duplicate new keys as the map is transformed
	 * (the order of map traversal is not guaranteed so the order in which duplicate entries overwrite each other is not guaranteed)
	 * @param throwIfDuplicateKeys true to throw an error if duplicate new keys are encountered, this flag overrides {@code ifDuplicateKeyKeepFirst}
	 * @return the map of transformed keys to original values
	 */
	public static final <K, V, R, S> Map<R, S> mapCheckNewKeyUniqueness(Map<K, V> values, Function<? super K, R> keyMapper, Function<? super V, S> valueMapper,
			boolean ifDuplicateKeyKeepFirst, boolean throwIfDuplicateKeys) {
		Map<R, S> resMap = new HashMap<>();
		// standard mapping operation
		if(!ifDuplicateKeyKeepFirst && !throwIfDuplicateKeys) {
			for(Map.Entry<K, V> entry : values.entrySet()) {
				resMap.put(keyMapper.apply(entry.getKey()), valueMapper.apply(entry.getValue()));
			}
		}

		// keep first if duplicates
		else if(ifDuplicateKeyKeepFirst & !throwIfDuplicateKeys) {
			for(Map.Entry<K, V> entry : values.entrySet()) {
				R newKey = keyMapper.apply(entry.getKey());
				if(!resMap.containsKey(newKey)) {
					resMap.put(newKey, valueMapper.apply(entry.getValue()));
				}
			}
		}

		// throw if duplicates
		else {
			for(Map.Entry<K, V> entry : values.entrySet()) {
				R newKey = keyMapper.apply(entry.getKey());
				if(resMap.containsKey(newKey)) {
					// find the original key for descriptive error
					for(Map.Entry<K, V> original : values.entrySet()) {
						if(keyMapper.apply(original.getKey()).equals(newKey)) {
							throw new IllegalArgumentException("duplicate key: '" + newKey + "' first: " + original.getKey() + ", second: " + entry);
						}
					}
				}
				resMap.put(newKey, valueMapper.apply(entry.getValue()));
			}
		}
		return resMap;
	}


	/** Returns a transformed map where all of the new keys are ensured to be unique (i.e. the size of the original and returned maps will be the same).
	 * An error is thrown if two new keys collide. The map's values are transfered without modification to the new map
	 * @param values the map of values
	 * @param entryMapper the function to transform keys
	 * @return the map of transformed keys to original values
	 * @see #mapCheckNewKeyUniqueness(Map, BiFunction, boolean, boolean)
	 */
	public static final <K, V, R, S> Map<R, S> mapRequireUnique(Map<K, V> values, BiFunction<? super K, ? super V, ? extends Map.Entry<R, S>> entryMapper) {
		return mapCheckNewKeyUniqueness(values, entryMapper, false, true);
	}


	/** Returns a map of transformed keys and values.
	 * @param values the map of values
	 * @param entryMapper the function to transform keys and values
	 * @param ifDuplicateKeyKeepFirst true to keep the first key if two new keys are duplicates, false to overwrite new keys with duplicate new keys as the map is transformed
	 * (the order of map traversal is not guaranteed so the order in which duplicate entries overwrite each other is not guaranteed)
	 * @param throwIfDuplicateKeys true to throw an error if duplicate new keys are encountered, this flag overrides {@code ifDuplicateKeyKeepFirst}
	 * @return the map of transformed keys to original values
	 */
	public static final <K, V, R, S> Map<R, S> mapCheckNewKeyUniqueness(Map<K, V> values, BiFunction<? super K, ? super V, ? extends Map.Entry<R, S>> entryMapper,
			boolean ifDuplicateKeyKeepFirst, boolean throwIfDuplicateKeys) {
		Map<R, S> resMap = new HashMap<>();
		// standard mapping operation
		if(!ifDuplicateKeyKeepFirst && !throwIfDuplicateKeys) {
			for(Map.Entry<K, V> entry : values.entrySet()) {
				Map.Entry<R, S> newEntry = entryMapper.apply(entry.getKey(), entry.getValue());
				resMap.put(newEntry.getKey(), newEntry.getValue());
			}
		}

		// keep first if duplicates
		else if(ifDuplicateKeyKeepFirst & !throwIfDuplicateKeys) {
			for(Map.Entry<K, V> entry : values.entrySet()) {
				Map.Entry<R, S> newEntry = entryMapper.apply(entry.getKey(), entry.getValue());
				if(!resMap.containsKey(newEntry.getKey())) {
					resMap.put(newEntry.getKey(), newEntry.getValue());
				}
			}
		}

		// throw if duplicates
		else {
			for(Map.Entry<K, V> entry : values.entrySet()) {
				Map.Entry<R, S> newEntry = entryMapper.apply(entry.getKey(), entry.getValue());
				R newKey = newEntry.getKey();
				if(resMap.containsKey(newKey)) {
					// find the original key for descriptive error, we inefficiently run the entryMapper over the map's entries
					// again, however, an error is about to be thrown so the overhead won't affect normal program execution
					for(Map.Entry<K, V> original : values.entrySet()) {
						if(entryMapper.apply(original.getKey(), original.getValue()).getKey().equals(newKey)) {
							throw new IllegalArgumentException("duplicate key: '" + newKey + "' first: " + original.getKey() + ", second: " + entry);
						}
					}
				}
				resMap.put(newKey, newEntry.getValue());
			}
		}
		return resMap;
	}


	/** Iterate over two arrays and stop once the end of the shorter array is reached
	 */
	public static final <K, V> void forEachPair(K[] keys, V[] values, BiConsumer<K, V> consumer) {
		for(int i = 0, size = Math.min(keys.length, values.length); i < size; i++) {
			consumer.accept(keys[i], values[i]);
		}
	}


	/** Iterate over two {@link Collection Collections} and stop once the end of the shorter collection is reached
	 */
	public static final <K, V> void forEachPair(Collection<? extends K> keys, Collection<? extends V> values, BiConsumer<K, V> consumer) {
		if(keys instanceof List && values instanceof List && keys instanceof RandomAccess && values instanceof List) {
			List<? extends K> keysList = (List<? extends K>)keys;
			List<? extends V> valuesList = (List<? extends V>)values;
			for(int i = 0, size = Math.min(keysList.size(), valuesList.size()); i < size; i++) {
				consumer.accept(keysList.get(i), valuesList.get(i));
			}
		}
		else {
			Iterator<? extends K> keyIter = keys.iterator();
			Iterator<? extends V> valIter = values.iterator();
			while(keyIter.hasNext() && valIter.hasNext()) {
				consumer.accept(keyIter.next(), valIter.next());
			}
		}
	}


	public static final <T> Stream<T> asStream(Iterator<T> iter) {
		return asStream(iter, false);
	}


	public static final <T> Stream<T> asStream(Iterator<T> iter, boolean parallel) {
		return StreamSupport.stream(Spliterators.spliteratorUnknownSize(iter, 0), parallel);
	}


	public static final <T> Stream<T> asStream(Iterator<T> iter, int characteristics, boolean parallel) {
		return StreamSupport.stream(Spliterators.spliteratorUnknownSize(iter, characteristics), parallel);
	}


	public static final <T, R extends Collection<? super T>> R toCollection(Stream<T> stream, R dst) {
		stream.forEach((item) -> dst.add(item));
		return dst;
	}


	public static final <T> List<T> toArrayList(Stream<T> stream) {
		return toList(stream, new ArrayList<>());
	}


	public static final <T, R extends List<? super T>> R toList(Stream<T> stream, R dst) {
		stream.forEach((item) -> dst.add(item));
		return dst;
	}


	public static final <T> Set<T> toHashSet(Stream<T> stream) {
		return toSet(stream, new HashSet<>());
	}


	public static final <T, R extends Set<? super T>> R toSet(Stream<T> stream, R dst) {
		stream.forEach((item) -> dst.add(item));
		return dst;
	}


	public static final <K, V> HashMap<K, V> toHashMap(Stream<? extends Map.Entry<K, V>> stream) {
		return toMap(stream, new HashMap<>());
	}


	public static final <K, V, R> HashMap<K, V> toHashMap(Stream<? extends R> stream, Function<R, Map.Entry<? extends K, ? extends V>> transformer) {
		return toMap(stream, transformer, new HashMap<K, V>());
	}


	public static final <K, V, R extends Map<? super K, ? super V>> R toMap(Stream<? extends Map.Entry<K, V>> stream, R dst) {
		stream.forEach((item) -> dst.put(item.getKey(), item.getValue()));
		return dst;
	}


	public static final <K, V, R, S extends Map<? super K, ? super V>> S toMap(Stream<? extends R> stream, Function<R, Map.Entry<? extends K, ? extends V>> transformer, S dst) {
		stream.forEach((obj) -> {
			Map.Entry<? extends K, ? extends V> entry = transformer.apply(obj);
			dst.put(entry.getKey(), entry.getValue());
		});
		return dst;
	}


	@SuppressWarnings("unchecked")
	public static final <T> T[] toArray(Stream<T> stream, Class<? super T> type) {
		return stream.toArray((size) -> (T[])Array.newInstance(type, size));
	}

}
