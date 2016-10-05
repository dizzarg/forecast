package forecast.swing;

import javax.swing.*;
import java.awt.*;

/**
 * Created with IntelliJ IDEA.
 * User: damir
 * Date: 24.12.13
 * Time: 23:49
 * To change this template use File | Settings | File Templates.
 */
public class SeparatorPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    protected Color leftColor;
    protected Color rightColor;

    public SeparatorPanel(Color leftColor, Color rightColor) {
        this.leftColor = leftColor;
        this.rightColor = rightColor;
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        g.setColor(leftColor);
        g.drawLine(0, 0, 0, getHeight());
        g.setColor(rightColor);
        g.drawLine(1, 0, 1, getHeight());
    }

}
