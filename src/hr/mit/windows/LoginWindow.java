package hr.mit.windows;

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

public class LoginWindow {

	protected Shell shlPrijava;
	private Label lblPrijava;
	private Label lblVoza;
	private Label lblVozilo;
	private Label lblLinija;
	private Label lblPolazak;
	private Combo combo;
	private Combo combo_1;
	private Combo combo_2;
	private Combo combo_3;
	private Button button;

	private VozniRed vozniRed;

	public LoginWindow() {
		vozniRed = new VozniRed(DbUtil.getConnection());
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
			lblVoza = new Label(shlPrijava, SWT.NONE);
			lblVoza.setAlignment(SWT.RIGHT);
			lblVoza.setFont(SWTResourceManager.getFont("Liberation Sans", 30, SWT.NORMAL));
			lblVoza.setBounds(40, 184, 180, 44);
			lblVoza.setText("Vozač");
		}
		{
			combo = new Combo(shlPrijava, SWT.READ_ONLY);
			combo.setFont(SWTResourceManager.getFont("Liberation Sans", 30, SWT.NORMAL));
			combo.setBounds(230, 184, 380, 44);
		}
		{
			lblVozilo = new Label(shlPrijava, SWT.NONE);
			lblVozilo.setAlignment(SWT.RIGHT);
			lblVozilo.setFont(SWTResourceManager.getFont("Liberation Sans", 30, SWT.NORMAL));
			lblVozilo.setBounds(40, 238, 180, 44);
			lblVozilo.setText("Vozilo");
		}
		{
			combo_1 = new Combo(shlPrijava, SWT.READ_ONLY);
			combo_1.setFont(SWTResourceManager.getFont("Liberation Sans", 30, SWT.NORMAL));
			combo_1.setBounds(230, 238, 187, 44);
		}
		{
			lblLinija = new Label(shlPrijava, SWT.NONE);
			lblLinija.setAlignment(SWT.RIGHT);
			lblLinija.setFont(SWTResourceManager.getFont("Liberation Sans", 30, SWT.NORMAL));
			lblLinija.setBounds(40, 292, 180, 44);
			lblLinija.setText("Linija");
		}
		{
			combo_2 = new Combo(shlPrijava, SWT.READ_ONLY);
			combo_2.setFont(SWTResourceManager.getFont("Liberation Sans", 20, SWT.NORMAL));
			combo_2.setBounds(230, 292, 530, 47);
			combo_2.setItems(vozniRed.getVozniRedi());
			combo_2.select(0);
		}
		{
			lblPolazak = new Label(shlPrijava, SWT.NONE);
			lblPolazak.setAlignment(SWT.RIGHT);
			lblPolazak.setFont(SWTResourceManager.getFont("Liberation Sans", 30, SWT.NORMAL));
			lblPolazak.setBounds(40, 346, 180, 44);
			lblPolazak.setText("Polazak");
		}
		{
			combo_3 = new Combo(shlPrijava, SWT.READ_ONLY);
			combo_3.setFont(SWTResourceManager.getFont("Liberation Sans", 30, SWT.NORMAL));
			combo_3.setBounds(230, 346, 187, 44);
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
			shlPrijava.dispose();
		}
	}
}
