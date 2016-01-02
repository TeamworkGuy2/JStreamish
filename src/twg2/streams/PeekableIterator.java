package twg2.streams;

import java.util.Iterator;

/** An {@link Iterator} that allows the next element to be 'peeked' and retrieved without reading it
 * @param <T> the data type of this iterator
 * @author TeamworkGuy2
 * @since 2015-1-31
 * @see EnhancedIterator
 * @see IteratorToSupplier
 */
public interface PeekableIterator<T> extends Iterator<T>, Peekable<T> {

	/** Peek at the next element in this iterator
	 * @return the next element in the stream without reading it,
	 * null if {@link #hasNext()} is false
	 */
	@Override
	public T peek();

}
