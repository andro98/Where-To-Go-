package com.example.wheretogo;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {

    private static final String TAG = "Login";
    private FirebaseAuth mAuth;

    // Widgets
    private EditText mEmail;
    private EditText mPassword;
    private Button mLogIn;
    private Button mReg;
    private ProgressBar mProg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();

        mEmail = findViewById(R.id.mEmail);
        mPassword = findViewById(R.id.mPSW);
        mLogIn = findViewById(R.id.loginBtn);
        mReg = findViewById(R.id.regBtn);
        mProg = findViewById(R.id.Prog_login);

        init();
    }

    private void init(){
        mProg.setVisibility(View.INVISIBLE);

        mLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logIn();
            }
        });

        mReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openRegister();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(isLogged()){
            openMain();
        }
    }

    private void logIn()
    {
        mProg.setVisibility(View.VISIBLE);
        final String Email = mEmail.getText().toString();
        final String Psw = mPassword.getText().toString();

        if(Email.isEmpty() || Psw.isEmpty()){
            Toast.makeText(this, "Please Enter sth", Toast.LENGTH_SHORT).show();
            mProg.setVisibility(View.INVISIBLE);
        }else{
            mAuth.signInWithEmailAndPassword(Email, Psw).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        Log.d(TAG, "Login: login success");
                        openMain();
                        mProg.setVisibility(View.INVISIBLE);
                    }else{
                        Toast.makeText(Login.this, "Please try again", Toast.LENGTH_SHORT).show();
                        mProg.setVisibility(View.INVISIBLE);
                    }
                }
            });
        }
    }

    private Boolean isLogged(){
        return (mAuth.getCurrentUser() != null);
    }

    private void openMain(){
        Intent intent = new Intent(Login.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void openRegister(){
        Intent intent = new Intent(Login.this, Register.class);
        startActivity(intent);
        finish();
    }
}
