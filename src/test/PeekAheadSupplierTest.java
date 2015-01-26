package test;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import streamUtils.LineReaderIterator;
import streamUtils.IterableLimited;
import streamUtils.StreamUtil;

/**
 * @author TeamworkGuy2
 * @since 2015-1-24
 */
public class PeekAheadSupplierTest {


	@Test
	public void defaultPeekAheadStreamTest() {
		List<String> lines = Arrays.asList("A1", "B22", "", "C333");
		Iterable<String> st = new IterableLimited<>(StreamUtil.asStream(new LineReaderIterator(new BufferedReader(new StringReader(join("\n", lines))))).iterator());

		int i = 0;
		for(String line : st) {
			Assert.assertEquals(lines.get(i), line);
			i++;
		}
	}


	public static String join(String delim, List<String> args) {
		StringBuilder strB = new StringBuilder();
		if(args != null && args.size() > 0) {
			for(int i = 0, size = args.size() - 1; i < size; i++) {
				strB.append(args.get(i));
				strB.append(delim);
			}
			strB.append(args.get(args.size() - 1));
		}
		return strB.toString();
	}

}
