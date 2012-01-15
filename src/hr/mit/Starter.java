package hr.mit;

import hr.mit.windows.LoginWindow;
import hr.mit.windows.ProdajaWindow;

public class Starter {
	public static Integer vozacID;
	public static Integer stupacID;

	public static void main(String[] args) {
		LoginWindow loginWindow = new LoginWindow();
		loginWindow.open();
		ProdajaWindow prodajaWindow = new ProdajaWindow(vozacID, stupacID);
		prodajaWindow.open();
	}

}
