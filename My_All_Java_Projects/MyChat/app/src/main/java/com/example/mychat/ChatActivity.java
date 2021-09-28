package com.example.mychat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mychat.Model.Message;
import com.example.mychat.ViewHolder.ChatViewHolder;
import com.example.mychat.ViewHolder.MessageViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;

public class ChatActivity extends AppCompatActivity {

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseUser firebaseUser;
    private String roomName;
    private String roomId;
    private RecyclerView recyclerView;
    private String userName;
    private FloatingActionButton floatingActionButton;
    private EditText editText;
    private MyAdapter myAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Intent intent=getIntent();
        if (intent==null){
            Toast.makeText(this, "somthing wrong", Toast.LENGTH_SHORT).show();
            return;
        }
        roomName=intent.getStringExtra("RoomName");
        roomId=intent.getStringExtra("RoomId");
        Toast.makeText(this, roomName, Toast.LENGTH_SHORT).show();

        recyclerView=findViewById(R.id.chat_recycler);

        floatingActionButton=findViewById(R.id.chatBtn);
        editText=findViewById(R.id.chatEdt);
        initFireBase();
        initRecycler();
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String chatMessage=editText.getText().toString();
                if (chatMessage==null){
                    return;
                }
                if (TextUtils.isEmpty(chatMessage)){
                    editText.requestFocus();
                    editText.setError("Enter your message");
                    return;
                }
                Message message=new Message();
                String messageId=databaseReference.push().getKey();
                message.setMessageId(messageId);
                message.setRoomId(roomId);
                message.setuId(FirebaseAuth.getInstance().getUid());
                message.setMessage(chatMessage);
                message.setCreated_at(new Date().toString());
                databaseReference.child(messageId).setValue(message);
                myAdapter.notifyItemInserted(myAdapter.getItemCount());
                editText.setText("");
            }
        });


    }

    private void initRecycler() {
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(ChatActivity.this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);

        Query query = FirebaseDatabase.getInstance()
                .getReference()
                .child("Messages").orderByChild("roomId").equalTo(roomId);

        FirebaseRecyclerOptions<Message> options =
                new FirebaseRecyclerOptions.Builder<Message>()
                        .setQuery(query, Message.class)
                        .build();

        myAdapter=new MyAdapter(options);
        recyclerView.setAdapter(myAdapter);
    }

    private void initFireBase() {
        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference("Messages");
        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
    }

    class MyAdapter extends FirebaseRecyclerAdapter<com.example.mychat.Model.Message, MessageViewHolder>{


        /**
         * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
         * {@link FirebaseRecyclerOptions} for configuration options.
         *
         * @param options
         */
        public MyAdapter(@NonNull FirebaseRecyclerOptions<com.example.mychat.Model.Message> options) {
            super(options);
        }

        @Override
        protected void onBindViewHolder(@NonNull final MessageViewHolder holder, int position, @NonNull final com.example.mychat.Model.Message model) {
            DatabaseReference databaseReference=firebaseDatabase.getReference("Users").child(model.getuId()).child("userName");
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String userName2=snapshot.getValue(String.class);
                    holder.senderTxt.setText(userName2);
                    if (model.getuId().equals(firebaseUser.getUid())){
                        holder.senderTxt.setTextColor(Color.RED);
                    }
                    else{
                        holder.senderTxt.setTextColor(Color.BLUE);

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            holder.messageTxt.setText(model.getMessage());


        }

        @NonNull
        @Override
        public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view1= LayoutInflater.from(ChatActivity.this).inflate(R.layout.message_layout,parent,false);
            return new MessageViewHolder(view1);
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
