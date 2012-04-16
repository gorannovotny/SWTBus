package hr.mit.windows;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

public class VirtualKeyboard {

	protected Object result;
	protected Shell shell;

	private Text polje;

	public VirtualKeyboard(Shell parent) {
		this.shell = parent;

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
		final Display display = shell.getDisplay();
		Runnable r = new Runnable() {
			@Override
			public void run() {
				if (shell.getBackground().equals(SWTResourceManager.getColor(SWT.COLOR_WHITE)))
					shell.setBackground(SWTResourceManager.getColor(SWT.COLOR_GRAY));
				else
					shell.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
				display.timerExec(400, this);
			}
		};

		display.timerExec(400, r);

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		display.timerExec(-1, r);

		return result;
	}

	/**
	 * Create contents of the dialog.
	 */
	@SuppressWarnings("unused")
	private void createContents() {
		shell = new Shell(shell, SWT.APPLICATION_MODAL | SWT.ON_TOP);
		shell.setSize(800, 215);
		shell.setLocation(shell.getLocation().x, shell.getLocation().y + 385);
		SelectionListener listener = new MyButtonSelectionListener();

		MyButton b1 = new MyButton(shell, "1", 4, 13, listener);
		MyButton b2 = new MyButton(shell, "2", 4, 14, listener);
		MyButton b3 = new MyButton(shell, "3", 4, 15, listener);
		MyButton b4 = new MyButton(shell, "4", 3, 13, listener);
		MyButton b5 = new MyButton(shell, "5", 3, 14, listener);
		MyButton b6 = new MyButton(shell, "6", 3, 15, listener);
		MyButton b7 = new MyButton(shell, "7", 2, 13, listener);
		MyButton b8 = new MyButton(shell, "8", 2, 14, listener);
		MyButton b9 = new MyButton(shell, "9", 2, 15, listener);
		MyButton b0 = new MyButton(shell, "0", 5, 13, listener);
		MyButton plus = new MyButton(shell, "+", 5, 1, listener);
		MyButton minus = new MyButton(shell, "-", 5, 2, listener);
		MyButton bs = new MyButton(shell, "\u2b05", 2, 11, listener);

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
		MyButton a = new MyButton(shell, "A", 3, 1, listener);
		MyButton s = new MyButton(shell, "S", 3, 2, listener);
		MyButton d = new MyButton(shell, "D", 3, 3, listener);
		MyButton f = new MyButton(shell, "F", 3, 4, listener);
		MyButton g = new MyButton(shell, "G", 3, 5, listener);
		MyButton h = new MyButton(shell, "H", 3, 6, listener);
		MyButton j = new MyButton(shell, "J", 3, 7, listener);
		MyButton k = new MyButton(shell, "K", 3, 8, listener);
		MyButton l = new MyButton(shell, "L", 3, 9, listener);
		MyButton š = new MyButton(shell, "Š", 3, 10, listener);
		MyButton đ = new MyButton(shell, "Đ", 3, 11, listener);
		MyButton y = new MyButton(shell, "Y", 4, 1, listener);
		MyButton x = new MyButton(shell, "X", 4, 2, listener);
		MyButton c = new MyButton(shell, "C", 4, 3, listener);
		MyButton v = new MyButton(shell, "V", 4, 4, listener);
		MyButton b = new MyButton(shell, "B", 4, 5, listener);
		MyButton n = new MyButton(shell, "N", 4, 6, listener);
		MyButton m = new MyButton(shell, "M", 4, 7, listener);
		MyButton zarez = new MyButton(shell, ",", 4, 8, listener);
		MyButton č = new MyButton(shell, "Č", 4, 9, listener);
		MyButton ć = new MyButton(shell, "Ć", 4, 10, listener);
		MyButton ž = new MyButton(shell, "Ž", 4, 11, listener);
		MyButton tocka = new MyButton(shell, ".", 5, 14, listener);
		// MyButton minus = new MyButton(shell, "-", 4, 10, listener);
		// MyButton plus = new MyButton(shell, "+", 4, 11, listener);

		MyButton ok = new MyButton(shell, "\u21b5", 5, 11, listener);
		ok.setBackground(SWTResourceManager.getColor(SWT.COLOR_LIST_SELECTION));
		MyButton ok1 = new MyButton(shell, "\u21b5", 5, 15, listener);
		ok1.setBackground(SWTResourceManager.getColor(SWT.COLOR_LIST_SELECTION));

		MyButton space = new MyButton(shell, " ", 5, 3, listener);
		MyButton slash = new MyButton(shell, "/", 5, 10, listener);

	}

	private class MyButtonSelectionListener extends SelectionAdapter {
		@Override
		public void widgetSelected(SelectionEvent e) {
			widgetDefaultSelected(e);
		}

		@Override
		public void widgetDefaultSelected(SelectionEvent e) {
			Button b = (Button) e.widget;
			if (b.getText().equals("\u21b5")) {
				polje.clearSelection();
				shell.dispose();
			} else if (b.getText().equals("\u2b05")) {
				polje.setSelection(polje.getCharCount() - 1, polje.getCharCount());
				polje.cut();
			} else {
				polje.insert(b.getText());
				polje.clearSelection();
			}
		}

	}
}
