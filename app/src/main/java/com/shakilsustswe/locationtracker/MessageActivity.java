package com.shakilsustswe.locationtracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageActivity extends AppCompatActivity {

    String reciverName, reciverImage, reciverUid, senderUid;
    CircleImageView ReciverImage;
    TextView ReciverName;
    FirebaseDatabase database;
    FirebaseAuth firebaseAuth;
    public static String sImage;
    public static String rImage;

    ///CardView sendBtn,editcardView;
    EditText edtMessage;
    ImageView sendBtn;
    String senderRoom, reciverRoom;

    RecyclerView messageAdater;
    ArrayList<Messages> messagesArrayList;
    MessageAdapter adater;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        database = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        ////////to add image and user name in Message activity

        reciverName = getIntent().getStringExtra("reciverName");
        reciverImage = getIntent().getStringExtra("reciverImage");
        reciverUid = getIntent().getStringExtra("reciverUid");

        messagesArrayList = new ArrayList<>();

        ReciverImage = findViewById(R.id.profile_image);
        ReciverName = findViewById(R.id.reciverName);

        messageAdater = findViewById(R.id.messageAdater);
        adater = new MessageAdapter(MessageActivity.this, messagesArrayList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        messageAdater.setLayoutManager(linearLayoutManager);
        messageAdater.setAdapter(adater);


        sendBtn=findViewById(R.id.sentBtn);
        edtMessage=findViewById(R.id.edtMessage);

        Picasso.get().load(reciverImage).into(ReciverImage);
        ReciverName.setText("" + reciverName);

        senderUid= firebaseAuth.getUid();

        senderRoom = senderUid + reciverUid;
        reciverRoom = reciverUid + senderUid;

        DatabaseReference reference = database.getReference().child("FriendsList").child(firebaseAuth.getUid());
        DatabaseReference chatRefrece = database.getReference().child("chats").child(senderRoom).child("messages");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try{
                    sImage = snapshot.child("imageUri").getValue().toString();
                    rImage = reciverImage;
                }
                catch (Exception e){

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        chatRefrece.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                messagesArrayList.clear();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    Messages messages = dataSnapshot.getValue(Messages.class);
                    messagesArrayList.add(messages);
                }
                adater.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = edtMessage.getText().toString();
                if (message.isEmpty()) {
                    Toast.makeText(MessageActivity.this, "Please enter Valid Message", Toast.LENGTH_SHORT).show();
                    // return;
                }
                edtMessage.setText("");
                Date date = new Date();

                Messages messages = new Messages(senderUid, message, date.getTime());

                database.getReference().child("chats")
                        .child(senderRoom)
                        .child("messages")
                        .push()
                        .setValue(messages).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        ///Messages  messages = new Messages(message, date.getTime(),reciverUid);
                        database.getReference().child("chats")
                                .child(reciverRoom)
                                .child("messages")
                                .push().setValue(messages).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                            }
                        });
                    }
                });

            }
        });

    }
}