package forecast.trend;

public interface TrendLine {

    public void setValues(double[] y, double[] x); // y ~ f(x)
    public double predict(double x); // получаем прогнозируемый y для данного x

}
