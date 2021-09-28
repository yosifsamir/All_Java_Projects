package com.example.mychat.Fragment;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.mychat.ChatActivity;
import com.example.mychat.MainActivity;
import com.example.mychat.Model.Room;
import com.example.mychat.R;
import com.example.mychat.ViewHolder.ChatViewHolder;
import com.example.mychat.ViewHolder.RoomViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.syd.oden.circleprogressdialog.core.CircleProgressDialog;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChatFragment extends Fragment {

    private View view;
    private Context context;
    private RecyclerView recyclerView;
    private ChatFragment.MyAdapter myAdapter;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context=context;
    }

    public ChatFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_chat, container, false);
        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference("My_Rooms");
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        recyclerView=view.findViewById(R.id.chatRecyclerView);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);

        Query query = FirebaseDatabase.getInstance()
                .getReference()
                .child("My_Rooms").child(FirebaseAuth.getInstance().getUid());

        FirebaseRecyclerOptions<Room> options =
                new FirebaseRecyclerOptions.Builder<Room>()
                        .setQuery(query, Room.class)
                        .build();
        myAdapter=new MyAdapter(options);
        recyclerView.setAdapter(myAdapter);

    }

    class MyAdapter extends FirebaseRecyclerAdapter<Room, ChatViewHolder> {

        /**
         * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
         * {@link FirebaseRecyclerOptions} for configuration options.
         *
         * @param options
         */
        public MyAdapter(@NonNull FirebaseRecyclerOptions<Room> options) {
            super(options);
        }

        @Override
        protected void onBindViewHolder(@NonNull ChatViewHolder holder, final int position, @NonNull final Room model) {
            holder.chatNameTxt.setText(model.getName());
            Glide.with(context).load(model.getImgUrl()).into(holder.chatImageView);
            holder.exitBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    databaseReference.child(FirebaseAuth.getInstance().getUid()).child(model.getId()).removeValue(new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                            if (error!=null){
                                Toast.makeText(context, error.getMessage().toString(), Toast.LENGTH_SHORT).show();
                                return;
                            }
                            MyAdapter.super.notifyDataSetChanged();
                        }
                    });
                }
            });
            holder.chatNameTxt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent=new Intent(context, ChatActivity.class);
                    intent.putExtra("RoomName",model.getName());
                    intent.putExtra("RoomId",model.getId());
                    startActivity(intent);
                }
            });
        }

        @NonNull
        @Override
        public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view1=LayoutInflater.from(context).inflate(R.layout.chat_layout,parent,false);
            return new ChatViewHolder(view1);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        myAdapter.startListening();

    }

    @Override
    public void onStop() {
        super.onStop();
        myAdapter.stopListening();
    }
}
