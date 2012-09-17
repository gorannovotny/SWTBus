package hr.mit.beans;

import hr.mit.utils.CijenaKarte;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

public class Stavka {

	private static ArrayList<Stavka> stavkaList = new ArrayList<Stavka>();
	private static ArrayList<Stavka> oldStavkaList;

	private Postaja odPostaje;
	private Postaja doPostaje;
	private Karta karta;
	private Popust popust;
	private ProdajnoMjesto prodajnoMjesto;
	private String brojKarte;
	private CijenaKarte cijenaKarte;
	private Stupac stupac;
	private BigDecimal cijenaZamjenske;
	private Boolean jeZamjenska;

	public Boolean getJeZamjenska() {
		return jeZamjenska;
	}

	public void setJeZamjenska(Boolean jeZamjenska) {
		this.jeZamjenska = jeZamjenska;
	}

	public static void clear() {
		stavkaList.clear();
	}

	public static void saveList() {
		oldStavkaList = new ArrayList<Stavka>(stavkaList);
	}

	public static List<Stavka> getStorno(List<Stavka> sl) {
		for (Stavka s : sl) {
			s.setCijena(s.getProdajnaCijena().negate());
		}
		return sl;
	}

	public static void add(Stavka stavka) {
		stavkaList.add(stavka);
	}

	public static List<Stavka> getList() {
		return stavkaList;
	}

	public static List<Stavka> getOldList() {
		return oldStavkaList;
	}

	public static String getOldDescription() {
		StringBuffer sb = new StringBuffer("");
		if (oldStavkaList != null && oldStavkaList.size() != 0) {
			sb.append(oldStavkaList.get(0).getRelacija());
			sb.append("\r");
			sb.append("----------------------------------------");
			sb.append("\r");
			for (Stavka s : oldStavkaList) {
				sb.append(s.getDesc());
				sb.append("\r");
			}
		}
		return sb.toString();
	}

	public static String getDescription() {
		StringBuffer sb = new StringBuffer("");
		if (stavkaList.size() != 0) {
			sb.append(stavkaList.get(0).getRelacija());
			sb.append("\r");
			sb.append("----------------------------------------");
			sb.append("\r");
			for (Stavka s : stavkaList) {
				sb.append(s.getDesc());
				sb.append("\r");
			}
		}
		return sb.toString();
	}

	public static Integer getCount() {
		return stavkaList.size();
	}

	public static BigDecimal getUkupno() {
		BigDecimal retval = BigDecimal.ZERO;
		for (Stavka s : stavkaList) {
			if (!s.getJeZamjenska())
				retval = retval.add(s.getProdajnaCijena());
		}
		return retval.setScale(2);
	}

	public Stavka(Stupac stupac, Postaja odPostaje, Postaja doPostaje, Karta karta, Popust popust) {
		this.stupac = stupac;
		this.odPostaje = odPostaje;
		this.doPostaje = doPostaje;
		this.karta = karta;
		this.popust = popust;
		this.brojKarte = "";
		this.jeZamjenska = false;
		this.cijenaKarte = new CijenaKarte(stupac, karta, odPostaje, doPostaje);
	}

	public String getDesc() {
		String k = karta.getNaziv();
		if (jeZamjenska)
			k = "(Z)" + k;
		if (k.length() > 19)
			k = k.substring(0, 19);
		String out;
		out = String.format("%-19s %3.0f%% %7.2f", k, popust.getPopust().doubleValue(), getProdajnaCijena().doubleValue());
		return out;
	}

	public String getRelacija() {
		return odPostaje.getNaziv() + " - " + doPostaje.getNaziv();
	}

	public String getRelacijaKontra() {
		return doPostaje.getNaziv() + " - " + odPostaje.getNaziv();
	}

	public BigDecimal getIznosPDV() {
		// Prodajna cijena * 0.2 ( Za PDV 25%)
		// Treba dodati odnos ino i domaćih kilometara
		return getProdajnaCijena().multiply(new BigDecimal(0.2));
	}

	public BigDecimal getProdajnaCijena() {
		BigDecimal retval;
		if (jeZamjenska)
			retval = cijenaZamjenske.setScale(2);
		else {
			if (popust.getId() != 0)
				retval = cijenaKarte.getCijena().multiply(BigDecimal.ONE.subtract(popust.getPopust().movePointLeft(2))).setScale(2);
			else
				retval = cijenaKarte.getCijena().multiply(BigDecimal.ONE.subtract(getKarta().getPopustProcent().movePointLeft(2)));
		}
		return CijenaKarte.zaokruzi(retval, this.getKarta().getRoundN());
	}

	public BigDecimal getNettoCijena() {
		return getProdajnaCijena().subtract(getIznosPDV());

	}

	public Postaja getOdPostaje() {
		return odPostaje;
	}

	public void setOdPostaje(Postaja odPostaje) {
		this.odPostaje = odPostaje;
		cijenaKarte = new CijenaKarte(stupac, karta, odPostaje, doPostaje);
	}

	public Postaja getDoPostaje() {
		return doPostaje;
	}

	public void setDoPostaje(Postaja doPostaje) {
		this.doPostaje = doPostaje;
		cijenaKarte = new CijenaKarte(stupac, karta, odPostaje, doPostaje);
	}

	public Karta getKarta() {
		return karta;
	}

	public void setKarta(Karta karta) {
		this.karta = karta;
		cijenaKarte = new CijenaKarte(stupac, karta, odPostaje, doPostaje);
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
		this.cijenaZamjenske = cijena;
	}

	public void setCijena(String s) {
		try {
			NumberFormat nf = NumberFormat.getNumberInstance();
			DecimalFormat df = (DecimalFormat) nf;
			df.parseObject(s);
			setCijena(new BigDecimal(s).setScale(2));
		} catch (Exception e) {
			setCijena(BigDecimal.ZERO.setScale(2));
		}
	}

	public Integer getKmLinije() {
		return cijenaKarte.getDistancaLinije() / 1000;
	}

	public Integer getKmDomaci() {
		return cijenaKarte.getDomDistanca() / 1000;
	}

	public Integer getKmIno() {
		return cijenaKarte.getInoDistanca() / 1000;
	}

}
