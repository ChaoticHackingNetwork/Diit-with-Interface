// 
// Decompiled by Procyon v0.5.36
// 

package invisibleinktoolkit.gui;

import java.io.File;
import javax.swing.filechooser.FileFilter;
import invisibleinktoolkit.util.InputImagesFileFilter;
import javax.swing.JFileChooser;
import java.awt.event.ActionEvent;
import java.awt.Component;
import java.awt.Dimension;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import java.awt.LayoutManager;
import java.awt.Container;
import javax.swing.BoxLayout;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import javax.swing.JPanel;

public class InputImagePanel extends JPanel implements ActionListener
{
    private JButton mButton;
    private JTextField mTPath;
    private String mPath;
    private Embedder mEmbedder;
    private ViewButton mViewButton;
    private static final long serialVersionUID = 0L;
    
    public InputImagePanel(final String title, final String buttontext, final boolean allowviewbutton) {
        this(title, buttontext, allowviewbutton, null);
    }
    
    public InputImagePanel(final String title, final String buttontext, final boolean allowviewbutton, final Embedder embed) {
        this.setLayout(new BoxLayout(this, 0));
        this.setBorder(new TitledBorder(title));
        this.setPreferredSize(new Dimension(740, 50));
        (this.mButton = new JButton(buttontext)).setToolTipText(title);
        this.mButton.setPreferredSize(new Dimension(130, 26));
        this.mButton.addActionListener(this);
        this.add(this.mButton);
        final JPanel fillerpanel = new JPanel();
        fillerpanel.setPreferredSize(new Dimension(20, 1));
        this.add(fillerpanel);
        (this.mTPath = new JTextField()).setToolTipText("The file currently selected");
        this.mTPath.setEditable(false);
        this.add(this.mTPath);
        this.mViewButton = new ViewButton("View");
        if (allowviewbutton) {
            this.add(this.mViewButton);
        }
        this.mPath = "";
        this.mEmbedder = embed;
    }
    
    public void actionPerformed(final ActionEvent e) {
        String s = this.getInputFile();
        if (s != null) {
            this.mPath = s;
            s = s.substring(s.lastIndexOf(System.getProperty("file.separator")) + 1, s.length());
            this.mTPath.setText(s);
            this.mViewButton.setImage(this.mPath);
            if (this.mEmbedder != null) {
                this.mEmbedder.updateEmbeddingRate();
            }
        }
    }
    
    private String getInputFile() {
        final JFileChooser jfc = new JFileChooser();
        jfc.setDialogTitle("Pick an image");
        jfc.setAcceptAllFileFilterUsed(true);
        jfc.addChoosableFileFilter(new InputImagesFileFilter());
        if (this.mPath != null && !this.mPath.equals("")) {
            final File afile = new File(this.getPath());
            if (afile.exists()) {
                jfc.setSelectedFile(afile);
            }
        }
        final int returnval = jfc.showOpenDialog(null);
        if (returnval == 0) {
            return jfc.getSelectedFile().getPath();
        }
        return null;
    }
    
    public String getPath() {
        return this.mPath;
    }
}
