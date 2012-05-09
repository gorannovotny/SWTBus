package hr.mit.windows;

import hr.mit.Starter;
import hr.mit.beans.Stavka;
import hr.mit.utils.PrintUtils;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.widgets.List;

public class VRFinder {

	protected Object result;
	protected Shell shell;
	protected Shell parent;
	private Text text;
	private Button btnNewButton;
	private Text text_1;
	private Button button;
	private List list;

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
	public VRFinder(Shell parent) {
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
		shell.setSize(684, 300);
		shell.setBounds(150, 150, 452, 300);
		text = new Text(shell, SWT.BORDER);
		text.setBounds(10, 10, 73, 27);
		btnNewButton = new Button(shell, SWT.NONE);
		btnNewButton.setFont(SWTResourceManager.getFont("Liberation Sans", 20, SWT.NORMAL));
		btnNewButton.setBounds(89, 10, 131, 27);
		btnNewButton.setText("New Button");
		text_1 = new Text(shell, SWT.BORDER);
		text_1.setBounds(226, 10, 73, 27);
		button = new Button(shell, SWT.NONE);
		button.setText("New Button");
		button.setFont(SWTResourceManager.getFont("Liberation Sans", 20, SWT.NORMAL));
		button.setBounds(305, 10, 131, 27);
		list = new List(shell, SWT.BORDER);
		list.setBounds(10, 87, 426, 201);

	}
}
