package com.suffix.fieldforce.akg.activity;

import android.graphics.BlendMode;
import android.graphics.BlendModeColorFilter;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.suffix.fieldforce.R;
import com.suffix.fieldforce.akg.adapter.MemoBodyListAdapter;
import com.suffix.fieldforce.akg.api.AkgApiClient;
import com.suffix.fieldforce.akg.api.AkgApiInterface;
import com.suffix.fieldforce.akg.model.AkgLoginResponse;
import com.suffix.fieldforce.akg.model.InvoiceDetail;
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

public class MemoDetailsActivity extends AppCompatActivity {

  @BindView(R.id.toolbar)
  Toolbar toolbar;

  @BindView(R.id.recyclerView)
  RecyclerView recyclerView;

  @BindView(R.id.txtTotalQuantity)
  TextView txtTotalQuantity;

  @BindView(R.id.txtTotalAmount)
  TextView txtTotalAmount;

  private FieldForcePreferences preferences;
  private AkgApiInterface apiInterface;
  private MemoBodyListAdapter memoBodyListAdapter;
  private List<InvoiceDetail> invoiceDetailList;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_memo_details);
    ButterKnife.bind(this);

    setupToolbar();

    invoiceDetailList = new ArrayList<>();

    preferences = new FieldForcePreferences(this);
    apiInterface = AkgApiClient.getApiClient().create(AkgApiInterface.class);

    LinearLayoutManager manager = new LinearLayoutManager(this);
    recyclerView.setLayoutManager(manager);
    memoBodyListAdapter = new MemoBodyListAdapter(this, invoiceDetailList);
    recyclerView.setAdapter(memoBodyListAdapter);

    MemoListResponse memoListResponse = getIntent().getParcelableExtra("MEMO_DETAIL");
    txtTotalAmount.setText(memoListResponse.getTotalAmount().toString());
    getMemoBody(memoListResponse);
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

  private void getMemoBody(MemoListResponse memoListResponse) {

    int totalQuantity = 0;
    int totalAmount = 0;

    AkgLoginResponse loginResponse = new Gson().fromJson(preferences.getLoginResponse(),
        AkgLoginResponse.class);
    String basicAuthorization = Credentials.basic(String.valueOf(loginResponse.getData().getUserId()),
        preferences.getPassword());

    Call<List<InvoiceDetail>> call = apiInterface.getMemoDetails(basicAuthorization, Integer.parseInt(memoListResponse.getInvNo()));

    call.enqueue(new Callback<List<InvoiceDetail>>() {
      @Override
      public void onResponse(Call<List<InvoiceDetail>> call, Response<List<InvoiceDetail>> response) {
        if (response.isSuccessful()) {
          List<InvoiceDetail> invoiceDetails = response.body();
          memoBodyListAdapter.setData(invoiceDetails);
        }
      }

      @Override
      public void onFailure(Call<List<InvoiceDetail>> call, Throwable t) {
        Toast.makeText(MemoDetailsActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
        call.cancel();
      }
    });
  }
}