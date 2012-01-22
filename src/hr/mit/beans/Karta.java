package hr.mit.beans;

import java.math.BigDecimal;

public class Karta {
	Integer id;
	String naziv;
	Integer nacinDolocanjaCene;
	Integer tarifniRazredID;
	BigDecimal fiksnaCena;
	Integer popustID;
	Integer kmPogoja;

	public static Integer TARIFNI_DALJINAR = 1;
	public static Integer FIKSNA_CIJENA = 2;
	public static Integer FIKSNI_KILOMETRI = 3;



	public Karta(int id, String naziv, int nacinDolocanjaCene, int tarifniRazredID, BigDecimal FiksnaCena, int popustID, int kmPogoja) {
		this.id = id;
		this.naziv = naziv;
		this.nacinDolocanjaCene = nacinDolocanjaCene;
		this.tarifniRazredID = tarifniRazredID;
		this.fiksnaCena = FiksnaCena;
		this.popustID = popustID;
		this.kmPogoja = kmPogoja;
	}

	public Integer getId() {
		return id;
	}

	public String getNaziv() {
		return naziv;
	}

	public Integer getNacinDolocanjaCene() {
		return nacinDolocanjaCene;
	}

	public Integer getTarifniRazredID() {
		return tarifniRazredID;
	}

	public BigDecimal getFiksnaCena() {
		return fiksnaCena;
	}

	public Integer getPopustID() {
		return popustID;
	}

	public Integer getKmPogoja() {
		return kmPogoja;
	}
}
