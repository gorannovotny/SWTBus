package hr.mit.windows;

import hr.mit.Starter;
import hr.mit.beans.Stavka;
import hr.mit.beans.VozniRed;
import hr.mit.utils.PrintUtils;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Label;

public class VRFinder {

	protected Object result;
	protected Shell shell;
	protected Shell parent;
	private Text tOdPostaje;
	private Text tDoPostaje;
	private Button btnDummy;
	private int index;
	private Label lblNewLabel;

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
	public VozniRed open() {
		createContents();
		shell.open();
		shell.layout();
		Display display = parent.getDisplay();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		return VozniRed.get(index);
	}

	/**
	 * Create contents of the dialog.
	 */
	private void createContents() {
		shell = new Shell(parent, SWT.APPLICATION_MODAL | SWT.ON_TOP);
		shell.setSize(760, 400);
		shell.setBounds(20, 20, 760, 400);
		tOdPostaje = new Text(shell, SWT.BORDER);
		tOdPostaje.setBounds(10, 60, 360, 50);
		tOdPostaje.setFont(SWTResourceManager.getFont("Liberation Sans", 25, SWT.NORMAL));
		tDoPostaje = new Text(shell, SWT.BORDER);
		tDoPostaje.setBounds(388, 60, 360, 50);
		tDoPostaje.setFont(SWTResourceManager.getFont("Liberation Sans", 25, SWT.NORMAL));

		btnDummy = new Button(shell, SWT.NONE);
		btnDummy.addSelectionListener(new BtnDummySelectionListener());
		btnDummy.setBounds(10, 120, 738, 50);
		btnDummy.setFont(SWTResourceManager.getFont("Liberation Sans", 20, SWT.NORMAL));
		btnDummy.setAlignment(SWT.LEFT);
		btnDummy.setText("Traži");
		lblNewLabel = new Label(shell, SWT.NONE);
		lblNewLabel.setFont(SWTResourceManager.getFont("Liberation Sans", 30, SWT.NORMAL));
		lblNewLabel.setBounds(10, 10, 610, 44);
		lblNewLabel.setText("Pretraga voznih redova");
		tOdPostaje.addMouseListener(new textMouseListener());
		tDoPostaje.addMouseListener(new textMouseListener());
	}

	private class BtnDummySelectionListener extends SelectionAdapter {
		public void widgetDefaultSelected(SelectionEvent e) {
			VozniRed.setupFinder(tOdPostaje.getText(), tDoPostaje.getText());
			Picker picker = new Picker(btnDummy, VozniRed.getArrayList(), 0);
			btnDummy = picker.open();
			if (btnDummy.getData() != null ) index = (Integer) btnDummy.getData();
			shell.dispose();
		}

		public void widgetSelected(SelectionEvent e) {
			widgetDefaultSelected(e);
		}
	}

	protected class textMouseListener extends MouseAdapter {
		@Override
		public void mouseDown(MouseEvent e) {
			Text t = (Text) e.widget;
			VirtualKeyboard keypad = new VirtualKeyboard(shell);
			t.selectAll();
			keypad.open(t);

		}
	}
}
