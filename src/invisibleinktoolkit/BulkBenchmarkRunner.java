// 
// Decompiled by Procyon v0.5.36
// 

package invisibleinktoolkit;

import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import invisibleinktoolkit.util.AFileFilter;
import java.awt.image.BufferedImage;
import java.io.File;
import invisibleinktoolkit.benchmark.TraditionalLaplaceGraph;
import java.io.Writer;
import java.io.BufferedWriter;
import java.io.FileWriter;
import invisibleinktoolkit.benchmark.LaplaceGraph;
import javax.imageio.ImageIO;
import java.awt.Component;
import javax.swing.JFileChooser;
import invisibleinktoolkit.util.TestingUtils;
import java.io.Reader;
import java.io.InputStreamReader;
import javax.swing.UIManager;
import java.io.BufferedReader;

public class BulkBenchmarkRunner
{
    private static BufferedReader stdin;
    
    public static void main(final String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch (Exception ex) {}
        try {
            BulkBenchmarkRunner.stdin = new BufferedReader(new InputStreamReader(System.in));
            printOptions();
            for (int input = getUserInput(); input != 6; input = getUserInput()) {
                if (input == 1) {
                    System.out.println("Please select a message output folder\n");
                    final File directory = getFolder();
                    if (directory == null) {
                        System.exit(1);
                    }
                    System.out.println("Please enter the starting number of KB:");
                    final int start = getUserInput();
                    System.out.println("Please enter the amount of KB to increment by:");
                    final int inc = getUserInput();
                    System.out.println("Please enter the number of files to write:");
                    final int numberfiles = getUserInput();
                    TestingUtils.createRandomMessages(directory, start, inc, numberfiles);
                }
                if (input == 2) {
                    System.out.println("Please select a file to use for source\n");
                    final JFileChooser jfc = new JFileChooser();
                    jfc.setDialogTitle("Pick a file to use for messages.");
                    final int choice = jfc.showSaveDialog(null);
                    if (choice == 0) {
                        final File tfile = jfc.getSelectedFile();
                        System.out.println("Please select a message output folder\n");
                        final File directory2 = getFolder();
                        if (directory2 != null) {
                            System.out.println("Please enter the starting number of KB:");
                            final int start2 = getUserInput();
                            System.out.println("Please enter the amount of KB to increment by:");
                            final int inc2 = getUserInput();
                            System.out.println("Please enter the number of files to write:");
                            final int numberfiles2 = getUserInput();
                            TestingUtils.cutTextIntoMessages(tfile, directory2, start2, inc2, numberfiles2);
                        }
                        else {
                            System.out.println("Cancelling...\n");
                        }
                    }
                    else {
                        System.out.println("Cancelling...\n");
                    }
                }
                if (input == 3) {
                    System.out.println("Please select a image input folder\n");
                    final File imagedirectory = getFolder();
                    System.out.println("Please select a message input folder\n");
                    final File messagedirectory = getFolder();
                    System.out.println("Please select a stego output folder\n");
                    final File outdirectory = getFolder();
                    final String[] algorithms = { "invisibleinktoolkit.algorithms.BlindHide", "invisibleinktoolkit.algorithms.BattleSteg", "invisibleinktoolkit.algorithms.FilterFirst", "invisibleinktoolkit.algorithms.HideSeek" };
                    final String[] filters = { "invisibleinktoolkit.filters.Laplace", "invisibleinktoolkit.filters.Sobel" };
                    System.out.println("Working, please wait....\n");
                    TestingUtils.combineFolders(imagedirectory, messagedirectory, outdirectory, false, algorithms, filters);
                }
                if (input == 4) {
                    System.out.println("Pick the image file...\n");
                    final BufferedImage stego = ImageIO.read(getInputStegoFile("Pick the stego image file"));
                    final String graph = LaplaceGraph.getCSVGraph(stego);
                    final JFileChooser jfc2 = new JFileChooser();
                    jfc2.setDialogTitle("Pick a file to write the results to.");
                    System.out.println("Pick a file to write the results to...\n");
                    final int choice2 = jfc2.showSaveDialog(null);
                    if (choice2 == 0) {
                        final BufferedWriter bw = new BufferedWriter(new FileWriter(jfc2.getSelectedFile()));
                        bw.write(graph, 0, graph.length());
                        bw.close();
                    }
                    else {
                        System.out.println("Cancelling...\n");
                    }
                }
                if (input == 5) {
                    System.out.println("Pick the image file...\n");
                    final BufferedImage stego = ImageIO.read(getInputStegoFile("Pick the stego image file"));
                    final String graph = TraditionalLaplaceGraph.getCSVGraph(stego);
                    final JFileChooser jfc2 = new JFileChooser();
                    jfc2.setDialogTitle("Pick a file to write the results to.");
                    System.out.println("Pick a file to write the results to...\n");
                    final int choice2 = jfc2.showSaveDialog(null);
                    if (choice2 == 0) {
                        final BufferedWriter bw = new BufferedWriter(new FileWriter(jfc2.getSelectedFile()));
                        bw.write(graph, 0, graph.length());
                        bw.close();
                    }
                    else {
                        System.out.println("Cancelling...\n");
                    }
                }
                printOptions();
            }
            System.exit(0);
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
    
    private static void printOptions() {
        System.out.println("\nDigital Invisible Ink Toolkit Bulk Benchmarker");
        System.out.println("==============================================\n");
        System.out.println("Please pick one of the following: \n");
        System.out.println(" 1. Generate random messages.");
        System.out.println(" 2. Generate messages from a given file.");
        System.out.println(" 3. Combine two folders of images/messages.");
        System.out.println(" 4. Output a CSV form laplace graph.");
        System.out.println(" 5. Output a traditional CSV form laplace graph.");
        System.out.println(" 6. Exit the program.");
        System.out.println();
        System.out.println();
    }
    
    private static int getUserInput() {
        try {
            System.out.print(" > ");
            final String info = BulkBenchmarkRunner.stdin.readLine();
            final int results = Integer.parseInt(info);
            if (results <= -1) {
                System.exit(0);
            }
            System.out.println();
            return results;
        }
        catch (Exception e) {
            System.out.println();
            System.out.println("Error getting input, please enter a number.");
            System.out.println();
            return getUserInput();
        }
    }
    
    private static File getFolder() {
        final JFileChooser jfc = new JFileChooser();
        jfc.setDialogTitle("Pick a directory to benchmark");
        jfc.setFileSelectionMode(1);
        final int choice = jfc.showOpenDialog(null);
        if (choice == 0) {
            return jfc.getSelectedFile();
        }
        return null;
    }
    
    private static File getInputStegoFile(final String message) {
        JFileChooser jfc;
        while (true) {
            jfc = new JFileChooser();
            jfc.setDialogTitle(message);
            jfc.addChoosableFileFilter(new AFileFilter(".png", "PNG images (.png)"));
            jfc.addChoosableFileFilter(new AFileFilter(".bmp", "Bitmap images (.bmp)"));
            jfc.showOpenDialog(null);
            if (jfc.getSelectedFile().getPath().endsWith(".png") || jfc.getSelectedFile().getPath().endsWith(".bmp")) {
                break;
            }
            JOptionPane.showMessageDialog(null, "You must enter in a valid image type!", "Error! Wrong filetype!", 0);
        }
        return jfc.getSelectedFile();
    }
}
