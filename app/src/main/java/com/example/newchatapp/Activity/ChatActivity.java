package com.example.newchatapp.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newchatapp.Adapter.MessagesAdapter;
import com.example.newchatapp.ModelClass.Messages;
import com.example.newchatapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;

public class ChatActivity extends AppCompatActivity {

    String ReciverUID,ReciverName,SenderUID,SenderName;
    TextView reciever_name;
    FirebaseDatabase database;
    FirebaseAuth firebaseAuth;
    CardView send_btn;
    EditText edit_message;

    String senderRoom,recieverRoom;

    RecyclerView messageAdapter;
    ArrayList<Messages> messagesArrayList;

    MessagesAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_chat);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        database= FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        ReciverName=getIntent().getStringExtra("name");
        ReciverUID=getIntent().getStringExtra("uid");


        messagesArrayList = new ArrayList<>();


        reciever_name = findViewById(R.id.reciever_name);
        edit_message = findViewById(R.id.edit_message);
        send_btn = findViewById(R.id.send_btn);

        messageAdapter = findViewById(R.id.messageAdapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        messageAdapter.setLayoutManager(linearLayoutManager);
        adapter= new MessagesAdapter(ChatActivity.this,messagesArrayList);
        messageAdapter.setAdapter(adapter);


        reciever_name.setText("" + ReciverName);

        SenderUID= firebaseAuth.getUid();



        senderRoom = SenderUID+ReciverUID;
        recieverRoom=ReciverUID+SenderUID;







        DatabaseReference reference = database.getReference().child("user").child(firebaseAuth.getUid());
        DatabaseReference chatReference = database.getReference().child("chats").child(senderRoom).child("messages");




        chatReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messagesArrayList.clear();
                for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                    Messages messages = dataSnapshot.getValue(Messages.class);
                    messagesArrayList.add(messages);
                }

                adapter.notifyDataSetChanged();




            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        send_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = edit_message.getText().toString();
                if (message.isEmpty()){
                    Toast.makeText(ChatActivity.this, "Please Enter Message", Toast.LENGTH_SHORT).show();
                    return;
                }
                edit_message.setText("");
                Date date = new Date();

                Messages messages = new Messages(message,SenderUID,date.getTime());

                database.getReference().child("chats")
                        .child(senderRoom)
                        .child("messages")
                        .push()
                        .setValue(messages).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                database.getReference().child("chats")
                                        .child(recieverRoom)
                                        .child("messages")
                                        .push().setValue(messages).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {

                                            }
                                        });
                            }
                        });


            }
        });
    }
}