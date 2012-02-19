package hr.mit.beans;

import hr.mit.utils.DbUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Vozac {
	private static ArrayList<Vozac> vozacList = new ArrayList<Vozac>();

	private Integer id;
	private Integer sifra;
	private String naziv;

	static {
		try {
			String sql = "SELECT id,Sifra,Naziv FROM PTVozaci ORDER BY Naziv";
			ResultSet rs = DbUtil.getConnection().createStatement().executeQuery(sql);
			while (rs.next()) {
				vozacList.add(new Vozac(rs.getInt(1),rs.getInt(2),rs.getString(3)));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public Vozac(Integer id, Integer sifra, String naziv) {
		super();
		this.id = id;
		this.sifra = sifra;
		this.naziv = naziv;
	}

	
	public static Vozac getBySifra(Integer sifra) {
		for (Vozac v : vozacList) {
			if (v.getSifra().equals(sifra)) return v;
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
		String[] l = new String[vozacList.size()];
		int x= 0;
		for (Vozac v : vozacList) {
			l[x] = v.getNaziv();
			x++;
		}
		return l;
	}
}
