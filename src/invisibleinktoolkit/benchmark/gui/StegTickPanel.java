// 
// Decompiled by Procyon v0.5.36
// 

package invisibleinktoolkit.benchmark.gui;

import java.awt.Component;
import java.awt.Dimension;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import java.awt.LayoutManager;
import java.awt.Container;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JPanel;

public class StegTickPanel extends JPanel
{
    private JCheckBox mLaplaceTick;
    private JCheckBox mRSAnalysisTick;
    private JCheckBox mSamplePairsTick;
    private static final long serialVersionUID = 0L;
    
    public StegTickPanel() {
        this.setLayout(new BoxLayout(this, 0));
        this.setBorder(new TitledBorder("Tick the analysis types to run"));
        this.setPreferredSize(new Dimension(740, 50));
        (this.mRSAnalysisTick = new JCheckBox("RS Analysis", true)).setToolTipText("RS Analysis");
        this.mRSAnalysisTick.setPreferredSize(new Dimension(180, 26));
        this.add(this.mRSAnalysisTick);
        (this.mSamplePairsTick = new JCheckBox("Sample Pairs", false)).setToolTipText("Sample Pairs");
        this.mSamplePairsTick.setPreferredSize(new Dimension(180, 26));
        this.add(this.mSamplePairsTick);
        (this.mLaplaceTick = new JCheckBox("Laplace Graph", false)).setToolTipText("Laplace Graph");
        this.mLaplaceTick.setPreferredSize(new Dimension(180, 26));
        this.add(this.mLaplaceTick);
        final JPanel spacer1 = new JPanel();
        this.add(spacer1);
    }
    
    public boolean isLaplaceSelected() {
        return this.mLaplaceTick.isSelected();
    }
    
    public boolean isRSAnalysisSelected() {
        return this.mRSAnalysisTick.isSelected();
    }
    
    public boolean isSamplePairsSelected() {
        return this.mSamplePairsTick.isSelected();
    }
}
