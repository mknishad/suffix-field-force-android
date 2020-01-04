package com.suffix.fieldforce.dialog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.suffix.fieldforce.R;
import com.suffix.fieldforce.adapter.UserAdapter;
import com.suffix.fieldforce.adapter.UserSelectionDialogAdapter;
import com.suffix.fieldforce.adapter.UserSelectionDialogAdapterListener;
import com.suffix.fieldforce.model.ChatGroupMemberDataObj;
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
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserSelectionDialog extends DialogFragment {

  @BindView(R.id.recyclerView)
  RecyclerView recyclerView;

  @BindView(R.id.btnCancel)
  Button btnCancel;

  @BindView(R.id.btnCreate)
  Button btnCreate;

  @BindView(R.id.txtGroupName)
  TextInputEditText txtGroupName;

  @OnClick({R.id.btnCancel, R.id.btnCreate})
  public void onViewClicked(View view) {
    switch (view.getId()) {
      case R.id.btnCancel:
        userSelectionDialogListener.onCancel();
        break;
      case R.id.btnCreate:
        userSelectionDialogListener.onCreate(txtGroupName.getText().toString(), chatGroupMemberDataObj);
        break;
    }
  }

  public void setUserSelectionDialogListener(UserSelectionDialogListener userSelectionDialogListener) {
    this.userSelectionDialogListener = userSelectionDialogListener;
  }

  private UserSelectionDialogListener userSelectionDialogListener;

  private List<ChatGroupMemberDataObj> chatGroupMemberDataObj;
  private APIInterface apiInterface;
  private List<ModelUserList> modelUserLists;
  private FieldForcePreferences preferences;
  private UserSelectionDialogAdapter adapter;

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    super.onCreateView(inflater, container, savedInstanceState);
    View view = inflater.inflate(R.layout.dialog_user_selection, container, false);
    ButterKnife.bind(this, view);

    chatGroupMemberDataObj = new ArrayList<>();
    modelUserLists = new ArrayList<>();
    preferences = new FieldForcePreferences(getContext());

    recyclerView.setHasFixedSize(true);
    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    adapter = new UserSelectionDialogAdapter(getContext(), modelUserLists);
    recyclerView.setAdapter(adapter);

    adapter.setUserSelectionDialogAdapterListener(new UserSelectionDialogAdapterListener() {
      @Override
      public void onCheckboxSelect(int position) {
        chatGroupMemberDataObj.add(new ChatGroupMemberDataObj(modelUserLists.get(position).getEmpOfficeId()));
      }
    });

    apiInterface = APIClient.getApiClient().create(APIInterface.class);
    Call<ModelUser> call = apiInterface.getUserList(
        Constants.INSTANCE.KEY,
        preferences.getUser().getUserId(),
        String.valueOf(preferences.getLocation().getLatitude()),
        String.valueOf(preferences.getLocation().getLongitude()));

    call.enqueue(new Callback<ModelUser>() {
      @Override
      public void onResponse(Call<ModelUser> call, Response<ModelUser> response) {
        if (response.isSuccessful()) {
          try {
            ModelUser modelUser = response.body();
            modelUserLists.clear();
            modelUserLists = modelUser.responseData;
            adapter.setData(modelUserLists);
            recyclerView.setItemViewCacheSize(modelUserLists.size());
          } catch (Exception e) {
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
          } finally {
            //progress.setVisibility(View.GONE);
          }
        }
      }

      @Override
      public void onFailure(Call<ModelUser> call, Throwable t) {
        Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
        //progress.setVisibility(View.GONE);
      }
    });

    return view;
  }

}
