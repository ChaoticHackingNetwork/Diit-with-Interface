// 
// Decompiled by Procyon v0.5.36
// 

package invisibleinktoolkit.filters;

import java.awt.image.BufferedImage;

public class Laplace implements Filter
{
    private BufferedImage mImage;
    private int mStartRange;
    private int mEndRange;
    
    public Laplace() {
        this(null, 1, 8);
    }
    
    public Laplace(final int startbits, final int endbits) {
        this(null, startbits, endbits);
    }
    
    public Laplace(final BufferedImage image, final int startbits, final int endbits) {
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
        final int reddiff = this.getRed(pixelval) * pixcount - (this.getRed(leftpix) + this.getRed(rightpix) + this.getRed(uppix) + this.getRed(downpix));
        final int greendiff = this.getGreen(pixelval) * pixcount - (this.getGreen(leftpix) + this.getGreen(rightpix) + this.getGreen(uppix) + this.getGreen(downpix));
        final int bluediff = this.getBlue(pixelval) * pixcount - (this.getBlue(leftpix) + this.getBlue(rightpix) + this.getBlue(uppix) + this.getBlue(downpix));
        return Math.abs(reddiff) + Math.abs(greendiff) + Math.abs(bluediff);
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
