package hr.mit.beans;

import hr.mit.utils.CijenaKarte;

import java.math.BigDecimal;
import java.util.ArrayList;

public class Stavka {
	
	private static ArrayList<Stavka> stavkaList = new ArrayList<Stavka>();

	private Postaja odPostaje;
	private Postaja doPostaje;
	private Karta karta;
	private Popust popust;
	private ProdajnoMjesto prodajnoMjesto;
	private String brojKarte;
	private BigDecimal cijena = BigDecimal.ZERO;

	private Stupac stupac;
	
	public Stavka(Stupac stupac,Postaja odPostaje, Postaja doPostaje, Karta karta, Popust popust) {
		this.odPostaje = odPostaje;
		this.doPostaje = doPostaje;
		this.karta = karta;
		this.popust = popust;
		CijenaKarte c = new CijenaKarte(stupac, karta, odPostaje, doPostaje);
		cijena = c.getUkupnaCijena();
		cijena = cijena.subtract(cijena.multiply(popust.getPopust().movePointLeft(2)));
	}

	public String getDescription() {
		String k = karta.getNaziv();
		if (k.length()>25) k = k.substring(0, 25);
		
		String out = String.format("%-25s %15.2f%% %8.2f",k,popust.getPopust().doubleValue(),getCijena().doubleValue());
		return out;
	}
	
	public String getRelacija() {
		return odPostaje.getNaziv() + " - " + doPostaje.getNaziv();
	}

	public BigDecimal getCijena() {
		return cijena;
	}
	
}
