package GUI;

import ImageProcessing.ImageProcessing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

/**
 * Created by Ксения on 10.10.2017.
 */
public class FilterFilling extends JFrame{

    private JTable table;
    private JButton bOK = new JButton("OK");
    private JButton back = new JButton("Назад");

    FilterSettings parent;
    Integer rows, cols, x, y;
    Float[][] matrix;

    enum Filtering {FILTER, DILATATION, EROSION, LOCKING, UNLOCKING};

    FilterFilling(final FilterSettings parent, final int rows, final int cols, final int x, final int y, final BufferedImage im, final Filtering type) {


        this.parent = parent;
        this.rows = rows;
        this.cols = cols;
        this.x = x;
        this.y = y;

        JPanel fill = new JPanel();
        JPanel p6 = new JPanel();
        JPanel p7 = new JPanel();

        fill.setLayout(new BorderLayout());
        p6.add(back);
        p6.add(bOK);

        table = new JTable(rows, cols);
        p7.setLayout(new BorderLayout());
        p7.add(table, BorderLayout.CENTER);

        fill.add(p7, BorderLayout.CENTER);
        fill.add(p6, BorderLayout.SOUTH);

        table.setRowHeight(40);

        add(fill);

        back.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                parent.setVisible(true);
                FilterFilling.this.dispose();
            }
        });

        bOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                matrix = new Float[rows][cols];
                try {
                    try {
                        for (int i = 0; i < rows; i++) {
                            for (int j = 0; j < cols; j++) {
                                matrix[i][j] = new Float((String) (table.getModel().getValueAt(i, j)));
                            }
                        }
                        FilterFilling.this.parent.dispose();
                        FilterFilling.this.dispose();
                        switch (type) {
                            case FILTER: new MainForm(ImageProcessing.filter(im, matrix, rows, cols, x, y), parent.parent.imBW, parent.parent.bin, 1);
                                            break;
                            case DILATATION:new MainForm(ImageProcessing.dilatation(im, matrix, rows, cols, x, y), parent.parent.imBW, parent.parent.bin, 1);
                                            break;
                            case EROSION: new MainForm(ImageProcessing.erosion(im, matrix, rows, cols, x, y), parent.parent.imBW, parent.parent.bin, 1);
                                            break;
                            case LOCKING: new MainForm(ImageProcessing.locking(im, matrix, rows, cols, x, y), parent.parent.imBW, parent.parent.bin, 1);
                                            break;
                            case UNLOCKING: new MainForm(ImageProcessing.unlocking(im, matrix, rows, cols, x, y), parent.parent.imBW, parent.parent.bin, 1);
                                            break;

                        }
                    } catch (NumberFormatException exc) {
                        JOptionPane.showMessageDialog(FilterFilling.this, "Введите числа!");
                    }

                } catch (Exception exc) {
                    exc.printStackTrace();
                }
            }
        });

        setSize(600, 400);
        setVisible(true);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

    }
}
