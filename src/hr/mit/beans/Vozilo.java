package hr.mit.beans;

import hr.mit.utils.DbUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Vozilo {
	private static ArrayList<Vozilo> voziloList = new ArrayList<Vozilo>();

	private Integer id;
	private String sifra;
	private String naziv;
	private String regSt;
	
	static {
		try {
			String sql = "SELECT id,Sifra,Naziv,RegSt FROM PromVozila ORDER BY Sifra";
			ResultSet rs = DbUtil.getConnection().createStatement().executeQuery(sql);
			while (rs.next()) {
				voziloList.add(new Vozilo(rs.getInt("id"),rs.getString("Sifra"),rs.getString("Naziv"),rs.getString("RegSt")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	public Vozilo(Integer id, String sifra, String naziv, String regSt) {
		super();
		this.id = id;
		this.sifra = sifra;
		this.naziv = naziv;
		this.regSt = regSt;
	}

	public Integer getId() {
		return id;
	}

	public String getSifra() {
		return sifra;
	}

	public String getNaziv() {
		return naziv;
	}

	public String getRegSt() {
		return regSt;
	}

	public static Vozilo getBySifra(String sifra) {
		for (Vozilo v : voziloList) {
			if (v.getSifra().equals(sifra)) return v;
		}
		return null;
	}

	public static String[] getList() {
		String[] l = new String[voziloList.size()];
		int x= 0;
		for (Vozilo v : voziloList) {
			l[x] = v.getNaziv();
			x++;
		}
		return l;
	}

}
