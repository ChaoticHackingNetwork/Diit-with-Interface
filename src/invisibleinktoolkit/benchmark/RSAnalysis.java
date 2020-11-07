// 
// Decompiled by Procyon v0.5.36
// 

package invisibleinktoolkit.benchmark;

import javax.imageio.ImageIO;
import java.io.File;
import java.util.Vector;
import java.util.Enumeration;
import java.awt.image.BufferedImage;

public class RSAnalysis extends PixelBenchmark
{
    public static final int ANALYSIS_COLOUR_RED = 0;
    public static final int ANALYSIS_COLOUR_GREEN = 1;
    public static final int ANALYSIS_COLOUR_BLUE = 2;
    private int[][] mMask;
    private int mM;
    private int mN;
    
    public RSAnalysis(final int m, final int n) {
        this.mMask = new int[2][m * n];
        int k = 0;
        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < m; ++j) {
                if ((j % 2 == 0 && i % 2 == 0) || (j % 2 == 1 && i % 2 == 1)) {
                    this.mMask[0][k] = 1;
                    this.mMask[1][k] = 0;
                }
                else {
                    this.mMask[0][k] = 0;
                    this.mMask[1][k] = 1;
                }
                ++k;
            }
        }
        this.mM = m;
        this.mN = n;
    }
    
    public double[] doAnalysis(final BufferedImage image, final int colour, final boolean overlap) {
        final int imgx = image.getWidth();
        final int imgy = image.getHeight();
        int startx = 0;
        int starty = 0;
        int[] block = new int[this.mM * this.mN];
        double numregular = 0.0;
        double numsingular = 0.0;
        double numnegreg = 0.0;
        double numnegsing = 0.0;
        double numunusable = 0.0;
        double numnegunusable = 0.0;
        while (startx < imgx && starty < imgy) {
            for (int m = 0; m < 2; ++m) {
                int k = 0;
                for (int i = 0; i < this.mN; ++i) {
                    for (int j = 0; j < this.mM; ++j) {
                        block[k] = image.getRGB(startx + j, starty + i);
                        ++k;
                    }
                }
                final double variationB = this.getVariation(block, colour);
                block = this.flipBlock(block, this.mMask[m]);
                final double variationP = this.getVariation(block, colour);
                block = this.flipBlock(block, this.mMask[m]);
                this.mMask[m] = this.invertMask(this.mMask[m]);
                final double variationN = this.getNegativeVariation(block, colour, this.mMask[m]);
                this.mMask[m] = this.invertMask(this.mMask[m]);
                if (variationP > variationB) {
                    ++numregular;
                }
                if (variationP < variationB) {
                    ++numsingular;
                }
                if (variationP == variationB) {
                    ++numunusable;
                }
                if (variationN > variationB) {
                    ++numnegreg;
                }
                if (variationN < variationB) {
                    ++numnegsing;
                }
                if (variationN == variationB) {
                    ++numnegunusable;
                }
            }
            if (overlap) {
                ++startx;
            }
            else {
                startx += this.mM;
            }
            if (startx >= imgx - 1) {
                startx = 0;
                if (overlap) {
                    ++starty;
                }
                else {
                    starty += this.mN;
                }
            }
            if (starty >= imgy - 1) {
                break;
            }
        }
        final double totalgroups = numregular + numsingular + numunusable;
        final double[] allpixels = this.getAllPixelFlips(image, colour, overlap);
        final double x = this.getX(numregular, numnegreg, allpixels[0], allpixels[2], numsingular, numnegsing, allpixels[1], allpixels[3]);
        double epf;
        if (2.0 * (x - 1.0) == 0.0) {
            epf = 0.0;
        }
        else {
            epf = Math.abs(x / (2.0 * (x - 1.0)));
        }
        double ml;
        if (x - 0.5 == 0.0) {
            ml = 0.0;
        }
        else {
            ml = Math.abs(x / (x - 0.5));
        }
        final double[] results = new double[28];
        results[0] = numregular;
        results[1] = numsingular;
        results[2] = numnegreg;
        results[3] = numnegsing;
        results[4] = Math.abs(numregular - numnegreg);
        results[5] = Math.abs(numsingular - numnegsing);
        results[6] = numregular / totalgroups * 100.0;
        results[7] = numsingular / totalgroups * 100.0;
        results[8] = numnegreg / totalgroups * 100.0;
        results[9] = numnegsing / totalgroups * 100.0;
        results[10] = results[4] / totalgroups * 100.0;
        results[11] = results[5] / totalgroups * 100.0;
        results[12] = allpixels[0];
        results[13] = allpixels[1];
        results[14] = allpixels[2];
        results[15] = allpixels[3];
        results[16] = Math.abs(allpixels[0] - allpixels[1]);
        results[17] = Math.abs(allpixels[2] - allpixels[3]);
        results[18] = allpixels[0] / totalgroups * 100.0;
        results[19] = allpixels[1] / totalgroups * 100.0;
        results[20] = allpixels[2] / totalgroups * 100.0;
        results[21] = allpixels[3] / totalgroups * 100.0;
        results[22] = results[16] / totalgroups * 100.0;
        results[23] = results[17] / totalgroups * 100.0;
        results[24] = totalgroups;
        results[25] = epf;
        results[26] = ml;
        results[27] = imgx * imgy * 3 * ml / 8.0;
        return results;
    }
    
    private double getX(final double r, final double rm, final double r1, final double rm1, final double s, final double sm, final double s1, final double sm1) {
        double x = 0.0;
        final double dzero = r - s;
        final double dminuszero = rm - sm;
        final double done = r1 - s1;
        final double dminusone = rm1 - sm1;
        final double a = 2.0 * (done + dzero);
        final double b = dminuszero - dminusone - done - 3.0 * dzero;
        final double c = dzero - dminuszero;
        if (a == 0.0) {
            x = c / b;
        }
        final double discriminant = Math.pow(b, 2.0) - 4.0 * a * c;
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
            final double cr = (rm - r) / (r1 - r + rm - rm1);
            final double cs = (sm - s) / (s1 - s + sm - sm1);
            x = (cr + cs) / 2.0;
        }
        if (x == 0.0) {
            final double ar = (rm1 - r1 + r - rm + (rm - r) / x) / (x - 1.0);
            final double as = (sm1 - s1 + s - sm + (sm - s) / x) / (x - 1.0);
            if (as > 0.0 | ar < 0.0) {
                final double cr2 = (rm - r) / (r1 - r + rm - rm1);
                final double cs2 = (sm - s) / (s1 - s + sm - sm1);
                x = (cr2 + cs2) / 2.0;
            }
        }
        return x;
    }
    
    private double[] getAllPixelFlips(final BufferedImage image, final int colour, final boolean overlap) {
        final int[] allmask = new int[this.mM * this.mN];
        for (int i = 0; i < allmask.length; ++i) {
            allmask[i] = 1;
        }
        final int imgx = image.getWidth();
        final int imgy = image.getHeight();
        int startx = 0;
        int starty = 0;
        int[] block = new int[this.mM * this.mN];
        double numregular = 0.0;
        double numsingular = 0.0;
        double numnegreg = 0.0;
        double numnegsing = 0.0;
        double numunusable = 0.0;
        double numnegunusable = 0.0;
        while (startx < imgx && starty < imgy) {
            for (int m = 0; m < 2; ++m) {
                int k = 0;
                for (int j = 0; j < this.mN; ++j) {
                    for (int l = 0; l < this.mM; ++l) {
                        block[k] = image.getRGB(startx + l, starty + j);
                        ++k;
                    }
                }
                block = this.flipBlock(block, allmask);
                final double variationB = this.getVariation(block, colour);
                block = this.flipBlock(block, this.mMask[m]);
                final double variationP = this.getVariation(block, colour);
                block = this.flipBlock(block, this.mMask[m]);
                this.mMask[m] = this.invertMask(this.mMask[m]);
                final double variationN = this.getNegativeVariation(block, colour, this.mMask[m]);
                this.mMask[m] = this.invertMask(this.mMask[m]);
                if (variationP > variationB) {
                    ++numregular;
                }
                if (variationP < variationB) {
                    ++numsingular;
                }
                if (variationP == variationB) {
                    ++numunusable;
                }
                if (variationN > variationB) {
                    ++numnegreg;
                }
                if (variationN < variationB) {
                    ++numnegsing;
                }
                if (variationN == variationB) {
                    ++numnegunusable;
                }
            }
            if (overlap) {
                ++startx;
            }
            else {
                startx += this.mM;
            }
            if (startx >= imgx - 1) {
                startx = 0;
                if (overlap) {
                    ++starty;
                }
                else {
                    starty += this.mN;
                }
            }
            if (starty >= imgy - 1) {
                break;
            }
        }
        final double[] results = { numregular, numsingular, numnegreg, numnegsing };
        return results;
    }
    
    public Enumeration getResultNames() {
        final Vector names = new Vector(28);
        names.add("Number of regular groups (positive)");
        names.add("Number of singular groups (positive)");
        names.add("Number of regular groups (negative)");
        names.add("Number of singular groups (negative)");
        names.add("Difference for regular groups");
        names.add("Difference for singular groups");
        names.add("Percentage of regular groups (positive)");
        names.add("Percentage of singular groups (positive)");
        names.add("Percentage of regular groups (negative)");
        names.add("Percentage of singular groups (negative)");
        names.add("Difference for regular groups %");
        names.add("Difference for singular groups %");
        names.add("Number of regular groups (positive for all flipped)");
        names.add("Number of singular groups (positive for all flipped)");
        names.add("Number of regular groups (negative for all flipped)");
        names.add("Number of singular groups (negative for all flipped)");
        names.add("Difference for regular groups (all flipped)");
        names.add("Difference for singular groups (all flipped)");
        names.add("Percentage of regular groups (positive for all flipped)");
        names.add("Percentage of singular groups (positive for all flipped)");
        names.add("Percentage of regular groups (negative for all flipped)");
        names.add("Percentage of singular groups (negative for all flipped)");
        names.add("Difference for regular groups (all flipped) %");
        names.add("Difference for singular groups (all flipped) %");
        names.add("Total number of groups");
        names.add("Estimated percent of flipped pixels");
        names.add("Estimated message length (in percent of pixels)(p)");
        names.add("Estimated message length (in bytes)");
        return names.elements();
    }
    
    private double getVariation(final int[] block, final int colour) {
        double var = 0.0;
        for (int i = 0; i < block.length; i += 4) {
            int colour2 = this.getPixelColour(block[0 + i], colour);
            int colour3 = this.getPixelColour(block[1 + i], colour);
            var += Math.abs(colour2 - colour3);
            colour2 = this.getPixelColour(block[3 + i], colour);
            colour3 = this.getPixelColour(block[2 + i], colour);
            var += Math.abs(colour2 - colour3);
            colour2 = this.getPixelColour(block[1 + i], colour);
            colour3 = this.getPixelColour(block[3 + i], colour);
            var += Math.abs(colour2 - colour3);
            colour2 = this.getPixelColour(block[2 + i], colour);
            colour3 = this.getPixelColour(block[0 + i], colour);
            var += Math.abs(colour2 - colour3);
        }
        return var;
    }
    
    private double getNegativeVariation(final int[] block, final int colour, final int[] mask) {
        double var = 0.0;
        for (int i = 0; i < block.length; i += 4) {
            int colour2 = this.getPixelColour(block[0 + i], colour);
            int colour3 = this.getPixelColour(block[1 + i], colour);
            if (mask[0 + i] == -1) {
                colour2 = this.invertLSB(colour2);
            }
            if (mask[1 + i] == -1) {
                colour3 = this.invertLSB(colour3);
            }
            var += Math.abs(colour2 - colour3);
            colour2 = this.getPixelColour(block[1 + i], colour);
            colour3 = this.getPixelColour(block[3 + i], colour);
            if (mask[1 + i] == -1) {
                colour2 = this.invertLSB(colour2);
            }
            if (mask[3 + i] == -1) {
                colour3 = this.invertLSB(colour3);
            }
            var += Math.abs(colour2 - colour3);
            colour2 = this.getPixelColour(block[3 + i], colour);
            colour3 = this.getPixelColour(block[2 + i], colour);
            if (mask[3 + i] == -1) {
                colour2 = this.invertLSB(colour2);
            }
            if (mask[2 + i] == -1) {
                colour3 = this.invertLSB(colour3);
            }
            var += Math.abs(colour2 - colour3);
            colour2 = this.getPixelColour(block[2 + i], colour);
            colour3 = this.getPixelColour(block[0 + i], colour);
            if (mask[2 + i] == -1) {
                colour2 = this.invertLSB(colour2);
            }
            if (mask[0 + i] == -1) {
                colour3 = this.invertLSB(colour3);
            }
            var += Math.abs(colour2 - colour3);
        }
        return var;
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
    
    private int[] flipBlock(final int[] block, final int[] mask) {
        for (int i = 0; i < block.length; ++i) {
            if (mask[i] == 1) {
                int red = this.getRed(block[i]);
                int green = this.getGreen(block[i]);
                int blue = this.getBlue(block[i]);
                red = this.negateLSB(red);
                green = this.negateLSB(green);
                blue = this.negateLSB(blue);
                final int newpixel = 0xFF000000 | (red & 0xFF) << 16 | (green & 0xFF) << 8 | (blue & 0xFF);
                block[i] = newpixel;
            }
            else if (mask[i] == -1) {
                int red = this.getRed(block[i]);
                int green = this.getGreen(block[i]);
                int blue = this.getBlue(block[i]);
                red = this.invertLSB(red);
                green = this.invertLSB(green);
                blue = this.invertLSB(blue);
                final int newpixel = 0xFF000000 | (red & 0xFF) << 16 | (green & 0xFF) << 8 | (blue & 0xFF);
                block[i] = newpixel;
            }
        }
        return block;
    }
    
    private int negateLSB(final int abyte) {
        final int temp = abyte & 0xFE;
        if (temp == abyte) {
            return abyte | 0x1;
        }
        return temp;
    }
    
    private int invertLSB(final int abyte) {
        if (abyte == 255) {
            return 256;
        }
        if (abyte == 256) {
            return 255;
        }
        return this.negateLSB(abyte + 1) - 1;
    }
    
    private int[] invertMask(final int[] mask) {
        for (int i = 0; i < mask.length; ++i) {
            mask[i] *= -1;
        }
        return mask;
    }
    
    public static void main(final String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: invisibleinktoolkit.benchmark.RSAnalysis <imagefilename>");
            System.exit(1);
        }
        try {
            System.out.println("\nRS Analysis results");
            System.out.println("-------------------");
            final RSAnalysis rsa = new RSAnalysis(2, 2);
            final BufferedImage image = ImageIO.read(new File(args[0]));
            double average = 0.0;
            double[] results = rsa.doAnalysis(image, 0, true);
            System.out.println("Result from red: " + results[26]);
            average += results[26];
            results = rsa.doAnalysis(image, 1, true);
            System.out.println("Result from green: " + results[26]);
            average += results[26];
            results = rsa.doAnalysis(image, 2, true);
            System.out.println("Result from blue: " + results[26]);
            average += results[26];
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
