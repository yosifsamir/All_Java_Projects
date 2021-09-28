package com.example.mychat.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mychat.R;

public class MessageViewHolder extends RecyclerView.ViewHolder {
    public TextView senderTxt;
    public TextView messageTxt;

    public MessageViewHolder(@NonNull View itemView) {
        super(itemView);
        senderTxt=itemView.findViewById(R.id.sender_txt);
        messageTxt=itemView.findViewById(R.id.message_txt);

    }
}
