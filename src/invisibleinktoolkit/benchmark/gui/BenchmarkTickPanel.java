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

public class BenchmarkTickPanel extends JPanel
{
    private JCheckBox mAADifference;
    private JCheckBox mCorrelationQuality;
    private JCheckBox mLPMSError;
    private JCheckBox mLpNorm;
    private JCheckBox mMSError;
    private JCheckBox mNCCorrelation;
    private JCheckBox mPSNRatio;
    private JCheckBox mSNRatio;
    private static final long serialVersionUID = 0L;
    
    public BenchmarkTickPanel() {
        this.setLayout(new BoxLayout(this, 1));
        this.setBorder(new TitledBorder("Tick the analysis types to run"));
        this.setPreferredSize(new Dimension(740, 100));
        final JPanel firstline = new JPanel();
        firstline.setLayout(new BoxLayout(firstline, 0));
        firstline.setPreferredSize(new Dimension(740, 30));
        final JPanel secondline = new JPanel();
        secondline.setLayout(new BoxLayout(secondline, 0));
        secondline.setPreferredSize(new Dimension(740, 30));
        (this.mAADifference = new JCheckBox("Average Absolute Difference", true)).setToolTipText("Average Absolute Difference");
        this.mAADifference.setPreferredSize(new Dimension(180, 26));
        firstline.add(this.mAADifference);
        (this.mCorrelationQuality = new JCheckBox("Correlation Quality", true)).setToolTipText("Correlation Quality");
        this.mCorrelationQuality.setPreferredSize(new Dimension(180, 26));
        firstline.add(this.mCorrelationQuality);
        (this.mLPMSError = new JCheckBox("Laplacian Mean Squared Error", true)).setToolTipText("Laplacian Mean Squared Error");
        this.mLPMSError.setPreferredSize(new Dimension(180, 26));
        firstline.add(this.mLPMSError);
        (this.mLpNorm = new JCheckBox("Lp Norm", true)).setToolTipText("Lp Norm");
        this.mLpNorm.setPreferredSize(new Dimension(180, 26));
        firstline.add(this.mLpNorm);
        final JPanel spacer1 = new JPanel();
        firstline.add(spacer1);
        (this.mMSError = new JCheckBox("Mean Squared Error", true)).setToolTipText("Mean Squared Error");
        this.mMSError.setPreferredSize(new Dimension(180, 26));
        secondline.add(this.mMSError);
        (this.mNCCorrelation = new JCheckBox("Normalised Cross-Correlation", true)).setToolTipText("Normalised Cross-Correlation");
        this.mNCCorrelation.setPreferredSize(new Dimension(180, 26));
        secondline.add(this.mNCCorrelation);
        (this.mPSNRatio = new JCheckBox("Peak Signal to Noise Ratio", true)).setToolTipText("Peak Signal to Noise Ratio");
        this.mPSNRatio.setPreferredSize(new Dimension(180, 26));
        secondline.add(this.mPSNRatio);
        (this.mSNRatio = new JCheckBox("Signal to Noise Ratio", true)).setToolTipText("Signal to Noise Ratio");
        this.mSNRatio.setPreferredSize(new Dimension(180, 26));
        secondline.add(this.mSNRatio);
        final JPanel spacer2 = new JPanel();
        secondline.add(spacer2);
        this.add(firstline);
        this.add(secondline);
    }
    
    public boolean isAverageAbsoluteDifferenceSelected() {
        return this.mAADifference.isSelected();
    }
    
    public boolean isCorrelationQualitySelected() {
        return this.mCorrelationQuality.isSelected();
    }
    
    public boolean isPeakSNRSelected() {
        return this.mPSNRatio.isSelected();
    }
    
    public boolean isSNRSelected() {
        return this.mSNRatio.isSelected();
    }
    
    public boolean isLpNormSelected() {
        return this.mLpNorm.isSelected();
    }
    
    public boolean isNCCorrelationSelected() {
        return this.mNCCorrelation.isSelected();
    }
    
    public boolean isMeanSquaredErrorSelected() {
        return this.mMSError.isSelected();
    }
    
    public boolean isLaplacianMSErrorSelected() {
        return this.mLPMSError.isSelected();
    }
}
