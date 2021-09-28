package com.example.mychat.Fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.mychat.Model.MyRoom;
import com.example.mychat.Model.Room;
import com.example.mychat.R;
import com.example.mychat.RoomActivity;
import com.example.mychat.ViewHolder.RoomViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    private View view;
    private FloatingActionButton floatingActionButton;
    private Context context;
    private RecyclerView recyclerView;
    private MyAdapter myAdapter;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    long size=0;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context=context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_home, container, false);
        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference("My_Rooms");
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        floatingActionButton=view.findViewById(R.id.floating_action_button);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, RoomActivity.class));
            }
        });

        recyclerView=view.findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);


        recyclerView.addItemDecoration(new DividerItemDecoration(context,DividerItemDecoration.VERTICAL));
        Query query = FirebaseDatabase.getInstance()
                .getReference()
                .child("Rooms");


        FirebaseRecyclerOptions<Room> options =
                new FirebaseRecyclerOptions.Builder<Room>()
                        .setQuery(query, Room.class)
                        .build();
        myAdapter=new MyAdapter(options);
        Toast.makeText(context, size+"", Toast.LENGTH_SHORT).show();
        recyclerView.setAdapter(myAdapter);
//        recyclerView.smoothScrollToPosition(myAdapter.getItemCount() - 1);

    }

    class MyAdapter extends FirebaseRecyclerAdapter<Room,RoomViewHolder>{

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
        protected void onBindViewHolder(@NonNull RoomViewHolder holder, int position, @NonNull final Room model) {
            holder.roomNameTxt.setText(model.getName());
            Glide.with(context).load(model.getImgUrl()).into(holder.roomImageView);
            holder.joinBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String id=databaseReference.push().getKey();
//                    MyRoom myRoom=new MyRoom(FirebaseAuth.getInstance().getUid(),model.getId());
                    databaseReference.child(FirebaseAuth.getInstance().getUid()).child(model.getId()).setValue(model);
                }
            });
        }

        @NonNull
        @Override
        public RoomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view1=LayoutInflater.from(context).inflate(R.layout.room_layout,parent,false);
            return new RoomViewHolder(view1);
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
