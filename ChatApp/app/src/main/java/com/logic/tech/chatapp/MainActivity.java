package com.logic.tech.chatapp;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private Toolbar mToolbar;

    private ViewPager mViewPager;
    private SelectionPagerAdapter mSelectionPagerAdapter;

    private TabLayout mTabLayout;
    private DatabaseReference mUserRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        mToolbar = (Toolbar) findViewById(R.id.main_page_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(R.string.lapit_chat);

        mUserRef = FirebaseDatabase.getInstance().getReference().child("User").child(mAuth.getCurrentUser().getUid());

        //tabs
        mViewPager = (ViewPager) findViewById(R.id.main_tab_pagers);
        mSelectionPagerAdapter = new SelectionPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mSelectionPagerAdapter);

        mTabLayout = (TabLayout) findViewById(R.id.main_tabs);
        mTabLayout.setupWithViewPager(mViewPager);


    }


    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser == null){
          sendToStart();
        }
        else{
            mUserRef.child("online").setValue("true");
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
        mUserRef.child("online").setValue(ServerValue.TIMESTAMP);
    }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.log_out) {

            mAuth.signOut();
            mUserRef.child("online").setValue(ServerValue.TIMESTAMP);
            sendToStart();

            return true;
        }
        if(id == R.id.setting_activity){
            Intent settingActivity = new Intent(MainActivity.this,SettingsActivity.class);
            startActivity(settingActivity);
        }

        if(id == R.id.all_users){
            Intent allUsersActivity = new Intent(MainActivity.this,UsersActivity.class);
            startActivity(allUsersActivity);
        }
        return super.onOptionsItemSelected(item);
    }
    private void sendToStart() {
        Intent startIntent = new Intent(MainActivity.this,StartActivity.class);
        startActivity(startIntent);
        finish();

    }

}
