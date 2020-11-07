// 
// Decompiled by Procyon v0.5.36
// 

package invisibleinktoolkit.util;

import java.io.File;
import javax.swing.filechooser.FileFilter;

public class InputImagesFileFilter extends FileFilter
{
    public boolean accept(final File afile) {
        if (afile != null) {
            if (afile.isDirectory()) {
                return true;
            }
            try {
                if (this.getFileExtension(afile).equalsIgnoreCase(".BMP") || this.getFileExtension(afile).equalsIgnoreCase(".JPG") || this.getFileExtension(afile).equalsIgnoreCase(".PNG")) {
                    return true;
                }
            }
            catch (Exception ex) {}
        }
        return false;
    }
    
    public String getFileExtension(final File afile) {
        return (afile != null) ? ((afile.getName().lastIndexOf(46) > 0 && afile.getName().lastIndexOf(46) < afile.getName().length() - 1) ? afile.getName().substring(afile.getName().lastIndexOf(46)).toLowerCase() : null) : null;
    }
    
    public String getDescription() {
        return "Image files (.jpg .bmp .png)";
    }
}
