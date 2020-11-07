// 
// Decompiled by Procyon v0.5.36
// 

package invisibleinktoolkit.stego;

import java.io.IOException;
import java.awt.image.RenderedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.awt.image.BufferedImage;

public class StegoImage
{
    private BufferedImage mStego;
    
    public StegoImage(final BufferedImage image) throws NullPointerException, IllegalArgumentException {
        if (image == null) {
            throw new NullPointerException("Image must be set to a non-null value");
        }
        this.mStego = image;
    }
    
    public boolean write(final String formatname, final File output) throws IllegalArgumentException, IOException {
        return ImageIO.write(this.mStego, formatname, output);
    }
    
    public int getLayerCount() {
        final int type = this.mStego.getType();
        if (type == 12) {
            return 0;
        }
        if (type == 13 || type == 10 || type == 11) {
            return 1;
        }
        return 3;
    }
    
    public BufferedImage getImage() {
        return this.mStego;
    }
    
    public int getPixelBit(final int xpos, final int ypos, final int layer, final int bitpos) {
        final int pixel = this.mStego.getRGB(xpos, ypos);
        final int layerpos = layer * 8 + bitpos;
        return pixel >> layerpos & 0x1;
    }
}
