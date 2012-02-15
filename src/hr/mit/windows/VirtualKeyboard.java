package hr.mit.windows;

import org.eclipse.core.databinding.SetBinding;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.wb.swt.SWTResourceManager;

public class VirtualKeyboard extends Dialog {

	protected Object result;
	protected Shell shell;

	private Text polje;

	/**
	 * Create the dialog.
	 * 
	 * @param parent
	 * @param style
	 */
	public VirtualKeyboard(Shell parent, int style) {
		super(parent, style);
		setText("SWT Dialog");

	}

	/**
	 * Open the dialog.
	 * 
	 * @return the result
	 */
	public Object open(Text polje) {
		this.polje = polje;
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
		shell.setSize(620, 270);
		shell.setLocation(getParent().getLocation().x, getParent().getLocation().y + 330);
		shell.setText(getText());
		shell.setBackground(SWTResourceManager.getColor(SWT.COLOR_DARK_GRAY));
		SelectionListener listener = new MyButtonSelectionListener();

		MyButton b1 = new MyButton(shell, "1", 1, 1, listener);
		MyButton b2 = new MyButton(shell, "2", 1, 2, listener);
		MyButton b3 = new MyButton(shell, "3", 1, 3, listener);
		MyButton b4 = new MyButton(shell, "4", 1, 4, listener);
		MyButton b5 = new MyButton(shell, "5", 1, 5, listener);
		MyButton b6 = new MyButton(shell, "6", 1, 6, listener);
		MyButton b7 = new MyButton(shell, "7", 1, 7, listener);
		MyButton b8 = new MyButton(shell, "8", 1, 8, listener);
		MyButton b9 = new MyButton(shell, "9", 1, 9, listener);
		MyButton b0 = new MyButton(shell, "0", 1, 10, listener);
		// MyButton apos = new MyButton(shell, "'", 1, 11, listener);
		// MyButton plus = new MyButton(shell, "+", 1, 12, listener);
		MyButton bs = new MyButton(shell, "BS", 1, 11, listener);

		MyButton q = new MyButton(shell, "Q", 2, 1, listener);
		MyButton w = new MyButton(shell, "W", 2, 2, listener);
		MyButton e = new MyButton(shell, "E", 2, 3, listener);
		MyButton r = new MyButton(shell, "R", 2, 4, listener);
		MyButton t = new MyButton(shell, "T", 2, 5, listener);
		MyButton z = new MyButton(shell, "Z", 2, 6, listener);
		MyButton u = new MyButton(shell, "U", 2, 7, listener);
		MyButton i = new MyButton(shell, "I", 2, 8, listener);
		MyButton o = new MyButton(shell, "O", 2, 9, listener);
		MyButton p = new MyButton(shell, "P", 2, 10, listener);
		MyButton š = new MyButton(shell, "Š", 2, 11, listener);
		MyButton đ = new MyButton(shell, "Đ", 2, 12, listener);
		MyButton a = new MyButton(shell, "A", 3, 1, listener);
		MyButton s = new MyButton(shell, "S", 3, 2, listener);
		MyButton d = new MyButton(shell, "D", 3, 3, listener);
		MyButton f = new MyButton(shell, "F", 3, 4, listener);
		MyButton g = new MyButton(shell, "G", 3, 5, listener);
		MyButton h = new MyButton(shell, "H", 3, 6, listener);
		MyButton j = new MyButton(shell, "J", 3, 7, listener);
		MyButton k = new MyButton(shell, "K", 3, 8, listener);
		MyButton l = new MyButton(shell, "L", 3, 9, listener);
		MyButton č = new MyButton(shell, "Č", 3, 10, listener);
		MyButton ć = new MyButton(shell, "Ć", 3, 11, listener);
		MyButton ž = new MyButton(shell, "Ž", 3, 12, listener);
		MyButton y = new MyButton(shell, "Y", 4, 1, listener);
		MyButton x = new MyButton(shell, "X", 4, 2, listener);
		MyButton c = new MyButton(shell, "C", 4, 3, listener);
		MyButton v = new MyButton(shell, "V", 4, 4, listener);
		MyButton b = new MyButton(shell, "B", 4, 5, listener);
		MyButton n = new MyButton(shell, "N", 4, 6, listener);
		MyButton m = new MyButton(shell, "M", 4, 7, listener);
		MyButton zarez = new MyButton(shell, ",", 4, 8, listener);
		MyButton tocka = new MyButton(shell, ".", 4, 9, listener);
		MyButton minus = new MyButton(shell, "-", 4, 10, listener);
		MyButton plus = new MyButton(shell, "+", 4, 11, listener);
		MyButton slash = new MyButton(shell, "/", 4, 12, listener);

		MyButton ok = new MyButton(shell, "OK", 5, 11, listener);
		MyButton space = new MyButton(shell, " ", 5, 1, listener);

	}

	private class MyButtonSelectionListener extends SelectionAdapter {
		public void widgetSelected(SelectionEvent e) {
			widgetDefaultSelected(e);
		}

		public void widgetDefaultSelected(SelectionEvent e) {
			Button b = (Button) e.widget;
			if (b.getText().equals("OK")) {
				shell.dispose();
			} else if (b.getText().equals("BS")) {
				polje.setText(polje.getText(0, polje.getCharCount() - 2));
			} else
				polje.append(b.getText());
		}

	}
}
