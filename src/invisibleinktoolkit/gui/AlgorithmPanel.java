// 
// Decompiled by Procyon v0.5.36
// 

package invisibleinktoolkit.gui;

import invisibleinktoolkit.filters.Filter;
import invisibleinktoolkit.filters.Filterable;
import javax.swing.JOptionPane;
import java.awt.event.ActionEvent;
import invisibleinktoolkit.algorithms.BattleSteg;
import java.util.Vector;
import java.awt.Component;
import java.awt.Dimension;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import java.awt.LayoutManager;
import java.awt.Container;
import javax.swing.BoxLayout;
import invisibleinktoolkit.stego.StegoAlgorithm;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import java.awt.Frame;
import java.awt.event.ActionListener;
import javax.swing.JPanel;

public class AlgorithmPanel extends JPanel implements ActionListener
{
    private Frame mParent;
    private JLabel mLabel;
    private JComboBox mComboBox;
    private JButton mButton;
    private JButton mHelpButton;
    private StegoAlgorithm mAlgorithm;
    private Embedder mEmbedder;
    private static final long serialVersionUID = 0L;
    
    public AlgorithmPanel(final Frame parent) {
        this(parent, null);
    }
    
    public AlgorithmPanel(final Frame parent, final Embedder embed) {
        this.setLayout(new BoxLayout(this, 0));
        this.setBorder(new TitledBorder("Select an algorithm to use"));
        this.setPreferredSize(new Dimension(740, 50));
        (this.mLabel = new JLabel("Select an algorithm:   ")).setPreferredSize(new Dimension(150, 26));
        this.add(this.mLabel);
        final Vector algorithms = new Vector();
        algorithms.add("BattleSteg");
        algorithms.add("BlindHide");
        algorithms.add("DynamicBattleSteg");
        algorithms.add("DynamicFilterFirst");
        algorithms.add("FilterFirst");
        algorithms.add("HideSeek");
        (this.mComboBox = new JComboBox(algorithms)).setEditable(false);
        this.mComboBox.setToolTipText("The algorithm to be used for hiding");
        this.mComboBox.addActionListener(this);
        this.add(this.mComboBox);
        (this.mHelpButton = new JButton("?")).setActionCommand("Explain");
        this.mHelpButton.setToolTipText("What does this algorithm do?");
        this.mHelpButton.setPreferredSize(new Dimension(40, 26));
        this.mHelpButton.addActionListener(this);
        this.add(this.mHelpButton);
        (this.mButton = new JButton("Options")).setToolTipText("Set the options for the algorithm");
        this.mButton.setPreferredSize(new Dimension(100, 26));
        this.mButton.addActionListener(this);
        this.add(this.mButton);
        this.mAlgorithm = new BattleSteg();
        this.mParent = parent;
        this.mEmbedder = embed;
    }
    
    public void actionPerformed(final ActionEvent e) {
        if (e.getActionCommand().equalsIgnoreCase("comboBoxChanged")) {
            final String algo = "invisibleinktoolkit.algorithms." + (String)this.mComboBox.getSelectedItem();
            try {
                this.mAlgorithm = (StegoAlgorithm)Class.forName(algo).newInstance();
            }
            catch (Exception exp) {
                exp.printStackTrace();
                System.exit(1);
            }
        }
        else if (e.getActionCommand().equalsIgnoreCase("options")) {
            this.mAlgorithm.openConfigurationWindow(this.mParent);
        }
        else if (e.getActionCommand().equalsIgnoreCase("explain")) {
            JOptionPane.showMessageDialog(this, this.mAlgorithm.explainMe(), this.mAlgorithm.getClass().getSimpleName(), 1);
        }
        if (this.mEmbedder != null) {
            this.mEmbedder.updateEmbeddingRate();
        }
    }
    
    public StegoAlgorithm getAlgorithm() {
        return this.mAlgorithm;
    }
    
    public String getAlgorithmName() {
        String name = this.mAlgorithm.getClass().getName();
        name = name.substring(name.lastIndexOf(46) + 1);
        if (this.mAlgorithm instanceof Filterable) {
            final Filter filt = ((Filterable)this.mAlgorithm).getFilter();
            String filtername = filt.getClass().getName();
            filtername = filtername.substring(filtername.lastIndexOf(46) + 1);
            name = String.valueOf(name) + "-" + filtername;
        }
        return name;
    }
}
