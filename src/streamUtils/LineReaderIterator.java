package streamUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Iterator;
import java.util.NoSuchElementException;

/** A wrapper to convert a {@link BufferedReader} to an {@link Iterator}.
 * The iterator returns each line read from the {@code BufferedReader} until
 * {@link BufferedReader#readLine()} returns null.
 * @author TeamworkGuy2
 * @since 2015-1-22
 */
public class LineReaderIterator implements ClosableIterator<String> {
	private BufferedReader reader;
	private String curLine;
	private String nextLine;


	public LineReaderIterator(BufferedReader reader) {
		this.reader = reader;
		nextLine();
	}


	private final void nextLine() {
		try {
			nextLine = reader.readLine();
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
	}


	@Override
	public boolean hasNext() {
		return nextLine != null;
	}


	@Override
	public String next() {
		curLine = nextLine;
		if(curLine == null) {
			throw new NoSuchElementException();
		}
		nextLine();
		return curLine;
	}


	@Override
	public void close() throws Exception {
		reader.close();
	}

}