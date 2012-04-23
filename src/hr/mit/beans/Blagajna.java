package hr.mit.beans;

import hr.mit.utils.DbUtil;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class Blagajna {
	
	public static void save(Vozac vozac,Vozilo vozilo,List<Stavka> stavkaList) {
		String sql = "INSERT INTO PTKTProdaja (Firma,Datum,VoznaKartaID,Code,Cena,Popust,Vozac1ID,StupacID,OdPostajeID,DoPostajeID,VoziloID,BRVoznji,BrPutnika,BRKarata,StatusZK,popust1ID,PCenaKarte,NCenaKarte,ZaPlatiti,KmLinijeVR,KmDomaci,KmIno) VALUES(5,DATETIME('now'),?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		try {
			DbUtil.getConnection2().setAutoCommit(false);
			PreparedStatement ps = DbUtil.getConnection2().prepareStatement(sql);
			
			for (Stavka stavka : stavkaList) {
				ps.setInt(1, stavka.getKarta().getId());
				ps.setString(2,stavka.getBrojKarte());
				
				ps.setDouble(3,stavka.getProdajnaCijena().doubleValue());
				ps.setDouble(4,stavka.getPopust().getPopust().doubleValue());
				ps.setInt(5, vozac.getId());
				ps.setInt(6, stavka.getStupac().getId());
				ps.setInt(7, stavka.getOdPostaje().getId());
				ps.setInt(8, stavka.getDoPostaje().getId());
				ps.setInt(9, vozilo.getId());
				ps.setInt(10,stavka.getKarta().getStVoznji());
				ps.setInt(11,1); //Broj putnika
				ps.setInt(12,1); //Broj karata
				if (stavka.getKarta().getId().equals(Karta.ZAMJENSKA_KARTA)) {
					ps.setInt(13,1); //Status zamjenske karte
					ps.setDouble(17,0.0); // Za platiti
				} else {
					ps.setInt(13,0); //Status zamjenske karte
					ps.setDouble(17,stavka.getProdajnaCijena().doubleValue()); // Za platiti
				}
				ps.setInt(14,stavka.getPopust().getId());
				ps.setDouble(15,stavka.getProdajnaCijena().doubleValue());
				ps.setDouble(16, stavka.getNettoCijena().doubleValue());
				ps.setInt(18,stavka.getKmLinije());
				ps.setInt(19,stavka.getKmDomaci());
				ps.setInt(20, stavka.getKmIno());
				ps.execute();
				ResultSet genKey = ps.getGeneratedKeys();
				if (genKey.next()) {
					stavka.setBrojKarte(new Integer(genKey.getInt(1)).toString());
				}
				genKey.close();
			}
			DbUtil.getConnection2().commit();
			DbUtil.getConnection2().setAutoCommit(true);
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	

	

	
	
	
}
