package streamUtils;

import java.util.Iterator;

/** An {@link AutoCloseable} {@link Iterator}
 * @param <T> the data type of this {@code Iterator}
 * 
 * @author TeamworkGuy2
 * @since 2015-1-22
 */
public interface ClosableIterator<T> extends Iterator<T>, AutoCloseable {

}
