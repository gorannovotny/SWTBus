package hr.mit.utils;

import hr.mit.beans.Vozac;
import hr.mit.beans.VozniRed;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class DbUtil {
	static Connection con;
	static Connection con2;

	public static int getFirma() {
		return 5;
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
	public static String getVersionInfo(){		
		return  "2012/06.01";
	}

	public static String getDbVersionInfo() {
		String  StrInfo="??";
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
	
	
	//***** to bu promjenjiva iz kalendar pickera !
	public static java.util.Date getNaDan(){
		java.util.Date Danas = new java.util.Date(); ;
		return  Danas;
	}
	
	public static java.sql.Date JavaDateToSqlDate(java.util.Date utilDate) {
		return new java.sql.Date(utilDate.getTime());
		}


	public static String JavaDateToSQLLiteDateStr(Date DanUra) 
	{
//		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
		String DatumStr = df.format(DanUra);
		return (DatumStr);
	}
	
	
	public static String getDayOfWeekStr(java.util.Date Danas) {
        Calendar c = Calendar.getInstance();
        c.setTime(Danas);
        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
        String DT = "";
       //** pripremimo datum tjedna *** 
       switch (dayOfWeek) {
          case 1: DT = "%7%";
          		  break;
          case 2: DT = "%1%"; 
          	      break;
          case 3: DT = "%2%"; 
          		  break;
          case 4: DT = "%3%"; 
          		  break;
          case 5: DT = "%4%"; 
          		  break;
          case 6: DT = "%5%"; 
          		  break;
          case 7: DT = "%6%"; 
       }
	 return DT;	
	}
	
	
	public static String getHHMM(Double time) {
		Integer hours = (int) (time * 24);
		Integer mins = (int) ((time * 24 - hours)*60);
		return String.format("%02d", hours) + ":" + String.format("%02d", mins);
	}
}
