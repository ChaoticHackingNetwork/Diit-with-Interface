// 
// Decompiled by Procyon v0.5.36
// 

package invisibleinktoolkit.benchmark;

public class PixelBenchmark
{
    public int getRed(final int pixel) {
        return pixel >> 16 & 0xFF;
    }
    
    public int getGreen(final int pixel) {
        return pixel >> 8 & 0xFF;
    }
    
    public int getBlue(final int pixel) {
        return pixel & 0xFF;
    }
}
