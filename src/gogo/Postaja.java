package gogo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Postaja {
	ArrayList<String> nazivList = new ArrayList<String>();
	ArrayList<Integer> zapStList = new ArrayList<Integer>();

	Postaja(Connection con, Integer stupacID) {
		try {
			String sql0 = "SELECT smerVoznje FROM PTStupciVR WHERE ID = ?";
			PreparedStatement ps0 = con.prepareStatement(sql0);
			ps0.setInt(1, stupacID);
			String smjer = DbUtil.getSingleResultString(ps0);
			ps0.close();
			StringBuffer sql1 = new StringBuffer("SELECT c.ZapSt,e.Naziv from PTStupciVR a,PTVarijanteVR b,PTPostajeVarijantVR c,PTPostajeVR d,PTPostaje e where a.VarijantaVRID = b.ID and   c.VarijantaID = b.ID and   c.NodePostajeVRID = d.ID and   d.PostajaID = e.id and   a.id = ? ORDER BY 1");
			if (smjer.equals("-"))
				sql1.append(" DESC");
			PreparedStatement ps = con.prepareStatement(sql1.toString());
			ps.setInt(1, stupacID);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				nazivList.add(rs.getString(2));
				zapStList.add(rs.getInt(1));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	String[] getPostaje() {
		return nazivList.toArray(new String[0]);
	}

	public Integer getPostajaZapSt(int selectionIndex) {
		if (selectionIndex >= 0)
			return zapStList.get(selectionIndex);
		else
			return new Integer(-1);
	}
}
