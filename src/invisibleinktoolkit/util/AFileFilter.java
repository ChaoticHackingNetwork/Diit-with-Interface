// 
// Decompiled by Procyon v0.5.36
// 

package invisibleinktoolkit.util;

import java.io.File;
import javax.swing.filechooser.FileFilter;

public class AFileFilter extends FileFilter
{
    private String mExtension;
    private String mDescription;
    
    public AFileFilter(final String extension, final String description) {
        this.mDescription = description;
        this.mExtension = extension;
    }
    
    public boolean accept(final File afile) {
        if (this.mExtension == null) {
            return true;
        }
        if (afile != null) {
            if (afile.isDirectory()) {
                return true;
            }
            try {
                if (this.getFileExtension(afile).equalsIgnoreCase(this.mExtension)) {
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
    
    public String getExtension() {
        return this.mExtension;
    }
    
    public String getDescription() {
        return (this.mExtension == null) ? "All files" : this.mDescription;
    }
}
