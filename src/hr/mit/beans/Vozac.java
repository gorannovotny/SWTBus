package hr.mit.beans;

import hr.mit.utils.DbUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Vozac {
	static ArrayList<String> nazivList = new ArrayList<String>();
	static ArrayList<Integer> idList = new ArrayList<Integer>();
	
	private Integer id;
	
	static {
		try {
			String sql = "SELECT id,Naziv FROM PTVozaci";
			ResultSet rs = DbUtil.getConnection().createStatement().executeQuery(sql);
			while (rs.next()) {
				nazivList.add(rs.getString(2));
				idList.add(rs.getInt(1));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	public Vozac(Integer id) {
		this.id = id;
		
	}

	public static String[] getList() {
		return nazivList.toArray(new String[0]);
	}


	public static Integer getVozacID(int selectionIndex) {
		if (selectionIndex >= 0)
			return idList.get(selectionIndex);
		else
			return new Integer(-1);
	}
	
	public String getNaziv() {
		return  nazivList.get(idList.indexOf(id));
	}
}
