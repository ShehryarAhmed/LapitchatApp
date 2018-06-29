package com.logic.tech.chatapp;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder>{

    private List<Message> mMessageList;
    private FirebaseAuth mAuth;
    private DatabaseReference mUserDatabaseRef;

    public MessageAdapter(List<Message> mMessageList){
        this.mMessageList = mMessageList;
    }

    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.message_single_layout, parent, false);
        return new MessageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final MessageViewHolder holder, int position) {

        Message message = mMessageList.get(position);

        mAuth = FirebaseAuth.getInstance();

        String  mCurrentUserID = mAuth.getCurrentUser().getUid();

        String from_user = message.getFrom();
        String message_type = message.getType();

        mUserDatabaseRef = FirebaseDatabase.getInstance().getReference().child("Users").child(from_user);

        mUserDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String name = dataSnapshot.child("name").getValue().toString();
                String image = dataSnapshot.child("thumb_image").getValue().toString();

                holder.single_usre_name.setText(name);
                Picasso.with(holder.circleImageView.getContext()).load(image)
                        .placeholder(R.drawable.default_image).into(holder.circleImageView);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        if(message_type.equals("text")){
            holder.messageTextView.setText(message.getMessage());
            holder.messageImage.setVisibility(View.INVISIBLE);
        }else {
            holder.messageTextView.setVisibility(View.INVISIBLE);
            Picasso.with(holder.circleImageView.getContext()).load(message.getMessage())
                    .placeholder(R.drawable.default_image).into(holder.messageImage);

        }
//
//        if (from_user.equals(mCurrentUserID)){
//
//            holder.currentMessageTextView.setBackgroundColor(Color.WHITE);
//            holder.currentMessageTextView.setTextColor(Color.BLACK);
////            holder.circleImageView.setVisibility(View.INVISIBLE);
//            holder.currentMessageTextView.setVisibility(View.VISIBLE);
//            holder.currentMessageTextView.setText(message.getMessage());
//
//        }else{
//
//            holder.messageTextView.setBackgroundResource(R.drawable.message_text_background);
//            holder.messageTextView.setTextColor(Color.WHITE);
//            holder.messageTextView.setVisibility(View.VISIBLE);
//            holder.messageTextView.setText(message.getMessage());
//
//
//        }

//        holder.messageTextView.setText(message.getMessage());
    }

    @Override
    public int getItemCount() {
        return mMessageList.size();
    }

    public class MessageViewHolder extends RecyclerView.ViewHolder {

        TextView messageTextView;
        TextView single_usre_name;
        TextView currentMessageTextView;
        CircleImageView circleImageView;
        public ImageView messageImage;
        public MessageViewHolder(View itemView) {
            super(itemView);

            single_usre_name = (TextView) itemView.findViewById(R.id.single_usre_name);
            messageTextView = (TextView) itemView.findViewById(R.id.message_textview);
            currentMessageTextView = (TextView) itemView.findViewById(R.id.current_message_textview);
            messageImage= (ImageView) itemView.findViewById(R.id.chat_message);
            circleImageView = (CircleImageView) itemView.findViewById(R.id.circleImageView);

        }
    }
}
