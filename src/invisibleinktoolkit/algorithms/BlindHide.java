// 
// Decompiled by Procyon v0.5.36
// 

package invisibleinktoolkit.algorithms;

import invisibleinktoolkit.algorithms.gui.StartEndWindow;
import java.awt.Frame;
import java.awt.image.BufferedImage;
import invisibleinktoolkit.stego.NoMessageException;
import invisibleinktoolkit.stego.RetrievedMessage;
import java.io.IOException;
import invisibleinktoolkit.util.Shot;
import java.util.Random;
import invisibleinktoolkit.stego.StegoImage;
import invisibleinktoolkit.stego.CoverImage;
import invisibleinktoolkit.stego.InsertableMessage;
import invisibleinktoolkit.stego.StegoAlgorithm;

public class BlindHide implements StegoAlgorithm
{
    private boolean mLSBMatch;
    private int mStartBits;
    private int mEndBits;
    private long mCountBits;
    
    public BlindHide(final int startbits, final int endbits) throws IllegalArgumentException {
        if (startbits > 6 || startbits < 0) {
            throw new IllegalArgumentException("Start bit range not in range 0-6!");
        }
        if (endbits > 6 || endbits < 0) {
            throw new IllegalArgumentException("End bit range not in range 1-6!");
        }
        if (startbits > endbits) {
            throw new IllegalArgumentException("End bit range must be higher than start range!");
        }
        this.mStartBits = startbits;
        this.mEndBits = endbits;
        this.mCountBits = 0L;
    }
    
    public BlindHide() throws IllegalArgumentException {
        this(0, 0);
    }
    
    public StegoImage encode(final InsertableMessage message, final CoverImage cimage, final long seed) throws IOException, IllegalArgumentException {
        if (!this.willMessageFit(message, cimage)) {
            throw new IllegalArgumentException("Message is too big for this image!");
        }
        this.mCountBits = 0L;
        final int messagesize = (int)message.getSize();
        final Random rgen = new Random(seed);
        for (int i = 0; i < 32; ++i) {
            final Shot sh = this.getShot(cimage.getImage().getHeight(), cimage.getImage().getWidth());
            final boolean bit = (messagesize >> i & 0x1) == 0x1;
            if (!this.mLSBMatch) {
                cimage.setPixelBit(sh.getX(), sh.getY(), sh.getLayer(), sh.getBitPosition(), bit);
            }
            else {
                cimage.matchPixelBit(sh.getX(), sh.getY(), sh.getLayer(), 8, bit, rgen.nextBoolean());
            }
        }
        while (message.notFinished()) {
            final Shot sh = this.getShot(cimage.getImage().getHeight(), cimage.getImage().getWidth());
            final boolean bit2 = message.nextBit();
            if (!this.mLSBMatch) {
                cimage.setPixelBit(sh.getX(), sh.getY(), sh.getLayer(), sh.getBitPosition(), bit2);
            }
            else {
                cimage.matchPixelBit(sh.getX(), sh.getY(), sh.getLayer(), 8, bit2, rgen.nextBoolean());
            }
        }
        return new StegoImage(cimage.getImage());
    }
    
    public RetrievedMessage decode(final StegoImage simage, final long seed, final String path) throws IOException, NoMessageException {
        int size = 0;
        this.mCountBits = 0L;
        for (int i = 0; i < 32; ++i) {
            final Shot sh = this.getShot(simage.getImage().getHeight(), simage.getImage().getWidth());
            final int bit = simage.getPixelBit(sh.getX(), sh.getY(), sh.getLayer(), sh.getBitPosition());
            size = (size << 1 | bit);
        }
        int size2 = 0;
        for (int j = 0; j < 32; ++j) {
            size2 = (size2 << 1 | (size >> j & 0x1));
        }
        size2 *= 8;
        final long imagespace = simage.getImage().getWidth() * simage.getImage().getHeight() * simage.getLayerCount() * (this.mEndBits - this.mStartBits + 1);
        if (size2 >= imagespace || size2 < 0) {
            throw new NoMessageException();
        }
        final RetrievedMessage rmess = new RetrievedMessage(path);
        for (int k = 0; k < size2; ++k) {
            final Shot sh = this.getShot(simage.getImage().getHeight(), simage.getImage().getWidth());
            rmess.setNext(simage.getPixelBit(sh.getX(), sh.getY(), sh.getLayer(), sh.getBitPosition()) == 1);
        }
        rmess.close();
        return rmess;
    }
    
    private Shot getShot(final int height, final int width) {
        final int bitsperpixel = this.mEndBits - this.mStartBits + 1;
        if (height * width * bitsperpixel * 3 < this.mCountBits) {
            return null;
        }
        final int rangeupto = (int)(this.mCountBits % (bitsperpixel * 3));
        final int xrow = (int)((this.mCountBits - rangeupto) / (bitsperpixel * 3) % width);
        final int yrow = (int)(((this.mCountBits - rangeupto) / (bitsperpixel * 3) - xrow) / width);
        final Shot sh = new Shot(xrow, yrow, rangeupto % bitsperpixel, (rangeupto - rangeupto % bitsperpixel) / bitsperpixel);
        ++this.mCountBits;
        return sh;
    }
    
    public boolean willMessageFit(final InsertableMessage message, final CoverImage image) throws IOException {
        final int imgX = image.getImage().getWidth();
        final int imgY = image.getImage().getHeight();
        final long imagespace = imgX * imgY * image.getLayerCount() * (this.mEndBits - this.mStartBits + 1);
        final int messagesize = (int)message.getSize() * 8 + 50;
        return messagesize <= imagespace;
    }
    
    public BufferedImage outputSimulation(final InsertableMessage message, final CoverImage simage, final long seed) throws IOException, IllegalArgumentException {
        if (!this.willMessageFit(message, simage)) {
            throw new IllegalArgumentException("Message is too big for this image!");
        }
        this.mCountBits = 0L;
        final BufferedImage image = simage.getImage();
        final int height = image.getHeight();
        final int width = image.getWidth();
        final int black = 0;
        for (int i = 0; i < height; ++i) {
            for (int j = 0; j < width; ++j) {
                image.setRGB(j, i, black);
            }
        }
        for (int i = 0; i < 32; ++i) {
            final Shot sh = this.getShot(height, width);
            image.setRGB(sh.getX(), sh.getY(), this.decreaseDarkness(image.getRGB(sh.getX(), sh.getY())));
        }
        while (message.notFinished()) {
            final Shot sh = this.getShot(height, width);
            image.setRGB(sh.getX(), sh.getY(), this.decreaseDarkness(image.getRGB(sh.getX(), sh.getY())));
            message.nextBit();
        }
        return image;
    }
    
    private int decreaseDarkness(final int colour) {
        return colour << 1 | 0xF0F0F0F;
    }
    
    public int getStartBits() {
        return this.mStartBits;
    }
    
    public int getEndBits() {
        return this.mEndBits;
    }
    
    public void setEndBits(final int newend) {
        this.mEndBits = newend;
    }
    
    public void setStartBits(final int newstart) {
        this.mStartBits = newstart;
    }
    
    public void openConfigurationWindow(final Frame parent) {
        new StartEndWindow(parent, this);
        parent.repaint();
    }
    
    public String explainMe() {
        return "Starts writing at (0,0) and moves along each pixel,\ncolour and bit in scan lines across the image.  Uses\npure steganography.";
    }
    
    public void setMatch(final boolean shouldMatch) {
        this.mLSBMatch = shouldMatch;
    }
    
    public boolean getMatch() {
        return this.mLSBMatch;
    }
}
