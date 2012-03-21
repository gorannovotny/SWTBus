package hr.mit.windows;

import javax.swing.text.StyledEditorKit.BoldAction;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.wb.swt.SWTResourceManager;

public class ObracunWindow {

	protected Shell shell;
	private Label lblObracun;
	private Button backButton;
	private Button exitButton;
	private boolean exit = false;
	

	/**
	 * @wbp.parser.entryPoint
	 */
	public boolean open(Shell parent) {
		Display display = Display.getDefault();
		shell = new Shell(parent,SWT.ON_TOP);
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
		lblObracun.setBounds(290, 10, 220, 67);
		lblObracun.setText("Obračun");
		
		backButton = new Button(shell, SWT.NONE);
		backButton.setBounds(40, 469, 75, 26);
		backButton.setText("Povratak");
		backButton.addSelectionListener( new SelectionListener() {
			public void widgetSelected(SelectionEvent arg0) {
				widgetDefaultSelected(arg0);
			}
			public void widgetDefaultSelected(SelectionEvent arg0) {
				shell.dispose();
			}
		});

		exitButton = new Button(shell, SWT.NONE);
		exitButton.setBounds(140, 469, 75, 26);
		exitButton.setText("Izlaz");
		exitButton.addSelectionListener( new SelectionListener() {
			public void widgetSelected(SelectionEvent arg0) {
				widgetDefaultSelected(arg0);
			}
			public void widgetDefaultSelected(SelectionEvent arg0)  {
				shell.dispose();
				exit = true;
			}
		});

	}




}
