// 
// Decompiled by Procyon v0.5.36
// 

package invisibleinktoolkit.algorithms;

import invisibleinktoolkit.filters.FilteredPixel;
import java.util.TreeSet;
import invisibleinktoolkit.filters.FPComparator;
import invisibleinktoolkit.util.PRandom;
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

public class DynamicBattleSteg extends BattleSteg implements StegoAlgorithm, Filterable
{
    private boolean mLSBMatch;
    private int mStartBits;
    private int mEndBits;
    private int mMoveAway;
    private int mInitShots;
    private int mShotsRange;
    private int mShotsIncrease;
    private Filter mFilter;
    
    public DynamicBattleSteg(final int startbits, final int endbits, final int moveaway, final int initshots, final int shotsincrease, final int shotrange, final Filter filter) throws IllegalArgumentException {
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
        this.mMoveAway = moveaway;
        this.mInitShots = initshots;
        this.mShotsIncrease = shotsincrease;
        this.mShotsRange = shotrange;
        this.mFilter = filter;
    }
    
    public DynamicBattleSteg() throws IllegalArgumentException {
        this(0, 0, 10, 5, 2, 1, new Laplace(1, 8));
    }
    
    public StegoImage encode(final InsertableMessage message, final CoverImage cimage, final long seed) throws IOException, IllegalArgumentException {
        if (!this.willMessageFit(message, cimage)) {
            throw new IllegalArgumentException("Message is too big for this image!");
        }
        final BPRandom rgen = new BPRandom(seed, cimage.getLayerCount(), this.mStartBits, this.mEndBits, cimage.getImage(), this.mMoveAway, this.mInitShots, this.mShotsIncrease, this.mShotsRange, this.mFilter);
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
        final BPRandom rgen = new BPRandom(seed, simage.getLayerCount(), this.mStartBits, this.mEndBits, simage.getImage(), this.mMoveAway, this.mInitShots, this.mShotsIncrease, this.mShotsRange, this.mFilter);
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
        final BPRandom rgen = new BPRandom(seed, simage.getLayerCount(), this.mStartBits, this.mEndBits, simage.getImage(), this.mMoveAway, this.mInitShots, this.mShotsIncrease, this.mShotsRange, this.mFilter);
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
    
    public String explainMe() {
        return "Works like BattleSteg only uses dynamic programming\nto prevent lots of memory from being used.  This is\nNOT compatible with normal BattleSteg.";
    }
    
    public void setMatch(final boolean shouldMatch) {
        this.mLSBMatch = shouldMatch;
    }
    
    public boolean getMatch() {
        return this.mLSBMatch;
    }
    
    private class BPRandom extends PRandom
    {
        private boolean[][][][] beenShot;
        private boolean[][] mShips;
        private int mNumShots;
        private int mMoveAwayFixed;
        private int mInitShots;
        private int mShotRange;
        private int mShotsIncrease;
        private int mMoveAway;
        
        public BPRandom(final long seed, final int numlayers, final int startrange, final int endrange, final BufferedImage image, final int moveaway, final int initshots, final int shotsincrease, final int shotrange, final Filter filter) throws IllegalArgumentException {
            super(seed, image.getWidth(), image.getHeight(), numlayers, startrange, endrange);
            this.mMoveAwayFixed = moveaway;
            this.mInitShots = initshots;
            this.mShotsIncrease = shotsincrease;
            this.mShotRange = shotrange;
            filter.setStartRange(endrange + 1);
            filter.setEndRange(8);
            this.beenShot = new boolean[image.getWidth()][image.getHeight()][3][8];
            for (int i = 0; i < image.getWidth(); ++i) {
                for (int j = 0; j < image.getHeight(); ++j) {
                    for (int k = 0; k < 8; ++k) {
                        this.beenShot[i][j][0][k] = false;
                        this.beenShot[i][j][1][k] = false;
                        this.beenShot[i][j][2][k] = false;
                    }
                }
            }
            this.mShips = new boolean[image.getWidth()][image.getHeight()];
            for (int i = 0; i < image.getWidth(); ++i) {
                for (int j = 0; j < image.getHeight(); ++j) {
                    this.mShips[i][j] = false;
                }
            }
            try {
                this.generateShips(image, filter);
            }
            catch (Exception e) {
                e.printStackTrace();
                System.exit(0);
            }
        }
        
        private void generateShips(final BufferedImage image, final Filter filter) throws Exception {
            filter.setImage(image);
            final int size = image.getWidth() * image.getHeight() / 10;
            System.gc();
            final TreeSet<FilteredPixel> sortedlist = new TreeSet(new FPComparator());
            for (int i = 0; i < image.getWidth(); ++i) {
                for (int j = 0; j < image.getHeight(); ++j) {
                    if (i * image.getHeight() + j < size) {
                        sortedlist.add(new FilteredPixel(i, j, Math.abs(filter.getValue(i, j))));
                    }
                    else if (Math.abs(filter.getValue(i, j)) > sortedlist.first().getFilterValue()) {
                        sortedlist.remove(sortedlist.first());
                        sortedlist.add(new FilteredPixel(i, j, Math.abs(filter.getValue(i, j))));
                    }
                }
            }
            final Object[] engines = sortedlist.toArray();
            final int halfway = sortedlist.last().getFilterValue() / 2;
            for (int k = 0; k < engines.length; ++k) {
                final int x = ((FilteredPixel)engines[k]).getX();
                final int y = ((FilteredPixel)engines[k]).getY();
                int acount = 0;
                if (x > 0 && Math.abs(filter.getValue(x - 1, y)) >= halfway) {
                    this.mShips[x - 1][y] = true;
                    ++acount;
                }
                if (x < image.getWidth() - 1 && Math.abs(filter.getValue(x + 1, y)) >= halfway) {
                    this.mShips[x + 1][y] = true;
                    ++acount;
                }
                if (y < image.getHeight() - 1 && Math.abs(filter.getValue(x, y + 1)) >= halfway) {
                    this.mShips[x][y + 1] = true;
                    ++acount;
                }
                if (y > 0 && Math.abs(filter.getValue(x, y - 1)) >= halfway) {
                    this.mShips[x][y - 1] = true;
                    ++acount;
                }
                this.mShips[x][y] = true;
            }
        }
        
        public Shot getShot() {
            if (this.mNumShots <= 0 || this.mMoveAway <= 0) {
                this.mMoveAway = this.mMoveAwayFixed;
                this.mNumShots = 0;
                Shot sh;
                for (sh = super.getShot(); this.beenShot[sh.getX()][sh.getY()][sh.getLayer()][sh.getBitPosition()]; sh = super.getShot()) {}
                this.beenShot[sh.getX()][sh.getY()][sh.getLayer()][sh.getBitPosition()] = true;
                if (this.mShips[sh.getX()][sh.getY()]) {
                    this.mNumShots = this.mInitShots;
                }
                return sh;
            }
            --this.mNumShots;
            --this.mMoveAway;
            Shot sh = super.getRangedShot(this.mShotRange);
            while (this.beenShot[sh.getX()][sh.getY()][sh.getLayer()][sh.getBitPosition()]) {
                if (this.mNumShots <= 0 || this.mMoveAway <= 0) {
                    sh = super.getShot();
                }
                else {
                    sh = super.getRangedShot(this.mShotRange);
                }
                --this.mNumShots;
                --this.mMoveAway;
            }
            this.beenShot[sh.getX()][sh.getY()][sh.getLayer()][sh.getBitPosition()] = true;
            if (this.mShips[sh.getX()][sh.getY()]) {
                this.mNumShots += this.mShotsIncrease;
                super.setLast(sh);
            }
            return sh;
        }
    }
}
