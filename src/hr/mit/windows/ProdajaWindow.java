package hr.mit.windows;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import hr.mit.beans.Postaja;
import hr.mit.beans.Stupac;
import hr.mit.beans.StupacList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.widgets.Label;

public class ProdajaWindow implements SelectionListener {

	protected Shell shell;
	private Text txtHello;
	private Postaja postaja;
	private Combo comboOdPostaje;
	private Combo comboDoPostaje;

	Integer vozacID;
	Stupac stupac;
	private Label lblClock;
	private Label lblPolazak;

	public ProdajaWindow(Integer vozacID, Integer stupacID) {
		this.vozacID = vozacID;
		stupac = new Stupac(stupacID);
		postaja = new Postaja(stupacID);
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
		{
			lblClock = new Label(shell, SWT.BORDER | SWT.RIGHT);
			lblClock.setFont(SWTResourceManager.getFont("Liberation Sans", 20, SWT.NORMAL));
			lblClock.setBounds(640, 10, 144, 30);
			lblClock.setText(new SimpleDateFormat("HH:mm:ss").format(new Date()));
		}

		comboOdPostaje = new Combo(shell, SWT.READ_ONLY);
		comboOdPostaje.addSelectionListener(this);
		comboOdPostaje.setFont(SWTResourceManager.getFont("Liberation Sans", 19, SWT.NORMAL));
		comboOdPostaje.setItems(postaja.getList());
		comboOdPostaje.setBounds(20, 80, 340, 50);
		comboOdPostaje.select(0);

		comboDoPostaje = new Combo(shell, SWT.READ_ONLY);
		comboDoPostaje.setFont(SWTResourceManager.getFont("Liberation Sans", 19, SWT.NORMAL));
		comboDoPostaje.setBounds(389, 80, 340, 50);
		comboDoPostaje.setItems(postaja.getList());
		comboDoPostaje.select(1);
		Button btnNewButton = new Button(shell, SWT.NONE);
		btnNewButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				((Button) e.widget).setText("Bok");
			}
		});
		btnNewButton.setBounds(357, 175, 103, 45);
		btnNewButton.setText("New Button");

		txtHello = new Text(shell, SWT.BORDER);
		txtHello.setToolTipText("Tu sam");
		txtHello.setText("Hello");
		txtHello.setBounds(365, 257, 75, 24);
	}

	public void modifyText(ModifyEvent e) {
	}

	public void widgetSelected(SelectionEvent e) {
		Combo c = (Combo) e.widget;
		txtHello.setText(postaja.getID(c.getSelectionIndex()).toString());
		comboDoPostaje.select(c.getSelectionIndex() + 1);
	}

	public void widgetDefaultSelected(SelectionEvent e) {
		Combo c = (Combo) e.widget;
		txtHello.setText(postaja.getID(c.getSelectionIndex()).toString());
		comboDoPostaje.select(c.getSelectionIndex() + 1);
	}
}
