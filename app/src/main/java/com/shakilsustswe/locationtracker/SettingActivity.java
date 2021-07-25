package com.shakilsustswe.locationtracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.Nullable;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingActivity extends AppCompatActivity {

    public EditText getSetting_name() {
        return setting_name;
    }

    public void setSetting_name(EditText setting_name) {
        this.setting_name = setting_name;
    }

    LinearLayout rootLayout;
    CircleImageView setting_image;
    EditText setting_name, setting_status;
    FirebaseAuth auth;
    FirebaseDatabase database;
    FirebaseStorage storage;
    Button save;
    Uri selctedImageUri;
    String email;
    ProgressDialog progressDialog;
    private static int RESULT_LOAD_IMAGE = 1;


    String finalImageUri;
    String user_name;
    String user_status;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);


        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();

        setting_image = findViewById(R.id.setting_image);
        setting_name = findViewById(R.id.setting_name);
        setting_status = findViewById(R.id.setting_status);
        save = findViewById(R.id.save);
        rootLayout = findViewById(R.id.activity_setting_root_layout);


        Snackbar snackbar = Snackbar
                .make(rootLayout, "Click an invent to cheagne or modify", Snackbar.LENGTH_LONG);
        snackbar.show();


        DatabaseReference reference = database.getReference().child("User").child(auth.getUid());
        StorageReference storageReference = storage.getReference().child("uplod").child(auth.getUid());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                email = snapshot.child("email").getValue().toString();
                String name = snapshot.child("name").getValue().toString();
                String status = snapshot.child("status").getValue().toString();
                String image = snapshot.child("imageUri").getValue().toString();

                setting_name.setText(name);
                setting_status.setText(status);
                Picasso.get().load(image).into(setting_image);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressDialog.show();



                String name = setting_name.getText().toString();
                String status = setting_status.getText().toString();

                if (selctedImageUri != null) {
                    ///Toast.makeText(SettingActivity.this, "hiiiiiii", Toast.LENGTH_SHORT).show();

                    storageReference.putFile(selctedImageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                            storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    finalImageUri = uri.toString();
                                    user_name = name;
                                    user_status = status;
                                    Users users = new Users(auth.getUid(), name, email, finalImageUri, status);

                                    reference.setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                updateLocationinfo();
                                                progressDialog.dismiss();
                                                Toast.makeText(SettingActivity.this, "Data Successfully Updated", Toast.LENGTH_SHORT).show();
                                                startActivity(new Intent(SettingActivity.this, HomePage.class));
                                            } else {
                                                progressDialog.dismiss();
                                                Toast.makeText(SettingActivity.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }
                            });
                        }
                    });
                } else {

                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            finalImageUri = uri.toString();
                            user_name = name;
                            user_status = status;
                            Users users = new Users(auth.getUid(), name, email, finalImageUri, status);

                            reference.setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        updateLocationinfo();
                                        progressDialog.dismiss();
                                        Toast.makeText(SettingActivity.this, "Data Successfully Updated", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(SettingActivity.this,HomePage.class));
                                    } else {
                                        progressDialog.dismiss();
                                        Toast.makeText(SettingActivity.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    });
                }

            }
        });

        setting_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 10);
            }
        });


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==10)
        {
            if(data!=null)
            {
                selctedImageUri =data.getData();
                setting_image.setImageURI(selctedImageUri);
            }
        }
    }

    private void updateLocationinfo(){
        Task<Void> firebaseDatabase1 = FirebaseDatabase.getInstance().getReference().child("Location").child(auth.getUid()).child("name").setValue(user_name);
        Task<Void> firebaseDatabase2 = FirebaseDatabase.getInstance().getReference().child("Location").child(auth.getUid()).child("imageUri").setValue(user_status);
        Task<Void> firebaseDatabase3 = FirebaseDatabase.getInstance().getReference().child("Location").child(auth.getUid()).child("satus").setValue(finalImageUri);
    }
}
