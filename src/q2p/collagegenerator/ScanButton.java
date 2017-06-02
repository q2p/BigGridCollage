package q2p.collagegenerator;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.JButton;
import javax.swing.JFileChooser;

@SuppressWarnings("serial")
final class ScanButton extends JButton implements ActionListener {
	ScanButton() {
		super("Scan folder");
		setBounds(Frame.OFF*3+Frame.W3*2, Frame.OFF, Frame.W3, Frame.HE);
		addActionListener(this);
	}
	
	public final void actionPerformed(final ActionEvent ae) {
		setEnabled(false);
		final JFileChooser fChooser = new JFileChooser();
		fChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		fChooser.setMultiSelectionEnabled(false);
		final File dir = new File(CollageMain.mainFldrDef);
		if(dir.exists() && dir.isDirectory()) fChooser.setCurrentDirectory(dir);
		if(fChooser.showOpenDialog(CollageMain.frame) == JFileChooser.APPROVE_OPTION) CollageMain.mainFldr = fChooser.getSelectedFile().getAbsolutePath();
		if(CollageMain.mainFldr != null && !CollageMain.mainFldr.endsWith("/")) CollageMain.mainFldr += "/";
		setEnabled(true);
	}
}
