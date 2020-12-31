package com.rachel.firstandroidlesson;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class registration extends AppCompatActivity {

    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        mAuth = FirebaseAuth.getInstance();

    }

    public void registration(View view) {
        EditText textEmail = findViewById(R.id.myEmail);
        String email = textEmail.getText().toString();
        EditText textPass = findViewById(R.id.myPass);
        String password = textPass.getText().toString();
        String phone = ((EditText)findViewById(R.id.phonenumber)).getText().toString();
        String city = ((EditText)findViewById(R.id.city)).getText().toString();
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(registration.this, "registration success.",
                                    Toast.LENGTH_SHORT).show();
                            Person p = new Person(email, phone, city);
                            //path where to input data:
                            FirebaseUser user = mAuth.getCurrentUser(); // get instance of user
                            String uid = user.getUid(); // get current user id
                            FirebaseDatabase database = FirebaseDatabase.getInstance(); // name of project
                            DatabaseReference myRef = database.getReference("persons").child(uid); // input user id

                            myRef.setValue(p);
                            // Sign in success, update UI with the signed-in user's information
                            goToCalc(view);

                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(registration.this, "registration failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }

    private void goToCalc(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}