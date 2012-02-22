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
	
	public static void clear() {
		stavkaList.clear();
	}
	
	public static void add(Stavka stavka) {
		stavkaList.add(stavka);
	}
	
	public static String getDescription() {
		StringBuffer sb = new StringBuffer();
		sb.append(stavkaList.get(0).getRelacija());sb.append("\n");
		sb.append("----------------------------------------");sb.append("\n");
		for (Stavka s : stavkaList) {
			sb.append(s.getDesc());sb.append("\n");
		}
		return sb.toString();
	}
	public static Integer getCount() {
		return stavkaList.size();
	}
	
	public static BigDecimal getUkupno(){
		BigDecimal retval = BigDecimal.ZERO;
		for (Stavka s : stavkaList) {
			retval = retval.add(s.getCijena());
		}
		return retval;
	}
	
	public Stavka(Stupac stupac) {
		this.stupac = stupac;
	}
	
	public Stavka(Stupac stupac,Postaja odPostaje, Postaja doPostaje, Karta karta, Popust popust) {
		this.odPostaje = odPostaje;
		this.doPostaje = doPostaje;
		this.karta = karta;
		this.popust = popust;
		CijenaKarte c = new CijenaKarte(stupac, karta, odPostaje, doPostaje);
		cijena = c.getUkupnaCijena();
		cijena = cijena.subtract(cijena.multiply(popust.getPopust().movePointLeft(2)));
	}

	public String getDesc() {
		String k = karta.getNaziv();
		if (k.length()>25) k = k.substring(0, 25);
		
		String out = String.format("%-25s %5.2f%% %7.2f",k,popust.getPopust().doubleValue(),getCijena().doubleValue());
		return out;
	}
	
	public String getRelacija() {
		return odPostaje.getNaziv() + " - " + doPostaje.getNaziv();
	}

	public BigDecimal getCijena() {
		return cijena;
	}
	public Postaja getOdPostaje() {
		return odPostaje;
	}
	public void setOdPostaje(Postaja odPostaje) {
		this.odPostaje = odPostaje;
	}
	public Postaja getDoPostaje() {
		return doPostaje;
	}
	public void setDoPostaje(Postaja doPostaje) {
		this.doPostaje = doPostaje;
	}
	public Karta getKarta() {
		return karta;
	}
	public void setKarta(Karta karta) {
		this.karta = karta;
	}
	public Popust getPopust() {
		return popust;
	}
	public void setPopust(Popust popust) {
		this.popust = popust;
	}
	public ProdajnoMjesto getProdajnoMjesto() {
		return prodajnoMjesto;
	}
	public void setProdajnoMjesto(ProdajnoMjesto prodajnoMjesto) {
		this.prodajnoMjesto = prodajnoMjesto;
	}
	public String getBrojKarte() {
		return brojKarte;
	}
	public void setBrojKarte(String brojKarte) {
		this.brojKarte = brojKarte;
	}
	public Stupac getStupac() {
		return stupac;
	}
	public void setCijena(BigDecimal cijena) {
		this.cijena = cijena;
	}
	
}
