package hr.mit.windows;

import hr.mit.beans.KartaList;
import hr.mit.beans.PostajaList;
import hr.mit.beans.Stavka;
import hr.mit.beans.Stupac;
import hr.mit.beans.Vozac;
import hr.mit.utils.CijenaKarte;

import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;

public class ProdajaWindow implements SelectionListener {

	protected Shell shell;
	private Combo cOdPostaje;
	private Combo cDoPostaje;
	private Label lClock;
	private Label lPolazak;
	private Combo cKarta;
	private Label lVozac;
	private Label lCijena;
	private List list;
	private Button bAdd;

	private PostajaList postaje;
	private KartaList karte;
	private Vozac vozac;
	private Stupac stupac;
	private CijenaKarte cijenaKarte;
	private Stavka stavka;

	public ProdajaWindow(Integer vozacID, Integer stupacID) {
		vozac = new Vozac(vozacID);
		stupac = new Stupac(stupacID);
		postaje = new PostajaList(stupacID);
		karte = new KartaList();
		// stavka = new Stavka();
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
				lClock.setText(new SimpleDateFormat("HH:mm:ss").format(new Date()));
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

		lPolazak = new Label(shell, SWT.NONE);
		lPolazak.setBackground(SWTResourceManager.getColor(0, 0, 0));
		lPolazak.setForeground(SWTResourceManager.getColor(192, 192, 192));
		lPolazak.setFont(SWTResourceManager.getFont("Liberation Sans", 20, SWT.NORMAL));
		lPolazak.setBounds(0, 0, 650, 40);
		lPolazak.setText(stupac.getLongDesc());

		lClock = new Label(shell, SWT.RIGHT);
		lClock.setForeground(SWTResourceManager.getColor(192, 192, 192));
		lClock.setBackground(SWTResourceManager.getColor(0, 0, 0));
		lClock.setFont(SWTResourceManager.getFont("Liberation Sans", 20, SWT.NORMAL));
		lClock.setBounds(650, 0, 144, 40);
		lClock.setText(new SimpleDateFormat("HH:mm:ss").format(new Date()));

		cOdPostaje = new Combo(shell, SWT.READ_ONLY);
		cOdPostaje.addSelectionListener(this);
		cOdPostaje.setFont(SWTResourceManager.getFont("Liberation Sans", 19, SWT.NORMAL));
		cOdPostaje.setItems(postaje.getNewList());
		cOdPostaje.setBounds(20, 80, 340, 50);
		cOdPostaje.select(0);

		cDoPostaje = new Combo(shell, SWT.READ_ONLY);
		cDoPostaje.addSelectionListener(this);
		cDoPostaje.setFont(SWTResourceManager.getFont("Liberation Sans", 19, SWT.NORMAL));
		cDoPostaje.setBounds(389, 80, 340, 50);
		cDoPostaje.setItems(postaje.getNewList());
		cDoPostaje.select(1);

		cKarta = new Combo(shell, SWT.READ_ONLY);
		cKarta.addSelectionListener(this);
		cKarta.setFont(SWTResourceManager.getFont("Liberation Sans", 19, SWT.NORMAL));
		cKarta.setBounds(20, 165, 340, 50);
		cKarta.setItems(karte.getList());
		cKarta.select(0);

		bAdd = new Button(shell, SWT.NONE);
		bAdd.addSelectionListener(new ButtonSelectionListener());
		bAdd.setBounds(20, 243, 19, 26);
		bAdd.setText("+");

		lCijena = new Label(shell, SWT.NONE);
		lCijena.setFont(SWTResourceManager.getFont("Liberation Sans", 25, SWT.BOLD));
		lCijena.setAlignment(SWT.RIGHT);
		lCijena.setBounds(590, 243, 190, 37);
		lCijena.setText("");

		list = new List(shell, SWT.BORDER);
		list.addMouseListener(new ListMouseListener());
		list.setFont(SWTResourceManager.getFont("Liberation Sans", 16, SWT.NORMAL));
		list.setBounds(20, 310, 520, 200);

		lVozac = new Label(shell, SWT.NONE);
		lVozac.setBackground(SWTResourceManager.getColor(0, 0, 0));
		lVozac.setForeground(SWTResourceManager.getColor(192, 192, 192));
		lVozac.setText(vozac.getNaziv());
		lVozac.setFont(SWTResourceManager.getFont("Liberation Sans", 20, SWT.NORMAL));
		lVozac.setBounds(0, 544, 790, 30);
	}

	public void widgetSelected(SelectionEvent e) {
		widgetDefaultSelected(e);
	}

	public void widgetDefaultSelected(SelectionEvent e) {
		// lblCijena.setText((new
		// DecimalFormat("#0.00")).format(stavka.getCijena()));
	}

	private class ButtonSelectionListener extends SelectionAdapter {
		public void widgetSelected(SelectionEvent e) {
			Stavka stavka = new Stavka(stupac, postaje.get(cOdPostaje.getSelectionIndex()), postaje.get(cDoPostaje.getSelectionIndex()), karte.get(cKarta.getSelectionIndex()), 0);
			list.add(stavka.getDescription());
		}
	}

	private class ListMouseListener extends MouseAdapter {
		@Override
		public void mouseDoubleClick(MouseEvent e) {
			List l = (List) e.widget;
			l.remove(l.getSelectionIndex());
		}
	}
}
