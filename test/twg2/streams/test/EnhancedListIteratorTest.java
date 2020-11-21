package twg2.streams.test;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;

import twg2.junitassist.checks.CheckTask;
import twg2.streams.EnhancedListIterator;

/**
 * @author TeamworkGuy2
 * @since 2016-1-2
 */
public class EnhancedListIteratorTest {

	public static EnhancedListIterator<String> getDefaultIter() {
		return new EnhancedListIterator<>(Arrays.asList(
			"A",
			"B",
			"C",
			"D",
			"E"
		));
	}


	@Test
	public void iterate() {
		EnhancedListIterator<String> iter = getDefaultIter();

		Assert.assertEquals(5, iter.size());

		Assert.assertEquals(0, iter.nextIndex());
		Assert.assertTrue(iter.hasNext());
		Assert.assertEquals(-1, iter.previousIndex());
		Assert.assertFalse(iter.hasPrevious());
		Assert.assertEquals("A", iter.peek());
		Assert.assertEquals("A", iter.next());

		Assert.assertEquals("B", iter.next());
		Assert.assertEquals("B", iter.peekPrevious());

		Assert.assertEquals("B", iter.previous());
		Assert.assertTrue(iter.hasNext());
		Assert.assertTrue(iter.hasPrevious());
		Assert.assertEquals(0, iter.previousIndex());
		Assert.assertEquals(1, iter.nextIndex());

		Assert.assertEquals("B", iter.next());
		Assert.assertEquals("C", iter.next());
		iter.mark();

		Assert.assertEquals("C", iter.previous());
		Assert.assertEquals("B", iter.previous());
		Assert.assertEquals("A", iter.previous());
		Assert.assertFalse(iter.hasPrevious());

		iter.reset();
		Assert.assertEquals("C", iter.peekPrevious());
		Assert.assertEquals("D", iter.next());
		Assert.assertEquals("D", iter.previous());
		Assert.assertEquals("C", iter.peekPrevious());
		Assert.assertEquals(2, iter.previousIndex());
		Assert.assertEquals(3, iter.nextIndex());

		Assert.assertEquals("D", iter.next());
		Assert.assertTrue(iter.hasNext());
		Assert.assertEquals("E", iter.next());
		Assert.assertFalse(iter.hasNext());
		Assert.assertNull(iter.next());
		Assert.assertNull(iter.peek());

		Assert.assertEquals(5, iter.size());
	}


	@Test
	public void invalidOperations() {
		EnhancedListIterator<String> iter = getDefaultIter();

		CheckTask.assertException(() -> iter.peekPrevious());
		CheckTask.assertException(() -> iter.previous());
		Assert.assertEquals("A", iter.next());
		CheckTask.assertException(() -> iter.add("-"));
		CheckTask.assertException(() -> iter.set("-"));
		CheckTask.assertException(() -> iter.remove());
		CheckTask.assertException(() -> iter.reset()); // without calling mark() first
		CheckTask.assertException(() -> iter.reset(-1));
		CheckTask.assertException(() -> iter.reset(100)); // larger than iterator size
	}

}
