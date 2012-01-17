package hr.mit.utils;

import hr.mit.beans.Karta;
import hr.mit.beans.Stupac;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;

public class CijenaKarte {
	BigDecimal cenaKarte;
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

	public CijenaKarte(Integer kartaID, Integer varijantaID, Integer odZapSt, Integer doZapSt) throws SQLException {
		this.varijantaID = varijantaID;
		if (odZapSt > doZapSt) {
			this.odZapSt = doZapSt;
			this.doZapSt = odZapSt;

		} else {
			this.odZapSt = odZapSt;
			this.doZapSt = doZapSt;
		}
		karta = new Karta(kartaID);

	}

	public static void Fn_PTTblPodatkiCeneVozneKarte(Integer voznaKartaID, Stupac stupac, Integer odPostajeID, Integer doPostajeID, Date zaDan) {

		Fn_PTTblPodatkiDistanceVarVR(stupac.getVarijantaID(), odPostajeID, doPostajeID, "191");

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
		String sql = "SELECT SUM(DistancaM) FROM PTPostajeVarijantVR a,PTPostajeVR b,PTPostaje c WHERE b.ID = a.NodePostajeVR AND c.ID = b.NodePostaje AND a.VarijantaID = ? AND a.ZapSt > ? AND a.ZapSt <= ? AND c.Drzava = '191'";
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
		if (karta.getNacinDolocanjaCene() == Karta.FIKSNI_KILOMETRI)
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
	
	public BigDecimal getIznos(){
		if (karta.getNacinDolocanjaCene() == Karta.FIKSNA_CIJENA)
			return karta.getFiksnaCena();
		else {
			// TODO Prvo iznimke, a onda po tarifnim razredima
			
		}
			
		return null;
	}

	private static void Fn_PTTblPodatkiDistanceVarVR(Integer varijantaID, Integer odZapStID, Integer doZapStID, String drzava) {
		String sql_kum = "SELECT SUM(DistancaM) FROM PTPostajeVarijantVR WHERE ID = ? AND ZapSt > ? AND ZapSt <= ?";
		String sql_ino = "SELECT SUM(DistancaM) FROM PTPostajeVarijantVR WHERE ID = ? AND ZapSt > ? AND ZapSt <= ? AND Drzava != ? ";
		String sql_distancaLinije = "SELECT SUM(DistancaM) FROM PTPostajeVarijantVR WHERE ID = ? AND ZapSt NOT IN (SELECT MIN(ZapSt) FROM PTPostajeVarijantVR WHERE ID = ?)";
	}
}
