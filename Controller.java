package sample;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import java.util.Random;
import java.util.stream.IntStream;

public class Controller  {
@FXML private Button calc;
@FXML private TextField solution;
@FXML private TextField assesment;
@FXML private TextField fault;
@FXML private TextField dispersion;
@FXML private Spinner<Integer> upperLimit;
@FXML private Spinner<Integer> lowerLimit;
@FXML private Spinner<Integer> numTest;

private double a, b, U, sum, x;
private int iteration;

private IntegralFunc integral = new IntegralFunc() {
    @Override
    public double integral(double x) {
        return Math.pow(x, 4) + 2;
    }

    @Override
    public double integratedFunc(double x){
        return Math.pow(x, 5) / 5 + 2 * x;
    }

    @Override
    public double integratedtwoFunc(double x){
        return Math.pow(x, 6) / 30 + Math.pow(x, 2);
    }
};


@FXML
private void calcIntegral(){
    clearFields();

    a = upperLimit.getValue();
    b = lowerLimit.getValue();
    iteration = numTest.getValue();
    sum = 0.0;
    Random random = new Random();

     //точное значение
    double exactSol = integral.integratedFunc(a) - integral.integratedFunc(b);
    solution.appendText(Double.toString(exactSol));

     //метод монте-карло
    IntStream.range(0, iteration)
            .forEach(index -> {
                U = random.nextDouble();
                x = b + (a - b) * U;
                sum += integral.integral(x);
            });

    //вводим оценку в textfield
    double assesmentMonteKarlo = (a - b)* sum / iteration;
    assesment.appendText(String.format("%.3f", assesmentMonteKarlo));
    //вводим погрешность в textfield
    double faultMonteKarlo = Math.abs(exactSol - assesmentMonteKarlo);
    fault.appendText(String.format("%.3f", faultMonteKarlo));
    //вводим дисперсию в textfield
    double dispersMonteKarlo = (integral.integratedtwoFunc(a) - integral.integratedFunc(b))
            *(a - b) - Math.pow(assesmentMonteKarlo, 2);
    dispersion.appendText(String.format("%.3f", dispersMonteKarlo));
    }

    private void clearFields(){
        solution.clear();
        assesment.clear();
        fault.clear();
        dispersion.clear();
    }

    @FXML
    private void exit(){
        Platform.exit();
    }
}





