package hr.mit.windows;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.wb.swt.SWTResourceManager;

public class MyButton extends Button {
	private int VISINA = 50;
	private int SIRINA = 50;

	public MyButton(Composite parent, String tipka, Integer red, Integer kolona,SelectionListener listener) {
		super(parent, SWT.NONE);
		if (tipka.equals(" ")) {
			setBounds(10 + (kolona - 1) * SIRINA, 10 + (red - 1) * VISINA, (SIRINA-2)*10, VISINA-2);
		} else if (tipka.equals("BS")||tipka.equals("OK")) {
			setBounds(10 + (kolona - 1) * SIRINA, 10 + (red - 1) * VISINA, (SIRINA-2)*2, VISINA-2);
		} else {
			setBounds(10 + (kolona - 1) * SIRINA, 10 + (red - 1) * VISINA, SIRINA-2, VISINA-2);
		}
		setFont(SWTResourceManager.getFont("Liberation Sans", 15, SWT.NORMAL));
		setText(tipka);
		addSelectionListener(listener);
	}
	

	protected void checkSubclass() {
	}
}
