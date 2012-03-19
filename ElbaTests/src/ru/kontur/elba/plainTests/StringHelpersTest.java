package ru.kontur.elba.plainTests;

import org.junit.Assert;
import org.junit.Test;
import ru.kontur.elba.core.StringHelpers;

import java.util.Arrays;

public class StringHelpersTest {
	@Test
	public void testJoin() {
		String[] s = new String[]{"abc", "def"};
		Assert.assertEquals("abc,def", StringHelpers.join(",", Arrays.asList(s)));
	}
}
