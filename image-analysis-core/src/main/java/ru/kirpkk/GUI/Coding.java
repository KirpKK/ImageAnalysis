package ru.kirpkk.GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * Created by Ксения on 22.11.2017.
 */

/*- поле ввода словаря симоволов
- поле ввода фразы, которую надо закодировать
-поле результата кодировки
- поле ввода последовательности, которую надо дескодировать
-поле результата дкодировки
- дополнительные поля
*/
public class Coding extends JFrame {

    private JPanel panel;
    private JLabel dict;
    private JLabel endSymbol;
    private JLabel dataLabel;
    private JTextField dictionaryField;
    private JTextField endField;
    private JTextField dataField;
    private JButton bOK;
    private JTextField code;
    private JTextField inter1;
    private JTextField inter2;

    String strAlphabet;
    String data;
    String end;
    ArrayList alphabet = new ArrayList();

    Coding() {
        JPanel pAlphabet = new JPanel(new GridLayout(1, 2));
        dict = new JLabel("Алфавит: ");
        dictionaryField = new JTextField(5);
        pAlphabet.add(dict);
        pAlphabet.add(dictionaryField);

        JPanel pData = new JPanel(new GridLayout(1, 2));
        dataLabel = new JLabel("Текст: ");
        dataField = new JTextField();
        pData.add(dataLabel);
        pData.add(dataField);

        JPanel pEnd = new JPanel(new GridLayout(1, 2));
        endSymbol = new JLabel("Символ конца: ");
        endField = new JTextField(5);
        pEnd.add(endSymbol);
        pEnd.add(endField);

        bOK = new JButton("Закодировать");
        code = new JTextField();
        inter1  =new JTextField();
        inter2 = new JTextField();

        panel = new JPanel();
        panel.setLayout(new GridLayout(5, 1, 10, 10));
        panel.add(pAlphabet);
        panel.add(pEnd);
        panel.add(pData);
        panel.add(bOK);
        panel.add(code);
        panel.add(inter1);
        panel.add(inter2);
        panel.setVisible(true);
        add(panel);

        bOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                alphabet.clear();
                strAlphabet = dictionaryField.getText();
                end = endField.getText();
                data = dataField.getText();
                if (strAlphabet.length() == 0) new JOptionPane("Введите алфавит");
                if (end.length() == 0) new JOptionPane("Введите символ конца");
                if (data.length() == 0) new JOptionPane("Введите текст");
                int k = 0;
                for (String symbol : strAlphabet.split(",")) {
                    if (symbol.toCharArray().length == 1) {
                        alphabet.add(k, symbol.toCharArray()[0]);
                        k++;
                    } else {
                        new JOptionPane("Введите символы!");
                    }
                }
                if (end.toCharArray().length == 1) {
                    alphabet.add(k, end.toCharArray()[0]);
                } else {
                    new JOptionPane("Введите символы!");
                }

                int i = 0; //номер символа в алфавите
                Double a = new Double(0);
                Double b = new Double(1);  //границы интервала
                int size = alphabet.size();
                double d = (double) b / size; //ширина интервала
                data = data + end;
                try {
                    for (char symbol : data.toCharArray()) {
                            i = alphabet.indexOf(symbol);
                            if (i > -1) {
                                a = a + i * d;
                                b = a + d;
                                d = d / size;
                            } else {
                                throw new Exception();
                            }
                    }
                    Double num = (a + b) / 2;
                    code.setText(num.toString());
                    inter1.setText(a.toString());
                    inter2.setText(b.toString());

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(Coding.this, "Сообщение содержит символ не из алфавита!",
                            "Error", 0);
                }
            }
        });

        setSize(400, 200);
        setVisible(true);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

    }
}
