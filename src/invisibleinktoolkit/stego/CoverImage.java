// 
// Decompiled by Procyon v0.5.36
// 

package invisibleinktoolkit.stego;

import java.io.IOException;
import javax.imageio.ImageIO;
import java.io.File;
import java.awt.image.BufferedImage;

public class CoverImage
{
    private BufferedImage mCover;
    
    public CoverImage(final String path) throws IOException, IllegalArgumentException, NullPointerException {
        this.mCover = ImageIO.read(new File(path));
        if (this.mCover == null) {
            throw new IllegalArgumentException("File type is not a recognisable type.");
        }
        if (this.getLayerCount() <= 1 || this.mCover.getType() == 9 || this.mCover.getType() == 8) {
            throw new IllegalArgumentException("Picture colour depth is not deep enough!");
        }
    }
    
    public int getLayerCount() {
        final int type = this.mCover.getType();
        if (type == 12) {
            return 0;
        }
        if (type == 13 || type == 10 || type == 11) {
            return 1;
        }
        return 3;
    }
    
    public BufferedImage getImage() {
        return this.mCover;
    }
    
    public void setPixelBit(final int xpos, final int ypos, final int layer, final int bitpos, final boolean newbit) throws IllegalArgumentException {
        if ((this.mCover.getType() == 9 || this.mCover.getType() == 8) && bitpos > 4) {
            throw new IllegalArgumentException("Bit position in incorrect position for image (0-4)!");
        }
        if (bitpos > 7 || bitpos < 0) {
            throw new IllegalArgumentException("Bit position in incorrect position for image (0-7)!");
        }
        if (layer > this.getLayerCount() || layer < 0) {
            throw new IllegalArgumentException("Layer is incorrect for image type!");
        }
        final int pixel = this.mCover.getRGB(xpos, ypos);
        int newcolour = 0;
        int newpixel;
        if (newbit) {
            newcolour = 1;
            newcolour <<= bitpos + layer * 8;
            newpixel = (pixel | newcolour);
        }
        else {
            newcolour = -2;
            for (int i = 0; i < bitpos + layer * 8; ++i) {
                newcolour = (newcolour << 1 | 0x1);
            }
            newpixel = (pixel & newcolour);
        }
        this.mCover.setRGB(xpos, ypos, newpixel);
    }
    
    public void matchPixelBit(final int x, final int y, final int layer, final int maxChangePosition, final boolean newbit, final boolean subtract) throws IllegalArgumentException {
        if (layer > this.getLayerCount() || layer < 0) {
            throw new IllegalArgumentException("Layer is incorrect for image type!");
        }
        final int pixel = this.mCover.getRGB(x, y);
        final byte thiscolour = (byte)(pixel >> layer * 8 & 0xFF);
        if (((thiscolour & 0x1) == 0x1 && newbit) || ((thiscolour & 0x1) == 0x0 && !newbit)) {
            return;
        }
        int leftmask = 0;
        int rightmask = 0;
        rightmask |= 0xFF;
        for (int i = 0; i < maxChangePosition; ++i) {
            leftmask = (leftmask << 1 | 0x1);
            rightmask = (rightmask << 1 & 0xFE);
        }
        final int tochange = thiscolour & leftmask;
        final int top = thiscolour & rightmask;
        int newcolour;
        if (subtract) {
            newcolour = (tochange & 0xFF) - 1;
        }
        else {
            newcolour = (tochange & 0xFF) + 1;
        }
        if (newcolour > leftmask && newbit) {
            newcolour = leftmask;
        }
        else if (newcolour > leftmask && !newbit) {
            newcolour = leftmask - 1;
        }
        if (newcolour < 0 && newbit) {
            newcolour = 1;
        }
        if (newcolour < 0 && !newbit) {
            newcolour = 0;
        }
        newcolour = ((newcolour | top) & 0xFF);
        final byte[] colours = new byte[4];
        for (int j = 0; j < 4; ++j) {
            colours[j] = (byte)(pixel >> j * 8 & 0xFF);
        }
        colours[layer] = (byte)newcolour;
        int finalcolour = 0;
        for (int k = 3; k >= 0; --k) {
            finalcolour = ((finalcolour << 8 | (colours[k] & 0xFF)) & -1);
        }
        this.mCover.setRGB(x, y, finalcolour);
    }
}
