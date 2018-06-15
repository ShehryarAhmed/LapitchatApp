package com.logic.tech.chatapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UsersActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private RecyclerView mUserList;
    private DatabaseReference mUsersDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);

        mUsersDatabase = FirebaseDatabase.getInstance().getReference().child("User");
        mToolbar = (Toolbar) findViewById(R.id.users_app_bar);
        setSupportActionBar(mToolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.all_users);

        mUserList = (RecyclerView) findViewById(R.id.user_list);
        mUserList.setHasFixedSize(true);
        mUserList.setLayoutManager(new LinearLayoutManager(this));


    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<User, UserViewholder> firebaseRecyclerAdapter
                = new FirebaseRecyclerAdapter<User, UserViewholder>(
                        User.class,
                        R.layout.users_single_layout,
                        UserViewholder.class,
                        mUsersDatabase
        ) {
            @Override
            protected void populateViewHolder(UserViewholder viewHolder, User model, int position) {
                viewHolder.setName(model.getName());
            }
        };
        mUserList.setAdapter(firebaseRecyclerAdapter);
    }

    public static class UserViewholder extends RecyclerView.ViewHolder{

        View mView;
        public UserViewholder(View itemView) {
            super(itemView);
            mView = itemView;
        }
        public void setName(String name){
            TextView  userNameView = (TextView) mView.findViewById(R.id.single_usre_name);
            userNameView.setText(name);
        }
        public void setStatus(String status){
            TextView  textView =(TextView)  mView.findViewById(R.id.single_user_status);

        }
        public void setImage(){

        }
    }
}

