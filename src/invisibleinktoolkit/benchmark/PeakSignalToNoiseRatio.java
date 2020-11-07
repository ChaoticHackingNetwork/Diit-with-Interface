// 
// Decompiled by Procyon v0.5.36
// 

package invisibleinktoolkit.benchmark;

import java.awt.image.BufferedImage;

public class PeakSignalToNoiseRatio extends PixelBenchmark implements Benchmark
{
    public double calculate(final BufferedImage original, final BufferedImage stego) {
        double totaldifference = 0.0;
        double maxdiff = 0.0;
        double thisdiff = 0.0;
        for (int i = 0; i < original.getHeight(); ++i) {
            for (int j = 0; j < original.getWidth(); ++j) {
                final int origpix = original.getRGB(j, i);
                final int stegpix = stego.getRGB(j, i);
                thisdiff = Math.pow(Math.abs(this.getRed(origpix)) + Math.abs(this.getGreen(origpix)) + Math.abs(this.getBlue(origpix)), 2.0);
                if (thisdiff >= maxdiff) {
                    maxdiff = thisdiff;
                }
                totaldifference += Math.pow(Math.abs(this.getRed(origpix) - this.getRed(stegpix)) + Math.abs(this.getGreen(origpix) - this.getGreen(stegpix)) + Math.abs(this.getBlue(origpix) - this.getBlue(stegpix)), 2.0);
            }
        }
        if (totaldifference == 0.0 || maxdiff == 0.0) {
            return 0.0;
        }
        return original.getWidth() * original.getHeight() * maxdiff / totaldifference;
    }
    
    public String toString() {
        return "Peak Signal to Noise Ratio";
    }
}
