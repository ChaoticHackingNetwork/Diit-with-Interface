// 
// Decompiled by Procyon v0.5.36
// 

package invisibleinktoolkit.filters;

import java.awt.image.BufferedImage;

public class Sobel implements Filter
{
    private BufferedImage mImage;
    private int mStartRange;
    private int mEndRange;
    
    public Sobel() {
        this(null, 1, 8);
    }
    
    public Sobel(final int startbits, final int endbits) {
        this(null, startbits, endbits);
    }
    
    public Sobel(final BufferedImage image, final int startbits, final int endbits) {
        this.mStartRange = startbits;
        this.mEndRange = endbits;
        this.mImage = image;
    }
    
    public int getValue(final int xpos, final int ypos) throws Exception {
        if (this.mImage == null) {
            throw new Exception("No image has been set!");
        }
        final int imgheight = this.mImage.getHeight();
        final int imgwidth = this.mImage.getWidth();
        int matrix1left = 0;
        int matrix1leftup = 0;
        int matrix1leftdown = 0;
        int matrix1right = 0;
        int matrix1rightup = 0;
        int matrix1rightdown = 0;
        int matrix2up = 0;
        int matrix2upleft = 0;
        int matrix2upright = 0;
        int matrix2down = 0;
        int matrix2downleft = 0;
        int matrix2downright = 0;
        if (xpos == 0 || ypos == 0 || ypos == imgheight - 1 || xpos == imgwidth - 1) {
            return 0;
        }
        if (xpos > 0) {
            matrix1left = this.mImage.getRGB(xpos - 1, ypos);
            if (ypos > 0) {
                matrix1leftup = this.mImage.getRGB(xpos - 1, ypos - 1);
                matrix2upleft = this.mImage.getRGB(xpos - 1, ypos - 1);
            }
            if (ypos < imgheight - 1) {
                matrix1leftdown = this.mImage.getRGB(xpos - 1, ypos + 1);
                matrix2downleft = this.mImage.getRGB(xpos - 1, ypos + 1);
            }
        }
        if (xpos < imgwidth - 1) {
            matrix1right = this.mImage.getRGB(xpos + 1, ypos);
            if (ypos > 0) {
                matrix1rightup = this.mImage.getRGB(xpos + 1, ypos - 1);
                matrix2upright = this.mImage.getRGB(xpos + 1, ypos - 1);
            }
            if (ypos < imgheight - 1) {
                matrix1rightdown = this.mImage.getRGB(xpos + 1, ypos + 1);
                matrix2downright = this.mImage.getRGB(xpos + 1, ypos + 1);
            }
        }
        if (ypos > 0) {
            matrix2up = this.mImage.getRGB(xpos, ypos - 1);
        }
        if (ypos < imgheight - 1) {
            matrix2down = this.mImage.getRGB(xpos, ypos + 1);
        }
        final int reddiff = (int)Math.sqrt(Math.pow(this.getRed(matrix1right) * 2 + this.getRed(matrix1rightup) + this.getRed(matrix1rightdown) - (this.getRed(matrix1left) * 2 + this.getRed(matrix1leftup) + this.getRed(matrix1leftdown)), 2.0) + Math.pow(this.getRed(matrix2up) * 2 + this.getRed(matrix2upleft) + this.getRed(matrix2upright) - (this.getRed(matrix2down) * 2 + this.getRed(matrix2downleft) + this.getRed(matrix2downright)), 2.0));
        final int greendiff = (int)Math.sqrt(Math.pow(this.getGreen(matrix1right) * 2 + this.getGreen(matrix1rightup) + this.getGreen(matrix1rightdown) - (this.getGreen(matrix1left) * 2 + this.getGreen(matrix1leftup) + this.getGreen(matrix1leftdown)), 2.0) + Math.pow(this.getGreen(matrix2up) * 2 + this.getGreen(matrix2upleft) + this.getGreen(matrix2upright) - (this.getGreen(matrix2down) * 2 + this.getGreen(matrix2downleft) + this.getGreen(matrix2downright)), 2.0));
        final int bluediff = (int)Math.sqrt(Math.pow(this.getBlue(matrix1right) * 2 + this.getBlue(matrix1rightup) + this.getBlue(matrix1rightdown) - (this.getBlue(matrix1left) * 2 + this.getBlue(matrix1leftup) + this.getBlue(matrix1leftdown)), 2.0) + Math.pow(this.getBlue(matrix2up) * 2 + this.getBlue(matrix2upleft) + this.getBlue(matrix2upright) - (this.getBlue(matrix2down) * 2 + this.getBlue(matrix2downleft) + this.getBlue(matrix2downright)), 2.0));
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
