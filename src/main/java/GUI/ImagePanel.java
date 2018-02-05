package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by Ксения on 10.10.2017.
 */
public class ImagePanel extends JPanel{
    private BufferedImage image;
    private int width, height;

    public ImagePanel(BufferedImage img) {
        super();
        this.image = img;
        width = image.getWidth();
        height = image.getHeight();
        this.setSize(width, height);
    }

    @Override
    public void paintComponent(Graphics g) {
        width = image.getWidth();
        height = image.getHeight();

        setSize(width, height);
        if (image != null) {
            g.drawImage(image, 0, 0, width, height, null);
        }

    }
}
