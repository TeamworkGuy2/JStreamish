package test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.IntStream;

import org.junit.Assert;
import org.junit.Test;

import checks.CheckTask;
import streamUtils.StreamSplitFilter;

/**
 * @author TeamworkGuy2
 * @since 2015-6-13
 */
public class StreamSplitFilterTest {

	@Test
	public void testStreamSplitFilter() {
		int[] elems = IntStream.range(0, 20).toArray();

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

		checkIntsAry(elems, expected, (i) -> {
			return i % 4;
		});

		checkIntsAry(elems, expectedFiltered, (i) -> {
			return i % 7 == 0 ? -1 : i % 4;
		});
	}


	private static void checkIntsAry(int[] src, int[][] expected, Function<Integer, Integer> mapper) {
		int size = expected.length;
		ArrayList<List<Integer>> dsts = new ArrayList<>();
		for(int i = 0; i < size; i++) {
			dsts.add(new ArrayList<>());
		}

		StreamSplitFilter.splitFilterNWay(Arrays.asList(IntStream.of(src).mapToObj((i) -> Integer.valueOf(i)).toArray((s) -> new Integer[s])), 4, dsts, mapper);

		int i = 0;
		for(List<Integer> dst : dsts) {
			Assert.assertArrayEquals(expected[i], dst.stream().mapToInt((val) -> val.intValue()).toArray());
			i++;
		}

	}

}
