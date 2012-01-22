package hr.mit.beans;

public class Postaja {
	private Integer zapSt;
	private String naziv;

	public Postaja(Integer zapSt, String naziv) {
		this.zapSt = zapSt;
		this.naziv = naziv;
	}

	public Integer getZapSt() {
		return zapSt;
	}

	public String getNaziv() {
		return naziv;
	}

}
