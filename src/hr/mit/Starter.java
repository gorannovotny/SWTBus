package hr.mit;

import hr.mit.beans.Stupac;
import hr.mit.beans.Vozac;
import hr.mit.beans.Vozilo;
import hr.mit.windows.LoginWindow;

import java.io.FileInputStream;
import java.util.Properties;

public class Starter {

	public static Vozac vozac = null;
	public static Stupac stupac = null;
	public static Vozilo vozilo = null;

	public static String Prefix = null;
	public static String SifraMobStroja = null;
	public static String ComPortRfid= null;
	public static String ComPortPrinter= null;
	public static String PrintRotate= null;
	public static String DebugMode= null;
	
	public static final Integer KARTA_DEFAULT = 3; 

	public static void main(String[] args) {
		Properties p = new Properties();
		try {
			p.load(new FileInputStream("mobile.conf"));
			Prefix = p.getProperty("Prefix");
			ComPortPrinter = p.getProperty("ComPortPrinter");
			ComPortRfid    = p.getProperty("ComPortRfid");
			SifraMobStroja = p.getProperty("SifraMobStroja");
			PrintRotate    = p.getProperty("PrintRotate");
			DebugMode      = p.getProperty("DebugMode");
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}		
		LoginWindow loginWindow = new LoginWindow();
		loginWindow.open();
	}

}
