package hr.mit.windows;

import java.io.IOException;

import hr.mit.beans.Blagajna;
import hr.mit.beans.Obracun;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.widgets.List;

public class ObracunWindow {

	protected Shell shell;
	private Label lblObracun;
	private Button backButton;
	private Button exitButton;
	private boolean exit = false;
	private Label text;
	private Button btnGaenje;
	private List list;

	/**
	 * @wbp.parser.entryPoint
	 */
	public boolean open(Shell parent) {
		Display display = Display.getDefault();
		shell = new Shell(parent, SWT.ON_TOP);
		shell.setSize(629, 300);
		shell.setBounds(0, 0, 800, 600);
		shell.setMaximized(true);
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
		list.setFont(SWTResourceManager.getFont("Liberation Sans", 15, SWT.NORMAL));
		list.setBounds(5, 120, 275, 210);
		list.setItems(Obracun.getList());

		text = new Label(shell, SWT.NONE);
		text.setFont(SWTResourceManager.getFont("Liberation Mono", 15, SWT.NORMAL));
		text.setBounds(290, 120, 500, 210);
		text.setText(Blagajna.getObracun());

		backButton = new Button(shell, SWT.NONE);
		backButton.setFont(SWTResourceManager.getFont("Liberation Sans", 20, SWT.NORMAL));
		backButton.setBounds(227, 350, 150, 50);
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

		exitButton = new Button(shell, SWT.NONE);
		exitButton.setFont(SWTResourceManager.getFont("Liberation Sans", 20, SWT.NORMAL));
		exitButton.setBounds(447, 350, 150, 50);
		exitButton.setText("Obračun");

		btnGaenje = new Button(shell, SWT.NONE);
		btnGaenje.addSelectionListener(new BtnGaenjeSelectionListener());
		btnGaenje.setText("Gašenje");
		btnGaenje.setFont(SWTResourceManager.getFont("Liberation Sans", 20, SWT.NORMAL));
		btnGaenje.setBounds(330, 435, 150, 50);
		exitButton.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				widgetDefaultSelected(arg0);
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				Obracun.closeObracun();
				shell.dispose();
				exit = true;
			}
		});

	}

	private class BtnGaenjeSelectionListener extends SelectionAdapter {
		@Override
		public void widgetSelected(SelectionEvent e) {
			try {
				Runtime.getRuntime().exec("/sbin/shutdown now");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}
}
