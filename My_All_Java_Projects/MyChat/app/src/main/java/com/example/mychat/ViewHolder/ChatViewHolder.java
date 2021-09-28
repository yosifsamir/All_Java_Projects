package com.example.mychat.ViewHolder;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mychat.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatViewHolder extends RecyclerView.ViewHolder {
    public CircleImageView chatImageView;
    public TextView chatNameTxt;
    public Button exitBtn;

    public ChatViewHolder(@NonNull View itemView) {
        super(itemView);
        chatImageView=itemView.findViewById(R.id.chat_image_room);
        chatNameTxt=itemView.findViewById(R.id.chat_room_txt);
        exitBtn=itemView.findViewById(R.id.chat_join_btn);

    }

}
