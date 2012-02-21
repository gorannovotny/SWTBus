package hr.mit.windows;

import hr.mit.beans.Blagajna;
import hr.mit.beans.Karta;
import hr.mit.beans.Popust;
import hr.mit.beans.Postaja;
import hr.mit.beans.ProdajnoMjesto;
import hr.mit.beans.Stavka;
import hr.mit.beans.Stupac;
import hr.mit.beans.Vozac;
import hr.mit.utils.CijenaKarte;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.ModifyEvent;

public class ProdajaWindow {

	protected Shell shell;

	private Combo cOdPostaje;
	private Combo cDoPostaje;
	private Combo cKarta;
	private Combo cPopust;
	private Combo cProdMjesto;

	private Text textCijena;
	private Text textBrKarte;

	private Text list;
	private Button bAdd;
	private Button btnPrint;

	private Vozac vozac;
	private Stupac stupac;
	private Blagajna blagajna;
	private Stavka stavka;

	private Label lClock;
	private Label lStupac;
	private Label lVozac;
	private Label lblOdPostaje;
	private Label lblDoPostaje;
	private Label lblVrstaKarte;
	private Label lblPopust;
	private Label lblProdajnoMjesto;
	private Label lblBrojKarte;
	private Label lblCijena;
	private Label lblUkupno;
	private Label lBlagajna;

	public ProdajaWindow(Vozac vozac, Stupac stupac) {
		this.vozac = vozac;
		this.stupac = stupac;
		Postaja.setStupac(stupac);

		Stavka.clear();
		stavka = new Stavka(stupac);
		blagajna = new Blagajna();
	}

	/**
	 * @wbp.parser.entryPoint
	 */
	public void open() {
		final Display display = Display.getDefault();
		createContents();
		attachListeners();
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

	private void attachListeners() {
		ComboSelectionListener c = new ComboSelectionListener();
		cOdPostaje.addSelectionListener(c);
		cDoPostaje.addSelectionListener(c);
		cKarta.addSelectionListener(c);
		cPopust.addSelectionListener(c);
		c.widgetDefaultSelected(null);

		bAdd.addSelectionListener(new ButtonSelectionListener());
	}

	protected void createContents() {
		shell = new Shell(SWT.SHELL_TRIM);

		shell.setSize(800, 600);
		shell.setText("SWT Application");

		lStupac = new Label(shell, SWT.NONE);
		lStupac.setBackground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		lStupac.setForeground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		lStupac.setFont(SWTResourceManager.getFont("Liberation Sans", 20, SWT.NORMAL));
		lStupac.setBounds(0, 0, 680, 50);
		lStupac.setText(stupac.getOpis() + " " + stupac.getVremeOdhoda());

		lClock = new Label(shell, SWT.RIGHT);
		lClock.setForeground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		lClock.setBackground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		lClock.setFont(SWTResourceManager.getFont("Liberation Sans", 20, SWT.NORMAL));
		lClock.setBounds(680, 0, 114, 50);
		lClock.setText(new SimpleDateFormat("HH:mm:ss").format(new Date()));

		lblOdPostaje = new Label(shell, SWT.NONE);
		lblOdPostaje.setFont(SWTResourceManager.getFont("Liberation Sans", 10, SWT.NORMAL));
		lblOdPostaje.setBounds(5, 55, 63, 15);
		lblOdPostaje.setText("Od postaje");

		lblDoPostaje = new Label(shell, SWT.NONE);
		lblDoPostaje.setText("Do postaje");
		lblDoPostaje.setFont(SWTResourceManager.getFont("Liberation Sans", 10, SWT.NORMAL));
		lblDoPostaje.setBounds(400, 55, 63, 15);

		cOdPostaje = new Combo(shell, SWT.DROP_DOWN | SWT.READ_ONLY);
		cOdPostaje.setFont(SWTResourceManager.getFont("Liberation Sans", 19, SWT.NORMAL));
		cOdPostaje.setItems(Postaja.getList());
		cOdPostaje.setBounds(5, 70, 390, 70);
		cOdPostaje.select(0);

		cDoPostaje = new Combo(shell, SWT.READ_ONLY);
		cDoPostaje.setFont(SWTResourceManager.getFont("Liberation Sans", 19, SWT.NORMAL));
		cDoPostaje.setBounds(400, 70, 390, 70);
		cDoPostaje.setItems(Postaja.getList());
		cDoPostaje.select(1);

		lblVrstaKarte = new Label(shell, SWT.NONE);
		lblVrstaKarte.setText("Vrsta karte");
		lblVrstaKarte.setFont(SWTResourceManager.getFont("Liberation Sans", 10, SWT.NORMAL));
		lblVrstaKarte.setBounds(5, 145, 64, 15);

		lblPopust = new Label(shell, SWT.NONE);
		lblPopust.setText("Popust");
		lblPopust.setFont(SWTResourceManager.getFont("Liberation Sans", 10, SWT.NORMAL));
		lblPopust.setBounds(400, 145, 41, 15);

		cKarta = new Combo(shell, SWT.READ_ONLY);
		cKarta.setFont(SWTResourceManager.getFont("Liberation Sans", 19, SWT.NORMAL));
		cKarta.setBounds(5, 160, 390, 70);
		cKarta.setItems(Karta.getList());
		cKarta.select(0);
		Popust.setKartaStupac(Karta.get(0), stupac);

		cPopust = new Combo(shell, SWT.READ_ONLY);
		cPopust.setItems(Popust.getList());
		cPopust.setFont(SWTResourceManager.getFont("Liberation Sans", 19, SWT.NORMAL));
		cPopust.setBounds(400, 160, 390, 70);
		cPopust.select(0);

		lblProdajnoMjesto = new Label(shell, SWT.NONE);
		lblProdajnoMjesto.setText("Prodajno mjesto");
		lblProdajnoMjesto.setFont(SWTResourceManager.getFont("Liberation Sans", 10, SWT.NORMAL));
		lblProdajnoMjesto.setBounds(5, 235, 94, 15);

		lblBrojKarte = new Label(shell, SWT.NONE);
		lblBrojKarte.setText("Broj karte");
		lblBrojKarte.setFont(SWTResourceManager.getFont("Liberation Sans", 10, SWT.NORMAL));
		lblBrojKarte.setBounds(340, 235, 94, 15);

		lblCijena = new Label(shell, SWT.NONE);
		lblCijena.setText("Cijena");
		lblCijena.setFont(SWTResourceManager.getFont("Liberation Sans", 10, SWT.NORMAL));
		lblCijena.setBounds(640, 235, 94, 15);

		cProdMjesto = new Combo(shell, SWT.READ_ONLY);
		cProdMjesto.setItems(ProdajnoMjesto.getList());
		cProdMjesto.setFont(SWTResourceManager.getFont("Liberation Sans", 19, SWT.NORMAL));
		cProdMjesto.setBounds(5, 250, 325, 70);
		cProdMjesto.select(0);

		textBrKarte = new Text(shell, SWT.BORDER | SWT.RIGHT);
		textBrKarte.setText("1234567890123");
		textBrKarte.setFont(SWTResourceManager.getFont("Liberation Sans", 30, SWT.NORMAL));
		textBrKarte.setBounds(335, 250, 295, 70);
		textBrKarte.setEnabled(false);

		textCijena = new Text(shell, SWT.BORDER | SWT.RIGHT);
		textCijena.setFont(SWTResourceManager.getFont("Liberation Sans", 30, SWT.NORMAL));
		textCijena.setBounds(635, 250, 155, 70);
		textCijena.setText("3334,12");

		bAdd = new Button(shell, SWT.NONE);
		bAdd.setBounds(5, 325, 785, 40);
		bAdd.setText("+");

		list = new Text(shell, SWT.BORDER | SWT.MULTI);
		list.setFont(SWTResourceManager.getFont("Liberation Mono", 14, SWT.NORMAL));
		list.setBounds(5, 370, 455, 145);

		lblUkupno = new Label(shell, SWT.RIGHT);
		lblUkupno.setText("");
		lblUkupno.setFont(SWTResourceManager.getFont("Liberation Sans", 30, SWT.BOLD));
		lblUkupno.setBounds(640, 380, 145, 50);

		btnPrint = new Button(shell, SWT.NONE);
		btnPrint.addSelectionListener(new BtnPrintSelectionListener());
		btnPrint.setText("Print");
		btnPrint.setFont(SWTResourceManager.getFont("Liberation Sans", 25, SWT.NORMAL));
		btnPrint.setBounds(640, 430, 145, 85);

		lVozac = new Label(shell, SWT.NONE);
		lVozac.setBackground(SWTResourceManager.getColor(0, 0, 0));
		lVozac.setForeground(SWTResourceManager.getColor(192, 192, 192));
		lVozac.setText(vozac.getNaziv());
		lVozac.setFont(SWTResourceManager.getFont("Liberation Sans", 20, SWT.NORMAL));
		lVozac.setBounds(0, 520, 640, 54);

		lBlagajna = new Label(shell, SWT.RIGHT);
		lBlagajna.setText("0,00");
		lBlagajna.setForeground(SWTResourceManager.getColor(192, 192, 192));
		lBlagajna.setFont(SWTResourceManager.getFont("Liberation Sans", 20, SWT.NORMAL));
		lBlagajna.setBackground(SWTResourceManager.getColor(0, 0, 0));
		lBlagajna.setBounds(640, 520, 155, 54);
	}

	private class ButtonSelectionListener extends SelectionAdapter {
		public void widgetSelected(SelectionEvent e) {
			Stavka.add(stavka);
			stavka = new Stavka(stupac);
			list.setText(Stavka.getDescription());
			lblUkupno.setText(Stavka.getUkupno().toString());
			if (Stavka.getCount().compareTo(0) > 0) {
				cOdPostaje.setEnabled(false);
				cDoPostaje.setEnabled(false);
			}
		}
	}

	private class ListMouseListener extends MouseAdapter {
		@Override
		public void mouseDoubleClick(MouseEvent e) {
			List l = (List) e.widget;
			// stavke.remove(l.getSelectionIndex());
			// list.setItems(stavke.getList());
			// textCijena.setText(new
			// DecimalFormat("#0.00").format(stavke.getUkupno()));
			cOdPostaje.setEnabled(true);
			cDoPostaje.setEnabled(true);

		}
	}

	private class BtnPrintSelectionListener extends SelectionAdapter {
		@Override
		public void widgetSelected(SelectionEvent e) {
			// blagajna.add(stavke.getUkupno());
			// lBlagajna.setText(new
			// DecimalFormat("#0.00").format(blagajna.getSaldo()));
			// stavke.clear();
			// list.setItems(stavke.getList());
			// textCijena.setText("0,00");
			cOdPostaje.setEnabled(true);
			cDoPostaje.setEnabled(true);

		}
	}

	private class ComboSelectionListener extends SelectionAdapter {
		public void widgetDefaultSelected(SelectionEvent e) {
			stavka.setOdPostaje(Postaja.get(cOdPostaje.getSelectionIndex()));
			stavka.setDoPostaje(Postaja.get(cDoPostaje.getSelectionIndex()));
			if (!Karta.get(cKarta.getSelectionIndex()).equals(stavka.getKarta())) {
				stavka.setKarta(Karta.get(cKarta.getSelectionIndex()));
				Popust.setKartaStupac(Karta.get(cKarta.getSelectionIndex()), stupac);
				cPopust.setItems(Popust.getList());
				cPopust.select(0);
			}
			stavka.setPopust(Popust.get(cPopust.getSelectionIndex()));
			if (stavka.getKarta().getId().equals(Karta.ZAMJENSKA_KARTA)) {
				cProdMjesto.setEnabled(true);
				textBrKarte.setEnabled(true);
				textCijena.setEnabled(true);
				cPopust.select(0);
				cPopust.setEnabled(false);
				cProdMjesto.select(2);
				textCijena.addModifyListener(new TextCijenaModifyListener());
			} else {
				CijenaKarte c = new CijenaKarte(stavka);
				stavka.setCijena(c.getUkupnaCijena().multiply(BigDecimal.ONE.subtract(stavka.getPopust().getPopust().movePointLeft(2))).setScale(2));
				cProdMjesto.select(0);
				cProdMjesto.setEnabled(false);
				textBrKarte.setEnabled(false);
				textCijena.setEnabled(false);
				cPopust.setEnabled(true);
				cProdMjesto.select(0);
			}
			textCijena.setText(stavka.getCijena().toString());
		}

		public void widgetSelected(SelectionEvent e) {
			widgetDefaultSelected(e);
		}
	}

	private class TextCijenaModifyListener implements ModifyListener {
		public void modifyText(ModifyEvent arg0) {
			Text t = (Text) arg0.widget;
			BigDecimal broj = BigDecimal.ZERO;
			try {
				broj = new BigDecimal(t.getText());
			} catch (Exception e) {
				broj = BigDecimal.ZERO;
			} finally {
				stavka.setCijena(broj.setScale(2));
			}
		}
	}

}
