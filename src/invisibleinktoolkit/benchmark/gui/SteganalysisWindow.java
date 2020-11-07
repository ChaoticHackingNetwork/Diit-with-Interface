// 
// Decompiled by Procyon v0.5.36
// 

package invisibleinktoolkit.benchmark.gui;

import java.awt.image.BufferedImage;
import java.io.Writer;
import java.io.BufferedWriter;
import java.io.FileWriter;
import javax.swing.JFileChooser;
import invisibleinktoolkit.benchmark.StegAnalyser;
import javax.swing.JOptionPane;
import javax.imageio.ImageIO;
import java.io.File;
import invisibleinktoolkit.gui.WorkingPanel;
import invisibleinktoolkit.gui.WorkerThread;
import java.awt.event.ActionEvent;
import java.awt.Container;
import javax.swing.BoxLayout;
import java.awt.Component;
import java.awt.LayoutManager;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.Frame;
import javax.swing.JTextArea;
import invisibleinktoolkit.gui.InputImagePanel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import javax.swing.JDialog;

public class SteganalysisWindow extends JDialog implements ActionListener
{
    private SteganalysisWindow mMe;
    private JButton mOkButton;
    private JButton mCancelButton;
    private OutputLocationPanel mOLPanel;
    private InputImagePanel mIIPanel;
    private StegTickPanel mSTPanel;
    private JTextArea mResults;
    private Frame mParent;
    private static final long serialVersionUID = 0L;
    
    public SteganalysisWindow(final Frame parent, final JTextArea results) {
        super(parent, "Run Steganalysis", true);
        this.mMe = this;
        this.mParent = parent;
        this.mResults = results;
        (this.mOkButton = new JButton("OK")).setPreferredSize(new Dimension(150, 26));
        (this.mCancelButton = new JButton("Cancel")).setPreferredSize(new Dimension(150, 26));
        final JPanel displaypanel = new JPanel();
        final GridBagLayout gridbag = new GridBagLayout();
        final GridBagConstraints c = new GridBagConstraints();
        displaypanel.setLayout(gridbag);
        displaypanel.setPreferredSize(new Dimension(750, 240));
        this.mIIPanel = new InputImagePanel("Pick the image to analyse", "Pick Image", false);
        c.weightx = 0.0;
        c.gridy = 0;
        c.gridwidth = 0;
        gridbag.setConstraints(this.mIIPanel, c);
        displaypanel.add(this.mIIPanel);
        this.mSTPanel = new StegTickPanel();
        c.weightx = 0.0;
        c.gridy = 1;
        c.gridwidth = 0;
        gridbag.setConstraints(this.mSTPanel, c);
        displaypanel.add(this.mSTPanel);
        this.mOLPanel = new OutputLocationPanel();
        c.weightx = 0.0;
        c.gridy = 2;
        c.gridwidth = 0;
        gridbag.setConstraints(this.mOLPanel, c);
        displaypanel.add(this.mOLPanel);
        final JPanel buttonpanel = new JPanel();
        buttonpanel.setPreferredSize(new Dimension(300, 30));
        buttonpanel.setLayout(new BoxLayout(buttonpanel, 0));
        buttonpanel.add(this.mOkButton);
        final JPanel spacer1 = new JPanel();
        spacer1.setPreferredSize(new Dimension(20, 10));
        buttonpanel.add(spacer1);
        buttonpanel.add(this.mCancelButton);
        final JPanel spacer2 = new JPanel();
        spacer2.setPreferredSize(new Dimension(500, 30));
        c.weightx = 0.0;
        c.gridy = 3;
        c.gridwidth = 0;
        gridbag.setConstraints(spacer2, c);
        displaypanel.add(spacer2);
        c.weightx = 0.0;
        c.gridy = 4;
        c.gridwidth = 0;
        gridbag.setConstraints(buttonpanel, c);
        displaypanel.add(buttonpanel);
        this.setContentPane(displaypanel);
        final JPanel fillerpanel = new JPanel();
        fillerpanel.setPreferredSize(new Dimension(500, 10));
        c.weightx = 0.0;
        c.gridy = 5;
        c.gridwidth = 0;
        gridbag.setConstraints(fillerpanel, c);
        displaypanel.add(fillerpanel);
        this.mOkButton.addActionListener(this);
        this.mCancelButton.addActionListener(this);
        this.pack();
        this.setResizable(false);
        this.setLocationRelativeTo(parent);
        this.setVisible(true);
    }
    
    public void actionPerformed(final ActionEvent e1) {
        if (e1.getActionCommand().equalsIgnoreCase("cancel")) {
            this.dispose();
        }
        else if (e1.getActionCommand().equalsIgnoreCase("ok")) {
            final WorkerThread worker = new WorkerThread() {
                public void doWork() {
                    SteganalysisWindow.this.mMe.setVisible(false);
                    SteganalysisWindow.this.mParent.setVisible(false);
                    final WorkingPanel pane = new WorkingPanel();
                    pane.show();
                    BufferedImage stego;
                    try {
                        stego = ImageIO.read(new File(SteganalysisWindow.this.mIIPanel.getPath()));
                        if (stego == null) {
                            throw new Exception("Could not read file.");
                        }
                    }
                    catch (Exception e) {
                        pane.hide();
                        JOptionPane.showMessageDialog(null, "ERROR: Could not read image file", "Error!", 0);
                        SteganalysisWindow.this.mParent.setVisible(true);
                        SteganalysisWindow.this.mMe.setVisible(true);
                        return;
                    }
                    if (!SteganalysisWindow.this.mSTPanel.isLaplaceSelected() && !SteganalysisWindow.this.mSTPanel.isRSAnalysisSelected() && !SteganalysisWindow.this.mSTPanel.isSamplePairsSelected()) {
                        pane.hide();
                        JOptionPane.showMessageDialog(null, "ERROR: You must select at least one steganalysis type!", "Error!", 0);
                        SteganalysisWindow.this.mParent.setVisible(true);
                        SteganalysisWindow.this.mMe.setVisible(true);
                        return;
                    }
                    if (!SteganalysisWindow.this.mOLPanel.isToFile() && !SteganalysisWindow.this.mOLPanel.isToResultsWindow()) {
                        pane.hide();
                        JOptionPane.showMessageDialog(null, "ERROR: You must select an output option!", "Error!", 0);
                        SteganalysisWindow.this.mParent.setVisible(true);
                        SteganalysisWindow.this.mMe.setVisible(true);
                        return;
                    }
                    final StegAnalyser sa = new StegAnalyser(SteganalysisWindow.this.mSTPanel.isRSAnalysisSelected(), SteganalysisWindow.this.mSTPanel.isSamplePairsSelected(), SteganalysisWindow.this.mSTPanel.isLaplaceSelected());
                    try {
                        final String results = sa.run(stego);
                        if (SteganalysisWindow.this.mOLPanel.isToFile()) {
                            try {
                                pane.hide();
                                final JFileChooser jfc = new JFileChooser();
                                jfc.setDialogTitle("Pick a file to save results to");
                                jfc.setAcceptAllFileFilterUsed(true);
                                final int choice = jfc.showSaveDialog(null);
                                if (choice == 0 && jfc.getSelectedFile() != null) {
                                    final BufferedWriter bw = new BufferedWriter(new FileWriter(jfc.getSelectedFile()));
                                    bw.write(results, 0, results.length());
                                    bw.close();
                                }
                                pane.show();
                            }
                            catch (Exception e2) {
                                pane.hide();
                                JOptionPane.showMessageDialog(null, "ERROR: Error writing results file, check permissions and try again.", "Error!", 0);
                                SteganalysisWindow.this.mParent.setVisible(true);
                                SteganalysisWindow.this.mMe.setVisible(true);
                                return;
                            }
                        }
                        if (SteganalysisWindow.this.mOLPanel.isToResultsWindow()) {
                            SteganalysisWindow.this.mResults.setText(results);
                            SteganalysisWindow.this.mResults.setCaretPosition(0);
                        }
                    }
                    catch (Exception e3) {
                        pane.hide();
                        JOptionPane.showMessageDialog(null, "ERROR: Error running benchmarks!", "Error!", 0);
                        SteganalysisWindow.this.mParent.setVisible(true);
                        SteganalysisWindow.this.mMe.setVisible(true);
                        return;
                    }
                    SteganalysisWindow.this.mParent.setVisible(true);
                    pane.hide();
                    JOptionPane.showMessageDialog(SteganalysisWindow.this.mParent, "Success! Steganalysis information generated.", "Success!", 1);
                    SteganalysisWindow.this.mMe.dispose();
                }
            };
            worker.start();
        }
    }
}
