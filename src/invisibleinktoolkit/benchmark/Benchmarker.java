// 
// Decompiled by Procyon v0.5.36
// 

package invisibleinktoolkit.benchmark;

import java.awt.image.BufferedImage;

public class Benchmarker
{
    private String mResultsString;
    private boolean mRunAverageAbsoluteDifference;
    private boolean mRunMeanSquaredError;
    private boolean mRunLpNorm;
    private boolean mRunLaplacianMeanSquaredError;
    private boolean mRunSignalToNoiseRatio;
    private boolean mRunPeakSignalToNoiseRatio;
    private boolean mRunNormalisedCrossCorrelation;
    private boolean mRunCorrelationQuality;
    
    public Benchmarker(final boolean runaadiff, final boolean runmserror, final boolean runlpnorm, final boolean runlpmserror, final boolean runsnr, final boolean runpeaksnr, final boolean runncc, final boolean runcquality) {
        this.mResultsString = "";
        this.mRunAverageAbsoluteDifference = runaadiff;
        this.mRunMeanSquaredError = runmserror;
        this.mRunLpNorm = runlpnorm;
        this.mRunLaplacianMeanSquaredError = runlpmserror;
        this.mRunSignalToNoiseRatio = runsnr;
        this.mRunPeakSignalToNoiseRatio = runpeaksnr;
        this.mRunNormalisedCrossCorrelation = runncc;
        this.mRunCorrelationQuality = runcquality;
    }
    
    public String run(final BufferedImage original, final BufferedImage stego) throws IllegalArgumentException, Exception {
        if (original == null) {
            throw new IllegalArgumentException("Original image must not be null!");
        }
        if (stego == null) {
            throw new IllegalArgumentException("Stego image must not be null!");
        }
        this.mResultsString = "Results of benchmark tests\n==========================\n\n";
        if (this.mRunAverageAbsoluteDifference) {
            final Benchmark bench = new AverageAbsoluteDifference();
            this.mResultsString = String.valueOf(this.mResultsString) + bench.toString() + ": " + bench.calculate(original, stego) + "\n";
        }
        if (this.mRunMeanSquaredError) {
            final Benchmark bench = new MeanSquaredError();
            this.mResultsString = String.valueOf(this.mResultsString) + bench.toString() + ": " + bench.calculate(original, stego) + "\n";
        }
        if (this.mRunLpNorm) {
            final Benchmark bench = new LpNorm();
            this.mResultsString = String.valueOf(this.mResultsString) + bench.toString() + ": " + bench.calculate(original, stego) + "\n";
        }
        if (this.mRunLaplacianMeanSquaredError) {
            final Benchmark bench = new LaplacianMeanSquaredError();
            this.mResultsString = String.valueOf(this.mResultsString) + bench.toString() + ": " + bench.calculate(original, stego) + "\n";
        }
        if (this.mRunSignalToNoiseRatio) {
            final Benchmark bench = new SignalToNoiseRatio();
            this.mResultsString = String.valueOf(this.mResultsString) + bench.toString() + ": " + bench.calculate(original, stego) + "\n";
        }
        if (this.mRunPeakSignalToNoiseRatio) {
            final Benchmark bench = new PeakSignalToNoiseRatio();
            this.mResultsString = String.valueOf(this.mResultsString) + bench.toString() + ": " + bench.calculate(original, stego) + "\n";
        }
        if (this.mRunNormalisedCrossCorrelation) {
            final Benchmark bench = new NormalisedCrossCorrelation();
            this.mResultsString = String.valueOf(this.mResultsString) + bench.toString() + ": " + bench.calculate(original, stego) + "\n";
        }
        if (this.mRunCorrelationQuality) {
            final Benchmark bench = new CorrelationQuality();
            this.mResultsString = String.valueOf(this.mResultsString) + bench.toString() + ": " + bench.calculate(original, stego) + "\n";
        }
        return this.mResultsString;
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
