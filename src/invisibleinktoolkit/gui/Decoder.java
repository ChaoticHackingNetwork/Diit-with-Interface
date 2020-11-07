// 
// Decompiled by Procyon v0.5.36
// 

package invisibleinktoolkit.gui;

import invisibleinktoolkit.stego.NoMessageException;
import java.io.IOException;
import javax.swing.JOptionPane;
import invisibleinktoolkit.stego.StegoImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.awt.event.ActionEvent;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Frame;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import javax.swing.JPanel;

public class Decoder extends JPanel implements ActionListener
{
    private MessageOutputPanel mMOPanel;
    private PasswordPanel mPPanel;
    private JButton mGoButton;
    private InputImagePanel mSPanel;
    private AlgorithmPanel mAPanel;
    private Frame mParent;
    private static final long serialVersionUID = 0L;
    
    public Decoder(final Frame parent) {
        this.mParent = parent;
        final GridBagLayout gridbag = new GridBagLayout();
        final GridBagConstraints c = new GridBagConstraints();
        this.setLayout(gridbag);
        this.setPreferredSize(new Dimension(750, 420));
        c.weightx = 0.0;
        c.gridy = 0;
        c.gridwidth = 0;
        gridbag.setConstraints(this.mSPanel = new InputImagePanel("Pick an image to decode", "Get Image", true), c);
        this.add(this.mSPanel);
        c.weightx = 0.0;
        c.gridy = 1;
        c.gridwidth = 0;
        gridbag.setConstraints(this.mPPanel = new PasswordPanel("Enter the password"), c);
        this.add(this.mPPanel);
        c.weightx = 0.0;
        c.gridy = 2;
        c.gridwidth = 0;
        gridbag.setConstraints(this.mAPanel = new AlgorithmPanel(parent), c);
        this.add(this.mAPanel);
        c.weightx = 0.0;
        c.gridy = 3;
        c.gridwidth = 0;
        gridbag.setConstraints(this.mMOPanel = new MessageOutputPanel(), c);
        this.add(this.mMOPanel);
        (this.mGoButton = new JButton("Go")).setToolTipText("Decode message from stego image");
        this.mGoButton.setPreferredSize(new Dimension(150, 40));
        this.mGoButton.addActionListener(this);
        final JPanel spacer = new JPanel();
        spacer.setPreferredSize(new Dimension(500, 10));
        c.weightx = 0.0;
        c.gridy = 4;
        c.gridwidth = 0;
        gridbag.setConstraints(spacer, c);
        this.add(spacer);
        c.weightx = 0.0;
        c.gridy = 5;
        c.gridwidth = 0;
        gridbag.setConstraints(this.mGoButton, c);
        this.add(this.mGoButton);
        final JPanel fillerpanel = new JPanel();
        fillerpanel.setPreferredSize(new Dimension(500, 165));
        c.weightx = 0.0;
        c.gridy = 6;
        c.gridwidth = 0;
        gridbag.setConstraints(fillerpanel, c);
        this.add(fillerpanel);
    }
    
    public void actionPerformed(final ActionEvent e) {
        final WorkerThread worker = new WorkerThread() {
            public void doWork() {
                final WorkingPanel pane = new WorkingPanel();
                Decoder.this.mParent.setVisible(false);
                pane.show();
                StegoImage stego;
                try {
                    stego = new StegoImage(ImageIO.read(new File(Decoder.this.mSPanel.getPath())));
                }
                catch (Exception e3) {
                    pane.hide();
                    JOptionPane.showMessageDialog(null, "ERROR: Stego image is not a valid file!", "Error!", 0);
                    Decoder.this.mParent.setVisible(true);
                    return;
                }
                if (Decoder.this.mMOPanel.getOutputMessageFile() == null) {
                    pane.hide();
                    JOptionPane.showMessageDialog(null, "ERROR: No message output file has been set!", "Error!", 0);
                    Decoder.this.mParent.setVisible(true);
                    return;
                }
                try {
                    Long pass = Decoder.this.mPPanel.getPassword();
                    String outputFile = Decoder.this.mMOPanel.getOutputMessageFile();
                    Decoder.this.mAPanel.getAlgorithm().decode(stego, Decoder.this.mPPanel.getPassword(), Decoder.this.mMOPanel.getOutputMessageFile());
                }
                catch (IOException e4) {
                    pane.hide();
                    JOptionPane.showMessageDialog(null, "ERROR: Could not decode message from image!", "Error!", 0);
                    Decoder.this.mParent.setVisible(true);
                    return;
                }
                catch (NoMessageException nme) {
                    pane.hide();
                    JOptionPane.showMessageDialog(null, "ERROR: Could not find a message on image with given settings!", "Error - No Message!", 0);
                    Decoder.this.mParent.setVisible(true);
                    return;
                }
                Decoder.this.mParent.setVisible(true);
                pane.hide();
                JOptionPane.showMessageDialog(Decoder.this.mParent, "Success! Message was retrieved.", "Success", 1);
            }
        };
        worker.start();
    }
}
