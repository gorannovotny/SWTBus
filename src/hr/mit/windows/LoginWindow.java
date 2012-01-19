package hr.mit.windows;

import java.sql.SQLException;

import hr.mit.Starter;
import hr.mit.beans.StupacList;
import hr.mit.beans.Vozac;
import hr.mit.beans.VozniRed;
import hr.mit.utils.DbUtil;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.ModifyEvent;

public class LoginWindow {

	protected Shell shlPrijava;
	private Label lblPrijava;
	private Label lblVozac;
	private Label lblVozilo;
	private Label lblLinija;
	private Label lblPolazak;
	private Combo comboVozac;
	private Combo comboVozilo;
	private Combo comboLinija;
	private Combo comboPolazak;
	private Button button;

	private VozniRed vozniRed;
	private StupacList polazak;

	public LoginWindow() {
		vozniRed = new VozniRed();
		polazak = new StupacList(DbUtil.getConnection());
	}

	/**
	 * @wbp.parser.entryPoint
	 */
	public void open() {
		Display display = Display.getDefault();
		createContents();
		shlPrijava.open();
		while (!shlPrijava.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	protected void createContents() {
		shlPrijava = new Shell(SWT.SHELL_TRIM);
		shlPrijava.setMaximized(true);
		shlPrijava.setSize(800, 600);
		shlPrijava.setText("Prijava");
		{
			lblPrijava = new Label(shlPrijava, SWT.NONE);
			lblPrijava.setFont(SWTResourceManager.getFont("Liberation Sans", 45, SWT.NORMAL));
			lblPrijava.setBounds(290, 43, 183, 67);
			lblPrijava.setText("Prijava");
		}
		{
			lblVozac = new Label(shlPrijava, SWT.NONE);
			lblVozac.setAlignment(SWT.RIGHT);
			lblVozac.setFont(SWTResourceManager.getFont("Liberation Sans", 30, SWT.NORMAL));
			lblVozac.setBounds(40, 184, 180, 44);
			lblVozac.setText("Vozač");
		}
		{
			comboVozac = new Combo(shlPrijava, SWT.READ_ONLY);
			comboVozac.setFont(SWTResourceManager.getFont("Liberation Sans", 30, SWT.NORMAL));
			comboVozac.setBounds(230, 184, 380, 44);
			comboVozac.setItems(Vozac.getList());
			comboVozac.select(0);
		}
		{
			lblVozilo = new Label(shlPrijava, SWT.NONE);
			lblVozilo.setAlignment(SWT.RIGHT);
			lblVozilo.setFont(SWTResourceManager.getFont("Liberation Sans", 30, SWT.NORMAL));
			lblVozilo.setBounds(40, 238, 180, 44);
			lblVozilo.setText("Vozilo");
		}
		{
			comboVozilo = new Combo(shlPrijava, SWT.READ_ONLY);
			comboVozilo.setFont(SWTResourceManager.getFont("Liberation Sans", 30, SWT.NORMAL));
			comboVozilo.setBounds(230, 238, 187, 44);
		}
		{
			lblLinija = new Label(shlPrijava, SWT.NONE);
			lblLinija.setAlignment(SWT.RIGHT);
			lblLinija.setFont(SWTResourceManager.getFont("Liberation Sans", 30, SWT.NORMAL));
			lblLinija.setBounds(40, 292, 180, 44);
			lblLinija.setText("Linija");
		}
		{
			comboLinija = new Combo(shlPrijava, SWT.READ_ONLY);
			comboLinija.addModifyListener(new Combo_2ModifyListener());
			comboLinija.setFont(SWTResourceManager.getFont("Liberation Sans", 20, SWT.NORMAL));
			comboLinija.setBounds(230, 292, 530, 47);
		}
		{
			lblPolazak = new Label(shlPrijava, SWT.NONE);
			lblPolazak.setAlignment(SWT.RIGHT);
			lblPolazak.setFont(SWTResourceManager.getFont("Liberation Sans", 30, SWT.NORMAL));
			lblPolazak.setBounds(40, 346, 180, 44);
			lblPolazak.setText("Polazak");
		}
		{
			comboPolazak = new Combo(shlPrijava, SWT.READ_ONLY);
			comboPolazak.setFont(SWTResourceManager.getFont("Liberation Sans", 30, SWT.NORMAL));
			comboPolazak.setBounds(230, 346, 187, 44);
			comboLinija.setItems(vozniRed.getList());
			comboLinija.select(0);
		}
		{
			button = new Button(shlPrijava, SWT.NONE);
			button.addSelectionListener(new ButtonSelectionListener());
			button.setFont(SWTResourceManager.getFont("Liberation Sans", 30, SWT.NORMAL));
			button.setBounds(700, 480, 60, 56);
			button.setText(">");
		}

	}

	private class ButtonSelectionListener extends SelectionAdapter {
		public void widgetSelected(SelectionEvent e) {
			Starter.vozacID = Vozac.getVozacID(comboVozac.getSelectionIndex());
			Starter.stupacID = polazak.getPolazakID(comboPolazak.getSelectionIndex());
			shlPrijava.dispose();
		}
	}

	private class Combo_2ModifyListener implements ModifyListener {
		public void modifyText(ModifyEvent arg0) {
			if (comboPolazak != null) {
				try {
					comboPolazak.setItems(polazak.getList(vozniRed.getID(comboLinija.getSelectionIndex())));
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				comboPolazak.select(0);
			}

		}
	}
}
