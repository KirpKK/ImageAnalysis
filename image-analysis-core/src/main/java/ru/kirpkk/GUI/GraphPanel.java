package ru.kirpkk.GUI;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Ксения on 17.10.2017.
 */
public class GraphPanel extends JPanel {
    private JComponent parent;
    int height, width;
    float[] stat;

    public GraphPanel(float[] stat) {
        super();
        width = 550;
        height = 400;

        setSize(width, height);
        this.stat = stat;
    }

    @Override
    public void paintComponent(Graphics g) {
        setSize(width, height);

        float max = 0;
        for (float t : stat) {
            max = (t > max) ? t : max;
        }

        float w = (float) ((width - 50)) / 256;
        float h = (float) ((height - 30)) / max;

        g.drawRect(30, height - 15, width - 40, 1);
        g.drawRect(30, 15, 1, height - 30);
        for (int t = 0;t < 256;t++) {
            g.drawRect((int) (30 + w * t), (int) (15 + (height - 30 - stat[t] * h)), (int) w, (int) (h * stat[t]));
        }
        String s = "0";
        g.drawChars(s.toCharArray(), 0, 1, 5, height - 1);
        s = "128";
        g.drawChars(s.toCharArray(), 0, 3, width / 2 - 10, height - 1);
        s = "255";
        g.drawChars(s.toCharArray(), 0, 3, width - 30, height - 1);
        g.drawChars((new Float(max / 2)).toString().toCharArray(), 0, 5, 0, height / 2 - 10);
        g.drawChars((new Float(max)).toString().toCharArray(), 0, 5, 0, 15);
    }

    void newIm(float[] stat) {
        this.stat = stat;
    }

}
