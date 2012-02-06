package hr.mit.beans;

import hr.mit.utils.DbUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Stupac {

	private Integer ID;
	private String longDesc;
	private Integer varijantaID;

	public Stupac(Integer stupacID) {
		Connection con = DbUtil.getConnection();
		try {
			String sql = "select b.Opis1, a.VremeOdhoda,a.VarijantaVRID from PTStupciVR a,PTVozniRedi b where b.ID = a.VozniRedID and a.id = ? ";
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setInt(1, stupacID);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				longDesc = rs.getString("Opis1") + " Polazak:" + DbUtil.getHHMM(rs.getDouble("VremeOdhoda"));
				varijantaID = rs.getInt("VarijantaVRID");
				ID = stupacID;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public String getLongDesc() {
		return longDesc;
	}

	public Integer getVarijantaID() {
		return varijantaID;
	}

	public Integer getID() {
		return ID;
	}

}
