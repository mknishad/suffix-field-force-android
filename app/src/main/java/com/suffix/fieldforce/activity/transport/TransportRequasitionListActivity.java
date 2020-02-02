package com.suffix.fieldforce.activity.transport;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.suffix.fieldforce.R;
import com.suffix.fieldforce.adapter.TransportRequasitionListAdapter;
import com.suffix.fieldforce.model.TransportListRequasition;
import com.suffix.fieldforce.model.TransportListRequasitionData;
import com.suffix.fieldforce.preference.FieldForcePreferences;
import com.suffix.fieldforce.retrofitapi.APIClient;
import com.suffix.fieldforce.retrofitapi.APIInterface;
import com.suffix.fieldforce.util.Constants;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TransportRequasitionListActivity extends AppCompatActivity {

  @BindView(R.id.recyclerView)
  RecyclerView recyclerView;
  @BindView(R.id.progressBar)
  ProgressBar progressBar;
  @BindView(R.id.imgMap)
  ImageView imgMap;
  @BindView(R.id.imgDrawer)
  ImageView imgDrawer;
  @BindView(R.id.toolBarTitle)
  TextView toolBarTitle;
  @BindView(R.id.buttonCreate)
  FloatingActionButton buttonCreate;

  private FieldForcePreferences preferences;
  private APIInterface apiInterface;

  private List<TransportListRequasitionData> transportListRequasitionData = new ArrayList<>();
  private TransportRequasitionListAdapter transportRequasitionListAdapter;

  @OnClick(R.id.imgDrawer)
  public void onViewClicked() {
    onBackPressed();
  }

  @OnClick(R.id.buttonCreate)
  public void createTR() {
    startActivity(new Intent(TransportRequasitionListActivity.this,CreateTransportRequasitionActivity.class));
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_transport_requasition_list);
    ButterKnife.bind(this);

    preferences = new FieldForcePreferences(this);
    apiInterface = APIClient.getApiClient().create(APIInterface.class);

    LinearLayoutManager manager = new LinearLayoutManager(this);
    recyclerView.setLayoutManager(manager);
    transportRequasitionListAdapter = new TransportRequasitionListAdapter(this, new ArrayList<>());

    progressBar.setVisibility(View.VISIBLE);

    getTransportRequasitionList();
  }

  @Override
  public void onBackPressed() {
    super.onBackPressed();
  }

  private void setActionBar() {
    imgDrawer.setImageResource(R.drawable.ic_arrow_back);
    imgMap.setImageResource(R.drawable.ic_edit);
    toolBarTitle.setText("Transport Requasition List");
    imgMap.setVisibility(View.GONE);
  }

  private void getTransportRequasitionList() {

    String userId = preferences.getUser().getUserId();

    Call<TransportListRequasition> getTransportRequisitionList = apiInterface.getTransportRequisitionList(
        Constants.KEY,
        userId,
        String.valueOf(preferences.getLocation().getLatitude()),
        String.valueOf(preferences.getLocation().getLongitude())
    );

    getTransportRequisitionList.enqueue(new Callback<TransportListRequasition>() {
      @Override
      public void onResponse(Call<TransportListRequasition> call, Response<TransportListRequasition> response) {

        progressBar.setVisibility(View.GONE);

        if (response.isSuccessful()) {
          TransportListRequasition transportListRequasition = response.body();
          transportListRequasitionData = transportListRequasition.getResponseData();
          transportRequasitionListAdapter.setData(transportListRequasitionData);
        }
      }

      @Override
      public void onFailure(Call<TransportListRequasition> call, Throwable t) {
        progressBar.setVisibility(View.GONE);
        Toast.makeText(TransportRequasitionListActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
        call.cancel();
      }
    });

  }

}
