package streamUtils;

import java.util.Iterator;
import java.util.function.Supplier;

/** A wrapper to convert an {@link Iterator} to a {@link Supplier}.
 * An option default value can be specified which is returned by {@link #get()}
 * once the end of the iterator is reached
 * @param <T> the data type of the input {@link Iterator} and the data type
 * of this {@link Supplier} class
 * 
 * @author TeamworkGuy2
 * @since 2015-1-24
 */
public class IteratorToSupplier<T> implements Supplier<T> {
	private Iterator<T> iter;
	private boolean ended = false;
	private T endValue;


	public IteratorToSupplier(Iterable<T> iterable) {
		this(iterable.iterator(), null);
	}


	/** Create an {@link Iterable} to {@link Supplier}
	 * @param iterable call {@link Iterable#iterator()} and use the resulting iterator as the source for this {@link Supplier}
	 * @param endValue the value to return when {@link #get()} is called and there is no underlying value to return
	 */
	public IteratorToSupplier(Iterable<T> iterable, T endValue) {
		this(iterable.iterator(), endValue);
	}


	/** Create an {@link Iterator} to {@link Supplier} that returns a default value of {@code null}
	 * when the {@code iter} completes
	 * @param iter the source iterator
	 */
	public IteratorToSupplier(Iterator<T> iter) {
		this(iter, null);
	}


	/** Create an {@link Iterator} to {@link Supplier}
	 * @param iter the iterator to us as this {@link Supplier}'s source
	 * @param endValue the value to return when {@link #get()} is called and there is no underlying value to return
	 */
	public IteratorToSupplier(Iterator<T> iter, T endValue) {
		this.iter = iter;
		this.endValue = endValue;
	}


	@Override
	public T get() {
		if(!iter.hasNext()) {
			ended = true;
		}
		if(ended) {
			return endValue;
		}
		return iter.next();
	}

}
