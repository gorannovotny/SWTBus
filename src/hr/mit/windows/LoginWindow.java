package hr.mit.windows;

import java.sql.SQLException;

import hr.mit.Starter;
import hr.mit.beans.StupacList;
import hr.mit.beans.Vozac;
import hr.mit.beans.Vozilo;
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
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;

public class LoginWindow {

	protected Shell shlPrijava;
	private Label lblPrijava;
	private Label lblVozac;
	private Label lblVozilo;
	private Label lblLinija;
	private Label lblPolazak;
	private Text tVozac;
	private Text tVozilo;
	private Text comboLinija;
	private Combo comboPolazak;
	private Button button;

	private VozniRed vozniRed;
	private StupacList polazak;
	private Label lOpisVozilo;
	private Label lOpisVozac;

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

		lblPrijava = new Label(shlPrijava, SWT.NONE);
		lblPrijava.setFont(SWTResourceManager.getFont("Liberation Sans", 45, SWT.NORMAL));
		lblPrijava.setBounds(290, 40, 183, 67);
		lblPrijava.setText("Prijava");

		lblVozac = new Label(shlPrijava, SWT.NONE);
		lblVozac.setAlignment(SWT.RIGHT);
		lblVozac.setFont(SWTResourceManager.getFont("Liberation Sans", 30, SWT.NORMAL));
		lblVozac.setBounds(0, 178, 220, 50);
		lblVozac.setText("Šifra vozača");

		tVozac = new Text(shlPrijava, SWT.BORDER | SWT.RIGHT);
		tVozac.addModifyListener(new TVozacModifyListener());
		tVozac.addMouseListener(new textMouseListener());
		tVozac.setFont(SWTResourceManager.getFont("Liberation Sans", 30, SWT.NORMAL));
		tVozac.setBounds(225, 174, 100, 50);
		tVozac.setText("");

		lOpisVozac = new Label(shlPrijava, SWT.NONE);
		lOpisVozac.setText("");
		lOpisVozac.setFont(SWTResourceManager.getFont("Liberation Sans", 20, SWT.NORMAL));
		lOpisVozac.setBounds(330, 188, 425, 30);

		lblVozilo = new Label(shlPrijava, SWT.NONE);
		lblVozilo.setAlignment(SWT.RIGHT);
		lblVozilo.setFont(SWTResourceManager.getFont("Liberation Sans", 30, SWT.NORMAL));
		lblVozilo.setBounds(20, 233, 200, 50);
		lblVozilo.setText("Šifra vozila");

		tVozilo = new Text(shlPrijava, SWT.BORDER | SWT.RIGHT);
		tVozilo.addModifyListener(new TVoziloModifyListener());
		tVozilo.setText("");
		tVozilo.addMouseListener(new textMouseListener());
		tVozilo.setFont(SWTResourceManager.getFont("Liberation Sans", 30, SWT.NORMAL));
		tVozilo.setBounds(225, 233, 100, 50);

		lOpisVozilo = new Label(shlPrijava, SWT.NONE);
		lOpisVozilo.setBounds(330, 246, 425, 30);
		lOpisVozilo.setFont(SWTResourceManager.getFont("Liberation Sans", 20, SWT.NORMAL));
		lOpisVozilo.setText("");

		lblLinija = new Label(shlPrijava, SWT.NONE);
		lblLinija.setAlignment(SWT.RIGHT);
		lblLinija.setFont(SWTResourceManager.getFont("Liberation Sans", 30, SWT.NORMAL));
		lblLinija.setBounds(40, 289, 180, 50);
		lblLinija.setText("Linija");

		comboLinija = new Text(shlPrijava, SWT.BORDER);
		comboLinija.setFont(SWTResourceManager.getFont("Liberation Sans", 20, SWT.NORMAL));
		comboLinija.setBounds(225, 289, 530, 50);

		lblPolazak = new Label(shlPrijava, SWT.NONE);
		lblPolazak.setAlignment(SWT.RIGHT);
		lblPolazak.setFont(SWTResourceManager.getFont("Liberation Sans", 30, SWT.NORMAL));
		lblPolazak.setBounds(40, 348, 180, 50);
		lblPolazak.setText("Polazak");

		comboPolazak = new Combo(shlPrijava, SWT.READ_ONLY);
		comboPolazak.setFont(SWTResourceManager.getFont("Liberation Sans", 30, SWT.NORMAL));
		comboPolazak.setBounds(225, 344, 150, 54);
		button = new Button(shlPrijava, SWT.ARROW | SWT.RIGHT);
		button.addSelectionListener(new ButtonSelectionListener());
		button.setFont(SWTResourceManager.getFont("Liberation Sans", 30, SWT.NORMAL));
		button.setBounds(700, 480, 60, 56);

	}

	private class ButtonSelectionListener extends SelectionAdapter {
		public void widgetSelected(SelectionEvent e) {
			// Starter.vozacID = Vozac.getVozacID(tVozac.getSelectionIndex());
			Starter.stupacID = polazak.getPolazakID(comboPolazak.getSelectionIndex());
			shlPrijava.dispose();
		}
	}

	private class textMouseListener extends MouseAdapter {
		@Override
		public void mouseDown(MouseEvent e) {
			Text t = (Text) e.widget;
			VirtualKeyboard keypad = new VirtualKeyboard(e.display.getActiveShell(), SWT.APPLICATION_MODAL);
			keypad.open(t);

		}
	}

	private class TVoziloModifyListener implements ModifyListener {
		public void modifyText(ModifyEvent e) {
			Text t = (Text) e.widget;
			if (t.getText().matches("\\d+"))
				lOpisVozilo.setText(Vozilo.getNaziv(Integer.parseInt(t.getText())));
		}
	}

	private class TVozacModifyListener implements ModifyListener {
		public void modifyText(ModifyEvent e) {
			Text t = (Text) e.widget;
			if (t.getText().matches("\\d+"))
				lOpisVozac.setText(Vozac.getNaziv(Integer.parseInt(t.getText())));
		}
	}
}
