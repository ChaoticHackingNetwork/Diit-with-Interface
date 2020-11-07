// 
// Decompiled by Procyon v0.5.36
// 

package invisibleinktoolkit.util;

import java.io.Reader;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.OutputStream;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import invisibleinktoolkit.stego.StegoImage;
import invisibleinktoolkit.stego.InsertableMessage;
import invisibleinktoolkit.stego.CoverImage;
import invisibleinktoolkit.filters.Filter;
import invisibleinktoolkit.filters.Filterable;
import invisibleinktoolkit.stego.StegoAlgorithm;
import java.io.IOException;
import java.io.Writer;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.Random;
import java.io.File;

public final class TestingUtils
{
    public static final void createRandomMessages(final File folder, final int startsize, final int increment, final int nummessages) throws IllegalArgumentException, IOException {
        if (!folder.isDirectory()) {
            throw new IllegalArgumentException("Passed file is not a folder!");
        }
        if (startsize <= 0) {
            throw new IllegalArgumentException("Start size must be greater than zero!");
        }
        if (increment <= 0) {
            throw new IllegalArgumentException("Increment must be greater than zero!");
        }
        if (nummessages <= 0) {
            throw new IllegalArgumentException("Number of message must be greater than zero");
        }
        final Random rangen = new Random(System.currentTimeMillis());
        File output = new File(folder, "message" + startsize + "K.txt");
        BufferedWriter message = new BufferedWriter(new FileWriter(output));
        for (int i = 0; i < nummessages; ++i) {
            output = new File(folder, "message" + (startsize + increment * i) + "K.txt");
            message = new BufferedWriter(new FileWriter(output));
            final byte[] randomtext = new byte[(startsize + increment * i) * 1024];
            rangen.nextBytes(randomtext);
            for (int j = 0; j < randomtext.length; ++j) {
                message.write((char)randomtext[j]);
            }
            message.close();
        }
    }
    
    public static final void combineFolders(final File imagefolder, final File messagefolder, final File outputfolder, final boolean bmpformat, final String[] algorithms, final String[] filters) throws IllegalArgumentException {
        String outformat = "png";
        if (bmpformat) {
            outformat = "bmp";
        }
        if (!imagefolder.isDirectory() || !messagefolder.isDirectory() || !outputfolder.isDirectory()) {
            throw new IllegalArgumentException("Not all passed files are folders!");
        }
        if (algorithms.length == 0) {
            throw new IllegalArgumentException("You must pass some algorithms to use!");
        }
        final String[] messagelist = messagefolder.list();
        final String[] imagelist = imagefolder.list();
        final String fileseparator = System.getProperty("file.separator");
        for (int i = 0; i < imagelist.length; ++i) {
            if (imagelist[i].endsWith(".bmp") || imagelist[i].endsWith(".jpg") || imagelist[i].endsWith(".png")) {
                final String coverfilepath = String.valueOf(imagefolder.getPath()) + fileseparator + imagelist[i];
                final String originalname = imagelist[i].substring(0, imagelist[i].indexOf("."));
                for (int j = 0; j < messagelist.length; ++j) {
                    if (messagelist[j].endsWith(".txt")) {
                        final String messagefilepath = String.valueOf(messagefolder.getPath()) + fileseparator + messagelist[j];
                        for (int k = 0; k < algorithms.length; ++k) {
                            try {
                                final StegoAlgorithm alg = (StegoAlgorithm)Class.forName(algorithms[k]).newInstance();
                                if (alg instanceof Filterable) {
                                    for (int l = 0; l < filters.length; ++l) {
                                        ((Filterable)alg).setFilter((Filter)Class.forName(filters[l]).newInstance());
                                        final CoverImage cimage = new CoverImage(coverfilepath);
                                        final InsertableMessage imess = new InsertableMessage(messagefilepath);
                                        final String outputpath = String.valueOf(originalname) + "~" + messagelist[j].substring(0, messagelist[j].lastIndexOf(".")) + "-" + algorithms[k].toLowerCase().substring(algorithms[k].lastIndexOf(".") + 1, algorithms[k].length()) + "-" + filters[l].toLowerCase().substring(filters[l].lastIndexOf(".") + 1, filters[l].length()) + "." + outformat;
                                        System.out.println("Outputting... " + outputpath);
                                        final StegoImage stego = alg.encode(imess, cimage, 0L);
                                        stego.write(outformat, new File(outputfolder, outputpath));
                                    }
                                }
                                else {
                                    final CoverImage cimage = new CoverImage(coverfilepath);
                                    final InsertableMessage imess = new InsertableMessage(messagefilepath);
                                    final String outputpath = String.valueOf(originalname) + "~" + messagelist[j].substring(0, messagelist[j].lastIndexOf(".")) + "-" + algorithms[k].toLowerCase().substring(algorithms[k].lastIndexOf(".") + 1, algorithms[k].length()) + "." + outformat;
                                    System.out.println("Outputting... " + outputpath);
                                    final StegoImage stego2 = alg.encode(imess, cimage, 0L);
                                    stego2.write(outformat, new File(outputfolder, outputpath));
                                }
                            }
                            catch (Exception e) {
                                System.out.println("Error processing image. Skipping...");
                            }
                        }
                    }
                }
            }
        }
    }
    
    public static boolean copyIntoTempFolder(final File sourcedir, final File stegdir, final File outputdir) throws IOException, SecurityException {
        if (!outputdir.exists()) {
            outputdir.mkdir();
        }
        if (!sourcedir.exists() || !stegdir.exists()) {
            return false;
        }
        final String[] sourcelist = sourcedir.list();
        final String[] steglist = stegdir.list();
        final byte[] abyte = new byte[10000];
        int read = 0;
        if (sourcedir != outputdir) {
            for (int i = 0; i < sourcelist.length; ++i) {
                final BufferedInputStream fis = new BufferedInputStream(new FileInputStream(new File(sourcedir, sourcelist[i])));
                final BufferedOutputStream fos = new BufferedOutputStream(new FileOutputStream(new File(outputdir, sourcelist[i])));
                for (read = fis.read(abyte, 0, abyte.length); read != -1; read = fis.read(abyte, 0, abyte.length)) {
                    fos.write(abyte, 0, read);
                }
                fis.close();
                fos.close();
            }
        }
        if (stegdir != outputdir) {
            for (int i = 0; i < steglist.length; ++i) {
                final BufferedInputStream fis = new BufferedInputStream(new FileInputStream(new File(stegdir, steglist[i])));
                final BufferedOutputStream fos = new BufferedOutputStream(new FileOutputStream(new File(outputdir, steglist[i])));
                for (read = fis.read(abyte, 0, abyte.length); read != -1; read = fis.read(abyte, 0, abyte.length)) {
                    fos.write(abyte, 0, read);
                }
                fis.close();
                fos.close();
            }
        }
        return true;
    }
    
    public static final void cutTextIntoMessages(final File textfile, final File folder, final int startsize, final int increment, final int nummessages) throws IllegalArgumentException, IOException {
        if (!textfile.exists()) {
            throw new IllegalArgumentException("Text file does not exist!");
        }
        if (!folder.isDirectory()) {
            throw new IllegalArgumentException("Passed file is not a folder!");
        }
        if (startsize <= 0) {
            throw new IllegalArgumentException("Start size must be greater than zero!");
        }
        if (increment <= 0) {
            throw new IllegalArgumentException("Increment must be greater than zero!");
        }
        if (nummessages <= 0) {
            throw new IllegalArgumentException("Number of message must be greater than zero");
        }
        for (int i = 0; i < nummessages; ++i) {
            final File output = new File(folder, "smessage" + (startsize + increment * i) + "K.txt");
            final BufferedWriter message = new BufferedWriter(new FileWriter(output));
            final BufferedReader tfile = new BufferedReader(new FileReader(textfile));
            final char[] structuredtext = new char[(startsize + increment * i) * 1024];
            for (int read = tfile.read(structuredtext, 0, structuredtext.length), j = 0; j < read; ++j) {
                message.write(structuredtext[j]);
            }
            tfile.close();
            message.close();
        }
    }
}
