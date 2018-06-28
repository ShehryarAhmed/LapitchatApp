package com.logic.tech.chatapp;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder>{

    private List<Message> mMessageList;
    private FirebaseAuth mAuth;

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
    public void onBindViewHolder(MessageViewHolder holder, int position) {

        Message message = mMessageList.get(position);

        String  mCurrentUserID = mAuth.getCurrentUser().getUid();
        String from_user = message.getFrom();

        if (from_user.equals(mCurrentUserID)){

            holder.messageTextView.setBackgroundColor(Color.WHITE);
            holder.messageTextView.setTextColor(Color.BLACK);
            holder.circleImageView.setVisibility(View.INVISIBLE);

        }else{

            holder.messageTextView.setBackgroundResource(R.drawable.message_text_background);
            holder.messageTextView.setTextColor(Color.WHITE);
        }

        holder.messageTextView.setText(message.getMessage());
    }

    @Override
    public int getItemCount() {
        return mMessageList.size();
    }

    public class MessageViewHolder extends RecyclerView.ViewHolder {

        TextView messageTextView;
        CircleImageView circleImageView;
        public MessageViewHolder(View itemView) {
            super(itemView);

            messageTextView = (TextView) itemView.findViewById(R.id.message_textview);
            circleImageView = (CircleImageView) itemView.findViewById(R.id.circleImageView);

        }
    }
}
