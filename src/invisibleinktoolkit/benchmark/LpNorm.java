// 
// Decompiled by Procyon v0.5.36
// 

package invisibleinktoolkit.benchmark;

import java.awt.image.BufferedImage;

public class LpNorm extends PixelBenchmark implements Benchmark
{
    private double mP;
    
    public LpNorm() {
        this.mP = 2.0;
    }
    
    public double calculate(final BufferedImage original, final BufferedImage stego) {
        final double ratio = original.getHeight() * original.getWidth();
        final double lpratio = 1.0 / this.mP;
        double totaldifference = 0.0;
        for (int i = 0; i < original.getHeight(); ++i) {
            for (int j = 0; j < original.getWidth(); ++j) {
                final int origpix = original.getRGB(j, i);
                final int stegpix = stego.getRGB(j, i);
                totaldifference += Math.pow(Math.abs(this.getRed(origpix) - this.getRed(stegpix)) + Math.abs(this.getGreen(origpix) - this.getGreen(stegpix)) + Math.abs(this.getBlue(origpix) - this.getBlue(stegpix)), this.mP);
            }
        }
        return totaldifference / ratio * lpratio;
    }
    
    public String toString() {
        return "LpNorm";
    }
    
    public double getP() {
        return this.mP;
    }
    
    public void setP(final double newp) {
        this.mP = newp;
    }
}
