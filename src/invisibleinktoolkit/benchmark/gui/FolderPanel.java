// 
// Decompiled by Procyon v0.5.36
// 

package invisibleinktoolkit.benchmark.gui;

import java.io.File;
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

public class FolderPanel extends JPanel implements ActionListener
{
    private JButton mButton;
    private JTextField mPath;
    private String mFilePath;
    private String mText;
    private boolean mShowOpen;
    private static final long serialVersionUID = 0L;
    
    public FolderPanel(final String text, final boolean showopen) {
        this.setLayout(new BoxLayout(this, 0));
        this.setBorder(new TitledBorder(text));
        this.setPreferredSize(new Dimension(740, 50));
        (this.mButton = new JButton("Set Folder")).addActionListener(this);
        this.mButton.setPreferredSize(new Dimension(130, 26));
        this.mButton.setToolTipText(text);
        this.add(this.mButton);
        final JPanel fillerpanel = new JPanel();
        fillerpanel.setPreferredSize(new Dimension(20, 1));
        this.add(fillerpanel);
        (this.mPath = new JTextField(50)).setEditable(false);
        this.mPath.setToolTipText("The folder currently selected");
        this.add(this.mPath);
        this.mText = text;
        this.mShowOpen = showopen;
    }
    
    public void actionPerformed(final ActionEvent e) {
        final String s = this.getOutputFile();
        if (s != null) {
            this.mPath.setText(s);
            this.mFilePath = s;
        }
    }
    
    private String getOutputFile() {
        final JFileChooser jfc = new JFileChooser();
        jfc.setDialogTitle(this.mText);
        jfc.addChoosableFileFilter(jfc.getAcceptAllFileFilter());
        jfc.setFileSelectionMode(1);
        if (this.mFilePath != null && !this.mFilePath.equals("")) {
            final File fmessage = new File(this.mFilePath);
            if (fmessage.exists()) {
                jfc.setSelectedFile(fmessage);
            }
        }
        int returnval;
        if (this.mShowOpen) {
            returnval = jfc.showOpenDialog(null);
        }
        else {
            returnval = jfc.showSaveDialog(null);
        }
        if (returnval == 0) {
            return jfc.getSelectedFile().getPath();
        }
        return null;
    }
    
    public String getOutputFolder() {
        return this.mFilePath;
    }
}
