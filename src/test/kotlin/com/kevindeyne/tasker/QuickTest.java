package com.kevindeyne.tasker;

import org.apache.commons.lang.StringEscapeUtils;
import org.junit.Assert;
import org.junit.Test;

public class QuickTest {

	@Test
	public void test() {
		System.out.println(5%7 == 0);
		System.out.println(7%7 == 0);
		System.out.println(10%7 == 0);
		System.out.println(14%7 == 0);
	}

	@Test
	public void encodingTest() {
		String actual = "Kevin&Co";
		actual = StringEscapeUtils.escapeHtml(actual);
		Assert.assertEquals("Kevin&amp;Co", actual);
	}

}
