package twg2.streams;

/** A data source that can be 'peeked' to view available data
 * @param <T> the data type of the peek-able data
 * @author TeamworkGuy2
 * @since 2015-8-1
 */
@FunctionalInterface
public interface Peekable<T> {

	/**
	 * @return the peek-able data if available, null if there is no data to peek at
	 */
	public T peek();

}
