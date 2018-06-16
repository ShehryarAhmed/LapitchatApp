package com.logic.tech.chatapp;

import android.app.ProgressDialog;
import android.icu.text.UnicodeSetSpanner;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;

public class ProfileActivity extends AppCompatActivity {

    ImageView profileImage;
    TextView profileName;
    TextView profileStatus;
    TextView totalFriend;
    TextView mutualFirens;
    Button mFriendReqSendBtn, mFriendReqDeclin;

    private DatabaseReference mUserDatabaseReference;
    private DatabaseReference mFriendReqDatabase;
    private DatabaseReference mFriendsDatabase;

    private ProgressDialog mProgressDialog;
    private String mCurrentState = "not_friend";

    private FirebaseUser mCurrentUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setTitle("Loading User Data");
        mProgressDialog.setMessage("Please Wait User data loading");
        mProgressDialog.show();
        mProgressDialog.setCanceledOnTouchOutside(false);


        final String userID = getIntent().getStringExtra("user_id");
        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        Log.d("TAG", "onCreate: "+mCurrentUser.getUid());
        mUserDatabaseReference = FirebaseDatabase.getInstance().getReference().child("User").child(userID);
        mFriendReqDatabase = FirebaseDatabase.getInstance().getReference().child("Friend_req");
        mFriendsDatabase = FirebaseDatabase.getInstance().getReference().child("Friends");

        profileImage = (ImageView) findViewById(R.id.profile_image);
        profileName = (TextView) findViewById(R.id.user_id);
        profileStatus = (TextView) findViewById(R.id.user_status);
        totalFriend = (TextView) findViewById(R.id.num_of_friends);
        mutualFirens = (TextView) findViewById(R.id.mutual_friend);
        mFriendReqSendBtn = (Button) findViewById(R.id.btn_send_request);
        mFriendReqDeclin = (Button) findViewById(R.id.btn_declain_request);


        mUserDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String image = dataSnapshot.child("image").getValue().toString();
                String display_name = dataSnapshot.child("name").getValue().toString();
                String status = dataSnapshot.child("status").getValue().toString();

                Glide.with(getApplicationContext()).load(image).placeholder(R.drawable.default_image).into(profileImage);
                profileName.setText(""+display_name);
                profileStatus.setText(""+status);

                //-------------------------Friend List / Request Featured --------------//

                mFriendReqDatabase.child(mCurrentUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.hasChild(userID)){

                            String req_type = dataSnapshot.child(userID).child("request_type").getValue().toString();

                            if(req_type.equals("received")){

                                mCurrentState = "req_received";
                                mFriendReqSendBtn.setText("Accept Friend Request");

                                mFriendReqDeclin.setEnabled(true);
                                mFriendReqDeclin.setVisibility(View.VISIBLE);

                            }else if(req_type.equals("sent")){

                                mCurrentState = "req_sent";
                                mFriendReqSendBtn.setText("Cancel Friend Request");


                            }
                        }
                        else {
                            mFriendsDatabase.child(mCurrentUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.hasChild(userID)){
                                        mCurrentState = "friends";
                                        mFriendReqSendBtn.setText("Unfriend this profile");



                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                mProgressDialog.dismiss();


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        mFriendReqSendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mFriendReqSendBtn.setEnabled(false);
                //-------------------------Not Friend --------------//
                if(mCurrentState.equals("not_friend")){




                    mFriendReqDatabase.child(mCurrentUser.getUid()).child(userID).child("request_type")
                            .setValue("sent").addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                    mFriendReqDatabase.child(userID).child(mCurrentUser.getUid()).child("request_type")
                            .setValue("received").addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                            Toast.makeText(ProfileActivity.this, "Request Sent Successfully", Toast.LENGTH_SHORT).show();

                            mFriendReqSendBtn.setEnabled(true);
                            mCurrentState = "req_sent";
                            mFriendReqSendBtn.setText("Cancel Friend Request");

                            }


                            else{
                                mFriendReqSendBtn.setEnabled(true);
                                Toast.makeText(ProfileActivity.this, "Failed Sending Request", Toast.LENGTH_SHORT).show();


                            }
                         }
                             });
                            }
                            else{



                                mFriendReqSendBtn.setEnabled(true);
                                Toast.makeText(ProfileActivity.this, "Failed Sending Request", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                //-------------------------Cancel Friend --------------//
                if(mCurrentState.equals("req_sent")) {

                    mFriendReqDatabase.child(mCurrentUser.getUid()).child(userID).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            mFriendReqDatabase.child(userID).child(mCurrentUser.getUid()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    mFriendReqSendBtn.setEnabled(true);
                                    mCurrentState = "not_friend";
                                    mFriendReqSendBtn.setText("Send Friend Request");

                                }
                            });

                        }
                    });
                }
                //-------------------------Req Received Friend --------------//
                if(mCurrentState.equals("req_received")){


                    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
                    final String currentDateandTime = sdf.format(new Date());

                    mFriendsDatabase.child(mCurrentUser.getUid()).child(userID).setValue(currentDateandTime)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            mFriendsDatabase.child(userID).child(mCurrentUser.getUid()).setValue(currentDateandTime)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                                mFriendReqDatabase.child(mCurrentUser.getUid()).child(userID).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        mFriendReqDatabase.child(userID).child(mCurrentUser.getUid()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {

                                                                mCurrentState = "friends";
                                                                mFriendReqSendBtn.setText("Unfriend this profile");

                                                                mFriendReqDeclin.setEnabled(true);
                                                                mFriendReqDeclin.setVisibility(View.VISIBLE);

                                                            }
                                                        });

                                                    }
                                                });
                                            }

                                    });
                        }
                    });
                    mFriendReqSendBtn.setEnabled(true);

                }
                //------------------already friend----------------------
                if(mCurrentState.equals("friends")){
                    mFriendsDatabase.child(mCurrentUser.getUid()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                mFriendsDatabase.child(userID).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()) {

                                            mCurrentState = "not_friend";
                                            mFriendReqSendBtn.setText("Send Friend Request");
                                        }
                                    }
                                });
                            }
                        }
                    });
                }
                mFriendReqSendBtn.setEnabled(true);
            }

        });

    }
}
