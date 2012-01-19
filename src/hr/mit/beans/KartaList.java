package hr.mit.beans;

import hr.mit.utils.DbUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class KartaList {
	ArrayList<String> nazivList = new ArrayList<String>();
	ArrayList<Integer> idList = new ArrayList<Integer>();
	Connection con;
	PreparedStatement ps;

	public KartaList() {
		this.con = DbUtil.getConnection();
		try {
			String sql = "Select id,Opis,* from PTKTVozneKarte WHERE PTKTVozneKarte.MobilnaProdaja = 1";
			ps = con.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				nazivList.add(rs.getString(2));
				idList.add(rs.getInt(1));
			}
			rs.close();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public String[] getList() {
		return nazivList.toArray(new String[0]);
	}

	public Integer getID(int selectionIndex) {
		if (selectionIndex >= 0) {
			return idList.get(selectionIndex);
		} else
			return new Integer(-1);
	}

}
