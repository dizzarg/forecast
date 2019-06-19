package forecast.swing;

import forecast.utils.TimeSeriesReader;
import org.jfree.data.time.TimeSeries;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class MainFrame extends JFrame{

    private static final String APP_NAME = "Прогнозирование";
    private static final Dimension MAIN_SIZE = new Dimension(960, 600);
    private final JTabbedPane tabbedPane = new JTabbedPane();
    private TimeSeriesReader reader = new TimeSeriesReader();
    private HelpPane helpPane = new HelpPane();
    private AboutPane aboutPane = new AboutPane();
    private final FileReaderSettingPane fileReaderSettingPane = new FileReaderSettingPane();

    private SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, d MMM yyyy");
    private SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm:ss");


    public MainFrame(){
        super(APP_NAME);
        JPanel contentPane = new JPanel();
        contentPane.setLayout(new BorderLayout());
        contentPane.setPreferredSize(MAIN_SIZE);
        getContentPane().add(contentPane);
        contentPane.add(tabbedPane);

        JMenu fileMenu = new JMenu("Файл");
        KeyStroke newKey = KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_MASK);
        fileMenu.add(createMenuItem("Новый", newKey, e -> createTab()));
        KeyStroke settingKey = KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK);
        fileMenu.add(createMenuItem("Настройки чтения", settingKey, e -> fileReaderSettingPane.show()));
        fileMenu.addSeparator();
        KeyStroke closeTabKey = KeyStroke.getKeyStroke(KeyEvent.VK_X, InputEvent.CTRL_MASK);
        fileMenu.add(createMenuItem("Закрыть", closeTabKey, e -> closeSelectedTab()));
        fileMenu.add(createMenuItem("Закрыть все", e -> tabbedPane.removeAll()));
        fileMenu.addSeparator();
        KeyStroke exitKey = KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.ALT_MASK);
        fileMenu.add(createMenuItem("Выход", exitKey, e -> System.exit(0)));

        JMenu aboutMenu = new JMenu("Помощь");
        aboutMenu.add(createMenuItem("Справка", e -> helpPane.show()));

        aboutMenu.add(createMenuItem("О программе", e -> aboutPane.show()));

        JMenuBar menuBar = new JMenuBar();
        menuBar.add(fileMenu);
        menuBar.add(Box.createHorizontalGlue());
        menuBar.add(aboutMenu);
        setJMenuBar(menuBar);

        JStatusBar statusBar = new JStatusBar();
        JLabel leftLabel = new JLabel("Запуск приложения.");
        statusBar.setLeftComponent(leftLabel);

        final JLabel dateLabel = new JLabel();
        dateLabel.setHorizontalAlignment(JLabel.CENTER);
        statusBar.addRightComponent(dateLabel);

        final JLabel timeLabel = new JLabel();
        timeLabel.setHorizontalAlignment(JLabel.CENTER);
        statusBar.addRightComponent(timeLabel);

        contentPane.add(statusBar, BorderLayout.SOUTH);

        Executors.newSingleThreadScheduledExecutor()
                .scheduleAtFixedRate(() -> {
                    Calendar currentCalendar = Calendar.getInstance();
                    Date currentTime = currentCalendar.getTime();
                    dateLabel.setText(dateFormat.format(currentTime));
                    timeLabel.setText(timeFormat.format(currentTime));
                }, 0, 1, TimeUnit.SECONDS);

        setSize(MAIN_SIZE);
        setVisible(true);
        setDefaultLookAndFeelDecorated(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        pack();
    }

    private void closeSelectedTab() {
        int index = tabbedPane.getSelectedIndex();
        if(index>=0){
            tabbedPane.remove(index);
        }
    }

    private void createTab() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("Текстовый файл", "txt"));
        fileChooser.setDialogTitle("Открыть файл");
        fileChooser.setCurrentDirectory(new File( "." ));
        fileChooser.showOpenDialog(this);
        File file = fileChooser.getSelectedFile();
        if(file!=null){
            try {
                TimeSeries fileSeries = new TimeSeries("Данные с файла");
                reader.setRegularTime(fileReaderSettingPane.getTimeFormat());
                reader.setSeparator(fileReaderSettingPane.getSeparator());
                reader.loadTimeSeriesFromFile(fileSeries, file);
                TabPanel panel = new TabPanel(file.getName() ,fileSeries);
                tabbedPane.addTab(panel.getTitle(), panel);
            } catch (Exception e) {
                ExceptionPane.show("Ошибка при отрисовке графика с файла", e);
            }

        }
    }

    private JMenuItem createMenuItem(String text, ActionListener actionListener) {
        JMenuItem menuItem = new JMenuItem(text);
        menuItem.addActionListener(actionListener);
        return menuItem;
    }

    private JMenuItem createMenuItem(String text,KeyStroke keyStroke, ActionListener actionListener) {
        JMenuItem menuItem = new JMenuItem(text);
        menuItem.addActionListener(actionListener);
        menuItem.setAccelerator(keyStroke);
        return menuItem;
    }
}
