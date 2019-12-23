package com.suffix.fieldforce.activity.chat;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.suffix.fieldforce.R;
import com.suffix.fieldforce.adapter.UserAdapter;
import com.suffix.fieldforce.model.ModelUser;
import com.suffix.fieldforce.model.ModelUserList;
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

  List<ModelUserList> modelUserLists = new ArrayList<>();
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
    adapter = new UserAdapter(UserListActivity.this,modelUserLists);
    recyclerViewUser.setAdapter(adapter);

    APIInterface apiInterface = APIClient.getApiClient().create(APIInterface.class);
    Call<ModelUser> call = apiInterface.getUserList(
        Constants.INSTANCE.KEY,
        preferences.getUser().getUserId(),
        String.valueOf(preferences.getLocation().getLatitude()),
        String.valueOf(preferences.getLocation().getLongitude()));

    call.enqueue(new Callback<ModelUser>() {
      @Override
      public void onResponse(Call<ModelUser> call, Response<ModelUser> response) {
        if(response.isSuccessful()){
          ModelUser modelUser = response.body();
          modelUserLists.clear();
          modelUserLists = modelUser.responseData;
          adapter.setData(modelUserLists);
        }
      }

      @Override
      public void onFailure(Call<ModelUser> call, Throwable t) {
        Toast.makeText(UserListActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
      }
    });
  }
}
