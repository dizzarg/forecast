package forecast.swing;

import javax.swing.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TimerThread extends Thread {

    private boolean isRunning;

    private JLabel dateLabel;
    private JLabel timeLabel;

    private SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, d MMM yyyy");
    private SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm:ss");

    public TimerThread(JLabel dateLabel, JLabel timeLabel) {
        this.dateLabel = dateLabel;
        this.timeLabel = timeLabel;
        this.isRunning = true;
    }

    @Override
    public void run() {
        while (isRunning) {
            SwingUtilities.invokeLater(() -> {
                Calendar currentCalendar = Calendar.getInstance();
                Date currentTime = currentCalendar.getTime();
                dateLabel.setText(dateFormat.format(currentTime));
                timeLabel.setText(timeFormat.format(currentTime));
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
