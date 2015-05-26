package test;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import streamUtils.EnhancedIterator;
import streamUtils.StringLineSupplier;

/**
 * @author TeamworkGuy2
 * @since 2015-5-26
 */
public class StringLineSupplierTest {

	@Test
	public void testStringLineSupplier() throws Exception {
		String[] inputStrs = new String[] {
				"-a\n-b\n-c",
				"\nnew\nvar\n",
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

		int i = 0;

		EnhancedIterator<String> supplier = null;
		for(String strA : inputStrs) {
			supplier = new EnhancedIterator<>(new StringLineSupplier(strA));
			List<String> lines = new ArrayList<>();
			supplier.forEachRemaining((ln) -> lines.add(ln));
			String[] linesAry = lines.toArray(new String[lines.size()]);

			Assert.assertArrayEquals(i + "", expected[i], linesAry);

			supplier = new EnhancedIterator<>(new StringLineSupplier(strA, 0, strA.length(), true, true));
			List<String> lines2 = new ArrayList<>();
			supplier.forEachRemaining((ln) -> lines2.add(ln));
			String[] linesAry2 = lines2.toArray(new String[lines2.size()]);

			Assert.assertArrayEquals(i + "", expectedEolNewLine[i], linesAry2);

			i++;
		}

		if(supplier != null) {
			supplier.close();
		}
	}

}
