package com.suffix.fieldforce.akg.activity;

import android.content.Intent;
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
import com.suffix.fieldforce.akg.adapter.MemoListInterface;
import com.suffix.fieldforce.akg.api.AkgApiClient;
import com.suffix.fieldforce.akg.api.AkgApiInterface;
import com.suffix.fieldforce.akg.model.AkgLoginResponse;
import com.suffix.fieldforce.akg.model.MemoListResponse;
import com.suffix.fieldforce.preference.FieldForcePreferences;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Credentials;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MemoListActivity extends AppCompatActivity {

  @BindView(R.id.recyclerView)
  RecyclerView recyclerView;

  @BindView(R.id.txtTotalMemo)
  TextView txtTotalMemo;

  private FieldForcePreferences preferences;
  private AkgApiInterface apiInterface;
  private MemoListAdapter memoListAdapter;
  private List<MemoListResponse> memoListResponse;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_memo);
    ButterKnife.bind(this);

    memoListResponse = new ArrayList<>();

    preferences = new FieldForcePreferences(this);
    apiInterface = AkgApiClient.getApiClient().create(AkgApiInterface.class);

    LinearLayoutManager manager = new LinearLayoutManager(this);
    recyclerView.setLayoutManager(manager);
    memoListAdapter = new MemoListAdapter(this, memoListResponse);
    recyclerView.setAdapter(memoListAdapter);

    memoListAdapter.setMemoListInterface(new MemoListInterface() {
      @Override
      public void onItemClick(int position) {
        MemoListResponse response = memoListResponse.get(position);

        Intent intent = new Intent(MemoListActivity.this, MemoDetailsActivity.class);
        intent.putExtra("MEMO_DETAIL", response);
        startActivity(intent);

      }
    });

    getMemoList();

  }

  private void getMemoList() {

    AkgLoginResponse loginResponse = new Gson().fromJson(preferences.getLoginResponse(),
        AkgLoginResponse.class);
    String basicAuthorization = Credentials.basic(String.valueOf(loginResponse.getData().getUserId()),
        preferences.getPassword());

    Call<List<MemoListResponse>> call = apiInterface.getMemoList(basicAuthorization, loginResponse.getData().getId());
    call.enqueue(new Callback<List<MemoListResponse>>() {
      @Override
      public void onResponse(Call<List<MemoListResponse>> call, Response<List<MemoListResponse>> response) {
        if (response.isSuccessful()) {
          memoListResponse = response.body();
          txtTotalMemo.setText("মোট : "+memoListResponse.size());
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