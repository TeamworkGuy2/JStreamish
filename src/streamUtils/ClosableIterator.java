package streamUtils;

import java.util.Iterator;
import java.util.function.Supplier;

/** A {@link AutoCloseable} {@link Iterator}
 * @param <T> the data type of this {@link Supplier}
 * 
 * @author TeamworkGuy2
 * @since 2015-1-22
 */
public interface ClosableIterator<T> extends Iterator<T>, AutoCloseable {

}
