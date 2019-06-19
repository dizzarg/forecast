package forecast.trend;

public interface TrendLine {

    void setValues(double[] y, double[] x); // y ~ f(x)
    double predict(double x); // получаем прогнозируемый y для данного x

}
