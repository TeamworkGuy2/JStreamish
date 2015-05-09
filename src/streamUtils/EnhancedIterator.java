package streamUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.NoSuchElementException;
import java.util.function.Supplier;

/** Converter from a {@link Supplier} to an {@link PeekableIterator Peekable}, {@link ClosableIterator}.
 * The iterator returns each line read from the {@code Supplier} until
 * {@link Supplier#get()} returns null.
 * @author TeamworkGuy2
 * @since 2015-1-31
 */
public class EnhancedIterator<T> implements ClosableIterator<T>, PeekableIterator<T> {
	private Supplier<T> source;
	private AutoCloseable sourceToClose;
	private T currentElem;
	private T nextElem;


	/** Create an enhanced iterator from a supplier and closable source
	 * @param source the source to read input from, null marks the end of the stream
	 * @param sourceToClose the source to close when {@link #close()} is called
	 */
	public EnhancedIterator(Supplier<T> source, AutoCloseable sourceToClose) {
		this.source = source;
		this.sourceToClose = sourceToClose;
		nextLine();
	}


	private final void nextLine() {
		nextElem = source.get();
	}


	@Override
	public boolean hasNext() {
		return nextElem != null;
	}


	@Override
	public T peek() {
		return nextElem;
	}


	@Override
	public T next() {
		currentElem = nextElem;
		if(currentElem == null) {
			throw new NoSuchElementException();
		}
		nextLine();
		return currentElem;
	}


	@Override
	public void close() throws Exception {
		if(sourceToClose != null) {
			sourceToClose.close();
		}
	}


	/** Create an {@code EnhancedIterator} from a {@link BufferedReader}
	 * @param reader
	 * @return an {@link EnhancedIterator} that iterates over the lines in the {@link BufferedReader} {@code reader}
	 */
	public static final EnhancedIterator<String> fromReader(BufferedReader reader) {
		EnhancedIterator<String> iter = new EnhancedIterator<String>(() -> {
			String nextLine;
			try {
				nextLine = reader.readLine();
			} catch (IOException e) {
				throw new UncheckedIOException(e);
			}
			return nextLine;
		}, () -> {
			try {
				reader.close();
			} catch (IOException e) {
				// do nothing
			}
		});

		return iter;
	}


	/** Create an {@code EnhancedIterator} from a specific {@link Path}.
	 * This is equivalent to {@code EnhancedIterator.fromUrl(file.toUri().toURL(), cs)}
	 * @param file
	 * @param cs
	 * @return an {@link EnhancedIterator} that iterates over the lines from the file represented by {@link Path} {@code file}
	 * @throws IOException 
	 * @throws MalformedURLException
	 */
	public static final EnhancedIterator<String> fromPath(Path file, Charset cs) throws IOException {
		return EnhancedIterator.fromUrl(file.toUri().toURL(), cs);
	}


	/** Create an {@code EnhancedIterator} from a URL source
	 * @param src
	 * @param cs
	 * @return an {@link EnhancedIterator} that iterates over the lines from the content of the {@link URL} {@code src}
	 * @throws IOException 
	 */
	public static final EnhancedIterator<String> fromUrl(URL src, Charset cs) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(src.openConnection().getInputStream(), cs));
		return EnhancedIterator.fromReader(reader);
	}

}
