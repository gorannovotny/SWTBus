package hr.mit.beans;

import hr.mit.utils.DbUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import java.math.BigDecimal;

public class KartaList {
	Connection con;
	PreparedStatement ps;

	List<Karta> karte = new ArrayList<Karta>();

	public KartaList() {
		this.con = DbUtil.getConnection();
		try {
			String sql = "Select id,Opis,NacinDolocanjaCene,TarifniRazredID,FiksnaCena,PopustID,KMPogoja from PTKTVozneKarte WHERE PTKTVozneKarte.MobilnaProdaja = 1";
			ps = con.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				Karta k = new Karta(rs.getInt("id"), rs.getString("Opis"), rs.getInt("NacinDolocanjaCene"), rs.getInt("TarifniRazredID"), new BigDecimal(rs.getDouble("FiksnaCena")),
			 			rs.getInt("PopustID"), rs.getInt("KMPogoja"));
				karte.add(k);
			}
			rs.close();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public String[] getList() {
		ArrayList<String> x = new ArrayList<String>();
		for (Karta k : karte) {
			x.add(k.getNaziv());
		}
		return x.toArray(new String[0]);
	}

	public Integer getID(int index) {
		return karte.get(index).getId();
	}

	public Karta get(Integer index) {
		return karte.get(index);
	}
}
