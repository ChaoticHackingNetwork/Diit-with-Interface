// 
// Decompiled by Procyon v0.5.36
// 

package invisibleinktoolkit.algorithms.gui;

import java.awt.event.ActionEvent;
import invisibleinktoolkit.filters.Filterable;
import java.awt.Component;
import java.awt.Container;
import javax.swing.BoxLayout;
import java.awt.LayoutManager;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.Frame;
import javax.swing.JButton;
import invisibleinktoolkit.algorithms.BattleSteg;
import java.awt.event.ActionListener;
import javax.swing.JDialog;

public class BattleStegWindow extends JDialog implements ActionListener
{
    private WriteableBitsPanel mBitsPanel;
    private BattleSteg mAlgorithm;
    private JButton mOkButton;
    private JButton mCancelButton;
    private FilterSelectionPanel mFilterPanel;
    private BattleStegOptionsPanel mBattleStegPanel;
    private LSBMatchPanel mLSBMatch;
    private static final long serialVersionUID = 0L;
    
    public BattleStegWindow(final Frame parent, final BattleSteg algorithm) {
        super(parent, "Set the algorithm options", true);
        final int start = algorithm.getStartBits();
        final int end = algorithm.getEndBits();
        this.mBitsPanel = new WriteableBitsPanel(start, end);
        final Filterable algo = algorithm;
        this.mFilterPanel = new FilterSelectionPanel(algo.getFilter(), this.mBitsPanel);
        this.mBattleStegPanel = new BattleStegOptionsPanel(algorithm);
        this.mLSBMatch = new LSBMatchPanel(this, algorithm.getMatch());
        this.mBitsPanel.setEnabled(!algorithm.getMatch());
        (this.mOkButton = new JButton("OK")).setPreferredSize(new Dimension(150, 26));
        (this.mCancelButton = new JButton("Cancel")).setPreferredSize(new Dimension(150, 26));
        final JPanel displaypanel = new JPanel();
        final GridBagLayout gridbag = new GridBagLayout();
        final GridBagConstraints c = new GridBagConstraints();
        displaypanel.setLayout(gridbag);
        displaypanel.setPreferredSize(new Dimension(620, 460));
        final JPanel buttonpanel = new JPanel();
        buttonpanel.setPreferredSize(new Dimension(300, 30));
        buttonpanel.setLayout(new BoxLayout(buttonpanel, 0));
        buttonpanel.add(this.mOkButton);
        final JPanel spacer1 = new JPanel();
        spacer1.setPreferredSize(new Dimension(20, 10));
        buttonpanel.add(spacer1);
        buttonpanel.add(this.mCancelButton);
        c.weightx = 0.0;
        c.gridy = 0;
        c.gridwidth = 0;
        gridbag.setConstraints(this.mBitsPanel, c);
        displaypanel.add(this.mBitsPanel);
        c.weightx = 0.0;
        c.gridy = 1;
        c.gridwidth = 0;
        gridbag.setConstraints(this.mFilterPanel, c);
        displaypanel.add(this.mFilterPanel);
        c.weightx = 0.0;
        c.gridy = 2;
        c.gridwidth = 0;
        gridbag.setConstraints(this.mLSBMatch, c);
        displaypanel.add(this.mLSBMatch);
        c.weightx = 0.0;
        c.gridy = 3;
        c.gridwidth = 0;
        gridbag.setConstraints(this.mBattleStegPanel, c);
        displaypanel.add(this.mBattleStegPanel);
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
        fillerpanel.setPreferredSize(new Dimension(500, 20));
        c.weightx = 0.0;
        c.gridy = 6;
        c.gridwidth = 0;
        gridbag.setConstraints(fillerpanel, c);
        displaypanel.add(fillerpanel);
        this.mAlgorithm = algorithm;
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
            if (!this.mLSBMatch.shouldMatch()) {
                this.mAlgorithm.setStartBits(this.mBitsPanel.getStartBit());
                this.mAlgorithm.setEndBits(this.mBitsPanel.getEndBit());
                this.mAlgorithm.setMatch(false);
                if (this.mAlgorithm instanceof Filterable) {
                    final Filterable falgo = this.mAlgorithm;
                    falgo.setFilter(this.mFilterPanel.getFilter());
                }
            }
            else {
                this.mAlgorithm.setStartBits(0);
                this.mAlgorithm.setEndBits(0);
                this.mAlgorithm.setMatch(true);
                if (this.mAlgorithm instanceof Filterable) {
                    final Filterable falgo = this.mAlgorithm;
                    falgo.setFilter(this.mFilterPanel.getFilter());
                }
            }
            this.mAlgorithm.setMoveAway(this.mBattleStegPanel.getMoveAway());
            this.mAlgorithm.setRange(this.mBattleStegPanel.getRange());
            this.mAlgorithm.setIncreaseShots(this.mBattleStegPanel.getIncreaseShots());
            this.mAlgorithm.setInitialShots(this.mBattleStegPanel.getInitialShots());
            this.dispose();
        }
        else if (this.mLSBMatch.shouldMatch()) {
            this.mBitsPanel.setEnabled(false);
            this.mFilterPanel.changeFilterBits(4);
        }
        else {
            this.mBitsPanel.setEnabled(true);
        }
    }
}
