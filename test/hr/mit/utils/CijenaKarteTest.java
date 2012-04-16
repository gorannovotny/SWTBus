package hr.mit.utils;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.sql.SQLException;

import org.junit.BeforeClass;
import org.junit.Test;

public class CijenaKarteTest {
	static CijenaKarte karta;
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
//		karta = new CijenaKarte(459,24783, 1, 17);
	}

	@Test
	public void testGetDistancaLinije() throws SQLException{
	}

	@Test
	public void testGetDistancaRelacije() throws SQLException {
		assertEquals(new Integer(23000),karta.getDistancaRelacije());
	}

	@Test
	public void testGetDomDistanca() throws SQLException {
		assertEquals(new Integer(23000),karta.getDomDistanca());
	}

	@Test
	public void testGetInoDistanca() throws SQLException {
		assertEquals(new Integer(0),karta.getInoDistanca());
	}

	@Test
	public void testGetDistancaCenika() throws SQLException {
		assertEquals(new Integer(23000),karta.getDistancaCenika());
	}

	@Test
	public void testGetCijena() throws SQLException {
		assertEquals(new BigDecimal(20.0),karta.getCijena());

	}

}
