package twg2.streams;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.function.Supplier;

/** A {@link PeekableIterator Peekable}, {@link ListIterator}, and {@link ClosableIterator} all rolled into one.<br>
 * Useful for both iterating over the results of a stream/supplier while simultaneously storing the results in a list
 * The iterator returns each element read from the source {@code Supplier} until {@link Supplier#get()} returns null.
 * An internal list of results returned by {@link #next()} is also build and can be accessed via {@link #getIteratorResults()}
 * @author TeamworkGuy2
 * @since 2016-1-2
 */
public class EnhancedListBuilderIterator<T> implements ListIterator<T>, ClosableIterator<T>, PeekableIterator<T> {
	private EnhancedIterator<T> iter;
	private List<T> results;
	private int nextIndex = 0;
	private int mark = -2;


	/** Create a list builder iterator from a source supplier
	 * @param source the source to read input from, null marks the end of the stream
	 */
	public EnhancedListBuilderIterator(Supplier<T> source) {
		this(new EnhancedIterator<>(source));
	}


	/** Create a list builder iterator from a supplier and closable source
	 * @param source the source to read input from, null marks the end of the stream
	 * @param sourceToClose the source to close when {@link #close()} is called
	 */
	public EnhancedListBuilderIterator(Supplier<T> source, AutoCloseable sourceToClose) {
		this(new EnhancedIterator<>(source, sourceToClose));
	}



	/** Create a list builder iterator from existing enhanced iterator
	 * @param iterator the source to read input from, null marks the end of the stream
	 */
	public EnhancedListBuilderIterator(EnhancedIterator<T> iterator) {
		this.iter = iterator;
		this.results = new ArrayList<>();
	}


	@Override
	public boolean hasNext() {
		if(nextIndex < results.size()) {
			return true;
		}
		return iter.hasNext();
	}


	@Override
	public T peek() {
		if(nextIndex < results.size()) {
			return results.get(nextIndex);
		}
		return iter.peek();
	}


	@Override
	public T next() {
		if(nextIndex < results.size()) {
			return results.get(nextIndex++);
		}
		T elem = iter.next();
		if(elem != null) {
			nextIndex++;
			results.add(elem);
		}
		return elem;
	}


	@Override
	public void close() throws Exception {
		iter.close();
	}


	@Override
	public boolean hasPrevious() {
		return nextIndex > 0;
	}


	@Override
	public T previous() {
		if(nextIndex == 0) {
			throw new IndexOutOfBoundsException("-1 of [0, unknown)");
		}
		nextIndex--;
		return results.get(nextIndex);
	}


	@Override
	public void remove() {
		throw new UnsupportedOperationException("EnhancedListBuilderIteratorTest.remove()");
	}


	@Override
	public void set(T e) {
		throw new UnsupportedOperationException("EnhancedListBuilderIteratorTest.set()");
	}


	@Override
	public void add(T e) {
		throw new UnsupportedOperationException("EnhancedListBuilderIteratorTest.add()");
	}


	/**
	 * @return the index of the last call to {@link #next()}, (i.e. after each {@code next()} call, {@code previousIndex()} returns indices forming the sequence -1, 0, 1, 2, ...)
	 */
	@Override
	public int previousIndex() {
		return nextIndex - 1;
	}


	/**
	 * @return the index of the next call to {@link #next()} (note: the next value may not exist, see {@link #hasNext()} to check),
	 * (i.e. after each {@code next()} call, {@code nextIndex()} returns indices forming the sequence 1, 2, 3, 4, ...)
	 */
	@Override
	public int nextIndex() {
		return nextIndex;
	}


	public T peekPrevious() {
		if(nextIndex > 0) {
			return results.get(nextIndex - 1);
		}
		throw new IndexOutOfBoundsException((nextIndex - 1) + " of [0, unknown)");
	}


	public int mark() {
		return mark = nextIndex;
	}


	public void reset() {
		if(mark < 0) { throw new IllegalStateException("iterator not yet marked"); }
		reset(this.mark);
	}

	public void reset(int mark) {
		if(mark < 0 || mark > results.size()) { throw new IndexOutOfBoundsException(mark + " of [0, " + results.size() + "]"); }
		nextIndex = mark;
	}


	/**
	 * @return NOTE: currently the returned collection is live, expect this to change in future.
	 * To be sure all elements are present, call this method once {@link #hasNext()} returns false.
	 */
	public List<T> getIteratorResults() {
		return results;
	}

}
