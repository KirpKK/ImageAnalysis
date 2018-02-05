package ImageProcessing;

import GUI.MainForm;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by Ксения on 28.09.2017.
 */
public class ImageProcessing {
    public static void main(String args[]){
        new MainForm(null, false,false, 1);

    }
    public static BufferedImage fast(BufferedImage image){
        BufferedImage im = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
        Color c;
        int r,g,b,gray;
        for (int i = 0; i < image.getWidth(); i++) {
            for (int j = 0; j < image.getHeight(); j++) {
                c = new Color(image.getRGB(i,j));
                r = c.getRed();
                g = c.getGreen();
                b = c.getBlue();
                gray = (r+g+b)/3;
                c = new Color(gray,gray,gray);
                im.setRGB(i,j,c.getRGB());
            }
        }
        return im;
    }

    public static BufferedImage forHuman(BufferedImage image){
        BufferedImage im = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
        Color c;
        int r,g,b,gray;
        for (int i = 0; i < image.getWidth(); i++) {
            for (int j = 0; j < image.getHeight(); j++) {
                c = new Color(image.getRGB(i,j));
                r = c.getRed();
                g = c.getGreen();
                b = c.getBlue();
                gray = (int)(0.3*r+0.59*g+0.11*b);
                c = new Color(gray,gray,gray);
                im.setRGB(i,j,c.getRGB());
            }
        }
        return im;
    }

    public static BufferedImage desat(BufferedImage image){
        BufferedImage im = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
        Color c;
        int r,g,b,gray;
        for (int i = 0; i < image.getWidth(); i++) {
            for (int j = 0; j < image.getHeight(); j++) {
                c = new Color(image.getRGB(i,j));
                r = c.getRed();
                g = c.getGreen();
                b = c.getBlue();
                gray = (min(r,g,b) + max(r,g,b))/2;
                c = new Color(gray,gray,gray);
                im.setRGB(i,j,c.getRGB());
            }
        }
        return im;
    }

    public static BufferedImage gradMax(BufferedImage image){
        BufferedImage im = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
        Color c;
        int r,g,b,gray;
        for (int i = 0; i < image.getWidth(); i++) {
            for (int j = 0; j < image.getHeight(); j++) {
                c = new Color(image.getRGB(i,j));
                r = c.getRed();
                g = c.getGreen();
                b = c.getBlue();
                gray = max(r,g,b);
                c = new Color(gray,gray,gray);
                im.setRGB(i,j,c.getRGB());
            }
        }
        return im;
    }

    public static BufferedImage gradMin(BufferedImage image){
        BufferedImage im = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
        Color c;
        int r,g,b,gray;
        for (int i = 0; i < image.getWidth(); i++) {
            for (int j = 0; j < image.getHeight(); j++) {
                c = new Color(image.getRGB(i,j));
                r = c.getRed();
                g = c.getGreen();
                b = c.getBlue();
                gray = min(r,g,b);
                c = new Color(gray,gray,gray);
                im.setRGB(i,j,c.getRGB());
            }
        }
        return im;
    }

    static int max(int a, int b, int c){
        int max;
        max = a>b?a:b;
        max = max>c?max:c;
        return max;
    }

    static int min(int a, int b, int c){
        int min;
        min = a<b?a:b;
        min = min<c?min:c;
        return min;
    }

    public static float[] graphBW(BufferedImage image) {
        float[] stat = new float[256];
        int color;
        int n = image.getHeight()*image.getWidth();
        for (float t:stat) {t = 0;}
        for (int i = 0; i < image.getWidth(); i++) {
            for (int j = 0; j < image.getHeight(); j++) {
                color = (new Color(image.getRGB(i,j))).getRed();
                stat[color] = stat[color] + (float)(1)/n;
            }
        }
        return stat;
    }

    public  static BufferedImage equalization(BufferedImage image) {
        BufferedImage im = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
        float[] bw = ImageProcessing.graphBW(image);
        int i =0;
        int j = 255;
        while (bw[i] == 0) {
            i++;
        }
        while (bw[j] == 0) {
            j--;
        }
        float n = ((float) 255)/(j - i);
        int r, gray;
        for (int k = 0; k < image.getWidth(); k++) {
            for (int l = 0; l < image.getHeight(); l++) {
                Color c = new Color(image.getRGB(k,l));
                r = c.getRed();
                gray = (int) ((r-i)*n);
                c = new Color(gray,gray,gray);
                im.setRGB(k,l,c.getRGB());
            }
        }
        return im;
    }

    public  static BufferedImage filter(BufferedImage image, Float[][] f,int numR, int numC, int i, int j) {
        BufferedImage im = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
        int r,g,b;
        Color c;
        float koeff = 0;
        for (int p = 0; p < numR; p++) {
            for (int y =0; y < numC; y++) {
                koeff += f[p][y];
            }
        }
        if (koeff == 0) koeff = 1;
        int wi = image.getWidth();
        int h = image.getHeight();
        for (int k = 0; k < wi; k++) {
            for (int l = 0; l < h; l++) {
                r = 0;
                g = 0;
                b = 0;

                for (int q = 0; q < numC; q++){
                    for (int w = 0; w < numR; w++){
                        int y = (h+l+w-i+1)%h;
                        int x = (wi+k+q-j+1)%wi;
                        //c = new Color(image.getRGB(2*(k+q-i+1)/wi,2*(l+w-j+1)/h));
                        c = new Color(image.getRGB(x,y));
                        r = r + (int)(c.getRed()*f[w][q]);
                        g = g + (int)(c.getGreen()*f[w][q]);
                        b = b + (int)(c.getBlue()*f[w][q]);
                    }
                }
                r = (int) (r/koeff);
                g = (int) (g/koeff);
                b = (int) (b/koeff);
                if (r < 0) r = 0;
                if (r > 255) r = 255;
                if (g < 0) g = 0;
                if (g > 255) g = 255;
                if (b < 0) b = 0;
                if (b > 255) b = 255;
                c = new Color(r,g,b);
                im.setRGB(k, l, c.getRGB());
            }
        }
        return im;
    }

    public static BufferedImage binary(BufferedImage image){
        BufferedImage im = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
        int threshold = 128;
        Color c;
        int r;
        for (int i = 0; i < image.getWidth(); i++) {
            for (int j = 0; j < image.getHeight(); j++) {
                c = new Color(image.getRGB(i,j));
                r = c.getRed();
                if (r > threshold) {
                    c = new Color(0xFFFFFF);
                } else {
                    c = new Color(0x000000);
                }
                im.setRGB(i,j,c.getRGB());
            }
        }
        return im;
    }

    public  static BufferedImage dilatation(BufferedImage image, Float[][] f,int numR, int numC, int i, int j) {
        BufferedImage im = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());

        int wi = image.getWidth();
        int h = image.getHeight();
        for (int k = 0; k < wi; k++) {
            for (int l = 0; l < h; l++) {
                im.setRGB(k,l,image.getRGB(k,l));
            }
        }
        for (int k = 0; k < wi; k++) {
            for (int l = 0; l < h; l++) {
                if (f[i - 1][j - 1] == 0) {
                    if ((new Color(0x000000)).getRGB() == (image.getRGB(k, l))) {
                        for (int q = 0; q < numC; q++) {
                            for (int w = 0; w < numR; w++) {
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
                        for (int q = 0; q < numC; q++) {
                            for (int w = 0; w < numR; w++) {
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

    public  static BufferedImage erosion(BufferedImage image, Float[][] f,int numR, int numC, int i, int j) {
        BufferedImage im = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
        boolean ok;

        int wi = image.getWidth();
        int h = image.getHeight();
        for (int k = 0; k < wi; k++) {
            for (int l = 0; l < h; l++) {
                im.setRGB(k,l,image.getRGB(k,l));
            }
        }
        for (int k = 0; k < wi; k++) {
            for (int l = 0; l < h; l++) {
                if (f[i-1][j-1] == 0) {
                    if ((new Color(0x000000)).getRGB() == (image.getRGB(k, l))) {
                        ok = true;
                        for (int q = 0; q < numC; q++) {
                            for (int w = 0; w < numR; w++) {
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
                if (f[i-1][j-1] == 1) {
                    if ((new Color(0xFFFFFF)).getRGB() == (image.getRGB(k, l))) {
                        ok = true;
                        for (int q = 0; q < numC; q++) {
                            for (int w = 0; w < numR; w++) {
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

    public  static BufferedImage locking(BufferedImage image, Float[][] f,int numR, int numC, int i, int j) {
        return erosion(dilatation(image, f, numR, numC, i, j), f, numR, numC, i, j);
    }

    public  static BufferedImage unlocking(BufferedImage image, Float[][] f,int numR, int numC, int i, int j) {
        return dilatation(erosion(image, f, numR, numC, i, j), f, numR, numC, i, j);
    }
}