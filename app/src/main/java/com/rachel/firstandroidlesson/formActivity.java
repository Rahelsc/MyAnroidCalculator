package com.rachel.firstandroidlesson;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class formActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;

    SharedPreferences sharedPreferences;
    EditText user;
    EditText pass;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);
        mAuth = FirebaseAuth.getInstance();

        sharedPreferences = getPreferences(MODE_PRIVATE);

        user = findViewById(R.id.myEmail);
        pass = findViewById(R.id.myPass);

        if( sharedPreferences.getString("keyUser" , null ) != null)
        {
            user.setText(sharedPreferences.getString("keyUser" , null ));
            pass.setText(sharedPreferences.getString("keyPass" , null ));
        }

    }

//    @Override
//    public void onStart() {
//        super.onStart();
//        // Check if user is signed in (non-null) and update UI accordingly.
//        FirebaseUser currentUser = mAuth.getCurrentUser();
////        updateUI(currentUser);
//    }

    public void registration(View view){
        Intent intent = new Intent(this, registration.class);
        startActivity(intent);
    }

    public void login(View view){
//        Intent intent = new Intent( this , MainActivity.class);

        EditText textEmail = findViewById(R.id.myEmail);
        final String email = textEmail.getText().toString();
        EditText textPass = findViewById(R.id.myPass);
        final String password = textPass.getText().toString();
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("keyUser" , user.getText().toString());
                            editor.putString("keyPass" , pass.getText().toString());
                            editor.apply();
                            Toast.makeText(formActivity.this, "Authentication success.",
                                    Toast.LENGTH_SHORT).show();
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            goToCalc();
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(formActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }

    private void goToCalc() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }


}