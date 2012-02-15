package hr.mit.beans;

import hr.mit.utils.DbUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;

public class Vozilo {
	static ArrayList<String> nazivList = new ArrayList<String>();
	static ArrayList<Integer> idList = new ArrayList<Integer>();
	
	private Integer id;
	
	static {
		try {
			String sql = "SELECT Sifra,Naziv,RegSt FROM PromVozila ORDER BY Sifra";
			ResultSet rs = DbUtil.getConnection().createStatement().executeQuery(sql);
			while (rs.next()) {
				nazivList.add(rs.getString(2)+" ("+rs.getString(3)+")");
				idList.add(rs.getInt(1));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	public Vozilo(Integer id) {
		this.id = id;
		
	}

	public static String[] getList() {
		return nazivList.toArray(new String[0]);
	}


	public static Integer getID(int selectionIndex) {
		if (selectionIndex >= 0)
			return idList.get(selectionIndex);
		else
			return new Integer(-1);
	}
	
	public static String getNaziv(Integer sifra) {
		int i = idList.indexOf(sifra);
		if (i != -1) 
		    return nazivList.get(i);
		else return "";
	}


}
