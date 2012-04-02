package hr.mit.windows;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.wb.swt.SWTResourceManager;

public class MyButton extends Button {
	private int VISINA = 50;
	private int SIRINA = 52;

	public MyButton(Composite parent, String tipka, Integer red, Integer kolona,SelectionListener listener) {
		super(parent, SWT.NONE);
		if (tipka.equals(" ")) {
			setBounds(5 + (kolona -1 ) * SIRINA, 5 + (red -2 ) * VISINA, (SIRINA)*7, VISINA);
		} else if (tipka.equals("\u2b05")||tipka.equals("\u21b5")) {
			setBounds(5 + (kolona - 1 ) * SIRINA, 5 + (red - 2 ) * VISINA, (SIRINA), VISINA);
		} else {
			setBounds(5 + (kolona - 1 ) * SIRINA, 5 + (red - 2) * VISINA, SIRINA, VISINA);
		}
		setFont(SWTResourceManager.getFont("Liberation Sans", 20, SWT.BOLD));
		setText(tipka);
		addSelectionListener(listener);
	}
	

	@Override
	protected void checkSubclass() {
	}
}
