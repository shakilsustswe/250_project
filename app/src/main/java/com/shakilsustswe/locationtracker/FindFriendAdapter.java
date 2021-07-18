package com.shakilsustswe.locationtracker;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class FindFriendAdapter extends FirebaseRecyclerAdapter<Users,FindFriendAdapter.MyViewHolder> {


    public FindFriendAdapter(@NonNull FirebaseRecyclerOptions<Users> options) {
        super(options);
    }
    FindFriendActivity findFriendActivity;

    Context mainActivity;
    ArrayList<Users> usersArrayList;

    @Override
   protected void onBindViewHolder(@NonNull MyViewHolder holder, int position,@NonNull Users Users) {


        //Users users = usersArrayList.get(position);
       /* holder.userName.setText(users.name);
        holder.userStatus.setText(users.status);
        ///Glide.with(mainActivity).load(users.getImageUri()).into(holder.userImage);
        Picasso.get().load(users.imageUri).into(holder.userImage);*/

        holder.userName.setText(Users.getName());
        holder.userStatus.setText(Users.getStatus());
        ///Glide.with(mainActivity).load(users.getImageUri()).into(holder.userImage);
        Picasso.get().load(Users.getImageUri()).into(holder.userImage);

        /*holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){

                Intent intent = new Intent(FindFriendAdapter.this,ViewFriendActivity.class);
                intent.putExtra("userKey",Users.getUid());
                findFriendActivity.startActivity(intent);
            }
        });*/

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_user_adapter, parent, false);///add user adapter in findfriendactivity
        return new MyViewHolder(view);
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        CircleImageView userImage;
        TextView userName;
        TextView userStatus;
        ///ImageButton imageButtong;

        public MyViewHolder(@NonNull View itemView) {

            super(itemView);

            userImage = itemView.findViewById(R.id.userImageId);
            userName = itemView.findViewById(R.id.usernameId);
            userStatus = itemView.findViewById(R.id.statusId);
           /// itemView.setOnClickListener((View.OnClickListener) this);
            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                Toast.makeText(v.getContext(), "position"+position, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(v.getContext(),ViewFriendActivity.class);
                v.getContext().startActivity(intent);
            });

        }
        
        public int getItemCount() {
            return usersArrayList.size();
        }
        
    }

   /* Context mainActivity;
    ArrayList<Users> usersArrayList;

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mainActivity).inflate(R.layout.activity_user_adapter, parent, false);///add user adapter in findfriendactivity
        return new FindFriendAdapter.MyViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position,@NonNull Users Users) {


        Users users = usersArrayList.get(position);
        holder.userName.setText(users.name);
        holder.userStatus.setText(users.status);
        /// Glide.with(mainActivity).load(users.getImageUri()).into(holder.userImage);
        Picasso.get().load(users.imageUri).into(holder.userImage);


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

            userImage = itemView.findViewById(R.id.userImageId);
            userName = itemView.findViewById(R.id.usernameId);
            userStatus = itemView.findViewById(R.id.statusId);

        }
    }*/
}
