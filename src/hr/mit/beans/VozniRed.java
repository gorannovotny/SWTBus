package hr.mit.beans;

import hr.mit.utils.DbUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class VozniRed {
	private static ArrayList<VozniRed> vrList = new ArrayList<VozniRed>();

	private Integer id;
	private String opis;
	
	static {
		try {
			String sql = "SELECT ID,Opis1 FROM PTVozniRedi WHERE PTVozniRedi.VeljaDo > '2012-01-01 00:00:00' AND vrstaVR = 1 ORDER BY 2";
			ResultSet rs = DbUtil.getConnection().createStatement().executeQuery(sql);
			while (rs.next()) {
				vrList.add(new VozniRed(rs.getInt(1),rs.getString(2)));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	
	public VozniRed(Integer id,String opis) {
		this.id = id;
		this.opis = opis;
	}
	
	public Integer getId() {
		return id;
	}
	
	public String getOpis(){
		return opis;
	}
	

	public static String[] getList() {
		String[] l = new String[vrList.size()];
		int x = 0;
		for (VozniRed v : vrList) {
			l[x] = v.getId() + " " + v.getOpis();
			x++;
		}
		return l;
	}

	public static List<String> getArrayList() {
		List<String> l = new ArrayList<String>();
		for (VozniRed v : vrList) {
			l.add(v.getId() + " " + v.getOpis());
		}
		return l;
	}
	
	public static VozniRed getByID(int id) {
		for (VozniRed v : vrList) {
			if (v.getId().equals(id))
				return v;
		}
		return null;
	}
	
	
	public static VozniRed get(int index) {
		return vrList.get(index);
	}
}
