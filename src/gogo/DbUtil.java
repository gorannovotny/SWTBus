package gogo;

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
				con = DriverManager.getConnection("jdbc:sqlite:sqlite.db");
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

}
