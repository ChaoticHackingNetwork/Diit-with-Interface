// 
// Decompiled by Procyon v0.5.36
// 

package invisibleinktoolkit.gui;

import java.awt.image.ImageObserver;
import java.awt.Image;
import java.awt.Graphics;
import javax.swing.JPanel;
import java.awt.image.BufferedImage;
import javax.swing.JOptionPane;
import java.awt.Component;
import javax.swing.JScrollPane;
import javax.imageio.ImageIO;
import java.io.File;
import java.awt.event.ActionEvent;
import java.awt.Dimension;
import javax.swing.JFrame;
import java.awt.event.ActionListener;
import javax.swing.JButton;

public class ViewButton extends JButton implements ActionListener
{
    private JFrame mImageViewer;
    private String mPath;
    private static final long serialVersionUID = 0L;
    
    public ViewButton(final String title) {
        super(title);
        this.setToolTipText("View selected image file");
        this.setPreferredSize(new Dimension(100, 26));
        this.addActionListener(this);
    }
    
    public void setImage(final String path) {
        this.mPath = path;
    }
    
    public void actionPerformed(final ActionEvent e) {
        if (this.mPath != null) {
            try {
                final BufferedImage img = ImageIO.read(new File(this.mPath));
                final ImagePanel panel = new ImagePanel(img);
                if (this.mImageViewer == null) {
                    (this.mImageViewer = new JFrame("Image Viewer")).setDefaultCloseOperation(2);
                    this.mImageViewer.setResizable(true);
                    final JScrollPane scrollpane = new JScrollPane(panel);
                    this.mImageViewer.getContentPane().add(scrollpane, "Center");
                    this.mImageViewer.setSize(620, 460);
                    this.mImageViewer.setLocation(20, 20);
                    this.mImageViewer.setVisible(true);
                }
                else {
                    this.mImageViewer.getContentPane().removeAll();
                    final JScrollPane scrollpane = new JScrollPane(panel);
                    this.mImageViewer.getContentPane().add(scrollpane, "Center");
                    this.mImageViewer.setSize(620, 460);
                    this.mImageViewer.setLocation(20, 20);
                    this.mImageViewer.setVisible(true);
                }
            }
            catch (Exception exp) {
                JOptionPane.showMessageDialog(null, "Image file is not a valid type!", "Error! Wrong image filetype!", 0);
            }
        }
    }
    
    private class ImagePanel extends JPanel
    {
        private BufferedImage image;
        private Dimension size;
        private static final long serialVersionUID = 0L;
        
        public ImagePanel(final BufferedImage image) {
            this.image = image;
            this.setOpaque(true);
            this.size = new Dimension(image.getWidth(), image.getHeight());
        }
        
        protected void paintComponent(final Graphics g) {
            super.paintComponent(g);
            final int w = this.getWidth();
            final int h = this.getHeight();
            final int x = (w - this.size.width) / 2;
            final int y = (h - this.size.height) / 2;
            g.drawImage(this.image, x, y, this);
        }
        
        public Dimension getPreferredSize() {
            return this.size;
        }
    }
}
