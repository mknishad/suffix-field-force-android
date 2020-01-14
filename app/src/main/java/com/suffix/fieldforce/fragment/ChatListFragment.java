package com.suffix.fieldforce.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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
import com.suffix.fieldforce.model.ModelUser;
import com.suffix.fieldforce.model.ModelUserList;
import com.suffix.fieldforce.model.User;
import com.suffix.fieldforce.preference.FieldForcePreferences;
import com.suffix.fieldforce.retrofitapi.APIClient;
import com.suffix.fieldforce.retrofitapi.APIInterface;
import com.suffix.fieldforce.util.Constants;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatListFragment extends Fragment {

  @BindView(R.id.recyclerViewUser)
  RecyclerView recyclerViewUser;

  @BindView(R.id.progress)
  LottieAnimationView progress;

  private APIInterface apiInterface           = null;
  private DatabaseReference reference         = null;
  private List<ModelUserList> modelUserLists  = null;
  private List<ModelChatList> modelChatLists  = null;
  private FieldForcePreferences preferences   = null;
  private UserAdapter adapter                 = null;
  private User currentUser                    = null;

  private ValueEventListener valueEventListener;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {

    View view = inflater.inflate(R.layout.fragment_chat_list, container, false);
    ButterKnife.bind(this, view);
    //Toast.makeText(getContext(), "ChatList Frament", Toast.LENGTH_SHORT).show();

    preferences     = new FieldForcePreferences(getContext());
    currentUser     = preferences.getUser();
    modelUserLists  = new ArrayList<>();
    modelChatLists  = new ArrayList<>();
    adapter         = new UserAdapter(getContext(), modelUserLists);

    recyclerViewUser.setHasFixedSize(true);
    recyclerViewUser.setLayoutManager(new LinearLayoutManager(getContext()));
    recyclerViewUser.setAdapter(adapter);

    reference = FirebaseDatabase.getInstance().getReference("Chatlist").child(currentUser.getUserId());
    valueEventListener = reference.addValueEventListener(new ValueEventListener() {
      @Override
      public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        modelChatLists.clear();
        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
          ModelChatList modelChatList = snapshot.getValue(ModelChatList.class);
          modelChatLists.add(modelChatList);
        }
        if (modelChatLists.size() > 0) {
          chatList();
        }
      }

      @Override
      public void onCancelled(@NonNull DatabaseError databaseError) {
        Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
      }
    });

    return view;
  }

  private void chatList() {

    modelUserLists.clear();
    apiInterface = APIClient.getApiClient().create(APIInterface.class);

    Call<ModelUser> getUserList = apiInterface.getUserList(
        Constants.INSTANCE.KEY,
        preferences.getUser().getUserId(),
        "0",
        "0");

    getUserList.enqueue(new Callback<ModelUser>() {
      @Override
      public void onResponse(Call<ModelUser> call, Response<ModelUser> response) {
        if (response.isSuccessful()) {
          try {
            ModelUser modelUser = response.body();
            List<ModelUserList> userListsData = modelUser.getResponseData();
            for (ModelUserList modelUserList : userListsData) {
              for (ModelChatList modelChatList : modelChatLists) {
                if (modelUserList.getEmpOfficeId().equals(modelChatList.getId())) {
                  modelUserLists.add(modelUserList);
                }
              }
            }
            adapter.setData(modelUserLists);
          } catch (Exception e) {
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
          } finally {
            progress.setVisibility(View.GONE);
          }
        }
      }

      @Override
      public void onFailure(Call<ModelUser> call, Throwable t) {
        Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
        progress.setVisibility(View.GONE);
        call.cancel();
      }
    });
  }

  @Override
  public void onPause() {
    super.onPause();
    reference.removeEventListener(valueEventListener);
  }
}
