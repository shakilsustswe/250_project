
package com.shakilsustswe.locationtracker;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.MyViewHolder> {
    Context mainActivity;
    ArrayList<Users> usersArrayList;


    onAdapterInteractionListener onAdapterInteractionListener;
    public void setOnAdapterInteractionListener(UserAdapter.onAdapterInteractionListener onAdapterInteractionListener) {
        this.onAdapterInteractionListener = onAdapterInteractionListener;
    }
    public UserAdapter(Context mainActivity, ArrayList<Users> usersArrayList) {
        this.mainActivity = mainActivity;
        this.usersArrayList = usersArrayList;
    }

    public UserAdapter(MainActivity mainActivity, ArrayList<Users> usersArrayList) {
        this.mainActivity = mainActivity;
        this.usersArrayList = usersArrayList;
    }

    public UserAdapter(Friendlist mainActivity, ArrayList<Users> usersArrayList) {
        this.mainActivity = mainActivity;
        this.usersArrayList = usersArrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mainActivity).inflate(R.layout.activity_user_adapter, parent, false);///add user adapter
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Users users = usersArrayList.get(position);
        holder.userName.setText(users.name);
        holder.userStatus.setText(users.status);
        /// Glide.with(mainActivity).load(users.getImageUri()).into(holder.userImage);
        Picasso.get().load(users.imageUri).into(holder.userImage);
        ///add item OnClickListener
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mainActivity, MessageActivity.class);
                intent.putExtra("reciverName", users.getName());
                intent.putExtra("reciverImage", users.getImageUri());
                intent.putExtra("reciverUid", users.getUid());
                mainActivity.startActivity(intent);
            }
        });

        holder.aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) onAdapterInteractionListener.onItemClick(users.getUid().toString());
                else{
                    Task<Void> ref = FirebaseDatabase.getInstance().getReference().child("GetSheareLocation").child(users.getUid()).child(FirebaseAuth.getInstance().getUid()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull @NotNull Task<Void> task) {

                        }
                    });
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return usersArrayList.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        CircleImageView userImage;
        //        TextView userName, userStatus;
        // ImageView userImage;
        TextView userName;
        TextView userStatus;
        ImageButton imageButtong;
        Switch aSwitch;

        public MyViewHolder(@NonNull View itemView) {

            super(itemView);
            userImage = itemView.findViewById(R.id.userImageId);
            userName = itemView.findViewById(R.id.emergency_help_user_name);
            userStatus = itemView.findViewById(R.id.statusId);
            aSwitch = itemView.findViewById(R.id.activity_user_apdater_location_switch);

        }
    }

    // interface, writer fahim
    public interface onAdapterInteractionListener{
        void onItemClick(String uid);
    }
}