package com.suffix.fieldforce.akg.activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.suffix.fieldforce.R;
import com.suffix.fieldforce.akg.adapter.MemoListAdapter;
import com.suffix.fieldforce.akg.api.AbulApiClient;
import com.suffix.fieldforce.akg.api.AbulApiInterface;
import com.suffix.fieldforce.akg.model.AbulLoginResponse;
import com.suffix.fieldforce.akg.model.MemoListResponse;
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

    Call<List<MemoListResponse>> call = apiInterface.getMemoList(abulLoginResponse.getData().getId());
    call.enqueue(new Callback<List<MemoListResponse>>() {
      @Override
      public void onResponse(Call<List<MemoListResponse>> call, Response<List<MemoListResponse>> response) {
        if (response.isSuccessful()) {
          memoListResponse = response.body();
          memoListAdapter.setData(memoListResponse);
        } else {
          Log.d("Memo", response.errorBody().toString());
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