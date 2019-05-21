package ru.kirpkk.image_processing;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import static ru.kirpkk.image_processing.Utils.getSortedArray;

public class ImageQuantization {
    public static BufferedImage getQuantizedImage(byte newBpp, BufferedImage image) throws Exception {
        if (newBpp < 1 || newBpp > 7) throw new Exception("Incorrect new color depth");
        int width = image.getWidth();
        int height = image.getHeight();
        List<Integer> red = new ArrayList();
        List<Integer> green = new ArrayList();
        List<Integer> blue = new ArrayList();
        BufferedImage newImage = new BufferedImage(image.getWidth(), image.getHeight(), 1);
        for (int k = 0; k < width; k++) {
            for (int l = 0; l < height; l++) {
                Color color = new Color(image.getRGB(k, l));
                red.add(color.getRed());
                green.add(color.getGreen());
                blue.add(color.getBlue());
            }
        }
        Integer[] arrayRed = getSortedArray(red);
        Integer[] arrayGreen = getSortedArray(green);
        Integer[] arrayBlue = getSortedArray(blue);

        int colorsNum = (int) Math.pow(2, newBpp);
        int[] paletteRed = getPalette(arrayRed, colorsNum);
        int[] paletteGreen = getPalette(arrayGreen, colorsNum);
        int[] paletteBlue = getPalette(arrayBlue, colorsNum);

        for (int k = 0; k < width; k++) {
            for (int l = 0; l < height; l++) {
                Color color = new Color(image.getRGB(k, l));
                Color newColor = getClosestColor(color, paletteRed, paletteGreen, paletteBlue);
                newImage.setRGB(k, l, newColor.getRGB());
                int errorR = (color.getRed() - newColor.getRed()) / 16;
                int errorG = (color.getGreen() - newColor.getGreen()) / 16;
                int errorB = (color.getBlue() - newColor.getBlue()) / 16;

                diffuseError(image, k, l, errorR, errorG, errorB);
            }
        }
        return newImage;
    }

    private static Color getClosestColor(Color color, int[] paletteR, int[] paletteG, int[] paletteB) {
        int r = color.getRed();
        int g = color.getGreen();
        int b = color.getBlue();
        return new Color(getClosest(r, paletteR), getClosest(g, paletteG), getClosest(b, paletteB));
    }

    private static int getClosest(int exact, int[] palette) {
        int delta = Math.abs(palette[0] - exact);
        for (int i = 0; i < palette.length; i++) {
            int newDelta = Math.abs(palette[i] - exact);
            if (newDelta > delta) {
                return palette[i - 1];
            }
            delta = newDelta;
        }
        return palette[palette.length - 1];
    }

    private static int[] getPalette(Integer[] colorsArray, int colorsNum) {
        int[] palette = new int[colorsNum];
        palette[0] = colorsArray[0];
        palette[colorsNum - 1] = colorsArray[colorsArray.length - 1];
        double delta = (colorsArray.length) / (colorsNum - 1);
        for (int i = 0; i < colorsNum - 2; i++) {
            int index = (int) ((i + 1) * delta);
            palette[i + 1] = (colorsArray[index]);
        }
        return palette;
    }

    private static void diffuseError(BufferedImage image, int indexX, int indexY, int errorR, int errorG, int errorB) {
        diffuseErrorToPixel(image, indexX + 1, indexY, errorR, errorG, errorB, 7);
        diffuseErrorToPixel(image, indexX - 1, indexY + 1, errorR, errorG, errorB, 3);
        diffuseErrorToPixel(image, indexX, indexY + 1, errorR, errorG, errorB, 5);
        diffuseErrorToPixel(image, indexX + 1, indexY + 1, errorR, errorG, errorB, 1);
    }

    private static void diffuseErrorToPixel(BufferedImage image, int indexX, int indexY, int errorR, int errorG, int errorB, int k) {
        if (indexX < image.getWidth() && indexY < image.getHeight() && indexX > -1) {
            Color c = new Color(image.getRGB(indexX, indexY));
            int newR = c.getRed() + k * errorR;
            int newG = c.getGreen() + k * errorG;
            int newB = c.getBlue() + k * errorB;
            if (newR < 0) newR = 0;
            if (newG < 0) newG = 0;
            if (newB < 0) newB = 0;
            if (newR > 255) newR = 255;
            if (newG > 255) newG = 255;
            if (newB > 255) newB = 255;
            Color newColor = new Color(newR, newG, newB);
            image.setRGB(indexX, indexY, newColor.getRGB());
        }
    }
}
