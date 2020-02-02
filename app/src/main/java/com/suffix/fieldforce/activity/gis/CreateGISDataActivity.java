package com.suffix.fieldforce.activity.gis;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.suffix.fieldforce.R;
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
  @BindView(R.id.btnAddTJB)
  TextView btnAddTJB;
  @BindView(R.id.btnSubmit)
  TextView btnSubmit;

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
      }
    });
    dialog.show(getSupportFragmentManager(), "TJB");
  }

  @OnClick(R.id.btnSubmit)
  public void submit() {

    linkName = txtLinkName.getText().toString();
    siteAAddress = txtLinkName.getText().toString();
    siteBAddress = txtLinkName.getText().toString();
    siteALat = "23.811616";
    siteALng = "90.4135393";
    siteBLat = "23.811616";
    siteBLng = "90.4135393";
    tjbInfoData = new Gson().toJson(tjbInfoDataObj);;
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
        if(response.isSuccessful()){
          Toast.makeText(CreateGISDataActivity.this, response.message(), Toast.LENGTH_SHORT).show();
        }else {
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
