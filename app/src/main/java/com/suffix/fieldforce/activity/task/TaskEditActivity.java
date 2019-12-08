package com.suffix.fieldforce.activity.task;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.suffix.fieldforce.R;
import com.suffix.fieldforce.model.Ticketstatus;
import com.suffix.fieldforce.preference.FieldForcePreferences;
import com.suffix.fieldforce.retrofitapi.APIClient;
import com.suffix.fieldforce.retrofitapi.APIInterface;
import com.suffix.fieldforce.util.Constants;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TaskEditActivity extends AppCompatActivity {

    private static final String TAG = "TaskEditActivity";

    @BindView(R.id.imgMap)
    ImageView imgMap;
    @BindView(R.id.imgDrawer)
    ImageView imgDrawer;
    @BindView(R.id.spinnerStatus)
    Spinner spinnerStatus;
    @BindView(R.id.txtIssueRemark)
    EditText txtIssueRemark;

    private String ticketId;
    private FieldForcePreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_edit);
        preferences = new FieldForcePreferences(TaskEditActivity.this);
        ButterKnife.bind(this);

        setActionBar();

        ticketId = getIntent().getStringExtra(Constants.INSTANCE.getTASK_ID());
    }

    private void setActionBar() {
        imgMap.setImageResource(R.drawable.ic_save);
        //igetActionBar().setTitle("Edit Task");
    }

    @OnClick(R.id.imgMap)
    public void onViewClic() {

        Log.wtf(TAG, "lat = " + preferences.getLocation().getLatitude() + " lng = " + preferences.getLocation().getLongitude());

        APIInterface apiInterface = APIClient.getApiClient().create(APIInterface.class);
        Call<List<Ticketstatus>> ticketStatus = null;
        switch (spinnerStatus.getSelectedItem().toString()) {
            case "Open":
                ticketStatus = apiInterface.ticketOpenInfo(Constants.INSTANCE.getUSER_ID(),
                        ticketId, txtIssueRemark.getText().toString(), String.valueOf(preferences.getLocation().getLatitude()), String.valueOf(preferences.getLocation().getLongitude()));
                break;
            case "In Progress":
                ticketStatus = apiInterface.ticketInprogressInfo(Constants.INSTANCE.getUSER_ID(),
                        ticketId, txtIssueRemark.getText().toString(),
                        String.valueOf(preferences.getLocation().getLatitude()),
                        String.valueOf(preferences.getLocation().getLongitude()));
                break;
            case "Close":
                ticketStatus = apiInterface.ticketCloseInfo(Constants.INSTANCE.getUSER_ID(),
                        ticketId, txtIssueRemark.getText().toString(),
                        String.valueOf(preferences.getLocation().getLatitude()),
                        String.valueOf(preferences.getLocation().getLongitude()));
                break;
        }

        ticketStatus.enqueue(new Callback<List<Ticketstatus>>() {
            @Override
            public void onResponse(Call<List<Ticketstatus>> call, Response<List<Ticketstatus>> response) {
                startActivity(new Intent(TaskEditActivity.this, TaskDashboard.class));
            }

            @Override
            public void onFailure(Call<List<Ticketstatus>> call, Throwable t) {

            }
        });

        super.onBackPressed();
        finish();
    }
}
