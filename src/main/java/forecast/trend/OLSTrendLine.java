package forecast.trend;

import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.stat.regression.OLSMultipleLinearRegression;

import java.util.Arrays;

public abstract class OLSTrendLine implements TrendLine {

    RealMatrix coef = null; // сбудет содержать прогнозируемые коэф(оцениваемые параметры) coefs

    protected abstract double[] xVector(double x); // создаем вектор из x
    protected abstract boolean logY(); // по умолчанию true logY, y>0, логарифмировать или нет

    @Override
    public void setValues(double[] y, double[] x) {
        if (x.length != y.length) {
            throw new IllegalArgumentException(String.format("The numbers of y and x values must be equal (%d != %d)",y.length,x.length));
        }
        double[][] xData = new double[x.length][];
        for (int i = 0; i < x.length; i++) {
            // сформирование вектора коэф-тов из одного х
            xData[i] = xVector(x[i]);
        }
        if(logY()) { //в некоторых моделях используем lny вместо y, поэтому заменяем
            y = Arrays.copyOf(y, y.length);
            for (int i = 0; i < x.length; i++) {
                y[i] = Math.log(y[i]);
            }
        }
        //используем лин. регрессию
        OLSMultipleLinearRegression ols = new OLSMultipleLinearRegression();
        ols.setNoIntercept(true);
        ols.newSampleData(y, xData);
        coef = MatrixUtils.createColumnRealMatrix(ols.estimateRegressionParameters()); // получаем матрицу коэф-тов
    }

    @Override
    public double predict(double x) {
        double yhat = coef.preMultiply(xVector(x))[0];
        if (logY()) yhat = (Math.exp(yhat)); // получаем у, если испрльзовали lny
        return yhat;
    }
}
