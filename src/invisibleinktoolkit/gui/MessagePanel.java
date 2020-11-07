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

public class MessagePanel extends JPanel implements ActionListener
{
    private JButton mButton;
    private JTextField mPath;
    private String mMessagePath;
    private Embedder mEmbedder;
    private static final long serialVersionUID = 0L;
    
    public MessagePanel() {
        this((Embedder)null);
    }
    
    public MessagePanel(final Embedder embed) {
        this.setLayout(new BoxLayout(this, 0));
        this.setBorder(new TitledBorder("Pick a message to embed"));
        this.setPreferredSize(new Dimension(740, 50));
        (this.mButton = new JButton("Get Message")).addActionListener(this);
        this.mButton.setPreferredSize(new Dimension(130, 26));
        this.mButton.setToolTipText("Pick a message to embed");
        this.add(this.mButton);
        final JPanel fillerpanel = new JPanel();
        fillerpanel.setPreferredSize(new Dimension(20, 1));
        this.add(fillerpanel);
        (this.mPath = new JTextField(50)).setEditable(false);
        this.mPath.setToolTipText("The file currently selected for the message");
        this.add(this.mPath);
        this.mMessagePath = "";
        this.mEmbedder = embed;
    }
    
    public void actionPerformed(final ActionEvent e) {
        String s = this.getMessageFile();
        if (s != null) {
            this.mMessagePath = s;
            s = s.substring(s.lastIndexOf(System.getProperty("file.separator")) + 1, s.length());
            this.mPath.setText(s);
            if (this.mEmbedder != null) {
                this.mEmbedder.updateEmbeddingRate();
            }
        }
    }
    
    private String getMessageFile() {
        final JFileChooser jfc = new JFileChooser();
        jfc.setDialogTitle("Pick a message to embed");
        jfc.addChoosableFileFilter(jfc.getAcceptAllFileFilter());
        if (this.mMessagePath != null && !this.mMessagePath.equals("")) {
            final File fmessage = new File(this.mMessagePath);
            if (fmessage.exists()) {
                jfc.setSelectedFile(fmessage);
            }
        }
        final int returnval = jfc.showOpenDialog(null);
        if (returnval == 0) {
            return jfc.getSelectedFile().getPath();
        }
        return null;
    }
    
    public String getPath() {
        return this.mMessagePath;
    }
}
