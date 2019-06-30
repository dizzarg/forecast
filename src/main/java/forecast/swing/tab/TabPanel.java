package forecast.swing.tab;

import forecast.swing.ExceptionPane;
import forecast.trend.TrendLine;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.time.RegularTimePeriod;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.time.TimeSeriesDataItem;
import org.jfree.ui.ExtensionFileFilter;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;

public class TabPanel extends JPanel{

    private ChartPanel chartPanel = null;

    private JPanel infoPane = new JPanel();
    private SettingPane settingPane;
    private JCheckBox showCenterMoveLineSeries = new JCheckBox("Отобразить центрировую скользящую средную:", false);
    private TimeSeriesCollection collection = new TimeSeriesCollection();
    private double determination;
    private TimeSeries sma;
    private TimeSeries forecast;
    private TimeSeries error;
    private double ysr;

    public TabPanel(String title, TimeSeries fileSeries) {
        setLayout(new BorderLayout());
        showCenterMoveLineSeries.addActionListener(e -> updatePanel(title, fileSeries));

        settingPane = new SettingPane(
                e -> updatePanel(title, fileSeries),
                e -> saveToFile(),
                e -> {
                    infoPane.revalidate();
                    infoPane.repaint();
                }
        );
        updateInfoPane();

        JPanel panel = new JPanel();
        panel.add(settingPane);
        add(infoPane, BorderLayout.NORTH);
        add(panel, BorderLayout.EAST);
        collection.addSeries(fileSeries);
        JFreeChart chart = createChart(title);
        updateChart(chart);
        add(chartPanel, BorderLayout.CENTER);
    }

    private void saveToFile() {
        try {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileFilter(new ExtensionFileFilter("Текстовый файл", "txt"));
            fileChooser.setDialogTitle("Сохранить в файл");
            fileChooser.setApproveButtonText("88");
            fileChooser.setCurrentDirectory(new File( "." ));
            fileChooser.showSaveDialog(null);
            File file = fileChooser.getSelectedFile();
            if(file!=null){
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                    SimpleDateFormat format = new SimpleDateFormat("yyyy MM dd");
                    if (!sma.isEmpty()) {
                        writeSMA(writer, format);
                    }
                    if (!sma.isEmpty()) {
                        writeSMA(writer, format);
                    }
                    writer.write("Среднее значения = " + ysr);
                    writer.newLine();
                    if (!error.isEmpty()) {
                        writeError(writer, format);
                    }
                    if (!forecast.isEmpty()) {
                        writeForecast(writer, format);
                    }
                    writer.flush();
                }
                JOptionPane.showMessageDialog(this, "Файл успешно сохранен!");
            }
        }
        catch (IOException e) {
            ExceptionPane.show("Ошибка присохранении в файл", e);
        }
    }

    private void writeForecast(BufferedWriter writer, SimpleDateFormat format) throws IOException {
        writer.write(forecast.getKey().toString());
        writer.newLine();
        for (Object o: forecast.getItems()){
            writeLine(writer, format, (TimeSeriesDataItem) o);
        }
    }

    private void writeError(BufferedWriter writer, SimpleDateFormat format) throws IOException {
        writer.write(error.getKey().toString());
        writer.newLine();
        for (Object o: error.getItems()){
            writeLine(writer, format, (TimeSeriesDataItem) o);
        }
    }

    private void writeSMA(BufferedWriter writer, SimpleDateFormat format) throws IOException {
        writer.write(sma.getKey().toString());
        writer.newLine();
        for (Object o: sma.getItems()){
            writeLine(writer, format, (TimeSeriesDataItem) o);
        }
    }

    private void writeLine(BufferedWriter writer, SimpleDateFormat format, TimeSeriesDataItem item) throws IOException {
        writer.write(format.format(item.getPeriod().getStart()) + ";" + item.getValue());
        writer.newLine();
    }

    private void updateInfoPane() {
        infoPane.add(new JLabel("Перод: " + settingPane.getPeriodValue()));
        infoPane.add(new JLabel("Модель тренда: " + settingPane.getSelectedTrend()));
        switch (settingPane.getModelType()) {
            case ADD:
                infoPane.add(new JLabel("Тип тренда: аддетивная"));
                break;
            case MULTI:
                infoPane.add(new JLabel("Тип тренда: мультипликативная"));
                break;
            default:
                // nothing to do
        }
        infoPane.add(new JLabel(String.format("Коэффициент детерминации = %s", determination)));
    }

    private void updatePanel(String title, TimeSeries fileSeries) {
        Integer period = settingPane.getPeriodValue();
        int itemCount = fileSeries.getItemCount();
        collection = new TimeSeriesCollection();
        collection.addSeries(fileSeries);
        sma = new TimeSeries("Скользащая средная c периодом " + period);
        if(period>0){
            boolean isodd = period%2==0;
            int k = (isodd?period/2:(period-1)/2);
            for (int j=k;j<itemCount-k;j++){
                RegularTimePeriod p = fileSeries.getTimePeriod(j);
                double sum=0;
                for (int i=j-k; i<j+k; i++){
                    sum += fileSeries.getValue(i).doubleValue();
                }
                if(isodd){
                    sum -= fileSeries.getValue(j-k).doubleValue()/2;
                    sum -= fileSeries.getValue(j+k).doubleValue()/2;
                }
                sma.add(p, sum/(period-1));
            }
            collection.addSeries(sma);
        }
//        collection.addSeries(moveAgr);
//        TimeSeries centerMoveAgr = createMoveAgr("Центрированная скользящая средняя", moveAgr);
//        centerMoveAgr = createMoveAgr("Центрированная скользящая средняя", sma);
        TimeSeries trendSeries = new TimeSeries("Тренд");
        TimeSeries evaluationSC = new TimeSeries("Оценка сезонной компоненты");
        TimeSeries adjustedSC = new TimeSeries("Скорректированная сезонная компонента");
        TimeSeries ts = new TimeSeries("ts");
        error = new TimeSeries("Остатки регрессии");
        forecast = new TimeSeries("Прогноз");
        if(showCenterMoveLineSeries.isSelected()){
//            collection.addSeries(centerMoveAgr);
        }
        for (int i=0; i<fileSeries.getItemCount(); i++){
            RegularTimePeriod timePeriod = fileSeries.getTimePeriod(i);
            if(sma.getValue(timePeriod)!=null){
                double f = getValue(fileSeries, i);
                double c = getValue(sma, timePeriod);
                evaluationSC.add(timePeriod, getResult(f, c));
            } else {
                evaluationSC.add(timePeriod, 0);
            }
        }
        // Показатели
        if(period>0 && period <=itemCount) {
            double[] pokazateli = new double[period];
            for (int i=0; i<itemCount; i++){
                pokazateli[i%period]+= getValue(evaluationSC, i) + getValue(fileSeries, i);
            }
            double sum=0;
            for (int i=0; i<pokazateli.length;i++){
                sum+=pokazateli[i];
            }
            double k = 1;
            switch (settingPane.getModelType()) {
                case ADD:
                    k = period / sum;
                    break;
                case MULTI:
                    k = period / sum;
                    break;
                default:
                    // nothing to do
            }
            for (int i=0; i<pokazateli.length;i++){
              //  pokazateli[i] = getAResult(pokazateli[i], k);
                pokazateli[i] = pokazateli[i] * k;
            }
            for (int i=0;i<itemCount;i++){
                adjustedSC.add(fileSeries.getTimePeriod(i), pokazateli[i%period]);
            }
        }
//        collection.addSeries(adjustedSC);

        TrendLine line = settingPane.getTrendLine();
        double[] x = new double[itemCount];
        double[] y = new double[itemCount];
        for (int i=0; i<itemCount; i++){
            TimeSeriesDataItem item = fileSeries.getDataItem(i);
            y[i] = item.getValue().doubleValue();
            x[i] = item.getPeriod().getSerialIndex();
        }
        line.setValues(y, x);
        ysr =0;
        for (int i=0; i<itemCount; i++){
            RegularTimePeriod p = fileSeries.getTimePeriod(i);
            ysr +=getValue(fileSeries, p);
            trendSeries.add(p, line.predict(p.getSerialIndex()));
        }
        ysr=ysr/itemCount;
        double e2 =0; // ESS - сумма квадратов остатков регрессии
        double yysr =0;  // TSS - общая сумма квадратов.
        for (int i = 0; i < itemCount - 1; i++) {
            double t = getValue(trendSeries, i);
            double s = getValue(adjustedSC, i);
            double f = getValue(fileSeries, i);
            double val = getAResult(t, s);
            RegularTimePeriod timePeriod = fileSeries.getTimePeriod(i);
            ts.add(timePeriod, val);
            double e = getResult(f, val);
            error.add(timePeriod, e);
            e2 += e*e;
            yysr += (f-ysr)*(f-ysr);
        }
        determination = 1 - e2 / yysr;
        RegularTimePeriod p = fileSeries.getTimePeriod(itemCount-1);
        for (int i=0; i<period; i++){
            p = p.next();
            double v=0;
            switch (settingPane.getModelType()) {
                case ADD:
                    v = line.predict(p.getSerialIndex()) + getValue(adjustedSC, i) + getValue(error, i);
                    break;
                case MULTI:
                    v = Math.log(line.predict(p.getSerialIndex())) + Math.log(getValue(adjustedSC, i)) + Math.log(getValue(error, i));
                    v = Math.exp(v);
                    break;
                default:
                    // nothing to do
            }
            forecast.add(p, v);
        }
        collection.addSeries(forecast);

        JFreeChart chart = createChart(title);
        updateChart(chart);
        infoPane.removeAll();
        updateInfoPane();
        infoPane.revalidate();
        infoPane.repaint();
    }

    private double getResult(double f, double c) {
        double result = 0;
        switch (settingPane.getModelType()) {
            case ADD:
                result = f - c;
                break;
            case MULTI:
                result = f / c;
                break;
            default:
                // nothing to do
        }
        return result;
    }

    private double getAResult(double f, double c) {
        double result = 0;
        switch (settingPane.getModelType()) {
            case ADD:
                result = f + c;
                break;
            case MULTI:
                result = f * c;
                break;
            default:
                // nothing to do
        }
        return result;
    }

    private double getValue(TimeSeries timeSeries, RegularTimePeriod timePeriod) {
        return timeSeries.getValue(timePeriod).doubleValue();
    }

    private double getValue(TimeSeries timeSeries, int i) {
        return timeSeries.getValue(i).doubleValue();
    }

    private JFreeChart createChart(String title) {
        return ChartFactory.createTimeSeriesChart(
                title,
                "Временной ряд",
                "Значения ряда",
                collection,
                true,
                true,
                false
        );
    }

    private void updateChart(JFreeChart chart) {
        if(chartPanel==null){
            chartPanel = new ChartPanel(chart);
        } else {
            chartPanel.setChart(chart);
        }
    }
}
