package hr.mit.beans;

import java.math.BigDecimal;

public class Popust {
	private Integer id;
	private String naziv;
	private BigDecimal popust;




	public Popust(int id, String naziv,double popust) {
		this.id = id;
		this.naziv = naziv;
		this.popust = new BigDecimal(popust);
	}

	public Integer getId() {
		return id;
	}

	public String getNaziv() {
		if (naziv.length()>25) naziv = naziv.substring(0,25);
		return String.format("%-25s (%2d%%)",naziv, popust.intValue());
	}
	public BigDecimal getPopust() {
		return popust;
	}


}
