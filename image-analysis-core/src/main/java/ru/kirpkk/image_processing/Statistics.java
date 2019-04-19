package ru.kirpkk.image_processing;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Statistics {
    double[] red = new double[256];
    double[] green = new double[256];
    double[] blue = new double[256];
    double rMean = 0;
    double gMean = 0;
    double bMean = 0;
    double rVariance = 0;
    double gVariance = 0;
    double bVariance = 0;
    double rKurtosis = 0;
    double gKurtosis = 0;
    double bKurtosis = 0;
    double rSkewness = 0;
    double gSkewness = 0;
    double bSkewness = 0;
    double rUniformity = 0;
    double gUniformity = 0;
    double bUniformity = 0;
    double rEntropy = 0;
    double gEntropy = 0;
    double bEntropy = 0;

    public Statistics(BufferedImage image) {
        createStatistic(image);
        createMeansUniformityEntropy();
        createVariance();
        createKurtosisSkewness();
    }

    @Override
    public String toString() {
        return "rMean = " + rMean + ";\n" +
                "gMean = " + gMean + ";\n" +
                "bMean = " + bMean + ";\n" +
                "rVariance = " + rVariance + ";\n" +
                "gVariance = " + gVariance + ";\n" +
                "bVariance = " + bVariance + ";\n" +
                "rKurtosis = " + rKurtosis + ";\n" +
                "gKurtosis = " + gKurtosis + ";\n" +
                "bKurtosis = " + bKurtosis + ";\n" +
                "rSkewness = " + rSkewness + ";\n" +
                "gSkewness = " + gSkewness + ";\n" +
                "bSkewness = " + bSkewness + ";\n" +
                "rUniformity = " + rUniformity + ";\n" +
                "gUniformity = " + gUniformity + ";\n" +
                "bUniformity = " + bUniformity + ";\n" +
                "rEntropy = " + rEntropy + ";\n" +
                "gEntropy = " + gEntropy + ";\n" +
                "bEntropy = " + bEntropy;
    }

    private void createStatistic(BufferedImage image) {
        int size = image.getWidth() * image.getHeight();
        for (int t = 0;t < 256;t++) {
            red[t] = 0;
            green[t] = 0;
            blue[t] = 0;
        }
        for (int j = 0;j < image.getHeight();j++) {
            for (int i = 0;i < image.getWidth();i++) {
                Color c = new Color(image.getRGB(i, j));
                red[c.getRed()] = red[c.getRed()] + (double) (1) / size;
                green[c.getGreen()] = red[c.getGreen()] + (double) (1) / size;
                blue[c.getBlue()] = red[c.getBlue()] + (double) (1) / size;
            }
        }
    }

    private void createMeansUniformityEntropy() {
        double dl = Math.log(2);
        for (int t = 0;t < 256;t++) {
            rMean = rMean + t * red[t];
            gMean = gMean + t * green[t];
            bMean = bMean + t * blue[t];
            rUniformity = rUniformity + red[t] * red[t];
            gUniformity = gUniformity + green[t] * green[t];
            bUniformity = bUniformity + blue[t] * blue[t];
            rEntropy = (red[t] != 0) ? (rEntropy + Math.log(red[t]) * red[t] * dl) : rEntropy;
            gEntropy = (green[t] != 0) ? (gEntropy + Math.log(green[t]) * green[t] * dl) : gEntropy;
            bEntropy = (blue[t] != 0) ? (bEntropy + Math.log(blue[t]) * blue[t] * dl) : bEntropy;
        }
        rEntropy = -rEntropy / dl;
        gEntropy = -gEntropy / dl;
        bEntropy = -bEntropy / dl;
    }

    private void createVariance() {
        for (int t = 0;t < 256;t++) {
            rVariance = rVariance + (t - rMean) * (t - rMean) * red[t];
            gVariance = gVariance + (t - gMean) * (t - gMean) * green[t];
            bVariance = bVariance + (t - bMean) * (t - bMean) * blue[t];
        }
        rVariance = Math.sqrt(rVariance);
        gVariance = Math.sqrt(gVariance);
        bVariance = Math.sqrt(bVariance);
    }

    private void createKurtosisSkewness() {
        if (rVariance != 0 || gVariance != 0 || bVariance != 0)
            for (int t = 0;t < 256;t++) {
                if (rVariance != 0) {
                    rKurtosis = rKurtosis + (t - rMean) * (t - rMean) * (t - rMean) * (t - rMean) * red[t];
                    rSkewness = rSkewness + (t - rMean) * (t - rMean) * (t - rMean) * red[t];
                }
                if (gVariance != 0) {
                    gKurtosis = rKurtosis + (t - gMean) * (t - gMean) * (t - gMean) * (t - gMean) * green[t];
                    gSkewness = rSkewness + (t - gMean) * (t - gMean) * (t - gMean) * green[t];
                }
                if (bVariance != 0) {
                    bKurtosis = rKurtosis + (t - bMean) * (t - bMean) * (t - bMean) * (t - bMean) * blue[t];
                    bSkewness = rSkewness + (t - bMean) * (t - bMean) * (t - bMean) * blue[t];
                }
            }
        rKurtosis = (rVariance != 0) ? (rKurtosis / (rVariance * rVariance * rVariance * rVariance) - 3) : 999999;
        gKurtosis = (gVariance != 0) ? (gKurtosis / (gVariance * gVariance * gVariance * gVariance) - 3) : 999999;
        bKurtosis = (bVariance != 0) ? (bKurtosis / (bVariance * bVariance * bVariance * bVariance) - 3) : 999999;
        rSkewness = (rVariance != 0) ? (rSkewness / (rVariance * rVariance * rVariance)) : 999999;
        gSkewness = (gVariance != 0) ? (gSkewness / (gVariance * gVariance * gVariance)) : 999999;
        bSkewness = (bVariance != 0) ? (bSkewness / (bVariance * bVariance * bVariance)) : 999999;
    }

    public double getrMean() {
        return rMean;
    }

    public double getgMean() {
        return gMean;
    }

    public double getbMean() {
        return bMean;
    }

    public double getrVariance() {
        return rVariance;
    }

    public double getgVariance() {
        return gVariance;
    }

    public double getbVariance() {
        return bVariance;
    }

    public double getrKurtosis() {
        return rKurtosis;
    }

    public double getgKurtosis() {
        return gKurtosis;
    }

    public double getbKurtosis() {
        return bKurtosis;
    }

    public double getrSkewness() {
        return rSkewness;
    }

    public double getgSkewness() {
        return gSkewness;
    }

    public double getbSkewness() {
        return bSkewness;
    }

    public double getrUniformity() {
        return rUniformity;
    }

    public double getgUniformity() {
        return gUniformity;
    }

    public double getbUniformity() {
        return bUniformity;
    }

    public double getrEntropy() {
        return rEntropy;
    }

    public double getgEntropy() {
        return gEntropy;
    }

    public double getbEntropy() {
        return bEntropy;
    }
}
