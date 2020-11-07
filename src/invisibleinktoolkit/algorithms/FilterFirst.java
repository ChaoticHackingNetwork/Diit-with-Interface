// 
// Decompiled by Procyon v0.5.36
// 

package invisibleinktoolkit.algorithms;

import java.util.Comparator;
import java.util.Arrays;
import invisibleinktoolkit.filters.FPComparator;
import invisibleinktoolkit.filters.FilteredPixel;
import invisibleinktoolkit.algorithms.gui.StartEndFilterWindow;
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
import invisibleinktoolkit.filters.Laplace;
import invisibleinktoolkit.filters.Filter;
import invisibleinktoolkit.filters.Filterable;
import invisibleinktoolkit.stego.StegoAlgorithm;

public class FilterFirst implements StegoAlgorithm, Filterable
{
    private boolean mLSBMatch;
    private int mStartBits;
    private int mEndBits;
    private Filter mFilter;
    
    public FilterFirst(final int startbits, final int endbits, final int moveaway, final int initshots, final int shotsincrease, final int shotrange, final Filter filter) throws IllegalArgumentException {
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
        this.mFilter = filter;
    }
    
    public FilterFirst() throws IllegalArgumentException {
        this(0, 0, 10, 5, 2, 5, new Laplace(1, 8));
    }
    
    public StegoImage encode(final InsertableMessage message, final CoverImage cimage, final long seed) throws IOException, IllegalArgumentException {
        if (!this.willMessageFit(message, cimage)) {
            throw new IllegalArgumentException("Message is too big for this image!");
        }
        final ShotPicker rgen = new ShotPicker(seed, this.mStartBits, this.mEndBits, cimage.getImage(), this.mFilter);
        final int messagesize = (int)message.getSize();
        final Random aran = new Random(seed);
        for (int i = 0; i < 32; ++i) {
            final Shot sh = rgen.getShot();
            final boolean bit = (messagesize >> i & 0x1) == 0x1;
            if (!this.mLSBMatch) {
                cimage.setPixelBit(sh.getX(), sh.getY(), sh.getLayer(), sh.getBitPosition(), bit);
            }
            else {
                cimage.matchPixelBit(sh.getX(), sh.getY(), sh.getLayer(), this.mFilter.getStartRange(), bit, aran.nextBoolean());
            }
        }
        while (message.notFinished()) {
            final Shot sh = rgen.getShot();
            final boolean bit2 = message.nextBit();
            if (!this.mLSBMatch) {
                cimage.setPixelBit(sh.getX(), sh.getY(), sh.getLayer(), sh.getBitPosition(), bit2);
            }
            else {
                cimage.matchPixelBit(sh.getX(), sh.getY(), sh.getLayer(), this.mFilter.getStartRange(), bit2, aran.nextBoolean());
            }
        }
        return new StegoImage(cimage.getImage());
    }
    
    public RetrievedMessage decode(final StegoImage simage, final long seed, final String path) throws IOException, NoMessageException {
        final ShotPicker rgen = new ShotPicker(seed, this.mStartBits, this.mEndBits, simage.getImage(), this.mFilter);
        int size = 0;
        for (int i = 0; i < 32; ++i) {
            final Shot sh = rgen.getShot();
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
            final Shot sh = rgen.getShot();
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
        final ShotPicker rgen = new ShotPicker(seed, this.mStartBits, this.mEndBits, simage.getImage(), this.mFilter);
        final BufferedImage image = simage.getImage();
        final int black = 0;
        for (int i = 0; i < image.getHeight(); ++i) {
            for (int j = 0; j < image.getWidth(); ++j) {
                image.setRGB(j, i, black);
            }
        }
        for (int i = 0; i < 32; ++i) {
            final Shot sh = rgen.getShot();
            image.setRGB(sh.getX(), sh.getY(), this.decreaseDarkness(image.getRGB(sh.getX(), sh.getY())));
        }
        while (message.notFinished()) {
            final Shot sh = rgen.getShot();
            image.setRGB(sh.getX(), sh.getY(), this.decreaseDarkness(image.getRGB(sh.getX(), sh.getY())));
            message.nextBit();
        }
        return image;
    }
    
    private int decreaseDarkness(final int colour) {
        return colour << 1 | 0xF0F0F0F;
    }
    
    public void setFilter(final Filter filter) {
        this.mFilter = filter;
    }
    
    public Filter getFilter() {
        return this.mFilter;
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
        new StartEndFilterWindow(parent, this);
        parent.repaint();
    }
    
    public String explainMe() {
        return "A pure steganography method, FilterFirst uses \nedge detecting filters to obtain an ordered list of\nthe best places to hide. It then writes to this list\nin the order given.";
    }
    
    public void setMatch(final boolean shouldMatch) {
        this.mLSBMatch = shouldMatch;
    }
    
    public boolean getMatch() {
        return this.mLSBMatch;
    }
    
    private class ShotPicker
    {
        private FilteredPixel[] fparray;
        private int mStartRange;
        private int mEndRange;
        private int mCountBits;
        
        public ShotPicker(final long seed, final int startrange, final int endrange, final BufferedImage image, final Filter filter) throws IllegalArgumentException {
            this.mStartRange = startrange;
            filter.setStartRange((this.mEndRange = endrange) + 1);
            filter.setEndRange(8);
            this.mCountBits = image.getHeight() * image.getWidth() * (this.mEndRange + 1 - this.mStartRange) * 3;
            try {
                this.generateList(image, filter);
            }
            catch (Exception e) {
                e.printStackTrace();
                System.exit(0);
            }
        }
        
        private void generateList(final BufferedImage image, final Filter filter) throws Exception {
            filter.setImage(image);
            this.fparray = new FilteredPixel[image.getWidth() * image.getHeight()];
            for (int i = 0; i < image.getWidth(); ++i) {
                for (int j = 0; j < image.getHeight(); ++j) {
                    this.fparray[i * image.getHeight() + j] = new FilteredPixel(i, j, Math.abs(filter.getValue(i, j)));
                }
            }
            Arrays.sort(this.fparray, new FPComparator());
        }
        
        public Shot getShot() {
            final int bitsperpixel = this.mEndRange - this.mStartRange + 1;
            final int rangeupto = this.mCountBits % (bitsperpixel * 3);
            final int arraypos = (this.mCountBits - rangeupto) / (bitsperpixel * 3) % this.fparray.length;
            final FilteredPixel fp = this.fparray[arraypos];
            final Shot sh = new Shot(fp.getX(), fp.getY(), rangeupto % bitsperpixel, (rangeupto - rangeupto % bitsperpixel) / bitsperpixel);
            --this.mCountBits;
            return sh;
        }
    }
}
