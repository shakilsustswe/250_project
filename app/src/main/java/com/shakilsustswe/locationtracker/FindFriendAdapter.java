package com.shakilsustswe.locationtracker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;

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
    onAdapterInteractionListener onAdapterInteractionListener;


    // method writer fahim, 21/july/2021
    // inteact with find friend activity class
    public void setOnAdapterInteractionListener(FindFriendAdapter.onAdapterInteractionListener onAdapterInteractionListener) {
        this.onAdapterInteractionListener = onAdapterInteractionListener;
    }

    @Override
   protected void onBindViewHolder(@NonNull MyViewHolder holder, int position,@NonNull Users Users) {



        holder.userName.setText(Users.getName());
        holder.userStatus.setText(Users.getStatus());
        ///Glide.with(mainActivity).load(users.getImageUri()).into(holder.userImage);
        Picasso.get().load(Users.getImageUri()).into(holder.userImage);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAdapterInteractionListener.onItemClick(Users.getUid(), Users.getName(),Users.getImageUri(),Users.getStatus(),Users.getEmail());
            }
        });


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
        Switch aSwitch;

        public MyViewHolder(@NonNull View itemView) {

            super(itemView);

            userImage = itemView.findViewById(R.id.userImageId);
            userName = itemView.findViewById(R.id.emergency_help_user_name);
            userStatus = itemView.findViewById(R.id.statusId);
            aSwitch = itemView.findViewById(R.id.activity_user_apdater_location_switch);
            aSwitch.setVisibility(View.INVISIBLE);

        }
        public int getItemCount() {
            return usersArrayList.size();
        }
    }


    // interface, writer fahim
    public interface onAdapterInteractionListener{
        void onItemClick(String uid, String name,String imageUri,String status,String email);

       /// void onItemClick();
    }



}
