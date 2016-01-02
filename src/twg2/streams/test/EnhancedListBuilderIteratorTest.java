package twg2.streams.test;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;

import twg2.streams.EnhancedListBuilderIterator;
import twg2.streams.IteratorToSupplier;

/**
 * @author TeamworkGuy2
 * @since 2016-1-2
 */
public class EnhancedListBuilderIteratorTest {

	public static EnhancedListBuilderIterator<String> getDefaultIter() {
		return new EnhancedListBuilderIterator<>(new IteratorToSupplier<>(Arrays.asList(
			"A",
			"B",
			"C",
			"D",
			"E"
		)));
	}


	@Test
	public void testInitialState() {
		EnhancedListBuilderIterator<String> iter = getDefaultIter();

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
	}

}
