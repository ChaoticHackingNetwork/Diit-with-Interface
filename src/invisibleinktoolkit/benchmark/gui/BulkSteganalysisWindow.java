// 
// Decompiled by Procyon v0.5.36
// 

package invisibleinktoolkit.benchmark.gui;

import java.util.Date;
import java.io.Writer;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.text.DateFormat;
import java.util.Calendar;
import java.io.File;
import invisibleinktoolkit.benchmark.StegAnalyser;
import javax.swing.JOptionPane;
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
import javax.swing.JTextArea;
import java.awt.Frame;
import invisibleinktoolkit.gui.AlgorithmPanel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import javax.swing.JDialog;

public class BulkSteganalysisWindow extends JDialog implements ActionListener
{
    private BulkSteganalysisWindow mMe;
    private JButton mOkButton;
    private JButton mCancelButton;
    private StegTickPanel mSTPanel;
    private FolderPanel mMFPanel;
    private FolderPanel mSourceFPanel;
    private FolderPanel mTFPanel;
    private FolderPanel mSavePanel;
    private AlgorithmPanel mAPanel;
    private FormatPanel mFormatPanel;
    private Frame mParent;
    private JTextArea mResults;
    private static final long serialVersionUID = 0L;
    
    public BulkSteganalysisWindow(final Frame parent, final JTextArea results) {
        super(parent, "Run Steganalysis across multiple images and messages", true);
        this.mMe = this;
        this.mParent = parent;
        this.mResults = results;
        (this.mOkButton = new JButton("OK")).setPreferredSize(new Dimension(150, 26));
        (this.mCancelButton = new JButton("Cancel")).setPreferredSize(new Dimension(150, 26));
        final JPanel displaypanel = new JPanel();
        final GridBagLayout gridbag = new GridBagLayout();
        final GridBagConstraints c = new GridBagConstraints();
        displaypanel.setLayout(gridbag);
        displaypanel.setPreferredSize(new Dimension(750, 440));
        this.mMFPanel = new FolderPanel("Pick a folder containing the messages", true);
        c.weightx = 0.0;
        c.gridy = 1;
        c.gridwidth = 0;
        gridbag.setConstraints(this.mMFPanel, c);
        displaypanel.add(this.mMFPanel);
        this.mSourceFPanel = new FolderPanel("Pick a folder containing the images", true);
        c.weightx = 0.0;
        c.gridy = 2;
        c.gridwidth = 0;
        gridbag.setConstraints(this.mSourceFPanel, c);
        displaypanel.add(this.mSourceFPanel);
        this.mAPanel = new AlgorithmPanel(parent);
        c.weightx = 0.0;
        c.gridy = 3;
        c.gridwidth = 0;
        gridbag.setConstraints(this.mAPanel, c);
        displaypanel.add(this.mAPanel);
        this.mTFPanel = new FolderPanel("Pick a folder to use for temporary files", false);
        c.weightx = 0.0;
        c.gridy = 4;
        c.gridwidth = 0;
        gridbag.setConstraints(this.mTFPanel, c);
        displaypanel.add(this.mTFPanel);
        this.mSTPanel = new StegTickPanel();
        c.weightx = 0.0;
        c.gridy = 5;
        c.gridwidth = 0;
        gridbag.setConstraints(this.mSTPanel, c);
        displaypanel.add(this.mSTPanel);
        this.mFormatPanel = new FormatPanel();
        c.weightx = 0.0;
        c.gridy = 6;
        c.gridwidth = 0;
        gridbag.setConstraints(this.mFormatPanel, c);
        displaypanel.add(this.mFormatPanel);
        this.mSavePanel = new FolderPanel("Pick a folder to output the file to", true);
        c.weightx = 0.0;
        c.gridy = 7;
        c.gridwidth = 0;
        gridbag.setConstraints(this.mSavePanel, c);
        displaypanel.add(this.mSavePanel);
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
        c.gridy = 8;
        c.gridwidth = 0;
        gridbag.setConstraints(spacer2, c);
        displaypanel.add(spacer2);
        c.weightx = 0.0;
        c.gridy = 9;
        c.gridwidth = 0;
        gridbag.setConstraints(buttonpanel, c);
        displaypanel.add(buttonpanel);
        final JPanel fillerpanel = new JPanel();
        fillerpanel.setPreferredSize(new Dimension(500, 10));
        c.weightx = 0.0;
        c.gridy = 10;
        c.gridwidth = 0;
        gridbag.setConstraints(fillerpanel, c);
        displaypanel.add(fillerpanel);
        this.setContentPane(displaypanel);
        this.mOkButton.addActionListener(this);
        this.mCancelButton.addActionListener(this);
        this.pack();
        this.setResizable(false);
        this.setLocationRelativeTo(parent);
        this.setVisible(true);
    }
    
    public void actionPerformed(final ActionEvent e) {
        if (e.getActionCommand().equalsIgnoreCase("cancel")) {
            this.dispose();
        }
        else if (e.getActionCommand().equalsIgnoreCase("ok")) {
            final WorkerThread worker = new WorkerThread() {
                public void doWork() {
                    BulkSteganalysisWindow.this.mMe.setVisible(false);
                    BulkSteganalysisWindow.this.mParent.setVisible(false);
                    final WorkingPanel pane = new WorkingPanel();
                    pane.show();
                    try {
                        if (BulkSteganalysisWindow.this.mSavePanel.getOutputFolder() == null) {
                            throw new Exception("No output folder is set!");
                        }
                        if (BulkSteganalysisWindow.this.mSavePanel.getOutputFolder() == "") {
                            throw new Exception("No output folder is set!");
                        }
                    }
                    catch (Exception e1) {
                        pane.hide();
                        JOptionPane.showMessageDialog(null, "ERROR: No output folder is set!", "Error!", 0);
                        BulkSteganalysisWindow.this.mParent.setVisible(true);
                        BulkSteganalysisWindow.this.mMe.setVisible(true);
                        return;
                    }
                    String errors = "";
                    final StegAnalyser sa = new StegAnalyser(BulkSteganalysisWindow.this.mSTPanel.isRSAnalysisSelected(), BulkSteganalysisWindow.this.mSTPanel.isSamplePairsSelected(), BulkSteganalysisWindow.this.mSTPanel.isLaplaceSelected());
                    if (BulkSteganalysisWindow.this.mMFPanel.getOutputFolder() != null) {
                        errors = sa.createCombineDirectories(new File(BulkSteganalysisWindow.this.mMFPanel.getOutputFolder()), new File(BulkSteganalysisWindow.this.mSourceFPanel.getOutputFolder()), new File(BulkSteganalysisWindow.this.mTFPanel.getOutputFolder()), BulkSteganalysisWindow.this.mAPanel.getAlgorithm());
                    }
                    String filename1 = "";
                    try {
                        final Calendar rightnow = Calendar.getInstance();
                        final Date timenow = rightnow.getTime();
                        String datenow = DateFormat.getDateTimeInstance().format(timenow);
                        datenow = datenow.replace(' ', '-');
                        datenow = datenow.replace(',', '-');
                        datenow = datenow.replace('.', '-');
                        datenow = datenow.replace('\\', '-');
                        datenow = datenow.replace('/', '-');
                        datenow = datenow.replaceAll(":", "");
                        if (BulkSteganalysisWindow.this.mFormatPanel.isCSVSelected()) {
                            final String csvfile = sa.getCSV(new File(BulkSteganalysisWindow.this.mTFPanel.getOutputFolder()), 700);
                            filename1 = "steganalysis-" + datenow + ".csv";
                            final File filetowrite = new File(BulkSteganalysisWindow.this.mSavePanel.getOutputFolder(), filename1);
                            final BufferedWriter bw = new BufferedWriter(new FileWriter(filetowrite));
                            bw.write(csvfile, 0, csvfile.length());
                            bw.close();
                        }
                        else {
                            File temp = new File(BulkSteganalysisWindow.this.mMFPanel.getOutputFolder());
                            String relation = temp.getName();
                            temp = new File(BulkSteganalysisWindow.this.mSourceFPanel.getOutputFolder());
                            relation = String.valueOf(relation) + "_" + temp.getName() + "_" + BulkSteganalysisWindow.this.mAPanel.getAlgorithmName() + "_" + datenow;
                            final String arfffile = sa.getARFF(new File(BulkSteganalysisWindow.this.mTFPanel.getOutputFolder()), 700, relation);
                            filename1 = String.valueOf(relation) + ".arff";
                            final File filetowrite = new File(BulkSteganalysisWindow.this.mSavePanel.getOutputFolder(), filename1);
                            final BufferedWriter bw2 = new BufferedWriter(new FileWriter(filetowrite));
                            bw2.write(arfffile, 0, arfffile.length());
                            bw2.close();
                        }
                    }
                    catch (Exception e2) {
                        pane.hide();
                        JOptionPane.showMessageDialog(null, "ERROR: Could not produce output file " + filename1, "Error!", 0);
                        BulkSteganalysisWindow.this.mParent.setVisible(true);
                        BulkSteganalysisWindow.this.mMe.setVisible(true);
                        BulkSteganalysisWindow.this.mResults.setText(errors);
                        BulkSteganalysisWindow.this.mResults.setCaretPosition(0);
                        return;
                    }
                    BulkSteganalysisWindow.this.mResults.setText(String.valueOf(errors) + "\n\n Success! File " + filename1 + " was successfully created.\n\n");
                    BulkSteganalysisWindow.this.mResults.setCaretPosition(0);
                    BulkSteganalysisWindow.this.mParent.setVisible(true);
                    pane.hide();
                    JOptionPane.showMessageDialog(BulkSteganalysisWindow.this.mParent, "Success! Bulk analysis information generated.", "Success!", 1);
                    BulkSteganalysisWindow.this.mMe.dispose();
                }
            };
            worker.start();
        }
    }
}
