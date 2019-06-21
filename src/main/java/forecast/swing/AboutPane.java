package forecast.swing;

import javax.swing.*;

public class AboutPane {

    public static void show() {
        String buffer = "<html><body>" +
                "<p align=center>Программа для построения графиков линий тренда<p>" +
                "<p align=center>Версия 1.0.2<p>" +
                "<p align=center>2016 г.<p>" +
                "</body></html>";
        JLabel aboutPanel = new JLabel(buffer);
        JOptionPane.showMessageDialog(null, aboutPanel, "О программе", JOptionPane.INFORMATION_MESSAGE);
    }

}
