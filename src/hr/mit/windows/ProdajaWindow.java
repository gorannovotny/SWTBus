package hr.mit.windows;

import hr.mit.beans.KartaList;
import hr.mit.beans.Postaja;
import hr.mit.beans.Stupac;
import hr.mit.beans.Vozac;
import hr.mit.utils.CijenaKarte;

import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.wb.swt.SWTResourceManager;

public class ProdajaWindow implements SelectionListener {

	protected Shell shell;
	private Combo comboOdPostaje;
	private Combo comboDoPostaje;
	private Label lblClock;
	private Label lblPolazak;
	private Combo comboKarta;
	private Label lblVozac;
	private Label lblCijena;

	private Postaja postaja;
	private KartaList kartaList;
	private Vozac vozac;
	private Stupac stupac;
	private CijenaKarte cijenaKarte;

	public ProdajaWindow(Integer vozacID, Integer stupacID) {
		vozac = new Vozac(vozacID);
		stupac = new Stupac(stupacID);
		postaja = new Postaja(stupacID);
		kartaList = new KartaList();
	}

	/**
	 * @wbp.parser.entryPoint
	 */
	public void open() {
		final Display display = Display.getDefault();
		createContents();
		shell.open();
		shell.layout();
		display.timerExec(1000, new Runnable() {
			public void run() {
				lblClock.setText(new SimpleDateFormat("HH:mm:ss").format(new Date()));
				display.timerExec(1000, this);
			}
		});

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	protected void createContents() {
		shell = new Shell(SWT.SHELL_TRIM);

		shell.setSize(800, 600);
		shell.setText("SWT Application");

		lblPolazak = new Label(shell, SWT.NONE);
		lblPolazak.setFont(SWTResourceManager.getFont("Liberation Sans", 20, SWT.NORMAL));
		lblPolazak.setBounds(10, 10, 620, 40);
		lblPolazak.setText(stupac.getLongDesc());

		lblClock = new Label(shell, SWT.BORDER | SWT.RIGHT);
		lblClock.setFont(SWTResourceManager.getFont("Liberation Sans", 20, SWT.NORMAL));
		lblClock.setBounds(640, 10, 144, 30);
		lblClock.setText(new SimpleDateFormat("HH:mm:ss").format(new Date()));

		comboOdPostaje = new Combo(shell, SWT.READ_ONLY);
		comboOdPostaje.addSelectionListener(this);
		comboOdPostaje.setFont(SWTResourceManager.getFont("Liberation Sans", 19, SWT.NORMAL));
		comboOdPostaje.setItems(postaja.getList());
		comboOdPostaje.setBounds(20, 80, 340, 50);
		comboOdPostaje.select(0);

		comboDoPostaje = new Combo(shell, SWT.READ_ONLY);
		comboDoPostaje.addSelectionListener(this);
		comboDoPostaje.setFont(SWTResourceManager.getFont("Liberation Sans", 19, SWT.NORMAL));
		comboDoPostaje.setBounds(389, 80, 340, 50);
		comboDoPostaje.setItems(postaja.getList());
		comboDoPostaje.select(1);

		comboKarta = new Combo(shell, SWT.READ_ONLY);
		comboKarta.addSelectionListener(this);
		comboKarta.setFont(SWTResourceManager.getFont("Liberation Sans", 19, SWT.NORMAL));
		comboKarta.setBounds(20, 165, 340, 50);
		comboKarta.setItems(kartaList.getList());
		comboKarta.select(0);

		lblCijena = new Label(shell, SWT.NONE);
		lblCijena.setFont(SWTResourceManager.getFont("Liberation Sans", 25, SWT.BOLD));
		lblCijena.setAlignment(SWT.RIGHT);
		lblCijena.setBounds(590, 243, 190, 37);
		lblCijena.setText("0,00");

		lblVozac = new Label(shell, SWT.NONE);
		lblVozac.setText(vozac.getNaziv());
		lblVozac.setFont(SWTResourceManager.getFont("Liberation Sans", 20, SWT.NORMAL));
		lblVozac.setBounds(10, 534, 500, 30);
	}

	public void widgetSelected(SelectionEvent e) {
		widgetDefaultSelected(e);
	}

	public void widgetDefaultSelected(SelectionEvent e) {
		Combo c = (Combo) e.widget;
		try {
			cijenaKarte = new CijenaKarte(kartaList.getID(comboKarta.getSelectionIndex()), stupac.getVarijantaID(), postaja.getID(comboOdPostaje.getSelectionIndex()), postaja.getID(comboDoPostaje
					.getSelectionIndex()));
			lblCijena.setText((new DecimalFormat("#.00")).format(cijenaKarte.getCijena()));
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
}
