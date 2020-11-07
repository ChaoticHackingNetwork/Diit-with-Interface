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
import invisibleinktoolkit.util.PRandom;
import invisibleinktoolkit.stego.StegoImage;
import invisibleinktoolkit.stego.CoverImage;
import invisibleinktoolkit.stego.InsertableMessage;
import invisibleinktoolkit.stego.StegoAlgorithm;

public class HideSeek implements StegoAlgorithm
{
    private boolean mLSBMatch;
    private int mStartBits;
    private int mEndBits;
    
    public HideSeek() throws IllegalArgumentException {
        this(0, 0);
    }
    
    public HideSeek(final int startbits, final int endbits) throws IllegalArgumentException {
        if (startbits > 7 || startbits < 0) {
            throw new IllegalArgumentException("Start bit range not in range 0-7!");
        }
        if (endbits > 7 || endbits < 0) {
            throw new IllegalArgumentException("End bit range not in range 1-7!");
        }
        if (startbits > endbits) {
            throw new IllegalArgumentException("End bit range must be higher than start range!");
        }
        this.mStartBits = startbits;
        this.mEndBits = endbits;
    }
    
    public StegoImage encode(final InsertableMessage message, final CoverImage cimage, final long seed) throws IOException, IllegalArgumentException {
        final int imgX = cimage.getImage().getWidth();
        final int imgY = cimage.getImage().getHeight();
        final boolean[][][][] haveWritten = new boolean[imgX][imgY][3][8];
        for (int i = 0; i < imgX; ++i) {
            for (int j = 0; j < imgY; ++j) {
                for (int k = 0; k < 8; ++k) {
                    haveWritten[i][j][0][k] = false;
                    haveWritten[i][j][1][k] = false;
                    haveWritten[i][j][2][k] = false;
                }
            }
        }
        if (!this.willMessageFit(message, cimage)) {
            throw new IllegalArgumentException("Message is too big for this image!");
        }
        final PRandom rgen = new PRandom(seed, imgX, imgY, cimage.getLayerCount(), this.mStartBits, this.mEndBits);
        final int messagesize = (int)message.getSize();
        final Random aran = new Random(seed);
        for (int l = 0; l < 32; ++l) {
            Shot sh;
            for (sh = rgen.getShot(); haveWritten[sh.getX()][sh.getY()][sh.getLayer()][sh.getBitPosition()]; sh = rgen.getShot()) {}
            haveWritten[sh.getX()][sh.getY()][sh.getLayer()][sh.getBitPosition()] = true;
            final boolean bit = (messagesize >> l & 0x1) == 0x1;
            if (!this.mLSBMatch) {
                cimage.setPixelBit(sh.getX(), sh.getY(), sh.getLayer(), sh.getBitPosition(), bit);
            }
            else {
                cimage.matchPixelBit(sh.getX(), sh.getY(), sh.getLayer(), 8, bit, aran.nextBoolean());
            }
        }
        while (message.notFinished()) {
            Shot sh;
            for (sh = rgen.getShot(); haveWritten[sh.getX()][sh.getY()][sh.getLayer()][sh.getBitPosition()]; sh = rgen.getShot()) {}
            final boolean bit2 = message.nextBit();
            if (!this.mLSBMatch) {
                cimage.setPixelBit(sh.getX(), sh.getY(), sh.getLayer(), sh.getBitPosition(), bit2);
            }
            else {
                cimage.matchPixelBit(sh.getX(), sh.getY(), sh.getLayer(), 8, bit2, aran.nextBoolean());
            }
            haveWritten[sh.getX()][sh.getY()][sh.getLayer()][sh.getBitPosition()] = true;
        }
        return new StegoImage(cimage.getImage());
    }
    
    public RetrievedMessage decode(final StegoImage simage, final long seed, final String path) throws IOException, NoMessageException {
        final int imgX = simage.getImage().getWidth();
        final int imgY = simage.getImage().getHeight();
        final boolean[][][][] haveWritten = new boolean[imgX][imgY][3][8];
        for (int i = 0; i < imgX; ++i) {
            for (int j = 0; j < imgY; ++j) {
                for (int k = 0; k < 8; ++k) {
                    haveWritten[i][j][0][k] = false;
                    haveWritten[i][j][1][k] = false;
                    haveWritten[i][j][2][k] = false;
                }
            }
        }
        final PRandom rgen = new PRandom(seed, imgX, imgY, simage.getLayerCount(), this.mStartBits, this.mEndBits);
        int size = 0;
        for (int l = 0; l < 32; ++l) {
            Shot sh;
            for (sh = rgen.getShot(); haveWritten[sh.getX()][sh.getY()][sh.getLayer()][sh.getBitPosition()]; sh = rgen.getShot()) {}
            haveWritten[sh.getX()][sh.getY()][sh.getLayer()][sh.getBitPosition()] = true;
            final int bit = simage.getPixelBit(sh.getX(), sh.getY(), sh.getLayer(), sh.getBitPosition());
            size = (size << 1 | bit);
        }
        int size2 = 0;
        for (int m = 0; m < 32; ++m) {
            size2 = (size2 << 1 | (size >> m & 0x1));
        }
        size2 *= 8;
        final long imagespace = simage.getImage().getWidth() * simage.getImage().getHeight() * simage.getLayerCount() * (this.mEndBits - this.mStartBits + 1);
        if (size2 >= imagespace || size2 < 0) {
            throw new NoMessageException();
        }
        final RetrievedMessage rmess = new RetrievedMessage(path);
        for (int k2 = 0; k2 < size2; ++k2) {
            Shot sh;
            for (sh = rgen.getShot(); haveWritten[sh.getX()][sh.getY()][sh.getLayer()][sh.getBitPosition()]; sh = rgen.getShot()) {}
            haveWritten[sh.getX()][sh.getY()][sh.getLayer()][sh.getBitPosition()] = true;
            rmess.setNext(simage.getPixelBit(sh.getX(), sh.getY(), sh.getLayer(), sh.getBitPosition()) == 1);
        }
        rmess.close();
        return rmess;
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
        final int height = simage.getImage().getHeight();
        final int width = simage.getImage().getWidth();
        final boolean[][][][] haveWritten = new boolean[width][height][3][8];
        for (int i = 0; i < width; ++i) {
            for (int j = 0; j < height; ++j) {
                for (int k = 0; k < 8; ++k) {
                    haveWritten[i][j][0][k] = false;
                    haveWritten[i][j][1][k] = false;
                    haveWritten[i][j][2][k] = false;
                }
            }
        }
        final PRandom rgen = new PRandom(seed, width, height, simage.getLayerCount(), this.mStartBits, this.mEndBits);
        final BufferedImage image = simage.getImage();
        final int black = 0;
        for (int l = 0; l < height; ++l) {
            for (int m = 0; m < width; ++m) {
                image.setRGB(m, l, black);
            }
        }
        for (int l = 0; l < 32; ++l) {
            Shot sh;
            for (sh = rgen.getShot(); haveWritten[sh.getX()][sh.getY()][sh.getLayer()][sh.getBitPosition()]; sh = rgen.getShot()) {}
            haveWritten[sh.getX()][sh.getY()][sh.getLayer()][sh.getBitPosition()] = true;
            image.setRGB(sh.getX(), sh.getY(), this.decreaseDarkness(image.getRGB(sh.getX(), sh.getY())));
        }
        while (message.notFinished()) {
            Shot sh;
            for (sh = rgen.getShot(); haveWritten[sh.getX()][sh.getY()][sh.getLayer()][sh.getBitPosition()]; sh = rgen.getShot()) {}
            haveWritten[sh.getX()][sh.getY()][sh.getLayer()][sh.getBitPosition()] = true;
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
        return "HideSeek randomly picks a pixel/colour/bit and hides there.\nIf it picks a bit it's written to before, it will skip over it\nand go onto to the next randomly selected bit.";
    }
    
    public void setMatch(final boolean shouldMatch) {
        this.mLSBMatch = shouldMatch;
    }
    
    public boolean getMatch() {
        return this.mLSBMatch;
    }
}
