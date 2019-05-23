package ru.kirpkk.image_processing;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.*;

public class ImageMarking {
    private static final Color BACKGROUND = new Color(0x000000);
    private static final Color OBJECT = new Color(0xFFFFFF);
    private static Stack<Integer> moments0 = new Stack<>();
    private static Stack<Double> moments10 = new Stack<>();
    private static Stack<Double> moments01 = new Stack<>();
    private static Stack<Double> moments20 = new Stack<>();
    private static Stack<Double> moments02 = new Stack<>();
    private static List<List<Pair>> objects = new ArrayList<>();
    private static int objectsNum = 0;

    public static BufferedImage markObjects(BufferedImage image) {
        try {
//            checkIsBinary(image);
            int width = image.getWidth();
            int height = image.getHeight();

            Set<Integer> colors = new HashSet<>();
            colors.add(BACKGROUND.getRGB());
            colors.add(OBJECT.getRGB());
            Set<Pair> processed = new HashSet<>();
            for (int k = 0; k < width; k++) {
                for (int l = 0; l < height; l++) {
//                    if (!colors.contains(image.getRGB(k, l))) throw new IllegalStateException("Unexpected color!");
                    if (isObject(image.getRGB(k, l))) {
                        objectsNum += 1;
                        objects.add(new ArrayList<>());
                        int newColor = getRandomColor(colors);
                        Stack<Pair> col = new Stack<>();
                        col.push(new Pair(k, l));
                        moments0.push(0);
                        moments10.push(new Double(0));
                        moments01.push(new Double(0));
                        while (!col.empty()) {
                            processed.add(col.peek());
                            for (Pair p : processObject(col.pop(), newColor, image)) {
                                if (!processed.contains(p)) col.push(p);
                            }
                        }
                        moments01.push(((double) moments01.pop()) / moments0.peek());
                        moments10.push(((double) moments10.pop()) / moments0.peek());
                        image.setRGB((int) (double) moments10.peek(), (int) (double) moments01.peek(), BACKGROUND.getRGB());
                    }
                }
            }
            getInertiaMoments();
            System.out.println(moments0);
            System.out.println(moments10);
            System.out.println(moments01);
            System.out.println(moments20);
            System.out.println(moments02);
            return image;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    private static List<Pair> processObject(Pair xy, int newColor, BufferedImage image) {
        image.setRGB(xy.getX(), xy.getY(), newColor);
        moments0.push(moments0.pop() + 1);
        moments10.push(moments10.pop() + xy.getX());
        moments01.push(moments01.pop() + xy.getY());
        objects.get(objectsNum - 1).add(xy);
        return get4Neighbours(xy, image);
    }

    private static List<Pair> get4Neighbours(Pair xy, BufferedImage image) {
        List<Pair> neighbours = new ArrayList<>();
        int x = xy.getX();
        int y = xy.getY();
        if (x - 1 >= 0 && isObject(image.getRGB(x - 1, y))) neighbours.add(new Pair(x - 1, y));
        if (x + 1 < image.getWidth() && isObject(image.getRGB(x + 1, y)))
            neighbours.add(new Pair(x + 1, y));
        if (y - 1 >= 0 && isObject(image.getRGB(x, y - 1))) neighbours.add(new Pair(x, y - 1));
        if (y + 1 < image.getHeight() && isObject(image.getRGB(x, y + 1)))
            neighbours.add(new Pair(x, y + 1));
        return neighbours;
    }

    private static int getRandomColor(Set<Integer> colors) {
        Random random = new Random();
        int newColor = random.nextInt(0xFFFFFF);
        while (colors.contains(newColor)) {
            newColor = random.nextInt(0xFFFFFF);
        }
        colors.add(newColor);
        return newColor;
    }

    private static boolean checkIsBinary(BufferedImage image) {
        for (int k = 0; k < image.getWidth(); k++) {
            for (int l = 0; l < image.getHeight(); l++) {
                if (!(isObject(image.getRGB(k, l)) || isBackground(image.getRGB(k, l))))
                    throw new IllegalStateException("Image isn't binary");
            }
        }
        return true;
    }

    private static boolean isBackground(Color color) {
        if (color.equals(BACKGROUND)) return true;
        else return false;
    }

    private static boolean isObject(Color color) {
        if (color.equals(OBJECT)) return true;
        else return false;
    }

    private static boolean isBackground(int color) {
        if (color == BACKGROUND.getRGB()) return true;
        else return false;
    }

    private static boolean isObject(int color) {
        if (color == OBJECT.getRGB()) return true;
        else return false;
    }

    private static void getInertiaMoments() {
        Stack<Double> dm10 = (Stack<Double>) moments10.clone();
        Stack<Double> dm01 = (Stack<Double>) moments01.clone();
        for (int i = objects.size() - 1; i >= 0; i--) {
            moments20.push(new Double(0));
            moments02.push(new Double(0));
            for (Pair xy : objects.get(i)) {
                moments20.push(moments20.pop() + Math.pow(xy.getX() - dm10.peek(), 2));
                moments02.push(moments02.pop() + Math.pow(xy.getY() - dm01.peek(), 2));
            }
            dm10.pop();
            dm01.pop();
        }
    }

}


class Pair {
    private int x;
    private int y;

    Pair(int x, int y) {
        this.x = x;
        this.y = y;
    }

    int getX() {
        return x;
    }

    int getY() {
        return y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Pair)) return false;
        Pair pair = (Pair) o;
        return x == pair.x &&
                y == pair.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
