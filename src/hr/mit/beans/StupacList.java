package hr.mit.beans;

import hr.mit.utils.DbUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class StupacList {
	ArrayList<String> nazivList = new ArrayList<String>();
	ArrayList<Integer> idList = new ArrayList<Integer>();
	Connection con;
	PreparedStatement ps;

	public StupacList(Connection con) {
		this.con = con;
		try {
			String sql = "SELECT a.id,a.vremeOdhoda FROM PTStupciVR a,PTVozniRedi b WHERE b.Sifra = ? AND a.VozniRedID = b.ID AND b.VrstaVR = 1 ORDER BY 2";
			ps = con.prepareStatement(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public String[] getList(String SifraVR) {
		nazivList.clear();
		idList.clear();
		try {
			ps.setString(1, SifraVR);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				nazivList.add(DbUtil.getHHMM(rs.getDouble(2)));
				idList.add(rs.getInt(1));
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return nazivList.toArray(new String[0]);
	}

	public Integer getPolazakID(int selectionIndex) {
		if (selectionIndex >= 0) {
			return idList.get(selectionIndex);
		} else
			return new Integer(-1);
	}

}
