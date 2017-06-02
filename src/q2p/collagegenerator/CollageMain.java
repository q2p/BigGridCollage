package q2p.collagegenerator;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Random;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

final class CollageMain {
	static final String mainFldrDef = "E:/@MyFolder/p/";
	static final String collDef = mainFldrDef + "collage/";
	static String mainFldr = null;
	static String collFldr = null;
	private static final Random random = new Random();
	private static final ArrayList<String> names = new ArrayList<>();
	static int colsSize = 1;
	static int cols = 1;
	private static int widthLimit;
	private static byte maxScale = 8;
	private static int total;
	private static int[] oy;
	private static int ii = 0;
	private static int nm = 0;
	static Object lock = new Object();
	static Frame frame;

	public static final void main(final String args[]) {
		frame = new Frame();
	}

	static final void begin() {
		getAllFiles();
		doHardWork();
	}
	
	private static final void doHardWork() {
		Frame.progressBar.setMaximum(total = names.size());
		widthLimit = (short)(colsSize*cols);
		oy = new int[cols];

		BufferedImage oimg = new BufferedImage(widthLimit, 2*maxScale*widthLimit, BufferedImage.TYPE_INT_RGB);
		BufferedImage bimg;
		while(!names.isEmpty()) {
			if(ii % 50 == 0) {
				Frame.progressBar.setValue(ii);
				Frame.beginButton.setText("Images processed: " + ii + "/" + total);
			}
			final String name = names.remove(random.nextInt(names.size()));
			try {
				bimg = compressImg(name);
			} catch (final Exception e) {
				continue;
			}
			if(bimg != null) {
				// do
				int lw = 0;
				for(int i = 1; i < cols; i++) if(oy[i] < oy[lw]) lw = i;
				oimg.getGraphics().drawImage(bimg, lw*colsSize, oy[lw], (lw+1)*colsSize, oy[lw]+bimg.getHeight(), 0, 0, bimg.getWidth(), bimg.getHeight(), null);
				oy[lw]+=bimg.getHeight();
				ii++;

				// shift
				boolean hasLower = false;
				for(lw = 0; lw < cols; lw++) if(oy[lw] < widthLimit) {hasLower = true; break;}
				if(!hasLower) {
					BufferedImage timg = new BufferedImage(widthLimit, widthLimit, BufferedImage.TYPE_INT_RGB);
					timg.getGraphics().drawImage(oimg, 0, 0, widthLimit, widthLimit, 0, 0, widthLimit, widthLimit, null);
					try {
						ImageIO.write(timg, "png", new File(collFldr + nm++ +".png"));
					} catch (final Exception e) {
						System.exit(1);
					}
					timg = new BufferedImage(widthLimit, oimg.getHeight(), BufferedImage.TYPE_INT_RGB);
					timg.getGraphics().drawImage(oimg, 0, 0, widthLimit, oimg.getHeight()-widthLimit, 0, widthLimit, widthLimit, oimg.getHeight(), null);
					oimg = timg;
					for(short i = 0; i < cols; i++) oy[i] -= widthLimit;
				}
			}
		}
		while(true) {
			boolean hasOverFlow = false;
			int lw;
			for(lw = 0; lw < cols; lw++) if(oy[lw] > 0) {hasOverFlow = true; break;}
			if(hasOverFlow) {
				BufferedImage timg = new BufferedImage(widthLimit, widthLimit, BufferedImage.TYPE_INT_RGB);
				timg.getGraphics().drawImage(oimg, 0, 0, widthLimit, widthLimit, 0, 0, widthLimit, widthLimit, null);
				try {
					ImageIO.write(timg, "png", new File(collFldr + nm++ +".png"));
				} catch (final Exception e) {
					JOptionPane.showMessageDialog(frame, "Can't write to file. Closing application.", "Error", JOptionPane.ERROR_MESSAGE);
					System.exit(1);
				}
				timg = new BufferedImage(widthLimit, oimg.getHeight(), BufferedImage.TYPE_INT_RGB);
				timg.getGraphics().drawImage(oimg, 0, 0, widthLimit, oimg.getHeight()-widthLimit, 0, widthLimit, widthLimit, oimg.getHeight(), null);
				oimg = timg;
				for(short i = 0; i < cols; i++) oy[i] -= widthLimit;
			} else
				break;
		}
		createHtml();
		Frame.done();
	}
	
	private static final void createHtml() {
		try {
			final File f = new File(collFldr + "index.html");
			if(!f.exists()) f.createNewFile();
			if(!f.isFile()) throw new Exception();
			final FileOutputStream fos = new FileOutputStream(f);
			fos.write(("<!DOCTYPE html><html><head><meta charset=utf-8><title>"+(ii-1)+" images collage</title><style>*{margin:0;padding:0}body{display:block;background-color:#000;margin-left:auto;margin-right:auto}img{display:block}</style><script>var widthLimit;var imageLimit;var offsetterDiv;var imgsDiv;var bodyDiv;var offic1;var offic2;var offi1;var offi2;function init(){widthLimit=" + widthLimit + ";imageLimit=" + (nm-1) + ";offsetterDiv=document.getElementById(\"offsetter\");imgsDiv=document.getElementById(\"imgs\");bodyDiv=document.getElementsByTagName(\"body\")[0];bodyDiv.style.width=widthLimit+\"px\";bodyDiv.style.height=(widthLimit*imageLimit)+\"px\";scroll()}function scroll(){var e=window.pageYOffset;if(offic1==undefined||Math.floor(e/widthLimit)!=offic1){var c=offi2-offi1;offic1=Math.floor(e/widthLimit);offic2=Math.floor((e+document.documentElement.clientHeight)/widthLimit);offi1=Math.max(0,offic1-1);offi2=Math.min(imageLimit,offic2+2);if((offi2-offi1)!=c){var b=\"\";for(var a=offi1;a<=offi2;a++){b+=\'<img width=\"\'+widthLimit+\'\">\'}imgsDiv.innerHTML=b}offsetterDiv.style.height=(offi1*widthLimit)+\"px\";var d=imgsDiv.children;for(var a=0;a<d.length;a++){d[a].alt=(offi1+a);d[a].src=(offi1+a)+\".png\"}}};</script></head><body onload=init() onscroll=scroll()><div id=offsetter></div><div id=imgs></div></body></html>").getBytes(StandardCharsets.UTF_8));
			fos.flush();
			fos.close();
		} catch (final Exception e) {
			JOptionPane.showMessageDialog(frame, "Can't create html document.", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	private static final BufferedImage compressImg(final String name) throws Exception {
		final BufferedImage simg = ImageIO.read(new File(name));
		final int nSizeY = (int)((float)(colsSize*simg.getHeight())/(float)simg.getWidth());
		if(nSizeY * maxScale < colsSize || nSizeY > maxScale * colsSize) return null;
		final BufferedImage timg = new BufferedImage(colsSize, nSizeY, BufferedImage.TYPE_INT_RGB);
		timg.getGraphics().drawImage(simg, 0, 0, colsSize, nSizeY, 0, 0, simg.getWidth(), simg.getHeight(), null);
		return timg;
	}

	private static final void getAllFiles() {
		File old = new File(collFldr);
		if(!old.exists()) old.mkdir();
		if(old.isDirectory()) {
			final String[] subs = old.list();
			for(final String d : subs) if(d.endsWith(".png") && d.length()-".png".length() > 0) {
				try {
					Integer.parseInt(d.substring(0, d.length() - ".png".length()));
				} catch(final NumberFormatException e) {continue;}
				old = new File(collFldr+d);
				old.delete();
			}
			old = new File(collFldr + "index.html");
			if(old.exists()) old.delete();
		}
		getListInDir(mainFldr);
		if(new File(mainFldr+"all/").exists() && new File(mainFldr+"nws/").exists()) {
			getListInDir(mainFldr+"all/");
			final File nwsMain = new File(mainFldr+"nws/");
			if(nwsMain.exists() && nwsMain.isDirectory()) {
				final String[] subs = nwsMain.list();
				for(final String d : subs) if(d.startsWith("nw") && d.length() > "nw".length()) try {
					Integer.parseInt(d.substring("nw".length()));
					getListInDir(mainFldr+"nws/"+d+"/");
				} catch(final NumberFormatException e) {}
			}
		}
	}

	private static final void getListInDir(final String directory) {
		final File dir = new File(directory);
		if(!dir.exists() || !dir.isDirectory()) return;
		final String[] files = dir.list();
		for(final String f : files) if(f.endsWith(".png")||f.endsWith(".jpg")||f.endsWith(".jpeg")||f.endsWith(".gif")) names.add(directory+f);
	}
}