// 
// Decompiled by Procyon v0.5.36
// 

package invisibleinktoolkit.gui;

import invisibleinktoolkit.stego.StegoImage;
import javax.swing.Icon;
import javax.swing.JOptionPane;
import java.awt.event.ActionEvent;
import invisibleinktoolkit.stego.StegoAlgorithm;
import invisibleinktoolkit.stego.CoverImage;
import invisibleinktoolkit.stego.InsertableMessage;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.LayoutManager;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Frame;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import javax.swing.JPanel;

public class Encoder extends JPanel implements ActionListener, Embedder
{
    private MessagePanel mMPanel;
    private InputImagePanel mCPanel;
    private PasswordPanel mPPanel;
    private PasswordPanel mRPPanel;
    private CapacityPanel mERPanel;
    private JButton mGoButton;
    private StegoImagePanel mSPanel;
    private AlgorithmPanel mAPanel;
    private Frame mParent;
    private static final long serialVersionUID = 0L;
    
    public Encoder(final Frame parent) {
        this.mParent = parent;
        final GridBagLayout gridbag = new GridBagLayout();
        final GridBagConstraints c = new GridBagConstraints();
        this.setLayout(gridbag);
        this.setPreferredSize(new Dimension(750, 420));
        c.weightx = 0.0;
        c.gridy = 0;
        c.gridwidth = 0;
        gridbag.setConstraints(this.mMPanel = new MessagePanel(this), c);
        this.add(this.mMPanel);
        c.weightx = 0.0;
        c.gridy = 1;
        c.gridwidth = 0;
        gridbag.setConstraints(this.mCPanel = new InputImagePanel("Pick a cover image", "Get Cover", true, this), c);
        this.add(this.mCPanel);
        c.weightx = 0.0;
        c.gridy = 2;
        c.gridwidth = 0;
        gridbag.setConstraints(this.mPPanel = new PasswordPanel("Enter a password"), c);
        this.add(this.mPPanel);
        c.weightx = 0.0;
        c.gridy = 3;
        c.gridwidth = 0;
        gridbag.setConstraints(this.mRPPanel = new PasswordPanel("Re-enter password"), c);
        this.add(this.mRPPanel);
        c.weightx = 0.0;
        c.gridy = 4;
        c.gridwidth = 0;
        gridbag.setConstraints(this.mAPanel = new AlgorithmPanel(parent, this), c);
        this.add(this.mAPanel);
        c.weightx = 0.0;
        c.gridy = 5;
        c.gridwidth = 0;
        gridbag.setConstraints(this.mERPanel = new CapacityPanel(), c);
        this.add(this.mERPanel);
        c.weightx = 0.0;
        c.gridy = 6;
        c.gridwidth = 0;
        gridbag.setConstraints(this.mSPanel = new StegoImagePanel(), c);
        this.add(this.mSPanel);
        final JPanel spacer = new JPanel();
        spacer.setPreferredSize(new Dimension(500, 10));
        c.weightx = 0.0;
        c.gridy = 7;
        c.gridwidth = 0;
        gridbag.setConstraints(spacer, c);
        this.add(spacer);
        (this.mGoButton = new JButton("Go")).setToolTipText("Encode message onto cover image");
        this.mGoButton.setPreferredSize(new Dimension(150, 40));
        this.mGoButton.addActionListener(this);
        c.weightx = 0.0;
        c.gridy = 8;
        c.gridwidth = 0;
        gridbag.setConstraints(this.mGoButton, c);
        this.add(this.mGoButton);
        final JPanel fillerpanel = new JPanel();
        fillerpanel.setPreferredSize(new Dimension(500, 15));
        c.weightx = 0.0;
        c.gridy = 9;
        c.gridwidth = 0;
        gridbag.setConstraints(fillerpanel, c);
        this.add(fillerpanel);
    }
    
    public void updateEmbeddingRate() {
        InsertableMessage mess;
        try {
            mess = new InsertableMessage(this.mMPanel.getPath());
        }
        catch (Exception e1) {
            return;
        }
        CoverImage img;
        try {
            img = new CoverImage(this.mCPanel.getPath());
        }
        catch (Exception e2) {
            return;
        }
        final StegoAlgorithm stego = this.mAPanel.getAlgorithm();
        this.mERPanel.setValue(mess, img, stego);
    }
    
    public void actionPerformed(final ActionEvent e) {
        final WorkerThread worker = new WorkerThread() {
            public void doWork() {
                final WorkingPanel pane = new WorkingPanel();
                Encoder.this.mParent.setVisible(false);
                pane.show();
                InsertableMessage mess;
                try {
                    mess = new InsertableMessage(Encoder.this.mMPanel.getPath());
                }
                catch (Exception e1) {
                    pane.hide();
                    JOptionPane.showMessageDialog(null, "ERROR: Could not read message file", "Error!", 0);
                    Encoder.this.mParent.setVisible(true);
                    return;
                }
                CoverImage img;
                try {
                    img = new CoverImage(Encoder.this.mCPanel.getPath());
                }
                catch (Exception e2) {
                    pane.hide();
                    JOptionPane.showMessageDialog(null, "ERROR: Could not read cover image file", "Error!", 0);
                    Encoder.this.mParent.setVisible(true);
                    return;
                }
                StegoImage stego;
                try {
                    if (Encoder.this.mPPanel.getPassword() != Encoder.this.mRPPanel.getPassword()) {
                        pane.hide();
                        JOptionPane.showMessageDialog(null, "ERROR: Password fields do not match!", "Error!", 0);
                        Encoder.this.mParent.setVisible(true);
                        return;
                    }
                    if (Encoder.this.mSPanel.getOutputFile() == null) {
                        pane.hide();
                        JOptionPane.showMessageDialog(null, "ERROR: No output file has been set!", "Error!", 0);
                        Encoder.this.mParent.setVisible(true);
                        return;
                    }
                    stego = Encoder.this.mAPanel.getAlgorithm().encode(mess, img, Encoder.this.mPPanel.getPassword());
                }
                catch (Exception e3) {
                    pane.hide();
                    JOptionPane.showMessageDialog(null, "ERROR: Message does not fit on image!", "Error!", 0);
                    Encoder.this.mParent.setVisible(true);
                    return;
                }
                try {
                    stego.write(Encoder.this.mSPanel.getFormat(), Encoder.this.mSPanel.getOutputFile());
                }
                catch (Exception e4) {
                    pane.hide();
                    JOptionPane.showMessageDialog(null, "ERROR: Could not output stego image file!", "Error!", 0);
                    Encoder.this.mParent.setVisible(true);
                    return;
                }
                Encoder.this.mParent.setVisible(true);
                pane.hide();
                final Object[] oarray = { "OK", "View Results" };
                final int results = JOptionPane.showOptionDialog(null, "Success! Message hiding was successful.", "Success!", 1, -1, null, oarray, oarray[0]);
                if (results == 1) {
                    final ViewButton vb = new ViewButton("View Results");
                    vb.setImage(Encoder.this.mSPanel.getOutputFile().getPath());
                    vb.doClick();
                }
            }
            
            public void isInterrupted() {
                Encoder.this.mParent.setVisible(true);
                JOptionPane.showMessageDialog(null, "ERROR: User Cancelled Operation", "Error!", 0);
            }
            
            public void finished() {
                Encoder.this.mParent.setVisible(true);
            }
        };
        worker.start();
    }
}
