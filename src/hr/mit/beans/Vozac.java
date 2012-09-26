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
	private String password;

	static {
		try {
			String sql = "SELECT ID,Sifra,Naziv,Coalesce(MobilePassword,'') FROM PTVozaci ORDER BY Naziv";
			ResultSet rs = DbUtil.getConnection().createStatement().executeQuery(sql);
			while (rs.next()) {
				vozacList.add(new Vozac(rs.getInt(1),rs.getInt(2),rs.getString(3),rs.getString(4)));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public Vozac(Integer id, Integer sifra, String naziv, String password) {
		super();
		this.id = id;
		this.sifra = sifra;
		this.naziv = naziv;
		this.password = password;
	}

	
	public static Vozac getBySifra(Integer sifra) {
		for (Vozac v : vozacList) {
			if (v.getSifra().equals(sifra)) return v;
		}
		return null;
	}
	public static Vozac getByID(Integer ID) {
		for (Vozac v : vozacList) {
			if (v.getId().equals(ID)) return v;
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
	
	public String getPassword() {
		return password;
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
