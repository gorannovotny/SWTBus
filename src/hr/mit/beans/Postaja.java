package hr.mit.beans;

import hr.mit.utils.DbUtil;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Postaja {

	private static ArrayList<Postaja> postajaList = new ArrayList<Postaja>();

	private Integer zapSt;
	private String naziv;
	private String vremeOdhoda;
	private String vremePrihoda;
	private Integer id;

	public static void setStupac(Stupac stupac) {
		String sql = "SELECT c.ZapSt,e.Naziv,f.VremeOdhoda,f.VremePrihoda,e.ID from PTStupciVR a,PTVarijanteVR b,PTPostajeVarijantVR c,PTPostajeVR d,PTPostaje e,PTCasiVoznjeVR f where a.VarijantaVRID = b.ID and   c.VarijantaID = b.ID and   c.NodePostajeVRID = d.ID and   d.PostajaID = e.id and   a.id = ? AND f.StupacVRID = a.ID AND f.NodePostajeVarijanteVRID = c.ID AND e.Prodaja = 'D' ORDER BY 1";
		if (stupac.getSmjer().equals("+"))
			sql = sql + " DESC";
		try {
			PreparedStatement ps = DbUtil.getConnection().prepareStatement(sql);
			ps.setInt(1, stupac.getId());
			ResultSet rs = ps.executeQuery();
			postajaList.clear();
			while (rs.next()) {
				postajaList.add(new Postaja(rs.getInt("ZapSt"), rs.getString("Naziv"), rs.getDouble("VremeOdhoda"), rs.getDouble("VremePrihoda"), rs.getInt("id")));
			}
			rs.close();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static String[] getList() {
		String[] l = new String[postajaList.size()];
		int x = 0;
		for (Postaja v : postajaList) {
			l[x] = v.getNaziv() + " " + v.getVremeOdhoda();
			x++;
		}
		return l;
	}

	public static Postaja getByID(Integer id) {
		String sql = "select a.ID,a.Naziv from PTPostaje a WHERE a.ID = ?";
		try {
			PreparedStatement ps = DbUtil.getConnection().prepareStatement(sql);
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				return new Postaja(id, rs.getString(2), 0.0, 0.0, id);
			} else
				return null;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public static Postaja getByNaziv(String naziv) {
		String pos1 = naziv;

		pos1 = pos1.replace('Ž', '_');
		pos1 = pos1.replace('Š', '_');
		pos1 = pos1.replace('Č', '_');
		pos1 = pos1.replace('Ć', '_');
		pos1 = pos1.replace('Đ', '_');

		String sql = "select a.ID,a.Naziv from PTPostaje a WHERE Upper(Naziv) like Upper(?) and PGrid=? order by Naziv limit 1";
		try {
			PreparedStatement ps = DbUtil.getConnection().prepareStatement(sql);
			ps.setString(1, pos1 + "%");
			ps.setInt(2, DbUtil.getPGRID());
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				return new Postaja(rs.getInt(1), rs.getString(2), 0.0, 0.0, rs.getInt(1));
			} else
				return null;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public static Postaja get(int index) {
		return postajaList.get(index);
	}

	public static List<String> getArrayList() {
		List<String> l = new ArrayList<String>();
		for (Postaja v : postajaList) {
			l.add(v.getNaziv() + " " + v.getVremeOdhoda());
		}
		return l;
	}

	public Postaja(Integer zapSt, String naziv, double vOdhoda, double vPrihoda, Integer id) {
		this.zapSt = zapSt;
		this.naziv = naziv;
		this.vremeOdhoda = DbUtil.getHHMM(vOdhoda);
		this.vremePrihoda = DbUtil.getHHMM(vPrihoda);
		this.id = id;
	}

	public Integer getZapSt() {
		return zapSt;
	}

	public String getNaziv() {
		return naziv;
	}

	public String getVremePrihoda() {
		return vremePrihoda;
	}

	public String getVremeOdhoda() {
		return vremeOdhoda;
	}

	public Integer getId() {
		return id;
	}
}
