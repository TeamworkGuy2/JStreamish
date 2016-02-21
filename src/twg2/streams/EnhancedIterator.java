package twg2.streams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.NoSuchElementException;
import java.util.function.Function;
import java.util.function.Supplier;

/** Converter from a {@link Supplier} to a {@link PeekableIterator Peekable}, {@link ClosableIterator}.
 * The iterator returns each elemtn read from the {@code Supplier} until
 * {@link Supplier#get()} returns null.
 * @author TeamworkGuy2
 * @since 2015-1-31
 */
public class EnhancedIterator<T> implements ClosableIterator<T>, PeekableIterator<T> {
	private Supplier<T> source;
	private AutoCloseable sourceToClose;
	private T currentElem;
	private T nextElem;
	// package-private
	int currentIndex = -1;


	public EnhancedIterator(Supplier<T> source) {
		this(source, null);
	}


	/** Create an enhanced iterator from a supplier and closable source
	 * @param source the source to read input from, null marks the end of the stream
	 * @param sourceToClose the source to close when {@link #close()} is called
	 */
	public EnhancedIterator(Supplier<T> source, AutoCloseable sourceToClose) {
		this.source = source;
		this.sourceToClose = sourceToClose != null ? sourceToClose : (source instanceof AutoCloseable ? (AutoCloseable)source : null);
		this.nextElem = source.get();
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
		nextElem = source.get();
		currentIndex++;
		return currentElem;
	}


	@Override
	public void close() throws Exception {
		if(sourceToClose != null) {
			sourceToClose.close();
		}
	}


	/**
	 * @return the index of the last call to {@link #next()}, (i.e. after each {@code next()} call, {@code previousIndex()} returns indices forming the sequence -1, 0, 1, 2, ...)
	 */
	public int previousIndex() {
		return currentIndex;
	}


	/**
	 * @return the index of the next call to {@link #next()} (note: the next value may not exist, see {@link #hasNext()} to check),
	 * (i.e. after each {@code next()} call, {@code nextIndex()} returns indices forming the sequence 1, 2, 3, 4, ...)
	 */
	public int nextIndex() {
		return currentIndex + 1;
	}


	/** Create an {@code EnhancedIterator} from a {@link BufferedReader}
	 * @param reader
	 * @param includeEolNewlines true to include newlines at the end of each line
	 * @param modifier an optional function which transforms each line of text before it is returned
	 * @return an {@link EnhancedIterator} that iterates over the lines in the {@link BufferedReader} {@code reader}
	 */
	public static final EnhancedIterator<String> fromReader(BufferedReader reader, boolean includeEolNewlines, Function<String, String> modifier) {
		EnhancedIterator<String> iter = new EnhancedIterator<String>(() -> {
			String nextLine;
			try {
				nextLine = reader.readLine();
				if(includeEolNewlines && nextLine != null) {
					nextLine = nextLine + '\n';
				}
			} catch (IOException e) {
				throw new UncheckedIOException(e);
			}
			if(modifier != null) {
				nextLine = modifier.apply(nextLine);
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
	 * @param includeEolNewlines true to include newlines at the end of each line
	 * @param modifier an optional function which transforms each line of text before it is returned
	 * @return an {@link EnhancedIterator} that iterates over the lines from the file represented by {@link Path} {@code file}
	 * @throws IOException
	 * @throws MalformedURLException
	 */
	public static final EnhancedIterator<String> fromPath(Path file, Charset cs, boolean includeEolNewlines, Function<String, String> modifier) throws IOException {
		return EnhancedIterator.fromUrl(file.toUri().toURL(), cs, includeEolNewlines, modifier);
	}


	/** Create an {@code EnhancedIterator} from a URL source
	 * @param src
	 * @param cs
	 * @param includeEolNewlines true to include newlines at the end of each line
	 * @param modifier an optional function which transforms each line of text before it is returned
	 * @return an {@link EnhancedIterator} that iterates over the lines from the content of the {@link URL} {@code src}
	 * @throws IOException
	 */
	public static final EnhancedIterator<String> fromUrl(URL src, Charset cs, boolean includeEolNewlines, Function<String, String> modifier) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(src.openConnection().getInputStream(), cs));
		return EnhancedIterator.fromReader(reader, includeEolNewlines, modifier);
	}

}
