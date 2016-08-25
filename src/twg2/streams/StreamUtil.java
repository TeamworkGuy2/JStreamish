package twg2.streams;

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
import java.util.function.Function;
import java.util.stream.BaseStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/** Utility methods for {@link Stream Streams} and stream related activities, such as for-each,
 * and reducing data sets, which are not provided by {@link StreamSupport}
 * @author TeamworkGuy2
 * @since 2014-11-2
 */
public class StreamUtil {

	private StreamUtil() { throw new AssertionError("cannot instantiate static class StreamUtil"); }


	/** Iterate over two arrays and stop once the end of the shorter array is reached
	 */
	public static final <K, V> void forEachPair(K[] keys, V[] values, BiConsumer<K, V> consumer) {
		for(int i = 0, size = Math.min(keys.length, values.length); i < size; i++) {
			consumer.accept(keys[i], values[i]);
		}
	}


	/** Iterate over two {@link Collection Collections} and stop once the end of the shorter collection is reached
	 */
	public static final <K, V> void forEachPair(Iterable<? extends K> keys, Iterable<? extends V> values, BiConsumer<K, V> consumer) {
		if(keys instanceof List && values instanceof List && keys instanceof RandomAccess && values instanceof RandomAccess) {
			List<? extends K> keysList = (List<? extends K>)keys;
			List<? extends V> valuesList = (List<? extends V>)values;
			for(int i = 0, size = Math.min(keysList.size(), valuesList.size()); i < size; i++) {
				consumer.accept(keysList.get(i), valuesList.get(i));
			}
		}
		else {
			forEachPair(keys.iterator(), values.iterator(), consumer);
		}
	}


	public static final <K, L extends BaseStream<? extends K, L>, V, W extends BaseStream<? extends V, W>> void forEachPair(
			BaseStream<? extends K, ? extends L> keys, BaseStream<? extends V, ? extends W> values, BiConsumer<K, V> consumer) {
		forEachPair(keys.iterator(), values.iterator(), consumer);
	}


	public static final <K, V> void forEachPair(Iterator<? extends K> keys, Iterator<? extends V> values, BiConsumer<K, V> consumer) {
		while(keys.hasNext() && values.hasNext()) {
			consumer.accept(keys.next(), values.next());
		}
	}


	public static final <T> Stream<T> asStream(Iterator<T> iter) {
		return asStream(iter, false);
	}


	public static final <T> Stream<T> asStream(Iterator<T> iter, int knownIteratorSize) {
		return asStream(iter, 0, false, knownIteratorSize);
	}


	public static final <T> Stream<T> asStream(Iterator<T> iter, boolean parallel) {
		return asStream(iter, 0, parallel);
	}


	public static final <T> Stream<T> asStream(Iterator<T> iter, boolean parallel, int knownIteratorSize) {
		return asStream(iter, 0, parallel, knownIteratorSize);
	}


	public static final <T> Stream<T> asStream(Iterator<T> iter, int characteristics, boolean parallel) {
		return StreamSupport.stream(Spliterators.spliteratorUnknownSize(iter, characteristics), parallel);
	}


	public static final <T> Stream<T> asStream(Iterator<T> iter, int characteristics, boolean parallel, int knownIteratorSize) {
		return StreamSupport.stream(Spliterators.spliterator(iter, knownIteratorSize, characteristics), parallel);
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


	public static final <K, V, S> HashMap<K, V> toHashMap(Stream<? extends S> stream, Function<S, Map.Entry<? extends K, ? extends V>> transformer) {
		return toMap(stream, transformer, new HashMap<K, V>());
	}


	public static final <K, V, R extends Map<? super K, ? super V>> R toMap(Stream<? extends Map.Entry<K, V>> stream, R dst) {
		stream.forEach((item) -> dst.put(item.getKey(), item.getValue()));
		return dst;
	}


	public static final <K, V, S, T extends Map<? super K, ? super V>> T toMap(Stream<? extends S> stream, Function<S, Map.Entry<? extends K, ? extends V>> transformer, T dst) {
		stream.forEach((obj) -> {
			Map.Entry<? extends K, ? extends V> entry = transformer.apply(obj);
			dst.put(entry.getKey(), entry.getValue());
		});
		return dst;
	}


	@SuppressWarnings("unchecked")
	public static final <R, T extends R> R[] toArray(Stream<T> stream, Class<R> type) {
		return stream.toArray((size) -> (R[])Array.newInstance(type, size));
	}

}
