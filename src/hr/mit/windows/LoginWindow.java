package hr.mit.windows;

import hr.mit.Starter;
import hr.mit.beans.Stupac;
import hr.mit.beans.Vozac;
import hr.mit.beans.Vozilo;
import hr.mit.utils.DbUtil;
import hr.mit.utils.PrintUtils;
import hr.mit.utils.FileChecksum;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

public class LoginWindow {

	protected Shell shlPrijava;
	private Label lblPrijava;
	private Label lblVozac;
	private Label lblVozilo;
	private Label lblPolazak;
	private Label lblVerzijaBaze;
	private Label lblVerzijaPrograma;
	private Text tVozac;
	private Text tVozilo;
	private Text tLinija;
	private Button btnNastavak;
	private Button btnGasenje;
	private Button btnPrintTest;

	protected Label lOpisVozilo;
	protected Label lOpisVozac;
	protected Label lOpisLinije;
	private Button btnUcitaj;
	private Text tLozinka;
	private Label lLozinka;
	private Button btnSearch;

	/**
	 * @wbp.parser.entryPoint
	 */
	public void open() {
		Display display = Display.getDefault();
		shlPrijava = new Shell(display, SWT.NONE);
		shlPrijava.setBackground(SWTResourceManager.getColor(SWT.COLOR_GRAY));
		shlPrijava.setSize(450, 332);
		shlPrijava.setBounds(0, 0, 800, 600);
		createContents();
		shlPrijava.open();
		shlPrijava.layout();
		while (!shlPrijava.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	protected void createContents() {
		lblPrijava = new Label(shlPrijava, SWT.NONE);
		lblPrijava.setBackground(SWTResourceManager.getColor(SWT.COLOR_GRAY));
		lblPrijava.setFont(SWTResourceManager.getFont("Liberation Sans", 45, SWT.NORMAL));
		lblPrijava.setBounds(290, 10, 183, 67);
		lblPrijava.setText("Prijava");

		lblVozac = new Label(shlPrijava, SWT.NONE);
		lblVozac.setBackground(SWTResourceManager.getColor(SWT.COLOR_GRAY));
		lblVozac.setFont(SWTResourceManager.getFont("Liberation Sans", 30, SWT.NORMAL));
		lblVozac.setBounds(5, 110, 160, 50);
		lblVozac.setText("Vozač");

		tVozac = new Text(shlPrijava, SWT.BORDER);
		tVozac.setFont(SWTResourceManager.getFont("Liberation Sans", 30, SWT.NORMAL));
		tVozac.setBounds(165, 110, 125, 50);

		lOpisVozac = new Label(shlPrijava, SWT.NONE);
		lOpisVozac.setBackground(SWTResourceManager.getColor(SWT.COLOR_GRAY));
		lOpisVozac.setText("");
		lOpisVozac.setFont(SWTResourceManager.getFont("Liberation Sans", 20, SWT.NORMAL));
		lOpisVozac.setBounds(310, 120, 480, 30);

		tLozinka = new Text(shlPrijava, SWT.BORDER | SWT.PASSWORD);
		tLozinka.setText("");
		tLozinka.setFont(SWTResourceManager.getFont("Liberation Sans", 30, SWT.NORMAL));
		tLozinka.setBounds(165, 160, 175, 50);
		tLozinka.addMouseListener(new textMouseListener());

		lLozinka = new Label(shlPrijava, SWT.NONE);
		lLozinka.setText("Lozinka");
		lLozinka.setFont(SWTResourceManager.getFont("Liberation Sans", 30, SWT.NORMAL));
		lLozinka.setBackground(SWTResourceManager.getColor(SWT.COLOR_GRAY));
		lLozinka.setBounds(5, 160, 160, 50);
		
		
		lblVozilo = new Label(shlPrijava, SWT.NONE);
		lblVozilo.setBackground(SWTResourceManager.getColor(SWT.COLOR_GRAY));
		lblVozilo.setFont(SWTResourceManager.getFont("Liberation Sans", 30, SWT.NORMAL));
		lblVozilo.setBounds(5, 210, 160, 50);
		lblVozilo.setText("Vozilo");

		tVozilo = new Text(shlPrijava, SWT.BORDER);
		tVozilo.setFont(SWTResourceManager.getFont("Liberation Sans", 30, SWT.NORMAL));
		tVozilo.setBounds(165, 210, 125, 50);

		lOpisVozilo = new Label(shlPrijava, SWT.NONE);
		lOpisVozilo.setBackground(SWTResourceManager.getColor(SWT.COLOR_GRAY));
		lOpisVozilo.setBounds(310, 220, 480, 30);
		lOpisVozilo.setFont(SWTResourceManager.getFont("Liberation Sans", 20, SWT.NORMAL));
		lOpisVozilo.setText("");

		
		
		tLinija = new Text(shlPrijava, SWT.BORDER);
		tLinija.setFont(SWTResourceManager.getFont("Liberation Sans", 30, SWT.NORMAL));
//		tLinija.setBounds(165, 260, 125, 50);
		tLinija.setBounds(165, 260, 135, 50);

		btnSearch = new Button(shlPrijava, SWT.NONE);
//		btnSearch.setBounds(290, 263, 46, 46);
		btnSearch.setBounds(302, 263, 46, 46);
		btnSearch.setText("\u2026");
		btnSearch.addSelectionListener(new SearchStupacButtonListener());

		lOpisLinije = new Label(shlPrijava, SWT.NONE);
		lOpisLinije.setBackground(SWTResourceManager.getColor(SWT.COLOR_GRAY));
		lOpisLinije.setText("");
		lOpisLinije.setAlignment(SWT.LEFT);
		lOpisLinije.setFont(SWTResourceManager.getFont("Liberation Sans", 14, SWT.NORMAL));
		lOpisLinije.setBounds(350, 270, 445, 30);

		lblPolazak = new Label(shlPrijava, SWT.NONE);
		lblPolazak.setBackground(SWTResourceManager.getColor(SWT.COLOR_GRAY));
		lblPolazak.setFont(SWTResourceManager.getFont("Liberation Sans", 30, SWT.NORMAL));
		lblPolazak.setBounds(5, 260, 160, 50);
		lblPolazak.setText("Polazak");

		lblVerzijaBaze = new Label(shlPrijava, SWT.NONE);
		lblVerzijaBaze.setBackground(SWTResourceManager.getColor(SWT.COLOR_GRAY));
		lblVerzijaBaze.setFont(SWTResourceManager.getFont("Liberation Sans", 14, SWT.NORMAL));
		lblVerzijaBaze.setBounds(5, 405 + 140, 420, 20);
		lblVerzijaBaze.setText("Podaci: " + DbUtil.getDbVersionInfo() + ", mob.stroj br.: " + Starter.SifraMobStroja);

		lblVerzijaPrograma = new Label(shlPrijava, SWT.NONE);
		lblVerzijaPrograma.setBackground(SWTResourceManager.getColor(SWT.COLOR_GRAY));
		lblVerzijaPrograma.setFont(SWTResourceManager.getFont("Liberation Sans", 14, SWT.NORMAL));
		lblVerzijaPrograma.setBounds(5, 425 + 140, 420+100, 25);
		lblVerzijaPrograma.setText("Verzija programa: " + DbUtil.getVersionInfo() + ", prn.port: " + Starter.ComPortPrinter +", rtp: "+Starter.PrintRotate);

		btnUcitaj = new Button(shlPrijava, SWT.NONE);
		btnUcitaj.addSelectionListener(new UcitajBazuButtonListener());
		btnUcitaj.setText("Učitaj bazu");
		btnUcitaj.setFont(SWTResourceManager.getFont("Liberation Sans", 20, SWT.NORMAL));
		btnUcitaj.setBounds(5, 486, 150, 50);

		btnPrintTest = new Button(shlPrijava, SWT.NONE);
		btnPrintTest.addSelectionListener(new BtnPrintTestSelectionListener());
		btnPrintTest.setText("Priprema papira");
		btnPrintTest.setFont(SWTResourceManager.getFont("Liberation Sans", 20, SWT.NORMAL));
		btnPrintTest.setBounds(190, 486, 200, 50);

		btnGasenje = new Button(shlPrijava, SWT.NONE);
		btnGasenje.addSelectionListener(new BtnGasenjeSelectionListener());
		btnGasenje.setText("Gašenje");
		btnGasenje.setFont(SWTResourceManager.getFont("Liberation Sans", 20, SWT.NORMAL));
		btnGasenje.setBounds(420, 486, 150, 50);

		btnNastavak = new Button(shlPrijava, SWT.CENTER);
		btnNastavak.setText("Nastavak");
		btnNastavak.setFont(SWTResourceManager.getFont("Liberation Sans", 20, SWT.NORMAL));
		btnNastavak.setBounds(638, 486, 150, 50);

		tLinija.addMouseListener(new textMouseListener());
		btnNastavak.addSelectionListener(new NextButtonListener());
		tVozac.addModifyListener(new TVozacModifyListener());
		tVozac.addMouseListener(new textMouseListener());
		tVozilo.addModifyListener(new TVoziloModifyListener());
		tVozilo.addMouseListener(new textMouseListener());
		tLinija.addModifyListener(new TLinijaModifyListener());

		if (Starter.DebugMode != null)  
			if (Starter.DebugMode.equals("D") || Starter.DebugMode.equals("1")){	
				  tVozac.setText("24");
			      tLozinka.setText("12345");
				  tVozilo.setText("557");
			  	  tLinija.setText("50547");
		}

	}

	private class BtnGasenjeSelectionListener extends SelectionAdapter {
		@Override
		public void widgetSelected(SelectionEvent e) {
			shlPrijava.dispose();
			if (new File(".shutdown").exists()) {
				try {
					Runtime.getRuntime().exec("/sbin/shutdown now");
				} catch (IOException e1) {
					throw new RuntimeException(e1);
				}
			} else {
				shlPrijava.dispose();

			}
		}
	}

	private class BtnPrintTestSelectionListener extends SelectionAdapter {
		@Override
		public void widgetSelected(SelectionEvent e) {
			
			PrintUtils.printPripremaPapira();
			
		/*	
			StringBuffer sb = new StringBuffer();
			sb.append("********************************\r");
			sb.append("AP d.d. Varazdin\r");
			sb.append("Gospodarska 56\r");
			sb.append("OIB: 51631089795\r");
			sb.append("\r");
			sb.append("Prijevoznik: AP d.d\r");
			sb.append("Vrijeme: " + (new SimpleDateFormat("dd.MM.yy\nHH:mm:ss").format(new Date())) + "\r");
			sb.append("--------------------------------\r");
			sb.append("--------------------------------\r");
			sb.append(" P R I P R E M A    P A P I R A \r");
			sb.append("--------------------------------\r");
			sb.append("--------------------------------\r");
			sb.append("********************************\r");
			sb.append(" \r \r \r");

			if (new File(".print").exists()) {
				char[] reset = { 27, 64, 13 };
				try {
					FileWriter out = new FileWriter(Starter.ComPortPrinter);
					out.write(reset);
					out.write(sb.toString());
					out.flush();
					out.close();
				} catch (IOException ioe) {
					ioe.printStackTrace();
				}
			} else
				System.out.println(sb.toString());
		*/	
		}
	}

	protected class NextButtonListener extends SelectionAdapter {
		@Override
		public void widgetSelected(SelectionEvent e) {
			Starter.vozac = Vozac.getBySifra(Integer.parseInt(tVozac.getText()));
			Starter.vozilo = Vozilo.getBySifra(tVozilo.getText());
			String ss  = tLozinka.getText();
			String ssp = Starter.vozac.getPassword();

			if (!tLinija.getText().toString().isEmpty())
				Starter.stupac = Stupac.getByID(new Integer(tLinija.getText()));
			if (Starter.vozac == null || Starter.stupac == null || Starter.vozilo == null) {
				MessageBox mb = new MessageBox(shlPrijava, SWT.OK | SWT.ICON_ERROR);
				mb.setMessage("Morate prijaviti vozača,vozilo i liniju!");
				mb.open();
			} 
			else 
				if ((ss.isEmpty()) || (!Starter.vozac.getPassword().equals(DbUtil.md5hash(tLozinka.getText())))) {
				MessageBox mb = new MessageBox(shlPrijava, SWT.OK | SWT.ICON_ERROR);
				mb.setMessage("Neispravna lozinka!");
				mb.open();
			} else {
				ProdajaWindow pw = new ProdajaWindow();
				boolean exit = pw.open();
				if (exit)
					shlPrijava.dispose();
			}
		}

	}

	protected class textMouseListener extends MouseAdapter {
		@Override
		public void mouseDown(MouseEvent e) {
			Text t = (Text) e.widget;
			VirtualKeyboard keypad = new VirtualKeyboard(shlPrijava);
			t.selectAll();
			keypad.open(t);

		}
	}

	protected class TVoziloModifyListener implements ModifyListener {
		@Override
		public void modifyText(ModifyEvent e) {
			Vozilo v = null;
			Text t = (Text) e.widget;
			if (t.getText().matches("\\d+"))
				v = Vozilo.getBySifra(t.getText());
			if (v == null)
				lOpisVozilo.setText("");
			else
				lOpisVozilo.setText("(" + v.getRegSt() + ") " + v.getNaziv());
		}
	}

	protected class TVozacModifyListener implements ModifyListener {
		@Override
		public void modifyText(ModifyEvent e) {
			Vozac v = null;
			Text t = (Text) e.widget;
			if (t.getText().matches("\\d+"))
				v = Vozac.getBySifra(Integer.parseInt(t.getText()));
			if (v == null)
				lOpisVozac.setText("");
			else
				lOpisVozac.setText(v.getNaziv());
		}
	}

	protected class TLinijaModifyListener implements ModifyListener {
		@Override
		public void modifyText(ModifyEvent e) {
			Integer id;
			Text t = (Text) e.widget;
			if (t.getText().matches("\\d+")) {
				id = Integer.parseInt(t.getText());
				if (Stupac.getByID(id) != null)
					lOpisLinije.setText(Stupac.getByID(id).getDescription());
				else
					lOpisLinije.setText("");
			}
		}
	}

	private class SearchStupacButtonListener extends SelectionAdapter {
		@Override
		public void widgetDefaultSelected(SelectionEvent e) {
			VRFinder vf = new VRFinder(shlPrijava);
			Stupac st = vf.open();
			if (st != null) {
				tLinija.setText(st.getId().toString());
				lOpisLinije.setText(st.getDescription());
			} else {
				tLinija.setText("");
				lOpisLinije.setText("");
			}
		}

		@Override
		public void widgetSelected(SelectionEvent e) {
			widgetDefaultSelected(e);
		}
	}

	private class UcitajBazuButtonListener extends SelectionAdapter {
		@Override
		public void widgetDefaultSelected(SelectionEvent e) {
			try {
				if (new File("/mnt/usb/baza.db").exists()) {
					Runtime.getRuntime().exec("cp /mnt/usb/baza.db .");
					if (FileChecksum.check("/mnt/usb/baza.db") == FileChecksum.check("baza.db")) {
						MessageBox mb = new MessageBox(shlPrijava, SWT.OK | SWT.ICON_INFORMATION);
						mb.setMessage("Učitana nova baza. Restartam ...");
						mb.open();
						System.exit(5);
					} else {
						MessageBox mb = new MessageBox(shlPrijava, SWT.OK | SWT.ICON_ERROR);
						mb.setMessage("Kopiranje nije uspjelo");
						mb.open();
					}
				} else {
					MessageBox mb = new MessageBox(shlPrijava, SWT.OK | SWT.ICON_ERROR);
					mb.setMessage("Ne mogu pronaći bazu");
					mb.open();
				}
			} catch (IOException e1) {
				throw new RuntimeException(e1);
			}
		}

		public void widgetSelected(SelectionEvent e) {
			widgetDefaultSelected(e);
		}
	}

}
