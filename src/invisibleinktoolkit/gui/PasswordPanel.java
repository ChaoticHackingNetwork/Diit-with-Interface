// 
// Decompiled by Procyon v0.5.36
// 

package invisibleinktoolkit.gui;

import java.security.MessageDigest;
import java.awt.Component;
import java.awt.Dimension;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import java.awt.LayoutManager;
import java.awt.Container;
import javax.swing.BoxLayout;
import javax.swing.JPasswordField;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class PasswordPanel extends JPanel
{
    private static char[] hexChar;
    private JLabel mLabel;
    private JPasswordField mPasswordField;
    private static final long serialVersionUID = 0L;
    
    static {
        PasswordPanel.hexChar = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
    }
    
    public PasswordPanel(final String text) {
        this.setLayout(new BoxLayout(this, 0));
        this.setBorder(new TitledBorder(text));
        this.setPreferredSize(new Dimension(740, 50));
        (this.mLabel = new JLabel(String.valueOf(text) + ":  ")).setPreferredSize(new Dimension(150, 26));
        this.add(this.mLabel);
        (this.mPasswordField = new JPasswordField(50)).setToolTipText("The password for hiding");
        this.add(this.mPasswordField);
    }
    
    public long getPassword() {
        final String pass = new String(this.mPasswordField.getPassword());
        if (pass == "") {
            return 0L;
        }
        try {
            final byte[] passbytes = pass.getBytes();
            final MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(passbytes);
            final byte[] md5sum = digest.digest();
            String smd5sum = toHexString(md5sum);
            smd5sum = smd5sum.substring(0, 15);
            return Long.parseLong(smd5sum, 16);
        }
        catch (Exception e) {
            return 0L;
        }
    }
    
    private static String toHexString(final byte[] bytestring) {
        final StringBuffer sb = new StringBuffer(bytestring.length * 2);
        for (int i = 0; i < bytestring.length; ++i) {
            sb.append(PasswordPanel.hexChar[(bytestring[i] & 0xF0) >>> 4]);
            sb.append(PasswordPanel.hexChar[bytestring[i] & 0xF]);
        }
        return sb.toString();
    }
}
