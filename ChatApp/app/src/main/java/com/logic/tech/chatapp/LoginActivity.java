package com.logic.tech.chatapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private TextInputLayout userEmail;
    private TextInputLayout userpassword;
    private Button loginBtn;
    private FirebaseAuth mAuth;
    private Toolbar mToolbar;
    private ProgressDialog mProgressDialog;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        mProgressDialog = new ProgressDialog(this);

        mToolbar = (Toolbar) findViewById(R.id.login_page_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(R.string.login_into_your_account);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        userEmail = (TextInputLayout) findViewById(R.id.reg_email);
        userpassword = (TextInputLayout) findViewById(R.id.reg_password);
        loginBtn = (Button) findViewById(R.id.login_account);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = userEmail.getEditText().getText().toString();
                String password = userpassword.getEditText().getText().toString();
                if (email.isEmpty()&&password.isEmpty()){
                    Toast.makeText(LoginActivity.this, "please insert detail", Toast.LENGTH_SHORT).show();
                }
                else {
                    mProgressDialog.setTitle(R.string.login);
                    mProgressDialog.setMessage("Please Wait while Login Your account");
                    mProgressDialog.setCanceledOnTouchOutside(false);
                    mProgressDialog.show();
                    login_user(email,password);
                    
                }}
        });
}

    private void login_user(String email, String password) {
        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(!task.isSuccessful()){
                    mProgressDialog.hide();
                    Toast.makeText(LoginActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
                }
                else{
                    mProgressDialog.dismiss();
                    Intent main_activity = new Intent(LoginActivity.this,MainActivity.class);
                    main_activity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                    startActivity(main_activity);
                }
            }
        });
    }
}
