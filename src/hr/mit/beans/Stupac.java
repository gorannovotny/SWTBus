package hr.mit.beans;

import hr.mit.utils.DbUtil;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Stupac {
	private static ArrayList<Stupac> stupacList = new ArrayList<Stupac>();

	private Integer id;
	private String  smjer;
	private Integer varijantaID;
	private String  opis;
	private String  vremeOdhoda;

	public static void setVozniRed(Integer sifraVR) {
		try {
			//String sql = "select a.ID,a.VozniRedID,a.VarijantaVRID,a.SmerVoznje,b.Opis1,b.Opis2,a.VremeOdhoda from PTStupciVR a,PTVarijanteVR b WHERE a.VarijantaVRID = b.id AND a.VozniRedID = ? AND a.id NOT IN (select StupacID from PTStupciVRMirovanja WHERE DATE('now') BETWEEN OdDatuma AND DoDatuma) ORDER BY a.VremeOdhoda";
		      java.util.Date Danas    = DbUtil.getNaDan();
	          String DVS = DbUtil.getDayOfWeekStr(Danas);
	          
	          String sql= "Select STP.ID,STP.VozniRedID,STP.VarijantaVRID,STP.SmerVoznje,VAR.Opis1,VAR.Opis2,STP.VremeOdhoda,DV.VoziOb"+ 
                        " from PTStupciVR STP"+
                        " inner join PTVarijanteVR VAR on STP.VarijantaVRID = VAR.id"+ 
                        " inner join PTDneviVoznje DV on DV.ID = STP.DneviVoznjeID"+
                        " WHERE STP.VozniRedID = ? "+//1
                        " AND DV.VoziOb like ? "+    //2
                        " AND Datetime(?) between STP.VeljaOd and STP.VeljaDo"+ //3
                        " AND STP.id NOT IN (select StupacID from PTStupciVRMirovanja WHERE Datetime(?) BETWEEN OdDatuma AND DoDatuma)"+ 
                        " ORDER BY STP.VremeOdhoda";		
			
			PreparedStatement ps = DbUtil.getConnection().prepareStatement(sql);
			ps.setInt(1,     sifraVR);
			ps.setString(2,  DVS);
			ps.setString(3,  DbUtil.JavaDateToSQLLiteDateStr(Danas)); 
			ps.setString(4,  DbUtil.JavaDateToSQLLiteDateStr(Danas)); 
			
			ResultSet rs = ps.executeQuery();
			stupacList.clear();
			while (rs.next()) {
				stupacList.add(new Stupac(rs.getInt("ID"), 
						                  rs.getInt("VozniRedID"),
						                  rs.getString("SmerVoznje"), 
						                  rs.getInt("VarijantaVRID"), 
						                  rs.getString("Opis1"), 
						                  rs.getString("Opis2"), 
						                  rs.getDouble("VremeOdhoda")));
			}
			rs.close();
			ps.close();
		} catch (SQLException e) {
			throw new RuntimeException(e);
//			e.printStackTrace();
		}
	}

	public Stupac(Integer id, Integer vozniRedID, String smjer, Integer varijantaID, String opis1, String opis2, double vremeOdhoda) {
		super();
		this.id          = id;
		this.smjer       = smjer;
		this.varijantaID = varijantaID;
		if (smjer.equals("-"))
			this.opis = opis1;
		else
			this.opis = opis2;
		this.vremeOdhoda = DbUtil.getHHMM(vremeOdhoda);
	}

	public static Stupac getByID(int id) {
		String sql = "select a.ID,a.VozniRedID,a.VarijantaVRID,a.SmerVoznje,b.Opis1,b.Opis2,a.VremeOdhoda from PTStupciVR a,PTVozniRedi b WHERE a.VozniRedID = b.id AND a.ID = ? ORDER BY a.VremeOdhoda";
		try {
			PreparedStatement ps = DbUtil.getConnection().prepareStatement(sql);
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
			 return new Stupac(rs.getInt("ID"), rs.getInt("VozniRedID"), rs.getString("SmerVoznje"), rs.getInt("VarijantaVRID"), rs.getString("Opis1"), rs.getString("Opis2"), rs
					.getDouble("VremeOdhoda"));
			} else return null;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

	}

	public static String[] getList() {
		String[] l = new String[stupacList.size()];
		int x = 0;
		for (Stupac v : stupacList) {
			l[x] = v.getVremeOdhoda() + " " + v.getOpis();
			x++;
		}
		return l;
	}

	public static List<String> getArrayList() {
		List<String> l = new ArrayList<String>();
		for (Stupac v : stupacList) {
			l.add(v.getVremeOdhoda() + " " + v.getOpis());
		}
		return l;
	}

	public String getVremeOdhoda() {
		return vremeOdhoda;
	}

	public static Stupac get(int index) {
		return stupacList.get(index);
	}

	public Integer getVarijantaID() {
		return varijantaID;
	}

	public Integer getId() {
		return id;
	}

	public String getOpis() {
		return opis;
	}

	public String getSmjer() {
		return smjer;
	}

	public String getSmjerOpis() {
		if (smjer.equals("-"))
			return "Odl.";
		else
			return "Dol.";
	}

}
