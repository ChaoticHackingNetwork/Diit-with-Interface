// 
// Decompiled by Procyon v0.5.36
// 

package invisibleinktoolkit.benchmark;

import java.awt.image.BufferedImage;

public class AverageAbsoluteDifference extends PixelBenchmark implements Benchmark
{
    public double calculate(final BufferedImage original, final BufferedImage stego) {
        final double ratio = original.getHeight() * original.getWidth();
        double totaldifference = 0.0;
        for (int i = 0; i < original.getHeight(); ++i) {
            for (int j = 0; j < original.getWidth(); ++j) {
                final int origpix = original.getRGB(j, i);
                final int stegpix = stego.getRGB(j, i);
                totaldifference += Math.abs(this.getRed(origpix) - this.getRed(stegpix)) + Math.abs(this.getGreen(origpix) - this.getGreen(stegpix)) + Math.abs(this.getBlue(origpix) - this.getBlue(stegpix));
            }
        }
        return totaldifference / ratio;
    }
    
    public String toString() {
        return "Average Absolute Difference";
    }
}
