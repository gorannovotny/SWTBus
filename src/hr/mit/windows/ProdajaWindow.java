package hr.mit.windows;

import hr.mit.beans.Blagajna;
import hr.mit.beans.KartaList;
import hr.mit.beans.PostajaList;
import hr.mit.beans.Stavka;
import hr.mit.beans.StavkaList;
import hr.mit.beans.Stupac;
import hr.mit.beans.Vozac;
import hr.mit.utils.CijenaKarte;

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
import org.eclipse.swt.custom.CLabel;

public class ProdajaWindow implements SelectionListener {

	protected Shell shell;
	private Combo cOdPostaje;
	private Combo cDoPostaje;
	private CLabel lClock;
	private CLabel lPolazak;
	private Combo cKarta;
	private CLabel lVozac;
	private CLabel lCijena;
	private List list;
	private Button bAdd;
	private Button bBarKod;
	private Combo combo;
	private Button bVanjska;
	private Button btnPrint;
	private CLabel lBlagajna;

	private PostajaList postaje;
	private KartaList karte;
	private Vozac vozac;
	private Stupac stupac;
	private StavkaList stavke;
	private Blagajna blagajna;

	public ProdajaWindow(Integer vozacID, Integer stupacID) {
		vozac = new Vozac(vozacID);
		stupac = new Stupac(stupacID);
		postaje = new PostajaList(stupacID);
		karte = new KartaList();
		stavke = new StavkaList();
		blagajna = new Blagajna();
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

		lPolazak = new CLabel(shell, SWT.HORIZONTAL | SWT.SHADOW_NONE);
		lPolazak.setBackground(SWTResourceManager.getColor(0, 0, 0));
		lPolazak.setForeground(SWTResourceManager.getColor(192, 192, 192));
		lPolazak.setFont(SWTResourceManager.getFont("Liberation Sans", 20, SWT.NORMAL));
		lPolazak.setBounds(0, 0, 670, 50);
		lPolazak.setText(stupac.getLongDesc());

		lClock = new CLabel(shell, SWT.RIGHT);
		lClock.setForeground(SWTResourceManager.getColor(192, 192, 192));
		lClock.setBackground(SWTResourceManager.getColor(0, 0, 0));
		lClock.setFont(SWTResourceManager.getFont("Liberation Sans", 20, SWT.NORMAL));
		lClock.setBounds(670, 0, 124, 50);
		lClock.setText(new SimpleDateFormat("HH:mm:ss").format(new Date()));

		cOdPostaje = new Combo(shell, SWT.READ_ONLY);
		cOdPostaje.addSelectionListener(this);
		cOdPostaje.setFont(SWTResourceManager.getFont("Liberation Sans", 19, SWT.NORMAL));
		cOdPostaje.setItems(postaje.getNewList());
		cOdPostaje.setBounds(10, 60, 310, 80);
		cOdPostaje.select(0);

		cDoPostaje = new Combo(shell, SWT.READ_ONLY);
		cDoPostaje.addSelectionListener(this);
		cDoPostaje.setFont(SWTResourceManager.getFont("Liberation Sans", 19, SWT.NORMAL));
		cDoPostaje.setBounds(330, 60, 310, 80);
		cDoPostaje.setItems(postaje.getNewList());
		cDoPostaje.select(1);

		bBarKod = new Button(shell, SWT.NONE);
		bBarKod.setFont(SWTResourceManager.getFont("Liberation Sans", 20, SWT.NORMAL));
		bBarKod.setBounds(650, 60, 130, 80);
		bBarKod.setText("Bar kod");

		cKarta = new Combo(shell, SWT.READ_ONLY);
		cKarta.addSelectionListener(this);
		cKarta.setFont(SWTResourceManager.getFont("Liberation Sans", 19, SWT.NORMAL));
		cKarta.setBounds(10, 150, 310, 80);
		cKarta.setItems(karte.getList());
		cKarta.select(0);

		combo = new Combo(shell, SWT.READ_ONLY);
		combo.setItems(new String[] { "10%", "20%", "50%" });
		combo.setFont(SWTResourceManager.getFont("Liberation Sans", 19, SWT.NORMAL));
		combo.setBounds(330, 150, 310, 80);
		combo.select(1);

		bVanjska = new Button(shell, SWT.NONE);
		bVanjska.setText("Vanjska karta");
		bVanjska.setFont(SWTResourceManager.getFont("Liberation Sans", 15, SWT.NORMAL));
		bVanjska.setBounds(650, 150, 130, 80);

		bAdd = new Button(shell, SWT.NONE);
		bAdd.addSelectionListener(new ButtonSelectionListener());
		bAdd.setBounds(10, 240, 630, 40);
		bAdd.setText("+");

		lCijena = new CLabel(shell, SWT.NONE);
		lCijena.setFont(SWTResourceManager.getFont("Liberation Sans", 27, SWT.BOLD));
		lCijena.setAlignment(SWT.RIGHT);
		lCijena.setBounds(650, 350, 130, 70);
		lCijena.setText("");

		list = new List(shell, SWT.BORDER);
		list.addMouseListener(new ListMouseListener());
		list.setFont(SWTResourceManager.getFont("Liberation Mono", 14, SWT.NORMAL));
		list.setBounds(10, 290, 630, 220);

		btnPrint = new Button(shell, SWT.NONE);
		btnPrint.addSelectionListener(new BtnPrintSelectionListener());
		btnPrint.setText("Print");
		btnPrint.setFont(SWTResourceManager.getFont("Liberation Sans", 25, SWT.NORMAL));
		btnPrint.setBounds(650, 430, 130, 80);

		lVozac = new CLabel(shell, SWT.NONE);
		lVozac.setBackground(SWTResourceManager.getColor(0, 0, 0));
		lVozac.setForeground(SWTResourceManager.getColor(192, 192, 192));
		lVozac.setText(vozac.getNaziv());
		lVozac.setFont(SWTResourceManager.getFont("Liberation Sans", 20, SWT.NORMAL));
		lVozac.setBounds(0, 520, 640, 54);

		lBlagajna = new CLabel(shell, SWT.RIGHT);
		lBlagajna.setText("0,00");
		lBlagajna.setForeground(SWTResourceManager.getColor(192, 192, 192));
		lBlagajna.setFont(SWTResourceManager.getFont("Liberation Sans", 20, SWT.NORMAL));
		lBlagajna.setBackground(SWTResourceManager.getColor(0, 0, 0));
		lBlagajna.setBounds(640, 520, 155, 54);
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
			stavke.add(stavka);
			list.setItems(stavke.getList());
			lCijena.setText(new DecimalFormat("#0.00").format(stavke.getUkupno()));
		}
	}

	private class ListMouseListener extends MouseAdapter {
		@Override
		public void mouseDoubleClick(MouseEvent e) {
			List l = (List) e.widget;
			stavke.remove(l.getSelectionIndex());
			list.setItems(stavke.getList());
			lCijena.setText(new DecimalFormat("#0.00").format(stavke.getUkupno()));
		}
	}

	private class BtnPrintSelectionListener extends SelectionAdapter {
		@Override
		public void widgetSelected(SelectionEvent e) {
			blagajna.add(stavke.getUkupno());
			lBlagajna.setText(new DecimalFormat("#0.00").format(blagajna.getSaldo()));
			stavke.clear();
			list.setItems(stavke.getList());
			lCijena.setText("0,00");
		}
	}
}
