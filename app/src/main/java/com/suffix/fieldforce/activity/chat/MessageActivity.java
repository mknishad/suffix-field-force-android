package com.suffix.fieldforce.activity.chat;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.android.material.appbar.AppBarLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.suffix.fieldforce.R;
import com.suffix.fieldforce.adapter.MessageAdapter;
import com.suffix.fieldforce.model.Chats;
import com.suffix.fieldforce.model.User;
import com.suffix.fieldforce.preference.FieldForcePreferences;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MessageActivity extends AppCompatActivity {

  @BindView(R.id.txtUserName)
  TextView txtUserName;

  @BindView(R.id.txtOnlineStatus)
  TextView txtOnlineStatus;

  @BindView(R.id.toolbar)
  Toolbar toolbar;

  @BindView(R.id.appbar)
  AppBarLayout appbar;

  @BindView(R.id.recyclerViewChat)
  RecyclerView recyclerViewChat;

  @BindView(R.id.imgAttachment)
  ImageView imgAttachment;

  @BindView(R.id.txtMessageInput)
  EditText txtMessageInput;

  @BindView(R.id.imgMessageSend)
  ImageView imgMessageSend;

  @BindView(R.id.imgProfilePicture)
  SimpleDraweeView imgProfilePicture;

  private Uri uri;
  private DatabaseReference ref;
  private List<Chats> mchats;
  private MessageAdapter messageAdapter;
  private FieldForcePreferences preferences;
  private User currentUser;
  private String receiverId;
  private String receiverName;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_message);
    ButterKnife.bind(this);

    preferences = new FieldForcePreferences(this);
    currentUser = preferences.getUser();

    if (getIntent().hasExtra("EMPLOYEE_NAME")) {
      receiverName = getIntent().getStringExtra("EMPLOYEE_NAME");
      txtUserName.setText(receiverName);
    }

    if (getIntent().hasExtra("EMPLOYEE_IMAGE")) {
      uri = Uri.parse(getIntent().getStringExtra("EMPLOYEE_IMAGE"));
      imgProfilePicture.setImageURI(uri);
    }

    if (getIntent().hasExtra("EMPLOYEE_ID")) {
      receiverId = getIntent().getStringExtra("EMPLOYEE_ID");
    }

    recyclerViewChat.setHasFixedSize(true);
    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
    linearLayoutManager.setStackFromEnd(true);
    recyclerViewChat.setLayoutManager(linearLayoutManager);

    imgMessageSend.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {

        if (!txtMessageInput.getText().toString().equals("")) {
          sendMessage(txtMessageInput.getText().toString());
          txtMessageInput.setText("");
        } else {
          Toast.makeText(MessageActivity.this, "message can not be empty", Toast.LENGTH_SHORT).show();
        }
      }
    });
    readMessage();
  }

  public void sendMessage(String message) {

    ref = FirebaseDatabase.getInstance().getReference();

    HashMap<String, Object> hashMap = new HashMap<>();
    hashMap.put("sender_id", currentUser.getUserId());
    hashMap.put("sender", currentUser.getUserName());
    hashMap.put("receiver_id", receiverId);
    hashMap.put("receiver", receiverName);
    hashMap.put("message", message);

    ref.child("chats").push().setValue(hashMap);
    readMessage();
  }

  public void readMessage() {
    mchats = new ArrayList<>();

    ref = FirebaseDatabase.getInstance().getReference("chats");

    ref.addValueEventListener(new ValueEventListener() {
      @Override
      public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        mchats.clear();
        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
          Chats chats = snapshot.getValue(Chats.class);
          if ((chats.getSender_id().equals(currentUser.getUserId()) && chats.getReceiver_id().equals(receiverId))
              || (chats.getSender_id().equals(receiverId) && chats.getReceiver_id().equals(currentUser.getUserId()))) {
            mchats.add(chats);
          }
        }
        messageAdapter = new MessageAdapter(MessageActivity.this, mchats);
        recyclerViewChat.setAdapter(messageAdapter);
      }

      @Override
      public void onCancelled(@NonNull DatabaseError databaseError) {

      }
    });
  }
}
