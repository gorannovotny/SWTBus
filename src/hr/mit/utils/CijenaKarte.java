package hr.mit.utils;

import hr.mit.beans.Karta;
import hr.mit.beans.Postaja;
import hr.mit.beans.Stupac;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CijenaKarte {
	// BigDecimal cenaKarte;
	BigDecimal znesekPopusta;
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

	public CijenaKarte(Karta karta, Integer varijantaID, Integer odZapSt, Integer doZapSt) throws SQLException {
		this.varijantaID = varijantaID;
		if (odZapSt > doZapSt) {
			this.odZapSt = doZapSt;
			this.doZapSt = odZapSt;

		} else {
			this.odZapSt = odZapSt;
			this.doZapSt = doZapSt;
		}
		this.karta = karta;

	}

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
	}

	public Integer getDistancaLinije() throws SQLException {
		String sql = "SELECT SUM(DistancaM) FROM PTPostajeVarijantVR WHERE VarijantaID = ? AND ZapSt NOT IN (SELECT MIN(ZapSt) FROM PTPostajeVarijantVR WHERE VarijantaID = ?)";
		PreparedStatement ps = DbUtil.getConnection().prepareStatement(sql);
		ps.setInt(1, varijantaID);
		ps.setInt(2, varijantaID);
		Integer retval = DbUtil.getSingleResult(ps);
		ps.close();
		return retval;
	}

	public Integer getDistancaRelacije() throws SQLException {
		String sql = "SELECT SUM(DistancaM) FROM PTPostajeVarijantVR WHERE VarijantaID = ? AND ZapSt > ? AND ZapSt <= ?";
		PreparedStatement ps = DbUtil.getConnection().prepareStatement(sql);
		ps.setInt(1, varijantaID);
		ps.setInt(2, odZapSt);
		ps.setInt(3, doZapSt);
		Integer retval = DbUtil.getSingleResult(ps);
		ps.close();
		return retval;
	}

	public Integer getDomDistanca() throws SQLException {
		String sql = "SELECT SUM(a.DistancaM) FROM PTPostajeVarijantVR a,PTPostajeVR b,PTPostaje c WHERE b.ID = a.NodePostajeVRID AND c.ID = b.PostajaID AND a.VarijantaID = ? AND a.ZapSt > ? AND a.ZapSt <= ? AND c.Drzava = '191'";
		PreparedStatement ps = DbUtil.getConnection().prepareStatement(sql);
		ps.setInt(1, varijantaID);
		ps.setInt(2, odZapSt);
		ps.setInt(3, doZapSt);
		Integer retval = DbUtil.getSingleResult(ps);
		ps.close();
		return retval;
	}

	public Integer getInoDistanca() throws SQLException {
		return getDistancaRelacije() - getDomDistanca();
	}

	public Integer getDistancaCenika() throws SQLException {
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

	public BigDecimal getCijena() {
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
					retval = new BigDecimal(DbUtil.getSingleResultDouble(ps));
					retval = retval.multiply(new BigDecimal(karta.getStVoznji()));
					ps.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return retval;
	}

	public BigDecimal getPopust() {
		Integer naciz;
		BigDecimal vrednost;
		BigDecimal retval = null;
		String sql = "SELECT NacinIzracuna,Vrednost FROM PTKTVrstePopustov WHERE ID = ?";
		try {
			PreparedStatement ps = DbUtil.getConnection().prepareStatement(sql);
			ps.setInt(1, karta.getPopustID());
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				naciz = rs.getInt("NacinIzracuna");
				vrednost = new BigDecimal(rs.getDouble("Vrednost"));
				if (naciz.equals(0))
					retval = getCijena().multiply(vrednost).divide(new BigDecimal(100));
				else
					retval = vrednost;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return retval;
	}

	public BigDecimal getUkupnaCijena() {
		return getCijena().multiply(BigDecimal.ONE.subtract(karta.getPopustProcent().movePointLeft(2)));
	}
}
