package hr.mit.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DbUtil {
	static Connection con;
	static Connection con2;

	public static Connection getConnection() {
		if (con == null) {
			try {
				Class.forName("org.sqlite.JDBC");
				con = DriverManager.getConnection("jdbc:sqlite:baza.db");
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		return con;
	}

	public static Connection getConnection2() {
		if (con2 == null) {
			try {
				Class.forName("org.sqlite.JDBC");
				con2 = DriverManager.getConnection("jdbc:sqlite:prodaja.db");
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		return con2;
	}

	public static Integer getSingleResult(PreparedStatement ps) throws SQLException {
		Integer retval;
		ResultSet rs = ps.executeQuery();
		if (rs.next())
			retval = rs.getInt(1);
		else
			retval = null;
		rs.close();
		return retval;
	}

	public static String getSingleResultString(PreparedStatement ps) throws SQLException {
		String retval;
		ResultSet rs = ps.executeQuery();
		if (rs.next())
			retval = rs.getString(1);
		else
			retval = null;
		rs.close();
		return retval;
	}

	public static Double getSingleResultDouble(PreparedStatement ps) throws SQLException {
		Double retval;
		ResultSet rs = ps.executeQuery();
		if (rs.next())
			retval = rs.getDouble(1);
		else
			retval = null;
		rs.close();
		return retval;
	}


	
	public static String getHHMM(Double time) {
		Integer hours = (int) (time * 24);
		Integer mins = (int) ((time * 24 - hours)*60);
		return String.format("%02d", hours) + ":" + String.format("%02d", mins);
	}
}
