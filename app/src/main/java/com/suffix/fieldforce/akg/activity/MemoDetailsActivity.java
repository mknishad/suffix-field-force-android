package com.suffix.fieldforce.akg.activity;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.suffix.fieldforce.R;
import com.suffix.fieldforce.akg.adapter.MemoBodyListAdapter;
import com.suffix.fieldforce.akg.api.AkgApiClient;
import com.suffix.fieldforce.akg.api.AkgApiInterface;
import com.suffix.fieldforce.akg.model.AbulLoginResponse;
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

  private void getMemoBody(MemoListResponse memoListResponse) {

    int totalQuantity = 0;
    int totalAmount = 0;

    AbulLoginResponse loginResponse = new Gson().fromJson(preferences.getLoginResponse(),
        AbulLoginResponse.class);
    String basicAuthorization = Credentials.basic(String.valueOf(loginResponse.getData().getUserId()),
        preferences.getPassword());

    Call<List<InvoiceDetail>> call = apiInterface.getMemoDetails(basicAuthorization, Integer.parseInt(memoListResponse.getInvNo()));

    call.enqueue(new Callback<List<InvoiceDetail>>() {
      @Override
      public void onResponse(Call<List<InvoiceDetail>> call, Response<List<InvoiceDetail>> response) {
        if(response.isSuccessful()){
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