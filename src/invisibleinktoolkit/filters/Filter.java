// 
// Decompiled by Procyon v0.5.36
// 

package invisibleinktoolkit.filters;

import java.awt.image.BufferedImage;

public interface Filter
{
    int getValue(final int p0, final int p1) throws Exception;
    
    void setImage(final BufferedImage p0);
    
    void setStartRange(final int p0);
    
    void setEndRange(final int p0);
    
    int getStartRange();
    
    int getEndRange();
}
