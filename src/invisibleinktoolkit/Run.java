package invisibleinktoolkit;

import java.util.Iterator;
import javax.swing.JOptionPane;
import javax.imageio.ImageIO;
import javax.swing.UIManager;
import invisibleinktoolkit.gui.GUI;

public class Run
{
    private static GUI mGUI;
    private static long m_initialJVMSize;
    
    public static void main(final String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch (Exception ex2) {}
        final Iterator bmpwriter = ImageIO.getImageWritersByFormatName("bmp");
        final Iterator pngwriter = ImageIO.getImageWritersByFormatName("png");
        if (!pngwriter.hasNext() && !bmpwriter.hasNext()) {
            JOptionPane.showMessageDialog(null, "This program requires Java 1.4 with the advanced imaging toolkit or Java 1.5 to run.\n  There appears to be no appropriate writers on your pc, suggesting you do not have the right version of Java.\n  The program will now exit.", "No LossLess Image Writers!", 0);
            System.exit(1);
        }
        else if (!pngwriter.hasNext()) {
            JOptionPane.showMessageDialog(null, "This program requires Java 1.4 with the advanced imaging toolkit or Java 1.5 to run.\n  It appears that you do not have the ability to write png files, with your Java installation.\n The program will continue but please use bitmaps (bmp) as the output format.", "No PNG Image Writers!", 0);
        }
        else if (!bmpwriter.hasNext()) {
            JOptionPane.showMessageDialog(null, "This program requires Java 1.4 with the advanced imaging toolkit or Java 1.5 to run.\n  It appears that you do not have the ability to write bmp files, with your Java installation.\n The program will continue but please use png as the output format.", "No Bitmap Image Writers!", 0);
        }
        try {
            (Run.mGUI = new GUI("Digital Invisible Ink Toolkit 1.5")).setVisible(true);
            final Thread memMonitor = new Thread() {
                public void run() {
                    while (true) {
                        try {
                            Thread.sleep(3000L);
                            System.gc();
                            if (Runtime.getRuntime().maxMemory() - Runtime.getRuntime().totalMemory() >= Run.m_initialJVMSize + 300000L) {
                                continue;
                            }
                            Run.mGUI.dispose();
                            System.gc();
                            Thread[] theGroup = new Thread[Thread.activeCount()];
                            Thread.enumerate(theGroup);
                            for (int i = 0; i < theGroup.length; ++i) {
                                final Thread t = theGroup[i];
                                if (t != null && t != Thread.currentThread()) {
                                    if (t.getName().startsWith("Thread")) {
                                        t.interrupt();
                                    }
                                    else if (t.getName().startsWith("AWT-EventQueue")) {
                                        t.interrupt();
                                    }
                                }
                            }
                            theGroup = null;
                            JOptionPane.showMessageDialog(null, "Not enough memory. \nPlease load a smaller image or use larger heap size by setting the -Xmx flag on the JVM.\nThe program will now exit.", "OutOfMemory", 2);
                            System.err.println("Not enough memory. Please load a smaller image or use larger heap size.");
                            System.err.println("Now exiting...");
                            System.exit(-1);
                        }
                        catch (InterruptedException ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            };
            memMonitor.setPriority(5);
            memMonitor.start();
        }
        catch (Exception ex) {
            ex.printStackTrace();
            System.err.println(ex.getMessage());
        }
    }
}
