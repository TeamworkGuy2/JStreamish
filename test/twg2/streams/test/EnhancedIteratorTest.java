package twg2.streams.test;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicReference;

import org.junit.Assert;
import org.junit.Test;

import twg2.junitassist.checks.CheckTask;
import twg2.streams.EnhancedIterator;
import twg2.streams.IteratorToSupplier;

/**
 * @author TeamworkGuy2
 * @since 2020-11-21
 */
public class EnhancedIteratorTest {

	public static EnhancedIterator<String> getDefaultIter() {
		return new EnhancedIterator<>(new IteratorToSupplier<>(Arrays.asList(
			"A",
			"B",
			"C",
			"D",
			"E"
		)));
	}


	@Test
	public void iterate() {
		EnhancedIterator<String> iter = getDefaultIter();

		Assert.assertEquals(0, iter.nextIndex());
		Assert.assertTrue(iter.hasNext());
		Assert.assertEquals(-1, iter.previousIndex());
		Assert.assertEquals("A", iter.peek());
		Assert.assertEquals("A", iter.next());

		Assert.assertEquals("B", iter.next());
		Assert.assertEquals(1, iter.previousIndex());
		Assert.assertEquals(2, iter.nextIndex());

		Assert.assertEquals("C", iter.next());
		Assert.assertEquals("D", iter.next());
		Assert.assertEquals(3, iter.previousIndex());
		Assert.assertEquals(4, iter.nextIndex());

		Assert.assertEquals("E", iter.next());
		Assert.assertFalse(iter.hasNext());
	}


	@Test
	public void invalidOperations() {
		EnhancedIterator<String> iter = getDefaultIter();

		Assert.assertEquals("A", iter.next());
		CheckTask.assertException(() -> iter.remove());
		while(iter.hasNext()) iter.next();
		CheckTask.assertException(() -> iter.next());
	}


	@Test
	public void closable() {
		AtomicReference<String> state = new AtomicReference<>("open");
		EnhancedIterator<String> iter = new EnhancedIterator<>(new IteratorToSupplier<>(Arrays.asList("A", "B")), () -> state.set("closed"));

		Exception iterEx = new IllegalStateException("no good");
		EnhancedIterator<String> iterThrows = new EnhancedIterator<>(new IteratorToSupplier<>(Arrays.asList("A", "B")), () -> { throw iterEx; });

		iter.next();
		try {
			iter.close();
			iterThrows.close();
		} catch(Exception ex) {
			Assert.assertSame(iterEx, ex); // only the second iterator throws on close and that error should match the one thrown
		}
		Assert.assertEquals("closed", state.get());

	}

}
