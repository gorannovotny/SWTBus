package hr.mit.windows;

import hr.mit.beans.Stupac;
import hr.mit.utils.DbUtil;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;
// josip
// josip

public class VRFinder {

	protected Object result;
	protected Shell shell;
	protected Shell parent;
	private Text tOdPostaje;
	private Text tDoPostaje;
	private Text tSati;
	private Text tMinute;
	private Button btnDummy;
	private Stupac stupac = null;
	private Label lblNewLabel;
	private Label lblSearchPostajaOd;
	private Label lblSearchPostajaDo;
	private Label lblTimeLabel;
	private Label lblTimeLabelO;
	private DateTime timeBox;

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
		lblNewLabel.setBounds(10, 10, 410, 44);
		lblNewLabel.setText("Pretraga voznih redova");

		lblTimeLabel = new Label(shell, SWT.NONE);
		lblTimeLabel.setFont(SWTResourceManager.getFont("Liberation Sans", 18, SWT.NORMAL));// 30
		lblTimeLabel.setBounds(520, 20, 110, 35);
		lblTimeLabel.setText("Polazak u");
		lblTimeLabel.setVisible(true);

		lblTimeLabelO = new Label(shell, SWT.NONE);
		lblTimeLabelO.setFont(SWTResourceManager.getFont("Liberation Sans", 18, SWT.NORMAL));// 30
		lblTimeLabelO.setBounds(640 + 50, 18, 8, 35);
		lblTimeLabelO.setText(":");
		lblTimeLabelO.setVisible(true);

		tOdPostaje.addMouseListener(new textMouseListener());
		tDoPostaje.addMouseListener(new textMouseListener());

		lblSearchPostajaOd = new Label(shell, SWT.NONE);
		lblSearchPostajaOd.setFont(SWTResourceManager.getFont("Liberation Sans", 16, SWT.NORMAL));// 30
		lblSearchPostajaOd.setBounds(10 + 35, 115, 290, 35);
		lblSearchPostajaOd.setText("??");

		lblSearchPostajaDo = new Label(shell, SWT.NONE);
		lblSearchPostajaDo.setFont(SWTResourceManager.getFont("Liberation Sans", 16, SWT.NORMAL));// 30
		lblSearchPostajaDo.setBounds(388 + 35, 115, 290, 35);
		lblSearchPostajaDo.setText("??");

		timeBox = new DateTime(shell, SWT.TIME | SWT.SHORT); //
		timeBox.setBounds(645, 10, 100, 44);
		timeBox.setFont(SWTResourceManager.getFont("Liberation Sans", 18, SWT.NORMAL));// 30
		timeBox.setVisible(false);
		timeBox.addMouseListener(new timeMouseListener());

		tSati = new Text(shell, SWT.BORDER);
		tSati.setBounds(640, 10, 50, 40);
		tSati.setFont(SWTResourceManager.getFont("Liberation Sans", 25, SWT.NORMAL));
		tSati.setVisible(true);
		tSati.setText(String.format("%02d", timeBox.getHours()));
		tSati.addMouseListener(new textMouseListener());
		tSati.addVerifyListener(new VerifyListener() {
			public void verifyText(VerifyEvent event) {
				event.doit = true;
				String inTimeStr = "";
				inTimeStr = tSati.getText() + event.text;

				if (DbUtil.checkIfNumber(inTimeStr)) {
					int num = Integer.parseInt(inTimeStr);
					event.doit = (num < 24);
				} else
					event.doit = false;
			}
		});

		tMinute = new Text(shell, SWT.BORDER);
		tMinute.setBounds(700, 10, 50, 40);
		tMinute.setFont(SWTResourceManager.getFont("Liberation Sans", 25, SWT.NORMAL));
		tMinute.setVisible(true);
		tMinute.setText(String.format("%02d", timeBox.getMinutes()));
		tMinute.addMouseListener(new textMouseListener());
		tMinute.addVerifyListener(new VerifyListener() {
			public void verifyText(VerifyEvent event) {
				event.doit = true;
				String inTimeStr = "";
				inTimeStr = tMinute.getText() + event.text;

				if (DbUtil.checkIfNumber(inTimeStr)) {
					int num = Integer.parseInt(inTimeStr);
					event.doit = (num < 60);
				} else
					event.doit = false;
			}
		});

		tOdPostaje.addVerifyListener(new VerifyListener() {
			public void verifyText(VerifyEvent event) {
				String outs = "??";
				String inputs = "";
				char myChar = event.character;
				inputs = tOdPostaje.getText() + event.text;

				if (myChar == '\b') {
					inputs = inputs.substring(0, inputs.length() - 1);
				}
				if (inputs.length() > 1)
					outs = Stupac.OpisPostajeFinder(inputs);
				lblSearchPostajaOd.setText(outs);

			}
		});

		tDoPostaje.addVerifyListener(new VerifyListener() {
			public void verifyText(VerifyEvent event) {
				String outs = "??";
				String inputs = "";
				char myChar = event.character;
				inputs = tDoPostaje.getText() + event.text;

				// pobrisemo
				if (myChar == '\b') {
					inputs = inputs.substring(0, inputs.length() - 1);
				}
				if (inputs.length() > 1)
					outs = Stupac.OpisPostajeFinder(inputs);
				lblSearchPostajaDo.setText(outs);
			}// public
		});

	} // create context

	private class BtnDummySelectionListener extends SelectionAdapter {
		public void widgetDefaultSelected(SelectionEvent e) {
			if (lblSearchPostajaDo.getText().equals("??") || lblSearchPostajaOd.getText().equals("??") || lblSearchPostajaDo.getText().equals("") || lblSearchPostajaOd.getText().equals("")) {
				MessageBox mb1 = new MessageBox(shell, SWT.OK | SWT.ICON_ERROR);
				mb1.setMessage("Nisu upisane postaje !");
				mb1.open();
				stupac = null;
				shell.dispose();
				return;
			}

			float Sati = 0;
			String inSatiStr = tSati.getText();
			if (DbUtil.checkIfNumber(inSatiStr))
				Sati = Integer.parseInt(inSatiStr);
			float Minute = 0;
			String inMinuteStr = tMinute.getText();
			if (DbUtil.checkIfNumber(inMinuteStr))
				Minute = Integer.parseInt(inMinuteStr);

			float Polazak = (float) Sati / 24 + Minute / 24 / 60;

			Stupac.setupFinder(tOdPostaje, tDoPostaje, Polazak);
			if (Stupac.getList().length == 0) {
				MessageBox mb = new MessageBox(shell, SWT.OK | SWT.ICON_ERROR);
				mb.setMessage("Nema rezultata pretraživanja !");
				mb.open();
				stupac = null;
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

	protected class timeMouseListener extends MouseAdapter {
		@Override
		public void mouseDown(MouseEvent e) {
			new VirtualKeyboard(shell);
		}
	}

}
