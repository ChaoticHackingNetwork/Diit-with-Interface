// 
// Decompiled by Procyon v0.5.36
// 

package invisibleinktoolkit.util;

import java.util.Random;

public class PRandom
{
    private Random mRandomGen;
    private int mNumLayers;
    private int mHeight;
    private int mWidth;
    private int mStart;
    private int mEnd;
    private Shot mLastShot;
    
    public PRandom(final long seed, final int width, final int height, final int numlayers, final int startrange, final int endrange) throws IllegalArgumentException {
        this.mHeight = height;
        this.mWidth = width;
        this.mNumLayers = numlayers;
        this.mStart = startrange;
        this.mEnd = endrange;
        this.mLastShot = null;
        this.mRandomGen = new Random(seed);
    }
    
    public Shot getShot() {
        final int shotx = Math.abs(this.mRandomGen.nextInt(this.mWidth));
        final int shoty = Math.abs(this.mRandomGen.nextInt(this.mHeight));
        final int layer = Math.abs(this.mRandomGen.nextInt(this.mNumLayers));
        final int bitpos = this.mStart + Math.abs(this.mRandomGen.nextInt(this.mEnd - this.mStart + 1));
        final Shot sh = new Shot(shotx, shoty, bitpos, layer);
        return this.mLastShot = sh;
    }
    
    public Shot getRangedShot(int range) {
        range = Math.abs(range);
        int rangeleft;
        if (this.mLastShot.getX() == 0) {
            rangeleft = 0;
        }
        else {
            rangeleft = range % this.mLastShot.getX();
        }
        int rangeright;
        if (this.mWidth - this.mLastShot.getX() == 0) {
            rangeright = 0;
        }
        else {
            rangeright = range % (this.mWidth - this.mLastShot.getX());
        }
        int rangeup;
        if (this.mLastShot.getY() == 0) {
            rangeup = 0;
        }
        else {
            rangeup = range % this.mLastShot.getY();
        }
        int rangedown;
        if (this.mHeight - this.mLastShot.getY() == 0) {
            rangedown = 0;
        }
        else {
            rangedown = range % (this.mHeight - this.mLastShot.getY());
        }
        final int distright = rangeleft + rangeright;
        final int distdown = rangeup + rangedown;
        final int shotx = this.mLastShot.getX() - rangeleft + this.mRandomGen.nextInt(distright);
        final int shoty = this.mLastShot.getY() - rangeup + this.mRandomGen.nextInt(distdown);
        final int layer = Math.abs(this.mRandomGen.nextInt(this.mNumLayers));
        final int bitpos = this.mStart + Math.abs(this.mRandomGen.nextInt(this.mEnd - this.mStart + 1));
        return new Shot(shotx, shoty, bitpos, layer);
    }
    
    public void setLast(final Shot sh) {
        this.mLastShot = sh;
    }
}
