package hr.mit.beans;

import hr.mit.utils.DbUtil;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PostajaList  {

	List<Postaja> postaje = new ArrayList<Postaja>();

	public PostajaList(Integer stupacID) {
		try {
			String sql0 = "SELECT smerVoznje FROM PTStupciVR WHERE ID = ?";
			PreparedStatement ps0 = DbUtil.getConnection().prepareStatement(sql0);
			ps0.setInt(1, stupacID);
			String smjer = DbUtil.getSingleResultString(ps0);
			ps0.close();
			StringBuffer sql1 = new StringBuffer(
					"SELECT c.ZapSt,e.Naziv,f.VremeOdhoda from PTStupciVR a,PTVarijanteVR b,PTPostajeVarijantVR c,PTPostajeVR d,PTPostaje e,PTCasiVoznjeVR f where a.VarijantaVRID = b.ID and   c.VarijantaID = b.ID and   c.NodePostajeVRID = d.ID and   d.PostajaID = e.id and   a.id = ? AND f.StupacVRID = a.ID AND f.NodePostajeVarijanteVRID = c.ID AND e.Prodaja = 'D' ORDER BY 1");
			if (smjer.equals("+"))
				sql1.append(" DESC");
			PreparedStatement ps = DbUtil.getConnection().prepareStatement(sql1.toString());
			ps.setInt(1, stupacID);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				Postaja p = new Postaja(rs.getInt(1), rs.getString(2));
				postaje.add(p);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public String[] getNewList() {
		ArrayList<String> x = new ArrayList<String>();
		for (Postaja p : postaje) {
			x.add(p.getNaziv());
		}
		return x.toArray(new String[0]);
	}
	
	public Integer getZapSt(Integer index) {
		return postaje.get(index).getZapSt();
	}

	public Postaja get(Integer index){
		return postaje.get(index);
	}
}
