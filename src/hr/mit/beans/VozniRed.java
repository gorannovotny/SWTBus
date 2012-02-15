package hr.mit.beans;

import hr.mit.utils.DbUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class VozniRed {
	static ArrayList<String> nazivList = new ArrayList<String>();
	static ArrayList<String> idList = new ArrayList<String>();

	static {
		try {
			String sql = "SELECT Sifra,Opis1 FROM PTVozniRedi WHERE PTVozniRedi.VeljaDo > '2012-01-01 00:00:00' AND vrstaVR = 1 ORDER BY 2";
			ResultSet rs = DbUtil.getConnection().createStatement().executeQuery(sql);
			while (rs.next()) {
				nazivList.add(rs.getString(2));
				idList.add(rs.getString(1));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static String[] getList() {
		return nazivList.toArray(new String[0]);
	}

	public static String getID(int selectionIndex) {
		if (selectionIndex >= 0)
			return idList.get(selectionIndex);
		else
			return "";
	}

	public static String getNaziv(String sifra) {
		int i = idList.indexOf(sifra);
		if (i != -1)
			return nazivList.get(i);
		else
			return "";
	}

}
