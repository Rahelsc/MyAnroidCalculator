package com.rachel.firstandroidlesson;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    TextView textView;
    double num1;
    String op;
    ArrayList<String> operations;
    ArrayList<Double> numbers;

// try and take care of the following exception: 5+*/8 = ? what should be the result of that?
// keep in mind that 8+-9 should be ok
// what happens if user doesn't put an operation in parenthesis?? data shouldn't be sent to calculate!
// add a decimal point to the calculator

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // זה מה שמקשר לXML

        textView = findViewById(R.id.output); // R represent res or resource
        operations = new ArrayList<>();
        numbers = new ArrayList<>();
    }

    public void funcNumber(View view) {
        Button b = (Button) view;
        textView.append(b.getText());

        String result = textView.getText().toString();
        num1 = Integer.parseInt(result);
    }

    public void funcOperator(View view) {
        Button b = (Button) view;
        textView.setText("");
        op = b.getText().toString();
        operations.add(op);
        if (!op.equals("("))
            numbers.add(num1);
    }


    public void funcEqual(View view) {
        if (!op.equals("(") && !op.equals(")")) // for last number to enter
            numbers.add(num1);

        int index = 0;
        while (operations.contains("("))
        {
            String checkParenthesis = operations.get(index);
            if (checkParenthesis.equals("(")){
                double res = calculate(numbers.get(index), numbers.get(index+1),
                        operations.get(index+1));
                operations.remove(index); // remove open parenthesis
                operations.remove(index); // remove operation
                operations.remove(index); // remove close parenthesis

                numbers.set(index, res);
                numbers.remove(index+1); // remove number already used
            }
            else index++;
        }

        index = 0;
        while (operations.contains("*") || operations.contains("/"))
        {
            String currentOp = operations.get(index);
            if (currentOp.equals("/") || currentOp.equals("*")){

                double res = calculate(numbers.get(index), numbers.get(index+1), currentOp);
                operations.remove(index);
                numbers.set(index, res);
                numbers.remove(index+1);
            }
            else index++;
        }

        index = 0;
        while (!operations.isEmpty()) {
            double res = calculate(numbers.get(index), numbers.get(index+1), operations.get(index));
            operations.remove(index);
            numbers.set(index, res);
            numbers.remove(index+1);
        }
        textView.setText(Double.toString(numbers.get(0)) + "\n" + numbers.toString());
        num1 = numbers.get(0); // save the last number in case user continues after equal
        numbers.clear();
    }

    public double calculate(double n1, double n2, String o){
        double sum = 0;
        switch (o){
            case "+": sum = n1+n2;
                break;
            case "-": sum = n1-n2;
                break;
            case "*": sum = n1*n2;
                break;
            case "/": sum = n1/n2;
                break;
        }
        return sum;
    }

    public void clearScreen(View view) {
        textView.setText("");
        operations.clear();
        numbers.clear();
    }
}