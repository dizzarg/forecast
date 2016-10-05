package forecast.swing;

import javax.swing.*;
import java.awt.*;

public class ExceptionPane {

    public void show(String title, Exception e) {
        StringBuilder sb = new StringBuilder();
        sb.append(e.getMessage());
        sb.append("\n");
        for (StackTraceElement ste : e.getStackTrace()) {
            sb.append(ste.toString());
            sb.append("\n");
        }
        JTextArea jta = new JTextArea(sb.toString());
        JScrollPane jsp = new JScrollPane(jta) {
            @Override
            public Dimension getPreferredSize() {
                return new Dimension(480, 320);
            }
        };
        JOptionPane.showMessageDialog(null, jsp, title, JOptionPane.ERROR_MESSAGE);
    }

}
