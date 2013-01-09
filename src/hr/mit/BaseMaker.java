package hr.mit;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Calendar;

public class BaseMaker {

	public static String PER_String(Date Datum) {
		DateFormat df = new SimpleDateFormat("yyyy.MM");
		String PerStr = "";
		try {
			PerStr = "'" + df.format(Datum) + "'";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return (PerStr);
	}

	public static void main(String[] args) throws SQLException {
		Connection con1 = null;
		Connection con2 = null;
		Connection con3 = null;
		boolean FMakeVR = false;
		boolean FMakeMesecne = false;
		boolean FMakeZaduzenja = false;
		boolean FAPConnection = false;

		for (String s : args) {
			// System.out.println(s);
			if (s.equalsIgnoreCase("/ALL")) {
				FMakeVR = true;
				FMakeMesecne = true;
				FMakeZaduzenja = true;
			} else if (s.equalsIgnoreCase("/VR")) {
				FMakeVR = true;
			} else if (s.equalsIgnoreCase("/MES")) {
				FMakeMesecne = true;
			} else if (s.equalsIgnoreCase("/ZAD")) {
				FMakeZaduzenja = true;
			} else if (s.equalsIgnoreCase("/APCON")) {
				FAPConnection = true;
			} else if (s.equalsIgnoreCase("/?")) {
				System.out.println("Parametri:");
				System.out.println(String.format("%-8s - %-30s", "/ALL", "Prenose se svi podaci"));
				System.out.println(String.format("%-8s - %-30s", "/VR", "Vozni redi"));
				System.out.println(String.format("%-8s - %-30s", "/MES", "Mjesecne karte"));
				System.out.println(String.format("%-8s - %-30s", "/ZAD", "Zaduzenja vozaca"));
				System.out.println(String.format("%-8s - %-30s", "/APCON", "AP server konekcija"));
				return;
			}

		}

		long stoperica = System.currentTimeMillis();
		try {
			Class.forName("org.sqlite.JDBC");
			Class.forName("net.sourceforge.jtds.jdbc.Driver");
			if (FAPConnection) {
				con1 = DriverManager.getConnection("jdbc:jtds:sqlserver://77.237.120.229:1433;DatabaseName=APSql", "sa", "klik2003");
			} else {
				con1 = DriverManager.getConnection("jdbc:jtds:sqlserver://tehnoinspekt.dyndns.org:1433;DatabaseName=MitSql", "sa", "SaKlik2003");
			}
			con2 = DriverManager.getConnection("jdbc:sqlite:baza.db");
			con3 = DriverManager.getConnection("jdbc:sqlite:prodaja.db");
			con2.setAutoCommit(false);
			con3.setAutoCommit(false);
			if (FMakeVR) {
				doSkValute(con1, con2);
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
				doPTKTProdaja(con1, con3);
				doPTKTTipiKarti(con1, con2);
				doPTKTPopusti(con1, con2);
				doPromVozila(con1, con2);
				doPTProdajnaMesta(con1, con2);
				doPTStupciVRMirovanja(con1, con2);
				doPTPrevozniki(con1, con2);
				doPTDneviVoznje(con1, con2);
				doPromAlmexMasine(con1, con2);
			}
			if (FMakeMesecne) {
				doPTMesRelacije(con1, con2);
				doPTMesOsebe(con1, con2);
				doPTMesCode(con1, con2);
				doPTMesProdaja(con1, con2);
			}
			if (FMakeZaduzenja) {
				doPTMobilnaZaduzenja(con1, con2);
			}
			doPTMobileSetup(con1, con2);
			doPTVerzija(con1, con2);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (con2 != null)
				con2.close();
			if (con1 != null)
				con1.close();
			if (con3 != null)
				con3.close();
			System.out.println(String.format("Trajanje: %d ms", (System.currentTimeMillis() - stoperica)));
		}

	}

	private static void doPTVozniRedi(Connection con1, Connection con2) throws SQLException {
		int i = 0;
		con2.createStatement().executeUpdate("drop table if exists PTVozniRedi;");
		con2.createStatement().executeUpdate("CREATE TABLE PTVozniRedi(ID INT NOT NULL,Firma INT NOT NULL,Sifra VARCHAR(12)  NOT NULL,OznakaLinije VARCHAR(40) ,PrivitakDozvole VARCHAR(20) ,Registracija VARCHAR(20) ,Opis1 VARCHAR(80)  ,Opis2 VARCHAR(80)  ,VeljaOd DATETIME,VeljaDo DATETIME, SifraOznakeVR INT, VrstaVR INT,KategorijaPrevoza INT,VrstaLinije INT,VrstaPrevoza INT,NacinPrevoza INT,VrstaPosadeID INT,PrevoznikID INT,Kooperacija INT,Pool INT,DOSVRID INT,Stat1 INT,PRIMARY KEY (ID))");
		PreparedStatement ps = con2.prepareStatement("INSERT INTO PTVozniRedi VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
		ResultSet rs = con1.createStatement().executeQuery("select * from PtVozniredi where VrstaVR=1 and veljaDo >= GETDATE()"); // svi danasnji i buduci vozni redi
		while (rs.next()) {
			ps.setInt(1, rs.getInt("ID"));
			ps.setInt(2, rs.getInt("Firma"));
			ps.setString(3, rs.getString("Sifra"));
			ps.setString(4, rs.getString("OznakaLinije"));
			ps.setString(5, rs.getString("PrivitakDozvole"));
			ps.setString(6, rs.getString("Registracija"));
			ps.setString(7, rs.getString("Opis1"));
			ps.setString(8, rs.getString("Opis2"));
			ps.setString(9, rs.getString("VeljaOD"));
			ps.setString(10, rs.getString("VeljaDo"));
			ps.setInt(11, rs.getInt("SifraOznakeVR"));
			ps.setInt(12, rs.getInt("VrstaVR"));
			ps.setInt(13, rs.getInt("KategorijaPrevoza"));
			ps.setInt(14, rs.getInt("VrstaLinije"));
			ps.setInt(15, rs.getInt("VrstaPrevoza"));
			ps.setInt(16, rs.getInt("NacinPrevoza"));
			ps.setInt(17, rs.getInt("VrstaPosadeID"));
			ps.setInt(18, rs.getInt("PrevoznikID"));
			ps.setInt(19, rs.getInt("Kooperacija"));
			ps.setInt(20, rs.getInt("Pool"));
			ps.setInt(21, rs.getInt("DOSVRID"));
			ps.setInt(22, rs.getInt("Status"));
			ps.addBatch();
			i++;
		}
		ps.executeBatch();
		con2.commit();
		System.out.println(String.format("%-26s -> %7d", "PTVozniRedi", i));
		rs.close();
		ps.close();
	}

	private static void doPTVarijanteVR(Connection con1, Connection con2) throws SQLException {
		int i = 0;
		con2.createStatement().executeUpdate("drop table if exists PTVarijanteVR;");
		con2.createStatement().executeUpdate("CREATE TABLE PTVarijanteVR(ID INT NOT NULL,VozniRedID INT NOT NULL,Varijanta INT NOT NULL,Opis1 VARCHAR,Opis2 VARCHAR,DOSVarID INT,PRIMARY KEY (ID),FOREIGN KEY (VozniRedID) REFERENCES PTVozniRedi (ID))");
		con2.createStatement().executeUpdate("CREATE INDEX [IXVarVRVozniRedID]  ON [PTVarijanteVR] ([VozniRedID])");
		PreparedStatement ps = con2.prepareStatement("INSERT INTO PTVarijanteVR VALUES (?,?,?,?,?,?)");
		ResultSet rs = con1.createStatement().executeQuery("select * from PTVarijanteVR where VozniRedID IN (SELECT ID FROM PTVozniRedi WHERE VrstaVR=1 and veljaDo >= GETDATE())"); //
		while (rs.next()) {
			ps.setInt(1, rs.getInt("ID"));
			ps.setInt(2, rs.getInt("VozniRedID"));
			ps.setInt(3, rs.getInt("Varijanta"));
			ps.setString(4, rs.getString("Opis1"));
			ps.setString(5, rs.getString("Opis2"));
			ps.setInt(6, rs.getInt("DOSVarID"));
			ps.addBatch();
			i++;
		}
		ps.executeBatch();
		con2.commit();
		System.out.println(String.format("%-26s -> %7d", "PTVarijanteVR", i));
		rs.close();
		ps.close();
	}

	private static void doPTStupciVR(Connection con1, Connection con2) throws SQLException {
		int i = 0;
		con2.createStatement().executeUpdate("drop table if exists PTStupciVR;");
		con2.createStatement().executeUpdate("CREATE TABLE PTStupciVR (ID INT NOT NULL,Firma INT NOT NULL,VozniRedID INT NOT NULL,VarijantaVRID INT NOT NULL,ZapSt VARCHAR NOT NULL,SmerVoznje VARCHAR,DneviVoznjeID INT,PrevoznikID INT,VrstaPrevoza INT,VrstaBusa INT,StBusov INT,NacinPrevoza INT,VrstaPosadeID INT,VremeOdhoda FLOAT, VeljaOd DATETIME,VeljaDo DATETIME,StatusERR INT,DOSID INT,PRIMARY KEY (ID)" +
				",FOREIGN KEY (VarijantaVRID) REFERENCES PTVarijanteVR (ID) ,FOREIGN KEY (VozniRedID) REFERENCES PTVozniRedi (ID))");
		con2.createStatement().executeUpdate("CREATE INDEX [IXStupciVRVozniRedID]   ON [PTStupciVR] ([VozniRedID])");
		con2.createStatement().executeUpdate("CREATE INDEX [IXStupciVRVarijantaID]  ON [PTStupciVR] ([VarijantaVRID])");
		PreparedStatement ps = con2.prepareStatement("INSERT INTO PTStupciVR VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
		ResultSet rs = con1.createStatement().executeQuery("SELECT * FROM PTStupciVR WHERE VarijantaVRID IN (select ID from PTVarijanteVR where VozniRedID IN (SELECT ID FROM PTVozniRedi WHERE  VrstaVR=1 and veljaDo >= GETDATE())) ");
		while (rs.next()) {
			ps.setInt(1, rs.getInt("ID"));
			ps.setInt(2, rs.getInt("Firma"));
			ps.setInt(3, rs.getInt("VozniRedID"));
			ps.setInt(4, rs.getInt("VarijantaVRID"));
			ps.setString(5, rs.getString("ZapSt"));
			ps.setString(6, rs.getString("SmerVoznje"));
			ps.setInt(7, rs.getInt("DneviVoznjeID"));
			ps.setInt(8, rs.getInt("PrevoznikID"));
			ps.setInt(9, rs.getInt("VrstaPrevoza"));
			ps.setInt(10, rs.getInt("VrstaBusa"));
			ps.setInt(11, rs.getInt("StBusov"));
			ps.setInt(12, rs.getInt("NacinPrevoza"));
			ps.setInt(13, rs.getInt("VrstaPosadeID"));
			ps.setDouble(14, rs.getDouble("VremeOdhoda"));
			ps.setString(15, rs.getString("VeljaOd"));
			ps.setString(16, rs.getString("VeljaDo"));
			ps.setInt(17, rs.getInt("StatusERR"));
			ps.setInt(18, rs.getInt("DOSID"));
			ps.addBatch();
			i++;
		}
		ps.executeBatch();
		con2.commit();
		System.out.println(String.format("%-26s -> %7d", "PTStupciVR", i));
		rs.close();
		ps.close();
	}
	
	private static void doPTStupciVRMirovanja(Connection con1, Connection con2) throws SQLException {
		int i = 0;
		con2.createStatement().executeUpdate("drop table if exists PTStupciVRMirovanja;");
		con2.createStatement().executeUpdate("CREATE TABLE PTStupciVRMirovanja(ID INT NOT NULL,StupacID INT NOT NULL,OdDatuma DATETIME,DoDatuma DATETIME,Opis VARCHAR(120),PRIMARY KEY (ID)" +
				" ,FOREIGN KEY (StupacID) REFERENCES PTStupciVR (ID) )");
		con2.createStatement().executeUpdate("CREATE INDEX [IXMirovanjaStupacID] ON [PTStupciVRMirovanja] ([StupacID])");
		PreparedStatement ps = con2.prepareStatement("INSERT INTO PTStupciVRMirovanja VALUES (?,?,?,?,?)");
		ResultSet rs = con1.createStatement().executeQuery("SELECT * FROM PTStupciVRMirovanja WHERE DoDatuma >= GETDATE()"); // svi dansanji i buduci su ok
		while (rs.next()) {
			ps.setInt(1, rs.getInt("ID"));
			ps.setInt(2, rs.getInt("StupacID"));
			ps.setString(3, rs.getString("OdDatuma"));
			ps.setString(4, rs.getString("DoDatuma"));
			ps.setString(5, rs.getString("Opis"));
			ps.addBatch();
			i++;
		}
		ps.executeBatch();
		con2.commit();
		System.out.println(String.format("%-26s -> %7d", "PTStupciVRMirovanja", i));
	}
	

	private static void doPTPostajeVarijantVR(Connection con1, Connection con2) throws SQLException {
		int i = 0;
		con2.createStatement().executeUpdate("drop table if exists PTPostajeVarijantVR;");
		con2.createStatement().executeUpdate("CREATE TABLE PTPostajeVarijantVR(ID INT NOT NULL,VarijantaID INT NOT NULL,NodePostajeVRID INT NOT NULL,ZapSt INT,KumDistancaM INT,DistancaM INT,Vozel INT,Staje CHAR,DOSID INT,PRIMARY KEY (ID),FOREIGN KEY (NodePostajeVRID) REFERENCES PTPostajeVR (ID),FOREIGN KEY (VarijantaID) REFERENCES PTVarijanteVR (ID))");
		con2.createStatement().executeUpdate("CREATE INDEX [IXPostajaVarVRVarijantaID] ON [PTPostajeVarijantVR] ([VarijantaID],[NodePostajeVRID])");
		con2.createStatement().executeUpdate("CREATE INDEX [IXPostajaVarVRNodePostajeVRID] ON [PTPostajeVarijantVR] ([NodePostajeVRID])");
		PreparedStatement ps = con2.prepareStatement("INSERT INTO PTPostajeVarijantVR VALUES (?,?,?,?,?,?,?,?,?)");
		ResultSet rs = con1.createStatement().executeQuery("SELECT * FROM PTPostajeVarijantVR WHERE VarijantaID IN(select ID from PTVarijanteVR where VozniRedID IN (SELECT ID FROM PTVozniRedi WHERE  VrstaVR=1 and veljaDo >= GETDATE()))");
		while (rs.next()) {
			ps.setInt(1, rs.getInt("ID"));
			ps.setInt(2, rs.getInt("VarijantaID"));
			ps.setInt(3, rs.getInt("NodePostajeVRID"));
			ps.setInt(4, rs.getInt("ZapSt"));
			ps.setInt(5, rs.getInt("KumDistancaM"));
			ps.setInt(6, rs.getInt("DistancaM"));
			ps.setInt(7, rs.getInt("Vozel"));
			ps.setString(8, rs.getString("Staje"));
			ps.setInt(9, rs.getInt("DOSID"));
			ps.addBatch();
			i++;
		}
		ps.executeBatch();
		con2.commit();
		System.out.println(String.format("%-26s -> %7d", "PTPostajeVarijantVR", i));
		rs.close();
		ps.close();
	}

	private static void doPTPostajeVR(Connection con1, Connection con2) throws SQLException {
		int i = 0;
		con2.createStatement().executeUpdate("drop table if exists PTPostajeVR;");
		con2.createStatement().executeUpdate("CREATE TABLE PTPostajeVR(ID INT NOT NULL,VozniRedID INT NOT NULL,ZapSt INT NOT NULL, Vozel INT NOT NULL,PostajaID INT NOT NULL,KumDistancaM INT,DistancaM INT,Staje CHAR,DosID INT,PRIMARY KEY (ID),FOREIGN KEY (PostajaID) REFERENCES PTPostaje (ID) ,FOREIGN KEY (VozniRedID) REFERENCES PTVozniRedi (ID))");
		con2.createStatement().executeUpdate("CREATE INDEX [IXPostajeVRVozniRedID] ON [PTPostajeVR] ([VozniRedID],[ZapSt])");
		PreparedStatement ps = con2.prepareStatement("INSERT INTO PTPostajeVR VALUES (?,?,?,?,?,?,?,?,?)");
		ResultSet rs = con1.createStatement().executeQuery("SELECT * FROM PTPostajeVR WHERE VozniRedID IN (SELECT ID FROM PTVozniRedi WHERE  VrstaVR=1 and veljaDo >= GETDATE())");
		while (rs.next()) {
			ps.setInt(1, rs.getInt("ID"));
			ps.setInt(2, rs.getInt("VozniRedID"));
			ps.setInt(3, rs.getInt("ZapSt"));
			ps.setInt(4, rs.getInt("Vozel"));
			ps.setInt(5, rs.getInt("PostajaID"));
			ps.setInt(6, rs.getInt("KumDistancaM"));
			ps.setInt(7, rs.getInt("DistancaM"));
			ps.setString(8, rs.getString("Staje"));
			ps.setInt(9, rs.getInt("DosID"));
			ps.addBatch();
			i++;
		}
		ps.executeBatch();
		con2.commit();
		System.out.println(String.format("%-26s -> %7d", "PTPostajeVR", i));
		rs.close();
		ps.close();
	}

	private static void doPTPostaje(Connection con1, Connection con2) throws SQLException {
		int i = 0;
		con2.createStatement().executeUpdate("drop table if exists PTPostaje;");
		con2.createStatement().executeUpdate("CREATE TABLE PTPostaje(ID INT NOT NULL,Sifra VARCHAR(12),Naziv VARCHAR(50),VrstaPostaje INT,Drzava INT,PGRID INT,Prodaja CHAR(1),PRIMARY KEY (ID))");
		con2.createStatement().executeUpdate("CREATE INDEX [IXPostajeVRNaziv] ON [PTPostaje] ([Naziv])");
		PreparedStatement ps = con2.prepareStatement("INSERT INTO PTPostaje VALUES (?,?,?,?,?,?,?)");
		ResultSet rs = con1.createStatement().executeQuery("SELECT * FROM PTPostaje");
		while (rs.next()) {
			ps.setInt(1, rs.getInt("ID"));
			ps.setString(2, rs.getString("Sifra"));
			ps.setString(3, rs.getString("Naziv"));
			ps.setInt(4, rs.getInt("VrstaPostaje"));
			ps.setInt(5, rs.getInt("Drzava"));
			ps.setInt(6, rs.getInt("PGRID"));
			ps.setString(7, rs.getString("Prodaja"));
			ps.addBatch();
			i++;
		}
		ps.executeBatch();
		con2.commit();
		System.out.println(String.format("%-26s -> %7d", "PTPostaje", i));
		rs.close();
		ps.close();
	}

	private static void doPTCasiVoznjeVR(Connection con1, Connection con2) throws SQLException {
		int i = 0;
		con2.createStatement().executeUpdate("drop table if exists PTCasiVoznjeVR;");
		con2.createStatement().executeUpdate("CREATE TABLE PTCasiVoznjeVR(ID INT NOT NULL,Firma INT NOT NULL,StupacVRID INT NOT NULL,NodePostajeVarijanteVRID INT NOT NULL,VremeOdhoda FLOAT,VremePrihoda FLOAT,PRIMARY KEY (ID),FOREIGN KEY (NodePostajeVarijanteVRID) REFERENCES PTPostajeVarijantVR (ID),FOREIGN KEY (StupacVRID) REFERENCES PTStupciVR (ID))");
		con2.createStatement().executeUpdate("CREATE INDEX [IXCasiVoznjeVRStupacID] ON [PTCasiVoznjeVR] ([StupacVRID],[NodePostajeVarijanteVRID])");
		PreparedStatement ps = con2.prepareStatement("INSERT INTO PTCasiVoznjeVR VALUES (?,?,?,?,?,?)");
		ResultSet rs = con1.createStatement().executeQuery("SELECT * FROM PTCasiVoznjeVR WHERE StupacVRID IN(SELECT ID FROM PTStupciVR WHERE VarijantaVRID IN (select ID from PTVarijanteVR where VozniRedID IN (SELECT ID FROM PTVozniRedi WHERE  VrstaVR=1 and veljaDo >= GETDATE())))");
		while (rs.next()) {
			ps.setInt(1, rs.getInt("ID"));
			ps.setInt(2, rs.getInt("Firma"));
			ps.setInt(3, rs.getInt("StupacVRID"));
			ps.setInt(4, rs.getInt("NodePostajeVarijanteVRID"));
			ps.setDouble(5, rs.getDouble("VremeOdhoda"));
			ps.setDouble(6, rs.getDouble("VremePrihoda"));
			ps.addBatch();
			i++;
		}
		ps.executeBatch();
		con2.commit();
		System.out.println(String.format("%-26s -> %7d", "PTCasiVoznjeVR", i));
		rs.close();
		ps.close();
	}

	private static void doPTKTVozneKarte(Connection con1, Connection con2) throws SQLException {
		int i = 0;
		con2.createStatement().executeUpdate("drop table if exists PTKTVozneKarte");
		con2.createStatement().executeUpdate("CREATE TABLE PTKTVozneKarte(ID INT NOT NULL , Firma INT NOT NULL,Sifra VARCHAR(10) NOT NULL,Oznaka VARCHAR(16) ,TipKarteID INT NOT NULL,TarifniRazredID INT,Opis VARCHAR(50) ,StVoznji INT,SmerVoznje INT,OdDanaM INT,DoDanaM INT,VeljaDniOdProdaje INT,Status INT,PrevoznikID INT,NacinDolocanjaCene INT,KMPogoja INT,FiksnaCena FLOAT(53),FaktorCene FLOAT(53),PopustProcent FLOAT(53),SifraValute INT,RoundN FLOAT(53),MobilnaProdaja INT,InternetProdaja INT,DOSID INT,CenaRezervacije FLOAT(53),KmRezervacije INT,KratkiOpis VARCHAR(20), ZamjenskaKarta int, ZBVPID int, PrelaznaKarta int,PRIMARY KEY (ID))");
		PreparedStatement ps = con2.prepareStatement("INSERT INTO PTKTVozneKarte VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
		ResultSet rs = con1.createStatement().executeQuery("SELECT * FROM PTKTVozneKarte");
		while (rs.next()) {
			ps.setInt(1, rs.getInt("ID"));
			ps.setInt(2, rs.getInt("Firma"));
			ps.setString(3, rs.getString("Sifra"));
			ps.setString(4, rs.getString("Oznaka"));
			ps.setInt(5, rs.getInt("TipKarteID"));
			ps.setInt(6, rs.getInt("TarifniRazredID"));
			ps.setString(7, rs.getString("Opis"));
			ps.setInt(8, rs.getInt("StVoznji"));
			ps.setInt(9, rs.getInt("SmerVoznje"));
			ps.setInt(10, rs.getInt("OdDanaM"));
			ps.setInt(11, rs.getInt("DoDanaM"));
			ps.setInt(12, rs.getInt("VeljaDniOdProdaje"));
			ps.setInt(13, rs.getInt("Status"));
			ps.setInt(14, rs.getInt("PrevoznikID"));
			ps.setInt(15, rs.getInt("NacinDolocanjaCene"));
			ps.setInt(16, rs.getInt("KMPogoja"));
			ps.setDouble(17, rs.getDouble("FiksnaCena"));
			ps.setDouble(18, rs.getDouble("FaktorCene"));
			ps.setDouble(19, rs.getDouble("PopustProcent"));
			ps.setInt(20, rs.getInt("SifraValute"));
			ps.setDouble(21, rs.getDouble("RoundN"));
			ps.setInt(22, rs.getInt("MobilnaProdaja"));
			ps.setInt(23, rs.getInt("InternetProdaja"));
			ps.setInt(24, rs.getInt("DOSID"));
			ps.setDouble(25, rs.getDouble("CenaRezervacije"));
			ps.setInt(26, rs.getInt("KmRezervacije"));
			ps.setString(27, rs.getString("KratkiOpis"));
			ps.setInt(28, rs.getInt("ZamjenskaKarta"));
			ps.setInt(29, rs.getInt("ZBVPID"));
			ps.setInt(30, rs.getInt("PrelaznaKarta"));
			ps.addBatch();
			i++;
		}
		ps.executeBatch();
		// con2.createStatement().execute("insert into PTKTVozneKarte (ID,Firma,Sifra,TipKarteID,kratkiOpis) VALUES (99,5,99,1,'ZAMJENSKA KARTA');");
		con2.createStatement().execute("UPDATE PTKTVozneKarte SET kratkiOpis = 'Dnevna' WHERE kratkiOpis IS NULL");
		con2.commit();
		System.out.println(String.format("%-26s -> %7d", "PTKTVozneKarte", i));
		rs.close();
		ps.close();
	}

	private static void doPTVozaci(Connection con1, Connection con2) throws SQLException {
		int i = 0;
		con2.createStatement().executeUpdate("drop table if exists PTVozaci;");
		con2.createStatement().executeUpdate("CREATE TABLE PTVozaci(ID INT NOT NULL,PGRID INT NOT NULL,Sifra INT NOT NULL,Naziv VARCHAR,OsebaID INT,JeVozac INT,JeKondukter INT,MobilePassword VARCHAR(40),OIB VARCHAR(15),PRIMARY KEY (ID))");
		PreparedStatement ps = con2.prepareStatement("INSERT INTO PTVozaci VALUES (?,?,?,?,?,?,?,?,?)");
		ResultSet rs = con1.createStatement().executeQuery("SELECT * FROM PTVozaci");
		while (rs.next()) {
			ps.setInt(1, rs.getInt("ID"));
			ps.setInt(2, rs.getInt("PGRID"));
			ps.setInt(3, rs.getInt("Sifra"));
			ps.setString(4, rs.getString("Naziv"));
			ps.setInt(5, rs.getInt("OsebaID"));
			ps.setInt(6, rs.getInt("JeVozac"));
			ps.setInt(7, rs.getInt("JeKondukter"));
			ps.setString(8, rs.getString("MobilePassword"));
			ps.setString(9, rs.getString("OIB"));
			ps.addBatch();
			i++;
		}
		ps.executeBatch();
		con2.commit();
		System.out.println(String.format("%-26s -> %7d", "PTVozaci", i));
		rs.close();
		ps.close();
	}

	private static void doSkValute(Connection con1, Connection con2) throws SQLException {
		int i = 0;
		con2.createStatement().executeUpdate("drop table if exists SkValute;");
		con2.createStatement().executeUpdate("CREATE TABLE SkValute(SifraValute INT NOT NULL,OznakaValute VARCHAR(3),NazivValute VARCHAR(30) ,PRIMARY KEY (SifraValute))");
		PreparedStatement ps = con2.prepareStatement("INSERT INTO SkValute VALUES (?,?,?)");
		ResultSet rs = con1.createStatement().executeQuery("SELECT * FROM SKValute");
		while (rs.next()) {
			ps.setInt(1, rs.getInt("SifraValute"));
			ps.setString(2, rs.getString("OznakaValute"));
			ps.setString(3, rs.getString("NazivValute"));
			ps.addBatch();
			i++;
		}
		ps.executeBatch();
		con2.commit();
		System.out.println(String.format("%-26s -> %7d", "SkValute", i));
		rs.close();
		ps.close();
	}

	private static void doPTKTTarifniRazrediCenik(Connection con1, Connection con2) throws SQLException {
		int i = 0;
		con2.createStatement().executeUpdate("drop table if exists PTKTTarifniRazrediCenik;");
		con2.createStatement().executeUpdate("CREATE TABLE PTKTTarifniRazrediCenik(ID INT NOT NULL ,IDRazreda INT NOT NULL,VeljaOd DATETIME NOT NULL,OdKM INT NOT NULL,Cena FLOAT(53),DOSID INT,PRIMARY KEY (ID))");
		con2.createStatement().executeUpdate("CREATE INDEX [IXTarRazCenikIDRazreda]  ON [PTKTTarifniRazrediCenik] ([IDRazreda],[VeljaOd])");
		PreparedStatement ps = con2.prepareStatement("INSERT INTO PTKTTarifniRazrediCenik VALUES (?,?,?,?,?,?)");
		ResultSet rs = con1.createStatement().executeQuery("SELECT * FROM PTKTTarifniRazrediCenik a WHERE IDRazreda IN ( SELECT DISTINCT TarifniRazredID FROM PTKTVozneKarte WHERE MobilnaProdaja = 1) AND VeljaOD = (SELECT MAX(VeljaOd) FROM PTKTTarifniRazrediCenik b WHERE b.IDRazreda = a.IDRazreda AND b.OdKM = a.OdKM AND b.VeljaOd <= GETDATE()+15)");
		while (rs.next()) {
			ps.setInt(1, rs.getInt("ID"));
			ps.setInt(2, rs.getInt("IDRazreda"));
			ps.setString(3, rs.getString("VeljaOd"));
			ps.setInt(4, rs.getInt("OdKM"));
			ps.setDouble(5, rs.getDouble("Cena"));
			ps.setInt(6, rs.getInt("DOSID"));
			ps.addBatch();
			i++;
		}
		ps.executeBatch();
		con2.commit();
		System.out.println(String.format("%-26s -> %7d", "PTKTTarifniRazrediCenik", i));
		rs.close();
		ps.close();
	}

	private static void doPTKTVrstePopustov(Connection con1, Connection con2) throws SQLException {
		int i = 0;
		con2.createStatement().executeUpdate("drop table if exists PTKTVrstePopustov;");
		con2.createStatement().executeUpdate("CREATE TABLE PTKTVrstePopustov(ID INT NOT NULL ,Firma INT NOT NULL,Sifra INT NOT NULL,Oznaka VARCHAR(10)  NOT NULL,Opis VARCHAR(50) ,NacinIzracuna INT,Vrednost FLOAT(53),PRIMARY KEY (ID))");
		PreparedStatement ps = con2.prepareStatement("INSERT INTO PTKTVrstePopustov VALUES (?,?,?,?,?,?,?)");
		ResultSet rs = con1.createStatement().executeQuery("SELECT * FROM PTKTVrstePopustov");
		while (rs.next()) {
			ps.setInt(1, rs.getInt("ID"));
			ps.setInt(2, rs.getInt("Firma"));
			ps.setInt(3, rs.getInt("Sifra"));
			ps.setString(4, rs.getString("Oznaka"));
			ps.setString(5, rs.getString("Opis"));
			ps.setInt(6, rs.getInt("NacinIzracuna"));
			ps.setDouble(7, rs.getDouble("Vrednost"));
			ps.addBatch();
			i++;
		}
		ps.executeBatch();
		con2.commit();
		System.out.println(String.format("%-26s -> %7d", "PTKTVrstePopustov", i));
		rs.close();
		ps.close();
	}
	private static void doPTIzjemeCenikaVR(Connection con1, Connection con2) throws SQLException {
		int i = 0;
		con2.createStatement().executeUpdate("drop table if exists PTIzjemeCenikaVR;");
		con2.createStatement().executeUpdate("CREATE TABLE PTIzjemeCenikaVR(ID INT NOT NULL,Firma INT NOT NULL,TarifniCenikID INT NOT NULL,VeljaOd DATETIME NOT NULL,VozniRedID INT NOT NULL,VarijantaID INT NOT NULL,StupacID INT NOT NULL,Postaja1ID INT NOT NULL,Postaja2ID INT NOT NULL,Cena FLOAT(53),PRIMARY KEY (ID))");
		con2.createStatement().executeUpdate("CREATE INDEX [IXIzjemaTRID]  ON [PTIzjemeCenikaVR] ([TarifniCenikID],[VeljaOd])");
		PreparedStatement ps = con2.prepareStatement("INSERT INTO PTIzjemeCenikaVR VALUES (?,?,?,?,?,?,?,?,?,?)");
		ResultSet rs = con1.createStatement().executeQuery("SELECT * FROM PTIzjemeCenikaVR");
		while (rs.next()) {
			ps.setInt(1, rs.getInt("ID"));
			ps.setInt(2, rs.getInt("Firma"));
			ps.setInt(3, rs.getInt("TarifniCenikID"));
			ps.setString(4, rs.getString("VeljaOd"));
			ps.setInt(5, rs.getInt("VozniRedID"));
			ps.setInt(6, rs.getInt("VarijantaID"));
			ps.setInt(7, rs.getInt("StupacID"));
			ps.setInt(8, rs.getInt("Postaja1ID"));
			ps.setInt(9, rs.getInt("Postaja2ID"));
			ps.setDouble(10, rs.getDouble("Cena"));
			ps.addBatch();
			i++;
		}
		ps.executeBatch();
		con2.commit();
		System.out.println(String.format("%-26s -> %7d", "PTIzjemeCenikaVR", i));
		rs.close();
		ps.close();
	}

	private static void doPTKTProdaja(Connection con1, Connection con2) throws SQLException {
		int i = 0;
		con2.createStatement().executeUpdate("drop table if exists PTKTProdaja;");
		con2.createStatement().executeUpdate("drop table if exists PTKTObracun;");
		con2.createStatement().executeUpdate("drop table if exists PTKTRacuni;");

		con2.createStatement().executeUpdate("CREATE TABLE PTKTRacuni(ID INTEGER PRIMARY KEY, Firma INT NOT NULL, PrefiksRacuna Varchar(15), BrojRacuna INT, Datum DATETIME, Vreme DATETIME, " +
                                             "FiskalStatus INT, ZKI VARCHAR(40), JIR VARCHAR(40))");
		con2.createStatement().executeUpdate("CREATE INDEX [IXRacunBroj] ON [PTKTRacuni] ([PrefiksRacuna],[BrojRacuna],[Firma])");

		con2.createStatement().executeUpdate("CREATE TABLE PTKTProdaja(ID INTEGER PRIMARY KEY ,Firma INT NOT NULL, Stevilka INT, DokumentProdajeID INT,VrsticaProdajeID INT,DokumentBlagajneID INT,BUSProdajaID INT,Datum DATETIME,Vreme DATETIME,VoznaKartaID INT,Code VARCHAR(20) ,BRVoznji INT, SifraValute INT, Cena FLOAT(53),Popust1ID INT,Popust2ID INT,Popust3ID INT,PCenaKarte FLOAT(53),NCenaKarte FLOAT(53),Popust FLOAT(53),ZaPlatiti FLOAT(53),PorezProcent INT, UdioPoreza FLOAT(53), ProdajnoMestoID INT,PrevoznikID INT,VrstaPosadeID INT,Vozac1ID INT,Vozac2ID INT,Vozac3ID INT,Blagajnik INT,Blagajna INT,StupacID INT,OdPostajeID INT,DoPostajeID INT,VoziloID INT,Rezervacija INT,StatusZK INT,KmLinijeVR INT,KmDomaci INT,KmIno INT," +
                                             "BRPutnika INT,BRKarata INT,MobStrojID INT,ObracunID INT, MobRacunID INT)");
		con2.createStatement().executeUpdate("CREATE INDEX [IXPRStevilka] ON [PTKTProdaja] ([Stevilka])");
		con2.createStatement().executeUpdate("CREATE INDEX [IXPRRacunID]  ON [PTKTProdaja] ([MobRacunID])");
		con2.createStatement().executeUpdate("CREATE INDEX [IXPRObracunID]ON [PTKTProdaja] ([ObracunID])");

		con2.createStatement().executeUpdate("CREATE TABLE PTKTObracun(ID INTEGER PRIMARY KEY,Datum DATETIME,VozacID INT,GUID VARCHAR)");
		con2.commit();
		System.out.println(String.format("%-26s -> %7d", "Prodaja/Racuni/Obracuni", i));
	}


	private static void doPTKTPopusti(Connection con1, Connection con2) throws SQLException {
		int i = 0;
		con2.createStatement().executeUpdate("drop table if exists PTKTPopusti;");
		con2.createStatement().executeUpdate("CREATE TABLE PTKTPopusti(ID INT NOT NULL , PrevoznikID INT NOT NULL,TipKarteID INT NOT NULL, Opis VARCHAR(40), KratkiOpis VARCHAR(20), StupacID INT, VeljaOd DATETIME NOT NULL, VrstaPopustaID INT NOT NULL, NacinIzracuna INT,Popust FLOAT(53),PRIMARY KEY (ID))");
		con2.createStatement().executeUpdate("CREATE INDEX [IXPopTipKarteID] ON [PTKTPopusti] ([TipKarteID],[VeljaOd])");
		PreparedStatement ps = con2.prepareStatement("INSERT INTO PTKTPopusti VALUES (?,?,?,?,?,?,?,?,?,?)");
		ResultSet rs = con1.createStatement().executeQuery("SELECT * FROM PTKTPopusti");
		while (rs.next()) {
			ps.setInt(1, rs.getInt("ID"));
			ps.setInt(2, rs.getInt("PrevoznikID"));
			ps.setInt(3, rs.getInt("TipKarteID"));
			ps.setString(4, rs.getString("Opis"));
			ps.setString(5, rs.getString("KratkiOpis"));
			ps.setInt(6, rs.getInt("StupacID"));
			ps.setString(7, rs.getString("VeljaOd"));
			ps.setInt(8, rs.getInt("VrstaPopustaID"));
			ps.setInt(9, rs.getInt("NacinIzracuna"));
			ps.setDouble(10, rs.getDouble("Popust"));
			ps.addBatch();
			i++;
		}
		ps.executeBatch();
		con2.commit();
		System.out.println(String.format("%-26s -> %7d", "PTKTPopusti", i));
	}

	private static void doPTKTTipiKarti(Connection con1, Connection con2) throws SQLException {
		int i = 0;
		con2.createStatement().executeUpdate("drop table if exists PTKTTipiKarti;");
		con2.createStatement().executeUpdate("CREATE TABLE PTKTTipiKarti(ID INT NOT NULL,PGRID INT NOT NULL,Sifra INT NOT NULL,Oznaka VARCHAR(10) NOT NULL, Povratna INT,Opis VARCHAR(50) , SkupinaKarte INT,KategorijaKarte INT,PRIMARY KEY (ID))");
		PreparedStatement ps = con2.prepareStatement("INSERT INTO PTKTTipiKarti VALUES (?,?,?,?,?,?,?,?)");
		ResultSet rs = con1.createStatement().executeQuery("SELECT * FROM PTKTTipiKarti");
		while (rs.next()) {
			ps.setInt(1, rs.getInt("ID"));
			ps.setInt(2, rs.getInt("PGRID"));
			ps.setInt(3, rs.getInt("Sifra"));
			ps.setString(4, rs.getString("Oznaka"));
			ps.setInt(5, rs.getInt("Povratna"));
			ps.setString(6, rs.getString("Opis"));
			ps.setInt(7, rs.getInt("SkupinaKarte"));
			ps.setInt(8, rs.getInt("KategorijaKarte"));
			ps.addBatch();
			i++;
		}
		ps.executeBatch();
		con2.commit();
		System.out.println(String.format("%-26s -> %7d", "PTKTTipiKarti", i));
	}

	private static void doPromVozila(Connection con1, Connection con2) throws SQLException {
		int i = 0;
		con2.createStatement().executeUpdate("drop table if exists PromVozila;");
		con2.createStatement().executeUpdate("CREATE TABLE PromVozila(ID INT NOT NULL,Firma INT NOT NULL,Sifra INT NOT NULL,PGRID INT,Status INT,GUID VARCHAR(40),Naziv VARCHAR(40),RegSt VARCHAR(12),LastnikVA VARCHAR(3),LastnikAnalitika INT,VrstaID INT,ZnamkaID INT,TipID INT,ModelID INT,OblikaKaroserije INT,Namena INT,Barva VARCHAR(20),StSasije VARCHAR(30),DrzavaIzdelave INT,LetoIzdelave DATETIME,PrvaRegistracija DATETIME,StSedezev INT,StStojisc INT,StLezisc INT,TezaPraznega INT,MaxTeza INT,MaxHitrost INT,StOsovin INT,VrstaMotorja INT,EuroStandard INT,MocKW INT,Vrtljaji INT,ZapremninaMotorja INT,DimDolzina INT,DimSirina INT,DimVisina INT,DimVolumen INT,StKoles INT,DimezijeGumPrednje VARCHAR(20),DimenzijeGumZadnje VARCHAR(20),VrstaZavor INT,Vleka INT,Vitlo INT,Opomba VARCHAR, PRIMARY KEY (ID))");
		PreparedStatement ps = con2.prepareStatement("INSERT INTO PromVozila (ID,Firma,Sifra,PGRID,Status,GUID,Naziv,RegSt,LastnikVA) VALUES (?,?,?,?,?,?,?,?,?)");
		ResultSet rs = con1.createStatement().executeQuery("SELECT * FROM PromVozila");
		while (rs.next()) {
			ps.setInt(1, rs.getInt("ID"));
			ps.setInt(2, rs.getInt("Firma"));
			ps.setInt(3, rs.getInt("Sifra"));
			ps.setInt(4, rs.getInt("PGRID"));
			ps.setInt(5, rs.getInt("Status"));
			ps.setString(6, rs.getString("GUID"));
			ps.setString(7, rs.getString("Naziv"));
			ps.setString(8, rs.getString("RegSt"));
			ps.setString(9, rs.getString("LastnikVA"));
			ps.addBatch();
			i++;
		}
		ps.executeBatch();
		con2.commit();
		System.out.println(String.format("%-26s -> %7d", "PromVozila", i));
	}

	private static void doPromAlmexMasine(Connection con1, Connection con2) throws SQLException {
		int i = 0;
		con2.createStatement().executeUpdate("drop table if exists ZBAlmexMasine;");
		con2.createStatement().executeUpdate("CREATE TABLE ZBAlmexMasine(ID INT NOT NULL,Pgrid INT NOT NULL,Sifra INT NOT NULL,Naziv VARCHAR(40), PRIMARY KEY (ID))");
		PreparedStatement ps = con2.prepareStatement("INSERT INTO ZBAlmexMasine (ID,PGRID,Sifra,Naziv) VALUES (?,?,?,?)");
		ResultSet rs = con1.createStatement().executeQuery("SELECT * FROM ZBAlmexMasine");
		while (rs.next()) {
			ps.setInt(1, rs.getInt("ID"));
			ps.setInt(2, rs.getInt("PGRID"));
			ps.setInt(3, rs.getInt("Sifra"));
			ps.setString(4, rs.getString("Naziv"));
			ps.addBatch();
			i++;
		}
		ps.executeBatch();
		con2.commit();
		System.out.println(String.format("%-26s -> %7d", "ZBAlmexMasine", i));
	}

	private static void doPTProdajnaMesta(Connection con1, Connection con2) throws SQLException {
		int i = 0;
		con2.createStatement().executeUpdate("drop table if exists PTProdajnaMesta;");
		con2.createStatement().executeUpdate("CREATE TABLE PTProdajnaMesta(ID INT NOT NULL ,Firma INT NOT NULL,Sifra INT NOT NULL, Oznaka VARCHAR(10),Naziv VARCHAR(50),Partner INT,Strm INT,OE INT,PostajaID INT,PRIMARY KEY (ID))");
		PreparedStatement ps = con2.prepareStatement("INSERT INTO PTProdajnaMesta VALUES (?,?,?,?,?,?,?,?,?)");
		ResultSet rs = con1.createStatement().executeQuery("SELECT * FROM PTProdajnaMesta WHERE Firma = 5");
		while (rs.next()) {
			ps.setInt(1, rs.getInt("ID"));
			ps.setInt(2, rs.getInt("Firma"));
			ps.setInt(3, rs.getInt("Sifra"));
			ps.setString(4, rs.getString("Oznaka"));
			ps.setString(5, rs.getString("Naziv"));
			ps.setInt(6, rs.getInt("Partner"));
			ps.setInt(7, rs.getInt("Strm"));
			ps.setInt(8, rs.getInt("OE"));
			ps.setInt(9, rs.getInt("PostajaID"));
			ps.addBatch();
			i++;
		}
		ps.executeBatch();
		con2.commit();
		System.out.println(String.format("%-26s -> %7d", "PTProdajnaMesta", i));
	}


	private static void doPTPrevozniki(Connection con1, Connection con2) throws SQLException {
		int i = 0;
		con2.createStatement().executeUpdate("drop table if exists PTPrevozniki;");
		con2.createStatement().executeUpdate("CREATE TABLE PTPrevozniki(ID INT NOT NULL,Firma INT NOT NULL,Sifra int not Null, Naziv VARCHAR(40),PRIMARY KEY (ID))");
		PreparedStatement ps = con2.prepareStatement("INSERT INTO PTPrevozniki VALUES (?,?,?,?)");
		ResultSet rs = con1.createStatement().executeQuery("SELECT * FROM PTPrevozniki");
		while (rs.next()) {
			ps.setInt(1, rs.getInt("ID"));
			ps.setInt(2, rs.getInt("Firma"));
			ps.setInt(3, rs.getInt("Sifra"));
			ps.setString(4, rs.getString("Naziv"));
			ps.addBatch();
			i++;
		}
		ps.executeBatch();
		con2.commit();
		System.out.println(String.format("%-26s -> %7d", "PTPrevozniki", i));
	}

	private static void doPTDneviVoznje(Connection con1, Connection con2) throws SQLException {
		int i = 0;
		con2.createStatement().executeUpdate("drop table if exists PTDneviVoznje;");
		con2.createStatement().executeUpdate("CREATE TABLE PTDneviVoznje(ID INT NOT NULL,PGrid INT NOT NULL,Sifra int not Null, VoziOb varchar(10), Naziv VARCHAR(80),PRIMARY KEY (ID))");
		PreparedStatement ps = con2.prepareStatement("INSERT INTO PTDneviVoznje VALUES (?,?,?,?,?)");
		ResultSet rs = con1.createStatement().executeQuery("SELECT * FROM PTDneviVoznje");
		while (rs.next()) {
			ps.setInt(1, rs.getInt("ID"));
			ps.setInt(2, rs.getInt("Pgrid"));
			ps.setInt(3, rs.getInt("Sifra"));
			ps.setString(4, rs.getString("VoziOb"));
			ps.setString(5, rs.getString("Naziv"));
			ps.addBatch();
			i++;
		}
		ps.executeBatch();
		con2.commit();
		System.out.println(String.format("%-26s -> %7d", "PTDneviVoznje", i));
	}

	// ******************* MESECNE KARTE *******************************
	private static void doPTMesRelacije(Connection con1, Connection con2) throws SQLException {
		int i = 0;
		con2.createStatement().executeUpdate("drop table if exists PTMesRelacije;");
		con2.createStatement().executeUpdate("CREATE TABLE PTMesRelacije(ID INT NOT NULL,Pgrid INT NOT NULL, Sifra int not Null,PrevoznikKarteID int," + "Opis1 varchar(80),Opis2 VARCHAR(80)," + "OdPostajeID int, Via1PostajaID int, Via2PostajaID int, DoPostajeID int," + "DistancaM int, DistancaVia1M int, DistancaVia2M," + "PRIMARY KEY (ID))");
		PreparedStatement ps = con2.prepareStatement("INSERT INTO PTMesRelacije VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)");

		// svi vazeci zadnjih 30 dana ili koji imaju neograniceno vazenje
		ResultSet rs = con1.createStatement().executeQuery("SELECT * FROM PTMesRelacije");
		while (rs.next()) {
			ps.setInt(1, rs.getInt("ID"));
			ps.setInt(2, rs.getInt("Pgrid"));
			ps.setInt(3, rs.getInt("Sifra"));
			ps.setInt(4, rs.getInt("PrevoznikID"));
			ps.setString(5, rs.getString("Opis1"));
			ps.setString(6, rs.getString("Opis2"));
			ps.setInt(7, rs.getInt("OdPostajeID"));
			ps.setInt(8, rs.getInt("Via1PostajaID"));
			ps.setInt(9, rs.getInt("Via2PostajaID"));
			ps.setInt(10, rs.getInt("DoPostajeID"));
			ps.setInt(11, rs.getInt("DistancaM"));
			ps.setInt(12, rs.getInt("DistancaVia1M"));
			ps.setInt(13, rs.getInt("DistancaVia2M"));
			ps.addBatch();
			i++;
		}
		ps.executeBatch();
		con2.commit();
		System.out.println(String.format("%-26s -> %7d", "PTMesRelacije", i));
	}

	private static void doPTMesOsebe(Connection con1, Connection con2) throws SQLException {
		int i = 0;
		con2.createStatement().executeUpdate("drop table if exists PTMesOsebe;");
		con2.createStatement().executeUpdate("CREATE TABLE PTMesOsebe(ID INT NOT NULL,Firma INT NOT NULL, Sifra int not Null," + "TipKarteID int,VoznaKartaID int,MesRelacijaID int,ProdajnoMestoID int,PrevoznikID int," + "OIB varchar(20),ImePriimek VARCHAR(30),Ulica varchar(30),Kraj VARCHAR(30)," + "DatumRojstva datetime, VeljaOd datetime, VeljaDo DateTime, " + "StatusBlokiran int, GodSkola int, Razred int," + "PRIMARY KEY (ID), FOREIGN KEY (MesRelacijaID) REFERENCES PTMesRelacije(ID))");
		PreparedStatement ps = con2.prepareStatement("INSERT INTO PTMesOsebe VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");

		// svi vazeci zadnjih 3 mjeseci ili koji imaju neograniceno valjanost
		ResultSet rs = con1.createStatement().executeQuery("SELECT * FROM PTMesOsebe where VeljaDo >= GETDATE()-60 or VeljaDo is null");
		while (rs.next()) {
			ps.setInt(1, rs.getInt("ID"));
			ps.setInt(2, rs.getInt("Firma"));
			ps.setInt(3, rs.getInt("Sifra"));
			ps.setInt(4, rs.getInt("TipKarteID"));
			ps.setInt(5, rs.getInt("VoznaKartaID"));
			ps.setInt(6, rs.getInt("MesRelacijaID"));
			ps.setInt(7, rs.getInt("ProdajnoMestoID"));
			ps.setInt(8, rs.getInt("PrevoznikID"));
			ps.setString(9, rs.getString("OIB"));
			ps.setString(10, rs.getString("ImePriimek"));
			ps.setString(11, rs.getString("Ulica"));
			ps.setString(12, rs.getString("Kraj"));
			ps.setString(13, rs.getString("DatumRojstva"));
			ps.setString(14, rs.getString("VeljaOd"));
			ps.setString(15, rs.getString("VeljaDo"));
			ps.setInt(16, rs.getInt("StatusBlokiran"));
			ps.setInt(17, rs.getInt("GodSkola"));
			ps.setInt(18, rs.getInt("Razred"));
			ps.addBatch();
			i++;
		}
		ps.executeBatch();
		con2.commit();
		System.out.println(String.format("%-26s -> %7d", "PTMesOsebe", i));
	}

	// ***** kodovi za iskaznice, rfid kartice i sl ********
	private static void doPTMesCode(Connection con1, Connection con2) throws SQLException {
		int i = 0;
		con2.createStatement().executeUpdate("drop table if exists PTMesCode;");
		con2.createStatement().executeUpdate("CREATE TABLE PTMesCode(ID INT NOT NULL,Firma INT NOT NULL, Vrsta int not null" + ",Code varchar(20) not null, MesUporabnikID not null" + ",PRIMARY KEY (ID), FOREIGN KEY (MesUporabnikID) REFERENCES PTMesUporabniki (ID))");
		con2.createStatement().executeUpdate("CREATE INDEX [IXMesCode] ON [PTMesCode] ([Code], [Vrsta], [Firma])");
		PreparedStatement ps = con2.prepareStatement("INSERT INTO PTMesCode VALUES (?,?,?,?,?)");
		ResultSet rs = con1.createStatement().executeQuery("SELECT * FROM PTMesCode");
		while (rs.next()) {
			ps.setInt(1, rs.getInt("ID"));
			ps.setInt(2, rs.getInt("Firma"));
			ps.setInt(3, rs.getInt("Vrsta"));
			ps.setString(4, rs.getString("Code"));
			ps.setInt(5, rs.getInt("MesUporabnikID"));
			ps.addBatch();
			i++;
		}
		ps.executeBatch();
		con2.commit();
		System.out.println(String.format("%-26s -> %7d", "PTMesCode", i));
	}

	// ***** prodaja mjesecnih karata ********
	private static void doPTMesProdaja(Connection con1, Connection con2) throws SQLException {
		int i = 0;

		// **** sva prodaja za zadnja tri mjeseca bude unutra *****
		Calendar cal = Calendar.getInstance();
		// cal.add(Calendar.DATE, -30);
		cal.add(Calendar.MONTH, -7);
		Date Datum = cal.getTime();

		con2.createStatement().executeUpdate("drop table if exists PTMesProdaja;");
		con2.createStatement().executeUpdate("CREATE TABLE PTMesPRodaja(ID INT NOT NULL,Firma INT NOT NULL,SifraMarkice int not null" + ",PER varchar(7) not null, MesOsebaID not null, PrevoznikID int, TipKarteID int, VoznaKartaID int" + ",MesRelacijaID not null, DistancaM int, BrVoznji int, PcenaKarte FLOAT(53), ZaPlatiti FLOAT(53)" + ",Datum DateTime, Storno int, Duplikat int, ProdajnoMestoID int" + ",VeljaOdDanaMes Smallint, VeljaDoDanaMes smallint, VeljaDniOdProdaje smallint " + ",PRIMARY KEY (ID)" + ",FOREIGN KEY (MesOsebaID)    REFERENCES PTMesUporabniki (ID) " + ",FOREIGN KEY (MesRelacijaID) REFERENCES PTMesRelacije (ID) " + ")");
		con2.createStatement().executeUpdate("CREATE INDEX [IXMesOsebaID] ON [PTMesProdaja] ([MesOsebaID],  [Firma])");
		con2.createStatement().executeUpdate("CREATE INDEX [IXMesMarkica] ON [PTMesProdaja] ([SifraMarkice],[Firma])");

		PreparedStatement ps = con2.prepareStatement("INSERT INTO PTMesProdaja VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
		ResultSet rs = con1.createStatement().executeQuery("SELECT * FROM PTMesProdaja where PER>=" + PER_String(Datum));
		while (rs.next()) {
			ps.setInt(1, rs.getInt("ID"));
			ps.setInt(2, rs.getInt("Firma"));
			ps.setInt(3, rs.getInt("SifraMarkice"));
			ps.setString(4, rs.getString("PER"));
			ps.setInt(5, rs.getInt("MesOsebaID"));
			ps.setInt(6, rs.getInt("PrevoznikID"));
			ps.setInt(7, rs.getInt("TipKarteID"));
			ps.setInt(8, rs.getInt("VoznaKartaID"));
			ps.setInt(9, rs.getInt("MesRelacijaID"));
			ps.setInt(10, rs.getInt("DistancaM"));
			ps.setInt(11, rs.getInt("BrVoznji"));
			ps.setDouble(12, rs.getDouble("PcenaKarte"));
			ps.setDouble(13, rs.getDouble("ZaPlatiti"));
			ps.setString(14, rs.getString("Datum"));
			ps.setInt(15, rs.getInt("Storno"));
			ps.setInt(16, rs.getInt("Duplikat"));
			ps.setInt(17, rs.getInt("ProdajnoMestoID"));
			ps.setShort(18, rs.getShort("VeljaOdDanaMes"));
			ps.setShort(19, rs.getShort("VeljaDoDanaMes"));
			ps.setShort(20, rs.getShort("VeljaDniOdProdaje"));
			ps.addBatch();
			i++;
		}
		ps.executeBatch();
		con2.commit();
		System.out.println(String.format("%-26s -> %7d", "PTMesProdaja", i));
	}

	private static void doPTVerzija(Connection con1, Connection con2) throws SQLException {
		int i = 0;
		con2.createStatement().executeUpdate("drop table if exists PTVersion;");
		con2.createStatement().executeUpdate("CREATE TABLE PTVersion(VerzijaDateTime DATETIME,PRIMARY KEY (VerzijaDateTime))");
		con2.createStatement().executeUpdate("INSERT INTO PTVersion VALUES (datetime('now'))");
		i++;
		con2.commit();
		System.out.println(String.format("%-26s -> %7d", "PTVersion", i));
	}

	private static void doPTMobileSetup(Connection con1, Connection con2) throws SQLException {
		int i = 0;
		con2.createStatement().executeUpdate("drop table if exists PTMobileSetup;");
		con2.createStatement().executeUpdate("Create table if not exists PTMobileSetup(KeyString varchar(30), KeyValue varchar(30), BlobValue BLOB, PRIMARY KEY (KeyString))");
		i++;
		con2.commit();
		System.out.println(String.format("%-26s -> %7d", "PTMobileSetup", i));
	}

	private static void doPTMobilnaZaduzenja(Connection con1, Connection con2) throws SQLException {
		int i = 0;
		con2.createStatement().executeUpdate("Drop table if exists PTMobilnaZaduzenja;");
		con2.createStatement().executeUpdate("CREATE TABLE PTMobilnaZaduzenja(VozacID int, ZBVPID, Datum DATETIME, OdBroja int, DoBroja Int, Cena FLOAT(53), SifraValute int)");
		con2.createStatement().executeUpdate("CREATE INDEX [IXMZVozac] ON [PTMobilnaZaduzenja] ([VozacID])");
		PreparedStatement ps = con2.prepareStatement("INSERT INTO PTMobilnaZaduzenja VALUES (?,?,?,?,?,?,?)");
		ResultSet rs = con1.createStatement().executeQuery("select OBV.VrstaPrometaID, OBR.Datum, OBR.Vozac1ID as VozacID, OBV.zaduzenjeOdBroja as OdBroja, OBV.zaduzenjeDoBroja as DoBroja, " + "OBV.zaduzenjeDoBroja - (coalesce(OBV.ZaduzenjeKomada,0) - coalesce(OBV.BrojKarata,0) - coalesce(OBV.RazduzenjeKomada,0)) + 1 as _OdBroja,Coalesce(OBV.ZaduzenjeCena,0) as Cena, " + "OBV.SifraValute,(coalesce(OBV.ZaduzenjeKomada,0) - coalesce(OBV.BrojKarata,0) - coalesce(OBV.RazduzenjeKomada,0)) as StanjeZaduzenja " + "from ZBObracuniVrstice OBV " + "inner join ZBObracuni OBR ON OBR.ID=OBV.IDDokumenta " + "inner join ZBVrstePrometa VP ON OBV.VrstaPrometaID=VP.ID " + "where VP.Zaduzenja=1 and OBV.ZaduzenjeKomada is not null " + "and (OBV.ZaduzenjeKomada - Coalesce(OBV.BrojKarata,0) - Coalesce(OBV.RazduzenjeKomada,0) > 0)");
		while (rs.next()) {
			ps.setInt(1, rs.getInt("VozacID"));
			ps.setInt(2, rs.getInt("VrstaPrometaID"));
			ps.setString(3, rs.getString("Datum"));
			ps.setInt(4, rs.getInt("_OdBroja")); // calc od broja
			ps.setInt(5, rs.getInt("DoBroja"));
			ps.setDouble(6, rs.getDouble("Cena"));
			ps.setInt(7, rs.getInt("SifraValute"));
			ps.addBatch();
			i++;
		}
		ps.executeBatch();
		con2.commit();
		System.out.println(String.format("%-26s -> %7d", "PTMobilnaZaduzenja", i));
	}

}