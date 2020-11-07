// 
// Decompiled by Procyon v0.5.36
// 

package invisibleinktoolkit.algorithms.gui;

import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import java.awt.Component;
import javax.swing.JLabel;
import java.awt.Dimension;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import java.awt.LayoutManager;
import java.awt.Container;
import javax.swing.BoxLayout;
import invisibleinktoolkit.algorithms.BattleSteg;
import javax.swing.JSpinner;
import javax.swing.JPanel;

public class BattleStegOptionsPanel extends JPanel
{
    private JSpinner mMoveAway;
    private JSpinner mInitialShots;
    private JSpinner mIncreaseNumber;
    private JSpinner mRange;
    private static final long serialVersionUID = 0L;
    
    public BattleStegOptionsPanel(final BattleSteg algorithm) {
        this.setLayout(new BoxLayout(this, 1));
        this.setBorder(new TitledBorder("Set the options for BattleSteg"));
        this.setPreferredSize(new Dimension(600, 150));
        final JPanel mapanel = new JPanel();
        mapanel.setPreferredSize(new Dimension(600, 50));
        mapanel.setLayout(new BoxLayout(mapanel, 0));
        final JLabel label1 = new JLabel("Set the maximum number of shots before moving away:");
        label1.setPreferredSize(new Dimension(400, 26));
        mapanel.add(label1);
        final SpinnerModel model1 = new SpinnerNumberModel(algorithm.getMoveAway(), 1, 50, 1);
        this.mMoveAway = new JSpinner(model1);
        ((JSpinner.DefaultEditor)this.mMoveAway.getEditor()).getTextField().setEditable(false);
        this.mMoveAway.setPreferredSize(new Dimension(100, 26));
        this.mMoveAway.setToolTipText("The maximum number of shots before moving away");
        mapanel.add(this.mMoveAway);
        final JPanel rangepanel = new JPanel();
        rangepanel.setPreferredSize(new Dimension(600, 50));
        rangepanel.setLayout(new BoxLayout(rangepanel, 0));
        final JLabel label2 = new JLabel("Set the range (radius) of the shots:");
        label2.setPreferredSize(new Dimension(400, 26));
        rangepanel.add(label2);
        final SpinnerModel model2 = new SpinnerNumberModel(algorithm.getRange(), 1, 50, 1);
        this.mRange = new JSpinner(model2);
        ((JSpinner.DefaultEditor)this.mRange.getEditor()).getTextField().setEditable(false);
        this.mRange.setPreferredSize(new Dimension(100, 26));
        this.mRange.setToolTipText("The range (radius) of the shots");
        rangepanel.add(this.mRange);
        final JPanel ispanel = new JPanel();
        ispanel.setPreferredSize(new Dimension(600, 50));
        ispanel.setLayout(new BoxLayout(ispanel, 0));
        final JLabel label3 = new JLabel("Set the number of initial shots to make:");
        label3.setPreferredSize(new Dimension(400, 26));
        ispanel.add(label3);
        final SpinnerModel model3 = new SpinnerNumberModel(algorithm.getInitialShots(), 0, 50, 1);
        this.mInitialShots = new JSpinner(model3);
        ((JSpinner.DefaultEditor)this.mInitialShots.getEditor()).getTextField().setEditable(false);
        this.mInitialShots.setPreferredSize(new Dimension(100, 26));
        this.mInitialShots.setToolTipText("The initial number of shots to make");
        ispanel.add(this.mInitialShots);
        final JPanel inpanel = new JPanel();
        inpanel.setPreferredSize(new Dimension(600, 50));
        inpanel.setLayout(new BoxLayout(inpanel, 0));
        final JLabel label4 = new JLabel("Set the number of shots to increase by when a hit occurs:");
        label4.setPreferredSize(new Dimension(400, 26));
        inpanel.add(label4);
        final SpinnerModel model4 = new SpinnerNumberModel(algorithm.getIncreaseShots(), 0, 50, 1);
        this.mIncreaseNumber = new JSpinner(model4);
        ((JSpinner.DefaultEditor)this.mIncreaseNumber.getEditor()).getTextField().setEditable(false);
        this.mIncreaseNumber.setPreferredSize(new Dimension(100, 26));
        this.mIncreaseNumber.setToolTipText("The number of shots to increase by");
        inpanel.add(this.mIncreaseNumber);
        this.add(rangepanel);
        final JPanel spacer = new JPanel();
        spacer.setPreferredSize(new Dimension(10, 5));
        this.add(spacer);
        this.add(mapanel);
        final JPanel spacer2 = new JPanel();
        spacer2.setPreferredSize(new Dimension(10, 5));
        this.add(spacer2);
        this.add(ispanel);
        final JPanel spacer3 = new JPanel();
        spacer3.setPreferredSize(new Dimension(10, 5));
        this.add(spacer3);
        this.add(inpanel);
        final JPanel spacer4 = new JPanel();
        spacer4.setPreferredSize(new Dimension(10, 5));
        this.add(spacer4);
    }
    
    public int getMoveAway() {
        return (int)this.mMoveAway.getValue();
    }
    
    public int getRange() {
        return (int)this.mRange.getValue();
    }
    
    public int getInitialShots() {
        return (int)this.mInitialShots.getValue();
    }
    
    public int getIncreaseShots() {
        return (int)this.mIncreaseNumber.getValue();
    }
}
