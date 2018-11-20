package sample;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import java.util.Random;
import java.util.stream.IntStream;

public class Controller  {
@FXML private TextField solution;
@FXML private TextField assesment;
@FXML private TextField fault;
@FXML private TextField DO;
@FXML private TextField dispersion;
@FXML private Spinner<Integer> upperLimit;
@FXML private Spinner<Integer> lowerLimit;
@FXML private Spinner<Integer> numTest;

private double a, b, U, sum, x;
private int iteration;
private final double z = 1.96;
private IntegralFunc integral = (x) -> Math.pow(x, 4) + 2,
                     integratedFunc = (x) -> Math.pow(x, 5) / 5 + 2 * x,
                     integratedtwoFunc = (x) -> Math.pow(x, 9) / 9 + Math.pow(x, 5) * 0.8 + 4 * x;


@FXML
private void calcIntegral(){
    clearFields();

    a = upperLimit.getValue();
    b = lowerLimit.getValue();
    iteration = numTest.getValue();
    sum = 0.0;
    Random random = new Random();

     //точное значение
    double exactSol = integratedFunc.calculate(a) - integratedFunc.calculate(b);
    solution.appendText(Double.toString(exactSol));

     //метод монте-карло
    IntStream.range(0, iteration)
            .forEach(index -> {
                U = random.nextDouble();
                x = b + (a - b) * U;
                sum += integral.calculate(x);
            });

    /*
    если интеграл двойной, то
    public static double doubleIntMonteCarlo(double a, double b, double c, double d, int n)
{
     double sum = 0D, xSum = 0D, ySum = 0D;

     for (int i = 0; i < n; i++)
     {
           xSum = a + Math.random() * Math.abs(b - a);
           ySum = c + Math.random() * Math.abs(d - c);
           sum += f(xSum, ySum);
     }
     return Math.abs(b - a) * Math.abs(d - c) * sum / n;
}
     */

    //вводим оценку в textfield
    double assesmentMonteKarlo = (a - b)* sum / iteration;
    assesment.appendText(String.format("%.3f", assesmentMonteKarlo));
    //вводим погрешность в textfield
    double faultMonteKarlo = Math.abs(exactSol - assesmentMonteKarlo);
    fault.appendText(String.format("%.3f", faultMonteKarlo));
    //вводим дисперсию в textfield
    double dispersMonteKarlo = (integratedtwoFunc.calculate(a) - integratedtwoFunc.calculate(b))
            *(a - b) - Math.pow(exactSol, 2);
    dispersion.appendText(String.format("%.3f", dispersMonteKarlo));
    //доверительный интверал
    double loyalinter1 = assesmentMonteKarlo + z* Math.sqrt(dispersMonteKarlo/iteration);
    double loyalinter2 = assesmentMonteKarlo - z* Math.sqrt(dispersMonteKarlo/iteration);
    String loyalinter = "(" + String.format("%.2f", loyalinter2) + "; " + String.format("%.2f", loyalinter1) + ")";
    DO.appendText(loyalinter);
    }

    private void clearFields(){
        solution.clear();
        assesment.clear();
        fault.clear();
        dispersion.clear();
        DO.clear();
    }

    @FXML
    private void exit(){
        Platform.exit();
    }
}





