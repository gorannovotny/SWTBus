package hr.mit.utils;

import static org.junit.Assert.*;

import org.junit.Test;

public class DbUtilTest {

	@Test
	public void testGetHHMM() {
		assertEquals("12:00", DbUtil.getHHMM(0.5));
		assertEquals("00:00", DbUtil.getHHMM(0.0));
		assertEquals("24:00", DbUtil.getHHMM(1.0));
	}

}
