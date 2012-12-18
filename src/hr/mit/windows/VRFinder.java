package hr.mit.windows;

import java.text.SimpleDateFormat;
import java.util.Calendar;


import hr.mit.beans.Stupac;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.events.VerifyListener; // josip
import org.eclipse.swt.events.VerifyEvent; // josip

public class VRFinder {

	protected Object result;
	protected Shell shell;
	protected Shell parent;
	private Text tOdPostaje;
	private Text tDoPostaje;
	private Button btnDummy;
	private Stupac stupac = null;
	private Label lblNewLabel;
	private Label lblSearchPostajaOd;
	private Label lblSearchPostajaDo;
	private Label lblTimeLabel;
	private DateTime timeBox;

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
		lblNewLabel.setBounds(10, 10, 410, 44);
		lblNewLabel.setText("Pretraga voznih redova");

		lblTimeLabel = new Label(shell, SWT.NONE);
		lblTimeLabel.setFont(SWTResourceManager.getFont("Liberation Sans", 18, SWT.NORMAL));// 30
		lblTimeLabel.setBounds(550, 20, 90, 35);
		lblTimeLabel.setText("Polazak:");
		lblTimeLabel.setVisible(true);
		
		tOdPostaje.addMouseListener(new textMouseListener());
		tDoPostaje.addMouseListener(new textMouseListener());

		lblSearchPostajaOd = new Label(shell, SWT.NONE);
		lblSearchPostajaOd.setFont(SWTResourceManager.getFont("Liberation Sans", 16, SWT.NORMAL));// 30
		lblSearchPostajaOd.setBounds(10+35, 115, 290, 35);
		lblSearchPostajaOd.setText("??");

		lblSearchPostajaDo = new Label(shell, SWT.NONE);
		lblSearchPostajaDo.setFont(SWTResourceManager.getFont("Liberation Sans", 16, SWT.NORMAL));// 30
		lblSearchPostajaDo.setBounds(388+35, 115, 290, 35);
		lblSearchPostajaDo.setText("??");

		timeBox = new DateTime (shell, SWT.TIME | SWT.SHORT ); //
		timeBox.setBounds(650, 10, 100, 44);
		timeBox.setFont(SWTResourceManager.getFont("Liberation Sans", 18, SWT.NORMAL));// 30
		timeBox.setVisible(true);
		timeBox.addMouseListener(new timeMouseListener());
		
		tOdPostaje.addVerifyListener(new VerifyListener() {
		      public void verifyText(VerifyEvent event) {
		    /*	  
		        // Assume we don't allow it
		        event.doit = false;

		        // Get the character typed
		        char myChar = event.character;
		        String text = ((Text) event.widget).getText();

		        // Allow '-' if first character
		        if (myChar == '-' && text.length() == 0)
		          event.doit = true;

		        // Allow 0-9
		        if (Character.isDigit(myChar))
		          event.doit = true;

		        // Allow backspace
		        if (myChar == '\b')
		          event.doit = true;
		          
		          --event.text;
		      */  
				     String outs   = "??";
				     String	inputs = "";
				     char myChar = event.character;
				     inputs = tOdPostaje.getText()+event.text;

				     // pobrisemo 
			    	 if (myChar == '\b') {
			    		 inputs = inputs.substring(0,inputs.length()-1); 
				    }
			    	if (inputs.length() > 1) 
				     	 outs = Stupac.OpisPostajeFinder(inputs);
				    lblSearchPostajaOd.setText(outs);
		        
		      }//public
		    });

		tDoPostaje.addVerifyListener(new VerifyListener() {
		      public void verifyText(VerifyEvent event) {
				     String outs   = "??";
				     String	inputs = "";
				     char myChar = event.character;
				     inputs = tDoPostaje.getText()+event.text;

				     // pobrisemo 
			    	 if (myChar == '\b') {
			    		 inputs = inputs.substring(0,inputs.length()-1); 
				    }
			    	if (inputs.length() > 1) 
				     	 outs = Stupac.OpisPostajeFinder(inputs);
		    	 lblSearchPostajaDo.setText(outs);
		      }//public
		    });
		
		} // create context

	
	
	
		private class BtnDummySelectionListener extends SelectionAdapter {
		public void widgetDefaultSelected(SelectionEvent e) {
          if (lblSearchPostajaDo.getText().equals("??") || lblSearchPostajaOd.getText().equals("??") ||
              lblSearchPostajaDo.getText().equals("")   || lblSearchPostajaOd.getText().equals("")){       
			MessageBox mb1 = new MessageBox(shell, SWT.OK | SWT.ICON_ERROR);
			mb1.setMessage("Nisu upisane postaje !");
			mb1.open();
			stupac = null;
			shell.dispose();
			return;
          }
			float  Ure     = timeBox.getHours();
			float  Minute  = timeBox.getMinutes(); 
            float  Polazak = (float) Ure/24+Minute/24/60;
            
			Stupac.setupFinder(tOdPostaje, tDoPostaje,Polazak);
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
				DateTime d = (DateTime) e.widget;
				VirtualKeyboard keypad = new VirtualKeyboard(shell);
				//d.selectAll(); // predelati keyboard, dela samo s text
				//keypad.open(d);

			}
		}
		
	
		   	
	
}
