package hr.mit.windows;

import hr.mit.Starter;
import hr.mit.beans.Blagajna;
import hr.mit.beans.Karta;
import hr.mit.beans.Obracun;
import hr.mit.beans.Popust;
import hr.mit.beans.Postaja;
import hr.mit.beans.ProdajnoMjesto;
import hr.mit.beans.Stavka;
import hr.mit.utils.PrintUtils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

public class ProdajaWindow  {

	protected Shell shell;

	Button cOdPostaje;
	Button cDoPostaje;
	Button cKarta;
	Button cPopust;
	Button cProdMjesto;

	Text textCijena;
	Text textBrKarte;

	Text textRacun;
	Button bAdd;
	Button btnPrint;

	Stavka stavka;

	CLabel lClock;
	private Button btnStupac;
	private Label lblOdPostaje;
	private Label lblDoPostaje;
	private Label lblVrstaKarte;
	private Label lblPopust;
	private Label lblProdajnoMjesto;
	private Label lblBrojKarte;
	private Label lblCijena;
	private Label lblUkupno;
	public Button lBlagajna;
	private Button btnClear;

	private TMouseListener mouseListener;

	protected boolean exit;
	private Button btnZadnji;

	protected List<Stavka> oldRacun;

	public ProdajaWindow() {

		Stavka.clear();
		mouseListener = new TMouseListener();
	}

	/**
	 * @wbp.parser.entryPoint
	 */
	public boolean open() {
		Postaja.setStupac(Starter.stupac);
		int random = new Random().nextInt(1000000000);

		final Display display = Display.getDefault();
		createContents();
		stavka = new Stavka(Starter.stupac, Postaja.get((Integer) cOdPostaje.getData()), Postaja.get((Integer) cDoPostaje.getData()), Karta.get((Integer) cKarta.getData()),
				Popust.get((Integer) cPopust.getData()));
		stavka.setBrojKarte(String.valueOf(random));
		screenToStavka();
		stavkaToScreen();
		attachListeners();
		shell.open();
		shell.layout();
		Runnable r = new Runnable() {
			@Override
			public void run() {
				lClock.setText(new SimpleDateFormat("HH:mm:ss").format(new Date()));
				display.timerExec(1000, this);
			}
		};

		display.timerExec(1000, r);

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		display.timerExec(-1, r);
		return exit;
	}

	protected void screenToStavka() {
		stavka.setOdPostaje(Postaja.get((Integer) cOdPostaje.getData()));
		stavka.setDoPostaje(Postaja.get((Integer) cDoPostaje.getData()));
		if (!Karta.get((Integer) cKarta.getData()).equals(stavka.getKarta())) {
			stavka.setKarta(Karta.get((Integer) cKarta.getData()));
			Popust.setKartaStupac(Karta.get((Integer) cKarta.getData()), Starter.stupac);
			cPopust.setData(0);
			cPopust.setText(Popust.getList()[0]);
			;
		}

		stavka.setPopust(Popust.get((Integer) cPopust.getData()));
		String s = textCijena.getText();
		try {
			NumberFormat nf = NumberFormat.getNumberInstance();
			DecimalFormat df = (DecimalFormat) nf;
			df.parseObject(s);
			stavka.setCijena(new BigDecimal(textCijena.getText()).setScale(2));
		} catch (Exception e) {
			stavka.setCijena(BigDecimal.ZERO.setScale(2));
		}
		stavka.setProdajnoMjesto(ProdajnoMjesto.get((Integer) cProdMjesto.getData()));
		stavka.setBrojKarte(textBrKarte.getText());
	}

	protected void stavkaToScreen() {
		if (stavka.getKarta().getId().equals(Karta.ZAMJENSKA_KARTA)) {
			cProdMjesto.setEnabled(true);
			textBrKarte.setEnabled(true);
			textCijena.setEnabled(true);
			cPopust.setData(0);
			cPopust.setText(Popust.getList()[0]);
			cPopust.setEnabled(false);
			if (! textCijena.isListening(SWT.MouseDown))
				textCijena.addMouseListener(mouseListener);
			if (! textBrKarte.isListening(SWT.MouseDown))
				textBrKarte.addMouseListener(mouseListener);					
		} else {
			textCijena.removeMouseListener(mouseListener);
			textBrKarte.removeMouseListener(mouseListener);
			// CijenaKarte c = new CijenaKarte(stavka);
			// stavka.setCijena(c.getUkupnaCijena().multiply(BigDecimal.ONE.subtract(stavka.getPopust().getPopust().movePointLeft(2))).setScale(2));
			cProdMjesto.setData(0);
			cProdMjesto.setText(ProdajnoMjesto.getList()[0]);
			cProdMjesto.setEnabled(false);
			textBrKarte.setEnabled(false);
			textCijena.setEnabled(false);
			textBrKarte.setText(stavka.getBrojKarte());
			textCijena.setText(stavka.getProdajnaCijena().toString());
			cPopust.setEnabled(true);
		}
		textRacun.setText(Stavka.getDescription());
		lblUkupno.setText("Kn " + Stavka.getUkupno().toString());

		if (Stavka.getCount() > 0) {
			cOdPostaje.setEnabled(false);
			cDoPostaje.setEnabled(false);
			btnPrint.setEnabled(true);
			btnClear.setEnabled(true);
		} else {
			cOdPostaje.setEnabled(true);
			cDoPostaje.setEnabled(true);
			btnPrint.setEnabled(false);
			btnClear.setEnabled(false);
		}
		if (Stavka.getOldList() == null || Stavka.getOldList().isEmpty())
			btnZadnji.setEnabled(false);
		else
			btnZadnji.setEnabled(true);

	}

	private void attachListeners() {
		PickerListener c = new PickerListener();
		cOdPostaje.addSelectionListener(c);
		cDoPostaje.addSelectionListener(c);

		cKarta.addSelectionListener(c);
		cPopust.addSelectionListener(c);
		cProdMjesto.addSelectionListener(c);

		bAdd.addSelectionListener(new PlusButtonListener());
	}

	protected void createContents() {
		// shell = new Shell(SWT.SYSTEM_MODAL | SWT.ON_TOP);
		// shell.setMaximized(true);
		shell = new Shell(SWT.NONE);
		shell.setSize(450, 333);
		shell.setBackground(SWTResourceManager.getColor(SWT.COLOR_GRAY));
		shell.setBounds(0, 0, 800, 600);
		// shell.setFullScreen(true);

		btnStupac = new Button(shell, SWT.NONE);
		btnStupac.setBackground(SWTResourceManager.getColor(SWT.COLOR_GRAY));
		btnStupac.setAlignment(SWT.LEFT);
		btnStupac.setFont(SWTResourceManager.getFont("Liberation Sans", 15, SWT.NORMAL));
		btnStupac.setBounds(5, 0, 785, 50);
		btnStupac.setText("Vozač: " + Starter.vozac.getNaziv() + "\t\tRelacija: " + Starter.stupac.getOpis() + "@" + Starter.stupac.getVremeOdhoda());
		btnStupac.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				shell.dispose();
			}

			@Override
			public void widgetSelected(SelectionEvent e) {
				widgetDefaultSelected(e);
			}
		});

		lblOdPostaje = new Label(shell, SWT.NONE);
		lblOdPostaje.setBackground(SWTResourceManager.getColor(SWT.COLOR_GRAY));
		lblOdPostaje.setFont(SWTResourceManager.getFont("Liberation Sans", 10, SWT.NORMAL));
		lblOdPostaje.setBounds(5, 50, 63, 15);
		lblOdPostaje.setText("Od postaje");

		lblDoPostaje = new Label(shell, SWT.NONE);
		lblDoPostaje.setBackground(SWTResourceManager.getColor(SWT.COLOR_GRAY));
		lblDoPostaje.setText("Do postaje");
		lblDoPostaje.setFont(SWTResourceManager.getFont("Liberation Sans", 10, SWT.NORMAL));
		lblDoPostaje.setBounds(400, 50, 63, 15);

		cOdPostaje = new Button(shell, SWT.READ_ONLY);
		cOdPostaje.setAlignment(SWT.LEFT);
		cOdPostaje.setFont(SWTResourceManager.getFont("Liberation Sans", 20, SWT.NORMAL));
		// cOdPostaje.setBackground(SWTResourceManager.getColor(SWT.COLOR_LIST_SELECTION));
		cOdPostaje.setBounds(5, 65, 390, 70);
		cOdPostaje.setText(Postaja.getList()[0]);
		cOdPostaje.setData(0);

		cDoPostaje = new Button(shell, SWT.READ_ONLY);
		cDoPostaje.setAlignment(SWT.LEFT);
		cDoPostaje.setFont(SWTResourceManager.getFont("Liberation Sans", 20, SWT.NORMAL));
		// cDoPostaje.setBackground(SWTResourceManager.getColor(SWT.COLOR_LIST_SELECTION));

		cDoPostaje.setBounds(400, 65, 390, 70);
		cDoPostaje.setText(Postaja.getList()[1]);
		cDoPostaje.setData(1);

		lblVrstaKarte = new Label(shell, SWT.NONE);
		lblVrstaKarte.setBackground(SWTResourceManager.getColor(SWT.COLOR_GRAY));
		lblVrstaKarte.setText("Vrsta karte");
		lblVrstaKarte.setFont(SWTResourceManager.getFont("Liberation Sans", 10, SWT.NORMAL));
		lblVrstaKarte.setBounds(5, 135, 64, 15);

		lblPopust = new Label(shell, SWT.NONE);
		lblPopust.setBackground(SWTResourceManager.getColor(SWT.COLOR_GRAY));
		lblPopust.setText("Popust");
		lblPopust.setFont(SWTResourceManager.getFont("Liberation Sans", 10, SWT.NORMAL));
		lblPopust.setBounds(400, 135, 41, 15);

		cKarta = new Button(shell, SWT.READ_ONLY);
		cKarta.setAlignment(SWT.LEFT);
		cKarta.setFont(SWTResourceManager.getFont("Liberation Sans", 22, SWT.NORMAL));
		cKarta.setBounds(5, 150, 390, 70);
		cKarta.setText(Karta.getList()[0]);
		Popust.setKartaStupac(Karta.get(0), Starter.stupac);
		cKarta.setData(0);

		cPopust = new Button(shell, SWT.READ_ONLY);
		cPopust.setAlignment(SWT.LEFT);
		cPopust.setFont(SWTResourceManager.getFont("Liberation Sans", 18, SWT.NORMAL));
		cPopust.setBounds(400, 150, 390, 70);
		cPopust.setData(0);
		cPopust.setText(Popust.getList()[0]);
		;

		lblProdajnoMjesto = new Label(shell, SWT.NONE);
		lblProdajnoMjesto.setBackground(SWTResourceManager.getColor(SWT.COLOR_GRAY));
		lblProdajnoMjesto.setText("Prodajno mjesto");
		lblProdajnoMjesto.setFont(SWTResourceManager.getFont("Liberation Sans", 10, SWT.NORMAL));
		lblProdajnoMjesto.setBounds(5, 220, 94, 15);

		lblBrojKarte = new Label(shell, SWT.NONE);
		lblBrojKarte.setBackground(SWTResourceManager.getColor(SWT.COLOR_GRAY));
		lblBrojKarte.setText("Broj karte");
		lblBrojKarte.setFont(SWTResourceManager.getFont("Liberation Sans", 10, SWT.NORMAL));
		lblBrojKarte.setBounds(340, 220, 94, 15);

		lblCijena = new Label(shell, SWT.NONE);
		lblCijena.setBackground(SWTResourceManager.getColor(SWT.COLOR_GRAY));
		lblCijena.setText("Cijena");
		lblCijena.setFont(SWTResourceManager.getFont("Liberation Sans", 10, SWT.NORMAL));
		lblCijena.setBounds(640, 220, 94, 15);

		cProdMjesto = new Button(shell, SWT.READ_ONLY);
		cProdMjesto.setFont(SWTResourceManager.getFont("Liberation Sans", 22, SWT.NORMAL));
		cProdMjesto.setBounds(5, 235, 325, 70);
		cProdMjesto.setAlignment(SWT.LEFT);
		cProdMjesto.setData(0);
		cProdMjesto.setText(ProdajnoMjesto.getList()[0]);

		textBrKarte = new Text(shell, SWT.BORDER | SWT.RIGHT);
		textBrKarte.setFont(SWTResourceManager.getFont("Liberation Sans", 25, SWT.NORMAL));
		textBrKarte.setBounds(335, 235, 295, 70);
		// textBrKarte.setText(stavka.getBrojKarte());
		textBrKarte.setEnabled(false);

		textCijena = new Text(shell, SWT.BORDER | SWT.RIGHT);
		textCijena.setFont(SWTResourceManager.getFont("Liberation Sans", 25, SWT.NORMAL));
		textCijena.setBounds(635, 235, 155, 70);
		// textCijena.setText("3334,12");

		bAdd = new Button(shell, SWT.NONE);
		bAdd.setFont(SWTResourceManager.getFont("Liberation Sans", 25, SWT.NORMAL));
		bAdd.setBounds(5, 310, 460, 70);
		bAdd.setText("Dodaj kartu");

		lblUkupno = new Label(shell, SWT.RIGHT);
		lblUkupno.setForeground(SWTResourceManager.getColor(SWT.COLOR_RED));
		lblUkupno.setBackground(SWTResourceManager.getColor(SWT.COLOR_GRAY));
		lblUkupno.setText("");
		lblUkupno.setFont(SWTResourceManager.getFont("Liberation Sans", 50, SWT.NORMAL));
		lblUkupno.setBounds(470, 310, 320, 70);

		textRacun = new Text(shell, SWT.BORDER | SWT.MULTI);
		textRacun.setEnabled(false);
		textRacun.setEditable(false);
		textRacun.setFont(SWTResourceManager.getFont("Liberation Mono", 14, SWT.NORMAL));
		textRacun.setBounds(5, 385, 460, 155);

		btnClear = new Button(shell, SWT.NONE);
		btnClear.addSelectionListener(new ClearButtonListener());
		btnClear.setText("Briši");
		btnClear.setFont(SWTResourceManager.getFont("Liberation Sans", 25, SWT.NORMAL));
		btnClear.setBounds(470, 385, 160, 75);

		btnZadnji = new Button(shell, SWT.NONE);
		btnZadnji.addSelectionListener(new BtnZadnjiSelectionListener());
		btnZadnji.setText("Zadnji");
		btnZadnji.setFont(SWTResourceManager.getFont("Liberation Sans", 25, SWT.NORMAL));
		btnZadnji.setBounds(470, 465, 160, 75);

		btnPrint = new Button(shell, SWT.NONE);
		btnPrint.addSelectionListener(new PrintButtonListener());
		btnPrint.setText("Print");
		btnPrint.setFont(SWTResourceManager.getFont("Liberation Sans", 25, SWT.NORMAL));
		btnPrint.setBounds(635, 385, 158, 155);

		lBlagajna = new Button(shell, SWT.LEFT);
		lBlagajna.setBackground(SWTResourceManager.getColor(SWT.COLOR_GRAY));
		lBlagajna.addSelectionListener(new LBlagajnaSelectionListener());
		lBlagajna.setText("Blagajna: " + Obracun.getSaldo().toString());
		// lBlagajna.setForeground(SWTResourceManager.getColor(192, 192, 192));
		lBlagajna.setFont(SWTResourceManager.getFont("Liberation Sans", 20, SWT.NORMAL));
		// lBlagajna.setBackground(SWTResourceManager.getColor(0, 0, 0));
		lBlagajna.setBounds(5, 544, 460, 54);

		lClock = new CLabel(shell, SWT.LEFT);
		lClock.setAlignment(SWT.RIGHT);
		lClock.setBackground(SWTResourceManager.getColor(SWT.COLOR_GRAY));
		// lClock.setForeground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		// lClock.setBackground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		lClock.setFont(SWTResourceManager.getFont("Liberation Sans", 20, SWT.NORMAL));
		lClock.setBounds(685, 546, 113, 52);
		lClock.setText(new SimpleDateFormat("HH:mm:ss").format(new Date()));
	}

	protected class PlusButtonListener extends SelectionAdapter {
		@Override
		public void widgetSelected(SelectionEvent e) {
			Stavka.add(stavka);
			stavkaToScreen();
			stavka = new Stavka(Starter.stupac, Postaja.get((Integer) cOdPostaje.getData()), Postaja.get((Integer) cDoPostaje.getData()), Karta.get((Integer) cKarta.getData()),
					Popust.get((Integer) cPopust.getData()));
			screenToStavka();
		}
	}

	protected class PrintButtonListener extends SelectionAdapter {
		@Override
		public void widgetSelected(SelectionEvent e) {
			Blagajna.save(Starter.vozac, Starter.vozilo, Stavka.getList());
			lBlagajna.setText("Blagajna: " + Obracun.getSaldo().toString() + " Kn");
			PrintUtils.print(Starter.vozac, Stavka.getList());
			Stavka.saveList();
			Stavka.clear();
			stavkaToScreen();
		}
	}

	protected class PickerListener extends SelectionAdapter {
		@Override
		public void widgetDefaultSelected(SelectionEvent e) {
			if (e.widget.equals(cOdPostaje)) {
				Integer odIndex = (Integer) cOdPostaje.getData();
				Integer doIndex = (Integer) cDoPostaje.getData();

				Picker picker = new Picker(cOdPostaje, Postaja.getArrayList(), odIndex);
				cOdPostaje = picker.open();
				odIndex = (Integer) cOdPostaje.getData();
				stavka.setOdPostaje(Postaja.get(odIndex));

				if (doIndex <= odIndex && (odIndex < Postaja.getList().length - 1)) {
					stavka.setDoPostaje(Postaja.get(odIndex + 1));
					cDoPostaje.setData(odIndex + 1);
					cDoPostaje.setText(Postaja.getList()[odIndex + 1]);
				}
			} else if (e.widget.equals(cDoPostaje)) {
				Integer odIndex = (Integer) cOdPostaje.getData();
				Integer doIndex = (Integer) cDoPostaje.getData();

				Picker picker = new Picker(cDoPostaje, Postaja.getArrayList(), doIndex);
				cDoPostaje = picker.open();
				doIndex = (Integer) cDoPostaje.getData();

				stavka.setDoPostaje(Postaja.get(doIndex));
				if ((odIndex >= doIndex) && (doIndex > 0)) {
					stavka.setOdPostaje(Postaja.get(doIndex - 1));
					cOdPostaje.setText(Postaja.getList()[doIndex - 1]);
				}
			} else if (e.widget.equals(cKarta)) {
				Integer index = (Integer) cKarta.getData();
				Picker picker = new Picker(cKarta, Karta.getArrayList(), index);
				cKarta = picker.open();

				stavka.setKarta(Karta.get((Integer) cKarta.getData()));
				Popust.setKartaStupac(Karta.get((Integer) cKarta.getData()), Starter.stupac);
				cPopust.setData(0);
				cPopust.setText(Popust.getList()[0]);
				if (stavka.getKarta().getId().equals(Karta.ZAMJENSKA_KARTA)) {
					cProdMjesto.setData(1);
					cProdMjesto.setText(ProdajnoMjesto.getList()[1]);
					stavka.setProdajnoMjesto(ProdajnoMjesto.get(1));
					textCijena.setText("0.00");
				}
			} else if (e.widget.equals(cPopust)) {
				Integer index = (Integer) cPopust.getData();
				Picker picker = new Picker(cPopust, Popust.getArrayList(), index);
				cPopust = picker.open();
				stavka.setPopust(Popust.get((Integer) cPopust.getData()));
			} else if (e.widget.equals(cProdMjesto)) {
				Integer index = (Integer) cProdMjesto.getData();
				Picker picker = new Picker(cProdMjesto, ProdajnoMjesto.getArrayList(), index);
				cProdMjesto = picker.open();
				stavka.setProdajnoMjesto(ProdajnoMjesto.get((Integer) cProdMjesto.getData()));
			}
			stavkaToScreen();

		}

		@Override
		public void widgetSelected(SelectionEvent e) {
			widgetDefaultSelected(e);
		}
	}

	protected class ClearButtonListener extends SelectionAdapter {
		@Override
		public void widgetDefaultSelected(SelectionEvent e) {
			Stavka.clear();
			stavkaToScreen();
		}

		@Override
		public void widgetSelected(SelectionEvent e) {
			widgetDefaultSelected(e);
		}
	}

	protected class TMouseListener extends MouseAdapter {
		@Override
		public void mouseDown(MouseEvent e) {
			Text t = (Text) e.widget;
			VirtualKeyboard keypad = new VirtualKeyboard(e.display.getActiveShell());
			t.selectAll();
			keypad.open(t);
			screenToStavka();
			if (t.equals(textCijena))
				t.setText(stavka.getProdajnaCijena().toString());
		}
	}

	private class LBlagajnaSelectionListener extends SelectionAdapter {
		@Override
		public void widgetDefaultSelected(SelectionEvent e) {
			ObracunWindow ow = new ObracunWindow();
			exit = ow.open(shell);
			lBlagajna.setText("Blagajna: " + Obracun.getSaldo().toString() + " Kn");
			if (exit)
				shell.dispose();
		}

		@Override
		public void widgetSelected(SelectionEvent e) {
			widgetDefaultSelected(e);
		}
	}

	private class BtnZadnjiSelectionListener extends SelectionAdapter {
		@Override
		public void widgetDefaultSelected(SelectionEvent e) {
			Zadnji z = new Zadnji(shell);
			z.open();
		}

		@Override
		public void widgetSelected(SelectionEvent e) {
			// TODO Auto-generated method stub
			widgetDefaultSelected(e);
		}
	}

}
