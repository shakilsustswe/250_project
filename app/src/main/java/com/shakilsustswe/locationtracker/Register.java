package com.shakilsustswe.locationtracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import de.hdodenhof.circleimageview.CircleImageView;

public class Register extends AppCompatActivity {

    EditText username, email, password, confermpassword;
    Button signUpButton;
    CircleImageView userImage;
    TextView signIn;
    FirebaseAuth auth;
    Uri imageUri;
    String imageURI;
    String person_name, person_email, person_password, conferm_password, status;
    FirebaseDatabase firebaseDatabase;
    FirebaseStorage firebaseStorage;
    DatabaseReference databaseReference;
    FirebaseUser firebaseUser;
    StorageReference storageReference;
    private static int RESULT_LOAD_IMAGE = 1;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // hiding action bar
        if(getSupportActionBar() != null){
            getSupportActionBar().hide();
        }

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Registering...");
        progressDialog.setMessage("Please wait😴😴");
        progressDialog.setCancelable(false);

        username = findViewById(R.id.emergency_help_user_name);
        email = findViewById(R.id.emailId);
        password = findViewById(R.id.passowrdId);
        confermpassword = findViewById(R.id.confermpasswordId);
        signUpButton = findViewById(R.id.shearlocation_share);
        signIn = findViewById(R.id.signInTextId);
        userImage = findViewById(R.id.profile_imageId);

        auth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();

        auth = FirebaseAuth.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        /////////check if user is null
        if (firebaseUser != null) {
            Intent intent = new Intent(Register.this, HomePage.class);
            startActivity(intent);
            finish();
        }

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Register.this,Login.class));
            }
        });
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                person_name = username.getText().toString();
                person_email = email.getText().toString();
                person_password = password.getText().toString();
                conferm_password = confermpassword.getText().toString();
                status = "Hey!i'm using this application";
                if (TextUtils.isEmpty(person_name) || TextUtils.isEmpty(person_email) || TextUtils.isEmpty(person_password)) {
                    progressDialog.dismiss();
                    Toast.makeText(Register.this, "All fill doesn't fill up", Toast.LENGTH_SHORT).show();
                } else if (person_password.length() < 6) {
                    progressDialog.dismiss();
                    Toast.makeText(Register.this, "Password must be at least 6 charecters", Toast.LENGTH_SHORT).show();
                } else if (!conferm_password.equals(person_password)) {
                    progressDialog.dismiss();
                    Toast.makeText(Register.this, "Conferm password not matching!", Toast.LENGTH_SHORT).show();
                } else {
                    register(person_name, person_email, person_password, conferm_password);
                }
            }
        });

        userImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, RESULT_LOAD_IMAGE);
                /*Intent intent =new Intent();
                intent.setType("image");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Selecte picture"),10);*/
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            /*Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
           userImage.setImageBitmap(BitmapFactory.decodeFile(picturePath));*/
            imageUri = data.getData();
            userImage.setImageURI(imageUri);
        }
       /*if(requestCode==10)
        {
            if(data!=null)
            {
                imageUri = data.getData();
                userImage.setImageURI(imageUri);
            }
        }*/

    }

    private void register(final String username, String email, String password, String conferm_password) {
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            Toast.makeText(Register.this, "success", Toast.LENGTH_SHORT).show();

                            FirebaseUser firebaseUser = auth.getCurrentUser();

                            //assert firebaseUser != null;
                            String userid = firebaseUser.getUid();

                            databaseReference = firebaseDatabase.getReference("User").child(userid);
                            storageReference = firebaseStorage.getReference().child("upload").child(userid);

                            if (imageUri != null) {
                                Toast.makeText(Register.this, "Image", Toast.LENGTH_SHORT).show();
                                storageReference.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                        if (task.isSuccessful()) {
                                            storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                @Override
                                                public void onSuccess(Uri uri) {
                                                    imageURI = uri.toString();
                                                    Users users = new Users(userid, person_name, person_email, imageURI, status);
                                                    databaseReference.setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()) {
                                                                progressDialog.dismiss();
                                                                startActivity(new Intent(Register.this, HomePage.class));
                                                            } else {
                                                                progressDialog.dismiss();
                                                                Toast.makeText(Register.this, "Error in creating a new user account", Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                    });
                                                }
                                            });
                                        }
                                    }
                                });
                            } else {
                                imageURI = "https://firebasestorage.googleapis.com/v0/b/chatting-app-916fb.appspot.com/o/iconfinder_user_1608727.png?alt=media&token=60700cb7-dfd2-4f19-958b-41e7bc17231d";
                                Users users = new Users(auth.getUid(), person_name, person_email, imageURI, status);
                                databaseReference.setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            progressDialog.dismiss();
                                            startActivity(new Intent(Register.this,HomePage.class));
                                        } else {
                                            progressDialog.dismiss();
                                            Toast.makeText(Register.this, "Error in creating a new user account", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });

                            }

                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(Register.this, "You can't register worth this email or password", Toast.LENGTH_SHORT).show();
                        }
                    }

                });

    }
}