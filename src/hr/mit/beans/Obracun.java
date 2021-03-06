package hr.mit.beans;

import hr.mit.Starter;
import hr.mit.utils.DbUtil;
import hr.mit.utils.PrintUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Obracun {

	private static ArrayList<Obracun> obracunList = new ArrayList<Obracun>();

	private Integer id;
	private String datum;
	private Vozac vozac;
	private String uuid;

	static {
		updateList();
	}

	private static void updateList() {

		try {
			String sql = "Select id,datum,vozacid,guid FROM PTKTObracun ORDER BY id DESC";
			ResultSet rs = DbUtil.getConnection2().createStatement().executeQuery(sql);
			obracunList.clear();
			obracunList.add(new Obracun(null, "Stanje blagajne", Starter.vozac.getId(), null));
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
		this.vozac = Vozac.getByID(vozac);
		this.uuid = uuid;
	}

	public static String[] getList() {
		updateList();
		String[] l = new String[obracunList.size()];
		int x = 0;
		for (Obracun v : obracunList) {
			if (v.id != null)
				l[x] = String.format("%5d %s", v.id, v.datum);
			else
				l[x] = "STANJE BLAGAJNE";
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

	public String getUuid() {
		return uuid;
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
			PreparedStatement ps = DbUtil.getConnection2().prepareStatement("SELECT COUNT(*) FROM PTKTProdaja WHERE obracunID IS NULL and Vozac1ID = ?");
			ps.setInt(1, Starter.vozac.getId());
			ResultSet rsCnt = ps.executeQuery();
			if (rsCnt.next()) {
				Integer id = rsCnt.getInt(1);
				if (id.equals(0))
					retval = false;
				else
					retval = true;
			}
			rsCnt.close();
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
			ps = DbUtil.getConnection2().prepareStatement("UPDATE PTKTProdaja SET ObracunID = ? WHERE ObracunID IS NULL and Vozac1ID = ?");
			ps.setInt(1, id);
			ps.setInt(2, Starter.vozac.getId());
			ps.execute();
			rs.close();
			ps.close();
			DbUtil.getConnection2().commit();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public static BigDecimal getSaldo() {
		BigDecimal saldo = BigDecimal.ZERO;
		try {
			PreparedStatement ps = DbUtil.getConnection2().prepareStatement("SELECT sum(ZaPlatiti) FROM PTKTProdaja WHERE ObracunID IS NULL AND StatusZK != 1 and Vozac1ID = ?");
			ps.setInt(1, Starter.vozac.getId());
			double ss = DbUtil.getSingleResultDouble(ps);
			saldo = new BigDecimal(ss);
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return saldo.setScale(2, BigDecimal.ROUND_DOWN);
	}

	public static String getObracun(Integer id) {
		double ukupno = 0;
		String sql = "select StupacID,Coalesce(StatusZK,0) AS StatusZK,VoznaKartaID, COUNT(*) Komada, SUM(PCenaKarte) cena from ptktprodaja";
		StringBuffer retval;
		PreparedStatement ps;
		String linija = "";
		String OznakaZK = "";
		try {
			if (id != null) {
				sql = sql + " WHERE obracunID=? GROUP BY 1,2,3 ORDER BY 1,2,3";
				ps = DbUtil.getConnection2().prepareStatement(sql);
				ps.setInt(1, id);
			} else {
				sql = sql + " WHERE obracunID IS NULL and Vozac1ID=? GROUP BY 1,2,3 ORDER BY 1,2,3";
				ps = DbUtil.getConnection2().prepareStatement(sql);
				ps.setInt(1, Starter.vozac.getId());
			}
			retval = new StringBuffer();
			ResultSet rs = ps.executeQuery();
			Integer stupacID = 0;
			while (rs.next()) {
				if (stupacID != rs.getInt("stupacID")) {
					stupacID = rs.getInt("stupacID");
					retval.append(" \r");
					if (Stupac.getByID(stupacID) != null) {
						linija = Stupac.getByID(stupacID).getOpis() + " (" + stupacID.toString() + ")\r";
					} else {
						linija = "Nema linije broj: (" + stupacID.toString() + ")\r";
					}

					if (linija.length() > 32) {
						retval.append(linija.substring(0, 32) + "\r");
						retval.append(linija.substring(32));
					} else
						retval.append(linija);
					retval.append("................................\r");
				}
				// retval.append("Tip                  Kom  Ukupno\r");
				// retval.append("--------------------------------\r");
				double cena = rs.getDouble("cena");
				if (rs.getInt("StatusZK") == 0)
				{
					ukupno = ukupno + cena;
				    OznakaZK = "";
				}
				else 
					OznakaZK = "(Z)";
				int kartaId = rs.getInt("VoznaKartaID");
				String opis = OznakaZK+Karta.getByID(kartaId).getNaziv();
				int komada = rs.getInt("Komada");
				retval.append(String.format("%-20s %3d %7.2f\r", opis, komada, cena));
				// Još ne znam kaj da tu radim
				// if (kartaId != Karta.ZAMJENSKA_KARTA) {
				// }
			}
			// if (stupacID != 0) {
			retval.append(" \r");
			retval.append("................................\r");
			retval.append(String.format("%-24s %7.2f", "Blagajna", ukupno));
			retval.append(" \r \r \r");
			rs.close();
			ps.close();
			// }
		} catch (SQLException e) {
			throw new RuntimeException(e);

		}
		return retval.toString();
	}

	public static void print(Obracun o) {
		char[] reset = { 27, 64, 13 };
		char[] rotate = { 28, 73, 2 };
		if (new File(".print").exists()) {
			try {
				FileWriter out = new FileWriter(Starter.ComPortPrinter);
				out.write(reset);

				if ((Starter.PrintRotate != null) && (Starter.PrintRotate.equals("1")))
					out.write(PrintUtils.filter(PrintUtils.reversePrint( zaglavlje(o) + getObracun(o.getId())  )));
				else
					out.write(PrintUtils.filter(zaglavlje(o) + getObracun(o.getId())));
				
//				out.write(PrintUtils.filter(zaglavlje(o) + getObracun(o.getId())));
	//			out.write(" \r \r \r");
				out.flush();
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else
			System.out.println(zaglavlje(o) + getObracun(o.getId()));
	}

	public static Obracun getById(Integer id) {
		for (Obracun v : obracunList) {
			if (v.getId().equals(id))
				return v;
		}
		return null;
	}

	private static String zaglavlje(Obracun o) {
		StringBuffer sb = new StringBuffer();
		if (o.getId() == null) {
			sb.append("Stanje blagajne\r");
			sb.append("  \r");
			sb.append("Vozač: " + o.vozac.getNaziv() + "\r");
			sb.append("  \r");
		} else {
			sb.append("Obračun br. " + o.getId().toString() + "\r");
			sb.append(o.getUuid() + "\r \r");
			sb.append("Vozač: " + o.vozac.getNaziv() + "\r");
			sb.append("Datum: " + o.datum + "\r \r");
		}

		return sb.toString();
	}

}
