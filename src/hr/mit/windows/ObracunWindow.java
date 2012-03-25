package hr.mit.windows;

import hr.mit.beans.Blagajna;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.widgets.Text;

public class ObracunWindow {

	protected Shell shell;
	private Label lblObracun;
	private Button backButton;
	private Button exitButton;
	private boolean exit = false;
	private Label text;

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

		text = new Label(shell, SWT.NONE);
		text.setFont(SWTResourceManager.getFont("Liberation Mono", 15, SWT.NORMAL));
		text.setBounds(120, 120, 555, 210);
		text.setText(Blagajna.getObracun());

		backButton = new Button(shell, SWT.NONE);
		backButton.setFont(SWTResourceManager.getFont("Liberation Sans", 20, SWT.NORMAL));
		backButton.setBounds(227, 350, 150, 50);
		backButton.setText("Povratak");
		backButton.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent arg0) {
				widgetDefaultSelected(arg0);
			}

			public void widgetDefaultSelected(SelectionEvent arg0) {
				shell.dispose();
			}
		});

		exitButton = new Button(shell, SWT.NONE);
		exitButton.setFont(SWTResourceManager.getFont("Liberation Sans", 20, SWT.NORMAL));
		exitButton.setBounds(447, 350, 150, 50);
		exitButton.setText("Obračun");
		exitButton.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent arg0) {
				widgetDefaultSelected(arg0);
			}

			public void widgetDefaultSelected(SelectionEvent arg0) {
				shell.dispose();
				exit = true;
			}
		});

	}
}
