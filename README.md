# Diit-with-Interface

Added new method to automate decode process
- decodeAllMethods

Example:
```
import java.io.File;

import invisibleinktoolkit.DiitDecoder;
import invisibleinktoolkit.DiitDecoderOutput;

public class Cryptography {
   
   public static DiitDecoderOutput diitDecode(String inputfilePath, String outputFilePath) {
      File image = new File(inputfilePath);
      if (!image.exists()) {
         System.err.println("Diit Decoder Error - Invalid File");
         return null;
      }
      DiitDecoder diitDecoder = new DiitDecoder();
      DiitDecoderOutput results = diitDecoder.decodeAllMethods(inputfilePath, outputFilePath);
      if (results == null) {
         System.err.println("Diit Decoder Error - Invalid return");
         return null;
      }
   
      if (results.wasSuccessful()) {
         return results;
      } else {
         return null;
      }
   }
}
```
