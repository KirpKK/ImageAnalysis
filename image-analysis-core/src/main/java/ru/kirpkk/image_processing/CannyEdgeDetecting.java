package ru.kirpkk.image_processing;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.*;

import static ru.kirpkk.image_processing.ImageFiltering.convolution;

public class CannyEdgeDetecting {
    private final int SIGMA;
    private final int WEAK;
    private final int STRONG;


    public CannyEdgeDetecting(int sigma, int weak, int strong) {
        this.SIGMA = sigma;
        this.WEAK = weak;
        this.STRONG = strong;
    }

    public BufferedImage processCanny(BufferedImage origin) {
        BufferedImage image;
        Float[][] gaussianBlur = createGaussianFilter();
        int size = gaussianBlur.length;
        image = ImageProcessing.desat(origin);
        image = convolution(image, gaussianBlur, size, size, (size - 1) / 2 + 1, (size - 1) / 2 + 1);
        Pair<Float>[][] sobelMatrix = convolutionSobel(image);
        Pair<Float>[][] nmsMatrix = nonMaximalSuppression(sobelMatrix, origin.getWidth(), origin.getHeight());
        Pair<Float>[][] tracedMatrix = traceWeakAndStrong(nmsMatrix, origin.getWidth(), origin.getHeight());
        return createImage(tracedMatrix, origin.getWidth(), origin.getHeight());
    }

    private Float[][] createGaussianFilter() {
        int size = ((3 * SIGMA) % 2 == 1) ? (3 * SIGMA) : 3 * SIGMA + 1;
        int radius = (size - 1) / 2;
        Float[][] kernel = new Float[size][size];
        float denominator = 0;
        for (int i = -radius; i <= radius; i++) {
            for (int j = -radius; j <= radius; j++) {
                kernel[i + radius][j + radius] = gauss(i, j);
                denominator += kernel[i + radius][j + radius];
            }
        }
        for (int i = -radius; i <= radius; i++) {
            for (int j = -radius; j <= radius; j++) {
                kernel[i + radius][j + radius] = kernel[i + radius][j + radius] / denominator;
            }
        }
        return kernel;
    }

    private float gauss(int i, int j) {
        return (float) Math.exp(-((float) (i * i + j * j)) / (2 * SIGMA * SIGMA));
    }

    public static Pair[][] convolutionSobel(BufferedImage image) {
        final Float[][] SOBEL_X = {{-1f, 0f, 1f}, {-2f, 0f, 2f}, {-1f, 0f, 1f}};
        final Float[][] SOBEL_Y = {{-1f, -2f, -1f}, {0f, 0f, 0f}, {1f, 2f, 1f}};
        int wi = image.getWidth();
        int h = image.getHeight();
        Pair<Float>[][] result = new Pair[h][wi];
        Color c;
        for (int k = 0; k < wi; k++) {
            for (int l = 0; l < h; l++) {
                float rX = 0;
                float rY = 0;
                for (int q = 0; q < 3; q++) {
                    for (int w = 0; w < 3; w++) {
                        int y = (h + l + w - 1) % h;
                        int x = (wi + k + q - 1) % wi;
                        c = new Color(image.getRGB(x, y));
                        rX = rX + c.getRed() * SOBEL_X[w][q];
                        rY = rY + c.getRed() * SOBEL_Y[w][q];
                    }
                }
                result[l][k] = new Pair((float) Math.sqrt(rX * rX + rY * rY), getAngle(rX, rY));
            }
        }
        return result;
    }

    private static float getAngle(float rX, float rY) {
        return ((int) Math.toDegrees(Math.atan(rX / rY)) / 45) * 45;
    }

    private Pair<Float>[][] nonMaximalSuppression(Pair<Float>[][] sobelMatrix, int width, int height) {
        Pair<Float>[][] nmsMatrix = new Pair[height][width];
        for (int x = 1; x < width - 1; x++) {
            for (int y = 1; y < height - 1; y++) {
                //  |
                if (sobelMatrix[y][x].getY() == 90f || sobelMatrix[y][x].getY() == -90f) {
                    if (sobelMatrix[y + 1][x].getX() < sobelMatrix[y][x].getX()
                            && sobelMatrix[y - 1][x].getX() < sobelMatrix[y][x].getX()) {
                        if (isStrong(sobelMatrix[y][x].getX()) != 0) {
                            nmsMatrix[y][x] = new Pair(sobelMatrix[y][x].getX(), isStrong(sobelMatrix[y][x].getX()));
                        } else {
                            nmsMatrix[y][x] = new Pair(0f, 0f);
                        }
                    } else {
                        nmsMatrix[y][x] = new Pair(0f, 0f);
                    }
                }
                //  /
                else if (sobelMatrix[y][x].getY() == 45f) {
                    if (sobelMatrix[y - 1][x + 1].getX() < sobelMatrix[y][x].getX()
                            && sobelMatrix[y + 1][x - 1].getX() < sobelMatrix[y][x].getX()) {
                        if (isStrong(sobelMatrix[y][x].getX()) != 0) {
                            nmsMatrix[y][x] = new Pair(sobelMatrix[y][x].getX(), isStrong(sobelMatrix[y][x].getX()));
                        } else {
                            nmsMatrix[y][x] = new Pair(0f, 0f);
                        }
                    } else {
                        nmsMatrix[y][x] = new Pair(0f, 0f);
                    }
                }
                //  -
                else if (sobelMatrix[y][x].getY() == 0f) {
                    if (sobelMatrix[y][x + 1].getX() < sobelMatrix[y][x].getX()
                            && sobelMatrix[y][x - 1].getX() < sobelMatrix[y][x].getX()) {
                        if (isStrong(sobelMatrix[y][x].getX()) != 0) {
                            nmsMatrix[y][x] = new Pair(sobelMatrix[y][x].getX(), isStrong(sobelMatrix[y][x].getX()));
                        } else {
                            nmsMatrix[y][x] = new Pair(0f, 0f);
                        }
                    } else {
                        nmsMatrix[y][x] = new Pair(0f, 0f);
                    }
                    //  \
                } else if (sobelMatrix[y][x].getY() == -45f) {
                    if (sobelMatrix[y + 1][x + 1].getX() < sobelMatrix[y][x].getX()
                            && sobelMatrix[y - 1][x - 1].getX() < sobelMatrix[y][x].getX()) {
                        if (isStrong(sobelMatrix[y][x].getX()) != 0) {
                            nmsMatrix[y][x] = new Pair(sobelMatrix[y][x].getX(), isStrong(sobelMatrix[y][x].getX()));
                        } else {
                            nmsMatrix[y][x] = new Pair(0f, 0f);
                        }
                    } else {
                        nmsMatrix[y][x] = new Pair(0f, 0f);
                    }
                } else
                    throw new IllegalStateException("angle must be multiple 45 degrees, but is " + sobelMatrix[y][x].getY()
                            + " for x=" + x + " y=" + y);
            }
        }
        return nmsMatrix;
    }

    /**
     * @param amplitude
     * @return 0 - suppressed, 1 - weak, 2 - strong
     */
    private float isStrong(float amplitude) {
        if (amplitude < WEAK) return 0;
        if (amplitude > STRONG) return 2;
        return 1;
    }

    Pair<Float>[][] traceWeakAndStrong(Pair<Float>[][] nmsMatrix, int width, int height) {
        Set<Pair> processed = new HashSet<>();
        for (int x = 1; x < width - 1; x++) {
            for (int y = 1; y < height - 1; y++) {
                if (nmsMatrix[y][x].getY() == 2f) {
                    Stack<Pair> col = new Stack<>();
                    col.push(new Pair(x, y));

                    while (!col.empty()) {
                        processed.add(col.peek());
                        for (Object p : processObject(col.pop(), nmsMatrix, width, height)) {
                            if (!processed.contains(p)) col.push((Pair) p);
                        }
                    }
                }
            }
        }
        return nmsMatrix;
    }

    private static java.util.List<Pair<Integer>> processObject(Pair<Integer> xy, Pair<Float>[][] nmsMatrix, int width, int height) {
        nmsMatrix[xy.getY()][xy.getX()].setY(2f);
        return get4Neighbours(xy, nmsMatrix, width, height);
    }

    private static java.util.List<Pair<Integer>> get4Neighbours(Pair<Integer> xy, Pair<Float>[][] nmsMatrix, int width, int height) {
        List<Pair<Integer>> neighbours = new ArrayList<>();
        int x = xy.getX();
        int y = xy.getY();
        if (x - 1 >= 1 && y >= 1 && y < height - 2 && (nmsMatrix[y][x - 1].getY() == 1f || nmsMatrix[y][x - 1].getY() == 2f))
            neighbours.add(new Pair<>(x - 1, y));
        if (x + 1 < width - 2 && y >= 1 && y < height - 2 && (nmsMatrix[y][x + 1].getY() == 1f || nmsMatrix[y][x + 1].getY() == 2f))
            neighbours.add(new Pair(x + 1, y));
        if (y - 1 >= 1 && x >= 1 && x < width - 2 && (nmsMatrix[y - 1][x].getY() == 1f || nmsMatrix[y - 1][x].getY() == 2f))
            neighbours.add(new Pair(x, y - 1));
        if (y + 1 < height - 2 && x >= 1 && x < width - 2 && (nmsMatrix[y + 1][x].getY() == 1f || nmsMatrix[y + 1][x].getY() == 2f))
            neighbours.add(new Pair(x, y + 1));
        return neighbours;
    }

    private BufferedImage createImage(Pair<Float>[][] tracedMatrix, int width, int height) {
        BufferedImage image = new BufferedImage(width, height, 1);
        final int BLACK = new Color(0, 0, 0).getRGB();
        final int WHITE = new Color(255, 255, 255).getRGB();
        for (int x = 1; x < width - 1; x++) {
            for (int y = 1; y < height - 1; y++) {
                if (tracedMatrix[y][x].getY() == 2f) image.setRGB(x, y, WHITE);
                else image.setRGB(x, y, BLACK);
            }
        }
        return image;
    }

}
