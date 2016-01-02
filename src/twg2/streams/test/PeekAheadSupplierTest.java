package twg2.streams.test;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import twg2.streams.EnhancedIterator;
import twg2.streams.IterableLimited;
import twg2.streams.StreamUtil;

/**
 * @author TeamworkGuy2
 * @since 2015-1-24
 */
public class PeekAheadSupplierTest {


	@Test
	public void defaultPeekAheadStreamTest() {
		List<String> lines = Arrays.asList("A1", "B22", "", "C333");
		{
			Iterable<String> st = new IterableLimited<>(StreamUtil.asStream(EnhancedIterator.fromReader(new BufferedReader(new StringReader(join("\n", lines))), false)).iterator());

			int i = 0;
			for(String line : st) {
				Assert.assertEquals(lines.get(i), line);
				i++;
			}
		}

		{
			Iterable<String> st = new IterableLimited<>(StreamUtil.asStream(EnhancedIterator.fromReader(new BufferedReader(new StringReader(join("\n", lines))), true)).iterator());

			int i = 0;
			for(String line : st) {
				Assert.assertEquals(lines.get(i) + '\n', line);
				i++;
			}
		}

	}


	public static String join(String delim, List<String> args) {
		StringBuilder sb = new StringBuilder();
		if(args != null && args.size() > 0) {
			for(int i = 0, size = args.size() - 1; i < size; i++) {
				sb.append(args.get(i));
				sb.append(delim);
			}
			sb.append(args.get(args.size() - 1));
		}
		return sb.toString();
	}

}
