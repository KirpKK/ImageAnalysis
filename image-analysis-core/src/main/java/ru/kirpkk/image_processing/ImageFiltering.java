package ru.kirpkk.image_processing;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import static ru.kirpkk.image_processing.Utils.getSortedArray;

public class ImageFiltering {
    public static BufferedImage convolution(BufferedImage image, Float[][] f, int numR, int numC,
                                            int i, int j) {
        BufferedImage im = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
        int r, g, b;
        Color c;

        int wi = image.getWidth();
        int h = image.getHeight();
        for (int k = 0; k < wi; k++) {
            for (int l = 0; l < h; l++) {
                r = 0;
                g = 0;
                b = 0;

                for (int q = 0; q < numC; q++) {
                    for (int w = 0; w < numR; w++) {
                        int y = (h + l + w - i + 1) % h;
                        int x = (wi + k + q - j + 1) % wi;
                        c = new Color(image.getRGB(x, y));
                        r = r + (int) (c.getRed() * f[w][q]);
                        g = g + (int) (c.getGreen() * f[w][q]);
                        b = b + (int) (c.getBlue() * f[w][q]);
                    }
                }
                if (r < 0) r = 0;
                if (r > 255) r = 255;
                if (g < 0) g = 0;
                if (g > 255) g = 255;
                if (b < 0) b = 0;
                if (b > 255) b = 255;
                c = new Color(r, g, b);
                im.setRGB(k, l, c.getRGB());
            }
        }
        return im;
    }

    public static BufferedImage rankFilter(BufferedImage image, int[][] aperture, int numR, int numC,
                                           int i, int j, int rank) throws Exception {
        int sum = 0;
        for (int q = 0; q < numC; q++) {
            for (int w = 0; w < numR; w++) {
                sum += aperture[w][q];

            }
        }
        if (rank < 1 || rank > sum) throw new Exception("Incorrect rank. Rank shold be between 1 and " + sum);
        BufferedImage im = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
        Color c;

        int wi = image.getWidth();
        int h = image.getHeight();
        for (int k = 0; k < wi; k++) {
            for (int l = 0; l < h; l++) {
                List<Integer> red = new ArrayList();
                List<Integer> green = new ArrayList();
                List<Integer> blue = new ArrayList();
                for (int q = 0; q < numC; q++) {
                    for (int w = 0; w < numR; w++) {
                        int y = (h + l + w - i + 1) % h;
                        int x = (wi + k + q - j + 1) % wi;
                        c = new Color(image.getRGB(x, y));
                        if (aperture[w][q] > 0) {
                            for (int n = 0; n < aperture[w][q]; n++) {
                                red.add(c.getRed());
                                green.add(c.getGreen());
                                blue.add(c.getBlue());
                            }
                        }
                    }
                }
                Integer[] arrayRed = getSortedArray(red);
                Integer[] arrayGreen = getSortedArray(green);
                Integer[] arrayBlue = getSortedArray(blue);
                c = new Color(arrayRed[rank - 1], arrayGreen[rank - 1], arrayBlue[rank - 1]);
                im.setRGB(k, l, c.getRGB());
            }
        }
        return im;
    }
}
