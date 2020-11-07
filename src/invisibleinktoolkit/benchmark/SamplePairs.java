// 
// Decompiled by Procyon v0.5.36
// 

package invisibleinktoolkit.benchmark;

import javax.imageio.ImageIO;
import java.io.File;
import java.awt.image.BufferedImage;

public class SamplePairs extends PixelBenchmark
{
    public static final int ANALYSIS_COLOUR_RED = 0;
    public static final int ANALYSIS_COLOUR_GREEN = 1;
    public static final int ANALYSIS_COLOUR_BLUE = 2;
    
    public double doAnalysis(final BufferedImage image, final int colour) {
        final int imgx = image.getWidth();
        final int imgy = image.getHeight();
        int startx = 0;
        int starty = 0;
        final int[] apair = new int[2];
        long W;
        long Z;
        long Y;
        long P;
        long X = P = (Y = (Z = (W = 0L)));
        for (starty = 0; starty < imgy; ++starty) {
            for (startx = 0; startx < imgx; startx += 2) {
                apair[0] = image.getRGB(startx, starty);
                apair[1] = image.getRGB(startx + 1, starty);
                final int u = this.getPixelColour(apair[0], colour);
                final int v = this.getPixelColour(apair[1], colour);
                if (u >> 1 == v >> 1 && (v & 0x1) != (u & 0x1)) {
                    ++W;
                }
                if (u == v) {
                    ++Z;
                }
                if ((v == v >> 1 << 1 && u < v) || (v != v >> 1 << 1 && u > v)) {
                    ++X;
                }
                if ((v == v >> 1 << 1 && u > v) || (v != v >> 1 << 1 && u < v)) {
                    ++Y;
                }
                ++P;
            }
        }
        for (starty = 0; starty < imgy; starty += 2) {
            for (startx = 0; startx < imgx; ++startx) {
                apair[0] = image.getRGB(startx, starty);
                apair[1] = image.getRGB(startx, starty + 1);
                final int u = this.getPixelColour(apair[0], colour);
                final int v = this.getPixelColour(apair[1], colour);
                if (u >> 1 == v >> 1 && (v & 0x1) != (u & 0x1)) {
                    ++W;
                }
                if (u == v) {
                    ++Z;
                }
                if ((v == v >> 1 << 1 && u < v) || (v != v >> 1 << 1 && u > v)) {
                    ++X;
                }
                if ((v == v >> 1 << 1 && u > v) || (v != v >> 1 << 1 && u < v)) {
                    ++Y;
                }
                ++P;
            }
        }
        final double a = 0.5 * (W + Z);
        final double b = (double)(2L * X - P);
        final double c = (double)(Y - X);
        if (a == 0.0) {}
        final double discriminant = Math.pow(b, 2.0) - 4.0 * a * c;
        double x;
        if (discriminant >= 0.0) {
            final double rootpos = (-1.0 * b + Math.sqrt(discriminant)) / (2.0 * a);
            final double rootneg = (-1.0 * b - Math.sqrt(discriminant)) / (2.0 * a);
            if (Math.abs(rootpos) <= Math.abs(rootneg)) {
                x = rootpos;
            }
            else {
                x = rootneg;
            }
        }
        else {
            x = c / b;
        }
        if (x == 0.0) {
            x = c / b;
        }
        return x;
    }
    
    public int getPixelColour(final int pixel, final int colour) {
        if (colour == 0) {
            return this.getRed(pixel);
        }
        if (colour == 1) {
            return this.getGreen(pixel);
        }
        if (colour == 2) {
            return this.getBlue(pixel);
        }
        return 0;
    }
    
    public static void main(final String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: invisibleinktoolkit.benchmark.SamplePairs <imagefilename>");
            System.exit(1);
        }
        try {
            System.out.println("\nSample Pairs Results");
            System.out.println("--------------------");
            final SamplePairs sp = new SamplePairs();
            final BufferedImage image = ImageIO.read(new File(args[0]));
            double average = 0.0;
            double results = sp.doAnalysis(image, 0);
            System.out.println("Result from red: " + results);
            average += results;
            results = sp.doAnalysis(image, 1);
            System.out.println("Result from green: " + results);
            average += results;
            results = sp.doAnalysis(image, 2);
            System.out.println("Result from blue: " + results);
            average += results;
            average /= 3.0;
            System.out.println("Average result: " + average);
            System.out.println();
        }
        catch (Exception e) {
            System.out.println("ERROR: Cannot process that image type, please try another image.");
            e.printStackTrace();
        }
    }
}
