package hr.mit.windows;

import hr.mit.beans.Blagajna;
import hr.mit.beans.KartaList;
import hr.mit.beans.PopustList;
import hr.mit.beans.PostajaList;
import hr.mit.beans.ProdajnoMjesto;
import hr.mit.beans.Stavka;
import hr.mit.beans.StavkaList;
import hr.mit.beans.Stupac;
import hr.mit.beans.Vozac;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;

public class ProdajaWindow implements SelectionListener {

	protected Shell shell;
	private Combo cOdPostaje;
	private Combo cDoPostaje;
	private CLabel lClock;
	private CLabel lPolazak;
	private Combo cKarta;
	private CLabel lVozac;
	private Text lCijena;
	private List list;
	private Button bAdd;
	private Combo cPopust;
	private Button btnPrint;
	private CLabel lBlagajna;

	private PostajaList postaje;
	private KartaList karte;
	private Vozac vozac;
	private Stupac stupac;
	private StavkaList stavke;
	private Blagajna blagajna;
	private PopustList popusti;
	private Combo cProdMjesto;
	private Label lblOdPostaje;
	private Label lblDoPostaje;
	private Label lblVrstaKarte;
	private Label lblPopust;
	private Label lblProdajnoMjesto;

	public ProdajaWindow(Vozac vozac, Integer stupacID) {
		this.vozac = vozac;
		stupac = new Stupac(stupacID);
		postaje = new PostajaList(stupacID);
		karte = new KartaList();
		stavke = new StavkaList();
		blagajna = new Blagajna();
		popusti = new PopustList(stupac, karte.get(0));
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

		lPolazak = new CLabel(shell, SWT.NONE);
		lPolazak.setBackground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		lPolazak.setForeground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		lPolazak.setFont(SWTResourceManager.getFont("Liberation Sans", 20, SWT.NORMAL));
		lPolazak.setBounds(0, 0, 680, 50);
		lPolazak.setText(stupac.getLongDesc());

		lClock = new CLabel(shell, SWT.RIGHT);
		lClock.setForeground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		lClock.setBackground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		lClock.setFont(SWTResourceManager.getFont("Liberation Sans", 20, SWT.NORMAL));
		lClock.setBounds(680, 0, 114, 50);
		lClock.setText(new SimpleDateFormat("HH:mm:ss").format(new Date()));
		{
			lblOdPostaje = new Label(shell, SWT.NONE);
			lblOdPostaje.setFont(SWTResourceManager.getFont("Liberation Sans", 10, SWT.NORMAL));
			lblOdPostaje.setBounds(5, 55, 63, 15);
			lblOdPostaje.setText("Od postaje");
		}
		{
			lblDoPostaje = new Label(shell, SWT.NONE);
			lblDoPostaje.setText("Do postaje");
			lblDoPostaje.setFont(SWTResourceManager.getFont("Liberation Sans", 10, SWT.NORMAL));
			lblDoPostaje.setBounds(400, 55, 63, 15);
		}

		cOdPostaje = new Combo(shell, SWT.DROP_DOWN | SWT.READ_ONLY);
		cOdPostaje.addSelectionListener(this);
		cOdPostaje.setFont(SWTResourceManager.getFont("Liberation Sans", 19, SWT.NORMAL));
		cOdPostaje.setItems(postaje.getNewList());
		cOdPostaje.setBounds(5, 70, 390, 70);
		cOdPostaje.select(0);

		cDoPostaje = new Combo(shell, SWT.READ_ONLY);
		cDoPostaje.addSelectionListener(this);
		cDoPostaje.setFont(SWTResourceManager.getFont("Liberation Sans", 19, SWT.NORMAL));
		cDoPostaje.setBounds(400, 70, 390, 70);
		cDoPostaje.setItems(postaje.getNewList());
		cDoPostaje.select(1);
		{
			lblVrstaKarte = new Label(shell, SWT.NONE);
			lblVrstaKarte.setText("Vrsta karte");
			lblVrstaKarte.setFont(SWTResourceManager.getFont("Liberation Sans", 10, SWT.NORMAL));
			lblVrstaKarte.setBounds(5, 145, 64, 15);
		}
		{
			lblPopust = new Label(shell, SWT.NONE);
			lblPopust.setText("Popust");
			lblPopust.setFont(SWTResourceManager.getFont("Liberation Sans", 10, SWT.NORMAL));
			lblPopust.setBounds(400, 145, 41, 15);
		}

		cKarta = new Combo(shell, SWT.READ_ONLY);
		cKarta.addSelectionListener(this);
		cKarta.setFont(SWTResourceManager.getFont("Liberation Sans", 19, SWT.NORMAL));
		cKarta.setBounds(5, 160, 390, 70);
		cKarta.setItems(karte.getList());
		cKarta.select(0);

		cPopust = new Combo(shell, SWT.READ_ONLY);
		cPopust.setItems(popusti.getList());
		cPopust.setFont(SWTResourceManager.getFont("Liberation Sans", 19, SWT.NORMAL));
		cPopust.setBounds(400, 160, 390, 70);
		cPopust.select(0);
		{
			lblProdajnoMjesto = new Label(shell, SWT.NONE);
			lblProdajnoMjesto.setText("Prodajno mjesto");
			lblProdajnoMjesto.setFont(SWTResourceManager.getFont("Liberation Sans", 10, SWT.NORMAL));
			lblProdajnoMjesto.setBounds(5, 235, 94, 15);
		}

		cProdMjesto = new Combo(shell, SWT.READ_ONLY);
		cProdMjesto.setItems(ProdajnoMjesto.getList());
		cProdMjesto.setFont(SWTResourceManager.getFont("Liberation Sans", 19, SWT.NORMAL));
		cProdMjesto.setBounds(5, 250, 310, 70);
		cProdMjesto.select(0);

		bAdd = new Button(shell, SWT.NONE);
		bAdd.addSelectionListener(new ButtonSelectionListener());
		bAdd.setBounds(10, 315, 630, 40);
		bAdd.setText("+");

		list = new List(shell, SWT.BORDER);
		list.addMouseListener(new ListMouseListener());
		list.setFont(SWTResourceManager.getFont("Liberation Mono", 14, SWT.NORMAL));
		list.setBounds(10, 360, 630, 150);

		lCijena = new Text(shell, SWT.BORDER);
		lCijena.setFont(SWTResourceManager.getFont("Liberation Sans", 27, SWT.BOLD));
		lCijena.setBounds(650, 350, 130, 70);
		lCijena.setText("");

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

	public void widgetDefaultSelected(SelectionEvent e) {
		// lblCijena.setText((new
		// DecimalFormat("#0.00")).format(stavka.getCijena()));
		popusti = new PopustList(stupac, karte.get(cKarta.getSelectionIndex()));
		cPopust.setItems(popusti.getList());
		cPopust.select(0);
	}

	private class ButtonSelectionListener extends SelectionAdapter {
		public void widgetSelected(SelectionEvent e) {
			Stavka stavka = new Stavka(stupac, postaje.get(cOdPostaje.getSelectionIndex()), postaje.get(cDoPostaje.getSelectionIndex()), karte.get(cKarta.getSelectionIndex()), popusti.get(cPopust
					.getSelectionIndex()));
			stavke.add(stavka);
			list.setItems(stavke.getList());
			lCijena.setText(new DecimalFormat("#0.00").format(stavke.getUkupno()));
			if (stavke.getCount().compareTo(0) > 0) {
				cOdPostaje.setEnabled(false);
				cDoPostaje.setEnabled(false);
			}
		}
	}

	private class ListMouseListener extends MouseAdapter {
		@Override
		public void mouseDoubleClick(MouseEvent e) {
			List l = (List) e.widget;
			stavke.remove(l.getSelectionIndex());
			list.setItems(stavke.getList());
			lCijena.setText(new DecimalFormat("#0.00").format(stavke.getUkupno()));
			cOdPostaje.setEnabled(true);
			cDoPostaje.setEnabled(true);

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
			cOdPostaje.setEnabled(true);
			cDoPostaje.setEnabled(true);

		}
	}

	@Override
	public void widgetSelected(SelectionEvent arg0) {
		// TODO Auto-generated method stub

	}
}
