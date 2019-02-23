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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Register extends AppCompatActivity {

    private static final String TAG = "Register";
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
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();

        mEmail = findViewById(R.id.mEmail_reg);
        mPassword = findViewById(R.id.mPSW_reg);
        mLogIn = findViewById(R.id.logBtn_reg);
        mReg = findViewById(R.id.regBtn_reg);
        mProg = findViewById(R.id.Prog_register);
        init();
    }

    private void init(){
        mProg.setVisibility(View.INVISIBLE);

        mLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openLogin();
            }
        });

        mReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(isLogged()){
            openLogin();
        }
    }

    private void register()
    {
        mProg.setVisibility(View.VISIBLE);
        final String Email = mEmail.getText().toString();
        final String Psw = mPassword.getText().toString();

        if(Email.isEmpty() || Psw.isEmpty()){
            Toast.makeText(this, "Please Enter sth", Toast.LENGTH_SHORT).show();
            mProg.setVisibility(View.INVISIBLE);
        }else{
            mAuth.createUserWithEmailAndPassword(Email, Psw).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        Log.d(TAG, "Login: login success");
                        openMain();
                        mProg.setVisibility(View.INVISIBLE);
                    }else{
                        Toast.makeText(Register.this, "Please try again", Toast.LENGTH_SHORT).show();
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
        Intent intent = new Intent(Register.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void openLogin(){
        Intent intent = new Intent(Register.this, Login.class);
        startActivity(intent);
        finish();
    }
}
