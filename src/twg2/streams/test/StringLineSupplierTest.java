package twg2.streams.test;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import twg2.streams.EnhancedIterator;
import twg2.streams.StringLineSupplier;

/**
 * @author TeamworkGuy2
 * @since 2015-5-26
 */
public class StringLineSupplierTest {

	@Test
	public void testStringLineSupplier() throws Exception {
		String[] inputStrs = new String[] {
				"-a\n-b\n-c",
				"\r\nnew\r\nvar\r\n",
				"\n123",
				"",
				"\n\n"
		};
		String[][] expected = new String[][] {
				{
					"-a", "-b", "-c"
				}, {
					"", "new", "var"
				}, {
					"", "123"
				}, {
					""
				}, {
					"", ""
				}
		};
		String[][] expectedEolNewLine = new String[][] {
				{
					"-a", "-b", "-c"
				}, {
					"", "new", "var", ""
				}, {
					"", "123"
				}, {
					""
				}, {
					"", "", ""
				}
		};
		String[][] expectedEolNewLineWithNewLine = new String[][] {
				{
					"-a\n", "-b\n", "-c"
				}, {
					"\n", "new\n", "var\n", ""
				}, {
					"\n", "123"
				}, {
					""
				}, {
					"\n", "\n", ""
				}
		};

		int i = 0;

		EnhancedIterator<String> supplier = null;
		for(String strA : inputStrs) {
			supplier = createLineIter(strA, true, false, false);
			String[] linesAry = supplierLines(supplier);

			Assert.assertArrayEquals(i + "", expected[i], linesAry);

			supplier = createLineIter(strA, true, true, false);
			String[] linesAry2 = supplierLines(supplier);

			Assert.assertArrayEquals(i + "", expectedEolNewLine[i], linesAry2);

			supplier = createLineIter(strA, true, true, true);
			String[] linesAry3 = supplierLines(supplier);

			Assert.assertArrayEquals(i + "", expectedEolNewLineWithNewLine[i], linesAry3);

			i++;
		}

		if(supplier != null) {
			supplier.close();
		}
	}


	private static final EnhancedIterator<String> createLineIter(String str, boolean treatEmptyLineAsLine, boolean treatEolNewlineAsTwoLines, boolean includeNewlinesAtEndOfReturnedLines) {
		StringLineSupplier strs = new StringLineSupplier(str, 0, str.length(), treatEmptyLineAsLine, treatEolNewlineAsTwoLines, includeNewlinesAtEndOfReturnedLines, true);
		return new EnhancedIterator<>(strs);
	}


	private static final String[] supplierLines(EnhancedIterator<String> supplier) {
		List<String> lines = new ArrayList<>();
		supplier.forEachRemaining((ln) -> lines.add(ln));
		String[] lineAry = lines.toArray(new String[lines.size()]);
		return lineAry;
	}

}
