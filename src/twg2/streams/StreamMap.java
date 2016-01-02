package twg2.streams;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.RandomAccess;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.StreamSupport;

/** Mapping methods for collections and arrays which are not provided by the
 * collections API or {@link StreamSupport}
 * @author TeamworkGuy2
 * @since 2015-5-25
 */
public class StreamMap {

	private StreamMap() { throw new AssertionError("cannot instantiate static class StreamMap"); }


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

}
