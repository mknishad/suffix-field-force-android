package com.suffix.fieldforce.activity.chat;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.suffix.fieldforce.R;
import com.suffix.fieldforce.adapter.UserAdapter;
import com.suffix.fieldforce.model.User;
import com.suffix.fieldforce.preference.FieldForcePreferences;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UserListActivity extends AppCompatActivity {

  @BindView(R.id.recyclerViewUser)
  RecyclerView recyclerViewUser;

  private List<User> mUser;
  private FieldForcePreferences preferences;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_user_list);
    ButterKnife.bind(this);

    preferences = new FieldForcePreferences(this);

    recyclerViewUser.setHasFixedSize(true);
    recyclerViewUser.setLayoutManager( new LinearLayoutManager(this));

    mUser = new ArrayList<>();

    DatabaseReference mref = FirebaseDatabase.getInstance().getReference("users");
    mref.addValueEventListener(new ValueEventListener() {
      @Override
      public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        mUser.clear();
        for(DataSnapshot snapshot : dataSnapshot.getChildren()){
          User user = snapshot.getValue(User.class);
          if(!user.getUserPhone1().equals(preferences.getUser().getUserPhone1())) {
            mUser.add(user);
          }
        }

        UserAdapter adapter = new UserAdapter(UserListActivity.this,mUser);
        recyclerViewUser.setAdapter(adapter);
      }

      @Override
      public void onCancelled(@NonNull DatabaseError databaseError) {

      }
    });
  }
}
