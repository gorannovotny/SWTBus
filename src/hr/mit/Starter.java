package hr.mit;

import hr.mit.windows.LoginWindow;
import hr.mit.windows.ProdajaWindow;

public class Starter {

	public static void main(String[] args) {
		while (true) {
			LoginWindow loginWindow = new LoginWindow();
			loginWindow.open();
			if (loginWindow.getVozac() != null && loginWindow.getStupac() != null) {
				ProdajaWindow prodajaWindow = new ProdajaWindow(loginWindow.getVozac(), loginWindow.getStupac());
				prodajaWindow.open();
			}
		}
	}

}
