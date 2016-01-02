package twg2.streams;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.RandomAccess;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @author TeamworkGuy2
 * @since 2015-6-13
 */
public class StreamSplitFilter {

	private StreamSplitFilter() { throw new AssertionError("cannot instantiate static class StreamSplitFilter"); }


	public static <E, C extends Collection<E>> Map.Entry<C, C> splitFilter(Collection<E> coll, Supplier<C> createColl, Function<E, Boolean> splitter) {
		C list1 = createColl.get();
		C list2 = createColl.get();
		return split2Way(coll, list1, list2, splitter);
	}


	/** @see #splitFilterNWay(Collection, int, Supplier, Function)
	 */
	public static <E, C extends Collection<E>> Map.Entry<C, C> split2Way(Collection<E> coll, C list1, C list2, Function<E, Boolean> splitter) {
		List<C> colls = new ArrayList<>(2);
		colls.add(list1);
		colls.add(list2);
		colls = splitFilterNWay(coll, 2, colls, (elem) -> splitter.apply(elem) ? 0 : 1);

		return new AbstractMap.SimpleImmutableEntry<C, C>(colls.get(0), colls.get(1));
	}


	public static <E, C extends Collection<E>> List<C> splitNWay(Collection<E> coll, int splitWays, Supplier<C> createColl, Function<E, Integer> splitter) {
		return splitFilterNWay(coll, splitWays, false, createColl, splitter);
	}


	public static <E, C extends Collection<E>> List<C> splitNWay(Collection<E> coll, int splitWays, List<C> colls, Function<E, Integer> splitter) {
		return splitFilterNWay(coll, splitWays, false, colls, splitter);
	}


	public static <E, C extends Collection<E>> List<C> splitFilterNWay(Collection<E> coll, int splitWays, Supplier<C> createColl, Function<E, Integer> splitter) {
		return splitFilterNWay(coll, splitWays, true, createColl, splitter);
	}


	/** Split a collection of elements into groups or filter out elements, return a list of sub-collections
	 * @param coll the source collection
	 * @param splitWays the number of sub-collections to split the source into
	 * @param colls the list of sub-collections to store the split results in
	 * @param splitter the function to determine which sub-collection to store each source element in,
	 * returned values must be in the range {@code [0, splitWays]} which will store the input
	 * element in that sub-collection, or -1 to filter out the element and not store it in a sub-collection
	 * @return the {@code colls}
	 */
	public static <E, C extends Collection<E>> List<C> splitFilterNWay(Collection<E> coll, int splitWays, List<C> colls, Function<E, Integer> splitter) {
		return splitFilterNWay(coll, splitWays, true, colls, splitter);
	}


	static <E, C extends Collection<E>> List<C> splitFilterNWay(Collection<E> coll, int splitWays, boolean allowNegativeToFilter, Supplier<C> createColl, Function<E, Integer> splitter) {
		List<C> resultColls = new ArrayList<>(splitWays);
		for(int i = 0; i < splitWays; i++) {
			resultColls.add(createColl.get());
		}
		return splitFilterNWay(coll, splitWays, allowNegativeToFilter, resultColls, splitter);
	}


	static <E, C extends Collection<E>> List<C> splitFilterNWay(Collection<E> coll, int splitWays, boolean allowNegativeToFilter, List<C> colls, Function<E, Integer> splitter) {
		if(coll instanceof RandomAccess && coll instanceof List) {
			List<E> list = (List<E>)coll;
			for(int i = 0, size = list.size(); i < size; i++) {
				E elem = list.get(i);
				Integer res = splitter.apply(elem);
				if(res == -1) {
					if(allowNegativeToFilter) {
						continue;
					}
					else {
						throw new IllegalStateException("splitter function cannot return negative results if filtering is disabled");
					}
				}
				if(res > splitWays) {
					throw new IllegalStateException("splitter result must be in the range [0," + splitWays + "] (or -1), was: " + res);
				}
				else {
					colls.get(res).add(elem);
				}
			}
		}
		else {
			for(E elem : coll) {
				Integer res = splitter.apply(elem);
				if(res == -1) {
					continue;
				}
				if(res > splitWays) {
					throw new IllegalStateException("splitter result must be in the range [0," + splitWays + "] (or -1), was: " + res);
				}
				else {
					colls.get(res).add(elem);
				}
			}
		}

		return colls;
	}


}
