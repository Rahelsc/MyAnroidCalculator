package com.rachel.firstandroidlesson;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class calcActivity extends AppCompatActivity {
    private TextView textView;
    private TextView userInput;
    private double num1;
    private String result;
    private String op;
    private boolean isNewNumber = false;
    private ArrayList<String> operations;
    private ArrayList<Double> numbers;
    private final String KEY = "MainActivity to CalcActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calc);

        String str = getIntent().getStringExtra(KEY);

        Toast.makeText(this, str, Toast.LENGTH_LONG).show();

        textView = findViewById(R.id.output); // R represent res or resource
        userInput = findViewById(R.id.userInput);
        operations = new ArrayList<>();
        numbers = new ArrayList<>();
    }

    public void funcNumber(View view) {
        Button b = (Button) view;
        textView.append(b.getText());
        userInput.append(b.getText());
        result = textView.getText().toString();
        num1 = Double.parseDouble(result);
        isNewNumber = true; // see comment in funcOperator for clarification
    }

    public void funcOperator(View view) {
        Button b = (Button) view;
        textView.setText("");
        op = b.getText().toString();
        userInput.append(op);

        operations.add(op);

        if (!op.equals("(") && isNewNumber) {
            numbers.add(num1);
            isNewNumber = false; // to make sure only a previously
                                 // un entered number is added to the array
                                 // (in case two operators were pressed consecutively)
        }
    }


    public void funcEqual(View view) {
        if (!op.equals(")")) // for last number to enter
            numbers.add(num1);

        int index = 0;
        while (operations.contains("SQRT")) {
            String checkSQRT = operations.get(index);
            if (checkSQRT.equals("SQRT")) {
                double res = calculate(numbers.get(index));
                operations.remove(index);
                numbers.set(index, res);
            }
        }

        index = 0;

        while (operations.contains("(")) {
            String checkParenthesis = operations.get(index);
            if (checkParenthesis.equals("(")) {
                double res = calculate(numbers.get(index), numbers.get(index + 1),
                        operations.get(index + 1));
                operations.remove(index); // remove open parenthesis
                operations.remove(index); // remove operation
                operations.remove(index); // remove close parenthesis

                numbers.set(index, res);
                numbers.remove(index + 1); // remove number already used
            } else index++;
        }


        index = 0;
        while (operations.contains("*") || operations.contains("/") || operations.contains("%") || operations.contains("POW")) {
            String currentOp = operations.get(index);
            if (currentOp.equals("/") || currentOp.equals("*") || currentOp.equals("%") || currentOp.equals("POW")) {

                double res = calculate(numbers.get(index), numbers.get(index + 1), currentOp);
                operations.remove(index);
                numbers.set(index, res);
                numbers.remove(index + 1);
            } else index++;
        }

        index = 0;
        while (!operations.isEmpty()) {
            double res = calculate(numbers.get(index), numbers.get(index + 1), operations.get(index));
            operations.remove(index);
            numbers.set(index, res);
            numbers.remove(index + 1);
        }
        textView.setText(Double.toString(numbers.get(0)));
        userInput.append("=" + Double.toString(numbers.get(0)));
        num1 = numbers.get(0); // save the last number in case user continues after equal
    }

    public double calculate(double n1, double n2, String o) {
        double sum = 0;
        switch (o) {
            case "+":
                sum = n1 + n2;
                break;
            case "-":
                sum = n1 - n2;
                break;
            case "*":
                sum = n1 * n2;
                break;
            case "/":
                try {
                    sum = 66;
                    if (n2 == 0.0)
                        throw new ArithmeticException("division by zero");
                    sum = n1 / n2;
                } catch (ArithmeticException e) {
                    Toast.makeText(this, "cant devide by zero", Toast.LENGTH_LONG);
                    textView.setText("");
                    userInput.setText("");
                    operations.clear();
                    numbers.clear();
                }
                break;
            case "%":
                try {
                    if (n2 == 0.0)
                        throw new ArithmeticException("division by zero");
                    sum = (int) n1 % (int) n2;
                } catch (ArithmeticException e) {
                    Toast.makeText(this, "cant devide by zero", Toast.LENGTH_LONG);
                    textView.setText("");
                    userInput.setText("");
                    operations.clear();
                    numbers.clear();
                }
                break;
            case "POW":
                sum = Math.pow(n1, n2);
                break;
        }
        return sum;
    }

    public double calculate(double n1) {
        double sum = 0;
        try {
            if (n1 < 0.0)
                throw new ArithmeticException("sqrt for negative number");
            sum = Math.sqrt(n1);
        } catch (ArithmeticException e) {
            Toast.makeText(this, "cant sqrt negative number", Toast.LENGTH_LONG);
            textView.setText("");
            userInput.setText("");
            operations.clear();
            numbers.clear();
        }
        return sum;
    }

    public void clearScreen(View view) {
        textView.setText("");
        userInput.setText("");
        operations.clear();
        numbers.clear();
    }

    public void funcSCI(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}