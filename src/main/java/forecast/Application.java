package forecast;

import forecast.swing.MainFrame;

import javax.swing.*;

public class Application {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Application::createForecastApplication);
    }

    private static void createForecastApplication() {
        new MainFrame();
    }

}
