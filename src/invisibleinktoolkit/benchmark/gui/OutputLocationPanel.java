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

public class OutputLocationPanel extends JPanel
{
    private JCheckBox mToFile;
    private JCheckBox mToResultsWindow;
    private static final long serialVersionUID = 0L;
    
    public OutputLocationPanel() {
        this.setLayout(new BoxLayout(this, 0));
        this.setBorder(new TitledBorder("Tick places to output results to"));
        this.setPreferredSize(new Dimension(740, 50));
        (this.mToResultsWindow = new JCheckBox("Print to results window", true)).setPreferredSize(new Dimension(180, 26));
        this.add(this.mToResultsWindow);
        (this.mToFile = new JCheckBox("Print to file", false)).setPreferredSize(new Dimension(180, 26));
        this.add(this.mToFile);
        final JPanel spacer1 = new JPanel();
        this.add(spacer1);
    }
    
    public boolean isToFile() {
        return this.mToFile.isSelected();
    }
    
    public boolean isToResultsWindow() {
        return this.mToResultsWindow.isSelected();
    }
}
