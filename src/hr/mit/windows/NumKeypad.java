package hr.mit.windows;

import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.SWT;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class NumKeypad extends Dialog {

	protected Object result;
	protected Shell shell;
	private Button bSlash;
	private Button bStar;
	private Button bMinus;
	private Button btnBs;
	private Button b4;
	private Button b9;
	private Button b8;
	private Button b7;
	private Button b2;
	private Button b6;
	private Button b1;
	private Button b5;
	private Button b3;
	private Button b0;
	private Button bzarez;
	private Button bPlus;
	private Button btnOk;
	private Text unos;

	/**
	 * Create the dialog.
	 * 
	 * @param parent
	 * @param style
	 */
	public NumKeypad(Shell parent, int style) {
		super(parent, style);
		setText("SWT Dialog");
	}

	/**
	 * Open the dialog.
	 * 
	 * @return the result
	 */
	public Object open(Text polje) {
		unos = polje;
		createContents();
		shell.open();
		shell.layout();
		Display display = getParent().getDisplay();
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
		shell = new Shell(getParent(), getStyle());
		shell.setSize(201, 271);
		shell.setText(getText());
		{
			bSlash = new Button(shell, SWT.NONE);
			bSlash.addSelectionListener(new ButtonSelectionListener());
			bSlash.setBounds(0, 0, 45, 45);
			bSlash.setText("/");
		}
		{
			bStar = new Button(shell, SWT.NONE);
			bStar.addSelectionListener(new ButtonSelectionListener());
			bStar.setText("*");
			bStar.setBounds(50, 0, 45, 45);
		}
		{
			bMinus = new Button(shell, SWT.NONE);
			bMinus.addSelectionListener(new ButtonSelectionListener());
			bMinus.setText("-");
			bMinus.setBounds(100, 0, 45, 45);
		}
		{
			btnBs = new Button(shell, SWT.NONE);
			btnBs.addSelectionListener(new ButtonSelectionListener());
			btnBs.setText("BS");
			btnBs.setBounds(150, 0, 45, 45);
		}
		{
			b7 = new Button(shell, SWT.NONE);
			b7.addSelectionListener(new ButtonSelectionListener());
			b7.setText("7");
			b7.setBounds(0, 50, 45, 45);
		}
		{
			b8 = new Button(shell, SWT.NONE);
			b8.addSelectionListener(new ButtonSelectionListener());
			b8.setText("8");
			b8.setBounds(50, 50, 45, 45);
		}
		{
			b9 = new Button(shell, SWT.NONE);
			b9.addSelectionListener(new ButtonSelectionListener());
			b9.setText("9");
			b9.setBounds(100, 50, 45, 45);
		}
		{
			b6 = new Button(shell, SWT.NONE);
			b6.addSelectionListener(new ButtonSelectionListener());
			b6.setText("6");
			b6.setBounds(100, 100, 45, 45);
		}
		{
			bPlus = new Button(shell, SWT.NONE);
			bPlus.addSelectionListener(new ButtonSelectionListener());
			bPlus.setText("+");
			bPlus.setBounds(150, 50, 45, 95);
		}
		{
			b4 = new Button(shell, SWT.NONE);
			b4.addSelectionListener(new ButtonSelectionListener());
			b4.setText("4");
			b4.setBounds(0, 100, 45, 45);
		}
		{
			b5 = new Button(shell, SWT.NONE);
			b5.addSelectionListener(new ButtonSelectionListener());
			b5.setFont(SWTResourceManager.getFont("Liberation Sans", 14, SWT.NORMAL));
			b5.setText("5");
			b5.setBounds(50, 100, 45, 45);
		}
		{
			b1 = new Button(shell, SWT.NONE);
			b1.addSelectionListener(new ButtonSelectionListener());
			b1.setFont(SWTResourceManager.getFont("Liberation Sans", 14, SWT.NORMAL));
			b1.setText("1");
			b1.setBounds(0, 150, 45, 45);
		}
		{
			b2 = new Button(shell, SWT.NONE);
			b2.addSelectionListener(new ButtonSelectionListener());
			b2.setFont(SWTResourceManager.getFont("Liberation Sans", 14, SWT.NORMAL));
			b2.setText("2");
			b2.setBounds(50, 150, 45, 45);
		}
		{
			b3 = new Button(shell, SWT.NONE);
			b3.addSelectionListener(new ButtonSelectionListener());
			b3.setFont(SWTResourceManager.getFont("Liberation Sans", 14, SWT.NORMAL));
			b3.setText("3");
			b3.setBounds(100, 150, 45, 45);
		}
		{
			bzarez = new Button(shell, SWT.NONE);
			bzarez.addSelectionListener(new ButtonSelectionListener());
			bzarez.setText(",");
			bzarez.setBounds(100, 200, 45, 45);
		}
		{
			btnOk = new Button(shell, SWT.NONE);
			btnOk.addSelectionListener(new ButtonSelectionListener());
			btnOk.setText("OK");
			btnOk.setBounds(150, 150, 45, 95);
		}
		{
			b0 = new Button(shell, SWT.NONE);
			b0.addSelectionListener(new ButtonSelectionListener());
			b0.setText("0");
			b0.setBounds(0, 200, 95, 45);
		}

	}

	private class ButtonSelectionListener extends SelectionAdapter {
		public void widgetSelected(SelectionEvent e) {
			Button b = (Button) e.widget;
			if (b.getText().equals("OK")) {
				shell.dispose();
			} else if (b.getText().equals("BS")) {
				unos.setText(unos.getText(0, unos.getCharCount() - 2));
			} else
				unos.append(b.getText());
		}
	}
}
