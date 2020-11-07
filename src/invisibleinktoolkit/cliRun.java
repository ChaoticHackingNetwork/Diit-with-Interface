package invisibleinktoolkit;

import invisibleinktoolkit.algorithms.*;
import invisibleinktoolkit.stego.StegoAlgorithm;
import invisibleinktoolkit.util.MimeTypesAndExtensions;
import org.apache.commons.io.FilenameUtils;

import invisibleinktoolkit.stego.StegoImage;

import javax.imageio.ImageIO;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.URLConnection;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;

public class cliRun {

    private static char[] hexChar;

    static {
        hexChar = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
    }

    public static void main(final String[] args) {

        if (args.length < 2) {
            System.err.println("Invalid Arguments!");
            System.out.println("Help:");
            System.out.println("diit.jar autoDecode <filePath>");
            System.exit(1);
        }

        StegoImage stego = null;
        String outputPath = "";
        try {
            stego = new StegoImage(ImageIO.read(new File(args[1])));
        }
        catch (Exception e) {
            System.err.println("ERROR: Stego image is not a valid file!");
            System.exit(1);
        }

        try {
            if (args.length >= 3) {
                if (new File(args[2]).isDirectory()) {
                    outputPath = args[2];
                }
            }
        }
        catch (Exception e) {
            System.err.println("ERROR: Output directory invalid!");
            System.exit(1);
        }

        StegoAlgorithm[] algorithms = {new BattleSteg(), new BlindHide(), new FilterFirst(), new HideSeek(), new DynamicBattleSteg(), new DynamicFilterFirst()};

        for (StegoAlgorithm algorithm : algorithms) {
            boolean success = true;
            Path p = Paths.get(args[1]);
            String outputFileName = FilenameUtils.removeExtension(p.getFileName().toString());
            String algorithmName = algorithm.getClass().getSimpleName();
            String output = outputPath + outputFileName + "-" + algorithmName;
            try {
                algorithm.decode(stego, getPassword(), output);
            } catch (Exception e) {
                System.err.println("ERROR - " + algorithm.getClass().getName());
                success = false;
            }

            if (success) {
                System.out.println("Success!");
                System.out.println("Algorithm: " + algorithmName);

                try {
                    File file = new File(output);
                    URLConnection connection = file.toURI().toURL().openConnection();
                    String mimeType = connection.getContentType();
                    connection.getInputStream().close();

                    System.out.print("Output: ");
                    if (mimeType != null) {

                        String extension = MimeTypesAndExtensions.getExtensionFromMimeType(mimeType);
                        if (!extension.equals(MimeTypesAndExtensions.EXTENSION_NOT_FOUND)) {
                            File oldFile = new File(output);
                            File newFile = new File(FilenameUtils.removeExtension(output) + "." + extension);
                            if (newFile.exists()) {
                                newFile.delete();
                            }
                            if (oldFile.renameTo(newFile)) {
                                System.out.println(newFile.getPath());
                                file = newFile;
                            } else {
                                System.out.println(oldFile.getPath());
                            }
                        }

                        if (mimeType.equals("text/plain") || mimeType.equals("content/unknown")) {
                            BufferedReader br = new BufferedReader(new FileReader(file));
                            String line;
                            while ((line = br.readLine()) != null) {
                                if (line.contains("NCL") || line.contains("SKY")) {
                                    System.out.println(line);
                                }
                            }
                            br.close();
                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }

    private static long getPassword() {
        final String pass = new String("");
        if (pass == "") {
            return 0L;
        }
        try {
            final byte[] passbytes = pass.getBytes();
            final MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(passbytes);
            final byte[] md5sum = digest.digest();
            String smd5sum = toHexString(md5sum);
            smd5sum = smd5sum.substring(0, 15);
            return Long.parseLong(smd5sum, 16);
        }
        catch (Exception e) {
            return 0L;
        }
    }

    private static String toHexString(final byte[] bytestring) {
        final StringBuffer sb = new StringBuffer(bytestring.length * 2);
        for (int i = 0; i < bytestring.length; ++i) {
            sb.append(hexChar[(bytestring[i] & 0xF0) >>> 4]);
            sb.append(hexChar[bytestring[i] & 0xF]);
        }
        return sb.toString();
    }
}