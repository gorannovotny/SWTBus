package hr.mit.beans;

import hr.mit.utils.DbUtil;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Karta {
	Integer id;
	Integer nacinDolocanjaCene;
	Integer tarifniRazredID;
	BigDecimal fiksnaCena;
	Integer popustID;
	Integer kmPogoja;

	public static Integer TARIFNI_DALJINAR = 1;
	public static Integer FIKSNA_CIJENA = 2;
	public static Integer FIKSNI_KILOMETRI = 3;

	public Karta(Integer id) throws SQLException {
		String sql = "SELECT NacinDolocanjaCene,TarifniRazredID,FiksnaCena,PopustID,KMPogoja from PTKTVozneKarte WHERE ID = ?";
		PreparedStatement ps = DbUtil.getConnection().prepareStatement(sql);
		ps.setInt(1, id);
		ResultSet rs = ps.executeQuery();
		if (rs.next()) {
			this.id = id;
			nacinDolocanjaCene = rs.getInt("NacinDolocanjaCene");
			tarifniRazredID = rs.getInt("TarifniRazredID");
			fiksnaCena = rs.getBigDecimal("FiksnaCena");
			popustID = rs.getInt("PopustID");
			kmPogoja = rs.getInt("KMPogoja");
		}
	}

	public Integer getId() {
		return id;
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
