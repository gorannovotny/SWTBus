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
		setBounds(10 + (kolona - 1) * VISINA, 10 + (red - 1) * SIRINA, VISINA, SIRINA);
		setFont(SWTResourceManager.getFont("Liberation Sans", 15, SWT.NORMAL));
		setText(tipka);
		addSelectionListener(listener);
	}
	

	protected void checkSubclass() {
	}
}
