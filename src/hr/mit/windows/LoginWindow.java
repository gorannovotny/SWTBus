package hr.mit.windows;

import java.io.File;
import java.io.IOException;

import hr.mit.Starter;
import hr.mit.beans.Stupac;
import hr.mit.beans.Vozac;
import hr.mit.beans.Vozilo;
import hr.mit.beans.VozniRed;

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
	private Label lblLinija;
	private Label lblPolazak;
	private Text tVozac;
	private Text tVozilo;
	private Text tLinija;
	protected Button comboPolazak;
	private Button button;

	protected Label lOpisVozilo;
	protected Label lOpisVozac;
	protected Label lOpisLinije;
	private Button btnUitaj;

	/**
	 * @wbp.parser.entryPoint
	 */
	public void open() {
		Display display = Display.getDefault();
		shlPrijava = new Shell(display, SWT.NONE);
		shlPrijava.setBackground(SWTResourceManager.getColor(SWT.COLOR_GRAY));
		shlPrijava.setSize(450, 332);
		shlPrijava.setBounds(0, 0, 800, 600);
		// shlPrijava.setMaximized(true);
		// shlPrijava.setFullScreen(true);
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
		lblVozac.setBounds(5, 130, 160, 50);
		lblVozac.setText("Vozač");

		tVozac = new Text(shlPrijava, SWT.BORDER);
		tVozac.setFont(SWTResourceManager.getFont("Liberation Sans", 30, SWT.NORMAL));
		tVozac.setBounds(165, 130, 125, 50);

		lOpisVozac = new Label(shlPrijava, SWT.NONE);
		lOpisVozac.setBackground(SWTResourceManager.getColor(SWT.COLOR_GRAY));
		lOpisVozac.setText("");
		lOpisVozac.setFont(SWTResourceManager.getFont("Liberation Sans", 20, SWT.NORMAL));
		lOpisVozac.setBounds(310, 140, 480, 30);

		lblVozilo = new Label(shlPrijava, SWT.NONE);
		lblVozilo.setBackground(SWTResourceManager.getColor(SWT.COLOR_GRAY));
		lblVozilo.setFont(SWTResourceManager.getFont("Liberation Sans", 30, SWT.NORMAL));
		lblVozilo.setBounds(5, 180, 160, 50);
		lblVozilo.setText("Vozilo");

		tVozilo = new Text(shlPrijava, SWT.BORDER);
		tVozilo.setFont(SWTResourceManager.getFont("Liberation Sans", 30, SWT.NORMAL));
		tVozilo.setBounds(165, 180, 125, 50);

		lOpisVozilo = new Label(shlPrijava, SWT.NONE);
		lOpisVozilo.setBackground(SWTResourceManager.getColor(SWT.COLOR_GRAY));
		lOpisVozilo.setBounds(310, 190, 480, 30);
		lOpisVozilo.setFont(SWTResourceManager.getFont("Liberation Sans", 20, SWT.NORMAL));
		lOpisVozilo.setText("");

		lblLinija = new Label(shlPrijava, SWT.NONE);
		lblLinija.setBackground(SWTResourceManager.getColor(SWT.COLOR_GRAY));
		lblLinija.setFont(SWTResourceManager.getFont("Liberation Sans", 30, SWT.NORMAL));
		lblLinija.setBounds(5, 230, 160, 50);
		lblLinija.setText("Linija");

		tLinija = new Text(shlPrijava, SWT.BORDER);
		tLinija.setFont(SWTResourceManager.getFont("Liberation Sans", 30, SWT.NORMAL));
		tLinija.setBounds(165, 230, 125, 50);

		lOpisLinije = new Label(shlPrijava, SWT.NONE);
		lOpisLinije.setBackground(SWTResourceManager.getColor(SWT.COLOR_GRAY));
		lOpisLinije.setText("");
		lOpisLinije.setFont(SWTResourceManager.getFont("Liberation Sans", 20, SWT.NORMAL));
		lOpisLinije.setBounds(310, 240, 480, 30);

		lblPolazak = new Label(shlPrijava, SWT.NONE);
		lblPolazak.setBackground(SWTResourceManager.getColor(SWT.COLOR_GRAY));
		lblPolazak.setFont(SWTResourceManager.getFont("Liberation Sans", 30, SWT.NORMAL));
		lblPolazak.setBounds(5, 280, 160, 50);
		lblPolazak.setText("Polazak");

		comboPolazak = new Button(shlPrijava, SWT.READ_ONLY);
		comboPolazak.addSelectionListener(new ComboPolazakSelectionListener());
		comboPolazak.setFont(SWTResourceManager.getFont("Liberation Sans", 30, SWT.NORMAL));
		comboPolazak.setAlignment(SWT.LEFT);
		comboPolazak.setBounds(165, 280, 200, 50);

		btnUitaj = new Button(shlPrijava, SWT.NONE);
		btnUitaj.addSelectionListener(new Button_1SelectionListener());
		btnUitaj.setText("Učitaj bazu");
		btnUitaj.setFont(SWTResourceManager.getFont("Liberation Sans", 20, SWT.NORMAL));
		btnUitaj.setBounds(5, 486, 150, 50);

		button = new Button(shlPrijava, SWT.ARROW | SWT.RIGHT);
		button.setFont(SWTResourceManager.getFont("Liberation Sans", 30, SWT.NORMAL));
		button.setBounds(700, 480, 60, 56);

		tVozac.addModifyListener(new TVozacModifyListener());
		tVozac.setText("117");
		tVozac.addMouseListener(new textMouseListener());
		tVozilo.addModifyListener(new TVoziloModifyListener());
		tVozilo.setText("557");
		tVozilo.addMouseListener(new textMouseListener());
		tLinija.addModifyListener(new TLinijaModifyListener());
		tLinija.setText("22209");
		tLinija.addMouseListener(new textMouseListener());
		button.addSelectionListener(new ButtonSelectionListener());

	}

	protected class ButtonSelectionListener extends SelectionAdapter {
		@Override
		public void widgetSelected(SelectionEvent e) {
			Starter.vozac = Vozac.getBySifra(Integer.parseInt(tVozac.getText()));
			Starter.vozilo = Vozilo.getBySifra(tVozilo.getText());
			if (Stupac.getList().length > 0)
				Starter.stupac = Stupac.get((Integer) comboPolazak.getData());
			if (Starter.vozac == null || Starter.stupac == null || Starter.vozilo == null) {
				MessageBox mb = new MessageBox(shlPrijava, SWT.OK | SWT.ICON_ERROR);
				mb.setMessage("Morate prijaviti vozača,vozilo i liniju!");
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
				lOpisLinije.setText(VozniRed.getNaziv(id));
				Stupac.setVozniRed(id);
			}
			if (Stupac.getList().length > 0) {
				comboPolazak.setText(Stupac.getList()[0]);
				comboPolazak.setData(0);
			} else {
				comboPolazak.setText("");
				comboPolazak.setData(null);

			}
		}
	}

	private class ComboPolazakSelectionListener extends SelectionAdapter {
		@Override
		public void widgetDefaultSelected(SelectionEvent e) {
			Integer index = (Integer) comboPolazak.getData();
			if (index != null) {
				Picker picker = new Picker(comboPolazak, Stupac.getArrayList(), index);
				comboPolazak = picker.open();
			}

		}

		@Override
		public void widgetSelected(SelectionEvent e) {
			widgetDefaultSelected(e);
		}
	}

	private class Button_1SelectionListener extends SelectionAdapter {
		@Override
		public void widgetDefaultSelected(SelectionEvent e) {
			try {
				if (new File("/mnt/usb/baza.db").exists())
					Runtime.getRuntime().exec("cp /mnt/usb/baza.db .");
				else {
					MessageBox mb = new MessageBox(shlPrijava, SWT.OK | SWT.ICON_ERROR);
					mb.setMessage("Ne mogu pronaći bazu");
					mb.open();
				}
			} catch (IOException e1) {
				throw new RuntimeException(e1);
			}
		}

		@Override
		public void widgetSelected(SelectionEvent e) {
			// TODO Auto-generated method stub
			widgetDefaultSelected(e);
		}
	}

}
