// 
// Decompiled by Procyon v0.5.36
// 

package invisibleinktoolkit.benchmark;

import java.awt.image.BufferedImage;

public class SignalToNoiseRatio extends PixelBenchmark implements Benchmark
{
    public double calculate(final BufferedImage original, final BufferedImage stego) {
        double totaldifference = 0.0;
        double originaldiff = 0.0;
        for (int i = 0; i < original.getHeight(); ++i) {
            for (int j = 0; j < original.getWidth(); ++j) {
                final int origpix = original.getRGB(j, i);
                final int stegpix = stego.getRGB(j, i);
                originaldiff += Math.pow(Math.abs(this.getRed(origpix)) + Math.abs(this.getGreen(origpix)) + Math.abs(this.getBlue(origpix)), 2.0);
                totaldifference += Math.pow(Math.abs(this.getRed(origpix) - this.getRed(stegpix)) + Math.abs(this.getGreen(origpix) - this.getGreen(stegpix)) + Math.abs(this.getBlue(origpix) - this.getBlue(stegpix)), 2.0);
            }
        }
        if (originaldiff == 0.0 || totaldifference == 0.0) {
            return 0.0;
        }
        return originaldiff / totaldifference;
    }
    
    public String toString() {
        return "Signal to Noise Ratio";
    }
}
