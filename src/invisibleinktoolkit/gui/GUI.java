// 
// Decompiled by Procyon v0.5.36
// 

package invisibleinktoolkit.gui;

import java.awt.Component;
import javax.swing.Icon;
import java.awt.Frame;
import javax.swing.JTabbedPane;
import javax.swing.JFrame;

public class GUI extends JFrame
{
    private static final long serialVersionUID = 0L;
    
    public GUI() {
        this("Digital Invisible Ink Toolkit");
    }
    
    public GUI(final String title) {
        super(title);
        JFrame.setDefaultLookAndFeelDecorated(true);
        this.setDefaultCloseOperation(3);
        final JTabbedPane program = new JTabbedPane();
        program.addTab("    Encode    ", null, new Encoder(this), "Encode a message onto an image");
        program.addTab("    Decode   ", null, new Decoder(this), "Decode a message from an image");
        program.addTab("   Simulate   ", null, new Simulator(this), "Simulate hiding a message on an image");
        program.addTab("   Analysis   ", null, new StegAnalyser(this), "Analyse a stego-image");
        program.setOpaque(true);
        this.getContentPane().add(program, "Center");
        this.pack();
        this.setResizable(false);
        this.setLocation(25, 25);
    }
}
