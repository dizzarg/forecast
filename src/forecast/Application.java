package forecast;

import forecast.swing.MainFrame;

import javax.swing.*;

public class Application {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                createForecastApplication();
            }
        });
    }

    private static void createForecastApplication() {
        new MainFrame();
    }

}
