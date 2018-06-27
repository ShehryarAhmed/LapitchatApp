package com.logic.tech.chatapp;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AlertDialogLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class FriendsFragment extends Fragment {

    private String mCurrent_user_id;
    private View mMainView;
    private RecyclerView mFriendList;
    private FirebaseAuth mAuth;
    private DatabaseReference mFriendsDatabase;
    private DatabaseReference mUsersDatabase;

    public FriendsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mMainView = inflater.inflate(R.layout.fragment_friends, container, false);

        mFriendList = (RecyclerView) mMainView.findViewById(R.id.friends_list);
        mAuth = FirebaseAuth.getInstance();

        mCurrent_user_id = mAuth.getCurrentUser().getUid();

        mFriendsDatabase = FirebaseDatabase.getInstance().getReference().child("Friends").child(mCurrent_user_id);
        mFriendsDatabase.keepSynced(true);
        mUsersDatabase = FirebaseDatabase.getInstance().getReference().child("User");
        mUsersDatabase.keepSynced(true);

        // Inflate the layout for this fragment
        mFriendList.setHasFixedSize(true);
        mFriendList.setLayoutManager(new LinearLayoutManager(getContext()));

        return mMainView;
    }

    @Override
    public void onStart() {
        super.onStart();

        final FirebaseRecyclerAdapter<Friends, FriendsViewHolder> friendsRecyclerViewAdapter = new FirebaseRecyclerAdapter<Friends, FriendsViewHolder>(
                Friends.class,
                R.layout.users_single_layout,
                FriendsViewHolder.class,
                mFriendsDatabase

        ) {
    @Override
    protected void populateViewHolder(final FriendsViewHolder viewHolder, final Friends model, int position) {

            viewHolder.setDate(model.getDate());

            final String list_user_id = getRef(position).getKey();

            mUsersDatabase.child(list_user_id).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    final String userName = dataSnapshot.child("name").getValue().toString();
                    final String userThumb = dataSnapshot.child("thumb_image").getValue().toString();

                    if (dataSnapshot.hasChild("online")){
                        String userOnline =  dataSnapshot.child("online").getValue().toString();
                        viewHolder.setUserOnline(userOnline);
                    }
                    viewHolder.setName(userName);
                    viewHolder.setUserThumbImage(userThumb,getContext());

                    viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            CharSequence options[] = new CharSequence[]{"Open Profile","Send message"};

                            final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                            builder.setTitle("Select Options");
                            builder.setItems(options, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                    switch (i){
                                        case 0:
                                            Intent profile_intent = new Intent(getContext(),ProfileActivity.class);
                                            profile_intent.putExtra("user_id",list_user_id);
                                            startActivity(profile_intent);
                                        break;
                                        case 1:
                                            Intent chat_intent = new Intent(getContext(),ChatActivity.class);
                                            chat_intent.putExtra("user_id",list_user_id);
                                            chat_intent.putExtra("user_name",userName);
                                            chat_intent.putExtra("user_image",userThumb);
                                            startActivity(chat_intent);
                                        break;
                                        default:
                                        return;

                                    }
                                }
                            });
                            builder.show();
                        }
                    });

                }
                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            }
        };
        mFriendList.setAdapter(friendsRecyclerViewAdapter);
    }

    public static class FriendsViewHolder extends RecyclerView.ViewHolder{

        View mView;

        public FriendsViewHolder(View itemView) {
            super(itemView);

            mView = itemView;
        }

        public void setDate(String date){
            TextView userDate = (TextView) mView.findViewById(R.id.single_user_status);
            userDate.setText(date);

        }

        public void setName(String name){
            TextView userNameView = (TextView) mView.findViewById(R.id.single_usre_name);
            userNameView.setText(name);
        }

        public  void setUserThumbImage(String thumbImage, Context context){

            CircleImageView userImageView = (CircleImageView) mView.findViewById(R.id.single_user_image);
            Picasso.with(context).load(thumbImage).placeholder(R.drawable.default_image).into(userImageView);

        }

        public void setUserOnline(String online_status){
            CircleImageView userOnlineView = (CircleImageView) mView.findViewById(R.id.single_user_online_icon);

            if(online_status.equals("true")){
                userOnlineView.setVisibility(View.VISIBLE);
            }
            else{
                userOnlineView.setVisibility(View.INVISIBLE);
            }
        }
    }
}
