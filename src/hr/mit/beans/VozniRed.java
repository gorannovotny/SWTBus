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
	private String opis;

	static {
		try {
			String sql = "SELECT a.ID,a.Opis1 FROM PTVozniRedi a WHERE VeljaDo > DATETIME('now') AND vrstaVR = 1 ORDER BY 2";
			ResultSet rs = DbUtil.getConnection().createStatement().executeQuery(sql);
			while (rs.next()) {
				vrList.add(new VozniRed(rs.getInt(1), rs.getString(2)));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public VozniRed(Integer id, String opis) {
		this.id = id;
		this.opis = opis;
	}

	public Integer getId() {
		return id;
	}

	public String getOpis() {
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

	public static void setupFinder(String pos1, String pos2) {
		String sql = "SELECT a.ID,a.Opis1 FROM PTVozniRedi a INNER JOIN PTStupciVR b ON b.VozniRedID = a.ID AND DATETIME('now') BETWEEN b.VeljaOd AND b.VeljaDo LEFT JOIN PTSTUPCIVRMIROVANJA c ON c.StupacID = b.ID  AND DATETIME('now') BETWEEN c.OdDatuma AND c.DoDatuma INNER JOIN PTPOSTAJEVR d ON d.VozniRedID = a.ID INNER JOIN PTPOSTAJE e on e.ID = d.PostajaID INNER JOIN PTPOSTAJEVR f ON f.VozniRedID = a.ID INNER JOIN PTPOSTAJE g on g.ID = f.PostajaID  WHERE UPPER(e.Naziv) LIKE ? AND UPPER(g.Naziv) LIKE ? AND c.id IS NULL GROUP BY 1,2";

		try {
			PreparedStatement ps = DbUtil.getConnection().prepareStatement(sql);
			ps.setString(1, pos1 + "%");
			ps.setString(2, pos2 + "%");
			ResultSet rs = ps.executeQuery();
			vrList.clear();
			while (rs.next()) {
				vrList.add(new VozniRed(rs.getInt(1), rs.getString(2)));
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

	}
}
