// 
// Decompiled by Procyon v0.5.36
// 

package invisibleinktoolkit.algorithms.gui;

import java.awt.event.ActionEvent;
import java.util.Vector;
import java.awt.Component;
import javax.swing.JLabel;
import java.awt.Dimension;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import java.awt.LayoutManager;
import java.awt.Container;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import java.awt.event.ActionListener;
import javax.swing.JPanel;

public class WriteableBitsPanel extends JPanel implements ActionListener
{
    private static final long serialVersionUID = -3025560889320558958L;
    private JComboBox mStartCombo;
    private JComboBox mEndCombo;
    private int mAllowedBits;
    private boolean mAlteringBits;
    
    public WriteableBitsPanel(final int start, final int finish) {
        this.mAllowedBits = 7;
        this.setLayout(new BoxLayout(this, 0));
        this.setBorder(new TitledBorder("Select the bits to write to"));
        this.setPreferredSize(new Dimension(600, 50));
        final JLabel label1 = new JLabel("Write from bit number:");
        label1.setPreferredSize(new Dimension(250, 26));
        this.add(label1);
        Vector numbers = new Vector();
        for (int i = 1; i <= this.mAllowedBits; ++i) {
            numbers.add(i);
        }
        (this.mStartCombo = new JComboBox(numbers)).setToolTipText("Start bit");
        this.mStartCombo.setEditable(false);
        this.mStartCombo.setSelectedIndex(start);
        this.add(this.mStartCombo);
        final JLabel label2 = new JLabel("  to:");
        label2.setPreferredSize(new Dimension(50, 26));
        this.add(label2);
        numbers = new Vector();
        for (int j = start + 1; j <= this.mAllowedBits; ++j) {
            numbers.add(j);
        }
        (this.mEndCombo = new JComboBox(numbers)).setToolTipText("End bit");
        this.mEndCombo.setEditable(false);
        if (finish >= this.getSmallestEndBit() && finish <= this.mAllowedBits) {
            this.mEndCombo.setSelectedIndex(finish - this.getSmallestEndBit());
        }
        else {
            this.mEndCombo.setSelectedIndex(0);
        }
        this.mStartCombo.addActionListener(this);
        this.add(this.mEndCombo);
        this.mAlteringBits = false;
    }
    
    public void actionPerformed(final ActionEvent e) {
        if (!this.mAlteringBits) {
            final int currentendbit = this.getEndBit();
            this.mEndCombo.removeAllItems();
            for (int i = this.getStartBit() + 1; i <= this.mAllowedBits; ++i) {
                this.mEndCombo.addItem(i);
            }
            if (currentendbit >= this.getSmallestEndBit()) {
                if (currentendbit > this.getSmallestEndBit() + this.mEndCombo.getItemCount()) {
                    this.mEndCombo.setSelectedIndex(this.mEndCombo.getItemCount() - 1);
                }
                else {
                    this.mEndCombo.setSelectedIndex(currentendbit - this.getSmallestEndBit());
                }
            }
            else {
                this.mEndCombo.setSelectedIndex(0);
            }
        }
    }
    
    public void setAllowedBits(int newAllowed) {
        if (newAllowed < 0) {
            newAllowed = 1;
        }
        if (newAllowed > 8) {
            newAllowed = 8;
        }
        if (newAllowed == this.mAllowedBits) {
            return;
        }
        this.mAllowedBits = newAllowed;
        this.mAlteringBits = true;
        final int currentbit = this.getStartBit();
        this.mStartCombo.setSelectedIndex(0);
        this.mStartCombo.removeAllItems();
        for (int i = 1; i <= this.mAllowedBits; ++i) {
            this.mStartCombo.addItem(i);
        }
        if (currentbit >= this.mAllowedBits) {
            this.mStartCombo.setSelectedIndex(this.mStartCombo.getItemCount() - 1);
        }
        else {
            this.mStartCombo.setSelectedIndex(currentbit);
        }
        final int currentendbit = this.getEndBit();
        this.mEndCombo.removeAllItems();
        for (int j = this.getStartBit() + 1; j <= this.mAllowedBits; ++j) {
            this.mEndCombo.addItem(j);
        }
        if (currentendbit >= this.getSmallestEndBit()) {
            if (currentendbit > this.getSmallestEndBit() + (this.mEndCombo.getItemCount() - 1)) {
                this.mEndCombo.setSelectedIndex(this.mEndCombo.getItemCount() - 1);
            }
            else {
                this.mEndCombo.setSelectedIndex(currentendbit - this.getSmallestEndBit());
            }
        }
        else {
            this.mEndCombo.setSelectedIndex(0);
        }
        this.mAlteringBits = false;
    }
    
    private int getSmallestEndBit() {
        return ((int) this.mEndCombo.getItemAt(0)) - 1;
    }
    
    public int getStartBit() {
        return (int)this.mStartCombo.getSelectedItem() - 1;
    }
    
    public int getEndBit() {
        return (int)this.mEndCombo.getSelectedItem() - 1;
    }
    
    public void setEnabled(final boolean enabled) {
        if (enabled) {
            this.mStartCombo.setEnabled(true);
            this.mEndCombo.setEnabled(true);
        }
        else {
            this.mStartCombo.setEnabled(false);
            this.mEndCombo.setEnabled(false);
        }
        super.setEnabled(enabled);
    }
}
