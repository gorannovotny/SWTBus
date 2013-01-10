package hr.mit.beans;

import hr.mit.utils.DbUtil;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.widgets.Text;

public class Stupac {
	private static ArrayList<Stupac> stupacList = new ArrayList<Stupac>();

	private Integer id;
	private Integer vozniRedID;
	private String smjer;
	private Integer varijantaID;
	private String opis;
	private String vremeOdhoda;

	public Stupac(Integer id, Integer vozniRedID, String smjer, Integer varijantaID, String opis1, String opis2, double vremeOdhoda) {
		super();
		this.id = id;
		this.vozniRedID = vozniRedID;
		this.smjer = smjer;
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
				return new Stupac(rs.getInt("ID"), rs.getInt("VozniRedID"), rs.getString("SmerVoznje"), rs.getInt("VarijantaVRID"), rs.getString("Opis1"), rs.getString("Opis2"), rs.getDouble("VremeOdhoda"));
			} else
				return null;
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

	public String getDescription() {
		return vremeOdhoda + " " + opis;
	}

	public static Stupac get(int index) {
		return stupacList.get(index);
	}

	public Integer getVarijantaID() {
		return varijantaID;
	}

	public Integer getVozniRedID() {
		return vozniRedID;
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

	public static void setupFinder(Text t1, Text t2, float FlPolazak) {
		try {
			stupacList.clear();
			Postaja OdPostaje;
			Postaja DoPostaje;

			java.util.Date Danas = DbUtil.getNaDan();
			String DT = DbUtil.getDayOfWeekStr(Danas);
			OdPostaje = Postaja.getByNaziv(t1.getText());
			DoPostaje = Postaja.getByNaziv(t2.getText());

			if ((OdPostaje == null) || (DoPostaje == null))
				return;

			t1.setText(OdPostaje.getNaziv());
			t2.setText(DoPostaje.getNaziv());
			String sql = "select distinct VR.ID, CASE WHEN STP.SmerVoznje='-' THEN VARVR.Opis1 WHEN STP.SmerVoznje='+' THEN VARVR.Opis2 END as Relacija" + ", STP.VremeOdhoda, STP.ID as StupacID, STP.SmerVoznje,STP.VarijantaVRID" + // , PRV.Naziv as Prevoznik"+
					" from PTStupciVR STP" + " inner Join PTVozniRedi VR ON VR.ID=STP.VozniRedID and Datetime(?) between VR.VeljaOd  and VR.VeljaDo" + " left  join PTStupciVRMirovanja MIR ON MIR.StupacID=STP.ID and Datetime(?) between MIR.OdDatuma and MIR.DoDatuma" + " left  join PTPrevozniki PRV ON PRV.ID=STP.PrevoznikID" + " inner join PTVarijanteVR VARVR ON VARVR.ID=STP.VarijantaVRID" + " inner join PTDneviVoznje DV on DV.ID = STP.DneviVoznjeID" + " where Datetime(?) between STP.VeljaOd and STP.VeljaDo" + " and VR.Firma=? " + // 4. param
					" and DV.VoziOb like ?" + // 5. param uvazavamo dane voznje, pojednostavljeno
					" and PRV.Sifra=?" + // 6. prevoznik je AP d.d.
					" and STP.VremeOdhoda>= ?" + // 7. param vreme odhoda
					" and MIR.ID is null" + // ne smije imati aktivno mirovanje na dan
					" and VR.VrstaVR=1" + // samo valjani vozni redi "1"
					" and VR.KategorijaPrevoza <> 50" + // međunarodni su isključeni za lokal = kategorija "50"
					" and CASE WHEN STP.SmerVoznje='-' THEN " + " VR.ID in (select distinct PVR1.VozniRedID from PTPostajeVR PVR1" + // obje postaje moraju biti prisutne u postajama voznog reda
					" INNER JOIN PTPostajeVR PVR2 ON PVR2.VozniRedID=PVR1.VozniRedID and PVR2.PostajaID=?" + // 8.param
					" where  PVR1.PostajaID=?" + // 8.param postajaOd mora biti manja dopostaje
					" and PVR1.zapst<PVR2.zapst" + ")" + // subselect
					" WHEN STP.SmerVoznje='+' THEN " + " VR.ID in (select distinct PVR1.VozniRedID from PTPostajeVR PVR1 " + // obje postaje moraju biti prisutne u postajama voznog reda
					" INNER JOIN PTPostajeVR PVR2 ON PVR2.VozniRedID=PVR1.VozniRedID and PVR2.PostajaID=?" + // 10.param
					" where  PVR1.PostajaID=?" + // 11.param postajaOd mora biti manja dopostaje
					" and PVR1.zapst>PVR2.zapst" + ")" + // subselect
					" END" + // case
					" order by STP.VremeOdhoda";

			/*
			 * " and VR.ID in (select distinct PVR1.VozniRedID from PTPostajeVR PVR1 "+ // obje postaje moraju biti prisutne u postajama voznog reda " INNER JOIN PTPostajeVR PVR2 ON PVR2.VozniRedID=PVR1.VozniRedID and PVR2.PostajaID=?"+ // 2.param " where  PVR1.PostajaID=?)"+ // 3.param postajaOd mora biti manja dopostaje " order by STP.VremeOdhoda";
			 */

			PreparedStatement ps = DbUtil.getConnection().prepareStatement(sql);
			// ps.setString(1, "2012-05-17 00:00:00"); // radi
			ps.setString(1, DbUtil.JavaDateToSQLLiteDateStr(Danas));
			ps.setString(2, DbUtil.JavaDateToSQLLiteDateStr(Danas));
			ps.setString(3, DbUtil.JavaDateToSQLLiteDateStr(Danas));
			ps.setInt(4, DbUtil.getFirma());
			ps.setString(5, DT);
			ps.setInt(6, DbUtil.getPrevoznikSifra());
			ps.setFloat(7, FlPolazak);

			ps.setInt(8, DoPostaje.getId());
			ps.setInt(9, OdPostaje.getId());
			ps.setInt(10, DoPostaje.getId());
			ps.setInt(11, OdPostaje.getId());

			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				stupacList.add(new Stupac(rs.getInt("StupacID"), rs.getInt("ID"), rs.getString("SmerVoznje"), rs.getInt("VarijantaVRID"), rs.getString("Relacija"), rs.getString("Relacija"), rs.getDouble("VremeOdhoda")));
			}
			rs.close();
			ps.close();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public static Integer getIndex(Stupac st) {
		Integer x = 0;
		for (Stupac v : stupacList) {
			if (v.getId().equals(st.getId()))
				break;
			x++;
		}
		return x;
	}

}
