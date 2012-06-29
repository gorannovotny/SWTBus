package hr.mit.utils;

import hr.mit.beans.Karta;
import hr.mit.beans.Postaja;
import hr.mit.beans.Stavka;
import hr.mit.beans.Stupac;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CijenaKarte {
	// BigDecimal cenaKarte;
	// Integer distancaCenika;
	// Integer distancaLinije;
	// Integer distancaRelacije;
	// Integer domDistanca;
	// Integer inoDistanca;
	Integer jeTarifnaRelacija;
	Integer jeCenaPosCenika;

	private Integer varijantaID;
	private Integer odZapSt;
	private Integer doZapSt;
	private Karta karta;
	private Integer distancaLinije;
	private Integer distancaRelacije;
	private Integer domDistanca;
	private Integer inoDistanca;
	private BigDecimal cijena;

	public CijenaKarte(Stupac stupac, Karta karta, Postaja odPostaje, Postaja doPostaje) {
		this.varijantaID = stupac.getVarijantaID();
		if (odPostaje.getZapSt() > doPostaje.getZapSt()) {
			this.odZapSt = doPostaje.getZapSt();
			this.doZapSt = odPostaje.getZapSt();

		} else {
			this.odZapSt = odPostaje.getZapSt();
			this.doZapSt = doPostaje.getZapSt();
		}
		this.karta = karta;
		this.distancaLinije = izracunajDistancaLinije();
		this.distancaRelacije = izracunajDistancaRelacije();
		this.domDistanca = izracunajDomDistanca();
		this.inoDistanca = izracunajInoDistanca();
		this.cijena = izracunajCijena();
	}

	private Integer izracunajDistancaLinije() {
		String sql = "SELECT SUM(DistancaM) FROM PTPostajeVarijantVR WHERE Staje <> '-' and VarijantaID = ? AND ZapSt NOT IN (SELECT MIN(ZapSt) FROM PTPostajeVarijantVR WHERE VarijantaID = ?)";
		Integer retval;
		try {
			PreparedStatement ps = DbUtil.getConnection().prepareStatement(sql);
			ps.setInt(1, varijantaID);
			ps.setInt(2, varijantaID);
			retval = DbUtil.getSingleResult(ps);
			ps.close();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return retval;
	}

	private Integer izracunajDistancaRelacije() {
		String sql = "SELECT SUM(DistancaM) FROM PTPostajeVarijantVR WHERE Staje <> '-' and VarijantaID = ? AND ZapSt > ? AND ZapSt <= ?";
		Integer retval;
		try {
			PreparedStatement ps = DbUtil.getConnection().prepareStatement(sql);
			ps.setInt(1, varijantaID);
			ps.setInt(2, odZapSt);
			ps.setInt(3, doZapSt);
			retval = DbUtil.getSingleResult(ps);
			ps.close();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return retval;
	}

	private Integer izracunajDomDistanca() {
		String sql = "SELECT SUM(a.DistancaM) FROM PTPostajeVarijantVR a,PTPostajeVR b,PTPostaje c WHERE a.staje <> '-'  and b.ID = a.NodePostajeVRID AND c.ID = b.PostajaID AND a.VarijantaID = ? AND a.ZapSt > ? AND a.ZapSt <= ? AND c.Drzava = '191'";
		Integer retval;
		try {
			PreparedStatement ps = DbUtil.getConnection().prepareStatement(sql);
			ps.setInt(1, varijantaID);
			ps.setInt(2, odZapSt);
			ps.setInt(3, doZapSt);
			retval = DbUtil.getSingleResult(ps);
			ps.close();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return retval;
	}

	private Integer izracunajInoDistanca() {
		return getDistancaRelacije() - getDomDistanca();
	}

	private Integer getDistancaCenika() throws SQLException {
		Integer retval;
		if (karta.getNacinDolocanjaCene().equals(Karta.FIKSNI_KILOMETRI))
			retval = karta.getKmPogoja() * 1000;
		else {
			String sql1 = "SELECT SUM(a.DistancaM) FROM PTPostajeVarijantVR a WHERE a.VarijantaID = ? AND a.ZapSt > ? AND a.ZapSt <= ? AND a.Vozel IN  (SELECT Vozel  FROM PTPostajeVarijantVR b WHERE b.VarijantaID = a.VarijantaID AND (b.ZapSt = ? OR b.ZapSt = ?))";
			PreparedStatement ps1 = DbUtil.getConnection().prepareStatement(sql1);
			ps1.setInt(1, varijantaID);
			ps1.setInt(2, odZapSt);
			ps1.setInt(3, doZapSt);
			ps1.setInt(4, odZapSt);
			ps1.setInt(5, doZapSt);
			retval = DbUtil.getSingleResult(ps1);
			ps1.close();
		}
		return retval;
	}

	private BigDecimal izracunajCijena() {
		BigDecimal retval = BigDecimal.ZERO;
		if (karta.getNacinDolocanjaCene().equals(Karta.FIKSNA_CIJENA))
			retval = karta.getFiksnaCena();
		else {
			if (false) {
				/*
				 * TODO Ima iznimke Select IC.Cena from PTIzjemeCenikaVR IC
				 * Where IC.TarifniCenikID = @TarifniCenikID and IC.VeljaOd <=
				 * (@ZaDan and IC.VozniRedID = @VozniRedID and IC.VarijantaID in
				 * (0,@VarijantaID) and IC.StupacID in (0,@StupacID) and
				 * ((IC.Postaja1ID = @OdPostajeID and IC.Postaja2ID =
				 * 
				 * @DoPostajeID) or (IC.Postaja1ID = @DoPostajeID and
				 * IC.Postaja2ID = @OdPostajeID)) order by IC.VeljaOd desc,
				 * IC.StupacID desc, IC.VarijantaID desc LIMIT 1;
				 */
			} else {
				String sql = "SELECT Cena FROM PTKTTarifniRazrediCenik WHERE IDRazreda = ? AND VeljaOd <= DATE('now') and OdKM <= ? order by VeljaOd desc, OdKM desc LIMIT 1";
				try {
					PreparedStatement ps = DbUtil.getConnection().prepareStatement(sql);
					ps.setInt(1, karta.getTarifniRazredID());
					ps.setInt(2, getDistancaCenika() / 1000);
					Double cena = DbUtil.getSingleResultDouble(ps);
					ps.close();
					if (cena != null) {
						retval = new BigDecimal(cena);
						retval = retval.multiply(karta.getFaktorCene());
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
//		return retval.multiply(BigDecimal.ONE.subtract(karta.getPopustProcent().movePointLeft(2)));
		return retval;
	}

	public Integer getDistancaLinije() {
		return distancaLinije;
	}

	public Integer getDistancaRelacije() {
		return distancaRelacije;
	}

	public Integer getDomDistanca() {
		return domDistanca;
	}

	public Integer getInoDistanca() {
		return inoDistanca;
	}

	public BigDecimal getCijena() {
		return cijena;
	}

	public static BigDecimal zaokruzi(BigDecimal iznos, BigDecimal r) {
		if (r == null || r.floatValue() < 0.01) return iznos;
		int i = (int) ((iznos.floatValue() - 0.0001) / r.floatValue());
		BigDecimal retval  = r.multiply(new BigDecimal(i+1)); 
		return retval;
	}
}
