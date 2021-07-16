package com.shakilsustswe.locationtracker;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class Friendlist extends RecyclerView.Adapter<UserAdapter.MyViewHolder> {
    Context mainActivity;
    ArrayList<Users> usersArrayList;

    public void UserAdapter(FindFriendActivity mainActivity, ArrayList<Users> usersArrayList) {
        this.mainActivity = mainActivity;
        this.usersArrayList = usersArrayList;
    }

    @NonNull
    @Override
    public UserAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mainActivity).inflate(R.layout.activity_user_adapter, parent, false);///add user adapter
        return new UserAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserAdapter.MyViewHolder holder, int position) {

        Users users = usersArrayList.get(position);
        holder.userName.setText(users.name);
        holder.userStatus.setText(users.status);
        /// Glide.with(mainActivity).load(users.getImageUri()).into(holder.userImage);
        Picasso.get().load(users.imageUri).into(holder.userImage);

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

    class MyViewHolder extends RecyclerView.ViewHolder {
        CircleImageView userImage;
        //        TextView userName, userStatus;
        // ImageView userImage;
        TextView userName;
        TextView userStatus;
        ImageButton imageButtong;

        public MyViewHolder(@NonNull View itemView) {

            super(itemView);
            userImage = itemView.findViewById(R.id.userImageId);
            userName = itemView.findViewById(R.id.usernameId);
            userStatus = itemView.findViewById(R.id.statusId);

        }
    }
}
