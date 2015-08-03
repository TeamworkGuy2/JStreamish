package streamUtils;

import java.util.Iterator;

/** An {@link Iterable} that only allows its {@link #iterator()} method to be called a specific number of times,
 * after which null is returned instead of a new iterator
 * @param <T> the data type of the iterable
 * 
 * @author TeamworkGuy2
 * @since 2015-1-24
 */
public class IterableLimited<T> implements Iterable<T> {
	private Iterator<T> iter;
	private int limit;


	/** Create an iterable that can only be called once
	 * @param iter the iterable source to use
	 */
	public IterableLimited(Iterator<T> iter) {
		this(iter, 1);
	}


	/** Create an iterable that only be called a specific number of times
	 * @param iter the iterable source to use
	 * @param limit the number of times {@link #iterator()} will return a new iterator before it begins returning null
	 */
	public IterableLimited(Iterator<T> iter, int limit) {
		this.iter = iter;
		this.limit = limit;
	}


	@Override
	public Iterator<T> iterator() {
		Iterator<T> it = iter;
		if(limit <= 0) {
			iter = null;
		}
		limit--;
		return it;
	}

}
