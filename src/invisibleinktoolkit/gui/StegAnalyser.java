// 
// Decompiled by Procyon v0.5.36
// 

package invisibleinktoolkit.gui;

import invisibleinktoolkit.benchmark.gui.BulkSteganalysisWindow;
import invisibleinktoolkit.benchmark.gui.BenchmarkWindow;
import invisibleinktoolkit.benchmark.gui.SteganalysisWindow;
import java.awt.event.ActionEvent;
import javax.swing.JScrollPane;
import java.awt.Color;
import java.awt.BorderLayout;
import java.awt.Component;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import java.awt.Container;
import javax.swing.BoxLayout;
import java.awt.Dimension;
import java.awt.LayoutManager;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Frame;
import javax.swing.JTextArea;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import javax.swing.JPanel;

public class StegAnalyser extends JPanel implements ActionListener
{
    private JButton mStegButton;
    private JButton mBenchmarkButton;
    private JButton mBulkButton;
    private JTextArea mResults;
    private Frame mParent;
    private static final long serialVersionUID = 0L;
    
    public StegAnalyser(final Frame parent) {
        this.mParent = parent;
        final GridBagLayout gridbag = new GridBagLayout();
        final GridBagConstraints c = new GridBagConstraints();
        this.setLayout(gridbag);
        this.setPreferredSize(new Dimension(750, 420));
        final JPanel buttonpanel = new JPanel();
        buttonpanel.setLayout(new BoxLayout(buttonpanel, 0));
        buttonpanel.setBorder(new TitledBorder("Pick an analysis method"));
        buttonpanel.setPreferredSize(new Dimension(740, 60));
        final JPanel spacer1 = new JPanel();
        spacer1.setPreferredSize(new Dimension(60, 10));
        buttonpanel.add(spacer1);
        (this.mStegButton = new JButton("Steganalysis")).setToolTipText("Analyse just the stego-image");
        this.mStegButton.setPreferredSize(new Dimension(180, 40));
        buttonpanel.add(this.mStegButton);
        final JPanel spacer2 = new JPanel();
        spacer2.setPreferredSize(new Dimension(30, 10));
        buttonpanel.add(spacer2);
        (this.mBenchmarkButton = new JButton("Benchmark")).setToolTipText("Analyse the stego-image versus the original");
        this.mBenchmarkButton.setPreferredSize(new Dimension(180, 40));
        buttonpanel.add(this.mBenchmarkButton);
        final JPanel spacer3 = new JPanel();
        spacer3.setPreferredSize(new Dimension(30, 10));
        buttonpanel.add(spacer3);
        (this.mBulkButton = new JButton("Bulk Steganalysis")).setToolTipText("Analyse methods using several pictures and messages");
        this.mBulkButton.setPreferredSize(new Dimension(180, 40));
        buttonpanel.add(this.mBulkButton);
        final JPanel spacer4 = new JPanel();
        spacer4.setPreferredSize(new Dimension(60, 10));
        buttonpanel.add(spacer4);
        c.weightx = 0.0;
        c.gridy = 0;
        c.gridwidth = 0;
        gridbag.setConstraints(buttonpanel, c);
        this.add(buttonpanel);
        final JPanel textpanel = new JPanel();
        textpanel.setLayout(new BorderLayout());
        textpanel.setBorder(new TitledBorder("Results from last analysis"));
        textpanel.setPreferredSize(new Dimension(740, 355));
        (this.mResults = new JTextArea()).setEditable(false);
        this.mResults.setBackground(Color.white);
        this.mResults.setSize(580, 300);
        this.mResults.setToolTipText("The last analysis results");
        this.mResults.setLineWrap(true);
        final JScrollPane scrollPane = new JScrollPane(this.mResults);
        scrollPane.setPreferredSize(new Dimension(600, 300));
        textpanel.add(scrollPane, "Center");
        c.weightx = 0.0;
        c.gridy = 1;
        c.gridwidth = 0;
        gridbag.setConstraints(textpanel, c);
        this.add(textpanel);
        this.mBenchmarkButton.addActionListener(this);
        this.mStegButton.addActionListener(this);
        this.mBulkButton.addActionListener(this);
    }
    
    public void actionPerformed(final ActionEvent e) {
        if (e.getActionCommand().equalsIgnoreCase("steganalysis")) {
            new SteganalysisWindow(this.mParent, this.mResults);
        }
        else if (e.getActionCommand().equalsIgnoreCase("benchmark")) {
            new BenchmarkWindow(this.mParent, this.mResults);
        }
        else if (e.getActionCommand().equalsIgnoreCase("bulk steganalysis")) {
            new BulkSteganalysisWindow(this.mParent, this.mResults);
        }
    }
}
