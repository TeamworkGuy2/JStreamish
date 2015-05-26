package streamUtils;

import java.io.BufferedReader;
import java.util.function.Supplier;

/** A {@link Supplier} that iterates through the lines in a string.<br>
 * Lines are separated by {@code '\n'}, {@code '\r'}, or {@code "\r\n"},
 * similar to the behavior of {@link BufferedReader}.<br>
 * This class is basically a shortcut for:
 * <pre>new BufferedReader(new StringReader(str)).lines().</pre><br>
 * Note: if the last character of the input string is a newline, the empty string between
 * the last character and the end of the string is not converted to a line.<br>
 * For example, the string {@code "a\nb\nc\n"} is only 3 lines, whereas {@code "a\nb\nc\nd"} is 4 lines
 * even though both strings have the same number of newlines.<br>
 * @author TeamworkGuy2
 * @since 2015-1-31
 */
public class StringLineSupplier implements Supplier<String> {
	private String src;
	private StringBuilder tmpBuf = new StringBuilder(32);
	private int off;
	private int max;
	private boolean treatEmptyLineAsLine;
	private boolean treatEolNewlineAsTwoLines;


	public StringLineSupplier(String str) {
		this(str, 0, str.length(), true, false);
	}


	/**
	 * @param str
	 * @param off
	 * @param len
	 * @param treatEmptyLineAsLine true if empty lines, such as {@code ""} should be considered 1 line,
	 * false if no lines should be returned
	 * @param treatEolNewlineAsTwoLines true if lines ending with a newline, such as {@code "...\n"} should be considered 2 lines,
	 * false if it should only be treated as 1 line
	 */
	public StringLineSupplier(String str, int off, int len, boolean treatEmptyLineAsLine, boolean treatEolNewlineAsTwoLines) {
		this.src = str;
		this.off = off;
		this.max = off + len;
		this.treatEmptyLineAsLine = treatEmptyLineAsLine;
		this.treatEolNewlineAsTwoLines = treatEolNewlineAsTwoLines;
	}


	@Override
	public String get() {
		String str = src;
		int offI = off;
		int maxI = max;
		StringBuilder buf = tmpBuf;
		buf.setLength(0);

		if(offI >= maxI) {
			// handle empty lines
			if(this.treatEmptyLineAsLine && offI == 0 && maxI == 0) {
				this.off = maxI + 1;
				return "";
			}
			// handle lines ending with "...\n", such a string would be considered 2 lines
			if(this.treatEolNewlineAsTwoLines && maxI > 0 && offI == maxI) {
				this.off = maxI + 1;
				char ch = str.charAt(offI - 1);
				if(ch == '\n' || ch == '\r') {
					return "";
				}
			}
			this.tmpBuf = null;
			return null;
		}

		for(; offI < maxI; offI++) {
			char ch = str.charAt(offI);
			if(ch == '\n' || ch == '\r') {
				if(ch == '\r' && offI + 1 < maxI && str.charAt(offI + 1) == '\n') {
					offI++;
				}
				offI++;
				break;
			}
			buf.append(ch);
		}

		String line = buf.toString();
		this.off = offI;

		return line;
	}


}
