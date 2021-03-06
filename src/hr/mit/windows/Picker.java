package hr.mit.windows;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.wb.swt.SWTResourceManager;

public class Picker extends Dialog {

	protected Shell shell;

	Button upButton, pgUpButton;
	Button downButton, pgDownButton;
	ArrayList<Button> buttonList = new ArrayList<Button>();
	List<String> items = null;
	Integer pos = 0;
	int MAX_PICKS = 5;

	Button c;

	public List<String> getItems() {
		return items;
	}

	public void setItems(List<String> items) {
		this.items = items;
	}

	/**
	 * Create the dialog.
	 * 
	 * @param parent
	 * @param style
	 */
	public Picker(Button c, List<String> items, Integer position) {
		super(c.getShell(), SWT.NONE);
		// super(new Shell(), SWT.NONE);
		shell = new Shell(getParent(), SWT.APPLICATION_MODAL | SWT.ON_TOP);
		this.c = c;
		this.items = items;
		this.pos = position;

		if (items.size() < MAX_PICKS)
			MAX_PICKS = items.size();

		if (pos > items.size() - MAX_PICKS)
			pos = items.size() - MAX_PICKS;
		upButton = new Button(shell, SWT.NONE);
		upButton.setFont(c.getFont());
		upButton.setLocation(0, 0);
		// upButton.setSize(c.getSize());
		upButton.setSize(c.getBounds().width / 2, c.getBounds().height);
		upButton.setAlignment(SWT.CENTER);
		upButton.setText("\u25B2");
		upButton.addSelectionListener(new ButtonUpListener());
		upButton.setBackground(SWTResourceManager.getColor(255, 255, 255));

		pgUpButton = new Button(shell, SWT.NONE);
		pgUpButton.setFont(c.getFont());
		pgUpButton.setLocation(c.getBounds().width / 2, 0);
		// pgUpButton .setSize(c.getSize());
		pgUpButton.setSize(c.getBounds().width / 2, c.getBounds().height);
		pgUpButton.setAlignment(SWT.CENTER);
		pgUpButton.setText("\u25B2\u25B2");
		pgUpButton.addSelectionListener(new ButtonUpListener());
		pgUpButton.setBackground(SWTResourceManager.getColor(255, 255, 255));

		for (int i = 0; i < MAX_PICKS; i++) {
			Button b = new Button(shell, SWT.NONE);
			b.setFont(c.getFont());
			b.setLocation(0, (i + 1) * c.getSize().y);
			b.setSize(c.getSize());
			b.setAlignment(c.getAlignment());
			b.setText(items.get(pos));
			b.setData(pos);
			b.setBackground(SWTResourceManager.getColor(255, 255, 255));
			b.addSelectionListener(new ButtonSelectListener());
			buttonList.add(b);
			pos++;
			if (pos >= items.size())
				break;
		}
		downButton = new Button(shell, SWT.NONE);
		downButton.addSelectionListener(new ButtonDownListener());
		downButton.setFont(c.getFont());
		downButton.setLocation(0, (MAX_PICKS + 1) * c.getSize().y);
		downButton.setSize(c.getBounds().width / 2, c.getBounds().height);
		downButton.setAlignment(SWT.CENTER);
		downButton.setText("\u25BC");
		downButton.setBackground(SWTResourceManager.getColor(255, 255, 255));

		pgDownButton = new Button(shell, SWT.NONE);
		pgDownButton.addSelectionListener(new ButtonDownListener());
		pgDownButton.setFont(c.getFont());
		pgDownButton.setLocation(c.getBounds().width / 2, (MAX_PICKS + 1) * c.getSize().y);
		pgDownButton.setSize(c.getBounds().width / 2, c.getBounds().height);
		pgDownButton.setAlignment(SWT.CENTER);
		pgDownButton.setText("\u25BC\u25BC");
		pgDownButton.setBackground(SWTResourceManager.getColor(255, 255, 255));

		if (pos - MAX_PICKS <= 0) {
			upButton.setEnabled(false);
			pgUpButton.setEnabled(false);
		}
		if (pos >= items.size()) {
			downButton.setEnabled(false);
			pgDownButton.setEnabled(false);
		}
		shell.setLocation(c.toDisplay(0, -c.getSize().y));
		shell.setSize(c.getSize().x, (MAX_PICKS + 2) * c.getSize().y);
		int van = shell.getBounds().y + shell.getBounds().height;
		if (van > 600)
			shell.setLocation(shell.getLocation().x, shell.getLocation().y - (van - 600));
	}

	/**
	 * Open the dialog.
	 * 
	 * @return the result
	 */
	public Button open() {
		if (items.size() > 0) {
			shell.open();
			Display display = getParent().getDisplay();
			while (!shell.isDisposed()) {
				if (!display.readAndDispatch()) {
					display.sleep();
				}
			}
			c.setData(pos);
			c.setText(items.get(pos));
		}
		return c;
	}

	class ButtonDownListener extends SelectionAdapter {
		@Override
		public void widgetSelected(SelectionEvent e) {
			int times = 1;
			if (e.widget == pgDownButton)
				times = MAX_PICKS;
			for (int a = 0; a < times; a++) {
				if (pos >= items.size())
					break;
				for (int i = 0; i < (MAX_PICKS - 1); i++) {
					buttonList.get(i).setText(buttonList.get(i + 1).getText());
					buttonList.get(i).setData(buttonList.get(i + 1).getData());
				}
				buttonList.get(MAX_PICKS - 1).setText(items.get(pos));
				buttonList.get(MAX_PICKS - 1).setData(pos);
				pos++;
				if (pos > 0) {
					upButton.setEnabled(true);
					pgUpButton.setEnabled(true);
				}
				if (pos >= items.size()) {
					downButton.setEnabled(false);
					pgDownButton.setEnabled(false);
				}
			}
		}
	}

	class ButtonUpListener extends SelectionAdapter {
		@Override
		public void widgetSelected(SelectionEvent e) {
			int times = 1;
			if (e.widget == pgUpButton)
				times = MAX_PICKS;
			for (int a = 0; a < times; a++) {
				if (pos - MAX_PICKS <= 0) // josip 1 imamo def.
				{	
					break;
				}
				for (int i = (MAX_PICKS - 1); i > 0; i--) {
					buttonList.get(i).setText(buttonList.get(i - 1).getText());
					buttonList.get(i).setData(buttonList.get(i - 1).getData());
				}
				pos--;
				if (pos - MAX_PICKS >= 0){ // josip
				  buttonList.get(0).setText(items.get(pos - MAX_PICKS));
			  	  buttonList.get(0).setData(pos - MAX_PICKS);
				}
				if (pos - MAX_PICKS <= 0) {
					upButton.setEnabled(false);
					pgUpButton.setEnabled(false);
				}
				if (pos < items.size()) {
					downButton.setEnabled(true);
					pgDownButton.setEnabled(true);
				}
			}
		}
	}

	class ButtonSelectListener extends SelectionAdapter {
		@Override
		public void widgetSelected(SelectionEvent e) {
			pos = (Integer) ((Button) e.widget).getData();
			shell.dispose();
		}
	}

}
