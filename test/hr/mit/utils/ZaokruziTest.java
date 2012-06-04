package hr.mit.utils;

import static org.junit.Assert.assertEquals;

import hr.mit.windows.LoginWindow;

import java.math.BigDecimal;

import org.junit.Test;

public class ZaokruziTest {

	@Test
	public void testZaokruzi() {
		BigDecimal iznos = new BigDecimal(9.9);
		BigDecimal r = new BigDecimal(1);
		assertEquals(new BigDecimal(10), CijenaKarte.zaokruzi(iznos, r));
		iznos = new BigDecimal(10);
		assertEquals(new BigDecimal(10), CijenaKarte.zaokruzi(iznos, r));
		iznos = new BigDecimal(10.1);
		assertEquals(new BigDecimal(11), CijenaKarte.zaokruzi(iznos, r));
		iznos = new BigDecimal(10.1);
		r = new BigDecimal(0.5);		
		assertEquals(new BigDecimal(10.5), CijenaKarte.zaokruzi(iznos, r));
		iznos = new BigDecimal(0.0);
		r = new BigDecimal(0.5);
		assertEquals(new BigDecimal(0.5), CijenaKarte.zaokruzi(iznos, r));
		iznos = new BigDecimal(0.0001);
		r = new BigDecimal(0.5);
		assertEquals(new BigDecimal(0.5), CijenaKarte.zaokruzi(iznos, r));

	}

}
