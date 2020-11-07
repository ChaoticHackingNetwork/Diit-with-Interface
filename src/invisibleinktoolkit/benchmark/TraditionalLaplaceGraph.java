// 
// Decompiled by Procyon v0.5.36
// 

package invisibleinktoolkit.benchmark;

import java.util.Comparator;
import java.util.Arrays;
import invisibleinktoolkit.filters.FPComparator;
import invisibleinktoolkit.filters.FilteredPixel;
import invisibleinktoolkit.filters.TraditionalLaplace;
import java.awt.image.BufferedImage;

public class TraditionalLaplaceGraph
{
    public static String getCSVGraph(final BufferedImage image) throws Exception {
        final StringBuffer sb = new StringBuffer();
        sb.append("\"Frequency\",\"Laplace Value\"\n");
        final double[][] graph = getGraph(image);
        for (int i = 0; i < graph.length; ++i) {
            sb.append(String.valueOf(graph[i][1]) + "," + graph[i][0] + "\n");
        }
        sb.append("\n\n");
        return sb.toString();
    }
    
    public static double[][] getGraph(final BufferedImage image) throws Exception {
        final TraditionalLaplace filter = new TraditionalLaplace(0, 8);
        filter.setImage(image);
        final FilteredPixel[] fparray = new FilteredPixel[image.getWidth() * image.getHeight()];
        for (int i = 0; i < image.getWidth(); ++i) {
            for (int j = 0; j < image.getHeight(); ++j) {
                fparray[i * image.getHeight() + j] = new FilteredPixel(i, j, filter.getValue(i, j));
            }
        }
        Arrays.sort(fparray, new FPComparator());
        int numdistinct = 1;
        for (int k = 1; k < fparray.length; ++k) {
            if (fparray[k].getFilterValue() != fparray[k - 1].getFilterValue()) {
                ++numdistinct;
            }
        }
        final double[][] results = new double[numdistinct][2];
        results[0][0] = fparray[0].getFilterValue();
        results[0][1] = 1.0;
        int l = 0;
        for (int m = 0; m < fparray.length; ++m) {
            if (results[l][0] != fparray[m].getFilterValue()) {
                ++l;
                results[l][0] = fparray[m].getFilterValue();
                results[l][1] = 1.0;
            }
            else {
                final double[] array = results[l];
                final int n = 1;
                ++array[n];
            }
        }
        for (int m = 0; m < results.length; ++m) {
            results[m][1] /= fparray.length;
        }
        return results;
    }
}
