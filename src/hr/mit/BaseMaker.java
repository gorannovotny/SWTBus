package hr.mit;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.DriverManager;

public class BaseMaker {

	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		Class.forName("org.sqlite.JDBC");
		Class.forName("net.sourceforge.jtds.jdbc.Driver");
		Connection con1 = DriverManager.getConnection("jdbc:jtds:sqlserver://tehnoinspekt.dyndns.org:1433;DatabaseName=MitSql", "sa", "SaKlik2003");
		Connection con2 = DriverManager.getConnection("jdbc:sqlite:test.db");
		con2.setAutoCommit(false);

		doPTVozniRedi(con1, con2);
		doPTVarijanteVR(con1, con2);
		doPTStupciVR(con1, con2);
		doPTPostajeVarijantVR(con1, con2);
		doPTPostajeVR(con1, con2);
		doPTPostaje(con1, con2);
		con2.close();
		con1.close();

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
		con2.createStatement().executeUpdate("CREATE TABLE PTStupciVR (ID INT NOT NULL,Firma INT NOT NULL,VozniRedID INT NOT NULL,VarijantaVRID INT NOT NULL,ZapSt VARCHAR NOT NULL,SmerVoznje VARCHAR,DneviVoznjeID INT,PrevoznikID INT,VrstaPrevoza INT,VrstaBusa INT,NacinPrevoza INT,VrstaPosadeID INT,VremeOdhoda FLOAT,OE INT,VeljaOd DATETIME,VeljaDo DATETIME,StatusERR INT,DOSID INT,PRIMARY KEY (ID),FOREIGN KEY (VarijantaVRID) REFERENCES PTVarijanteVR (ID))");
		PreparedStatement ps = con2.prepareStatement("INSERT INTO PTStupciVR VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
		ResultSet rs = con1.createStatement().executeQuery("SELECT * FROM PTStupciVR WHERE VarijantaVRID IN (select ID from PTVarijanteVR where VozniRedID IN (SELECT ID FROM PTVozniRedi WHERE VeljaOd< GETDATE() and veljaDo > GETDATE()))");
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
		con2.createStatement().executeUpdate("CREATE TABLE PTPostajeVarijantVR(ID INT NOT NULL,VarijantaID INT NOT NULL,NodePostajeVRID INT NOT NULL,ZapSt INT,KumDistancaM INT,DistancaM INT,Vozel INT,Staje CHAR,DOSID INT,PRIMARY KEY (ID),FOREIGN KEY (NodePostajeVRID) REFERENCES PTPostajeVR (ID),FOREIGN KEY (VarijantaID) REFERENCES PTVarijanteVR (ID))");
		PreparedStatement ps = con2.prepareStatement("INSERT INTO PTPostajeVarijantVR VALUES (?,?,?,?,?,?,?,?,?)");
		ResultSet rs = con1.createStatement().executeQuery("SELECT * FROM PTPostajeVarijantVR WHERE VarijantaID IN(select ID from PTVarijanteVR where VozniRedID IN (SELECT ID FROM PTVozniRedi WHERE VeljaOd< GETDATE() and veljaDo > GETDATE()))");
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
		con2.createStatement().executeUpdate("CREATE TABLE PTPostajeVR(ID INT NOT NULL,VozniRedID INT NOT NULL,ZapSt INT NOT NULL, Vozel INT NOT NULL,PostajaID INT NOT NULL,KumDistancaM INT,DistancaM INT,Staje CHAR,DosID INT,PRIMARY KEY (ID),FOREIGN KEY (PostajaID) REFERENCES PTPostaje (ID) ,FOREIGN KEY (VozniRedID) REFERENCES PTVozniRedi (ID))");
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
}
