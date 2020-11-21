package twg2.streams.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import org.junit.Assert;
import org.junit.Test;

import twg2.streams.StreamSplitFilter;

/**
 * @author TeamworkGuy2
 * @since 2015-6-13
 */
public class StreamSplitFilterTest {
	List<Integer> elems = toIntegers(IntStream.range(0, 20).toArray());

	int[][] expected = new int[][] {
			{ 0, 4, 8, 12, 16 },
			{ 1, 5, 9, 13, 17 },
			{ 2, 6, 10, 14, 18 },
			{ 3, 7, 11, 15, 19 }
	};
	int[][] expectedFiltered = new int[][] {
			{ 4, 8, 12, 16 },
			{ 1, 5, 9, 13, 17 },
			{ 2, 6, 10, 18 },
			{ 3, 11, 15, 19 }
	};
	int[] evens = new int[] { 0, 2, 4, 6, 8, 10, 12, 14, 16, 18 };
	int[] odds = new int[] { 1, 3, 5, 7, 9, 11, 13, 15, 17, 19 };


	@Test
	public void splitFilterNWayLists() {
		List<List<Integer>> dsts;

		dsts = StreamSplitFilter.splitFilterNWay(elems, 4, createLists(expected.length), (i) -> {
			return i % 4;
		});

		checkResults(dsts, expected);

		dsts = StreamSplitFilter.splitFilterNWay(elems, 4, createLists(expectedFiltered.length), (i) -> {
			return i % 7 == 0 ? -1 : i % 4;
		});

		checkResults(dsts, expectedFiltered);
	}


	@Test
	public void splitFilterNWaySupplier() {
		List<List<Integer>> dsts;

		dsts = StreamSplitFilter.splitFilterNWay(elems, 4, () -> new ArrayList<Integer>(), (i) -> {
			return i % 4;
		});

		checkResults(dsts, expected);

		dsts = StreamSplitFilter.splitFilterNWay(elems, 4, () -> new ArrayList<Integer>(), (i) -> {
			return i % 7 == 0 ? -1 : i % 4;
		});

		checkResults(dsts, expectedFiltered);
	}


	@Test
	public void splitFilter() {
		Map.Entry<List<Integer>, List<Integer>> results;

		results = StreamSplitFilter.splitFilter(elems, () -> new ArrayList<Integer>(), (i) -> {
			return i % 2 == 0;
		});

		Assert.assertArrayEquals(evens, toInts(results.getKey()));
		Assert.assertArrayEquals(odds, toInts(results.getValue()));

		results = StreamSplitFilter.split2Way(new LinkedList<>(elems), new ArrayList<Integer>(), new ArrayList<Integer>(), (i) -> {
			return i % 2 == 0;
		});

		Assert.assertArrayEquals(evens, toInts(results.getKey()));
		Assert.assertArrayEquals(odds, toInts(results.getValue()));
	}


	private static void checkResults(List<List<Integer>> results, int[][] expected) {
		int i = 0;
		for(List<Integer> resultSet : results) {
			Assert.assertArrayEquals(expected[i], toInts(resultSet));
			i++;
		}
	}


	private static List<List<Integer>> createLists(int count) {
		ArrayList<List<Integer>> dsts = new ArrayList<>();
		for(int i = 0; i < count; i++) {
			dsts.add(new ArrayList<>());
		}
		return dsts;
	}


	private static List<Integer> toIntegers(int[] src) {
		return Arrays.asList(IntStream.of(src).mapToObj((i) -> Integer.valueOf(i)).toArray((s) -> new Integer[s]));
	}


	private static int[] toInts(List<Integer> dst) {
		return dst.stream().mapToInt((val) -> val.intValue()).toArray();
	}

}
