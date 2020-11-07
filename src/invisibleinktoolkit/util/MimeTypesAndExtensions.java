package invisibleinktoolkit.util;

import java.util.HashMap;

public class MimeTypesAndExtensions {

    static HashMap<String, String> TheHashMap = new HashMap<String, String>();
    public static final String EXTENSION_NOT_FOUND = "Extension Not Found";

    public MimeTypesAndExtensions() {
        super();
    }

    static {
        CreateHashMap();
    }

    private static void CreateHashMap() {
        TheHashMap.put("text/plain", "txt");
        TheHashMap.put("image/png", "png");
        TheHashMap.put("image/jpeg", "jpeg");
        TheHashMap.put("image/gif", "gif");
        TheHashMap.put("image/bmp", "bmp");
        TheHashMap.put("application/pdf", "pdf");
        TheHashMap.put("application/zip", "zip");
        TheHashMap.put("audio/mpeg", "mp3");
        TheHashMap.put("content/unknown", "txt");
    }

    public static String getExtensionFromMimeType(String TheMimeType) {

        String TheExtension = EXTENSION_NOT_FOUND;
        for(String key: TheHashMap.keySet()){
            if(key.toLowerCase().equals(TheMimeType.toLowerCase())) {
                TheExtension = TheHashMap.get(key);
                return TheExtension;
            }
        }

        return TheExtension;
    }

    public static String getMimeTypeFromExtension(String TheExtension) {

        StringBuilder TheMimeTypes = new StringBuilder();
        for(String key: TheHashMap.keySet()){
            if(TheHashMap.get(key.toLowerCase()).equals(TheExtension.toLowerCase())) {
                TheMimeTypes.append(key.concat("\n"));
            }
        }

        if(TheMimeTypes.length() == 0)
            return "MimeType Not Found";

        return TheMimeTypes.toString();
    }

    public static String getMimeTypesAndExtensions(){

        StringBuilder TheMimeTypesAndExtensions = new StringBuilder();
        for(String key: TheHashMap.keySet()){
            TheMimeTypesAndExtensions.append(TheHashMap.get(key).concat("   ----->   ").concat(key).concat("\n"));
        }

        return TheMimeTypesAndExtensions.toString();
    }
}