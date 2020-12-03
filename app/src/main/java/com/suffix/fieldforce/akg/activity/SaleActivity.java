package com.suffix.fieldforce.akg.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BlendMode;
import android.graphics.BlendModeColorFilter;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.nex3z.togglebuttongroup.SingleSelectToggleGroup;
import com.suffix.fieldforce.R;
import com.suffix.fieldforce.akg.adapter.CustomArrayAdapter;
import com.suffix.fieldforce.akg.adapter.CustomerListAdapter;
import com.suffix.fieldforce.akg.adapter.CustomerListInterface;
import com.suffix.fieldforce.akg.database.manager.RealMDatabaseManager;
import com.suffix.fieldforce.akg.model.AkgLoginResponse;
import com.suffix.fieldforce.akg.model.CustomerData;
import com.suffix.fieldforce.akg.model.GlobalSettings;
import com.suffix.fieldforce.akg.util.AkgConstants;
import com.suffix.fieldforce.akg.util.LocationUtils;
import com.suffix.fieldforce.preference.FieldForcePreferences;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.SmartLocation;

public class SaleActivity extends AppCompatActivity {

  private static final String TAG = "SaleActivity";

  @BindView(R.id.toolbar)
  Toolbar toolbar;

  @BindView(R.id.toggleGroup)
  SingleSelectToggleGroup toggleGroup;

  @BindView(R.id.imgDropArrow)
  ImageView imgDropArrow;

  @BindView(R.id.txtName)
  TextView txtName;

  @BindView(R.id.txtAddress)
  TextView txtAddress;

  @BindView(R.id.spinnerUsers)
  Spinner spinnerUsers;

  @BindView(R.id.customerRecyclerView)
  RecyclerView customerRecyclerView;

  @BindView(R.id.btnSale)
  Button btnSale;

  @OnClick(R.id.btnSale)
  public void gotoCheckout() {
    if (spinnerUsers.getSelectedItemPosition() > 0) {
      selectedCustomer = filteredCustomerList.get(spinnerUsers.getSelectedItemPosition());
      Intent intent = new Intent(SaleActivity.this, ProductCategoryActivity.class);
      intent.putExtra(AkgConstants.CUSTOMER_INFO, selectedCustomer);
      startActivity(intent);
    } else {
      Toast.makeText(SaleActivity.this, "You must select a customer first.", Toast.LENGTH_SHORT).show();
    }
  }

  @OnClick(R.id.imgDropArrow)
  public void toggleKeyboard() {
    spinnerUsers.performClick();
  }

  private FieldForcePreferences preferences;
  private AkgLoginResponse loginResponse;
  private ArrayAdapter<CustomerData> spinnerAdapter;
  private CustomerListAdapter customerListAdapter;
  private List<CustomerData> customerDataList;
  private List<CustomerData> filteredCustomerList;
  private CustomerData selectedCustomer = null;
  private RealMDatabaseManager realMDatabaseManager;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_sale);
    ButterKnife.bind(this);

    setupToolbar();

    realMDatabaseManager = new RealMDatabaseManager();
    preferences = new FieldForcePreferences(this);
    loginResponse = new Gson().fromJson(preferences.getLoginResponse(), AkgLoginResponse.class);
    customerDataList = new ArrayList<>();
    filteredCustomerList = new ArrayList<>();

    getAllCustomer();
    setupToggleGroup();
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
      getSupportActionBar().setTitle("সেল");
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

  private void setupToggleGroup() {
    toggleGroup.setOnCheckedChangeListener(new SingleSelectToggleGroup.OnCheckedChangeListener() {
      @Override
      public void onCheckedChanged(SingleSelectToggleGroup group, int checkedId) {
        switch (checkedId) {
          case R.id.choice_a:
            filterCustomers("a");
            spinnerUsers.setSelection(0);
            txtName.setText("Select Customer");
            txtAddress.setText("");
            break;
          case R.id.choice_b:
            filterCustomers("b");
            spinnerUsers.setSelection(0);
            txtName.setText("Select Customer");
            txtAddress.setText("");
            break;
          case R.id.choice_c:
            filterCustomers("c");
            spinnerUsers.setSelection(0);
            txtName.setText("Select Customer");
            txtAddress.setText("");
            break;
          case R.id.choice_d:
            filterCustomers("d");
            spinnerUsers.setSelection(0);
            txtName.setText("Select Customer");
            txtAddress.setText("");
            break;
          case R.id.choice_e:
            filterCustomers("e");
            spinnerUsers.setSelection(0);
            txtName.setText("Select Customer");
            txtAddress.setText("");
            break;
          case R.id.choice_f:
            filterCustomers("f");
            spinnerUsers.setSelection(0);
            txtName.setText("Select Customer");
            txtAddress.setText("");
            break;
          case R.id.choice_g:
            filterCustomers("g");
            spinnerUsers.setSelection(0);
            txtName.setText("Select Customer");
            txtAddress.setText("");
            break;
          case R.id.choice_h:
            filterCustomers("h");
            spinnerUsers.setSelection(0);
            txtName.setText("Select Customer");
            txtAddress.setText("");
            break;
          case R.id.choice_i:
            filterCustomers("i");
            spinnerUsers.setSelection(0);
            txtName.setText("Select Customer");
            txtAddress.setText("");
            break;
          case R.id.choice_j:
            filterCustomers("j");
            spinnerUsers.setSelection(0);
            txtName.setText("Select Customer");
            txtAddress.setText("");
            break;
          case R.id.choice_k:
            filterCustomers("k");
            spinnerUsers.setSelection(0);
            txtName.setText("Select Customer");
            txtAddress.setText("");
            break;
          case R.id.choice_l:
            filterCustomers("l");
            spinnerUsers.setSelection(0);
            txtName.setText("Select Customer");
            txtAddress.setText("");
            break;
          case R.id.choice_m:
            filterCustomers("m");
            spinnerUsers.setSelection(0);
            txtName.setText("Select Customer");
            txtAddress.setText("");
            break;
          case R.id.choice_n:
            filterCustomers("n");
            spinnerUsers.setSelection(0);
            txtName.setText("Select Customer");
            txtAddress.setText("");
            break;
          case R.id.choice_o:
            filterCustomers("o");
            spinnerUsers.setSelection(0);
            txtName.setText("Select Customer");
            txtAddress.setText("");
            break;
          case R.id.choice_p:
            filterCustomers("p");
            spinnerUsers.setSelection(0);
            txtName.setText("Select Customer");
            txtAddress.setText("");
            break;
          case R.id.choice_q:
            filterCustomers("q");
            spinnerUsers.setSelection(0);
            txtName.setText("Select Customer");
            txtAddress.setText("");
            break;
          case R.id.choice_r:
            filterCustomers("r");
            spinnerUsers.setSelection(0);
            txtName.setText("Select Customer");
            txtAddress.setText("");
            break;
          case R.id.choice_s:
            filterCustomers("s");
            spinnerUsers.setSelection(0);
            txtName.setText("Select Customer");
            txtAddress.setText("");
            break;
          case R.id.choice_t:
            filterCustomers("t");
            spinnerUsers.setSelection(0);
            txtName.setText("Select Customer");
            txtAddress.setText("");
            break;
          case R.id.choice_u:
            filterCustomers("u");
            spinnerUsers.setSelection(0);
            txtName.setText("Select Customer");
            txtAddress.setText("");
            break;
          case R.id.choice_v:
            filterCustomers("v");
            spinnerUsers.setSelection(0);
            txtName.setText("Select Customer");
            txtAddress.setText("");
            break;
          case R.id.choice_w:
            filterCustomers("w");
            spinnerUsers.setSelection(0);
            txtName.setText("Select Customer");
            txtAddress.setText("");
            break;
          case R.id.choice_x:
            filterCustomers("x");
            spinnerUsers.setSelection(0);
            txtName.setText("Select Customer");
            txtAddress.setText("");
            break;
          case R.id.choice_y:
            filterCustomers("y");
            spinnerUsers.setSelection(0);
            txtName.setText("Select Customer");
            txtAddress.setText("");
            break;
          case R.id.choice_z:
            filterCustomers("z");
            spinnerUsers.setSelection(0);
            txtName.setText("Select Customer");
            txtAddress.setText("");
            break;
          case R.id.choice_all:
            filterCustomers("all");
            spinnerUsers.setSelection(0);
            txtName.setText("Select Customer");
            txtAddress.setText("");
            break;
        }
      }
    });
  }

  private void filterCustomers(String start) {
    filteredCustomerList.clear();
    //filteredCustomerList.add(new CustomerData("Select Customer"));
    if (start.toLowerCase().equalsIgnoreCase("all")) {
      filteredCustomerList.addAll(customerDataList);
    } else {
      for (CustomerData customerData : customerDataList) {
        if (customerData.getCustomerName().toLowerCase().startsWith(start.toLowerCase())) {
          filteredCustomerList.add(customerData);
        }
      }
    }
    Log.d(TAG, "filterCustomers: filteredCustomerList = " + filteredCustomerList);
    spinnerAdapter.notifyDataSetChanged();
    customerListAdapter.notifyDataSetChanged();
  }

  private void getAllCustomer() {
    customerDataList = realMDatabaseManager.prepareCustomerData();
    filteredCustomerList.clear();
    //filteredCustomerList.add(new CustomerData("Select Customer"));
    filteredCustomerList.addAll(customerDataList);

    setupCustomerSpinner();
    setupCustomerList();
  }

  private void setupCustomerSpinner() {
    spinnerAdapter = new CustomArrayAdapter(SaleActivity.this, R.layout.spinner_item, filteredCustomerList);
    spinnerUsers.setAdapter(spinnerAdapter);
    spinnerUsers.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
      @Override
      public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        btnSale.setVisibility(View.INVISIBLE);
        if (position > 0) {
          CustomerData customerData = filteredCustomerList.get(position);
          Log.d(TAG, "onItemSelected: customer location = " + customerData.getLat() + ", " +
              customerData.getLng());
          SmartLocation.with(SaleActivity.this).location().oneFix()
              .start(new OnLocationUpdatedListener() {
                @Override
                public void onLocationUpdated(Location location) {
                  double distance = LocationUtils.getDistance(customerData.getLat(), customerData.getLng(),
                      location.getLatitude(), location.getLongitude());
                  double distanceThreshold = 10.0;
                  for (GlobalSettings settings : loginResponse.getData().getGlobalSettingList()) {
                    if (settings.getAttributeName().equalsIgnoreCase("GEO_SYNC_INTERVAL")) {
                      distanceThreshold = Double.parseDouble(settings.getAttributeValue());
                    }
                  }
                  if (distance > distanceThreshold) {
                    new AlertDialog.Builder(SaleActivity.this)
                        .setMessage("আপনি কাস্টমার থেকে দূরে অবস্থান করছেন!")

                        // Specifying a listener allows you to take an action before dismissing the dialog.
                        // The dialog is automatically dismissed when a dialog button is clicked.
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                          public void onClick(DialogInterface dialog, int which) {
                            // Continue with delete operation
                          }
                        })

                        // A null listener allows the button to dismiss the dialog and take no further action.
                        //.setNegativeButton(android.R.string.no, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
                    /*Toast.makeText(SaleActivity.this, "আপনি কাস্টমার থেকে দূরে অবস্থান করছেন!",
                        Toast.LENGTH_SHORT).show();*/
                    spinnerUsers.setSelection(0);
                    btnSale.setVisibility(View.INVISIBLE);
                  } else {
                    btnSale.setVisibility(View.VISIBLE);
                  }
                }
              });
        }
      }

      @Override
      public void onNothingSelected(AdapterView<?> parent) {

      }
    });
  }

  private void setupCustomerList() {
    customerListAdapter = new CustomerListAdapter(SaleActivity.this, filteredCustomerList);
    customerRecyclerView.setAdapter(customerListAdapter);
    customerListAdapter.setCustomerListInterface(new CustomerListInterface() {
      @Override
      public void onItemClick(int position, CustomerData customerData) {
        selectedCustomer = customerData;
        /*Toast.makeText(SaleActivity.this, customerData.getCustomerName() + "\n" +
            customerData.getAddress(), Toast.LENGTH_SHORT).show();*/
        txtName.setText("Select Customer");
        txtAddress.setText("");

        SmartLocation.with(SaleActivity.this).location().oneFix()
            .start(new OnLocationUpdatedListener() {
              @Override
              public void onLocationUpdated(Location location) {
                double distance = LocationUtils.getDistance(selectedCustomer.getLat(), selectedCustomer.getLng(),
                    location.getLatitude(), location.getLongitude());
                double distanceThreshold = 10.0;
                for (GlobalSettings settings : loginResponse.getData().getGlobalSettingList()) {
                  if (settings.getAttributeName().equalsIgnoreCase("GEO_SYNC_INTERVAL")) {
                    distanceThreshold = Double.parseDouble(settings.getAttributeValue());
                  }
                }
                if (distance > distanceThreshold) {
                  new AlertDialog.Builder(SaleActivity.this)
                      .setMessage("আপনি কাস্টমার থেকে দূরে অবস্থান করছেন!")

                      // Specifying a listener allows you to take an action before dismissing the dialog.
                      // The dialog is automatically dismissed when a dialog button is clicked.
                      .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                          // Continue with delete operation
                        }
                      })

                      // A null listener allows the button to dismiss the dialog and take no further action.
                      //.setNegativeButton(android.R.string.no, null)
                      .setIcon(android.R.drawable.ic_dialog_alert)
                      .show();
                    /*Toast.makeText(SaleActivity.this, "আপনি কাস্টমার থেকে দূরে অবস্থান করছেন!",
                        Toast.LENGTH_SHORT).show();*/
                  txtName.setText("Select Customer");
                  txtAddress.setText("");
                } else {
                  txtName.setText(selectedCustomer.getCustomerName());
                  txtAddress.setText(selectedCustomer.getAddress());
                  btnSale.setVisibility(View.VISIBLE);
                }
              }
            });
      }
    });
  }
}
