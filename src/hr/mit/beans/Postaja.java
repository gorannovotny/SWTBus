package hr.mit.beans;

import hr.mit.utils.DbUtil;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Postaja  extends SuperBean{

	public Postaja(Integer stupacID) {
		try {
			String sql0 = "SELECT smerVoznje FROM PTStupciVR WHERE ID = ?";
			PreparedStatement ps0 = con.prepareStatement(sql0);
			ps0.setInt(1, stupacID);
			String smjer = DbUtil.getSingleResultString(ps0);
			ps0.close();
			StringBuffer sql1 = new StringBuffer("SELECT c.ZapSt,e.Naziv,f.VremeOdhoda from PTStupciVR a,PTVarijanteVR b,PTPostajeVarijantVR c,PTPostajeVR d,PTPostaje e,PTCasiVoznjeVR f where a.VarijantaVRID = b.ID and   c.VarijantaID = b.ID and   c.NodePostajeVRID = d.ID and   d.PostajaID = e.id and   a.id = ? AND f.StupacVRID = a.ID AND f.NodePostajeVarijanteVRID = c.ID ORDER BY 1");
			if (smjer.equals("+"))
				sql1.append(" DESC");
			PreparedStatement ps = con.prepareStatement(sql1.toString());
			ps.setInt(1, stupacID);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				nazivList.add(rs.getString(2) + " " + DbUtil.getHHMM(rs.getDouble(3)));
				idList.add(rs.getInt(1));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
