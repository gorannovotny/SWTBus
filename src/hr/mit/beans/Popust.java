package hr.mit.beans;

import hr.mit.utils.DbUtil;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Popust {

	private static ArrayList<Popust> popustList = new ArrayList<Popust>();

	private Integer id;
	private String naziv;
	private BigDecimal popust;

	public static void setKartaStupac(Karta karta, Stupac stupac) {
		String sql = "select a.ID,a.Opis,a.Popust from PTKTPopusti a WHERE a.TipKarteID = ? AND (a.StupacID = ? OR a.stupacID = 0 ) AND VeljaOd = (SELECT MAX(VeljaOd) FROM PTKTPopusti b WHERE b.TipKarteID = a.TipKarteID AND   b.VrstaPopustaID = a.VrstaPopustaID AND   b.StupacID = a.StupacID AND   b.VeljaOd <= DATETIME('now')) ";
		popustList.clear();
		popustList.add(new Popust(0,"Bez popusta",0.0));
		try {
			PreparedStatement ps = DbUtil.getConnection().prepareStatement(sql);
			ps.setInt(1,karta.getTipKarteID());
			ps.setInt(2, stupac.getId());
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				popustList.add(new Popust(rs.getInt("ID"),rs.getString("Opis"),rs.getDouble("Popust")));	
			}
			rs.close();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public Popust(int id, String naziv, double popust) {
		this.id = id;
		this.naziv = naziv;
		this.popust = new BigDecimal(popust);
	}
	
	public static String[] getList() {
		String[] l = new String[popustList.size()];
		int x = 0;
		for (Popust v : popustList) {
			l[x] = v.getPopust().toString() + "% " + v.getNaziv();
			x++;
		}
		return l;
	}

	public static Popust get(int index) {
		return popustList.get(index);
	}
	public Integer getId() {
		return id;
	}

	public String getNaziv() {
		return naziv;
	}

	public BigDecimal getPopust() {
		return popust;
	}

}
