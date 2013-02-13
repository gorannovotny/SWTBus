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
	private Boolean jeZamjenska = false;
	private Boolean jePrelazna;
	
	public Boolean getJePrelazna() {
		return jePrelazna;
	}

	public void setJePrelazna(Boolean prelazna) {
		this.jePrelazna = prelazna;
	}

	
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
		this.jePrelazna  = false;
		this.cijenaKarte = new CijenaKarte(stupac, karta, odPostaje, doPostaje);
	}

	public String getDesc() {
		String k = karta.getNaziv();
	/*	
		if (jeZamjenska)
			k = "(Z)" + k;
		if (jePrelazna)
			//k = "prijelaz-" + doPostaje.getNaziv();
		    k = k;
	*/	
		if (k.length() > 19)
			k = k.substring(0, 19);
		String out;
		out = String.format("%-19s %3.0f%% %7.2f", k, popust.getPopust().doubleValue(), getProdajnaCijena().doubleValue());
		return out;
	}

	public String getDodInfoRelacije() {
		String k = "";
		if (jeZamjenska)
			k =  k;
		if (jePrelazna)
		    k = "-" + doPostaje.getNaziv();
		if (k.length() > 19)
			k = k.substring(0, 19);
		return k;
	}

	public String getDodOpisKarte() {
		String k = "";
		if ((jeZamjenska)&&(jePrelazna))
			k =  "PRIJELAZNA ZAMJENSKA";
		else if (jeZamjenska)
			k =  "ZAMJENSKA KARTA";
		else if (jePrelazna)
		    k = "PRIJELAZNA KARTA";
		
		if (k.length() > 22)
			k = k.substring(0, 22);
		return k;
	}

	
	
	
	public String getRelacija() {
		return odPostaje.getNaziv() + "-" + doPostaje.getNaziv();
	}

	public String getRelacijaKontra() {
		return doPostaje.getNaziv() + " - " + odPostaje.getNaziv();
	}

	public BigDecimal getIznosPDV() {
		// Prodajna cijena * 0.2 ( Za PDV 25%)
		// Treba dodati odnos ino i domaćih kilometara
		return getProdajnaCijena().multiply(new BigDecimal(0.2));
	}

	public BigDecimal getPunaCijena() {
		BigDecimal retval;
		if (jeZamjenska)
			retval = cijenaZamjenske.setScale(2);
		else if (jePrelazna)
			retval = cijenaKarte.getCijena();
		else {
			retval = cijenaKarte.getCijena();
		}
		retval = CijenaKarte.zaokruzi(retval, this.getKarta().getRoundN()); //josip 27.12.2012
		return retval.setScale(2);
	}
	
	
	public BigDecimal getProdajnaCijena() {
		BigDecimal retval;
		if (jeZamjenska)
			retval = cijenaZamjenske.setScale(2);
		else if (jePrelazna)
			retval = cijenaKarte.getCijena();
		else {
			if (popust.getId() != 0)
				retval = cijenaKarte.getCijena().multiply(BigDecimal.ONE.subtract(popust.getPopust().movePointLeft(2))).setScale(2,BigDecimal.ROUND_CEILING);
			else
				retval = cijenaKarte.getCijena().multiply(BigDecimal.ONE.subtract(getKarta().getPopustProcent().movePointLeft(2)));
		}
		retval = CijenaKarte.zaokruzi(retval, this.getKarta().getRoundN()); //josip 27.12.2012
		return retval.setScale(2);
	}

	public BigDecimal getNettoCijena() {
		return getProdajnaCijena().subtract(getIznosPDV());

	}

	public BigDecimal getCijenaVoznje() {
		BigDecimal stVoznji = new BigDecimal (karta.getStVoznji());
	        if (stVoznji.equals(0)) 
	            stVoznji = new BigDecimal(1);	
	    BigDecimal Cena = getProdajnaCijena().divide(stVoznji);
	    CijenaKarte.zaokruzi(Cena, this.getKarta().getRoundN());
	    
		return (Cena);

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
 		this.cijenaKarte.setCijena(cijena); // josip 27.12.2012
		this.cijenaZamjenske = cijena;
	}

	public Integer getKmLinije() {
		return cijenaKarte.getDistancaLinije() / 1000;
	}

	public Integer getKmDomaci() {
		return cijenaKarte.getDomDistanca() / 1000;
	}

	public Integer getKmCenika() {
		return cijenaKarte.getDistancaCenika() / 1000;
	}

	public Integer getKmIno() {
		return cijenaKarte.getInoDistanca() / 1000;
	}

	public void setCijena(String s) {
		try {
			NumberFormat nf = NumberFormat.getNumberInstance();
			DecimalFormat df = (DecimalFormat) nf;
			df.parseObject(s);
			setCijena(new BigDecimal(s));
		} catch (Exception e) {
			setCijena(BigDecimal.ZERO);
		}
	}
	
	public void setCijenaPrelaz(Stavka oldstavka) {
		// 'this' je prelazna stavka
		// oldstavka je napunjena
		// izračunaš cijenu na osnovu this i oldstavka i setiraš cijenu
		//
		if ((oldstavka.karta.getNacinDolocanjaCene() == Karta.TARIFNI_DALJINAR)  &&    
		    (this.karta.getNacinDolocanjaCene()      == Karta.TARIFNI_DALJINAR)  &&   // oba su po tarifnom daljinaru
		    (oldstavka.karta.getTarifniRazredID() == this.karta.getTarifniRazredID()) // imaju iste tarifne razrede
		   ) 
		{   
		 if (! oldstavka.jeZamjenska) {	
			 int kmOld = oldstavka.getKmCenika();
			 int kmNew = this.getKmCenika();
			 int ukupniKm = kmOld + kmNew; // izracunamo ukupne km
			 BigDecimal staraCijena = oldstavka.getProdajnaCijena();
			 BigDecimal ukupnaTarifnaCena = this.cijenaKarte.izracunajTarifnoCeneKarte(ukupniKm).multiply(BigDecimal.ONE.subtract(popust.getPopust().movePointLeft(2))).setScale(2,BigDecimal.ROUND_CEILING);
			 
			 BigDecimal novaCijenaKarte = ukupnaTarifnaCena.subtract(staraCijena); 
			 setCijena(novaCijenaKarte);
		 }
		 else // zamjenska prijelazna 
		 {
		    this.setJeZamjenska(oldstavka.jeZamjenska); // postavimo da je i ta zamjenska
		    this.setBrojKarte(oldstavka.brojKarte);     // postavimo isti broj karte
		    setCijena(new BigDecimal(0));               // zamjenske imaju cijenu prijelaza 0
		 }
		}
	}
}
