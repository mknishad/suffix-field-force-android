package com.suffix.fieldforce.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.suffix.fieldforce.R;
import com.suffix.fieldforce.adapter.UserAdapter;
import com.suffix.fieldforce.model.ModelChatList;
import com.suffix.fieldforce.model.ModelUserList;
import com.suffix.fieldforce.model.User;
import com.suffix.fieldforce.preference.FieldForcePreferences;
import com.suffix.fieldforce.retrofitapi.APIInterface;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChatListFragment extends Fragment {

  @BindView(R.id.recyclerViewUser)
  RecyclerView recyclerViewUser;

  @BindView(R.id.progress)
  LottieAnimationView progress;

  private APIInterface apiInterface             = null;
  private DatabaseReference ref                 = null;
  private ValueEventListener seenListener       = null;
  private List<ModelUserList> modelUserLists    = null;
  private List<ModelChatList> modelChatLists    = null;
  private FieldForcePreferences preferences     = null;
  private UserAdapter adapter                   = null;
  private User currentUser                      = null;
  private String receiverId                     = null;
  private String receiverName                   = null;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {

    View view = inflater.inflate(R.layout.fragment_chat_list, container, false);
    ButterKnife.bind(this,view);

    preferences = new FieldForcePreferences(getContext());
    currentUser = preferences.getUser();
    modelUserLists = new ArrayList<>();
    modelChatLists = new ArrayList<>();

    recyclerViewUser.setHasFixedSize(true);
    recyclerViewUser.setLayoutManager(new LinearLayoutManager(getContext()));
    adapter = new UserAdapter(getContext(), modelUserLists);
    recyclerViewUser.setAdapter(adapter);

    ref = FirebaseDatabase.getInstance().getReference("Chatlist").child(currentUser.getUserId());
    ref.addValueEventListener(new ValueEventListener() {
      @Override
      public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        modelUserLists.clear();
        for(DataSnapshot snapshot : dataSnapshot.getChildren()){
          ModelUserList modelUserList = snapshot.getValue(ModelUserList.class);
          modelUserLists.add(modelUserList);
        }
        chatList();
      }

      @Override
      public void onCancelled(@NonNull DatabaseError databaseError) {

      }
    });

    return view;
  }

  private void chatList() {
    ref = FirebaseDatabase.getInstance().getReference("Users");
    ref.addValueEventListener(new ValueEventListener() {
      @Override
      public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        modelChatLists.clear();
        for(DataSnapshot snapshot : dataSnapshot.getChildren()){
          ModelChatList modelChatList = snapshot.getValue(ModelChatList.class);
          for(ModelUserList modelUserList : modelUserLists){
            if(modelUserList.getEmpOfficeId().equals(modelChatList.getId())){
              modelChatLists.add(modelChatList);
            }
          }
        }
      }

      @Override
      public void onCancelled(@NonNull DatabaseError databaseError) {

      }
    });
  }

}
