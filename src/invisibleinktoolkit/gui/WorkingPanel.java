// 
// Decompiled by Procyon v0.5.36
// 

package invisibleinktoolkit.gui;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JProgressBar;
import java.awt.Component;
import javax.swing.JLabel;
import java.awt.LayoutManager;
import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import java.awt.Color;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JWindow;

public class WorkingPanel
{
    private JWindow mDialog;
    private JButton mCancelButton;
    
    public WorkingPanel() {
        final Frame parent = null;
        this.mDialog = new JWindow(parent);
        final JPanel contentPanel = new JPanel();
        contentPanel.setBorder(new LineBorder(Color.BLACK));
        contentPanel.setPreferredSize(new Dimension(200, 85));
        contentPanel.setLayout(new BorderLayout());
        final JLabel label = new JLabel("    WORKING, PLEASE WAIT...  ");
        label.setPreferredSize(new Dimension(200, 40));
        (this.mCancelButton = new JButton("Exit DIIT")).setPreferredSize(new Dimension(200, 30));
        contentPanel.add(label, "North");
        final JProgressBar movingThing = new JProgressBar();
        movingThing.setIndeterminate(true);
        movingThing.setPreferredSize(new Dimension(200, 15));
        contentPanel.add(movingThing, "Center");
        contentPanel.add(this.mCancelButton, "South");
        this.mCancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent ae) {
                System.exit(1);
            }
        });
        this.mDialog.getContentPane().add(contentPanel);
        this.mDialog.pack();
        this.mDialog.repaint();
        this.mDialog.setLocationRelativeTo(parent);
    }
    
    public void show() {
        this.mDialog.setVisible(true);
    }
    
    public void hide() {
        this.mDialog.setVisible(false);
    }
}
