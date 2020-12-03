package com.suffix.fieldforce.akg.activity;

import android.graphics.BlendMode;
import android.graphics.BlendModeColorFilter;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

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
import com.suffix.fieldforce.akg.model.Distributor;
import com.suffix.fieldforce.akg.model.InvoiceProduct;
import com.suffix.fieldforce.akg.model.InvoiceRequest;
import com.suffix.fieldforce.akg.util.AkgConstants;
import com.suffix.fieldforce.akg.util.AkgPrintingService;
import com.suffix.fieldforce.preference.FieldForcePreferences;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;

public class MemoDetailsActivity extends AppCompatActivity {

  @BindView(R.id.toolbar)
  Toolbar toolbar;

  @BindView(R.id.recyclerView)
  RecyclerView recyclerView;

  @BindView(R.id.txtTotalQuantity)
  TextView txtTotalQuantity;

  @BindView(R.id.txtTotalAmount)
  TextView txtTotalAmount;

  @BindView(R.id.txtStoreName)
  TextView txtStoreName;

  @BindView(R.id.txtStoreLocation)
  TextView txtStoreLocation;

  @OnClick(R.id.btnPrint)
  public void printMemo() {
    Distributor distributor = new Gson().fromJson(preferences.getDistributor(), Distributor.class);
    AkgLoginResponse loginResponse = new Gson().fromJson(preferences.getLoginResponse(),
        AkgLoginResponse.class);
    new AkgPrintingService(this).print(distributor.getData().getDistributorName(),
        "dist", loginResponse, invoiceRequest);
  }

  private FieldForcePreferences preferences;
  private AkgApiInterface apiInterface;
  private MemoBodyListAdapter memoBodyListAdapter;
  private List<InvoiceProduct> invoiceDetailList;
  private InvoiceRequest invoiceRequest;
  private Realm realm;
  private int totalQuantity = 0;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_memo_details);
    ButterKnife.bind(this);

    setupToolbar();

    realm = Realm.getDefaultInstance();

    invoiceDetailList = new ArrayList<>();

    preferences = new FieldForcePreferences(this);
    apiInterface = AkgApiClient.getApiClient().create(AkgApiInterface.class);

    LinearLayoutManager manager = new LinearLayoutManager(this);
    recyclerView.setLayoutManager(manager);
    memoBodyListAdapter = new MemoBodyListAdapter(this, invoiceDetailList);
    recyclerView.setAdapter(memoBodyListAdapter);

    invoiceRequest = getIntent().getParcelableExtra(AkgConstants.MEMO_DETAIL);
    txtStoreName.setText(invoiceRequest.getCustomerName());
    txtStoreLocation.setText(invoiceRequest.getCustomerAddress());
    txtTotalAmount.setText(String.valueOf(invoiceRequest.getTotalAmount()));

    for (InvoiceProduct invoiceProduct : invoiceRequest.getInvoiceProducts()) {
      totalQuantity += invoiceProduct.getProductQty();
    }

    txtTotalQuantity.setText(String.valueOf(totalQuantity));
    memoBodyListAdapter.setData(invoiceRequest.getInvoiceProducts());
    //getMemoBody(memoListResponse);
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
}
