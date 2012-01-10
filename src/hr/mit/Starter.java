package hr.mit;

import hr.mit.windows.LoginWindow;
import hr.mit.windows.ProdajaWindow;

public class Starter {

	public static void main(String[] args) {
		LoginWindow loginWindow = new LoginWindow();
		loginWindow.open();
		ProdajaWindow prodajaWindow = new ProdajaWindow();
		prodajaWindow.open();
	}

}
