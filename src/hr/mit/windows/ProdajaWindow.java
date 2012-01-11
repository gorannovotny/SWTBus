package hr.mit.windows;

import hr.mit.beans.Postaja;
import hr.mit.utils.DbUtil;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

public class ProdajaWindow implements SelectionListener {

	protected Shell shell;
	private Text txtHello;
	private Postaja p;
	private Combo combo_1;

	Integer vozacID;
	Integer stupacID;

	/**
	 * @wbp.parser.entryPoint
	 */
	public ProdajaWindow(Integer vozacID, Integer stupacID) {
		this.vozacID = vozacID;
		this.stupacID = stupacID;
		this.p = new Postaja(stupacID);
	}

	public void open() {
		Display display = Display.getDefault();
		createContents();
		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	protected void createContents() {
		shell = new Shell(SWT.SHELL_TRIM);

		shell.setSize(800, 600);
		shell.setText("SWT Application");
		Button btnNewButton = new Button(shell, SWT.NONE);
		btnNewButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				((Button) e.widget).setText("Bok");
			}
		});
		btnNewButton.setBounds(345, 94, 103, 45);
		btnNewButton.setText("New Button");

		txtHello = new Text(shell, SWT.BORDER);
		txtHello.setToolTipText("Tu sam");
		txtHello.setText("Hello");
		txtHello.setBounds(345, 145, 75, 24);

		Combo combo = new Combo(shell, SWT.READ_ONLY);
		combo.addSelectionListener(this);
		combo.setFont(SWTResourceManager.getFont("Liberation Sans", 19, SWT.NORMAL));
		combo.setItems(p.getList());
		combo.setBounds(30, 20, 340, 50);
		combo.select(0);

		combo_1 = new Combo(shell, SWT.READ_ONLY);
		combo_1.setFont(SWTResourceManager.getFont("Liberation Sans", 19, SWT.NORMAL));
		combo_1.setBounds(412, 20, 340, 50);
		combo_1.setItems(p.getList());
		combo_1.select(1);
	}

	public void modifyText(ModifyEvent e) {
	}

	public void widgetSelected(SelectionEvent e) {
		Combo c = (Combo) e.widget;
		txtHello.setText(p.getID(c.getSelectionIndex()).toString());
		combo_1.select(c.getSelectionIndex() + 1);
	}

	public void widgetDefaultSelected(SelectionEvent e) {
		Combo c = (Combo) e.widget;
		txtHello.setText(p.getID(c.getSelectionIndex()).toString());
		combo_1.select(c.getSelectionIndex() + 1);
	}
}
