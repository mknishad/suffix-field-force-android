package com.suffix.fieldforce.abul.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.suffix.fieldforce.R;
import com.suffix.fieldforce.abul.adapter.MemoListAdapter;
import com.suffix.fieldforce.abul.api.AbulApiClient;
import com.suffix.fieldforce.abul.api.AbulApiInterface;
import com.suffix.fieldforce.abul.model.AbulLoginResponse;
import com.suffix.fieldforce.abul.model.MemoListResponse;
import com.suffix.fieldforce.adapter.TransportRequasitionListAdapter;
import com.suffix.fieldforce.preference.FieldForcePreferences;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MemoListActivity extends AppCompatActivity {

  @BindView(R.id.recyclerView)
  RecyclerView recyclerView;
  
  @BindView(R.id.txtTotalMemo)
  TextView txtTotalMemo;

  private FieldForcePreferences preferences;
  private AbulApiInterface apiInterface;
  private MemoListAdapter memoListAdapter;
  private List<MemoListResponse> memoListResponse;
  
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_memo);
    ButterKnife.bind(this);

    memoListResponse = new ArrayList<>();

    preferences = new FieldForcePreferences(this);
    apiInterface = AbulApiClient.getApiClient().create(AbulApiInterface.class);
    
    LinearLayoutManager manager = new LinearLayoutManager(this);
    recyclerView.setLayoutManager(manager);
    memoListAdapter = new MemoListAdapter(this, memoListResponse);
    recyclerView.setAdapter(memoListAdapter);

    memoListAdapter.setMemoListInterface(new MemoListInterface() {
      @Override
      public void onItemClick(int position) {
        MemoListResponse response = memoListResponse.get(position);
      }
    });

    getMemoList();
    
  }

  private void getMemoList() {
    AbulLoginResponse abulLoginResponse = new Gson().fromJson(preferences.getLoginResponse(), AbulLoginResponse.class);

    Call<List<MemoListResponse>> call = apiInterface.getMemoList( abulLoginResponse.getData().getId() );
    call.enqueue(new Callback<List<MemoListResponse>>() {
      @Override
      public void onResponse(Call<List<MemoListResponse>> call, Response<List<MemoListResponse>> response) {
        if(response.isSuccessful()){
          memoListResponse = response.body();
          memoListAdapter.setData(memoListResponse);
        }
      }

      @Override
      public void onFailure(Call<List<MemoListResponse>> call, Throwable t) {
        Toast.makeText(MemoListActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
        call.cancel();
      }
    });

  }
}