package com.suffix.fieldforce.activity.chat;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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
  private String currentUser;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_message);
    ButterKnife.bind(this);

    preferences = new FieldForcePreferences(this);
    currentUser = preferences.getUser().getUserName();

    if (getIntent().hasExtra("EMPLOYEE_NAME")) {
      txtUserName.setText(getIntent().getStringExtra("EMPLOYEE_NAME"));
    }

    if (getIntent().hasExtra("EMPLOYEE_IMAGE")) {
      uri = Uri.parse(getIntent().getStringExtra("EMPLOYEE_IMAGE"));
      imgProfilePicture.setImageURI(uri);
    }

    recyclerViewChat.setHasFixedSize(true);
    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
    linearLayoutManager.setStackFromEnd(true);
    recyclerViewChat.setLayoutManager(linearLayoutManager);

    imgMessageSend.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {

        if (!txtMessageInput.getText().toString().equals("")) {
          sendMessage(currentUser, getIntent().getStringExtra("EMPLOYEE_NAME"), txtMessageInput.getText().toString());
          txtMessageInput.setText("");
        } else {
          Toast.makeText(MessageActivity.this, "message can not be empty", Toast.LENGTH_SHORT).show();
        }
      }
    });
    readMessage(currentUser, getIntent().getStringExtra("EMPLOYEE_NAME"));
  }

  public void sendMessage(String sender, String receiver, String message) {

    ref = FirebaseDatabase.getInstance().getReference();

    HashMap<String, Object> hashMap = new HashMap<>();
    hashMap.put("sender", sender);
    hashMap.put("receiver", receiver);
    hashMap.put("message", message);

    ref.child("chats").push().setValue(hashMap);
    readMessage(sender, receiver);
  }

  public void readMessage(final String sender, final String receiver) {
    mchats = new ArrayList<>();

    ref = FirebaseDatabase.getInstance().getReference("chats");

    ref.addValueEventListener(new ValueEventListener() {
      @Override
      public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        mchats.clear();
        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
          Chats chats = snapshot.getValue(Chats.class);
          if ((chats.getSender().equals(sender) && chats.getReceiver().equals(receiver)) || (chats.getSender().equals(receiver) && chats.getReceiver().equals(sender))) {
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
