package hr.mit.beans;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class VozniRed {
	ArrayList<String> nazivList = new ArrayList<String>();
	ArrayList<Integer> idList = new ArrayList<Integer>();

	public VozniRed(Connection con) {
		try {
			String sql = "SELECT id,Opis1 FROM PTVozniRedi WHERE PTVozniRedi.VeljaDo > '2012-01-01 00:00:00'";
			ResultSet rs = con.createStatement().executeQuery(sql);
			while (rs.next()) {
				nazivList.add(rs.getString(2));
				idList.add(rs.getInt(1));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public String[] getVozniRedi() {
		return nazivList.toArray(new String[0]);
	}

	public Integer getVozniRedID(int selectionIndex) {
		if (selectionIndex >= 0)
			return idList.get(selectionIndex);
		else
			return new Integer(-1);
	}
}
