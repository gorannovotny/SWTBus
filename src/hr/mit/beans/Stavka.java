package hr.mit.beans;

import hr.mit.utils.CijenaKarte;

import java.math.BigDecimal;

public class Stavka {
	private Postaja odPostaje;
	private Postaja doPostaje;
	private Karta karta;
	private Integer popust;
	private BigDecimal cijena = BigDecimal.ZERO;

	public Stavka(Stupac stupac,Postaja odPostaje, Postaja doPostaje, Karta karta, Integer popust) {
		this.odPostaje = odPostaje;
		this.doPostaje = doPostaje;
		this.karta = karta;
		this.popust = popust;
		CijenaKarte c = new CijenaKarte(stupac, karta, odPostaje, doPostaje);
		cijena = c.getUkupnaCijena();
	}

	public String getDescription() {
		String oddo = odPostaje.getNaziv() + "-" + doPostaje.getNaziv();
		if (oddo.length()>35) oddo = oddo.substring(0, 35);
		String k = karta.getNaziv();
		if (k.length()>15) k = k.substring(0, 15);
		String out = String.format("%-35s:%-15s %5.2f",oddo,k,getCijena().doubleValue());
		return out;
	}

	public BigDecimal getCijena() {
		return cijena;
	}
	
}
