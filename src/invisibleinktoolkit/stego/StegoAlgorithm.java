// 
// Decompiled by Procyon v0.5.36
// 

package invisibleinktoolkit.stego;

import java.awt.Frame;
import java.awt.image.BufferedImage;
import java.io.IOException;

public interface StegoAlgorithm
{
    StegoImage encode(final InsertableMessage p0, final CoverImage p1, final long p2) throws IOException;
    
    RetrievedMessage decode(final StegoImage p0, final long p1, final String p2) throws IOException, NoMessageException;
    
    BufferedImage outputSimulation(final InsertableMessage p0, final CoverImage p1, final long p2) throws IOException;
    
    int getStartBits();
    
    int getEndBits();
    
    void setEndBits(final int p0);
    
    void setStartBits(final int p0);
    
    void openConfigurationWindow(final Frame p0);
    
    String explainMe();
    
    void setMatch(final boolean p0);
    
    boolean getMatch();
}
