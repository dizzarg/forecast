package forecast.swing;

import javax.swing.*;
import java.awt.*;

/**
 * Created with IntelliJ IDEA.
 * User: damir
 * Date: 24.12.13
 * Time: 23:45
 * To change this template use File | Settings | File Templates.
 */
public class JStatusBar extends JPanel {

    private static final long serialVersionUID = 1L;

    protected JPanel leftPanel;
    protected JPanel rightPanel;

    public JStatusBar() {
        createPartControl();
    }

    protected void createPartControl() {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(getWidth(), 23));

        leftPanel = new JPanel(new FlowLayout(
                FlowLayout.LEADING, 5, 3));
        leftPanel.setOpaque(false);
        add(leftPanel, BorderLayout.WEST);

        rightPanel = new JPanel(new FlowLayout(
                FlowLayout.TRAILING, 5, 3));
        rightPanel.setOpaque(false);
        add(rightPanel, BorderLayout.EAST);
    }

    public void setLeftComponent(JComponent component) {
        leftPanel.add(component);
    }

    public void addRightComponent(JComponent component) {
        JPanel panel = new JPanel(new FlowLayout(
                FlowLayout.LEADING, 5, 0));
        panel.add(new SeparatorPanel(Color.GRAY, Color.WHITE));
        panel.add(component);
        rightPanel.add(panel);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int y = 0;
        g.setColor(new Color(156, 154, 140));
        g.drawLine(0, y, getWidth(), y);
        y++;

        g.setColor(new Color(196, 194, 183));
        g.drawLine(0, y, getWidth(), y);
        y++;

        g.setColor(new Color(218, 215, 201));
        g.drawLine(0, y, getWidth(), y);
        y++;

        g.setColor(new Color(233, 231, 217));
        g.drawLine(0, y, getWidth(), y);

        y = getHeight() - 3;

        g.setColor(new Color(233, 232, 218));
        g.drawLine(0, y, getWidth(), y);
        y++;

        g.setColor(new Color(233, 231, 216));
        g.drawLine(0, y, getWidth(), y);
        y++;

        g.setColor(new Color(221, 221, 220));
        g.drawLine(0, y, getWidth(), y);
    }

}
