package hr.mit.windows;

import hr.mit.Starter;
import hr.mit.beans.Stavka;
import hr.mit.utils.PrintUtils;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.wb.swt.SWTResourceManager;

public class Zadnji {

	protected Object result;
	protected Shell shell;
	protected Shell parent;
	private Text text;
	private Button btnZatvori;
	private Button btnPrint;

	/**
	 * Create the dialog.
	 * 
	 * @param parent
	 * @param style
	 */
	/**
	 * Open the dialog.
	 * 
	 * @return the result
	 */
	public Zadnji(Shell parent) {
		this.parent = parent;
	}

	/**
	 * @wbp.parser.entryPoint
	 */
	public Object open() {
		createContents();
		shell.open();
		shell.layout();
		Display display = parent.getDisplay();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		return result;
	}

	/**
	 * Create contents of the dialog.
	 */
	private void createContents() {
		shell = new Shell(parent, SWT.APPLICATION_MODAL | SWT.ON_TOP);
		shell.setSize(450, 260);
		shell.setBounds(150, 150, 452, 300);

		text = new Text(shell, SWT.MULTI | SWT.BORDER);
		text.setBounds(5, 5, 440, 180);
		text.setEditable(false);
		text.setFont(SWTResourceManager.getFont("Liberation Mono", 14, SWT.NORMAL));
		text.setText(Stavka.getOldDescription());

		btnZatvori = new Button(shell, SWT.NONE);
		btnZatvori.setFont(SWTResourceManager.getFont("Liberation Sans", 20, SWT.NORMAL));
		btnZatvori.addSelectionListener(new BtnZatvoriSelectionListener());
		btnZatvori.setBounds(50, 200, 150, 75);
		btnZatvori.setText("Zatvori");

		btnPrint = new Button(shell, SWT.NONE);
		btnPrint.setFont(SWTResourceManager.getFont("Liberation Sans", 20, SWT.NORMAL));
		btnPrint.addSelectionListener(new BtnZatvoriSelectionListener());
		btnPrint.setBounds(250, 200, 150, 75);
		btnPrint.setText("Storno");

	}

	private class BtnZatvoriSelectionListener extends SelectionAdapter {
		@Override
		public void widgetDefaultSelected(SelectionEvent e) {
			if (e.widget.equals(btnZatvori))
				shell.dispose();
			if (e.widget.equals(btnPrint))
				PrintUtils.print(Starter.vozac, Stavka.getStorno(Stavka.getOldList()));
		}

		@Override
		public void widgetSelected(SelectionEvent e) {
			widgetDefaultSelected(e);
		}
	}
}
