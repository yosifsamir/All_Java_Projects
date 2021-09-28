package com.example.mychat.ViewHolder;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mychat.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class RoomViewHolder extends RecyclerView.ViewHolder {
    public CircleImageView roomImageView;
    public TextView roomNameTxt;
    public Button joinBtn;

    public RoomViewHolder(@NonNull View itemView) {
        super(itemView);
        roomImageView=itemView.findViewById(R.id.image_room);
        roomNameTxt=itemView.findViewById(R.id.room_txt);
        joinBtn=itemView.findViewById(R.id.join_btn);

    }
}
