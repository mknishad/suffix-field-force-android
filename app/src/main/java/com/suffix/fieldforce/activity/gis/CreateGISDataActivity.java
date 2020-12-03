package com.suffix.fieldforce.activity.gis;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
//import com.shivtechs.maplocationpicker.LocationPickerActivity;
//import com.shivtechs.maplocationpicker.MapUtility;
import com.suffix.fieldforce.R;
import com.suffix.fieldforce.activity.MainActivity;
import com.suffix.fieldforce.adapter.TJBAdapter;
import com.suffix.fieldforce.dialog.TJBDialog;
import com.suffix.fieldforce.dialog.TJBDialogListener;
import com.suffix.fieldforce.model.TJBInfoData;
import com.suffix.fieldforce.preference.FieldForcePreferences;
import com.suffix.fieldforce.retrofitapi.APIClient;
import com.suffix.fieldforce.retrofitapi.APIInterface;
import com.suffix.fieldforce.util.Constants;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateGISDataActivity extends AppCompatActivity {

  @BindView(R.id.imgMap)
  ImageView imgMap;
  @BindView(R.id.imgDrawer)
  ImageView imgDrawer;
  @BindView(R.id.toolBarTitle)
  TextView toolBarTitle;
  @BindView(R.id.txtLinkName)
  TextInputEditText txtLinkName;
  @BindView(R.id.txtAAddress)
  TextInputEditText txtAAddress;
  @BindView(R.id.txtAOH)
  TextInputEditText txtAOH;
  @BindView(R.id.txtAOG)
  TextInputEditText txtAOG;
  @BindView(R.id.txtBAddress)
  TextInputEditText txtBAddress;
  @BindView(R.id.txtBOH)
  TextInputEditText txtBOH;
  @BindView(R.id.txtBOG)
  TextInputEditText txtBOG;
  @BindView(R.id.recyclerView)
  RecyclerView recyclerView;
  @BindView(R.id.layoutButton)
  LinearLayout layoutButton;
  @BindView(R.id.layoutTJB)
  LinearLayout layoutTJB;
  @BindView(R.id.btnAddTJB)
  TextView btnAddTJB;
  @BindView(R.id.btnSubmit)
  TextView btnSubmit;

  public final int ADDRESS_PICKER_REQUEST_ONE = 777;
  public final int ADDRESS_PICKER_REQUEST_TWO = 778;

  private FieldForcePreferences preferences;
  private APIInterface apiInterface;

  private List<TJBInfoData> tjbInfoDataObj = new ArrayList<TJBInfoData>();
  private TJBAdapter tjbAdapter;

  String linkName;
  String siteAAddress;
  String siteBAddress;
  String siteALat;
  String siteALng;
  String siteBLat;
  String siteBLng;
  String tjbInfoData;
  String overHeadDistance;
  String underGroundDistance;

  @OnClick(R.id.imgDrawer)
  public void back() {
    onBackPressed();
  }

  @OnClick(R.id.btnAddTJB)
  public void addTJB() {
    TJBDialog dialog = new TJBDialog();
    dialog.setTjbDialogListener(new TJBDialogListener() {
      @Override
      public void onSubmit(TJBInfoData tjbInfoData) {
        tjbInfoDataObj.add(tjbInfoData);
        tjbAdapter.setData(tjbInfoDataObj);

        if(layoutTJB.getVisibility() != View.VISIBLE){
          layoutTJB.setVisibility(View.VISIBLE);
        }
      }
    });
    dialog.show(getSupportFragmentManager(), "TJB");
  }

  @OnClick({R.id.txtAAddress, R.id.txtBAddress})
  public void onViewClicked(View view) {
//    switch (view.getId()) {
//      case R.id.txtAAddress:
//        Intent i = new Intent(CreateGISDataActivity.this, LocationPickerActivity.class);
//        startActivityForResult(i, ADDRESS_PICKER_REQUEST_ONE);
//        break;
//      case R.id.txtBAddress:
//        Intent intent = new Intent(CreateGISDataActivity.this, LocationPickerActivity.class);
//        startActivityForResult(intent, ADDRESS_PICKER_REQUEST_TWO);
//        break;
//    }
  }

  @OnClick(R.id.btnSubmit)
  public void submit() {

    linkName = txtLinkName.getText().toString();
    siteAAddress = txtLinkName.getText().toString();
    siteBAddress = txtLinkName.getText().toString();
    tjbInfoData = "{\"tjbInfoDataObj\":"+new Gson().toJson(tjbInfoDataObj)+"}";
    overHeadDistance = txtAOH.getText().toString();
    underGroundDistance = txtAOG.getText().toString();

    createGis();
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_create_gisdata);
    ButterKnife.bind(this);
    setActionBar();

    //MapUtility.apiKey = getResources().getString(R.string.google_maps_key);

    preferences = new FieldForcePreferences(this);
    apiInterface = APIClient.getApiClient().create(APIInterface.class);

    LinearLayoutManager manager = new LinearLayoutManager(this);
    recyclerView.setLayoutManager(manager);
    tjbAdapter = new TJBAdapter(this, new ArrayList<>());
    recyclerView.setAdapter(tjbAdapter);

  }

  private void setActionBar() {
    imgDrawer.setImageResource(R.drawable.ic_arrow_back);
    imgMap.setImageResource(R.drawable.ic_edit);
    toolBarTitle.setText("Create GIS Data");
    imgMap.setVisibility(View.GONE);
  }

  @Override
  public void onBackPressed() {
    super.onBackPressed();
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == ADDRESS_PICKER_REQUEST_ONE) {
      try {
//        if (data != null && data.getStringExtra(MapUtility.ADDRESS) != null) {
//          String address = data.getStringExtra(MapUtility.ADDRESS);
//          double selectedLatitude = data.getDoubleExtra(MapUtility.LATITUDE, 0.0);
//          double selectedLongitude = data.getDoubleExtra(MapUtility.LONGITUDE, 0.0);
//          siteALat = String.valueOf(selectedLatitude);
//          siteALng = String.valueOf(selectedLongitude);
//          txtAAddress.setText(address);
//        }
      } catch (Exception ex) {
        ex.printStackTrace();
      }
    }else if (requestCode == ADDRESS_PICKER_REQUEST_TWO) {
      try {
//        if (data != null && data.getStringExtra(MapUtility.ADDRESS) != null) {
//          String address = data.getStringExtra(MapUtility.ADDRESS);
//          double selectedLatitude = data.getDoubleExtra(MapUtility.LATITUDE, 0.0);
//          double selectedLongitude = data.getDoubleExtra(MapUtility.LONGITUDE, 0.0);
//          siteBLat = String.valueOf(selectedLatitude);
//          siteBLng = String.valueOf(selectedLongitude);
//          txtBAddress.setText(address);
//        }
      } catch (Exception ex) {
        ex.printStackTrace();
      }
    }
  }

  private void createGis() {

    String userId = preferences.getUser().getUserId();

    Call<ResponseBody> postGISDataCollection = apiInterface.postGISDataCollection(
        Constants.KEY,
        userId,
        linkName,
        siteAAddress,
        siteBAddress,
        siteALat,
        siteALng,
        siteBLat,
        siteBLng,
        tjbInfoData,
        overHeadDistance,
        underGroundDistance
    );

    postGISDataCollection.enqueue(new Callback<ResponseBody>() {
      @Override
      public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
        if (response.isSuccessful()) {
          Toast.makeText(CreateGISDataActivity.this, response.message(), Toast.LENGTH_SHORT).show();
        } else {
          Toast.makeText(CreateGISDataActivity.this, response.message(), Toast.LENGTH_SHORT).show();
        }
        onBackPressed();
      }

      @Override
      public void onFailure(Call<ResponseBody> call, Throwable t) {
        Toast.makeText(CreateGISDataActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
        call.cancel();
      }
    });
  }
}
