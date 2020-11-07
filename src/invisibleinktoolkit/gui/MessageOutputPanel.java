// 
// Decompiled by Procyon v0.5.36
// 

package invisibleinktoolkit.gui;

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

public class MessageOutputPanel extends JPanel implements ActionListener
{
    private JButton mButton;
    private JTextField mPath;
    private String mFilePath;
    private static final long serialVersionUID = 0L;
    
    public MessageOutputPanel() {
        this.setLayout(new BoxLayout(this, 0));
        this.setBorder(new TitledBorder("Pick a message file to write to"));
        this.setPreferredSize(new Dimension(740, 50));
        (this.mButton = new JButton("Set Message")).addActionListener(this);
        this.mButton.setPreferredSize(new Dimension(130, 26));
        this.mButton.setToolTipText("Pick a message file to write to");
        this.add(this.mButton);
        final JPanel fillerpanel = new JPanel();
        fillerpanel.setPreferredSize(new Dimension(20, 1));
        this.add(fillerpanel);
        (this.mPath = new JTextField(50)).setEditable(false);
        this.mPath.setToolTipText("The file currently selected for the message output");
        this.add(this.mPath);
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
        jfc.setDialogTitle("Pick an output message file");
        jfc.addChoosableFileFilter(jfc.getAcceptAllFileFilter());
        if (this.mFilePath != null && !this.mFilePath.equals("")) {
            final File fmessage = new File(this.mFilePath);
            if (fmessage.exists()) {
                jfc.setSelectedFile(fmessage);
            }
        }
        final int returnval = jfc.showSaveDialog(null);
        if (returnval == 0) {
            return jfc.getSelectedFile().getPath();
        }
        return null;
    }
    
    public String getOutputMessageFile() {
        return this.mFilePath;
    }
}
