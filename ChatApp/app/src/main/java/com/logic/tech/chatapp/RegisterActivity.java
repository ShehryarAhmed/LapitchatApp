package com.logic.tech.chatapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    private TextInputLayout displayName;
    private TextInputLayout userEmail;
    private TextInputLayout userpassword;
    private Button createBtn;
    private FirebaseAuth mAuth;
    private Toolbar mToolbar;
    private ProgressDialog mProgressDialog;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();

        mToolbar = (Toolbar) findViewById(R.id.register_page_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(R.string.create_account);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mProgressDialog = new ProgressDialog(this);

        displayName = (TextInputLayout) findViewById(R.id.displayName);
        userEmail = (TextInputLayout) findViewById(R.id.reg_email);
        userpassword = (TextInputLayout) findViewById(R.id.reg_password);
        createBtn = (Button) findViewById(R.id.create_account);
        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String deviceToken = FirebaseInstanceId.getInstance().getToken();
                String name = displayName.getEditText().getText().toString();
                String email = userEmail.getEditText().getText().toString();
                String password = userpassword.getEditText().getText().toString();
                if (name.isEmpty() && email.isEmpty() && password.isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "please insert detail", Toast.LENGTH_SHORT).show();
                } else {

                    mProgressDialog.setTitle(getString(R.string.sign_up));
                    mProgressDialog.setMessage("Please Wait while Create Your account");
                    mProgressDialog.setCanceledOnTouchOutside(false);
                    mProgressDialog.show();
                    register_user(name, email, password,deviceToken);


                }
            }
        });
    }

    private void register_user(final String name, String email, String password, final String deviceToken) {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
                    String user_id = current_user.getUid();
                    mDatabase = FirebaseDatabase.getInstance().getReference().child("User").child(user_id);

                    HashMap<String, String> userMap = new HashMap<String, String>();
                    userMap.put("name", name);
                    userMap.put("status", getString(R.string.default_status));
                    userMap.put("image", "default");
                    userMap.put("thumb_image", "default");
                    userMap.put("device_token", deviceToken);

                    mDatabase.setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                mProgressDialog.dismiss();
                                Intent main_activity = new Intent(RegisterActivity.this, MainActivity.class);
                                main_activity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(main_activity);
                                finish();
                            }
                        }
                    });
                } else {
                    String error = "";
                    mProgressDialog.hide();
                    try{
                        throw task.getException();
                    }catch (FirebaseAuthWeakPasswordException e){
                        error = "Weak Password";
                    }catch (FirebaseAuthInvalidCredentialsException e){
                        error = "Invalid Email";
                    }
                    catch (FirebaseAuthUserCollisionException e){
                        error = "Existing Account";
                    }
                    catch (Exception e){
                        error = "Unknown error!";
                        e.printStackTrace();
                    }
                    Toast.makeText(RegisterActivity.this, error, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
