// 
// Decompiled by Procyon v0.5.36
// 

package invisibleinktoolkit.benchmark;

import invisibleinktoolkit.filters.Laplace;
import java.awt.image.BufferedImage;

public class LaplacianMeanSquaredError extends PixelBenchmark implements Benchmark
{
    public double calculate(final BufferedImage original, final BufferedImage stego) {
        double totaldifference = 0.0;
        double originaldiff = 0.0;
        final Laplace origfilter = new Laplace(0, 8);
        final Laplace stegfilter = new Laplace(0, 8);
        origfilter.setImage(original);
        stegfilter.setImage(stego);
        try {
            for (int i = 0; i < original.getHeight(); ++i) {
                for (int j = 0; j < original.getWidth(); ++j) {
                    originaldiff += Math.pow(origfilter.getValue(j, i) - stegfilter.getValue(j, i), 2.0);
                    totaldifference += Math.pow(origfilter.getValue(j, i), 2.0);
                }
            }
        }
        catch (Exception e) {
            System.out.println("Error filtering image.");
            e.printStackTrace();
        }
        if (originaldiff == 0.0 || totaldifference == 0.0) {
            return 0.0;
        }
        return originaldiff / totaldifference;
    }
    
    public String toString() {
        return "Laplacian Mean Squared Error";
    }
}
