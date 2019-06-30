package forecast.swing.tab;

import forecast.trend.*;

import javax.swing.*;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionListener;

public class SettingPane extends JPanel {

    private JSpinner periodSpinner = new JSpinner();
    private String[] trends = {"Линейный", "Параболический",
            "Логарифмическая", "Степенная", "Экспоненциальная"};
    private JComboBox trendBox = new JComboBox<>(trends);
    private ModelType modelType = ModelType.ADD;

    public SettingPane(ActionListener updatePanel,
                       ActionListener saveToFile,
                       ChangeListener periodSpinnerAction) {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(2, 2, 2, 2);
        periodSpinner.addChangeListener(periodSpinnerAction);
        addComponents(new JLabel("Период:"), periodSpinner, gbc);

        JRadioButton addTypeBth = new JRadioButton("Аддетивная", true);
        JRadioButton multiTypeBth = new JRadioButton("Мультипликативная", false);
        ButtonGroup group = new ButtonGroup();
        group.add(addTypeBth);
        group.add(multiTypeBth);
        addTypeBth.addActionListener(e -> modelType = ModelType.ADD);
        multiTypeBth.addActionListener(e -> modelType = ModelType.MULTI);

        JPanel groupPane = new JPanel();
        groupPane.setLayout(new GridLayout(2, 1));
        groupPane.add(addTypeBth);
        groupPane.add(multiTypeBth);

        addComponents(new JLabel("Тип модели:"), groupPane, gbc);
        addComponents(new JLabel("Тип тренда:"), trendBox, gbc);
        JButton updateButton = new JButton("Обновить");
        updateButton.addActionListener(updatePanel);
        addCenterComponents(updateButton, gbc);
        JButton saveFromFileButton = new JButton("Сохранить в файл");
        saveFromFileButton.addActionListener(saveToFile);
        addCenterComponents(saveFromFileButton, gbc);
    }

    public ModelType getModelType() {
        return modelType;
    }

    public Integer getPeriodValue() {
        return (Integer) periodSpinner.getValue();
    }

    public TrendLine getTrendLine() {
        switch (trendBox.getSelectedIndex()) {
            case 0:
                return new PolyTrendLine(1);
            case 1:
                return new PolyTrendLine(2);
            case 2:
                return new LogTrendLine();
            case 3:
                return new PowerTrendLine();
            case 4:
                return new ExpTrendLine();
            case 5:
                return new PolyTrendLine(3);
            default:
                return new PolyTrendLine(1);
        }
    }

    public Object getSelectedTrend() {
        return trendBox.getSelectedItem();
    }

    private void addComponents(JLabel label, JComponent component, GridBagConstraints gbc) {
        gbc.anchor = GridBagConstraints.EAST;
        gbc.gridwidth = GridBagConstraints.RELATIVE;
        add(label, gbc);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        add(component, gbc);
    }

    private void addCenterComponents(JComponent component, GridBagConstraints gbc) {
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        add(component, gbc);
    }
}
