package invisibleinktoolkit;

import invisibleinktoolkit.algorithms.*;

import java.util.ArrayList;
import java.util.List;

public class DiitDecoderOutput {

    public enum Algorithm {
        BATTLE_STEG("Battle Steg", BattleSteg.class),
        BLIND_HIDE("Blind Hide", BlindHide.class),
        DYNAMIC_BATTLE_STEG("Dynamic Battle Steg", DynamicBattleSteg.class),
        DYAMIC_FILTER_FIRST("Dynamic Filter First", DynamicFilterFirst.class),
        FILTER_FIRST("Filter First", FilterFirst.class),
        HIDE_SEEK("Hide Seek", HideSeek.class);

        private String name;
        private Class algorithmClass;

        Algorithm(String name, Class algorithm) {
            this.name = name;
            this.algorithmClass = algorithm;
        }

        public String getName() {
            return name;
        }

        public Class getAlgorithmClass() {
            return algorithmClass;
        }

        public static Algorithm getAlgorithm(String algorithmName) {
            for (Algorithm algorithm : Algorithm.values()) {
                if (algorithm.name == algorithmName) {
                    return algorithm;
                }
            }
            return null;
        }

        public static Algorithm getAlgorithm(Class algorithmClass) {
            for (Algorithm algorithm : Algorithm.values()) {
                if (algorithm.algorithmClass == algorithmClass) {
                    return algorithm;
                }
            }
            return null;
        }
    }

    public static String FAILED_STATUS = "FAILED";
    public static String SUCCESS_STATUS = "SUCCESS";
    private static final int FIELD_COUNT = 4;
    private static final int NAME_FIELD = 0;
    private static final int STATUS_FIELD = 1;
    private static final int OUTPUT_FIELD = 2;
    private static final int FILE_FIELD = 3;
    private String status;
    private String statusOutput;
    private String[][] algorithmStatus = new String[Algorithm.values().length][FIELD_COUNT];

    DiitDecoderOutput() {
    }

    public Boolean wasSuccessful() {
        return status.equals(SUCCESS_STATUS);
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusOutput() {
        return statusOutput;
    }

    public void setStatusOutput(String statusOutput) {
        this.statusOutput = statusOutput;
    }

    public void addAlgorithmStatus(Algorithm algorithm, String status, String output, String file) {
        algorithmStatus[algorithm.ordinal()][NAME_FIELD] = algorithm.getName();
        algorithmStatus[algorithm.ordinal()][STATUS_FIELD] = status;
        algorithmStatus[algorithm.ordinal()][OUTPUT_FIELD] = output;
        algorithmStatus[algorithm.ordinal()][FILE_FIELD] = file;
    }

    public void setAlgorithmOutput(Algorithm algorithm, String output) {
        algorithmStatus[algorithm.ordinal()][OUTPUT_FIELD] = output;
    }

    public void setAlgorithmOutputFile(Algorithm algorithm, String filePath) {
        algorithmStatus[algorithm.ordinal()][FILE_FIELD] = filePath;
    }

    public List<String[]> getAlgorithmStatus() {
        ArrayList<String[]> status = new ArrayList<>();
        for (int i = 0; i < algorithmStatus.length; i++) {
            status.add(algorithmStatus[i]);
        }
        return status;
    }

    public String[] getAlgorithmStatus(String algorithmName) {
        return algorithmStatus[(Algorithm.getAlgorithm(algorithmName)).ordinal()];
    }
}
