package forecast.swing;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static java.lang.Math.PI;

/**
 * Created with IntelliJ IDEA.
 * User: damir
 * Date: 24.12.13
 * Time: 23:59
 * To change this template use File | Settings | File Templates.
 */
public class TimerThread extends Thread {

    protected boolean isRunning;

    protected JLabel dateLabel;
    protected JLabel timeLabel;
//    protected int delta = -1;
    private final static int persent=100;
    private double value = 3*PI/2;

    protected SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, d MMM yyyy");
    protected SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm:ss");

    public TimerThread(JLabel dateLabel, JLabel timeLabel) {
        this.dateLabel = dateLabel;
        this.timeLabel = timeLabel;
        this.isRunning = true;
    }

    @Override
    public void run() {
        while (isRunning) {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    Calendar currentCalendar = Calendar.getInstance();
                    Date currentTime = currentCalendar.getTime();
                    dateLabel.setText(dateFormat.format(currentTime));
                    timeLabel.setText(timeFormat.format(currentTime));
                }
            });

            try {
                Thread.sleep(1000L);
            } catch (InterruptedException e) {
            }
        }
    }

    public void setRunning(boolean isRunning) {
        this.isRunning = isRunning;
    }

}
