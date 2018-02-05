package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

/**
 * Created by Ксения on 29.09.2017.
 */
public class FilterSettings extends JFrame {

    private JLabel numRow = new JLabel("Число строк");
    private JLabel numColumn = new JLabel("Число столбцов");
    private JTextField row = new JTextField(3);
    private JTextField col = new JTextField(3);

    private JLabel cenRow = new JLabel("Строка ядра");
    private JLabel cenColumn = new JLabel("Столбец ядра");
    private JTextField xCen = new JTextField(3);
    private JTextField yCen = new JTextField(3);

    private JButton next = new JButton("Далее");

    private JTable table = new JTable(3, 3);
    private JButton bOK = new JButton("OK");
    private JButton back = new JButton("Назад");

    MainForm parent;
    Integer rows, cols, x, y;

    FilterSettings(MainForm parent, final BufferedImage im, final FilterFilling.Filtering type) {

        this.parent = parent;

        JPanel p1 = new JPanel();
        JPanel p2 = new JPanel();
        JPanel p3 = new JPanel();
        JPanel p4 = new JPanel();
        JPanel p5 = new JPanel();

        JPanel set = new JPanel();

        p1.add(numRow);
        p1.add(row);
        p2.add(numColumn);
        p2.add(col);
        p3.add(cenRow);
        p3.add(yCen);
        p4.add(cenColumn);
        p4.add(xCen);

        set.setLayout(new BorderLayout());
        p5.setLayout(new GridLayout(0, 1, 10, 10));
        p5.add(p1);
        p5.add(p2);
        p5.add(p3);
        p5.add(p4);
        set.add(p5, BorderLayout.CENTER);
        set.add(next, BorderLayout.SOUTH);


        final JPanel fill = new JPanel();
        JPanel p6 = new JPanel();

        fill.setLayout(new GridLayout(0, 1, 10, 10));
        p6.add(back);
        p6.add(bOK);
        fill.add(table);
        fill.add(p6);

        add(set);

        next.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    rows = new Integer(row.getText());
                    cols = new Integer(col.getText());
                    x = new Integer(xCen.getText());
                    y = new Integer(yCen.getText());
                    boolean b = true;
                    if (rows < 0 || cols < 0) {
                        b = false;
                        JOptionPane.showMessageDialog(FilterSettings.this,"Некорректные размеры матрицы");
                    }
                    if (x < 1 || y < 1 || x > cols || y > rows) {
                        b = false;
                        JOptionPane.showMessageDialog(FilterSettings.this,"Некорректные размеры ядра");
                    }
                    if (b) {
                        new FilterFilling(FilterSettings.this, rows, cols, x, y, im, type);
                        FilterSettings.this.setVisible(false);
                    }
                } catch (NumberFormatException exc) {
                    JOptionPane.showMessageDialog(FilterSettings.this, "Введите числа!");
                }
            }
        });

        table.setSize(800, 100);

        setSize(600, 400);
        setVisible(true);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

    }
}
