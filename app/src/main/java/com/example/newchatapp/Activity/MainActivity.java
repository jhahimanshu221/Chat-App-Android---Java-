package com.example.newchatapp.Activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newchatapp.R;
import com.example.newchatapp.ModelClass.User;
import com.example.newchatapp.Adapter.UserAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private RecyclerView main_recycler;
    private UserAdapter adapter;
    private FirebaseDatabase database;
    private ArrayList<User> usersArrayList;
    private ImageView img_logOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize FirebaseAuth instance
        auth = FirebaseAuth.getInstance();

        // Initialize FirebaseDatabase instance
        database = FirebaseDatabase.getInstance();

        // Initialize RecyclerView and ArrayList
        main_recycler = findViewById(R.id.main_recycler);
        usersArrayList = new ArrayList<>();

        // Initialize UserAdapter and set it to RecyclerView
        adapter = new UserAdapter(MainActivity.this, usersArrayList);
        main_recycler.setLayoutManager(new LinearLayoutManager(this));
        main_recycler.setAdapter(adapter);

        // Initialize ImageView for logout button
        img_logOut = findViewById(R.id.img_logOut);

        // Load users data
        loadUsers();

        // Set onClick listener for logout button
        img_logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logoutUser();
            }
        });
    }

    // Method to load users data
    private void loadUsers() {
        DatabaseReference reference = database.getReference().child("user");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                usersArrayList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    User user = dataSnapshot.getValue(User.class);
                    usersArrayList.add(user);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
            }
        });
    }

    // Method to logout user
    private void logoutUser() {
        Dialog dialog = new Dialog(MainActivity.this, R.style.Dialogue);
        dialog.setContentView(R.layout.dialog_layout);

        Button yes_btn, no_btn;
        yes_btn = dialog.findViewById(R.id.yes_btn);
        no_btn = dialog.findViewById(R.id.no_btn);

        yes_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(MainActivity.this, SignupActivity.class));
                finish(); // Finish MainActivity after logout
            }
        });

        no_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }
}