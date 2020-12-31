package com.rachel.firstandroidlesson;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;

import Fragments.fragmentCalc;
import Fragments.fragmentScientific;

public class FragmentActivity extends AppCompatActivity {

    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);

        operations = new ArrayList<>();
        numbers = new ArrayList<>();
        textView = findViewById(R.id.output);
        userInput = findViewById(R.id.userInput);

        Switch s = findViewById(R.id.switch1);

        s.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
             if (isChecked)
                loadScientific();
             else
             {
                 fragmentTransaction = fragmentManager.beginTransaction();
                 fragmentTransaction.replace(R.id.fragmentPlace, new fragmentCalc()).addToBackStack(null).commit();
             }
            }
        });

        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fragmentPlace, new fragmentCalc()).commit();
    }

    public void loadScientific() {
        Log.d("yoad", "here");
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentPlace, new fragmentScientific()).addToBackStack(null).commit();
    }


    // --------------------------------------------------

    private TextView textView;
    private TextView userInput;
    private double num1;
    private String result;
    private String op;
    private boolean isNewNumber = false;
    private ArrayList<String> operations;
    private ArrayList<Double> numbers;

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
}