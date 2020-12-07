package com.suffix.fieldforce.akg.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.BlendMode;
import android.graphics.BlendModeColorFilter;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.suffix.fieldforce.R;
import com.suffix.fieldforce.akg.adapter.MemoBodyListAdapter;
import com.suffix.fieldforce.akg.adapter.PrintingInterface;
import com.suffix.fieldforce.akg.adapter.ProductUpdateListener;
import com.suffix.fieldforce.akg.api.AkgApiClient;
import com.suffix.fieldforce.akg.api.AkgApiInterface;
import com.suffix.fieldforce.akg.database.RealmDatabseManagerInterface;
import com.suffix.fieldforce.akg.database.manager.RealMDatabaseManager;
import com.suffix.fieldforce.akg.database.manager.SyncManager;
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

  @BindView(R.id.layoutCollection)
  LinearLayout layoutCollection;

  @BindView(R.id.txtCollection)
  TextInputEditText txtCollection;

  @BindView(R.id.btnUpdate)
  Button btnUpdate;
  private String paymentStatus = "Paid";

  @OnClick(R.id.btnUpdate)
  public void updateCollection() {

    new SyncManager(this).updateInvoiceRequest(invoiceRequest, Double.parseDouble(txtCollection.getText().toString()), new RealmDatabseManagerInterface.Sync() {
      @Override
      public void onSuccess() {
        finish();
      }
    });

  }

  @OnClick(R.id.btnPrint)
  public void printMemo() {

    progressDialog.show();

    Distributor distributor = new Gson().fromJson(preferences.getDistributor(), Distributor.class);
    AkgLoginResponse loginResponse = new Gson().fromJson(preferences.getLoginResponse(),
        AkgLoginResponse.class);
    new AkgPrintingService().print(distributor.getData().getDistributorName(),
        distributor.getData().getMobile(), loginResponse, invoiceRequest, paymentStatus, new PrintingInterface() {
          @Override
          public void onPrintSuccess(String message) {
            progressDialog.dismiss();
            updateCollection();
            builder.setMessage(message);
            builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
              @Override
              public void onClick(DialogInterface dialog, int which) {
                //onBackPressed();
              }
            });

            AlertDialog alertDialog = builder.create();
            alertDialog.setCancelable(false);
            alertDialog.show();
          }

          @Override
          public void onPrintFail(String message) {

            progressDialog.dismiss();
            updateCollection();
            builder.setMessage(message);
            builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
              @Override
              public void onClick(DialogInterface dialog, int which) {
                //alertDialog.dismiss();
              }
            });

            AlertDialog alertDialog = builder.create();
            alertDialog.setCancelable(false);
            alertDialog.show();
          }
        });
  }

  private static final String TAG = "MemoDetailsActivity";

  private FieldForcePreferences preferences;
  private AkgApiInterface apiInterface;
  private MemoBodyListAdapter memoBodyListAdapter;
  private List<InvoiceProduct> invoiceDetailList;
  private InvoiceRequest invoiceRequest;
  private Realm realm;
  private int totalQuantity = 0;

  private ProgressDialog progressDialog;
  private AlertDialog.Builder builder;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_memo_details);
    ButterKnife.bind(this);

    setupToolbar();

    realm = Realm.getDefaultInstance();

    progressDialog = new ProgressDialog(this);
    progressDialog.setMessage("Printing...");

    builder = new AlertDialog.Builder(this);

    invoiceDetailList = new ArrayList<>();

    preferences = new FieldForcePreferences(this);
    apiInterface = AkgApiClient.getApiClient().create(AkgApiInterface.class);

    invoiceRequest = getIntent().getParcelableExtra(AkgConstants.MEMO_DETAIL);

    LinearLayoutManager manager = new LinearLayoutManager(this);
    recyclerView.setLayoutManager(manager);
    memoBodyListAdapter = new MemoBodyListAdapter(this, invoiceDetailList,
        invoiceRequest.getInvoiceId(),
        new ProductUpdateListener() {
          @Override
          public void onSuccess() {
            List<InvoiceRequest> invoiceRequests = new RealMDatabaseManager().prepareInvoiceRequest();
            for (InvoiceRequest invoice : invoiceRequests) {
              if (invoice.getInvoiceId().equalsIgnoreCase(invoiceRequest.getInvoiceId())) {
                memoBodyListAdapter.setData(invoice.getInvoiceProducts());
              }
            }
          }
        });
    recyclerView.setAdapter(memoBodyListAdapter);

    Log.d(TAG, "onCreate: invoiceRequest = " + invoiceRequest);
    txtStoreName.setText(invoiceRequest.getCustomerName());
    txtStoreLocation.setText(invoiceRequest.getCustomerAddress());
    txtTotalAmount.setText(String.valueOf(invoiceRequest.getTotalAmount()));

    if(invoiceRequest.getTotalAmount() - invoiceRequest.getRecievedAmount() != 0.0f){
      paymentStatus = "Due";
      txtCollection.setText(String.valueOf(invoiceRequest.getTotalAmount() - invoiceRequest.getRecievedAmount()));
    }else{
      txtCollection.setVisibility(View.GONE);
    }

    for (InvoiceProduct invoiceProduct : invoiceRequest.getInvoiceProducts()) {
      totalQuantity += invoiceProduct.getProductQty();
    }

    //txtTotalQuantity.setText(String.valueOf(totalQuantity));
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
