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
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class EmergencyHelpAdapter extends RecyclerView.Adapter<EmergencyHelpAdapter.MyViewHolder> {
    Context mainActivity;
    ArrayList<LocationHelper> usersArrayList;
    onAdapterInteractionListener onAdapterInteractionListener;
    public void setOnAdapterInteractionListener(EmergencyHelpAdapter.onAdapterInteractionListener onAdapterInteractionListener) {
        this.onAdapterInteractionListener = onAdapterInteractionListener;
    }

    public EmergencyHelpAdapter(FragmentActivity mainActivity, ArrayList<LocationHelper> usersArrayList) {
        this.mainActivity = mainActivity;
        this.usersArrayList = usersArrayList;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mainActivity).inflate(R.layout.emergency_help, parent, false);///add user adapter
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        LocationHelper users = usersArrayList.get(position);
        holder.userName.setText(users.getName());
        holder.userStatus.setText(users.getStatus());
        Picasso.get().load(users.getImageUri()).into(holder.userImage);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAdapterInteractionListener.onItemClick(users);
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


        public MyViewHolder(@NonNull View itemView) {

            super(itemView);
            userImage = itemView.findViewById(R.id.emergency_help_user_image_id);
            userName = itemView.findViewById(R.id.emergency_help_user_name_id);
            userStatus = itemView.findViewById(R.id.emergency_help_user_status_id);


        }
    }
    public interface onAdapterInteractionListener{
        void onItemClick(LocationHelper helper);
    }
}
