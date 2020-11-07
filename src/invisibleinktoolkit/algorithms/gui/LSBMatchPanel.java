// 
// Decompiled by Procyon v0.5.36
// 

package invisibleinktoolkit.algorithms.gui;

import java.awt.Component;
import java.awt.Dimension;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import java.awt.LayoutManager;
import java.awt.Container;
import javax.swing.BoxLayout;
import java.awt.event.ActionListener;
import javax.swing.JCheckBox;
import javax.swing.JPanel;

public class LSBMatchPanel extends JPanel
{
    private static final long serialVersionUID = -5347327864521430768L;
    private JCheckBox mShouldMatch;
    
    public LSBMatchPanel(final ActionListener listener, final boolean match) {
        this.setLayout(new BoxLayout(this, 0));
        this.setBorder(new TitledBorder("Should this algorithm use LSB Matching?"));
        this.setPreferredSize(new Dimension(600, 50));
        this.mShouldMatch = new JCheckBox("Use LSB Matching?", match);
        final JPanel spacer = new JPanel();
        this.add(this.mShouldMatch);
        this.add(spacer);
        this.mShouldMatch.addActionListener(listener);
    }
    
    public boolean shouldMatch() {
        return this.mShouldMatch.isSelected();
    }
}
