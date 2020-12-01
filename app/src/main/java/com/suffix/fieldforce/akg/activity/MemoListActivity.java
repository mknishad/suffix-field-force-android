package com.suffix.fieldforce.akg.activity;

import android.content.Intent;
import android.graphics.BlendMode;
import android.graphics.BlendModeColorFilter;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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

  @BindView(R.id.toolbar)
  Toolbar toolbar;

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

    setupToolbar();

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

  private void setupToolbar() {
    setSupportActionBar(toolbar);
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      toolbar.setTitleTextColor(getResources().getColor(android.R.color.white, null));
    } else {
      toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
    }

    if (getSupportActionBar() != null) {
      getSupportActionBar().setDisplayShowTitleEnabled(true);
      getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    toolbar.setNavigationOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        onBackPressed();
      }
    });

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
      toolbar.getNavigationIcon().setColorFilter(new BlendModeColorFilter(Color.WHITE,
          BlendMode.SRC_ATOP));
    } else {
      toolbar.getNavigationIcon().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
    }
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