package com.suffix.fieldforce.activity.chat;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.AppBarLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.suffix.fieldforce.R;
import com.suffix.fieldforce.adapter.MessageAdapter;
import com.suffix.fieldforce.model.Chats;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MessageActivity extends AppCompatActivity {

  @BindView(R.id.name)
  TextView name;
  @BindView(R.id.toolbar)
  Toolbar toolbar;
  @BindView(R.id.appbar)
  AppBarLayout appbar;
  @BindView(R.id.chats)
  RecyclerView recyclerView;
  @BindView(R.id.txt_send)
  EditText txtSend;
  @BindView(R.id.btn_send)
  ImageButton btnSend;

  private DatabaseReference ref;

  List<Chats> mchats;
  MessageAdapter messageAdapter;
  String currentUser = null;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_message);
    ButterKnife.bind(this);

    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);


    recyclerView = (RecyclerView)findViewById(R.id.chats);
    recyclerView.setHasFixedSize(true);
    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
    linearLayoutManager.setStackFromEnd(true);
    recyclerView.setLayoutManager(linearLayoutManager);

    name.setText(getIntent().getStringExtra("name"));

    btnSend.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {

        if (!txtSend.getText().toString().equals("")) {
          sendMessage(currentUser, getIntent().getStringExtra("name"), txtSend.getText().toString());
          txtSend.setText("");
        } else {
          Toast.makeText(MessageActivity.this, "message can not be empty", Toast.LENGTH_SHORT).show();

        }
      }
    });
    readMessage(currentUser, getIntent().getStringExtra("name"));
  }

  public void sendMessage(String sender, String receiver, String message) {

    ref = FirebaseDatabase.getInstance().getReference();

    HashMap<String, Object> hashMap = new HashMap<>();
    hashMap.put("sender", sender);
    hashMap.put("receiver", receiver);
    hashMap.put("message", message);

    ref.child("chats").push().setValue(hashMap);
    readMessage(sender,receiver);
  }
  public void readMessage(final String sender, final String receiver) {
    mchats = new ArrayList<>();

    ref = FirebaseDatabase.getInstance().getReference("chats");

    ref.addValueEventListener(new ValueEventListener() {
      @Override
      public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        mchats.clear();

        for(DataSnapshot snapshot : dataSnapshot.getChildren()){
          Chats chats = snapshot.getValue(Chats.class);
          if((chats.getSender().equals(sender)&&chats.getReceiver().equals(receiver))||(chats.getSender().equals(receiver)&&chats.getReceiver().equals(sender))){
            mchats.add(chats);
          }
        }

        messageAdapter = new MessageAdapter(MessageActivity.this,mchats);
        recyclerView.setAdapter(messageAdapter);
      }

      @Override
      public void onCancelled(@NonNull DatabaseError databaseError) {

      }
    });
  }
}
