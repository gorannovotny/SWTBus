package hr.mit.beans;

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

		//		cijena =  
	}

	public String getDescription() {
		return odPostaje.getNaziv() + " " + doPostaje.getNaziv();
	}

	public BigDecimal getCijena() {
		return cijena;
	}
	
}
