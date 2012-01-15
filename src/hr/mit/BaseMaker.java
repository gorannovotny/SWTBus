package hr.mit;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Date;

public class BaseMaker {

	public static void main(String[] args) throws SQLException {
		Connection con1 = null;
		Connection con2 = null;
		long stoperica = System.currentTimeMillis();
		try {
			Class.forName("org.sqlite.JDBC");
			Class.forName("net.sourceforge.jtds.jdbc.Driver");
			con1 = DriverManager.getConnection("jdbc:jtds:sqlserver://tehnoinspekt.dyndns.org:1433;DatabaseName=MitSql", "sa", "SaKlik2003");
			con2 = DriverManager.getConnection("jdbc:sqlite:test.db");
			con2.setAutoCommit(false);

			 doPTVozniRedi(con1, con2);
			 doPTVarijanteVR(con1, con2);
			 doPTStupciVR(con1, con2);
			 doPTPostaje(con1, con2);
			 doPTPostajeVR(con1, con2);
			 doPTPostajeVarijantVR(con1, con2);
			 doPTCasiVoznjeVR(con1, con2);
			 doPTKTVozneKarte(con1, con2);
			 doPTVozaci(con1, con2);
			 doPTKTTarifniRazrediCenik(con1, con2);
			 doPTKTVrstePopustov(con1, con2);
			doPTIzjemeCenikaVR(con1, con2);
			doPTKTProdaja(con1, con2);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (con2 != null)
				con2.close();
			if (con1 != null)
				con1.close();
			System.out.println(String.format("Trajanje: %d ms",(System.currentTimeMillis() - stoperica)));
		}

	}

	private static void doPTVozniRedi(Connection con1, Connection con2) throws SQLException {
		int i = 0;
		con2.createStatement().executeUpdate("drop table if exists PTVozniRedi;");
		con2.createStatement()
				.executeUpdate(
						"CREATE TABLE PTVozniRedi(ID INT NOT NULL,Firma INT NOT NULL,Sifra VARCHAR(12)  NOT NULL,OznakaLinije VARCHAR(40) ,PrivitakDozvole VARCHAR(20) ,Registracija VARCHAR(20) ,Opis1 VARCHAR(80)  ,Opis2 VARCHAR(80)  ,VeljaOd DATETIME,VeljaDo DATETIME,VrstaVR INT,KategorijaPrevoza INT,VrstaLinije INT,VrstaPrevoza INT,NacinPrevoza INT,VrstaPosadeID INT,OE INT,PrevoznikID INT,Kooperacija INT,Pool INT,DOSVRID INT,Stat1 INT,PRIMARY KEY (ID))");
		PreparedStatement ps = con2.prepareStatement("INSERT INTO PTVozniRedi VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
		ResultSet rs = con1.createStatement().executeQuery("select * from ptvozniredi where VeljaOd< GETDATE() and veljaDo > GETDATE()");
		while (rs.next()) {
			ps.setInt(1, rs.getInt(1));
			ps.setInt(2, rs.getInt(2));
			ps.setString(3, rs.getString(3));
			ps.setString(4, rs.getString(4));
			ps.setString(5, rs.getString(5));
			ps.setString(6, rs.getString(6));
			ps.setString(7, rs.getString(7));
			ps.setString(8, rs.getString(8));
			ps.setString(9, rs.getString("VeljaOD"));
			ps.setString(10, rs.getString("VeljaDo"));
			ps.setInt(11, rs.getInt(11));
			ps.setInt(12, rs.getInt(12));
			ps.setInt(13, rs.getInt(13));
			ps.setInt(14, rs.getInt(14));
			ps.setInt(15, rs.getInt(15));
			ps.setInt(16, rs.getInt(16));
			ps.setInt(17, rs.getInt(17));
			ps.setInt(18, rs.getInt(18));
			ps.setInt(19, rs.getInt(19));
			ps.setInt(20, rs.getInt(20));
			ps.setInt(21, rs.getInt(21));
			ps.setInt(22, rs.getInt(22));
			ps.addBatch();
			i++;
		}
		ps.executeBatch();
		con2.commit();
		System.out.println("PTVozniRedi -> " + i);
		rs.close();
		ps.close();
	}

	private static void doPTVarijanteVR(Connection con1, Connection con2) throws SQLException {
		int i = 0;
		con2.createStatement().executeUpdate("drop table if exists PTVarijanteVR;");
		con2.createStatement()
				.executeUpdate(
						"CREATE TABLE PTVarijanteVR(ID INT NOT NULL,VozniRedID INT NOT NULL,Varijanta INT NOT NULL,Opis1 VARCHAR,Opis2 VARCHAR,DOSVarID INT,PRIMARY KEY (ID),FOREIGN KEY (VozniRedID) REFERENCES PTVozniRedi (ID))");
		PreparedStatement ps = con2.prepareStatement("INSERT INTO PTVarijanteVR VALUES (?,?,?,?,?,?)");
		ResultSet rs = con1.createStatement().executeQuery("select * from PTVarijanteVR where VozniRedID IN (SELECT ID FROM PTVozniRedi WHERE VeljaOd< GETDATE() and veljaDo > GETDATE())");
		while (rs.next()) {
			ps.setInt(1, rs.getInt(1));
			ps.setInt(2, rs.getInt(2));
			ps.setInt(3, rs.getInt(3));
			ps.setString(4, rs.getString(4));
			ps.setString(5, rs.getString(5));
			ps.setInt(6, rs.getInt(6));
			ps.addBatch();
			i++;
		}
		ps.executeBatch();
		con2.commit();
		System.out.println("PTVarijanteVR -> " + i);
		rs.close();
		ps.close();
	}

	private static void doPTStupciVR(Connection con1, Connection con2) throws SQLException {
		int i = 0;
		con2.createStatement().executeUpdate("drop table if exists PTStupciVR;");
		con2.createStatement()
				.executeUpdate(
						"CREATE TABLE PTStupciVR (ID INT NOT NULL,Firma INT NOT NULL,VozniRedID INT NOT NULL,VarijantaVRID INT NOT NULL,ZapSt VARCHAR NOT NULL,SmerVoznje VARCHAR,DneviVoznjeID INT,PrevoznikID INT,VrstaPrevoza INT,VrstaBusa INT,NacinPrevoza INT,VrstaPosadeID INT,VremeOdhoda FLOAT,OE INT,VeljaOd DATETIME,VeljaDo DATETIME,StatusERR INT,DOSID INT,PRIMARY KEY (ID),FOREIGN KEY (VarijantaVRID) REFERENCES PTVarijanteVR (ID))");
		PreparedStatement ps = con2.prepareStatement("INSERT INTO PTStupciVR VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
		ResultSet rs = con1.createStatement().executeQuery(
				"SELECT * FROM PTStupciVR WHERE VarijantaVRID IN (select ID from PTVarijanteVR where VozniRedID IN (SELECT ID FROM PTVozniRedi WHERE VeljaOd< GETDATE() and veljaDo > GETDATE()))");
		while (rs.next()) {
			ps.setInt(1, rs.getInt(1));
			ps.setInt(2, rs.getInt(2));
			ps.setInt(3, rs.getInt(3));
			ps.setInt(4, rs.getInt(4));
			ps.setString(5, rs.getString(5));
			ps.setString(6, rs.getString(6));
			ps.setInt(7, rs.getInt(7));
			ps.setInt(8, rs.getInt(8));
			ps.setInt(9, rs.getInt(9));
			ps.setInt(10, rs.getInt(10));
			ps.setInt(11, rs.getInt(11));
			ps.setInt(12, rs.getInt(12));
			ps.setDouble(13, rs.getDouble(13));
			ps.setInt(14, rs.getInt(14));
			ps.setString(15, rs.getString(15));
			ps.setString(16, rs.getString(16));
			ps.setInt(17, rs.getInt(17));
			ps.setInt(18, rs.getInt(18));
			ps.addBatch();
			i++;
		}
		ps.executeBatch();
		con2.commit();
		System.out.println("PTStupciVR -> " + i);
		rs.close();
		ps.close();
	}

	private static void doPTPostajeVarijantVR(Connection con1, Connection con2) throws SQLException {
		int i = 0;
		con2.createStatement().executeUpdate("drop table if exists PTPostajeVarijantVR;");
		con2.createStatement()
				.executeUpdate(
						"CREATE TABLE PTPostajeVarijantVR(ID INT NOT NULL,VarijantaID INT NOT NULL,NodePostajeVRID INT NOT NULL,ZapSt INT,KumDistancaM INT,DistancaM INT,Vozel INT,Staje CHAR,DOSID INT,PRIMARY KEY (ID),FOREIGN KEY (NodePostajeVRID) REFERENCES PTPostajeVR (ID),FOREIGN KEY (VarijantaID) REFERENCES PTVarijanteVR (ID))");
		PreparedStatement ps = con2.prepareStatement("INSERT INTO PTPostajeVarijantVR VALUES (?,?,?,?,?,?,?,?,?)");
		ResultSet rs = con1
				.createStatement()
				.executeQuery(
						"SELECT * FROM PTPostajeVarijantVR WHERE VarijantaID IN(select ID from PTVarijanteVR where VozniRedID IN (SELECT ID FROM PTVozniRedi WHERE VeljaOd< GETDATE() and veljaDo > GETDATE()))");
		while (rs.next()) {
			ps.setInt(1, rs.getInt(1));
			ps.setInt(2, rs.getInt(2));
			ps.setInt(3, rs.getInt(3));
			ps.setInt(4, rs.getInt(4));
			ps.setInt(5, rs.getInt(5));
			ps.setInt(6, rs.getInt(6));
			ps.setInt(7, rs.getInt(7));
			ps.setString(8, rs.getString(8));
			ps.setInt(9, rs.getInt(9));
			ps.addBatch();
			i++;
		}
		ps.executeBatch();
		con2.commit();
		System.out.println("PTPostajeVarijantVR -> " + i);
		rs.close();
		ps.close();
	}

	private static void doPTPostajeVR(Connection con1, Connection con2) throws SQLException {
		int i = 0;
		con2.createStatement().executeUpdate("drop table if exists PTPostajeVR;");
		con2.createStatement()
				.executeUpdate(
						"CREATE TABLE PTPostajeVR(ID INT NOT NULL,VozniRedID INT NOT NULL,ZapSt INT NOT NULL, Vozel INT NOT NULL,PostajaID INT NOT NULL,KumDistancaM INT,DistancaM INT,Staje CHAR,DosID INT,PRIMARY KEY (ID),FOREIGN KEY (PostajaID) REFERENCES PTPostaje (ID) ,FOREIGN KEY (VozniRedID) REFERENCES PTVozniRedi (ID))");
		PreparedStatement ps = con2.prepareStatement("INSERT INTO PTPostajeVR VALUES (?,?,?,?,?,?,?,?,?)");
		ResultSet rs = con1.createStatement().executeQuery("SELECT * FROM PTPostajeVR WHERE VozniRedID IN (SELECT ID FROM PTVozniRedi WHERE VeljaOd< GETDATE() and veljaDo > GETDATE())");
		while (rs.next()) {
			ps.setInt(1, rs.getInt(1));
			ps.setInt(2, rs.getInt(2));
			ps.setInt(3, rs.getInt(3));
			ps.setInt(4, rs.getInt(4));
			ps.setInt(5, rs.getInt(5));
			ps.setInt(6, rs.getInt(6));
			ps.setInt(7, rs.getInt(7));
			ps.setString(8, rs.getString(8));
			ps.setInt(9, rs.getInt(9));
			ps.addBatch();
			i++;
		}
		ps.executeBatch();
		con2.commit();
		System.out.println("PTPostajeVR -> " + i);
		rs.close();
		ps.close();
	}

	private static void doPTPostaje(Connection con1, Connection con2) throws SQLException {
		int i = 0;
		con2.createStatement().executeUpdate("drop table if exists PTPostaje;");
		con2.createStatement().executeUpdate("CREATE TABLE PTPostaje(ID INT NOT NULL,Sifra VARCHAR(12),Naziv VARCHAR(50),VrstaPostaje INT,Drzava INT,PGRID INT,Prodaja CHAR(1),PRIMARY KEY (ID))");
		PreparedStatement ps = con2.prepareStatement("INSERT INTO PTPostaje VALUES (?,?,?,?,?,?,?)");
		ResultSet rs = con1.createStatement().executeQuery("SELECT * FROM PTPostaje");
		while (rs.next()) {
			ps.setInt(1, rs.getInt(1));
			ps.setString(2, rs.getString(2));
			ps.setString(3, rs.getString(3));
			ps.setInt(4, rs.getInt(4));
			ps.setInt(5, rs.getInt(5));
			ps.setInt(6, rs.getInt(6));
			ps.setString(7, rs.getString(7));
			ps.addBatch();
			i++;
		}
		ps.executeBatch();
		con2.commit();
		System.out.println("PTPostaje -> " + i);
		rs.close();
		ps.close();
	}

	private static void doPTCasiVoznjeVR(Connection con1, Connection con2) throws SQLException {
		int i = 0;
		con2.createStatement().executeUpdate("drop table if exists PTCasiVoznjeVR;");
		con2.createStatement()
				.executeUpdate(
						"CREATE TABLE PTCasiVoznjeVR(ID INT NOT NULL,Firma INT NOT NULL,StupacVRID INT NOT NULL,NodePostajeVarijanteVRID INT NOT NULL,VremeOdhoda FLOAT,VremePrihoda FLOAT,PRIMARY KEY (ID),FOREIGN KEY (NodePostajeVarijanteVRID) REFERENCES PTPostajeVarijantVR (ID),FOREIGN KEY (StupacVRID) REFERENCES PTStupciVR (ID))");
		PreparedStatement ps = con2.prepareStatement("INSERT INTO PTCasiVoznjeVR VALUES (?,?,?,?,?,?)");
		ResultSet rs = con1
				.createStatement()
				.executeQuery(
						"SELECT * FROM PTCasiVoznjeVR WHERE StupacVRID IN(SELECT ID FROM PTStupciVR WHERE VarijantaVRID IN (select ID from PTVarijanteVR where VozniRedID IN (SELECT ID FROM PTVozniRedi WHERE VeljaOd< GETDATE() and veljaDo > GETDATE())))");
		while (rs.next()) {
			ps.setInt(1, rs.getInt(1));
			ps.setInt(2, rs.getInt(2));
			ps.setInt(3, rs.getInt(3));
			ps.setInt(4, rs.getInt(4));
			ps.setDouble(5, rs.getDouble(5));
			ps.setDouble(6, rs.getDouble(6));
			ps.addBatch();
			i++;
		}
		ps.executeBatch();
		con2.commit();
		System.out.println("PTCasiVoznjeVR " + i);
		rs.close();
		ps.close();
	}

	private static void doPTKTVozneKarte(Connection con1, Connection con2) throws SQLException {
		int i = 0;
		con2.createStatement().executeUpdate("drop table if exists PTKTVozneKarte");
		con2.createStatement()
				.executeUpdate(
						"CREATE TABLE PTKTVozneKarte(ID INT NOT NULL , Firma INT NOT NULL,Sifra VARCHAR(10) NOT NULL,Oznaka VARCHAR(16) ,TipKarteID INT NOT NULL,TarifniRazredID INT,Opis VARCHAR(50) ,StVoznji INT,SmerVoznje INT,VeljaOdDatuma DATETIME,VeljaDoDatuma DATETIME,VeljaDniOdProdaje INT,Status INT,PrevoznikID INT,NacinDolocanjaCene INT,KMPogoja INT,FiksnaCena FLOAT(53),PopustID INT,PopustProcent FLOAT(53),SifraValute INT,MobilnaProdaja INT,PRIMARY KEY (ID))");
		PreparedStatement ps = con2.prepareStatement("INSERT INTO PTKTVozneKarte VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
		ResultSet rs = con1.createStatement().executeQuery("SELECT * FROM PTKTVozneKarte");
		while (rs.next()) {
			ps.setInt(1, rs.getInt(1));
			ps.setInt(2, rs.getInt(2));
			ps.setString(3, rs.getString(3));
			ps.setString(4, rs.getString(4));
			ps.setInt(5, rs.getInt(5));
			ps.setInt(6, rs.getInt(6));
			ps.setString(7, rs.getString(7));
			ps.setInt(8, rs.getInt(8));
			ps.setInt(9, rs.getInt(9));
			ps.setString(10, rs.getString(10));
			ps.setString(11, rs.getString(11));
			ps.setInt(12, rs.getInt(12));
			ps.setInt(13, rs.getInt(13));
			ps.setInt(14, rs.getInt(14));
			ps.setInt(15, rs.getInt(15));
			ps.setInt(16, rs.getInt(16));
			ps.setDouble(17, rs.getDouble(17));
			ps.setInt(18, rs.getInt(18));
			ps.setDouble(19, rs.getDouble(19));
			ps.setInt(20, rs.getInt(20));
			ps.setInt(21, rs.getInt(21));
			ps.addBatch();
			i++;
		}
		ps.executeBatch();
		con2.commit();
		System.out.println("PTKTVozneKarte " + i);
		rs.close();
		ps.close();
	}

	private static void doPTVozaci(Connection con1, Connection con2) throws SQLException {
		int i = 0;
		con2.createStatement().executeUpdate("drop table if exists PTVozaci;");
		con2.createStatement().executeUpdate("CREATE TABLE PTVozaci(ID INT NOT NULL,PGRID INT NOT NULL,Sifra INT NOT NULL,Naziv VARCHAR,OsebaID INT,JeVozac INT,JeKondukter INT,PRIMARY KEY (ID))");
		PreparedStatement ps = con2.prepareStatement("INSERT INTO PTVozaci VALUES (?,?,?,?,?,?,?)");
		ResultSet rs = con1.createStatement().executeQuery("SELECT * FROM PTVozaci");
		while (rs.next()) {
			ps.setInt(1, rs.getInt(1));
			ps.setInt(2, rs.getInt(2));
			ps.setInt(3, rs.getInt(3));
			ps.setString(4, rs.getString(4));
			ps.setInt(5, rs.getInt(5));
			ps.setInt(6, rs.getInt(6));
			ps.setInt(7, rs.getInt(7));
			ps.addBatch();
			i++;
		}
		ps.executeBatch();
		con2.commit();
		System.out.println("PTVozaci " + i);
		rs.close();
		ps.close();
	}

	private static void doPTKTTarifniRazrediCenik(Connection con1, Connection con2) throws SQLException {
		int i = 0;
		con2.createStatement().executeUpdate("drop table if exists PTKTTarifniRazrediCenik;");
		con2.createStatement().executeUpdate(
				"CREATE TABLE PTKTTarifniRazrediCenik(ID INT NOT NULL ,IDRazreda INT NOT NULL,VeljaOd DATETIME NOT NULL,OdKM INT NOT NULL,Cena FLOAT(53),DOSID INT,PRIMARY KEY (ID))");
		PreparedStatement ps = con2.prepareStatement("INSERT INTO PTKTTarifniRazrediCenik VALUES (?,?,?,?,?,?)");
		ResultSet rs = con1.createStatement().executeQuery("SELECT * FROM PTKTTarifniRazrediCenik");
		while (rs.next()) {
			ps.setInt(1, rs.getInt(1));
			ps.setInt(2, rs.getInt(2));
			ps.setString(3, rs.getString(3));
			ps.setInt(4, rs.getInt(4));
			ps.setDouble(5, rs.getDouble(5));
			ps.setInt(6, rs.getInt(6));
			ps.addBatch();
			i++;
		}
		ps.executeBatch();
		con2.commit();
		System.out.println("PTKTTarifniRazrediCenik " + i);
		rs.close();
		ps.close();
	}

	private static void doPTKTVrstePopustov(Connection con1, Connection con2) throws SQLException {
		int i = 0;
		con2.createStatement().executeUpdate("drop table if exists PTKTVrstePopustov;");
		con2.createStatement()
				.executeUpdate(
						"CREATE TABLE PTKTVrstePopustov(ID INT NOT NULL ,Firma INT NOT NULL,Sifra INT NOT NULL,Oznaka VARCHAR(10)  NOT NULL,Opis VARCHAR(50) ,NacinIzracuna INT,Vrednost FLOAT(53),PRIMARY KEY (ID))");
		PreparedStatement ps = con2.prepareStatement("INSERT INTO PTKTVrstePopustov VALUES (?,?,?,?,?,?,?)");
		ResultSet rs = con1.createStatement().executeQuery("SELECT * FROM PTKTVrstePopustov");
		while (rs.next()) {
			ps.setInt(1, rs.getInt(1));
			ps.setInt(2, rs.getInt(2));
			ps.setInt(3, rs.getInt(3));
			ps.setString(4, rs.getString(4));
			ps.setString(5, rs.getString(5));
			ps.setInt(6, rs.getInt(6));
			ps.setDouble(7, rs.getDouble(7));
			ps.addBatch();
			i++;
		}
		ps.executeBatch();
		con2.commit();
		System.out.println("PTKTVrstePopustov " + i);
		rs.close();
		ps.close();
	}

	private static void doPTIzjemeCenikaVR(Connection con1, Connection con2) throws SQLException {
		int i = 0;
		con2.createStatement().executeUpdate("drop table if exists PTIzjemeCenikaVR;");
		con2.createStatement()
				.executeUpdate(
						"CREATE TABLE PTIzjemeCenikaVR(ID INT NOT NULL,Firma INT NOT NULL,TarifniCenikID INT NOT NULL,VeljaOd DATETIME NOT NULL,VozniRedID INT NOT NULL,VarijantaID INT NOT NULL,StupacID INT NOT NULL,Postaja1ID INT NOT NULL,Postaja2ID INT NOT NULL,Cena FLOAT(53),PRIMARY KEY (ID))");
		PreparedStatement ps = con2.prepareStatement("INSERT INTO PTIzjemeCenikaVR VALUES (?,?,?,?,?,?,?,?,?,?)");
		ResultSet rs = con1.createStatement().executeQuery("SELECT * FROM PTIzjemeCenikaVR");
		while (rs.next()) {
			ps.setInt(1, rs.getInt(1));
			ps.setInt(2, rs.getInt(2));
			ps.setInt(3, rs.getInt(3));
			ps.setString(4, rs.getString(4));
			ps.setInt(5, rs.getInt(5));
			ps.setInt(6, rs.getInt(6));
			ps.setInt(7, rs.getInt(7));
			ps.setInt(8, rs.getInt(8));
			ps.setInt(9, rs.getInt(9));
			ps.setDouble(10, rs.getDouble(10));
			ps.addBatch();
			i++;
		}
		ps.executeBatch();
		con2.commit();
		System.out.println("PTIzjemeCenikaVR " + i);
		rs.close();
		ps.close();
	}

	private static void doPTKTProdaja(Connection con1, Connection con2) throws SQLException {
		int i = 0;
		con2.createStatement().executeUpdate("drop table if exists PTKTProdaja;");
		con2.createStatement()
				.executeUpdate(
						"CREATE TABLE PTKTProdaja(ID INT NOT NULL ,Firma INT NOT NULL,DokumentProdajeID INT,VrsticaProdajeID INT,DokumentBlagajneID INT,BUSProdajaID INT,Datum DATETIME,Vreme DATETIME,VoznaKartaID INT,Code VARCHAR(20) ,BRVoznji INT,Cena FLOAT(53),Popust1ID INT,Popust2ID INT,Popust3ID INT,PCenaKarte FLOAT(53),NCenaKarte FLOAT(53),Popust FLOAT(53),ZaPlatiti FLOAT(53),PorezProcent INT,ProdajnoMestoID INT,PrevoznikID INT,VrstaPosadeID INT,Vozac1ID INT,Vozac2ID INT,Vozac3ID INT,Blagajnik INT,Blagajna INT,StupacID INT,OdPostajeID INT,DoPostajeID INT,VoziloID INT,Rezervacija INT,StatusZK INT,KmLinijeVR INT,KmDomaci INT,KmIno INT,BRPutnika INT,BRKarata INT,MobStrojID INT,GUID VARCHAR(40) ,PRIMARY KEY (ID))");
		con2.commit();
		System.out.println("PTKTProdaja " + i);
	}

}
