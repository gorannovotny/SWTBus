package hr.mit.windows;

import hr.mit.beans.Postaja;
import hr.mit.beans.Stavka;
import hr.mit.beans.Stupac;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

public class Prelaz extends VRFinder {

	private Postaja prelaz;

	public Prelaz(Shell parent) {
		super(parent);
	}

	public Stavka open(Stavka stavka) {
		createContents(stavka);
		shell.open();
		shell.layout();
		Display display = parent.getDisplay();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		Stavka s = new Stavka(stupac, stavka.getDoPostaje(), prelaz, stavka.getKarta(), stavka.getPopust()); 
		s.setJePrelazna(true);
		return s;
	}

	protected void createContents(Stavka stavka) {
		shell = new Shell(parent, SWT.APPLICATION_MODAL);
		shell.setSize(760, 400);
		shell.setBounds(20, 20, 760, 500);
		tOdPostaje = new Text(shell, SWT.BORDER);
		tOdPostaje.setBounds(10, 60, 360, 50);
		tOdPostaje.setFont(SWTResourceManager.getFont("Liberation Sans", 25, SWT.NORMAL));
		tOdPostaje.setText(stavka.getDoPostaje().getNaziv());
		tOdPostaje.setEnabled(false);
		tDoPostaje = new Text(shell, SWT.BORDER);
		tDoPostaje.setBounds(388, 60, 360, 50);
		tDoPostaje.setFont(SWTResourceManager.getFont("Liberation Sans", 25, SWT.NORMAL));

		btnDummy = new Button(shell, SWT.NONE);
		btnDummy.addSelectionListener(new BtnDummySelectionListener());
		btnDummy.setBounds(10, 150, 738, 50);
		btnDummy.setFont(SWTResourceManager.getFont("Liberation Sans", 20, SWT.NORMAL));
		btnDummy.setAlignment(SWT.LEFT);
		btnDummy.setText("Traži");

		btnExit = new Button(shell, SWT.NONE);
		btnExit.addSelectionListener(new BtnExitSelectionListener());
		btnExit.setBounds(675, 5, 75, 50);
		btnExit.setFont(SWTResourceManager.getFont("Liberation Sans", 20, SWT.NORMAL));
		btnExit.setText("Izlaz");

		lblNewLabel = new Label(shell, SWT.NONE);
		lblNewLabel.setFont(SWTResourceManager.getFont("Liberation Sans", 30, SWT.NORMAL));
		lblNewLabel.setBounds(10, 10, 410, 44);
		lblNewLabel.setText("Prelaz");

		tDoPostaje.addMouseListener(new textMouseListener());

		lblSearchPostajaOd = new Label(shell, SWT.NONE);
		lblSearchPostajaOd.setFont(SWTResourceManager.getFont("Liberation Sans", 16, SWT.NORMAL));// 30
		lblSearchPostajaOd.setBounds(10 + 35, 115, 290, 35);
		lblSearchPostajaOd.setText(stavka.getDoPostaje().getNaziv());

		lblSearchPostajaDo = new Label(shell, SWT.NONE);
		lblSearchPostajaDo.setFont(SWTResourceManager.getFont("Liberation Sans", 16, SWT.NORMAL));// 30
		lblSearchPostajaDo.setBounds(388 + 35, 115, 290, 35);
		lblSearchPostajaDo.setText("??");

		tDoPostaje.addVerifyListener(new SearchVerifyListener(lblSearchPostajaDo));

	}

	protected class BtnDummySelectionListener extends SelectionAdapter {
		public void widgetDefaultSelected(SelectionEvent e) {
			if (lblSearchPostajaDo.getText().equals("??") || lblSearchPostajaOd.getText().equals("??") || lblSearchPostajaDo.getText().equals("") || lblSearchPostajaOd.getText().equals("")) {
				MessageBox mb1 = new MessageBox(shell, SWT.OK | SWT.ICON_ERROR);
				mb1.setMessage("Nisu upisane postaje !");
				mb1.open();
				return;
			}

			float Polazak = 0;
			Stupac.setupFinder(tOdPostaje, tDoPostaje, Polazak);
			if (Stupac.getList().length == 0) {
				MessageBox mb = new MessageBox(shell, SWT.OK | SWT.ICON_ERROR);
				mb.setMessage("Nema rezultata pretraživanja !");
				mb.open();
			} else {
				btnExit.setEnabled(false);
				Picker picker = new Picker(btnDummy, Stupac.getArrayList(), 0);
				btnDummy = picker.open();
				if (btnDummy.getData() != null) {
					stupac = Stupac.get((Integer) btnDummy.getData());
					prelaz = Postaja.getByNaziv(tDoPostaje.getText());
				}
			}
			shell.dispose();
		}

		public void widgetSelected(SelectionEvent e) {
			widgetDefaultSelected(e);
		}
	}
}
