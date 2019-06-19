package forecast.swing;

import forecast.utils.RegularTimeEnum;

import javax.swing.*;
import java.awt.*;

public class FileReaderSettingPane {

    private String separator = ";";
    private RegularTimeEnum timeFormat = RegularTimeEnum.YEARS;

    public void show() {

        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(2,2,2,2);
        JComboBox comboBox = new JComboBox<>(new String[]{";", ","});
        comboBox.setSelectedItem(separator);
        addComponents(new JLabel("Разделитель:"), comboBox, panel, gbc);
        JComboBox regularTimeEnumJComboBox = new JComboBox<>(RegularTimeEnum.values());
        regularTimeEnumJComboBox.setSelectedItem(timeFormat);
        addComponents(new JLabel("Формат временного периода:"), regularTimeEnumJComboBox, panel, gbc);

        int result = JOptionPane.showConfirmDialog(null, panel, "Настройки чтения файла", JOptionPane.YES_NO_OPTION);
        if(result==0){
            separator = (String) comboBox.getSelectedItem();
            timeFormat= (RegularTimeEnum) regularTimeEnumJComboBox.getSelectedItem();
        }
    }

    String getSeparator() {
        return separator;
    }

    RegularTimeEnum getTimeFormat() {
        return timeFormat;
    }

    private void addComponents(JLabel label, JComponent component, JPanel p,
                               GridBagConstraints gbc)
    {
        gbc.anchor = GridBagConstraints.EAST;
        gbc.gridwidth = GridBagConstraints.RELATIVE;
        p.add(label, gbc);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        p.add(component, gbc);
    }
}
