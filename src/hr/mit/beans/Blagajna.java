package hr.mit.beans;

import hr.mit.utils.DbUtil;

import java.io.ObjectInputStream.GetField;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class Blagajna {
	
	public static void save(Vozac vozac,List<Stavka> stavkaList) {
		String sql = "INSERT INTO PTKTProdaja (Firma,Datum,VoznaKartaID,Code,Cena,Popust,Vozac1ID,StupacID,OdPostajeID,DoPostajeID,VoziloID) VALUES(5,DATETIME('now'),?,?,?,?,?,?,?,?,?)";
		try {
			DbUtil.getConnection().setAutoCommit(false);
			PreparedStatement ps = DbUtil.getConnection().prepareStatement(sql);
			
			for (Stavka stavka : stavkaList) {
				ps.setInt(1, stavka.getKarta().getId());
				ps.setString(2,stavka.getBrojKarte());
				ps.setDouble(3,stavka.getCijena().doubleValue());
				ps.setDouble(4,stavka.getPopust().getPopust().doubleValue());
				ps.setInt(5, vozac.getId());
				ps.setInt(6, stavka.getStupac().getId());
				ps.setInt(7, stavka.getOdPostaje().getZapSt());
				ps.setInt(8, stavka.getDoPostaje().getZapSt());
				ps.execute();
			}
			DbUtil.getConnection().commit();
			DbUtil.getConnection().setAutoCommit(true);
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static BigDecimal getSaldo() {
		BigDecimal saldo = BigDecimal.ZERO;
		try {
			PreparedStatement ps = DbUtil.getConnection().prepareStatement("SELECT sum(cena) FROM PTKTProdaja"); 
			double ss = DbUtil.getSingleResultDouble(ps);
			saldo = new BigDecimal(ss);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return saldo.setScale(2,BigDecimal.ROUND_DOWN);
	}
	
	public static String getObracun() {
		double ukupno = 0;
		StringBuffer retval = new StringBuffer("Tip                           Komada  Ukupno\n--------------------------------------------\n");
		try {
			PreparedStatement ps = DbUtil.getConnection().prepareStatement("select a.VoznaKartaID,c.Opis,COUNT(*) Komada,SUM(Cena) cena from ptktprodaja a,PTKTVozneKarte b,PTKTTipiKarti c WHERE a.VoznaKartaID = b.ID AND c.ID = b.TipKarteID AND StatusZK IS NULL GROUP BY 1,2 ORDER BY 1,2");
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				int id = rs.getInt("VoznaKartaID");
				String opis = rs.getString("Opis");
				int komada = rs.getInt("Komada");
				double cena =  rs.getDouble("cena");
				retval.append(String.format("%-30s %5d %7.2f\n",opis,komada,cena));
				if (id != Karta.ZAMJENSKA_KARTA) {
					ukupno = ukupno + cena;
				}
			}
			retval.append(String.format("--------------------------------------------\n%-36s %7.2f","Blagajna",ukupno));
		} catch(SQLException e) {
			e.printStackTrace();
			
		}
		return retval.toString();
	}
}
