package hr.mit.windows;

import hr.mit.beans.Stupac;

import org.eclipse.swt.SWT;
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

public class VRFinder {

	protected Object result;
	protected Shell shell;
	protected Shell parent;
	private Text tOdPostaje;
	private Text tDoPostaje;
	private Button btnDummy;
	private Stupac stupac;
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
	public Stupac open() {
		createContents();
		shell.open();
		shell.layout();
		Display display = parent.getDisplay();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		return stupac;
	}

	/**
	 * Create contents of the dialog.
	 */
	private void createContents() {
		shell = new Shell(parent, SWT.APPLICATION_MODAL);
		shell.setSize(760, 400);
		shell.setBounds(20, 20, 760, 440);
		tOdPostaje = new Text(shell, SWT.BORDER);
		tOdPostaje.setBounds(10, 60, 360, 50);
		tOdPostaje.setFont(SWTResourceManager.getFont("Liberation Sans", 25, SWT.NORMAL));
		tDoPostaje = new Text(shell, SWT.BORDER);
		tDoPostaje.setBounds(388, 60, 360, 50);
		tDoPostaje.setFont(SWTResourceManager.getFont("Liberation Sans", 25, SWT.NORMAL));

		btnDummy = new Button(shell, SWT.NONE);
		btnDummy.addSelectionListener(new BtnDummySelectionListener());
		btnDummy.setBounds(10, 160, 738, 50);
		btnDummy.setFont(SWTResourceManager.getFont("Liberation Sans", 20, SWT.NORMAL)); // 20
		btnDummy.setAlignment(SWT.LEFT);
		btnDummy.setText("Traži");
		lblNewLabel = new Label(shell, SWT.NONE);
		lblNewLabel.setFont(SWTResourceManager.getFont("Liberation Sans", 30, SWT.NORMAL));// 30
		lblNewLabel.setBounds(10, 10, 610, 44);
		lblNewLabel.setText("Pretraga voznih redova");
		tOdPostaje.addMouseListener(new textMouseListener());
		tDoPostaje.addMouseListener(new textMouseListener());
	}

	private class BtnDummySelectionListener extends SelectionAdapter {
		public void widgetDefaultSelected(SelectionEvent e) {
			Stupac.setupFinder(tOdPostaje, tDoPostaje);
			if (Stupac.getList().length == 0) {
				MessageBox mb = new MessageBox(shell, SWT.OK | SWT.ICON_ERROR);
				mb.setMessage("Nema rezultata pretraživanja !");
				mb.open();
			} else {
				Picker picker = new Picker(btnDummy, Stupac.getArrayList(), 0);
				btnDummy = picker.open();
				if (btnDummy.getData() != null)
					stupac = Stupac.get((Integer) btnDummy.getData());
			}
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
