package q2p.collagegenerator;
import java.awt.Dimension;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

@SuppressWarnings("serial")
final class Frame extends JFrame implements WindowListener {
	static final short OFF = 8;
	static final short W3 = 128;
	static final short HE = 24;
	static JTextField colWidthField;
	static JTextField colAmtField;
	static ScanButton scanButton;
	static DestinationButton destinationButton;
	static BeginButton beginButton;
	static JProgressBar progressBar;

	Frame() {
		super("Collage Generator");
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		setResizable(false);
		getContentPane().setLayout(null);
		getContentPane().setPreferredSize(new Dimension(W3*3+OFF*4, OFF*5+HE*4));
		
		getContentPane().add(new JLabel("Column width")).setBounds(OFF, OFF, W3, HE);
		getContentPane().add(new JLabel("Amount of columns")).setBounds(OFF*2+W3, OFF, W3, HE);
		getContentPane().add(colWidthField = new JTextField()).setBounds(OFF, OFF*2+HE, W3, HE);
		getContentPane().add(colAmtField = new JTextField()).setBounds(OFF*2+W3, OFF*2+HE, W3, HE);
		getContentPane().add(scanButton = new ScanButton());
		getContentPane().add(destinationButton = new DestinationButton());
		getContentPane().add(beginButton = new BeginButton());
		getContentPane().add(progressBar = new JProgressBar()).setBounds(OFF, OFF*4+HE*3, W3*3+OFF*2, HE);

		addWindowListener(this);
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}

	final static void done() {
		CollageMain.collFldr = null;
		colAmtField.setEditable(true);
		colWidthField.setEditable(true);
		scanButton.setEnabled(true);
		destinationButton.setEnabled(true);
		beginButton.setText("Begin");
		beginButton.setEnabled(true);
		progressBar.setValue(0);
		JOptionPane.showMessageDialog(CollageMain.frame, "Collage is ready.", "Done", JOptionPane.INFORMATION_MESSAGE);
	}
	
	public final void windowClosing(final WindowEvent we) {
		if(!beginButton.isEnabled() && JOptionPane.showConfirmDialog(this, "Collage isnt generated yet. Do you want to close application?", "Exit", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE) == JOptionPane.NO_OPTION) return;
		System.exit(0);
	}

	public final void windowActivated(final WindowEvent we) {}
	public final void windowClosed(final WindowEvent we) {}
	public final void windowDeactivated(final WindowEvent we) {}
	public final void windowDeiconified(final WindowEvent we) {}
	public final void windowIconified(final WindowEvent we) {}
	public final void windowOpened(final WindowEvent we) {}
}
