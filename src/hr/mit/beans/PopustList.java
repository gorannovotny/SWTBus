package hr.mit.beans;

import hr.mit.utils.DbUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PopustList {
	Connection con;
	PreparedStatement ps;

	List<Popust> popusti = new ArrayList<Popust>();

	public PopustList(Stupac stupac,Karta karta) {
		popusti.add(new Popust(0,"",0)); // Dodaj prazni popust
		this.con = DbUtil.getConnection();
		try {
			// TODO riješiti stupce
			String sql = "select * from PTKTPopusti a WHERE a.TipKarteID = ? AND (a.StupacID = ? OR a.stupacID = 0 ) AND VeljaOd = (SELECT MAX(VeljaOd) FROM PTKTPopusti b WHERE b.TipKarteID = a.TipKarteID AND   b.VrstaPopustaID = a.VrstaPopustaID AND   b.StupacID = a.StupacID AND   b.VeljaOd <= DATETIME('now')) ";
			ps = con.prepareStatement(sql);
			ps.setInt(1, karta.getTipKarteID());
			ps.setInt(2, stupac.getID());
			ResultSet rs = ps.executeQuery();
			
			while (rs.next()) {
				Popust p = new Popust(rs.getInt("ID"),rs.getString("Opis"),rs.getDouble("Popust"));
				popusti.add(p);
			}
			rs.close();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public String[] getList() {
		ArrayList<String> x = new ArrayList<String>();
		for (Popust p : popusti) {
			x.add(p.getNaziv());
		}
		return x.toArray(new String[0]);
	}

	public Integer getID(int index) {
		return popusti.get(index).getId();
	}

	public Popust get(Integer index) {
		return popusti.get(index);
	}
}
