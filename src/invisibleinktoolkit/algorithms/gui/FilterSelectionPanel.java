// 
// Decompiled by Procyon v0.5.36
// 

package invisibleinktoolkit.algorithms.gui;

import java.awt.event.ActionEvent;
import invisibleinktoolkit.filters.Prewitt;
import invisibleinktoolkit.filters.Sobel;
import invisibleinktoolkit.filters.Laplace;
import java.util.Vector;
import java.awt.Component;
import javax.swing.JLabel;
import java.awt.Dimension;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import java.awt.LayoutManager;
import java.awt.Container;
import javax.swing.BoxLayout;
import invisibleinktoolkit.filters.Filter;
import javax.swing.JComboBox;
import java.awt.event.ActionListener;
import javax.swing.JPanel;

public class FilterSelectionPanel extends JPanel implements ActionListener
{
    private JComboBox mFilterBox;
    private JComboBox mBitBox;
    private WriteableBitsPanel mWBPanel;
    private static final long serialVersionUID = 0L;
    
    public FilterSelectionPanel(final Filter filter, final WriteableBitsPanel wbpanel) {
        this.setLayout(new BoxLayout(this, 1));
        this.setBorder(new TitledBorder("Select the filter to use"));
        this.setPreferredSize(new Dimension(600, 115));
        final JPanel toppanel = new JPanel();
        toppanel.setPreferredSize(new Dimension(600, 26));
        toppanel.setLayout(new BoxLayout(toppanel, 0));
        final JLabel label1 = new JLabel("Select the filter to use:");
        label1.setPreferredSize(new Dimension(400, 26));
        toppanel.add(label1);
        final Vector filters = new Vector();
        filters.add("Laplace");
        filters.add("Sobel");
        filters.add("Prewitt");
        this.mFilterBox = new JComboBox(filters);
        if (filter instanceof Laplace) {
            this.mFilterBox.setSelectedIndex(0);
        }
        else if (filter instanceof Sobel) {
            this.mFilterBox.setSelectedIndex(1);
        }
        else if (filter instanceof Prewitt) {
            this.mFilterBox.setSelectedIndex(2);
        }
        else {
            this.mFilterBox.setSelectedIndex(0);
        }
        toppanel.add(this.mFilterBox);
        this.mFilterBox.setToolTipText("The filter that will be used by the algorithm");
        final JPanel midpanel = new JPanel();
        midpanel.setPreferredSize(new Dimension(600, 26));
        midpanel.setLayout(new BoxLayout(midpanel, 0));
        final JLabel label2 = new JLabel("Set how many bits the filter should use:");
        label2.setPreferredSize(new Dimension(400, 26));
        midpanel.add(label2);
        final Vector numbers = new Vector();
        for (int i = 2; i <= 7; ++i) {
            numbers.add(i);
        }
        (this.mBitBox = new JComboBox(numbers)).setPreferredSize(new Dimension(50, 26));
        int index = filter.getEndRange() - filter.getStartRange();
        index -= 2;
        this.mBitBox.setSelectedIndex(index);
        this.mBitBox.setToolTipText("The number of most significant bits the filter will use");
        midpanel.add(this.mBitBox);
        this.mBitBox.addActionListener(this);
        this.add(toppanel);
        final JPanel spacer = new JPanel();
        spacer.setPreferredSize(new Dimension(10, 5));
        this.add(spacer);
        this.add(midpanel);
        final JPanel spacer2 = new JPanel();
        spacer2.setPreferredSize(new Dimension(10, 5));
        this.add(spacer2);
        final JPanel warningpanel = new JPanel();
        warningpanel.setPreferredSize(new Dimension(600, 32));
        warningpanel.setLayout(new BoxLayout(warningpanel, 0));
        final JLabel warning = new JLabel("Please note: changing the number of filter bits may affect your write-to bit selection above.");
        warning.setPreferredSize(new Dimension(530, 26));
        warningpanel.add(warning);
        this.add(warningpanel);
        this.mWBPanel = wbpanel;
        this.actionPerformed(null);
    }
    
    public void actionPerformed(final ActionEvent e) {
        final int bitstaken = this.mBitBox.getSelectedIndex() + 2;
        final int bitsleft = 8 - bitstaken;
        this.mWBPanel.setAllowedBits(bitsleft);
    }
    
    public void changeFilterBits(final int change) {
        this.mBitBox.setSelectedIndex(change - 2);
        final int bitstaken = this.mBitBox.getSelectedIndex() + 2;
        final int bitsleft = 8 - bitstaken;
        this.mWBPanel.setAllowedBits(bitsleft);
    }
    
    public Filter getFilter() {
        try {
            final Filter filter = (Filter)Class.forName("invisibleinktoolkit.filters." + (String)this.mFilterBox.getSelectedItem()).newInstance();
            filter.setStartRange(8 - (this.mBitBox.getSelectedIndex() + 2));
            filter.setEndRange(8);
            return filter;
        }
        catch (Exception exp) {
            exp.printStackTrace();
            System.exit(1);
            return new Laplace(1, 8);
        }
    }
    
    public void setEnabled(final boolean enabled) {
        this.mFilterBox.setEnabled(enabled);
        this.mBitBox.setEnabled(enabled);
        this.mWBPanel.setEnabled(enabled);
        super.setEnabled(enabled);
    }
}
