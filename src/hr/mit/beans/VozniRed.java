package hr.mit.beans;

import java.sql.ResultSet;
import java.sql.SQLException;

public class VozniRed extends SuperBean{
	public VozniRed() {
		try {
			String sql = "SELECT id,Sifra,Opis1 FROM PTVozniRedi WHERE PTVozniRedi.VeljaDo > '2012-01-01 00:00:00' AND vrstaVR = 1 ORDER BY 2";
			ResultSet rs = con.createStatement().executeQuery(sql);
			while (rs.next()) {
				nazivList.add(rs.getString(2) + " " + rs.getString(3));
				idList.add(rs.getInt(1));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
