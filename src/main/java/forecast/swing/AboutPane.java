package forecast.swing;

import javax.swing.*;

public class AboutPane {

    public void show() {
        StringBuilder buffer = new StringBuilder();
        buffer.append("<html><body>");
        buffer.append("<p align=center>Программа для построения графиков линий тренда<p>");
        buffer.append("<p align=center>Версия 1.0.2<p>");
        buffer.append("<p align=center>2016 г.<p>");
        buffer.append("</body></html>");
        JLabel aboutPanel = new JLabel(buffer.toString());
        JOptionPane.showMessageDialog(null, aboutPanel, "О программе", JOptionPane.INFORMATION_MESSAGE);
    }

}
