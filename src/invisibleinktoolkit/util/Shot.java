// 
// Decompiled by Procyon v0.5.36
// 

package invisibleinktoolkit.util;

public class Shot
{
    private int mXPosition;
    private int mYPosition;
    private int mBitPosition;
    private int mLayer;
    
    public Shot(final int xpos, final int ypos) {
        this(xpos, ypos, 0, 0);
    }
    
    public Shot(final int xpos, final int ypos, final int bitpos, final int layer) {
        this.mXPosition = xpos;
        this.mYPosition = ypos;
        this.mBitPosition = bitpos;
        this.mLayer = layer;
    }
    
    public int getX() {
        return this.mXPosition;
    }
    
    public int getY() {
        return this.mYPosition;
    }
    
    public int getBitPosition() {
        return this.mBitPosition;
    }
    
    public int getLayer() {
        return this.mLayer;
    }
    
    public String toString() {
        return "X: " + this.mXPosition + " Y: " + this.mYPosition + " layer: " + this.mLayer + " bit: " + this.mBitPosition;
    }
    
    public String toStringMinusBit() {
        return "X: " + this.mXPosition + " Y: " + this.mYPosition + " layer: " + this.mLayer;
    }
    
    public String toStringXandY() {
        return "X: " + this.mXPosition + " Y: " + this.mYPosition;
    }
}
