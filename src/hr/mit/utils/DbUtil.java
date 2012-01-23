package hr.mit.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DbUtil {
	static Connection con;

	public static Connection getConnection() {
		if (con == null) {
			try {
				Class.forName("org.sqlite.JDBC");
				con = DriverManager.getConnection("jdbc:sqlite:/home/goran/workspace/SWTBus/test.db");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return con;
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
