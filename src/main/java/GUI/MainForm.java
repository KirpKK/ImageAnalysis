package GUI;

import ImageProcessing.ImageProcessing;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by Ксения on 28.09.2017.
 */
public class MainForm extends JFrame{
    private JMenuBar menu = new JMenuBar();

    static BufferedImage im;

    boolean imBW = false;
    boolean bin = false;
    final int MFWidth = 1200;
    final int MFHeight = 600;

    public MainForm(BufferedImage image,boolean bw, boolean binary, int type) {
        super();
        setTitle("Анализ изображений");
        imBW = bw;
        bin = binary;
        im = image;
        if (type == 1) {
            if (im != null) {
                ImagePanel imP = new ImagePanel(im);
                add(imP);
                if (im.getWidth() < MFWidth & im.getHeight() < MFHeight) {
                    imP.setSize(im.getWidth(),im.getHeight());
                    setSize(MFWidth, MFHeight);
                }
                if (im.getWidth() < MFWidth & im.getHeight() > MFHeight) {
                    setSize(MFWidth,im.getHeight()+65);
                    imP.setSize(im.getWidth(), getHeight()-65);
                }
                if (im.getWidth() > MFWidth & im.getHeight() < MFHeight) {
                    setSize(im.getWidth(),MFHeight);
                    imP.setSize(getWidth(), im.getHeight());
                }
                if (im.getWidth() > MFWidth & im.getHeight() > MFHeight) {
                    setSize(im.getWidth(),im.getHeight()+65);
                    imP.setSize(getWidth(), getHeight()-65);
                }

            } else {
                setSize(MFWidth,MFHeight);
            }
        };
        if (type == 2) {
            if (bw) {
                add(new GraphPanel(ImageProcessing.graphBW(im)));
            } else {
                add(new GraphPanel(ImageProcessing.graphBW(ImageProcessing.forHuman(im))));
            }

            setSize(600,550);
        }


        JMenuItem load = new JMenuItem("Загрузить");
        load.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    JFileChooser fileChooser = new JFileChooser();
                    fileChooser.setVisible(true);
                    fileChooser.setCurrentDirectory(new File("C:\\Users\\Ксения\\Desktop\\Анализ изображений"));
                    fileChooser.setDialogType(0);

                    fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                    int val = fileChooser.showOpenDialog((Component) e.getSource());
                    if (val == JFileChooser.APPROVE_OPTION) {
                        File file = fileChooser.getSelectedFile();
                        if (file.getName().toLowerCase().endsWith(".jpg")||file.getName().toLowerCase().endsWith(".jpeg")
                                ||file.getName().toLowerCase().endsWith(".png")) {
                            BufferedImage image = ImageIO.read(file);
                            new MainForm(image, false, false, 1);

                        } else JOptionPane.showMessageDialog(MainForm.this,"Incorrect type",
                                "Error",2);
                    }

                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });
        JMenuItem save = new JMenuItem("Сохранить");
        save.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                    JFileChooser fileChooser = new JFileChooser();
                    fileChooser.setVisible(true);
                    fileChooser.setCurrentDirectory(new File("C:\\Users\\Ксения\\Desktop\\Анализ изображений"));
                    fileChooser.setDialogType(0);
                    fileChooser.setFileSelectionMode(JFileChooser.SAVE_DIALOG);
                    int val = fileChooser.showSaveDialog((Component) e.getSource());
                    if (val == JFileChooser.APPROVE_OPTION) {
                        File file = fileChooser.getSelectedFile();
                        try {
                            ImageIO.write(MainForm.im, "jpg", file);
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                    }
            }
        });

        JMenu toBW = new JMenu("Перевести в ч/б");

        JMenuItem fast = new JMenuItem("Быстрый");
        fast.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (im != null) {
                    new MainForm(ImageProcessing.fast(im),true, false, 1);
                } else JOptionPane.showMessageDialog(MainForm.this, "Выберите изображение!",
                        "Error", 0);
            }
        });

        JMenuItem forEye = new JMenuItem("С коррекцией под человеческий глаз");
        forEye.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (im != null) {
                    new MainForm(ImageProcessing.forHuman(im), true, false, 1);
                } else JOptionPane.showMessageDialog(MainForm.this, "Выберите изображение!",
                        "Error", 0);;
            }
        });

        JMenuItem desat = new JMenuItem("Десатурация");
        desat.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (im != null) {
                    new MainForm(ImageProcessing.desat(im), true, false, 1);
                }else JOptionPane.showMessageDialog(MainForm.this, "Выберите изображение!",
                        "Error", 0);;
            }
        });

        JMenuItem gradMin = new JMenuItem("Градация по минимуму");
        gradMin.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (im != null) {
                    new MainForm(ImageProcessing.gradMin(im),true, false,1);
                }else JOptionPane.showMessageDialog(MainForm.this, "Выберите изображение!",
                        "Error", 0);;
            }
        });

        final JMenuItem gradMax = new JMenuItem("Градация по максимуму");
        gradMax.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (im != null) {
                    new MainForm(ImageProcessing.gradMax(im),true, false, 1);
                }else JOptionPane.showMessageDialog(MainForm.this, "Выберите изображение!", "Error", 0);
            }
        });

        toBW.add(fast);
        toBW.add(forEye);
        toBW.add(desat);
        toBW.add(gradMin);
        toBW.add(gradMax);

        JMenuItem gist = new JMenuItem("Построить гистограмму");
        gist.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new MainForm(im, imBW, false, 2);
            }
        });

        JMenuItem equalize = new JMenuItem("Эквализация");
        equalize.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (imBW) {
                    new MainForm(ImageProcessing.equalization(im), imBW,false,  1);
                }else JOptionPane.showMessageDialog(MainForm.this, "Нет ч/б изображения!",
                        "Error", 2);
            }
        });

        JMenuItem filter = new JMenuItem("Фильтрация");
        filter.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (im != null) {
                    new FilterSettings(MainForm.this, im, FilterFilling.Filtering.FILTER);
                }else JOptionPane.showMessageDialog(MainForm.this, "Выберите изображение!",
                        "Error", 0);
            }
        });

        JMenu coding = new JMenu("Кодирование");
        JMenuItem toCode = new JMenuItem("Закодировать");
        coding.add(toCode);
        JMenuItem toDecode = new JMenuItem("Раскодировать");
        coding.add(toDecode);

        toCode.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                    new Coding();
            }
        });

        toDecode.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new Decoding();
            }
        });

        JMenuItem toBinary = new JMenuItem("Бинаризовать");
        toBinary.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (im != null) {
                    new MainForm(ImageProcessing.binary(im), false, true,  1);
                }else JOptionPane.showMessageDialog(MainForm.this, "Выберите изображение!",
                        "Error", 0);;
            }
        });


        JMenu morph = new JMenu("Морфология");

        JMenuItem dilatation = new JMenuItem("Дилатация");
        dilatation.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (im != null) {
                    if (bin) {
                        new FilterSettings(MainForm.this, im, FilterFilling.Filtering.DILATATION);
                    } else JOptionPane.showMessageDialog(MainForm.this, "Не бинаризовано!",
                            "Error", 0);
                } else JOptionPane.showMessageDialog(MainForm.this, "Выберите изображение!",
                        "Error", 0);
            }
        });

        JMenuItem erosion = new JMenuItem("Эрозия");
        erosion.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (im != null) {
                    if (bin) {
                        new FilterSettings(MainForm.this, im, FilterFilling.Filtering.EROSION);
                    } else JOptionPane.showMessageDialog(MainForm.this, "Не бинаризовано!",
                            "Error", 0);
                } else JOptionPane.showMessageDialog(MainForm.this, "Выберите изображение!",
                        "Error", 0);
            }
        });

        JMenuItem locking = new JMenuItem("Замыкание");
        locking.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (im != null) {
                    if (bin) {
                        new FilterSettings(MainForm.this, im,FilterFilling.Filtering.LOCKING);
                    } else JOptionPane.showMessageDialog(MainForm.this, "Не бинаризовано!",
                            "Error", 0);
                } else JOptionPane.showMessageDialog(MainForm.this, "Выберите изображение!",
                        "Error", 0);
            }
        });

        JMenuItem unlocking = new JMenuItem("Размыкание");
        unlocking.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (im != null) {
                    if (bin) {
                        new FilterSettings(MainForm.this, im, FilterFilling.Filtering.UNLOCKING);
                    } else JOptionPane.showMessageDialog(MainForm.this, "Не бинаризовано!",
                            "Error", 0);
                } else JOptionPane.showMessageDialog(MainForm.this, "Выберите изображение!",
                        "Error", 0);
            }
        });

        morph.add(dilatation);
        morph.add(erosion);
        morph.add(locking);
        morph.add(unlocking);

        menu.add(load);
        menu.add(save);
        menu.add(toBW);
        menu.add(gist);
        menu.add(equalize);
        menu.add(filter);
        menu.add(coding);
        menu.add(toBinary);
        menu.add(morph);
        menu.setBorderPainted(true);

        setJMenuBar(menu);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setVisible(true);
        setLocationRelativeTo(null);
    }

    public static BufferedImage getIm() {
        return im;
    }
}
