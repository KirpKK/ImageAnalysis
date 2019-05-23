package ru.kirpkk.image_processing;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.data.statistics.HistogramDataset;
import org.jfree.data.statistics.HistogramType;
import ru.kirpkk.GUI.MainForm;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by Ксения on 28.09.2017.
 */
public class ImageProcessing {
    public static void main(String args[]) {
        new MainForm(null, false, false, 1);

    }

    public static BufferedImage blend(BufferedImage image, BufferedImage second, BufferedImage alpha) {
        int width = min(image.getWidth(), second.getWidth(), alpha.getWidth());
        int height = min(image.getHeight(), second.getHeight(), alpha.getHeight());
        BufferedImage result = new BufferedImage(width, height, image.getType());
        int a;
        int rImage;
        int gImage;
        int bImage;
        int rSecond;
        int gSecond;
        int bSecond;
        int rResult;
        int gResult;
        int bResult;
        int r, g, b, gray;
        for (int i = 0;i < result.getWidth();i++) {
            for (int j = 0;j < result.getHeight();j++) {
                a = new Color(alpha.getRGB(i, j)).getRed();
                rImage = new Color(image.getRGB(i, j)).getRed();
                gImage = new Color(image.getRGB(i, j)).getGreen();
                bImage = new Color(image.getRGB(i, j)).getBlue();
                rSecond = new Color(second.getRGB(i, j)).getRed();
                gSecond = new Color(second.getRGB(i, j)).getGreen();
                bSecond = new Color(second.getRGB(i, j)).getBlue();

                rResult = (int) ((double) (rImage * a + rSecond * (255 - a)) / 255);
                gResult = (int) ((double) (gImage * a + gSecond * (255 - a)) / 255);
                bResult = (int) ((double) (bImage * a + bSecond * (255 - a)) / 255);

                result.setRGB(i, j, new Color(rResult, gResult, bResult).getRGB());
            }
        }
        return result;
    }

    public static BufferedImage fast(BufferedImage image) {
        BufferedImage im = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
        Color c;
        int r, g, b, gray;
        for (int i = 0;i < image.getWidth();i++) {
            for (int j = 0;j < image.getHeight();j++) {
                c = new Color(image.getRGB(i, j));
                r = c.getRed();
                g = c.getGreen();
                b = c.getBlue();
                gray = (r + g + b) / 3;
                c = new Color(gray, gray, gray);
                im.setRGB(i, j, c.getRGB());
            }
        }
        return im;
    }

    public static BufferedImage forHuman(BufferedImage image) {
        BufferedImage im = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
        Color c;
        int r, g, b, gray;
        for (int i = 0;i < image.getWidth();i++) {
            for (int j = 0;j < image.getHeight();j++) {
                c = new Color(image.getRGB(i, j));
                r = c.getRed();
                g = c.getGreen();
                b = c.getBlue();
                gray = (int) (0.3 * r + 0.59 * g + 0.11 * b);
                c = new Color(gray, gray, gray);
                im.setRGB(i, j, c.getRGB());
            }
        }
        return im;
    }

    public static BufferedImage desat(BufferedImage image) {
        BufferedImage im = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
        Color c;
        int r, g, b, gray;
        for (int i = 0;i < image.getWidth();i++) {
            for (int j = 0;j < image.getHeight();j++) {
                c = new Color(image.getRGB(i, j));
                r = c.getRed();
                g = c.getGreen();
                b = c.getBlue();
                gray = (min(r, g, b) + max(r, g, b)) / 2;
                c = new Color(gray, gray, gray);
                im.setRGB(i, j, c.getRGB());
            }
        }
        return im;
    }

    public static BufferedImage gradMax(BufferedImage image) {
        BufferedImage im = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
        Color c;
        int r, g, b, gray;
        for (int i = 0;i < image.getWidth();i++) {
            for (int j = 0;j < image.getHeight();j++) {
                c = new Color(image.getRGB(i, j));
                r = c.getRed();
                g = c.getGreen();
                b = c.getBlue();
                gray = max(r, g, b);
                c = new Color(gray, gray, gray);
                im.setRGB(i, j, c.getRGB());
            }
        }
        return im;
    }

    public static BufferedImage gradMin(BufferedImage image) {
        BufferedImage im = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
        Color c;
        int r, g, b, gray;
        for (int i = 0;i < image.getWidth();i++) {
            for (int j = 0;j < image.getHeight();j++) {
                c = new Color(image.getRGB(i, j));
                r = c.getRed();
                g = c.getGreen();
                b = c.getBlue();
                gray = min(r, g, b);
                c = new Color(gray, gray, gray);
                im.setRGB(i, j, c.getRGB());
            }
        }
        return im;
    }

    static int max(int a, int b, int c) {
        int max;
        max = a > b ? a : b;
        max = max > c ? max : c;
        return max;
    }

    static int min(int a, int b, int c) {
        int min;
        min = a < b ? a : b;
        min = min < c ? min : c;
        return min;
    }

    public static float[] graphBW(BufferedImage image) {
        float[] stat = new float[256];
        int color;
        int n = image.getHeight() * image.getWidth();
        for (float t : stat) {
            t = 0;
        }
        for (int i = 0;i < image.getWidth();i++) {
            for (int j = 0;j < image.getHeight();j++) {
                color = (new Color(image.getRGB(i, j))).getRed();
                stat[color] = stat[color] + (float) (1) / n;
            }
        }
        return stat;
    }

    public static BufferedImage equalization(BufferedImage image) {
        BufferedImage im = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
        float[] bw = ImageProcessing.graphBW(image);
        int i = 0;
        int j = 255;
        while (bw[i] == 0) {
            i++;
        }
        while (bw[j] == 0) {
            j--;
        }
        float n = ((float) 255) / (j - i);
        int r, gray;
        for (int k = 0;k < image.getWidth();k++) {
            for (int l = 0;l < image.getHeight();l++) {
                Color c = new Color(image.getRGB(k, l));
                r = c.getRed();
                gray = (int) ((r - i) * n);
                c = new Color(gray, gray, gray);
                im.setRGB(k, l, c.getRGB());
            }
        }
        return im;
    }

    public static BufferedImage getHistogram(BufferedImage image) {
        int size = image.getWidth() * image.getHeight();
        double[] red = new double[size];
        double[] green = new double[size];
        double[] blue = new double[size];
        for (int l = 0;l < image.getHeight();l++) {
            for (int k = 0;k < image.getWidth();k++) {
                Color c = new Color(image.getRGB(k, l));
                red[l * image.getWidth() + k] = c.getRed();
                green[l * image.getWidth() + k] = c.getGreen();
                ;
                blue[l * image.getWidth() + k] = c.getBlue();
            }
        }
        HistogramDataset data = new HistogramDataset();
        data.setType(HistogramType.RELATIVE_FREQUENCY);
        data.addSeries("red", red, 256, 0, 255);
        data.addSeries("green", green, 256, 0, 255);
        data.addSeries("blue", blue, 256, 0, 255);
        JFreeChart histogram = ChartFactory.createHistogram("Гистограмма интенсивностей", "интенсивность", "частота", data);
        BufferedImage im = histogram.createBufferedImage(700, 500, 1, null);

        return im;
    }

    public static int[][] getCooccurerenceMatrix(BufferedImage image, int dr, int dc) {
        if (dr < 0) dr = -dr;
        if (dc < 0) dc = -dc;
        int[][] matrix = new int[257][256];
        for (int[] t : matrix) {
            for (int w : t) {
                w = 0;
            }
        }
        int max = 0;
        System.out.println("--- start processing co-occurrence matrix ---");
        if (dr < image.getHeight() || dc < image.getWidth()) {
            for (int l = 0;l < image.getHeight() - dr;l++) {
                for (int k = 0;k < image.getWidth() - dc;k++) {
                    int first = new Color(image.getRGB(k, l)).getRed();
                    int second = new Color(image.getRGB(k + dc, l + dr)).getRed();
                    matrix[first][second] = matrix[first][second] + 1;
                    max = (matrix[first][second] > max) ? matrix[first][second] : max;
                }
            }
        }
        matrix[256][0] = max;
        System.out.println("--- finish processing co-occurrence matrix ---");
        return matrix;
    }

    public static BufferedImage getCooccurerenceMatrixPlot(BufferedImage image, int dr, int dc) {
        int[][] matrix = getCooccurerenceMatrix(image, dr, dc);
        int max = matrix[256][0];
        /*-----------------------------------------------------------------*/
        System.out.println("--- start build co-occurrence matrix plot ---");
        BufferedImage coocurrenceMatrix = new BufferedImage(256, 256, 1);
        for (int t = 0;t < 256;t++) {
            for (int w = 0;w < 256;w++) {
                int value = (max == 0) ? 0 : 255 - matrix[t][w] * 255 / max;
                coocurrenceMatrix.setRGB(t, w, new Color(value, value, value).getRGB());
            }

        }
        return coocurrenceMatrix;
    }

    public static double getCooccurerenceMatrixUniformity(BufferedImage image, int dr, int dc) {
        int[][] matrix = getCooccurerenceMatrix(image, dr, dc);
        int max = matrix[256][0];
        long numberOfPairs = (image.getHeight() - dr)*(image.getWidth() - dc);
        /*-----------------------------------------------------------------*/
        double uniformity = 0;
        for (int t = 0;t < 256;t++) {
            for (int w = 0;w < 256;w++) {
                uniformity = uniformity +  matrix[t][w]*matrix[t][w];
                System.out.println(uniformity);
            }
        }
        return uniformity/(numberOfPairs*numberOfPairs);
    }

    /**
     * binarize image using only red component
     * less than threshold -> black
     * more than threshold -> white
     *
     * @param image
     * @param threshold
     * @return
     */
    public static BufferedImage binary(BufferedImage image, int threshold) {
        BufferedImage im = new BufferedImage(image.getWidth(), image.getHeight(), 1);
        Color c;
        int r;
        for (int i = 0;i < image.getWidth();i++) {
            for (int j = 0;j < image.getHeight();j++) {
                c = new Color(image.getRGB(i, j));
                r = c.getRed();
                if (r > threshold) {
                    c = new Color(255,255,255,0);
                } else {
                    c = new Color(0,0,0,0);
                }
                im.setRGB(i, j, c.getRGB());
            }
        }
        return im;
    }

    public static BufferedImage dilatation(BufferedImage image, Float[][] f, int numR, int numC, int i, int j) {
        BufferedImage im = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());

        int wi = image.getWidth();
        int h = image.getHeight();
        for (int k = 0;k < wi;k++) {
            for (int l = 0;l < h;l++) {
                im.setRGB(k, l, image.getRGB(k, l));
            }
        }
        for (int k = 0;k < wi;k++) {
            for (int l = 0;l < h;l++) {
                if (f[i - 1][j - 1] == 0) {
                    if ((new Color(0x000000)).getRGB() == (image.getRGB(k, l))) {
                        for (int q = 0;q < numC;q++) {
                            for (int w = 0;w < numR;w++) {
                                int y = (l + w - j + 1);
                                int x = (k + q - i + 1);
                                if (x > 0 & y > 0 & x < wi & y < h & f[w][q] == 0) {
                                    im.setRGB(x, y, 0x000000);
                                }
                            }
                        }
                    }
                }
                if (f[i - 1][j - 1] == 1) {
                    if ((new Color(0xFFFFFF)).getRGB() == (image.getRGB(k, l))) {
                        for (int q = 0;q < numC;q++) {
                            for (int w = 0;w < numR;w++) {
                                int y = (l + w - j + 1);
                                int x = (k + q - i + 1);
                                if (x > 0 & y > 0 & x < wi & y < h & f[w][q] == 1) {
                                    im.setRGB(x, y, 0xFFFFFF);
                                }
                            }
                        }
                    }
                }
            }
        }
        return im;
    }

    public static BufferedImage erosion(BufferedImage image, Float[][] f, int numR, int numC, int i, int j) {
        BufferedImage im = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
        boolean ok;

        int wi = image.getWidth();
        int h = image.getHeight();
        for (int k = 0;k < wi;k++) {
            for (int l = 0;l < h;l++) {
                im.setRGB(k, l, image.getRGB(k, l));
            }
        }
        for (int k = 0;k < wi;k++) {
            for (int l = 0;l < h;l++) {
                if (f[i - 1][j - 1] == 0) {
                    if ((new Color(0x000000)).getRGB() == (image.getRGB(k, l))) {
                        ok = true;
                        for (int q = 0;q < numC;q++) {
                            for (int w = 0;w < numR;w++) {
                                int y = (l + w - j + 1);
                                int x = (k + q - i + 1);
                                if (x > 0 & y > 0 & x < wi & y < h & f[w][q] == 0) {
                                    if (image.getRGB(x, y) != (new Color(0x000000)).getRGB()) ok = false;
                                }
                            }
                        }
                        if (ok) im.setRGB(k, l, 0x000000);
                        else im.setRGB(k, l, 0xFFFFFF);
                    }
                }
                if (f[i - 1][j - 1] == 1) {
                    if ((new Color(0xFFFFFF)).getRGB() == (image.getRGB(k, l))) {
                        ok = true;
                        for (int q = 0;q < numC;q++) {
                            for (int w = 0;w < numR;w++) {
                                int y = (l + w - j + 1);
                                int x = (k + q - i + 1);
                                if (x > 0 & y > 0 & x < wi & y < h & f[w][q] == 1) {
                                    if (image.getRGB(x, y) != (new Color(0xFFFFFF)).getRGB()) ok = false;
                                }
                            }
                        }
                        if (ok) im.setRGB(k, l, 0xFFFFFF);
                        else im.setRGB(k, l, 0x000000);
                    }
                }
            }
        }
        return im;
    }

    public static BufferedImage locking(BufferedImage image, Float[][] f, int numR, int numC, int i, int j) {
        return erosion(dilatation(image, f, numR, numC, i, j), f, numR, numC, i, j);
    }

    public static BufferedImage unlocking(BufferedImage image, Float[][] f, int numR, int numC, int i, int j) {
        return dilatation(erosion(image, f, numR, numC, i, j), f, numR, numC, i, j);
    }
}