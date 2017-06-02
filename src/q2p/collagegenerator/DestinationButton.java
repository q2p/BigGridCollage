package q2p.collagegenerator;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.JButton;
import javax.swing.JFileChooser;

@SuppressWarnings("serial")
final class DestinationButton extends JButton implements ActionListener {
	DestinationButton() {
		super("Destination");
		setBounds(Frame.OFF*3+Frame.W3*2, Frame.OFF*2+Frame.HE, Frame.W3, Frame.HE);
		addActionListener(this);
	}
	
	public final void actionPerformed(final ActionEvent ae) {
		setEnabled(false);
		final JFileChooser fChooser = new JFileChooser();
		fChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		fChooser.setMultiSelectionEnabled(false);
		final File dir = new File(CollageMain.collDef);
		if(dir.exists() && dir.isDirectory()) fChooser.setCurrentDirectory(dir);
		if (fChooser.showOpenDialog(CollageMain.frame) == JFileChooser.APPROVE_OPTION) CollageMain.collFldr = fChooser.getSelectedFile().getAbsolutePath();
		if(CollageMain.collFldr != null && !CollageMain.collFldr.endsWith("/")) CollageMain.collFldr += "/";
		setEnabled(true);
	}
}
