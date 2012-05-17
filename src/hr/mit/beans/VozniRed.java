package hr.mit.beans;

import hr.mit.utils.DbUtil;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class VozniRed {
	private static ArrayList<VozniRed> vrList = new ArrayList<VozniRed>();

	private Integer id;
	private String  polazak;
	private String  opis;
	private Integer stupacId;

	static {
		try {
			String sql = "SELECT a.ID,a.Opis1 FROM PTVozniRedi a WHERE VeljaDo > DATETIME('now') AND vrstaVR = 1 ORDER BY 2";
			ResultSet rs = DbUtil.getConnection().createStatement().executeQuery(sql);
			while (rs.next()) {
				vrList.add(new VozniRed(rs.getInt(1), rs.getString(2),"",0));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public VozniRed(Integer id, String opis, String polazak, Integer stupacId) {
		this.id       = id;
		this.opis     = opis;
		this.polazak  = polazak;
		this.stupacId = stupacId;
	}

	public Integer getId() {
		return id;
	}

	public Integer getStupacId() {
		return stupacId;
	}

	public String getOpis() {
		return opis;
	}

	public String getPolazak() {
		return polazak;
	}

	public static String[] getList() {
		String[] l = new String[vrList.size()];
		int x = 0;
		for (VozniRed v : vrList) {
			l[x] = v.getPolazak() + " " + v.getOpis();
			x++;
		}
		return l;
	}

	public static List<String> getArrayList() {
		List<String> l = new ArrayList<String>();
		for (VozniRed v : vrList) {
			l.add(v.getPolazak() + " " + v.getOpis());
		}
		return l;
	}

	public static VozniRed getByID(int id) {
		for (VozniRed v : vrList) {
			if (v.getPolazak().equals(id))
				return v;
		}
		return null;
	}

	public static VozniRed get(int index) {
		if (index < vrList.size())
		return vrList.get(index);
		else return null;
	}
/*
	public static void setupFinder(String pos1, String pos2) {
		// String sql =
		// "SELECT a.ID,a.Opis1 FROM PTVozniRedi a INNER JOIN PTStupciVR b ON b.VozniRedID = a.ID AND DATETIME('now') BETWEEN b.VeljaOd AND b.VeljaDo LEFT JOIN PTSTUPCIVRMIROVANJA c ON c.StupacID = b.ID  AND DATETIME('now') BETWEEN c.OdDatuma AND c.DoDatuma INNER JOIN PTPOSTAJEVR d ON d.VozniRedID = a.ID INNER JOIN PTPOSTAJE e on e.ID = d.PostajaID INNER JOIN PTPOSTAJEVR f ON f.VozniRedID = a.ID INNER JOIN PTPOSTAJE g on g.ID = f.PostajaID  WHERE UPPER(e.Naziv) LIKE ? AND UPPER(g.Naziv) LIKE ? AND c.id IS NULL GROUP BY 1,2";
		String sql = "SELECT a.ID,a.Opis1 FROM PTVozniRedi a INNER JOIN PTStupciVR b ON b.VozniRedID = a.ID AND DATETIME('now') BETWEEN b.VeljaOd AND b.VeljaDo LEFT JOIN PTSTUPCIVRMIROVANJA c ON c.StupacID = b.ID  AND DATETIME('now') BETWEEN c.OdDatuma AND c.DoDatuma WHERE a.Opis1 LIKE ? AND a.Opis1 LIKE ? AND c.id IS NULL GROUP BY 1,2";

		try {
			PreparedStatement ps = DbUtil.getConnection().prepareStatement(sql);
			ps.setString(1, "%" + pos1 + "%");
			ps.setString(2, "%" + pos2 + "%");
			ResultSet rs = ps.executeQuery();
			vrList.clear();
			while (rs.next()) {
				vrList.add(new VozniRed(rs.getInt(1), rs.getString(2)));
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
*/
	public static void setupFinder(String pos1, String pos2) {
            
    	try {
    		vrList.clear();
            int OdPostajeID = 0;
            int DoPostajeID = 0;

            java.util.Date Danas    = DbUtil.getNaDan();
            String DT = DbUtil.getDayOfWeekStr(Danas);
//            java.sql.Date  SqlNaDan = DbUtil.JavaDateTimeToSqlDateTime(Danas);
//          java.util.Date Danas = new java.util.Date();
//          java.sql.Date  SqlNaDan = new java.sql.Date(Danas.getTime());
            pos1= pos1.replace('Ž','_');
            pos1= pos1.replace('Š','_');
            pos1= pos1.replace('Č','_');
            pos1= pos1.replace('Ć','_');
            pos1= pos1.replace('Đ','_');

            pos2= pos2.replace('Ž','_');
            pos2= pos2.replace('Š','_');
            pos2= pos2.replace('Č','_');
            pos2= pos2.replace('Ć','_');
            pos2= pos2.replace('Đ','_');
            // najdemo za postaju 1 najblizega
		    String sql = "Select ID from PTPostaje WHERE Upper(Naziv) like Upper(?) and PGrid=? order by Naziv limit 2"; 
			PreparedStatement ps = DbUtil.getConnection().prepareStatement(sql);
			ps.setString(1,pos1 + "%");
			ps.setInt(2,DbUtil.getPGRID());
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				OdPostajeID = rs.getInt(1);
				break;
			}
		    sql = "Select ID from PTPostaje WHERE Upper(Naziv) like Upper(?) and PGrid=? order by Naziv limit 2"; 
			ps = DbUtil.getConnection().prepareStatement(sql);
			ps.setString(1,pos2 + "%");
			ps.setInt(2,DbUtil.getPGRID());
			rs = ps.executeQuery();
			while (rs.next()) {
				DoPostajeID = rs.getInt(1);
				break;
			}
			if ((OdPostajeID == 0) && (DoPostajeID==0)) 
				 return; 
		
        sql="select distinct VR.ID, CASE WHEN STP.SmerVoznje='-' THEN VARVR.Opis1 WHEN STP.SmerVoznje='+' THEN VARVR.Opis2 END as Relacija" +
        	", STP.VremeOdhoda, STP.ID as StupacID"+ // , PRV.Naziv as Prevoznik"+ 
            " from PTStupciVR STP"+ 
 	        " inner Join PTVozniRedi VR ON VR.ID=STP.VozniRedID and Datetime(?) between VR.VeljaOd  and VR.VeljaDo"+
	        " left  join PTStupciVRMirovanja MIR ON MIR.StupacID=STP.ID and Datetime(?) between MIR.OdDatuma and MIR.DoDatuma"+
            " left  join PTPrevozniki PRV ON PRV.ID=STP.PrevoznikID"+ 
	        " inner join PTVarijanteVR VARVR ON VARVR.ID=STP.VarijantaVRID"+ 
	        " inner join PTDneviVoznje DV on DV.ID = STP.DneviVoznjeID"+
            " where Datetime(?) between STP.VeljaOd and STP.VeljaDo"+ 
	        " and VR.Firma=? "+ //4. param
            " and MIR.ID is null"+   // ne smije imati aktivno mirovanje
            " and VR.VrstaVR=1"+     // samo valjani vozni redi
            " and VR.ID in (select distinct PVR1.VozniRedID from PTPostajeVR PVR1 "+ // obje postaje moraju biti prisutne u postajama voznog reda
              " INNER JOIN PTPostajeVR PVR2 ON PVR2.VozniRedID=PVR1.VozniRedID and PVR2.PostajaID=?"+ // 2.param
              " where  PVR1.PostajaID=?)"+       // 3.param postajaOd mora biti manja dopostaje
            " and DV.VoziOb like ?"+             // uvazavamo dane voznje, pojednostavljeno
            " order by STP.VremeOdhoda";
	
        
			ps = DbUtil.getConnection().prepareStatement(sql);
//			ps.setString(1,  "2012-05-17 00:00:00"); // radi 
			ps.setString(1,  DbUtil.JavaDateToSQLLiteDateStr(Danas)); 
			ps.setString(2,  DbUtil.JavaDateToSQLLiteDateStr(Danas)); 
			ps.setString(3,  DbUtil.JavaDateToSQLLiteDateStr(Danas)); 
			ps.setInt(4,     DbUtil.getFirma());
			ps.setInt(5,     DoPostajeID);
			ps.setInt(6,     OdPostajeID);
			ps.setString(7,  DT);
			
			rs = ps.executeQuery();
			while (rs.next()) {
				vrList.add(new VozniRed(rs.getInt(1), rs.getString(2), DbUtil.getHHMM(rs.getDouble(3)), rs.getInt(4) ));
			}
			rs.close();
			ps.close();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	
	
}
