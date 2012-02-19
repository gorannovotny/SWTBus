package hr.mit.beans;

import hr.mit.utils.DbUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ProdajnoMjesto {
	static ArrayList<ProdajnoMjesto> pmList = new ArrayList<ProdajnoMjesto>();

	private Integer id;
	private Integer sifra;
	private String naziv;

	static {
		pmList.add(new ProdajnoMjesto(0, 0, "U autobusu"));
		try {
			String sql = "SELECT id,Sifra,Naziv FROM PTProdajnaMesta ORDER BY 1";
			ResultSet rs = DbUtil.getConnection().createStatement().executeQuery(sql);
			while (rs.next()) {
				pmList.add(new ProdajnoMjesto(rs.getInt(1), rs.getInt(2), rs.getString(3)));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public ProdajnoMjesto(Integer id, Integer sifra, String naziv) {
		super();
		this.id = id;
		this.sifra = sifra;
		this.naziv = naziv;
	}

	public static ProdajnoMjesto getBySifra(Integer sifra) {
		for (ProdajnoMjesto pm : pmList) {
			if (pm.getSifra().equals(sifra))
				return pm;
		}
		return null;
	}

	public Integer getId() {
		return id;
	}

	public Integer getSifra() {
		return sifra;
	}

	public String getNaziv() {
		return naziv;
	}

	public static String[] getList() {
		String[] l = new String[pmList.size()];
		for (int i = 0; i < pmList.size(); i++) {
			l[i] = pmList.get(i).getNaziv();
		}
		return l;
	}
}
