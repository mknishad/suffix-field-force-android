package com.suffix.fieldforce.activity.chat;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.suffix.fieldforce.R;
import com.suffix.fieldforce.adapter.UserAdapter;
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

public class UserListActivity extends AppCompatActivity {

  @BindView(R.id.recyclerViewUser)
  RecyclerView recyclerViewUser;

  private List<User> mUser;
  List<ModelUserList> modelUserLists;
  private FieldForcePreferences preferences;
  private UserAdapter adapter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_user_list);
    ButterKnife.bind(this);

    preferences = new FieldForcePreferences(this);

    recyclerViewUser.setHasFixedSize(true);
    recyclerViewUser.setLayoutManager( new LinearLayoutManager(this));

    mUser = new ArrayList<>();

    /*DatabaseReference mref = FirebaseDatabase.getInstance().getReference("users");
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
    });*/

    APIInterface apiInterface = APIClient.getApiClient().create(APIInterface.class);
    Call<List<ModelUserList>> call = apiInterface.getUserList(
        Constants.INSTANCE.KEY,
        preferences.getUser().getUserId(),
        String.valueOf(preferences.getLocation().getLatitude()),
        String.valueOf(preferences.getLocation().getLongitude()));

    call.enqueue(new Callback<List<ModelUserList>>() {
      @Override
      public void onResponse(Call<List<ModelUserList>> call, Response<List<ModelUserList>> response) {
        if(response.isSuccessful()){
          modelUserLists = response.body();
          adapter = new UserAdapter(UserListActivity.this,modelUserLists);
          recyclerViewUser.setAdapter(adapter);
        }
      }

      @Override
      public void onFailure(Call<List<ModelUserList>> call, Throwable t) {

      }
    });
  }
}
