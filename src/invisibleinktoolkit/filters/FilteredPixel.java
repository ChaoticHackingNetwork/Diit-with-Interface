// 
// Decompiled by Procyon v0.5.36
// 

package invisibleinktoolkit.filters;

public class FilteredPixel
{
    private int mXPosition;
    private int mYPosition;
    private int mFilterValue;
    
    public FilteredPixel(final int x, final int y, final int value) {
        this.mXPosition = x;
        this.mYPosition = y;
        this.mFilterValue = value;
    }
    
    public int getX() {
        return this.mXPosition;
    }
    
    public int getY() {
        return this.mYPosition;
    }
    
    public int getFilterValue() {
        return this.mFilterValue;
    }
    
    public String toString() {
        return "X: " + this.mXPosition + " Y: " + this.mYPosition;
    }
}
