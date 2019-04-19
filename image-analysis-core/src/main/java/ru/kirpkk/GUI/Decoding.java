package ru.kirpkk.GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * Created by Ксения on 30.11.2017.
 */
/*- поле ввода словаря симоволов
- поле ввода фразы, которую надо закодировать
-поле результата кодировки
- поле ввода последовательности, которую надо дескодировать
-поле результата дкодировки
- дополнительные поля
*/
public class Decoding extends JFrame {

    private JPanel panel;
    private JLabel dict;
    private JLabel endSymbol;
    private JLabel dataLabel;
    private JTextField dictionaryField;
    private JTextField endField;
    private JTextField dataField;
    private JButton bOK;
    private JTextField decode;

    String strAlphabet;
    String data;
    String end;
    ArrayList alphabet = new ArrayList();
    Double num;

    Decoding() {
        JPanel pAlphabet = new JPanel(new GridLayout(1, 2));
        dict = new JLabel("Алфавит: ");
        dictionaryField = new JTextField(5);
        pAlphabet.add(dict);
        pAlphabet.add(dictionaryField);

        JPanel pData = new JPanel(new GridLayout(1, 2));
        dataLabel = new JLabel("Число: ");
        dataField = new JTextField();
        pData.add(dataLabel);
        pData.add(dataField);

        JPanel pEnd = new JPanel(new GridLayout(1, 2));
        endSymbol = new JLabel("Символ конца: ");
        endField = new JTextField(5);
        pEnd.add(endSymbol);
        pEnd.add(endField);

        bOK = new JButton("Раскодировать");
        decode = new JTextField();

        panel = new JPanel();
        panel.setLayout(new GridLayout(5,1,10,10));
        panel.add(pAlphabet);
        panel.add(pEnd);
        panel.add(pData);
        panel.add(bOK);
        panel.add(decode);
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
                try {
                    num = new Double(data);
                } catch (NumberFormatException ex) {
                    new JOptionPane("Введите символ конца");
                }
                int k = 0;
                for (String symbol:strAlphabet.split(",")) {
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
                double a = 0, b = 1;  //границы интервала
                int size = alphabet.size();
                double d = (double) b/size; //ширина интервала
                Character symbol;
                Character endSymbol = (Character) alphabet.get(k);
                StringBuilder sb = new StringBuilder();
                do {
                    i = 0;
                    boolean ok = false;
                    while (!ok) {
                        try {
                        if ((num > a +d*i) & (num < a + d*(i+1))) ok = true;
                        else i++;
                        if (i > k) throw new Exception("Something's wrong");
                    } catch (Exception exc) {
                            new JOptionPane(exc.getMessage());
                        }
                    }
                    symbol = (Character) alphabet.get(i);
                    a = a + d*i;
                    b = a + d;
                    d = d/size;
                    sb.append(symbol);
                } while (!endSymbol.equals(symbol));

                sb.deleteCharAt(sb.length()-1);

                decode.setText(sb.toString());
            }
        });

        setSize(400, 200);
        setVisible(true);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

    }
}