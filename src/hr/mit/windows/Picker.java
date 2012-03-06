package hr.mit.windows;

import hr.mit.beans.Postaja;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.SWT;
import org.eclipse.wb.swt.SWTResourceManager;

public class Picker extends Dialog {

	protected Shell shell;

	private Button upButton;
	private Button downButton;
	private ArrayList<Button> buttonList = new ArrayList<Button>();
	private List<String> items = Postaja.getArrayList();
	private Integer pos = 0;

	private int MAX_PICKS = 5;

	/**
	 * Create the dialog.
	 * 
	 * @param parent
	 * @param style
	 */
	public Picker(Control c, Integer position) {
		super(c.getShell(), SWT.NONE);
		shell = new Shell(getParent(), getStyle());
		
		this.pos = position;
		if (pos > items.size() - MAX_PICKS) pos = items.size() - MAX_PICKS;
		upButton = new Button(shell,SWT.NONE);
		upButton.setFont(SWTResourceManager.getFont("Liberation Sans", 25, SWT.NORMAL));
		upButton.setLocation(0,0);
		upButton.setSize(c.getSize());
		upButton.setAlignment(SWT.CENTER);
		upButton.setText("\u25B2");
		upButton.addSelectionListener(new ButtonUpListener());
		
		for (int i = 0; i < MAX_PICKS; i++) {
			Button b = new Button(shell, SWT.NONE);
			b.setFont(SWTResourceManager.getFont("Liberation Sans", 25, SWT.NORMAL));
			b.setLocation(0,(i+1) * c.getSize().y);
			b.setSize(c.getSize());
			b.setAlignment(SWT.LEFT);
			b.setText(items.get(pos));
			b.setData(pos);
			b.addSelectionListener(new ButtonSelectListener());
			buttonList.add(b);
			pos++;
			if (pos >= items.size()) break;
		}
		downButton = new Button(shell,SWT.NONE);
		downButton.addSelectionListener(new ButtonDownListener());
		downButton.setFont(SWTResourceManager.getFont("Liberation Sans", 25, SWT.NORMAL));
		downButton.setLocation(0,(MAX_PICKS+1) * c.getSize().y);
		downButton.setSize(c.getSize());
		downButton.setAlignment(SWT.CENTER);
		downButton.setText("\u25BC");
		if (pos - MAX_PICKS <= 0) upButton.setEnabled(false);
		if (pos >= items.size()) downButton.setEnabled(false);

		shell.setLocation(c.toDisplay(0, 0));
		shell.setSize(c.getSize().x, (MAX_PICKS+2) * c.getSize().y);
	}

	/**
	 * Open the dialog.
	 * 
	 * @return the result
	 */
	public int open() {
		shell.open();
		shell.layout();

		Display display = getParent().getDisplay();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		return pos;
	}
	
	private class ButtonDownListener extends SelectionAdapter {
		public void widgetSelected(SelectionEvent e) {
			for (int i = 0; i < (MAX_PICKS-1); i++) {
				buttonList.get(i).setText(buttonList.get(i+1).getText());
				buttonList.get(i).setData(buttonList.get(i+1).getData());
			
			}
			buttonList.get(MAX_PICKS-1).setText(Postaja.get(pos).getNaziv()+ " " + Postaja.get(pos).getVremeOdhoda());
			buttonList.get(MAX_PICKS-1).setData(pos);
			pos++;
			if (pos > 0 ) upButton.setEnabled(true);
			if (pos >= items.size()) downButton.setEnabled(false);
		}
	}

	private class ButtonUpListener extends SelectionAdapter {
		public void widgetSelected(SelectionEvent e) {
			for (int i = (MAX_PICKS -1); i > 0 ; i--) {
				buttonList.get(i).setText(buttonList.get(i-1).getText());
				buttonList.get(i).setData(buttonList.get(i-1).getData());
			
			}
			pos--;
			buttonList.get(0).setText(Postaja.get(pos-MAX_PICKS).getNaziv()+ " " + Postaja.get(pos-MAX_PICKS).getVremeOdhoda());
			buttonList.get(0).setData(pos-MAX_PICKS);
			if (pos -MAX_PICKS <=0 ) upButton.setEnabled(false);
			if (pos < items.size()) downButton.setEnabled(true);
		}
	}

	private class ButtonSelectListener extends SelectionAdapter {
		public void widgetSelected(SelectionEvent e) {
			pos = (Integer) ((Button) e.widget).getData();
			shell.dispose();
		}
	}

	
}
