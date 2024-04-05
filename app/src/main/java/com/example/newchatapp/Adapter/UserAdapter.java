package com.example.newchatapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newchatapp.Activity.ChatActivity;
import com.example.newchatapp.Activity.MainActivity;
import com.example.newchatapp.ModelClass.User;
import com.example.newchatapp.R;

import java.util.ArrayList;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {
     Context mainActivity;
     ArrayList<User> usersArrayList;


    public UserAdapter(MainActivity mainActivity, ArrayList<User> usersArrayList) {
        this.mainActivity = mainActivity;
        this.usersArrayList = usersArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mainActivity).inflate(R.layout.item_user_row,parent,false);
        return new ViewHolder(view);
    }



    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User users = usersArrayList.get(position);

        holder.user_name.setText(users.name);
        holder.status.setText(users.status);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mainActivity, ChatActivity.class);
                intent.putExtra("name",users.getName());
                intent.putExtra("uid",users.getUid());
                mainActivity.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return usersArrayList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView user_name;
        TextView status;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            user_name = itemView.findViewById(R.id.user_name);
            status = itemView.findViewById(R.id.status);
        }
    }
}
