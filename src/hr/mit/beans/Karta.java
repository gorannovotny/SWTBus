package hr.mit.beans;

import java.math.BigDecimal;

public class Karta {
	private Integer id;
	private String naziv;
	private Integer stVoznji;
	private Integer nacinDolocanjaCene;
	private Integer tarifniRazredID;
	private BigDecimal fiksnaCena;
	private Integer popustID;
	private Integer kmPogoja;

	public static Integer TARIFNI_DALJINAR = 1;
	public static Integer FIKSNA_CIJENA = 2;
	public static Integer FIKSNI_KILOMETRI = 3;



	public Karta(int id, String naziv,int stVoznji, int nacinDolocanjaCene, int tarifniRazredID, BigDecimal FiksnaCena, int popustID, int kmPogoja) {
		this.id = id;
		this.naziv = naziv;
		this.stVoznji = stVoznji;
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

	public Integer getStVoznji() {
		return stVoznji;
	}
}
