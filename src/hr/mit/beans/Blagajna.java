package hr.mit.beans;

import hr.mit.Starter;
import hr.mit.utils.DbUtil;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class Blagajna {

	public static void save(Vozac vozac, Vozilo vozilo, List<Stavka> stavkaList) {
		String sql = "INSERT INTO PTKTProdaja (Firma,Datum,VoznaKartaID,Code,Cena,Popust,Vozac1ID,StupacID,OdPostajeID,DoPostajeID,VoziloID,BRVoznji,BrPutnika,BRKarata,StatusZK,popust1ID,PCenaKarte,NCenaKarte,ZaPlatiti,KmLinijeVR,KmDomaci,KmIno,Stevilka,MobStrojID,SifraValute) VALUES(5,DATETIME('now'),?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		try {
			DbUtil.getConnection2().setAutoCommit(false);
			PreparedStatement ps = DbUtil.getConnection2().prepareStatement(sql);

			for (Stavka stavka : stavkaList) {
				ps.setInt(1, stavka.getKarta().getId());

				ps.setDouble(3, stavka.getProdajnaCijena().doubleValue());
				ps.setDouble(4, stavka.getPopust().getPopust().doubleValue());
				ps.setInt(5, vozac.getId());
				ps.setInt(6, stavka.getStupac().getId());
				ps.setInt(7, stavka.getOdPostaje().getId());
				ps.setInt(8, stavka.getDoPostaje().getId());
				ps.setInt(9, vozilo.getId());
				ps.setInt(11, 1); // Broj putnika
				ps.setInt(14, stavka.getPopust().getId());
				ps.setDouble(15, stavka.getProdajnaCijena().doubleValue());
				ps.setDouble(16, stavka.getNettoCijena().doubleValue());
				ps.setInt(18, stavka.getKmLinije());
				ps.setInt(19, stavka.getKmDomaci());
				ps.setInt(20, stavka.getKmIno());
				ps.setInt(22,Integer.parseInt(Starter.SifraMobStroja)); // MobStrojID
				ps.setInt(23, 191); // SifraValute

				if (stavka.getJeZamjenska()) {
					ps.setString(2, stavka.getBrojKarte());
					ps.setInt(13, 1); // Status zamjenske karte
					ps.setDouble(17, 0.0); // Za platiti
					ps.setInt(12, 0); // Broj karata
					ps.setInt(10, 0); // Broj voznji
					ps.setObject(21, null);  // stevilka
				} else {
					Integer i = getNextStevilka();
					ps.setInt(21, i);  // stevilka
					ps.setString(2, Starter.Prefix + Starter.SifraMobStroja + String.format("%05d", i));
					ps.setInt(13, 0); // Status zamjenske karte
					ps.setDouble(17, stavka.getProdajnaCijena().doubleValue()); // Za platiti
					ps.setInt(12, 1); // Broj karata
					ps.setInt(10, stavka.getKarta().getStVoznji()); // Broj voznji
					stavka.setBrojKarte(Starter.Prefix + Starter.SifraMobStroja + String.format("%05d", i));
				}
				
				ps.execute();
			}
			DbUtil.getConnection2().commit();
			DbUtil.getConnection2().setAutoCommit(true);
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	private static int getNextStevilka() {
		int i = 0;
		try {
			ResultSet rs =  DbUtil.getConnection2().createStatement().executeQuery("SELECT MAX(Stevilka) FROM PTKTProdaja");
			if (rs.next()) {
				i = rs.getInt(1); 
			} 
			i++;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return i;
	}

}
