package forecast.swing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

public class HelpPane {

    public void show() {
        JPanel helpPanel = new JPanel();
        helpPanel.setLayout(new BorderLayout());
        String trentFile = "/help/trend.html";
        String buffer = readHelpFile(trentFile);
        URL imgUrl = getClass().getClassLoader().getResource("image/trend3.jpg");
        String htmlString = "<center><img src=\"" +imgUrl.toString()+"\" /></center>";
        buffer = htmlString + buffer;
        final JTextPane textPane = new JTextPane();
        textPane.setEditable(false);
        textPane.setContentType("text/html");
        textPane.setText(buffer);
        textPane.setCaretPosition(0);
        JScrollPane jsp = new JScrollPane(textPane) {
            @Override
            public Dimension getPreferredSize() {
                return new Dimension(640, 480);
            }
        };
        JPanel btnPane = new JPanel();
        JButton btn0 = new JButton("Главная");
        btn0.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String str = readHelpFile("/help/trend.html");
                URL imgUrl = getClass().getClassLoader().getResource("image/trend3.jpg");
                String htmlString = "<img src=\"" +imgUrl.toString()+"\" />";
                str = htmlString + str;
                textPane.setText(str);
                textPane.setCaretPosition(0);
                textPane.repaint();
                textPane.revalidate();
            }
        });
        JButton btn1 = new JButton("Виды тренда");
        btn1.addActionListener(e -> {
            String str = readHelpFile("/help/trend_kind.html");
            textPane.setText(str);
            textPane.setCaretPosition(0);
            textPane.repaint();
            textPane.revalidate();
        });
        JButton btn2 = new JButton("Методы оценки тренда");
        btn2.addActionListener(e -> {
            String str = readHelpFile("/help/methods.html");
            textPane.setText(str);
            textPane.setCaretPosition(0);
            textPane.repaint();
            textPane.revalidate();
        });
        JButton btn3 = new JButton("Линии тренда");
        btn3.addActionListener(e -> {
            String str = readHelpFile("/help/trend_line.html");
            textPane.setText(str);
            textPane.setCaretPosition(0);
            textPane.repaint();
            textPane.revalidate();
        });
        JButton btn4 = new JButton("Типы тренда");
        btn4.addActionListener(e -> {
            String str = readHelpFile("/help/trend_type.html");
            textPane.setText(str);
            textPane.setCaretPosition(0);
            textPane.repaint();
            textPane.revalidate();
        });
        btnPane.add(btn0);
        btnPane.add(btn1);
        btnPane.add(btn2);
        btnPane.add(btn3);
        btnPane.add(btn4);
        helpPanel.add(btnPane, BorderLayout.NORTH);
        helpPanel.add(jsp, BorderLayout.EAST);
        JOptionPane.showMessageDialog(null, helpPanel, "Справка", JOptionPane.INFORMATION_MESSAGE);

    }

    private String readHelpFile(String trentFile) {
        String file = "";
        try {
            InputStream is = getClass().getResourceAsStream(trentFile);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));
            StringBuffer buffer = new StringBuffer();
            while (bufferedReader.ready()) {
                String line = bufferedReader.readLine();
                buffer.append(line);
                buffer.append("\n");
            }
            file = buffer.toString();
        } catch (IOException e) {
            new ExceptionPane().show("Ошибка чтение файла", e);
        } catch (Exception e) {
            new ExceptionPane().show("Ошибка", e);
        }
        return file;
    }
}
