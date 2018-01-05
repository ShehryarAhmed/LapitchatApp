package com.logic.tech.chatapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class StatusActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private EditText mStatus;
    private Button mSaveBtn;

    //Firebase
    private DatabaseReference mDatabase;
    private FirebaseUser mUser;

    //Progressbar
    private ProgressDialog mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);
        String status = getIntent().getStringExtra("status");

        mToolbar = (Toolbar) findViewById(R.id.status_app_bar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Account Status");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mStatus = (EditText) findViewById(R.id.status_update);
        mSaveBtn= (Button) findViewById(R.id.update_status);

        mStatus.setText(status);

        mProgress = new ProgressDialog(this);

        mUser = FirebaseAuth.getInstance().getCurrentUser();
        String userId = mUser.getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("User").child(userId);

        mSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mProgress.setTitle("Saving Changes");
                mProgress.setMessage("Please Wait While we save the changes");
                mProgress.show();
                mDatabase.child("status").setValue(mStatus.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            mProgress.dismiss();
                            Intent settingActivity = new Intent(getApplicationContext(),SettingsActivity.class);
                            startActivity(settingActivity);
                            settingActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            finish();
                        }
                        else {
                            Toast.makeText(StatusActivity.this, "", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });

    }
}
