package hr.mit.beans;

import hr.mit.utils.DbUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class VozniRed {
	private static ArrayList<VozniRed> vrList = new ArrayList<VozniRed>();

	private Integer id;
	private String opis;
	
	static {
		try {
			String sql = "SELECT b.ID,b.Opis1 FROM PTVozniRedi a,PTVarijanteVR b WHERE b.VozniRedID = a.id AND a.VeljaDo > DATETIME('now') AND vrstaVR = 1 ORDER BY 2";
			ResultSet rs = DbUtil.getConnection().createStatement().executeQuery(sql);
			while (rs.next()) {
				vrList.add(new VozniRed(rs.getInt(1),rs.getString(2)));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	
	public VozniRed(Integer id,String opis) {
		this.id = id;
		this.opis = opis;
	}
	
	public Integer getId() {
		return id;
	}
	
	public String getOpis(){
		return opis;
	}
	

	public static String[] getList() {
		String[] l = new String[vrList.size()];
		int x = 0;
		for (VozniRed v : vrList) {
			l[x] = v.getId() + " " + v.getOpis();
			x++;
		}
		return l;
	}

	public static List<String> getArrayList() {
		List<String> l = new ArrayList<String>();
		for (VozniRed v : vrList) {
			l.add(v.getId() + " " + v.getOpis());
		}
		return l;
	}
	
	public static VozniRed getByID(int id) {
		for (VozniRed v : vrList) {
			if (v.getId().equals(id))
				return v;
		}
		return null;
	}
	
	
	public static VozniRed get(int index) {
		return vrList.get(index);
	}
	/*
SELECT a.ID,d.ZapSt,g.ZapSt,f.Naziv,i.Naziv,CASE WHEN a.SmerVoznje = '-' THEN c.Opis1 ELSE c.Opis2 END Opis
FROM PTStupciVR a
LEFT JOIN PTStupciVRMirovanja b ON b.StupacID = a.ID AND DATETIME('now') BETWEEN b.OdDatuma AND b.DoDatuma  
INNER JOIN PTVarijanteVR c ON c.ID = a.VarijantaVRID

INNER JOIN PTPostajeVarijantVR d ON d.VarijantaID = c.ID
INNER JOIN PTPostajeVR e ON e.ID = d.NodePostajeVRID
INNER JOIN PTPostaje f ON f.ID = e.PostajaID  

INNER JOIN PTPostajeVarijantVR g ON g.VarijantaID = c.ID
INNER JOIN PTPostajeVR h ON h.ID = g.NodePostajeVRID
INNER JOIN PTPostaje i ON i.ID = h.PostajaID  


WHERE b.ID IS null
AND  DATETIME('now') BETWEEN a.VeljaOd AND a.VeljaDo
AND f.Naziv = 'Varaždin' 
AND i.Naziv = 'Turčin'
AND 
((d.ZapSt < g.ZapSt AND a.SmerVoznje = '-') OR (d.ZapSt > g.ZapSt AND a.SmerVoznje = '+') ) 
group by 1
	 */
}
