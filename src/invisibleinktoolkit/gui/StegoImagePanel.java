// 
// Decompiled by Procyon v0.5.36
// 

package invisibleinktoolkit.gui;

import javax.swing.filechooser.FileFilter;
import invisibleinktoolkit.util.AFileFilter;
import javax.swing.JFileChooser;
import java.awt.event.ActionEvent;
import java.awt.Component;
import java.awt.Dimension;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import java.awt.LayoutManager;
import java.awt.Container;
import javax.swing.BoxLayout;
import java.io.File;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import javax.swing.JPanel;

public class StegoImagePanel extends JPanel implements ActionListener
{
    private JButton mButton;
    private JTextField mPath;
    private File mStegoFile;
    private static final long serialVersionUID = 0L;
    
    public StegoImagePanel() {
        this("Set the stego image to write to", "Set Image");
    }
    
    public StegoImagePanel(final String heading, final String buttontext) {
        this.setLayout(new BoxLayout(this, 0));
        this.setBorder(new TitledBorder(heading));
        this.setPreferredSize(new Dimension(740, 50));
        (this.mButton = new JButton(buttontext)).addActionListener(this);
        this.mButton.setPreferredSize(new Dimension(130, 26));
        this.mButton.setToolTipText(heading);
        this.add(this.mButton);
        final JPanel fillerpanel = new JPanel();
        fillerpanel.setPreferredSize(new Dimension(20, 1));
        this.add(fillerpanel);
        (this.mPath = new JTextField(50)).setEditable(false);
        this.mPath.setPreferredSize(new Dimension(540, 26));
        this.mPath.setToolTipText("The file currently selected for the image");
        this.add(this.mPath);
    }
    
    public void actionPerformed(final ActionEvent e) {
        final File s = this.getOutputStegoFile();
        if (s != null) {
            this.mPath.setText(s.getPath());
            this.mStegoFile = s;
        }
    }
    
    private File getOutputStegoFile() {
        final JFileChooser jfc = new JFileChooser();
        jfc.setAcceptAllFileFilterUsed(false);
        jfc.setDialogTitle("Pick a stego image file");
        final AFileFilter pngfilter = new AFileFilter(".png", "PNG images (.png)");
        final AFileFilter bmpfilter = new AFileFilter(".bmp", "Bitmap images (.bmp)");
        jfc.addChoosableFileFilter(bmpfilter);
        jfc.addChoosableFileFilter(pngfilter);
        if (this.mStegoFile != null && this.mStegoFile.exists()) {
            jfc.setSelectedFile(this.mStegoFile);
            final String aformat = this.getFormat();
            if (aformat.equalsIgnoreCase("png")) {
                jfc.setFileFilter(pngfilter);
            }
            else {
                jfc.setFileFilter(bmpfilter);
            }
        }
        final int returnval = jfc.showSaveDialog(null);
        if (returnval == 0) {
            final AFileFilter filter = (AFileFilter)jfc.getFileFilter();
            String path;
            if (!jfc.getSelectedFile().getPath().endsWith(".png") && filter.getExtension() == ".png") {
                path = String.valueOf(jfc.getSelectedFile().getPath()) + ".png";
            }
            else if (!jfc.getSelectedFile().getPath().endsWith(".bmp") && filter.getExtension() == ".bmp") {
                path = String.valueOf(jfc.getSelectedFile().getPath()) + ".bmp";
            }
            else {
                path = jfc.getSelectedFile().getPath();
            }
            return new File(path);
        }
        return null;
    }
    
    public File getOutputFile() {
        return this.mStegoFile;
    }
    
    public String getFormat() {
        String filepath = this.mStegoFile.getPath();
        filepath = filepath.substring(filepath.lastIndexOf(".") + 1, filepath.length());
        return filepath;
    }
}
