// 
// Decompiled by Procyon v0.5.36
// 

package invisibleinktoolkit.benchmark.gui;

import java.awt.Component;
import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import java.awt.Dimension;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import java.awt.LayoutManager;
import java.awt.Container;
import javax.swing.BoxLayout;
import javax.swing.JRadioButton;
import javax.swing.JPanel;

public class FormatPanel extends JPanel
{
    private JRadioButton mCSVButton;
    private JRadioButton mARFFButton;
    private static final long serialVersionUID = 0L;
    
    public FormatPanel() {
        this.setLayout(new BoxLayout(this, 0));
        this.setBorder(new TitledBorder("Choose the output format"));
        this.setPreferredSize(new Dimension(740, 50));
        (this.mCSVButton = new JRadioButton("CSV file")).setToolTipText("CSV File - Comma Separated Values");
        this.mCSVButton.setPreferredSize(new Dimension(180, 26));
        this.mCSVButton.setSelected(true);
        (this.mARFFButton = new JRadioButton("ARFF (WEKA) file")).setToolTipText("ARFF file - WEKA machine learning file format");
        this.mARFFButton.setPreferredSize(new Dimension(180, 26));
        this.mARFFButton.setSelected(false);
        final ButtonGroup bgroup = new ButtonGroup();
        bgroup.add(this.mCSVButton);
        bgroup.add(this.mARFFButton);
        this.add(this.mCSVButton);
        this.add(this.mARFFButton);
        final JPanel spacer1 = new JPanel();
        this.add(spacer1);
    }
    
    public boolean isCSVSelected() {
        return this.mCSVButton.isSelected();
    }
}
