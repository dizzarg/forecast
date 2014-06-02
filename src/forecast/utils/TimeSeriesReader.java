package forecast.utils;

import org.jfree.data.time.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

public class TimeSeriesReader {

//    public static final String years = "%d";
//    public static final String month = "%d %d";
//    public static final String days = "%d %d %d";
//    public static final String quarter ="%d %d";

    private String separator;
    private RegularTimeEnum regularTime;

    public void setSeparator(String separator) {
        this.separator = separator;
    }

    public void setRegularTime(RegularTimeEnum regularTime) {
        this.regularTime = regularTime;
    }

    public TimeSeries loadTimeSeriesFromFile(Comparable name, File file) throws Exception{
        BufferedReader reader = new BufferedReader(new FileReader(file));
        TimeSeries fileSeries = new TimeSeries(name);
        Map<String, Double> fileValue = new HashMap<String, Double>();
        RegularTimeEnum type = null;
        while (reader.ready()){
            String line = reader.readLine();
            String[] values = line.split(";");
            String[] dateValue = values[0].split(" ");
            if(type==null){
                if(dateValue.length==1) type=RegularTimeEnum.YEARS;
                if(dateValue.length==3) type=RegularTimeEnum.DAYS;
                if(dateValue.length==2){
                    String year = dateValue[0];
                    String part = dateValue[1];

                }
            }
            fileValue.put(values[0], Double.valueOf(values[1]));
        }
        for (String s: fileValue.keySet()){

        }
        reader.close();
        return fileSeries;
    }

    public void loadTimeSeriesFromFile(TimeSeries fileSeries,  File file) throws Exception{
        BufferedReader reader = new BufferedReader(new FileReader(file));
        while (reader.ready()){
            String line = reader.readLine();
            String[] values = line.split(separator);
            RegularTimePeriod period = parsePeriod(values[0]);
            Double value = Double.parseDouble(values[1]);
            fileSeries.add(period, value);
        }
        reader.close();
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
            case YEARS: period = new Year(Integer.parseInt(value)); break;
        }
        return period;
    }

    public static TimeSeries loadTimeSeriesFromFile(TimeSeries fileSeries, String timeFormat, File file, String separator) throws Exception{
        BufferedReader reader = new BufferedReader(new FileReader(file));
        while (reader.ready()){
            String line = reader.readLine();
            String[] values = line.split(separator);
            Year year = new Year(Integer.parseInt(values[0]));
            Double value = Double.parseDouble(values[1]);
            fileSeries.add(year, value);
        }
        return fileSeries;
    }

}
