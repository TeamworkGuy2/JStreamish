package streamUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.RandomAccess;
import java.util.Spliterators;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/** Utility methods for {@link Stream Streams} and stream related activities, such as mapping,
 * and reducing data sets, which are not provided by {@link StreamSupport}
 * @author TeamworkGuy2
 * @since 2014-11-2
 */
public class StreamUtil {

	public static final <T, R> List<R> map(T[] values, Function<? super T, ? extends R> convert) {
		ArrayList<R> res = new ArrayList<R>();

		for(T value : values) {
			res.add(convert.apply(value));
		}

		return res;
	}


	public static final <T, R> List<R> map(Collection<? extends T> values, Function<? super T, ? extends R> convert) {
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

}
