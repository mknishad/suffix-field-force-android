package com.suffix.fieldforce.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.suffix.fieldforce.R;
import com.suffix.fieldforce.adapter.GroupListAdapter;
import com.suffix.fieldforce.dialog.UserSelectionDialog;
import com.suffix.fieldforce.dialog.UserSelectionDialogListener;
import com.suffix.fieldforce.model.ChatGroupMemberDataObj;
import com.suffix.fieldforce.model.GroupChatInfo;
import com.suffix.fieldforce.model.ModelGroupChat;
import com.suffix.fieldforce.preference.FieldForcePreferences;
import com.suffix.fieldforce.retrofitapi.APIClient;
import com.suffix.fieldforce.retrofitapi.APIInterface;
import com.suffix.fieldforce.util.Constants;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MultipleChatFragment extends Fragment {

  @BindView(R.id.recyclerViewUser)
  RecyclerView recyclerViewUser;

  @BindView(R.id.btnCreate)
  FloatingActionButton btnCreate;

  @OnClick(R.id.btnCreate)
  public void createGroup(){
   showUerListDialog();
  }

  private APIInterface apiInterface;
  private FieldForcePreferences preferences;

  List<GroupChatInfo> groupChatInfos;
  private GroupListAdapter adapter;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_multiple_chat, container, false);
    ButterKnife.bind(this,view);

    preferences = new FieldForcePreferences(getContext());
    apiInterface = APIClient.getApiClient().create(APIInterface.class);
    groupChatInfos = new ArrayList<>();
    adapter = new GroupListAdapter(getContext(),groupChatInfos);

    recyclerViewUser.setHasFixedSize(true);
    recyclerViewUser.setLayoutManager(new LinearLayoutManager(getContext()));
    recyclerViewUser.setAdapter(adapter);

    getGroupList();

    return view;
  }

  private void showUerListDialog(){
    UserSelectionDialog dialog =  new UserSelectionDialog();
    dialog.setUserSelectionDialogListener(new UserSelectionDialogListener() {
      @Override
      public void onCreate(String groupName, List<ChatGroupMemberDataObj> chatGroupMemberDataObj) {
        createNewGroup(groupName,chatGroupMemberDataObj);
        dialog.dismiss();
      }

      @Override
      public void onCancel() {
        dialog.dismiss();
      }
    });
    dialog.show(getFragmentManager(),"UerSelectioDialog");
  }

  private void getGroupList() {
    Toast.makeText(getContext(), "Get Group", Toast.LENGTH_SHORT).show();
    Call<ModelGroupChat> call = apiInterface.getChatGroupList(
        Constants.INSTANCE.KEY,
        preferences.getUser().getUserId(),
        String.valueOf(preferences.getLocation().getLatitude()),
        String.valueOf(preferences.getLocation().getLongitude()));

    call.enqueue(new Callback<ModelGroupChat>() {
      @Override
      public void onResponse(Call<ModelGroupChat> call, Response<ModelGroupChat> response) {
        if (response.isSuccessful()) {
          ModelGroupChat modelGroupChat = response.body();
          List<GroupChatInfo> groupChatInfos = modelGroupChat.responseData.getChatGroupObj().getResponseData();
          adapter.setData(groupChatInfos);
          Toast.makeText(getContext(), response.message(), Toast.LENGTH_SHORT).show();
        } else {

        }
      }

      @Override
      public void onFailure(Call<ModelGroupChat> call, Throwable t) {
        Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
        call.cancel();
      }
    });
  }

  private void createNewGroup(String groupName, List<ChatGroupMemberDataObj> chatGroupMemberDataObj) {
    Toast.makeText(getContext(), "Create Group", Toast.LENGTH_SHORT).show();
    Call<ResponseBody> addChatGroup = apiInterface.addChatGroup(
        Constants.INSTANCE.KEY,
        preferences.getUser().getUserId(),
        String.valueOf(preferences.getLocation().getLatitude()),
        String.valueOf(preferences.getLocation().getLongitude()),
        chatGroupMemberDataObj,
        groupName
    );

    addChatGroup.enqueue(new Callback<ResponseBody>() {
      @Override
      public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
        if(response.isSuccessful()){
          Toast.makeText(getContext(), response.message(), Toast.LENGTH_SHORT).show();
          getGroupList();
        }else{

        }
      }

      @Override
      public void onFailure(Call<ResponseBody> call, Throwable t) {
        Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
      }
    });
  }

}
