package hr.mit.windows;

import java.io.File;
import java.io.IOException;

import hr.mit.beans.Obracun;
import hr.mit.utils.FileChecksum;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.wb.swt.SWTResourceManager;

public class ObracunWindow {

	protected Shell shell;
	private Label lblObracun;
	private Button backButton;
	private Button exitButton;
	private boolean exit = false;
	private Label text;
	private Button btnGaenje;
	private List list;
	private Button btnPrint;
	private Button btnSnimi;

	/**
	 * @wbp.parser.entryPoint
	 */
	public boolean open(Shell parent) {
		Display display = Display.getDefault();
		shell = new Shell(parent, SWT.NONE);
		shell.setSize(629, 331);
		// shell.setSize(629, 300);
		shell.setBounds(0, 0, 800, 600);
		// shell.setMaximized(true);
		shell.setFullScreen(true);
		createContents();
		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		return exit;
	}

	protected void createContents() {
		lblObracun = new Label(shell, SWT.NONE);
		lblObracun.setFont(SWTResourceManager.getFont("Liberation Sans", 45, SWT.NORMAL));
		lblObracun.setBounds(290, 10, 229, 67);
		lblObracun.setText("Obračun");

		list = new List(shell, SWT.BORDER);
		list.setDragDetect(false);
		list.addSelectionListener(new ListSelectionListener());
		list.setFont(SWTResourceManager.getFont("Liberation Sans", 15, SWT.NORMAL));
		list.setBounds(5, 120, 275, 400);
		list.setItems(Obracun.getList());

		text = new Label(shell, SWT.NONE);
		text.setFont(SWTResourceManager.getFont("Liberation Mono", 15, SWT.NORMAL));
		text.setBounds(290, 120, 500, 400);
		text.setText("");

		backButton = new Button(shell, SWT.NONE);
		backButton.setFont(SWTResourceManager.getFont("Liberation Sans", 20, SWT.NORMAL));
		backButton.setBounds(5, 540, 150, 50);
		backButton.setText("Povratak");
		backButton.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				widgetDefaultSelected(arg0);
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				shell.dispose();
			}
		});

		btnSnimi = new Button(shell, SWT.NONE);
		btnSnimi.addSelectionListener(new BtnSnimiSelectionListener());
		btnSnimi.setText("Snimi");
		btnSnimi.setFont(SWTResourceManager.getFont("Liberation Sans", 20, SWT.NORMAL));
		btnSnimi.setBounds(162, 540, 150, 50);

		btnGaenje = new Button(shell, SWT.NONE);
		btnGaenje.addSelectionListener(new BtnGaenjeSelectionListener());
		btnGaenje.setText("Gašenje");
		btnGaenje.setFont(SWTResourceManager.getFont("Liberation Sans", 20, SWT.NORMAL));
		btnGaenje.setBounds(317, 540, 150, 50);

		exitButton = new Button(shell, SWT.NONE);
		exitButton.setFont(SWTResourceManager.getFont("Liberation Sans", 20, SWT.NORMAL));
		exitButton.setBounds(472, 540, 150, 50);
		exitButton.setText("Obračun");

		btnPrint = new Button(shell, SWT.NONE);
		btnPrint.addSelectionListener(new BtnPrintSelectionListener());
		btnPrint.setText("Print");
		btnPrint.setFont(SWTResourceManager.getFont("Liberation Sans", 20, SWT.NORMAL));
		btnPrint.setBounds(627, 540, 150, 50);

		exitButton.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				widgetDefaultSelected(arg0);
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				if (Obracun.imaZaObracun()) {
					Obracun.closeObracun();
					list.setItems(Obracun.getList());
					list.select(0);
					text.setText(Obracun.getObracun(null));
				} else {
					MessageBox mb = new MessageBox(shell, SWT.OK | SWT.ICON_INFORMATION);
					mb.setMessage("Nema nijedne stavke za obračun");
					mb.open();
				}

			}
		});

	}

	private class BtnGaenjeSelectionListener extends SelectionAdapter {
		@Override
		public void widgetSelected(SelectionEvent e) {
			shell.dispose();
			exit = true;
			if (new File(".shutdown").exists()) {
				try {
					Runtime.getRuntime().exec("/sbin/shutdown now");
				} catch (IOException e1) {
					throw new RuntimeException(e1);
				}
			} else {
				shell.dispose();
				exit = true;

			}
		}
	}

	private class ListSelectionListener extends SelectionAdapter {
		@Override
		public void widgetDefaultSelected(SelectionEvent e) {
			List l = (List) e.widget;
			Obracun obr = Obracun.get(l.getSelectionIndex());
			text.setText(Obracun.getObracun(obr.getId()));
		}

		@Override
		public void widgetSelected(SelectionEvent e) {
			// TODO Auto-generated method stub
			widgetDefaultSelected(e);
		}
	}

	private class BtnSnimiSelectionListener extends SelectionAdapter {
		@Override
		public void widgetDefaultSelected(SelectionEvent e) {
			try {
				if (new File("/mnt/usb").exists()) {
					Runtime.getRuntime().exec("cp prodaja.db /mnt/usb");
					if (FileChecksum.check("/mnt/usb/prodaja.db") == FileChecksum.check("prodaja.db")) {
						MessageBox mb = new MessageBox(shell, SWT.OK | SWT.ICON_INFORMATION);
						mb.setMessage("Datoteka prodaje iskopirana na USB");
						mb.open();
					}
					else {
						MessageBox mb = new MessageBox(shell, SWT.OK | SWT.ICON_ERROR);
						mb.setMessage("Kopiranje nije uspjelo");
						mb.open();
					}
				} else {
					MessageBox mb = new MessageBox(shell, SWT.OK | SWT.ICON_ERROR);
					mb.setMessage("USB stick nije priključen");
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

	private class BtnPrintSelectionListener extends SelectionAdapter {
		@Override
		public void widgetSelected(SelectionEvent e) {
			Obracun.print(Obracun.get(list.getSelectionIndex()));
		}
	}
}
