package q2p.collagegenerator;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JOptionPane;

@SuppressWarnings("serial")
final class BeginButton extends JButton implements ActionListener {
	BeginButton() {
		super("Begin");
		setBounds(Frame.OFF, Frame.OFF*3+Frame.HE*2, Frame.W3*3+Frame.OFF*2, Frame.HE);
		addActionListener(this);
	}
	
	public final void actionPerformed(final ActionEvent ae) {
		setEnabled(false);

		String msg = "";
		if(CollageMain.collFldr == null) msg += "Choose a destination folder.\n";
		if(CollageMain.mainFldr == null) msg += "Choose a scan folder.\n";
		try {CollageMain.cols = Integer.parseInt(Frame.colAmtField.getText().trim());}
		catch(final NumberFormatException nfe) {CollageMain.cols = 1; msg += "Amount of columns must be a number.\n";}
		if(CollageMain.cols <= 0) msg += "Amount of columns must be greater then zero.\n";
		if(CollageMain.cols > 512) msg += "Amount of columns must not be greater then 512.\n";
		try {CollageMain.colsSize = Integer.parseInt(Frame.colWidthField.getText().trim());}
		catch(final NumberFormatException nfe) {CollageMain.colsSize = 1; msg += "Width of columns must be a number.\n";}
		if(CollageMain.colsSize <= 0) msg += "Width of columns must be greater then zero.\n";
		if(CollageMain.colsSize > 2*1024) msg += "Width of columns must not be greater then "+2*1024+".\n";
		if(!msg.equals("")) {
			JOptionPane.showMessageDialog(CollageMain.frame, msg.trim(), "Error", JOptionPane.ERROR_MESSAGE);
			setEnabled(true);
			return;
		}

		Frame.colAmtField.setEditable(false);
		Frame.colWidthField.setEditable(false);
		Frame.scanButton.setEnabled(false);
		Frame.destinationButton.setEnabled(false);
		Frame.progressBar.setStringPainted(true);

		final Thread t = new Thread(new OffStream());
		t.start();
	}
}
