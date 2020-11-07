// 
// Decompiled by Procyon v0.5.36
// 

package invisibleinktoolkit.gui;

import invisibleinktoolkit.stego.StegoAlgorithm;
import invisibleinktoolkit.stego.CoverImage;
import invisibleinktoolkit.stego.InsertableMessage;
import java.awt.Component;
import javax.swing.UIManager;
import java.awt.Color;
import java.awt.Dimension;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import java.awt.LayoutManager;
import java.awt.Container;
import javax.swing.BoxLayout;
import javax.swing.JProgressBar;
import javax.swing.JPanel;

public class CapacityPanel extends JPanel
{
    private JProgressBar mCapacityBar;
    private static final long serialVersionUID = 0L;
    
    public CapacityPanel() {
        this.setLayout(new BoxLayout(this, 0));
        this.setBorder(new TitledBorder("Current Embedding Rate"));
        this.setPreferredSize(new Dimension(740, 50));
        UIManager.put("ProgressBar.selectionForeground", Color.BLACK);
        UIManager.put("ProgressBar.selectionBackground", Color.BLACK);
        (this.mCapacityBar = new JProgressBar(0, 100)).setValue(0);
        this.mCapacityBar.setStringPainted(true);
        this.mCapacityBar.setToolTipText("Aim for less than 10%");
        this.add(this.mCapacityBar);
    }
    
    public void setValue(final InsertableMessage im, final CoverImage ci, final StegoAlgorithm sa) {
        try {
            final long size = im.getSize();
            final int imgx = ci.getImage().getWidth();
            final int imgy = ci.getImage().getHeight();
            final int layercount = ci.getLayerCount();
            final int bitspace = sa.getEndBits() - sa.getStartBits() + 1;
            final float space = (float)(imgx * imgy * layercount * bitspace);
            float percent;
            if (size * 8L > space) {
                percent = 100.0f;
            }
            else {
                percent = size * 8L / space * 100.0f;
            }
            if (percent <= 90.0f) {
                this.remove(this.mCapacityBar);
                UIManager.put("ProgressBar.foreground", Color.GREEN);
                (this.mCapacityBar = new JProgressBar(0, 100)).setStringPainted(true);
                this.mCapacityBar.setToolTipText("Aim for less than 10%");
                this.mCapacityBar.setValue(Math.round(percent));
                this.add(this.mCapacityBar);
            }
            else {
                this.remove(this.mCapacityBar);
                UIManager.put("ProgressBar.foreground", Color.RED);
                (this.mCapacityBar = new JProgressBar(0, 100)).setStringPainted(true);
                this.mCapacityBar.setValue(100);
                this.mCapacityBar.setString("Not enough space to hide selected message in selected cover.");
                this.mCapacityBar.setToolTipText("Try changing the message, cover image, algorithm or algorithm options");
                this.add(this.mCapacityBar);
            }
        }
        catch (Exception e) {}
    }
}
