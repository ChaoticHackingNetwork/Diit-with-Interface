// 
// Decompiled by Procyon v0.5.36
// 

package invisibleinktoolkit.benchmark.gui;

import java.awt.image.BufferedImage;
import java.io.Writer;
import java.io.BufferedWriter;
import java.io.FileWriter;
import javax.swing.JFileChooser;
import invisibleinktoolkit.benchmark.Benchmarker;
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

public class BenchmarkWindow extends JDialog implements ActionListener
{
    private BenchmarkWindow mMe;
    private JButton mOkButton;
    private JButton mCancelButton;
    private InputImagePanel mIIPanel;
    private InputImagePanel mIIPanel2;
    private BenchmarkTickPanel mBTPanel;
    private OutputLocationPanel mOLPanel;
    private JTextArea mResults;
    private Frame mParent;
    private static final long serialVersionUID = 0L;
    
    public BenchmarkWindow(final Frame parent, final JTextArea results) {
        super(parent, "Run a Benchmark", true);
        this.mMe = this;
        this.mParent = parent;
        this.mResults = results;
        (this.mOkButton = new JButton("OK")).setPreferredSize(new Dimension(150, 26));
        (this.mCancelButton = new JButton("Cancel")).setPreferredSize(new Dimension(150, 26));
        final JPanel displaypanel = new JPanel();
        final GridBagLayout gridbag = new GridBagLayout();
        final GridBagConstraints c = new GridBagConstraints();
        displaypanel.setLayout(gridbag);
        displaypanel.setPreferredSize(new Dimension(750, 340));
        this.mIIPanel = new InputImagePanel("Pick the original image", "Get Image", false);
        c.weightx = 0.0;
        c.gridy = 0;
        c.gridwidth = 0;
        gridbag.setConstraints(this.mIIPanel, c);
        displaypanel.add(this.mIIPanel);
        this.mIIPanel2 = new InputImagePanel("Pick the stego image", "Get Image", false);
        c.weightx = 0.0;
        c.gridy = 1;
        c.gridwidth = 0;
        gridbag.setConstraints(this.mIIPanel2, c);
        displaypanel.add(this.mIIPanel2);
        this.mBTPanel = new BenchmarkTickPanel();
        c.weightx = 0.0;
        c.gridy = 2;
        c.gridwidth = 0;
        gridbag.setConstraints(this.mBTPanel, c);
        displaypanel.add(this.mBTPanel);
        this.mOLPanel = new OutputLocationPanel();
        c.weightx = 0.0;
        c.gridy = 3;
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
        c.gridy = 4;
        c.gridwidth = 0;
        gridbag.setConstraints(spacer2, c);
        displaypanel.add(spacer2);
        c.weightx = 0.0;
        c.gridy = 5;
        c.gridwidth = 0;
        gridbag.setConstraints(buttonpanel, c);
        displaypanel.add(buttonpanel);
        this.setContentPane(displaypanel);
        final JPanel fillerpanel = new JPanel();
        fillerpanel.setPreferredSize(new Dimension(500, 10));
        c.weightx = 0.0;
        c.gridy = 6;
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
                    BenchmarkWindow.this.mMe.setVisible(false);
                    BenchmarkWindow.this.mParent.setVisible(false);
                    final WorkingPanel pane = new WorkingPanel();
                    pane.show();
                    BufferedImage orig;
                    try {
                        orig = ImageIO.read(new File(BenchmarkWindow.this.mIIPanel.getPath()));
                    }
                    catch (Exception e) {
                        pane.hide();
                        JOptionPane.showMessageDialog(null, "ERROR: Could not read original image file", "Error!", 0);
                        BenchmarkWindow.this.mParent.setVisible(true);
                        BenchmarkWindow.this.mMe.setVisible(true);
                        return;
                    }
                    BufferedImage stego;
                    try {
                        stego = ImageIO.read(new File(BenchmarkWindow.this.mIIPanel2.getPath()));
                    }
                    catch (Exception e) {
                        pane.hide();
                        JOptionPane.showMessageDialog(null, "ERROR: Could not read stego image file", "Error!", 0);
                        BenchmarkWindow.this.mParent.setVisible(true);
                        BenchmarkWindow.this.mMe.setVisible(true);
                        return;
                    }
                    if (orig.getWidth() != stego.getWidth() || orig.getHeight() != stego.getHeight()) {
                        pane.hide();
                        JOptionPane.showMessageDialog(null, "ERROR: Images must be the same size!", "Error!", 0);
                        BenchmarkWindow.this.mParent.setVisible(true);
                        BenchmarkWindow.this.mMe.setVisible(true);
                        return;
                    }
                    if (!BenchmarkWindow.this.mBTPanel.isAverageAbsoluteDifferenceSelected() && !BenchmarkWindow.this.mBTPanel.isMeanSquaredErrorSelected() && !BenchmarkWindow.this.mBTPanel.isLpNormSelected() && !BenchmarkWindow.this.mBTPanel.isLaplacianMSErrorSelected() && !BenchmarkWindow.this.mBTPanel.isSNRSelected() && !BenchmarkWindow.this.mBTPanel.isPeakSNRSelected() && !BenchmarkWindow.this.mBTPanel.isNCCorrelationSelected() && !BenchmarkWindow.this.mBTPanel.isCorrelationQualitySelected()) {
                        pane.hide();
                        JOptionPane.showMessageDialog(null, "ERROR: You must select some benchmarking types!", "Error!", 0);
                        BenchmarkWindow.this.mParent.setVisible(true);
                        BenchmarkWindow.this.mMe.setVisible(true);
                        return;
                    }
                    if (!BenchmarkWindow.this.mOLPanel.isToFile() && !BenchmarkWindow.this.mOLPanel.isToResultsWindow()) {
                        pane.hide();
                        JOptionPane.showMessageDialog(null, "ERROR: You must select an output option!", "Error!", 0);
                        BenchmarkWindow.this.mParent.setVisible(true);
                        BenchmarkWindow.this.mMe.setVisible(true);
                        return;
                    }
                    final Benchmarker bmark = new Benchmarker(BenchmarkWindow.this.mBTPanel.isAverageAbsoluteDifferenceSelected(), BenchmarkWindow.this.mBTPanel.isMeanSquaredErrorSelected(), BenchmarkWindow.this.mBTPanel.isLpNormSelected(), BenchmarkWindow.this.mBTPanel.isLaplacianMSErrorSelected(), BenchmarkWindow.this.mBTPanel.isSNRSelected(), BenchmarkWindow.this.mBTPanel.isPeakSNRSelected(), BenchmarkWindow.this.mBTPanel.isNCCorrelationSelected(), BenchmarkWindow.this.mBTPanel.isCorrelationQualitySelected());
                    try {
                        final String results = bmark.run(orig, stego);
                        if (BenchmarkWindow.this.mOLPanel.isToFile()) {
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
                                BenchmarkWindow.this.mParent.setVisible(true);
                                BenchmarkWindow.this.mMe.setVisible(true);
                                return;
                            }
                        }
                        if (BenchmarkWindow.this.mOLPanel.isToResultsWindow()) {
                            BenchmarkWindow.this.mResults.setText(results);
                            BenchmarkWindow.this.mResults.setCaretPosition(0);
                        }
                    }
                    catch (Exception e3) {
                        pane.hide();
                        JOptionPane.showMessageDialog(null, "ERROR: Error running benchmarks!", "Error!", 0);
                        BenchmarkWindow.this.mParent.setVisible(true);
                        BenchmarkWindow.this.mMe.setVisible(true);
                        return;
                    }
                    BenchmarkWindow.this.mParent.setVisible(true);
                    pane.hide();
                    JOptionPane.showMessageDialog(BenchmarkWindow.this.mParent, "Success! Benchmarking information generated.", "Success!", 1);
                    BenchmarkWindow.this.mMe.dispose();
                }
            };
            worker.start();
        }
    }
}
