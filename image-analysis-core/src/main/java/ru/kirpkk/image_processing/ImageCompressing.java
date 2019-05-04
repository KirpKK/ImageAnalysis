package ru.kirpkk.image_processing;


import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.List;

public class ImageCompressing {
    public static BufferedImage getCompressedImage(byte newBpp, BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        Set<Integer> red = new HashSet<>();
        Set<Integer> green = new HashSet<>();
        Set<Integer> blue = new HashSet<>();
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
//        return palette.stream().min(Comparator.comparingInt(value -> Math.abs(value - exact)))
//                .orElse(0);

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
        double delta = colorsArray.length / (colorsNum * 2);
        for (int i = 0; i < colorsNum; i++) {
            int index = (int) ((2 * i + 1) * delta);
            palette[i] = (colorsArray[index]);
        }
        return palette;
    }

    private static Integer[] getSortedArray(Set<Integer> colorsSet) {
        Integer[] array = new Integer[colorsSet.size()];
        Iterator iterator = colorsSet.iterator();
        for (int i = 0; i < array.length; i++) {
            array[i] = (Integer) iterator.next();
        }
        Arrays.sort(array);
        return array;
    }


}
