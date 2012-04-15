package hr.mit.beans;

import hr.mit.Starter;
import hr.mit.utils.DbUtil;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;

public class Obracun {

	private static ArrayList<Obracun> obracunList = new ArrayList<Obracun>();

	private Integer id;
	private String datum;
	private Vozac vozac;
	private String uuid;

	static {
		updateList();
	}
	
	private static void updateList(){
		try {
			String sql = "Select id,datum,vozacid,guid FROM PTKTObracun ORDER BY id DESC";
			ResultSet rs = DbUtil.getConnection2().createStatement().executeQuery(sql);
			obracunList.clear();
			while (rs.next()) {
				obracunList.add(new Obracun(rs.getInt("id"), rs.getString("datum"), rs.getInt("vozacID"), rs.getString("GUID")));
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	public Obracun(Integer id, String datum, Integer vozac, String uuid) {
		super();
		this.id = id;
		this.datum = datum;
		this.vozac = Vozac.getBySifra(vozac);
		this.uuid = uuid;
	}

	public static String[] getList() {
		updateList();
		String[] l = new String[obracunList.size()];
		int x = 0;
		for (Obracun v : obracunList) {
			l[x] = String.format("%5d %s", v.id, v.datum);
			x++;
		}
		return l;
	}

	public static Obracun get(int index) {
		return obracunList.get(index);
	}

	public Integer getId() {
		return id;
	}

	public static List<String> getArrayList() {
		List<String> l = new ArrayList<String>();
		updateList();
		for (Obracun v : obracunList) {
			l.add(String.format("%5d %s", v.id, v.datum));
		}
		return l;
	}

	public static boolean imaZaObracun() {
		boolean retval = false;
		try {
			ResultSet rsCnt = DbUtil.getConnection2().createStatement().executeQuery("SELECT COUNT(*) FROM PTKTProdaja WHERE obracunID IS NULL");
			if (rsCnt.next()) {
				Integer id = rsCnt.getInt(1);
				if (id.equals(0))  retval = false;
				else retval = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return retval;
	}

	public static void closeObracun() {
		String sql = "INSERT INTO PTKTObracun(Datum,VozacID,GUID) VALUES(datetime('now','localtime'),?,?)";
		Integer id;
		try {
			DbUtil.getConnection2().setAutoCommit(false);
			PreparedStatement ps = DbUtil.getConnection2().prepareStatement(sql, java.sql.Statement.RETURN_GENERATED_KEYS);
			ps.setInt(1, Starter.vozac.getId());
			ps.setString(2, java.util.UUID.randomUUID().toString());
			ps.execute();
			ResultSet rs = ps.getGeneratedKeys();
			if (rs.next())
				id = rs.getInt(1);
			else
				id = 0;
			ps = DbUtil.getConnection2().prepareStatement("UPDATE PTKTProdaja SET ObracunID = ? WHERE ObracunID IS NULL");
			ps.setInt(1, id);
			int rows = ps.executeUpdate();
			if (rows > 0)
				DbUtil.getConnection2().commit();
			else
				DbUtil.getConnection2().rollback();

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public static BigDecimal getSaldo() {
		BigDecimal saldo = BigDecimal.ZERO;
		try {
			PreparedStatement ps = DbUtil.getConnection2().prepareStatement("SELECT sum(cena) FROM PTKTProdaja WHERE ObracunID IS NULL");
			double ss = DbUtil.getSingleResultDouble(ps);
			saldo = new BigDecimal(ss);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return saldo.setScale(2, BigDecimal.ROUND_DOWN);
	}

	public static String getObracun(Integer id) {
		double ukupno = 0;
		String sql = "select stupacID,VoznaKartaID,COUNT(*) Komada,SUM(Cena) cena from ptktprodaja";
		StringBuffer retval;
		PreparedStatement ps;
		try {
			if (id != null) {
				sql = sql + " WHERE obracunID = ? GROUP BY 1,2 ORDER BY 1,2";
				ps = DbUtil.getConnection2().prepareStatement(sql);
				ps.setInt(1, id);
			} else {
				sql = sql + " WHERE id IS NULL GROUP BY 1,2 ORDER BY 1,2";
				ps = DbUtil.getConnection2().prepareStatement(sql);
			}
			retval = new StringBuffer();
			ResultSet rs = ps.executeQuery();
			Integer stupacID = 0;
			while (rs.next()) {
				if (stupacID != rs.getInt("stupacID")){
					stupacID = rs.getInt("stupacID");
					retval.append(Stupac.getByID(stupacID).getOpis() + " ("+stupacID.toString() + ")\n");
					retval.append("--------------------------------\n");
				}
//				retval.append("Tip                  Kom  Ukupno\n");
//				retval.append("--------------------------------\n");
				int kartaId = rs.getInt("VoznaKartaID");
				String opis = Karta.getByID(kartaId).getNaziv();
				int komada = rs.getInt("Komada");
				double cena = rs.getDouble("cena");
				retval.append(String.format("%-20s %3d %7.2f\n", opis, komada, cena));
				if (kartaId != Karta.ZAMJENSKA_KARTA) {
					ukupno = ukupno + cena;
				}
			}
			retval.append("--------------------------------\n");
			retval.append(String.format("%-24s %7.2f", "Blagajna", ukupno));
		} catch (SQLException e) {
			throw new RuntimeException(e);

		}
		return retval.toString();
	}

}
