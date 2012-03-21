package hr.mit;

import hr.mit.beans.Stupac;
import hr.mit.beans.Vozac;
import hr.mit.beans.Vozilo;
import hr.mit.windows.LoginWindow;
import hr.mit.windows.ProdajaWindow;

public class Starter {

	public static Vozac vozac = null;
	public static Stupac stupac = null;
	public static Vozilo vozilo = null;
	
	public static void main(String[] args) {
		while (true) {
			LoginWindow loginWindow = new LoginWindow();
			ProdajaWindow prodajaWindow = new ProdajaWindow();
			loginWindow.open();
			boolean exit = prodajaWindow.open();
			if (exit) break;
		}
	}

}
