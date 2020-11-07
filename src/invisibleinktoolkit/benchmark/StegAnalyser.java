// 
// Decompiled by Procyon v0.5.36
// 

package invisibleinktoolkit.benchmark;

import java.util.HashMap;
import java.util.Enumeration;
import javax.imageio.ImageIO;
import invisibleinktoolkit.stego.StegoImage;
import invisibleinktoolkit.util.TestingUtils;
import invisibleinktoolkit.stego.InsertableMessage;
import invisibleinktoolkit.stego.CoverImage;
import invisibleinktoolkit.stego.StegoAlgorithm;
import java.io.File;
import java.awt.image.BufferedImage;

public class StegAnalyser
{
    private String mResultsString;
    private boolean mRunRSAnalysis;
    private boolean mRunSamplePairs;
    private boolean mRunLaplaceGraph;
    
    public StegAnalyser(final boolean runrsanalysis, final boolean runsamplepairs, final boolean runlaplacegraph) {
        this.mResultsString = "";
        this.mRunRSAnalysis = runrsanalysis;
        this.mRunSamplePairs = runsamplepairs;
        this.mRunLaplaceGraph = runlaplacegraph;
    }
    
    public String run(final BufferedImage stego) throws IllegalArgumentException, Exception {
        if (stego == null) {
            throw new IllegalArgumentException("Stego image must not be null!");
        }
        final StringBuffer results = new StringBuffer("Results of steganalysis\n==========================\n\n");
        double averageresults = 0.0;
        double averagelength = 0.0;
        if (this.mRunRSAnalysis) {
            results.append("RS ANALYSIS\n============\n\n");
            results.append("RS Analysis (Non-overlapping groups)\n");
            for (int j = 0; j < 3; ++j) {
                final RSAnalysis rsa = new RSAnalysis(2, 2);
                final double[] testresults = rsa.doAnalysis(stego, j, false);
                String colour;
                if (j == 0) {
                    colour = "red";
                }
                else if (j == 1) {
                    colour = "green";
                }
                else {
                    colour = "blue";
                }
                results.append("Percentage in " + colour + ": ");
                results.append(String.valueOf(this.round(testresults[26] * 100.0, 5)) + "\n");
                results.append("Approximate length (in bytes) from " + colour + ": " + this.round(testresults[27], 5) + "\n");
                averageresults += testresults[26];
                averagelength += testresults[27];
            }
            results.append("\nRS Analysis (Overlapping groups)\n");
            for (int j = 0; j < 3; ++j) {
                final RSAnalysis rsa = new RSAnalysis(2, 2);
                final double[] testresults = rsa.doAnalysis(stego, j, true);
                String colour;
                if (j == 0) {
                    colour = "red";
                }
                else if (j == 1) {
                    colour = "green";
                }
                else {
                    colour = "blue";
                }
                results.append("Percentage in " + colour + ": ");
                results.append(String.valueOf(this.round(testresults[26] * 100.0, 5)) + "\n");
                results.append("Approximate length (in bytes) from " + colour + ": " + this.round(testresults[27], 5) + "\n");
                averageresults += testresults[26];
                averagelength += testresults[27];
            }
            results.append("\nAverage across all groups/colours: " + this.round(averageresults / 6.0 * 100.0, 5));
            results.append("\nAverage approximate length across all groups/colours: " + this.round(averagelength / 6.0, 5));
            results.append("\n\n\n");
        }
        averageresults = 0.0;
        averagelength = 0.0;
        if (this.mRunSamplePairs) {
            results.append("SAMPLE PAIRS\n=============\n");
            for (int j = 0; j < 3; ++j) {
                final SamplePairs sp = new SamplePairs();
                final double estimatedlength = sp.doAnalysis(stego, j);
                final double numbytes = stego.getHeight() * stego.getWidth() * 3 / 8 * estimatedlength;
                String colour;
                if (j == 0) {
                    colour = "red";
                }
                else if (j == 1) {
                    colour = "green";
                }
                else {
                    colour = "blue";
                }
                results.append("Percentage in " + colour + ": ");
                results.append(String.valueOf(this.round(estimatedlength * 100.0, 5)) + "\n");
                results.append("Approximate length (in bytes) from " + colour + ": " + this.round(numbytes, 5) + "\n");
                averageresults += estimatedlength;
                averagelength += numbytes;
            }
            results.append("\nAverage across all groups/colours: " + this.round(averageresults / 3.0 * 100.0, 5));
            results.append("\nAverage approximate length across all groups/colours: " + this.round(averagelength / 3.0, 5));
            results.append("\n\n\n");
        }
        if (this.mRunLaplaceGraph) {
            results.append("LAPLACE GRAPH (CSV formatted)\n==============================\n\n");
            results.append(LaplaceGraph.getCSVGraph(stego));
        }
        results.append("\n\n\n\n");
        return this.mResultsString = results.toString();
    }
    
    public String createCombineDirectories(final File messagedir, final File imagedir, final File tempdir, final StegoAlgorithm algorithm) throws IllegalArgumentException {
        if (!messagedir.isDirectory() || !imagedir.isDirectory() || !tempdir.isDirectory()) {
            throw new IllegalArgumentException("All passed files must be directories!");
        }
        final StringBuffer errors = new StringBuffer("Errors: \n========\n\n");
        final String[] messagelist = messagedir.list();
        final String[] imagelist = imagedir.list();
        final String fileseparator = System.getProperty("file.separator");
        for (int i = 0; i < imagelist.length; ++i) {
            if (imagelist[i].endsWith(".bmp") || imagelist[i].endsWith(".jpg") || imagelist[i].endsWith(".png")) {
                final String coverfilepath = String.valueOf(imagedir.getPath()) + fileseparator + imagelist[i];
                final String originalname = imagelist[i].substring(0, imagelist[i].indexOf("."));
                for (int j = 0; j < messagelist.length; ++j) {
                    try {
                        if (messagelist[j].endsWith(".txt")) {
                            final String messagefilepath = String.valueOf(messagedir.getPath()) + fileseparator + messagelist[j];
                            final CoverImage cimage = new CoverImage(coverfilepath);
                            final InsertableMessage imess = new InsertableMessage(messagefilepath);
                            final String outputpath = String.valueOf(originalname) + "~" + messagelist[j].substring(0, messagelist[j].lastIndexOf(".")) + "-" + algorithm.getClass().getName().toLowerCase().substring(algorithm.getClass().getName().lastIndexOf(".") + 1, algorithm.getClass().getName().length()) + "." + "png";
                            final StegoImage stego = algorithm.encode(imess, cimage, 0L);
                            stego.write("png", new File(tempdir, outputpath));
                        }
                    }
                    catch (Exception e) {
                        errors.append("Error: Could not process: " + imagelist[i] + " with " + messagelist[j] + "\n");
                    }
                    System.gc();
                }
            }
        }
        try {
            TestingUtils.copyIntoTempFolder(imagedir, tempdir, tempdir);
        }
        catch (Exception ex) {}
        return errors.toString();
    }
    
    public String getCSV(final File directory, final int laplacelimit) {
        System.out.print("\n\nCSV Progress: {");
        final String[] files = directory.list();
        final int fivepercent = (int)Math.floor(files.length / 20);
        final StringBuffer csv = new StringBuffer();
        if (this.mRunRSAnalysis) {
            final RSAnalysis rsa = new RSAnalysis(2, 2);
            String rflag = "(rs overlapping)";
            for (int i = 0; i < 3; ++i) {
                final Enumeration rnames = rsa.getResultNames();
                String colour;
                if (i == 0) {
                    colour = " red ";
                }
                else if (i == 1) {
                    colour = " green ";
                }
                else {
                    colour = " blue ";
                }
                while (rnames.hasMoreElements()) {
                    final String aname = rnames.nextElement().toString();
                    String towrite = String.valueOf(aname) + colour + rflag + ",";
                    towrite = towrite.replace(' ', '-');
                    csv.append(towrite);
                }
            }
            rflag = "(rs non-overlapping)";
            for (int i = 0; i < 3; ++i) {
                final Enumeration rnames = rsa.getResultNames();
                String colour;
                if (i == 0) {
                    colour = " red ";
                }
                else if (i == 1) {
                    colour = " green ";
                }
                else {
                    colour = " blue ";
                }
                while (rnames.hasMoreElements()) {
                    final String aname = rnames.nextElement().toString();
                    String towrite = String.valueOf(aname) + colour + rflag + ",";
                    towrite = towrite.replace(' ', '-');
                    csv.append(towrite);
                }
            }
        }
        if (this.mRunSamplePairs) {
            for (int j = 0; j < 3; ++j) {
                String colour2;
                if (j == 0) {
                    colour2 = "-red-";
                }
                else if (j == 1) {
                    colour2 = "-green-";
                }
                else {
                    colour2 = "-blue-";
                }
                csv.append("SP-Percentage" + colour2 + ",");
                csv.append("SP-Approximate-Bytes" + colour2 + ",");
            }
        }
        if (this.mRunLaplaceGraph) {
            for (int k = 0; k < laplacelimit; ++k) {
                csv.append("Laplace-value-" + k + ",");
            }
        }
        csv.append("Steganography-Type,Image-Name\n");
        for (int k = 0; k < files.length; ++k) {
            if (k > 0 && fivepercent > 0 && k % fivepercent == 0) {
                System.out.print("#");
            }
            if (!files[k].endsWith(".bmp") && !files[k].endsWith(".png")) {
                if (!files[k].endsWith(".jpg")) {
                    continue;
                }
            }
            try {
                final BufferedImage image = ImageIO.read(new File(directory, files[k]));
                if (this.mRunRSAnalysis) {
                    for (int l = 0; l < 3; ++l) {
                        final RSAnalysis rsa2 = new RSAnalysis(2, 2);
                        final double[] testresults = rsa2.doAnalysis(image, l, true);
                        for (int m = 0; m < testresults.length; ++m) {
                            csv.append(String.valueOf(testresults[m]) + ",");
                        }
                    }
                    for (int l = 0; l < 3; ++l) {
                        final RSAnalysis rsa2 = new RSAnalysis(2, 2);
                        final double[] testresults = rsa2.doAnalysis(image, l, false);
                        for (int m = 0; m < testresults.length; ++m) {
                            csv.append(String.valueOf(testresults[m]) + ",");
                        }
                    }
                }
                if (this.mRunSamplePairs) {
                    for (int l = 0; l < 3; ++l) {
                        final SamplePairs sp = new SamplePairs();
                        final double estimatedlength = sp.doAnalysis(image, l);
                        final double numbytes = image.getHeight() * image.getWidth() * 3 / 8 * estimatedlength;
                        csv.append(String.valueOf(estimatedlength) + "," + numbytes + ",");
                    }
                }
                if (this.mRunLaplaceGraph) {
                    final double[][] lgres = LaplaceGraph.getGraph(image);
                    for (int j2 = 0; j2 < laplacelimit; ++j2) {
                        if (lgres.length <= laplacelimit && j2 >= lgres.length) {
                            csv.append("0,");
                        }
                        else if (lgres[j2][0] != j2) {
                            csv.append("0,");
                        }
                        else {
                            csv.append(String.valueOf(lgres[j2][1]) + ",");
                        }
                    }
                }
                String flag;
                if (files[k].indexOf("_") >= 0 || files[k].indexOf("-") >= 0) {
                    if (files[k].indexOf("_") >= 0) {
                        flag = files[k].substring(files[k].indexOf("_") + 1, files[k].lastIndexOf("."));
                    }
                    else {
                        flag = files[k].substring(files[k].indexOf("-") + 1, files[k].lastIndexOf("."));
                    }
                }
                else {
                    flag = "none";
                }
                csv.append(flag);
                csv.append("," + files[k]);
                if (csv.charAt(csv.length() - 1) == ',') {
                    csv.deleteCharAt(csv.length() - 1);
                }
                csv.append("\n");
            }
            catch (Exception ex) {}
            System.gc();
        }
        System.out.println("} Complete!");
        csv.append("\n");
        return csv.toString();
    }
    
    public String getARFF(final File directory, final int laplacelimit, final String relationname) {
        final StringBuffer arff = new StringBuffer();
        System.out.print("\n\nARFF Progress: {");
        final String[] files = directory.list();
        final int fivepercent = (int)Math.floor(files.length / 20);
        arff.append("% Steganography Benchmarking Data\n%\n");
        arff.append("% Sourced from automatic generation in Digital Invisible Ink Toolkit\n");
        arff.append("% Generator created by Kathryn Hempstalk.\n");
        arff.append("% Generator copyright under the Gnu General Public License, 2005\n");
        arff.append("\n");
        arff.append("\n@relation '" + relationname + "'\n\n");
        if (this.mRunRSAnalysis) {
            final RSAnalysis rsa = new RSAnalysis(2, 2);
            String rflag = "(rs overlapping)";
            for (int i = 0; i < 3; ++i) {
                final Enumeration rnames = rsa.getResultNames();
                String colour;
                if (i == 0) {
                    colour = " red ";
                }
                else if (i == 1) {
                    colour = " green ";
                }
                else {
                    colour = " blue ";
                }
                while (rnames.hasMoreElements()) {
                    final String aname = rnames.nextElement().toString();
                    final String towrite = String.valueOf(aname) + colour + rflag;
                    arff.append("@attribute '" + towrite + "' numeric\n");
                }
            }
            rflag = "(rs non-overlapping)";
            for (int i = 0; i < 3; ++i) {
                final Enumeration rnames = rsa.getResultNames();
                String colour;
                if (i == 0) {
                    colour = " red ";
                }
                else if (i == 1) {
                    colour = " green ";
                }
                else {
                    colour = " blue ";
                }
                while (rnames.hasMoreElements()) {
                    final String aname = rnames.nextElement().toString();
                    final String towrite = String.valueOf(aname) + colour + rflag;
                    arff.append("@attribute '" + towrite + "' numeric\n");
                }
            }
        }
        if (this.mRunSamplePairs) {
            for (int j = 0; j < 3; ++j) {
                String colour2;
                if (j == 0) {
                    colour2 = " red ";
                }
                else if (j == 1) {
                    colour2 = " green ";
                }
                else {
                    colour2 = " blue ";
                }
                arff.append("@attribute 'SP Percentage" + colour2 + "' numeric\n");
                arff.append("@attribute 'SP Approximate Bytes" + colour2 + "' numeric\n");
            }
        }
        if (this.mRunLaplaceGraph) {
            for (int k = 0; k < laplacelimit; ++k) {
                arff.append("@attribute 'Laplace value " + k + "' numeric\n");
            }
        }
        arff.append("@attribute 'Steganography Type' {");
        final HashMap stegotypes = this.getStegTypes(directory.list());
        final Object[] valuesarray = stegotypes.values().toArray();
        arff.append((String)valuesarray[0]);
        for (int l = 1; l < valuesarray.length; ++l) {
            arff.append("," + (String)valuesarray[l]);
        }
        arff.append("}\n");
        arff.append("@attribute 'Image Name' string\n");
        arff.append("\n@data\n");
        for (int l = 0; l < files.length; ++l) {
            if (l > 0 && fivepercent > 0 && l % fivepercent == 0 && l != 0) {
                System.out.print("#");
            }
            if (!files[l].endsWith(".bmp") && !files[l].endsWith(".png")) {
                if (!files[l].endsWith(".jpg")) {
                    continue;
                }
            }
            try {
                final BufferedImage image = ImageIO.read(new File(directory, files[l]));
                if (this.mRunRSAnalysis) {
                    for (int m = 0; m < 3; ++m) {
                        final RSAnalysis rsa2 = new RSAnalysis(2, 2);
                        final double[] testresults = rsa2.doAnalysis(image, m, true);
                        for (int k2 = 0; k2 < testresults.length; ++k2) {
                            arff.append(String.valueOf(testresults[k2]) + ",");
                        }
                    }
                    for (int m = 0; m < 3; ++m) {
                        final RSAnalysis rsa2 = new RSAnalysis(2, 2);
                        final double[] testresults = rsa2.doAnalysis(image, m, false);
                        for (int k2 = 0; k2 < testresults.length; ++k2) {
                            arff.append(String.valueOf(testresults[k2]) + ",");
                        }
                    }
                }
                if (this.mRunSamplePairs) {
                    for (int m = 0; m < 3; ++m) {
                        final SamplePairs sp = new SamplePairs();
                        final double estimatedlength = sp.doAnalysis(image, m);
                        final double numbytes = image.getHeight() * image.getWidth() * 3 / 8 * (estimatedlength / 100.0);
                        arff.append(String.valueOf(estimatedlength) + "," + numbytes + ",");
                    }
                }
                if (this.mRunLaplaceGraph) {
                    final double[][] lgres = LaplaceGraph.getGraph(image);
                    for (int j2 = 0; j2 < laplacelimit; ++j2) {
                        if (lgres.length <= laplacelimit && j2 >= lgres.length) {
                            arff.append("0,");
                        }
                        else if (lgres[j2][0] != j2) {
                            arff.append("0,");
                        }
                        else {
                            arff.append(String.valueOf(lgres[j2][1]) + ",");
                        }
                    }
                }
                String flag;
                if (files[l].indexOf("_") >= 0 || files[l].indexOf("-") >= 0) {
                    if (files[l].indexOf("_") >= 0) {
                        flag = files[l].substring(files[l].indexOf("_") + 1, files[l].lastIndexOf("."));
                    }
                    else {
                        flag = files[l].substring(files[l].indexOf("-") + 1, files[l].lastIndexOf("."));
                    }
                }
                else {
                    flag = "none";
                }
                arff.append(flag);
                arff.append("," + files[l]);
                if (arff.charAt(arff.length() - 1) == ',') {
                    arff.deleteCharAt(arff.length() - 1);
                }
                arff.append("\n");
            }
            catch (Exception ex) {}
            System.gc();
        }
        System.out.println("} Complete!");
        return arff.toString();
    }
    
    private HashMap getStegTypes(final String[] files) {
        final HashMap stegotypes = new HashMap();
        stegotypes.put("none", "none");
        String stegtype = "";
        for (int i = 0; i < files.length; ++i) {
            if (files[i].endsWith(".bmp") || files[i].endsWith(".png")) {
                if (files[i].indexOf("_") >= 0) {
                    stegtype = files[i].substring(files[i].indexOf("_") + 1, files[i].lastIndexOf("."));
                    stegotypes.put(stegtype, stegtype);
                }
                else if (files[i].indexOf("-") >= 0) {
                    stegtype = files[i].substring(files[i].indexOf("-") + 1, files[i].lastIndexOf("."));
                    stegotypes.put(stegtype, stegtype);
                }
            }
        }
        return stegotypes;
    }
    
    public String toString() {
        return this.mResultsString;
    }
    
    public double round(double number, final int places) {
        final double multiple = Math.pow(10.0, places);
        number *= multiple;
        final long num2 = Math.round(number);
        number = num2 / multiple;
        if (number < 1.0E-12) {
            return 0.0;
        }
        return number;
    }
}
