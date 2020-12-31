package com.rachel.firstandroidlesson;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {


    private TextView textView;
    private TextView userInput;
    private double num1;
    private String op;
    private boolean isNewNumber = false;
    private ArrayList<String> operations;
    private ArrayList<Double> numbers;
    private final String KEY = "MainActivity to CalcActivity";
    private FirebaseAuth mAuth;

// try and take care of the following exception: 5+*/8 = ? what should be the result of that?
// keep in mind that 8+-9 should be ok
// what happens if user doesn't put an operation in parenthesis?? data shouldn't be sent to calculate!
// solve toast - why doesn't it work?

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); // תשמור את המצב הקיים של האנדרואיד כדי שאם יקרוס זה לא יהרוס את מערכת ההפעלה
        setContentView(R.layout.activity_main); // זה מה שמקשר לXML
        textView = findViewById(R.id.output); // R represent res or resource
        userInput = findViewById(R.id.userInput);
        operations = new ArrayList<>();
        numbers = new ArrayList<>();
        mAuth = FirebaseAuth.getInstance();

        FirebaseUser user = mAuth.getCurrentUser(); // get instance of user
        String uid = user.getUid(); // get current user id
        FirebaseDatabase database = FirebaseDatabase.getInstance(); // name of project
        DatabaseReference myRef = database.getReference("persons").child(uid); // input user i
        // causes crashing for the login form
//        myRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                Log.d("yoad","whatttt??");
//                // This method is called once with the initial value and again
//                // whenever data at this location is updated.
//                String value = dataSnapshot.getValue(Person.class).getEmail();
//                userInput.setText("hello "+value);
//            }
//
//            @Override
//            public void onCancelled(DatabaseError error) {
//                // Failed to read value
//            }
//        });

        /*
        *
        * public class MainActivity extends AppCompatActivity {
            SharedPreferences sharedPreferences;
            EditText user;
            EditText pass;
            @Override
            protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_main);
                sharedPreferences = getPreferences(MODE_PRIVATE);

                 user = findViewById(R.id.editTextUser);
                 pass = findViewById(R.id.editTextPass);

                if( sharedPreferences.getString("keyUser" , null ) != null)
                {
                  user.setText(sharedPreferences.getString("keyUser" , null ));
                  pass.setText(sharedPreferences.getString("keyPass" , null ));
                }
            }

            public void func(View view) {



                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("keyUser" , user.getText().toString());
                editor.putString("keyPass" , pass.getText().toString());
                editor.apply();

                Intent intent = new Intent( this , MainActivity2.class);

        *
        *
        *
        * */


    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    public void funcNumber(View view) {
        Button b = (Button) view;
        textView.append(b.getText());
        userInput.append(b.getText());
        String result = textView.getText().toString();
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

        textView.setText(numbers.toString() + "\n" + operations.toString());

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
        textView.setText(Double.toString(numbers.get(0)));
        userInput.append("=" + Double.toString(numbers.get(0)));
        num1 = numbers.get(0); // save the last number in case user continues after equal
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
            case "/":
                try {
                    if (n2 == 0.0)
                        throw new ArithmeticException("division by zero");
                    sum = n1/n2;
                }
                catch (ArithmeticException e){
                    Toast.makeText(this, "cant devide by zero", Toast.LENGTH_LONG);
                    textView.setText("");
                    userInput.setText("");
                    operations.clear();
                    numbers.clear();
                }
                break;

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
        Intent intent = new Intent(this, calcActivity.class);
        intent.putExtra(KEY, "welcome");
        startActivity(intent);
    }
}