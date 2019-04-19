package ru.kirpkk.image_processing.matrix;

import java.awt.image.BufferedImage;

/**
 * transform matrices:
 * - transposition
 * - vertical and horizontal reflection
 */
public class MatrixTransformation {
    private MatrixTransformation() {
        throw new IllegalStateException("No instance can be initialized");
    }

    public static BufferedImage transpose(BufferedImage original) {
        BufferedImage transposed = new BufferedImage(original.getHeight(), original.getWidth(), original.getType());
        for (int column = 0; column < original.getWidth(); column++) {
            for (int row = 0; row < original.getHeight(); row++) {
                transposed.setRGB(row, column, original.getRGB(column, row));
            }
        }
        return transposed;
    }

    public static BufferedImage verticalReflection(BufferedImage original) {
        BufferedImage transposed = new BufferedImage(original.getWidth(), original.getHeight(), original.getType());
        int width = original.getWidth();
        for (int column = 0; column < original.getWidth(); column++) {
            for (int row = 0; row < original.getHeight(); row++) {
                transposed.setRGB(width - column - 1, row, original.getRGB(column, row));
            }
        }
        return transposed;
    }

    public static BufferedImage horizontalReflection(BufferedImage original) {
        BufferedImage transposed = new BufferedImage(original.getWidth(), original.getHeight(), original.getType());
        int height = original.getHeight();
        for (int row = 0; row < original.getHeight(); row++) {
            for (int column = 0; column < original.getWidth(); column++) {
                transposed.setRGB(column, height - row - 1, original.getRGB(column, row));
            }
        }
        return transposed;
    }

}
