package hr.mit.beans;

import hr.mit.utils.DbUtil;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Stupac {
	private static ArrayList<Stupac> stupacList = new ArrayList<Stupac>();

	private Integer id;
	private Integer vozniRedID;
	private String smjer;
	private Integer varijantaID;
	private String opis;
	private String vremeOdhoda;

	public static void setVozniRed(String sifraVR) {
		try {
			String sql = "select a.ID,a.VozniRedID,a.VarijantaVRID,a.SmerVoznje,b.Opis1,b.Opis2,a.VremeOdhoda from PTStupciVR a,PTVozniRedi b WHERE a.VozniRedID = b.id AND b.Sifra = ? ORDER BY a.VremeOdhoda";
			PreparedStatement ps = DbUtil.getConnection().prepareStatement(sql);
			ps.setString(1, sifraVR);
			ResultSet rs = ps.executeQuery();
			stupacList.clear();
			while (rs.next()) {
				stupacList.add(new Stupac(rs.getInt("ID"), rs.getInt("VozniRedID"), rs.getString("SmerVoznje"), rs.getInt("VarijantaVRID"), rs.getString("Opis1"), rs.getString("Opis2"), rs
						.getDouble("VremeOdhoda")));
			}
			rs.close();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public Stupac(Integer id, Integer vozniRedID, String smjer, Integer varijantaID, String opis1, String opis2, double vremeOdhoda) {
		super();
		this.id = id;
		this.vozniRedID = vozniRedID;
		this.smjer = smjer;
		this.varijantaID = varijantaID;
		if (smjer.equals("-"))
			this.opis = opis1;
		else
			this.opis = opis2;
		this.vremeOdhoda = DbUtil.getHHMM(vremeOdhoda);
	}

	public static String[] getList() {
		String[] l = new String[stupacList.size()];
		int x = 0;
		for (Stupac v : stupacList) {
			l[x] = v.getVremeOdhoda();
			x++;
		}
		return l;
	}

	public static List<String> getArrayList() {
		List<String> l = new ArrayList<String>();
		for (Stupac v : stupacList) {
			l.add(v.getVremeOdhoda());
		}
		return l;
	}
	
	public String getVremeOdhoda() {
		return vremeOdhoda;
	}

	public static Stupac get(int index) {
		return stupacList.get(index);
	}

	public Integer getVarijantaID() {
		return varijantaID;
	}

	public Integer getId() {
		return id;
	}

	public String getOpis() {
		return opis;
	}

	public String getSmjer() {
		return smjer;
	}

}
