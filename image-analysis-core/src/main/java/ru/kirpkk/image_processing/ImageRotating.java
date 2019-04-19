package ru.kirpkk.image_processing;

import Jama.Matrix;

import java.awt.*;
import java.awt.image.BufferedImage;

public class ImageRotating {
    static int dWidth;
    static int dHeight;

    static Matrix getForwardMatrix(double angle) {
        double[][] result = new double[3][3];
        result[0][0] = Math.cos(angle);
        result[0][1] = Math.sin(angle);
        result[0][2] = 0;
        result[1][0] = -Math.sin(angle);
        result[1][1] = Math.cos(angle);
        result[1][2] = 0;
        result[2][0] = 0;
        result[2][1] = 0;
        result[2][2] = 1;
        return new Matrix(result);
    }

    static Matrix getBackMatrix(double angle) {
        double[][] result = new double[3][3];
        result[0][0] = Math.cos(angle);
        result[0][1] = -Math.sin(angle);
        result[0][2] = 0;
        result[1][0] = Math.sin(angle);
        result[1][1] = Math.cos(angle);
        result[1][2] = 0;
        result[2][0] = 0;
        result[2][1] = 0;
        result[2][2] = 1;
        return new Matrix(result);
    }

    static BufferedImage getEmptyImage(double angle, BufferedImage image) {
        Matrix forwardMatrix = getForwardMatrix(angle);
        double[][] a = {{0, 0, 1}};
        double[][] b = {{image.getWidth(), 0, 1}};
        double[][] c = {{0, image.getHeight(), 1}};
        double[][] d = {{image.getWidth(), image.getHeight(), 1}};
        Matrix corner1 = new Matrix(a).times(forwardMatrix);
        Matrix corner2 = new Matrix(b).times(forwardMatrix);
        Matrix corner3 = new Matrix(c).times(forwardMatrix);
        Matrix corner4 = new Matrix(d).times(forwardMatrix);
        int maxX = (int) Math.max(Math.max(Math.max(corner1.get(0, 0), corner2.get(0, 0)), corner3.get(0, 0)), corner4.get(0, 0));
        int minX = (int) Math.min(Math.min(Math.min(corner1.get(0, 0), corner2.get(0, 0)), corner3.get(0, 0)), corner4.get(0, 0));
        int maxY = (int) Math.max(Math.max(Math.max(corner1.get(0, 1), corner2.get(0, 1)), corner3.get(0, 1)), corner4.get(0, 1));
        int minY = (int) Math.min(Math.min(Math.min(corner1.get(0, 1), corner2.get(0, 1)), corner3.get(0, 1)), corner4.get(0, 1));
        dWidth = minX;
        dHeight = minY;
        return new BufferedImage(maxX - minX, maxY - minY, 1);

    }

    public static BufferedImage getRotatedImage(double angle, BufferedImage image) {
        angle = Math.toRadians(angle);
        BufferedImage rotatedImage = getEmptyImage(angle, image);
        Matrix backMatrix = getBackMatrix(angle);
        for (int k = 0; k < rotatedImage.getWidth(); k++) {
            for (int l = 0; l < rotatedImage.getHeight(); l++) {
                double[][] coord = {{k + dWidth, l + dHeight, 1}};
                Matrix corner1 = new Matrix(coord).times(backMatrix);
                double x = corner1.get(0, 0);
                double y = corner1.get(0, 1);
                if (x >= 0 && x < image.getWidth() && y >= 0 && y < image.getHeight()) {
                    double dx = (x - (int) x < 0) ? x - (int) x + 1 : x - (int) x;
                    double dy = (y - (int) y < 0) ? y - (int) y + 1 : y - (int) y;
                    int[][] neighboursRed = new int[4][4];
                    int[][] neighboursGreen = new int[4][4];
                    int[][] neighboursBlue = new int[4][4];
                    for (int i = 0; i < 4; i++) {
                        for (int j = 0; j < 4; j++) {
                            try {
                                neighboursRed[i][j] = new Color(image.getRGB(i + (int) (x - dx) - 1, j + (int) (y - dy) - 1)).getRed();
                                neighboursGreen[i][j] = new Color(image.getRGB(i + (int) (x - dx) - 1, j + (int) (y - dy) - 1)).getGreen();
                                neighboursBlue[i][j] = new Color(image.getRGB(i + (int) (x - dx) - 1, j + (int) (y - dy) - 1)).getBlue();
                            } catch (ArrayIndexOutOfBoundsException e) {
                                neighboursRed[i][j] = 128;
                                neighboursGreen[i][j] = 128;
                                neighboursBlue[i][j] = 128;
                            }
                        }
                    }
                    rotatedImage.setRGB(k, l, getInterpolatedColor(neighboursRed, neighboursGreen, neighboursBlue, dx, dy));
                } else {
                    rotatedImage.setRGB(k, l, (new Color(14, 14, 14)).getRGB());
                }
            }
        }
        return rotatedImage;
    }

    public static int getInterpolatedColor(int[][] neighboursRed, int[][] neighboursGreen, int[][] neighboursBlue, double x, double y) {
        double[] b = new double[16];
        b[0] = (x - 1) * (x - 2) * (x + 1) * (y - 1) * (y - 2) * (y + 1) / 4;
        b[1] = -x * (x + 1) * (x - 2) * (y - 1) * (y - 2) * (y + 1) / 4;
        b[2] = -y * (x - 1) * (x - 2) * (x + 1) * (y + 1) * (y - 2) / 4;
        b[3] = x * y * (x + 1) * (x - 2) * (y + 1) * (y - 2) / 4;
        b[4] = -x * (x - 1) * (x - 2) * (y - 1) * (y - 2) * (y + 1) / 12;
        b[5] = -y * (x - 1) * (x - 2) * (x + 1) * (y - 1) * (y - 2) / 12;
        b[6] = x * y * (x - 1) * (x - 2) * (y + 1) * (y - 2) / 12;
        b[7] = x * y * (x + 1) * (x - 2) * (y - 1) * (y - 2) / 12;
        b[8] = x * (x - 1) * (x + 1) * (y - 1) * (y - 2) * (y + 1) / 12;
        b[9] = y * (x - 1) * (x - 2) * (x + 1) * (y - 1) * (y + 1) / 12;
        b[10] = x * y * (x - 1) * (x - 2) * (y - 1) * (y - 2) / 36;
        b[11] = -x * y * (x - 1) * (x + 1) * (y + 1) * (y - 2) / 12;
        b[12] = -x * y * (x + 1) * (x - 2) * (y - 1) * (y + 1) / 12;
        b[13] = -x * y * (x - 1) * (x + 1) * (y - 1) * (y - 2) / 36;
        b[14] = -x * y * (x - 1) * (x - 2 * (y - 1) * (y + 1)) / 36;
        b[15] = x * y * (x - 1) * (x + 1) * (y - 1) * (y + 1) / 36;

        return (new Color(getComponent(neighboursRed, b), getComponent(neighboursGreen, b), getComponent(neighboursBlue, b))).getRGB();
    }

    private static int getComponent(int[][] neighbours, double[] b) {
        int component = (int) (b[0] * neighbours[1][1] + b[1] * neighbours[1][2] +
                b[2] * neighbours[2][1] + b[3] * neighbours[2][2] +
                b[4] * neighbours[1][0] + b[5] * neighbours[0][1] +
                b[6] * neighbours[2][1] + b[7] * neighbours[0][2] +
                b[8] * neighbours[1][3] + b[9] * neighbours[3][1] +
                b[10] * neighbours[0][0] + b[11] * neighbours[2][3] +
                b[12] * neighbours[3][2] + b[13] * neighbours[0][3] +
                b[14] * neighbours[3][0] + b[15] * neighbours[3][3]);
        if (component < 0) component = 0;
        if (component > 255) component = 255;
        return component;
    }
}
