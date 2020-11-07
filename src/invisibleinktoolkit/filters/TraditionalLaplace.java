// 
// Decompiled by Procyon v0.5.36
// 

package invisibleinktoolkit.filters;

import java.awt.image.BufferedImage;

public class TraditionalLaplace implements Filter
{
    private BufferedImage mImage;
    private int mStartRange;
    private int mEndRange;
    
    public TraditionalLaplace() {
        this(null, 1, 8);
    }
    
    public TraditionalLaplace(final int startbits, final int endbits) {
        this(null, startbits, endbits);
    }
    
    public TraditionalLaplace(final BufferedImage image, final int startbits, final int endbits) {
        this.mStartRange = startbits;
        this.mEndRange = endbits;
        this.mImage = image;
    }
    
    public int getValue(final int xpos, final int ypos) throws Exception {
        if (this.mImage == null) {
            throw new Exception("No image has been set!");
        }
        final int pixelval = this.mImage.getRGB(xpos, ypos);
        final int imgheight = this.mImage.getHeight();
        final int imgwidth = this.mImage.getWidth();
        int leftpix = 0;
        int uppix = 0;
        int rightpix = 0;
        int downpix = 0;
        int pixcount = 4;
        if (xpos <= 0) {
            --pixcount;
        }
        else {
            leftpix = this.mImage.getRGB(xpos - 1, ypos);
        }
        if (xpos >= imgwidth - 1) {
            --pixcount;
        }
        else {
            rightpix = this.mImage.getRGB(xpos + 1, ypos);
        }
        if (ypos <= 0) {
            --pixcount;
        }
        else {
            uppix = this.mImage.getRGB(xpos, ypos - 1);
        }
        if (ypos >= imgheight - 1) {
            --pixcount;
        }
        else {
            downpix = this.mImage.getRGB(xpos, ypos + 1);
        }
        final int value = pixcount * this.getLuminance(pixelval) - (this.getLuminance(leftpix) + this.getLuminance(rightpix) + this.getLuminance(uppix) + this.getLuminance(downpix));
        return value;
    }
    
    protected int getLuminance(final int pixel) {
        return (int)(0.299 * this.getRed(pixel) + 0.587 * this.getGreen(pixel) + 0.114 * this.getBlue(pixel));
    }
    
    protected int getRed(final int pixel) {
        return pixel >> 16 & this.getByteMask();
    }
    
    protected int getGreen(final int pixel) {
        return pixel >> 8 & this.getByteMask();
    }
    
    protected int getBlue(final int pixel) {
        return pixel & this.getByteMask();
    }
    
    protected int getByteMask() {
        int abyte = 0;
        int abyte2 = 0;
        for (int i = 0; i < 8; ++i) {
            byte bit;
            if (i <= this.mEndRange && i >= this.mStartRange) {
                bit = 1;
            }
            else {
                bit = 0;
            }
            abyte = (byte)(abyte << 1 | bit);
        }
        for (int i = 0; i < 8; ++i) {
            abyte2 = (abyte2 << 1 | (abyte >> i & 0x1));
        }
        return abyte2;
    }
    
    public void setImage(final BufferedImage image) {
        this.mImage = image;
    }
    
    public void setStartRange(final int startrange) {
        this.mStartRange = startrange;
    }
    
    public void setEndRange(final int endrange) {
        this.mEndRange = endrange;
    }
    
    public int getStartRange() {
        return this.mStartRange;
    }
    
    public int getEndRange() {
        return this.mEndRange;
    }
}
