package invisibleinktoolkit;

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

public class DiitDecoder {

    private static char[] hexChar;

    static {
        hexChar = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
    }

    public DiitDecoderOutput decodeAllMethods(String inputFilePath, String outputFileDirectory) {

        DiitDecoderOutput output = new DiitDecoderOutput();
        StegoImage stego = null;
        String outputPath = "";
        try {
            stego = new StegoImage(ImageIO.read(new File(inputFilePath)));
        }
        catch (Exception e) {
            output.setStatus(DiitDecoderOutput.FAILED_STATUS);
            output.setStatusOutput("Stego image is not a valid file!");
            return output;
        }

        try {
            if (new File(outputFileDirectory).isDirectory()) {
                outputPath = outputFileDirectory;
            }
        }
        catch (Exception e) {
            output.setStatus(DiitDecoderOutput.FAILED_STATUS);
            output.setStatusOutput("Output directory invalid!");
            return output;
        }

        for (DiitDecoderOutput.Algorithm algorithm : DiitDecoderOutput.Algorithm.values()) {
            boolean success = true;
            Path p = Paths.get(inputFilePath);
            String outputFileName = FilenameUtils.removeExtension(p.getFileName().toString());
            String algorithmName = algorithm.getAlgorithmClass().getSimpleName();
            String outputFilePath = outputPath + File.separator + outputFileName + "-" + algorithmName;
            try {
                ((StegoAlgorithm) algorithm.getAlgorithmClass().newInstance()).decode(stego, getPassword(), outputFilePath);
            } catch (Exception e) {
                output.addAlgorithmStatus(algorithm, DiitDecoderOutput.FAILED_STATUS, null, null);
                success = false;
            }

            if (success) {
                output.addAlgorithmStatus(algorithm, DiitDecoderOutput.SUCCESS_STATUS, null, null);

                try {
                    File file = new File(outputFilePath);
                    URLConnection connection = file.toURI().toURL().openConnection();
                    String mimeType = connection.getContentType();
                    connection.getInputStream().close();

                    if (mimeType != null) {

                        String extension = MimeTypesAndExtensions.getExtensionFromMimeType(mimeType);
                        if (!extension.equals(MimeTypesAndExtensions.EXTENSION_NOT_FOUND)) {
                            File oldFile = new File(outputFilePath);
                            File newFile = new File(FilenameUtils.removeExtension(outputFilePath) + "." + extension);
                            if (newFile.exists()) {
                                newFile.delete();
                            }
                            if (oldFile.renameTo(newFile)) {
                                output.setAlgorithmOutputFile(algorithm, newFile.getPath());
                                file = newFile;
                            } else {
                                output.setAlgorithmOutputFile(algorithm, oldFile.getPath());
                            }
                        }

                        if (mimeType.equals("text/plain") || mimeType.equals("content/unknown")) {
                            BufferedReader br = new BufferedReader(new FileReader(file));
                            String line;
                            while ((line = br.readLine()) != null) {
                                if (line.contains("NCL") || line.contains("SKY")) {
                                    output.setAlgorithmOutput(algorithm, line);
                                }
                            }
                            br.close();
                        }

                    }
                } catch (Exception e) {
                    output.setStatus(DiitDecoderOutput.FAILED_STATUS);
                    output.setStatusOutput(e.getStackTrace().toString());
                    return output;
                }
            }
        }
        output.setStatus(DiitDecoderOutput.SUCCESS_STATUS);
        return output;
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