package hr.mit.beans;

import hr.mit.utils.DbUtil;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class Blagajna {
	
	public static void save(Vozac vozac,List<Stavka> stavkaList) {
		String sql = "INSERT INTO PTKTProdaja (Firma,Datum,VoznaKartaID,Code,Cena,Popust,Vozac1ID,StupacID,OdPostajeID,DoPostajeID,VoziloID) VALUES(5,DATETIME('now'),?,?,?,?,?,?,?,?,?)";
		try {
			DbUtil.getConnection2().setAutoCommit(false);
			PreparedStatement ps = DbUtil.getConnection2().prepareStatement(sql);
			
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
			DbUtil.getConnection2().commit();
			DbUtil.getConnection2().setAutoCommit(true);
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	

	

	
	
	
}
