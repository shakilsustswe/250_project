package com.shakilsustswe.locationtracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class FriendsAdapter extends RecyclerView.Adapter<FriendsAdapter.MyViewHolder> {
    Context mainActivity;
    ArrayList<Users> usersArrayList;

    public FriendsAdapter(MyfriendList mainActivity, ArrayList<Users> usersArrayList) {
        this.mainActivity = mainActivity;
        this.usersArrayList = usersArrayList;
    }

    @NonNull
    @Override
    public FriendsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mainActivity).inflate(R.layout.activity_friends_adapter, parent, false);///add user adapter
        return new FriendsAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FriendsAdapter.MyViewHolder holder, int position) {


        Users users = usersArrayList.get(position);

        holder.userName.setText(users.name);
        holder.userStatus.setText(users.status);
        /// Glide.with(mainActivity).load(users.getImageUri()).into(holder.userImage);
        Picasso.get().load(users.imageUri).into(holder.userImage);
        ///add item OnClickListener
        /*holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mainActivity, MessageActivity.class);
                intent.putExtra("reciverName", users.getName());
                intent.putExtra("reciverImage", users.getImageUri());
                intent.putExtra("reciverUid", users.getUid());
                mainActivity.startActivity(intent);
            }
        });*/

    }

    @Override
    public int getItemCount() {
        return usersArrayList.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        CircleImageView userImage;
        TextView userName;
        TextView userStatus;
        ImageButton imageButtong;

        public MyViewHolder(@NonNull View itemView) {

            super(itemView);
            userImage = itemView.findViewById(R.id.friendImageId);
            userName = itemView.findViewById(R.id.friendnameId);
            userStatus = itemView.findViewById(R.id.friendstatusId);

        }
    }
}