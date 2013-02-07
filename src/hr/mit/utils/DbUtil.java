package hr.mit.utils;

import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DbUtil {
	static Connection con;
	static Connection con2;

	public static int getFirma() {
		return 5;
	}
	
	public static int getPrevoznikSifra() {
		return 1; // AP d.d. vž
	}

	public static int getPGRID() {
		return 0;
	}

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


	public static String getDbVersionInfo() {
		String StrInfo = "??";
		try {
			String sql = "SELECT VerzijaDateTime from PTVersion order by 1 desc limit 1";
			ResultSet rs = DbUtil.getConnection().createStatement().executeQuery(sql);
			while (rs.next()) {
				StrInfo = rs.getString(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return StrInfo;
	}

	// ***** to bu promjenjiva iz kalendar pickera !
	public static java.util.Date getNaDan() {
		java.util.Date Danas = new java.util.Date();
		;
		return Danas;
	}

	public static java.sql.Date JavaDateToSqlDate(java.util.Date utilDate) {
		return new java.sql.Date(utilDate.getTime());
	}

	public static String JavaDateToSQLLiteDateStr(Date DanUra) {
		// DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
		String DatumStr = df.format(DanUra);
		return (DatumStr);
	}

	public static String getDayOfWeekStr(java.util.Date Danas) {
		Calendar c = Calendar.getInstance();
		c.setTime(Danas);
		int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
		String DT = "";
		// ** pripremimo datum tjedna ***
		switch (dayOfWeek) {
		case 1:
			DT = "%7%";
			break;
		case 2:
			DT = "%1%";
			break;
		case 3:
			DT = "%2%";
			break;
		case 4:
			DT = "%3%";
			break;
		case 5:
			DT = "%4%";
			break;
		case 6:
			DT = "%5%";
			break;
		case 7:
			DT = "%6%";
		}
		return DT;
	}

	public static String getHHMM(Double time) {
		time = time + 0.00001157407407; 
		Integer hours = (int) (time * 24);
		Integer mins = (int) ((time * 24 - hours) * 60);
		return String.format("%02d", hours) + ":" + String.format("%02d", mins);
	}

	public static String md5hash(String text) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(text.getBytes("CP1250"));
			return bytesToHex(md.digest());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private static String bytesToHex(byte[] b) {
		char hexDigit[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
		StringBuffer buf = new StringBuffer();
		for (int j = 0; j < b.length; j++) {
			buf.append(hexDigit[(b[j] >> 4) & 0x0f]);
			buf.append(hexDigit[b[j] & 0x0f]);
		}
		return buf.toString();
	}
	
	
	public  static boolean checkIfNumber(String in) {
	        try {
	            Integer.parseInt(in);
	        } catch (NumberFormatException ex) {
	            return false;
	        }
	        return true;
	    }
	   
	public static String getVersionInfo() {
		return "2013/02.07";
	}
	

}
