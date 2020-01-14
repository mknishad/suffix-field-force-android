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

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
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
import java.util.EventListener;
import java.util.HashMap;
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
  private DatabaseReference reference;
  private List<GroupChatInfo> groupChatInfos;
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

//    Call<ModelGroupChat> call = apiInterface.getChatGroupList(
//        Constants.INSTANCE.KEY,
//        preferences.getUser().getUserId(),
//        String.valueOf(preferences.getLocation().getLatitude()),
//        String.valueOf(preferences.getLocation().getLongitude()));
//
//    call.enqueue(new Callback<ModelGroupChat>() {
//      @Override
//      public void onResponse(Call<ModelGroupChat> call, Response<ModelGroupChat> response) {
//        if (response.isSuccessful()) {
//          ModelGroupChat modelGroupChat = response.body();
//          List<GroupChatInfo> groupChatInfos = modelGroupChat.responseData.getChatGroupObj().getResponseData();
//          adapter.setData(groupChatInfos);
//        } else {
//
//        }
//      }
//
//      @Override
//      public void onFailure(Call<ModelGroupChat> call, Throwable t) {
//        Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
//        call.cancel();
//      }
//    });

    reference = FirebaseDatabase.getInstance().getReference("Group");
    reference.addValueEventListener(new ValueEventListener() {
      @Override
      public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        groupChatInfos.clear();
        for(DataSnapshot snapshot : dataSnapshot.getChildren()){
          String gropuName = snapshot.getKey();
          groupChatInfos.add(new GroupChatInfo(gropuName));
        }
        adapter.setData(groupChatInfos);
      }

      @Override
      public void onCancelled(@NonNull DatabaseError databaseError) {

      }
    });
  }

  private void createNewGroup(String groupName, List<ChatGroupMemberDataObj> object) {
//    Gson gson = new Gson();
//    String chatGroupMemberDataObj = gson.toJson(object);
//    //Toast.makeText(getContext(), "Create Group", Toast.LENGTH_SHORT).show();
//    Call<ResponseBody> addChatGroup = apiInterface.addChatGroup(
//        Constants.INSTANCE.KEY,
//        preferences.getUser().getUserId(),
//        String.valueOf(preferences.getLocation().getLatitude()),
//        String.valueOf(preferences.getLocation().getLongitude()),
//        chatGroupMemberDataObj,
//        groupName
//    );
//
//    addChatGroup.enqueue(new Callback<ResponseBody>() {
//      @Override
//      public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//        if(response.isSuccessful()){
//          Toast.makeText(getContext(), response.message(), Toast.LENGTH_SHORT).show();
//          getGroupList();
//        }else{
//
//        }
//      }
//
//      @Override
//      public void onFailure(Call<ResponseBody> call, Throwable t) {
//        Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
//      }
//    });
    reference = FirebaseDatabase.getInstance().getReference();
    HashMap<String, Object> hashMap = new HashMap<>();

    for(int i = 0; i<object.size(); i++){
      hashMap.put("member_"+i, object.get(i).getGroupMemberId());
    }
    reference.child("Group").child(groupName).push().setValue(hashMap);
  }
}
