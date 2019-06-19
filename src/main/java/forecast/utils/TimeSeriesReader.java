package forecast.utils;

import org.jfree.data.time.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class TimeSeriesReader {

    private String separator;
    private RegularTimeEnum regularTime;

    public void setSeparator(String separator) {
        this.separator = separator;
    }

    public void setRegularTime(RegularTimeEnum regularTime) {
        this.regularTime = regularTime;
    }

    public void loadTimeSeriesFromFile(TimeSeries fileSeries,  File file) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            while (reader.ready()) {
                String line = reader.readLine();
                String[] values = line.split(separator);
                RegularTimePeriod period = parsePeriod(values[0]);
                Double value = Double.parseDouble(values[1]);
                fileSeries.add(period, value);
            }
        }
    }

    private RegularTimePeriod parsePeriod(String value) {
        RegularTimePeriod period = null;
        switch (regularTime){
            case DAYS:{
                String[] vals = value.split(" ");
                int year = Integer.parseInt(vals[0]);
                int month = Integer.parseInt(vals[1]);
                int day = Integer.parseInt(vals[2]);
                period = new Day(day, month, year);
            } break;
            case MONTH:{
                String[] vals = value.split(" ");
                Year year = new Year(Integer.parseInt(vals[0]));
                period = new Month(Integer.parseInt(vals[1]), year);
            } break;
            case QUARTER:{
                String[] vals = value.split(" ");
                Year year = new Year(Integer.parseInt(vals[0]));
                period = new Quarter(Integer.parseInt(vals[1]), year);
            } break;
            case YEARS:
                period = new Year(Integer.parseInt(value));
                break;
            default:
                // Nothing to do
        }
        return period;
    }

}
