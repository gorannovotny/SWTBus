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
			String sql = "SELECT id,vremeOdhoda FROM PTStupciVR WHERE VozniRedID = ? ORDER BY 2";
			ps = con.prepareStatement(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public String[] getList(Integer vozniRedID) throws SQLException {
		nazivList.clear();
		idList.clear();
		ps.setInt(1, vozniRedID);
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			nazivList.add(DbUtil.getHHMM(rs.getDouble(2)));
			idList.add(rs.getInt(1));
		}
		rs.close();
		return nazivList.toArray(new String[0]);
	}

	public Integer getPolazakID(int selectionIndex) {
		if (selectionIndex >= 0) {
			return idList.get(selectionIndex);
		} else
			return new Integer(-1);
	}

}
