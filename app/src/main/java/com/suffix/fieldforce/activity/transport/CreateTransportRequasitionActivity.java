package com.suffix.fieldforce.activity.transport;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.suffix.fieldforce.R;
import com.suffix.fieldforce.model.Project;
import com.suffix.fieldforce.model.ProjectData;
import com.suffix.fieldforce.preference.FieldForcePreferences;
import com.suffix.fieldforce.retrofitapi.APIClient;
import com.suffix.fieldforce.retrofitapi.APIInterface;
import com.suffix.fieldforce.util.Constants;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateTransportRequasitionActivity extends AppCompatActivity {

  @BindView(R.id.imgMap)
  ImageView imgMap;
  @BindView(R.id.imgDrawer)
  ImageView imgDrawer;
  @BindView(R.id.toolBarTitle)
  TextView toolBarTitle;
  @BindView(R.id.project)
  AutoCompleteTextView project;
  @BindView(R.id.checkboxRegMaintenance)
  CheckBox checkboxRegMaintenance;
  @BindView(R.id.checkboxSurvey)
  CheckBox checkboxSurvey;
  @BindView(R.id.checkboxClientConnectivity)
  CheckBox checkboxClientConnectivity;
  @BindView(R.id.checkboxImplementation)
  CheckBox checkboxImplementation;
  @BindView(R.id.startDate)
  TextInputEditText startDate;
  @BindView(R.id.startTime)
  TextInputEditText startTime;
  @BindView(R.id.endDate)
  TextInputEditText endDate;
  @BindView(R.id.endTime)
  TextInputEditText endTime;
  @BindView(R.id.remarks)
  TextInputEditText remarks;
  @BindView(R.id.destination)
  TextInputEditText destination;
  @BindView(R.id.progressBar)
  ProgressBar progressBar;
  @BindView(R.id.btnSubmit)
  TextView btnSubmit;

  private FieldForcePreferences preferences;
  private APIInterface apiInterface;
  private final Calendar myCalendar = Calendar.getInstance();
  private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yy-MM-dd");

  private List<ProjectData> projectDatas = new ArrayList<ProjectData>();

  private String strProjectId = null;
  private String strRegMaintenance = null;
  private String strSurvey = null;
  private String strClientConnectivity = null;
  private String strImplementation = null;
  private String strStartTime = null;
  private String strEndTime = null;
  private String strDestination = null;
  private String strRemark = null;

  @OnClick(R.id.imgDrawer)
  public void back() {
    onBackPressed();
  }

  @OnClick(R.id.startDate)
  public void startDate() {
    DatePickerDialog datePickerDialog = new DatePickerDialog(this, startDateListener, myCalendar
        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
        myCalendar.get(Calendar.DAY_OF_MONTH));

    datePickerDialog.setTitle("Select Start Date");
    datePickerDialog.show();
  }

  @OnClick(R.id.endDate)
  public void endDate() {
    DatePickerDialog datePickerDialog = new DatePickerDialog(this, endDateListener, myCalendar
        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
        myCalendar.get(Calendar.DAY_OF_MONTH));

    datePickerDialog.setTitle("Select End Date");
    datePickerDialog.show();
  }


  @OnClick(R.id.startTime)
  public void startTime() {
    Calendar mcurrentTime = Calendar.getInstance();
    int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
    int minute = mcurrentTime.get(Calendar.MINUTE);
    TimePickerDialog mTimePicker;
    mTimePicker = new TimePickerDialog(CreateTransportRequasitionActivity.this, new TimePickerDialog.OnTimeSetListener() {
      @Override
      public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
        String HH = String.valueOf(selectedHour);
        String MM = String.valueOf(selectedMinute);
        if (selectedHour < 10) {
          HH = "0" + HH;
        }
        if (selectedMinute < 10) {
          MM = "0" + MM;
        }
        startTime.setText(HH + ":" + MM + ":00");
      }
    }, hour, minute, true);//Yes 24 hour time
    mTimePicker.setTitle("Select Time");
    mTimePicker.show();
  }

  @OnClick(R.id.endTime)
  public void endTime() {
    Calendar mcurrentTime = Calendar.getInstance();
    int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
    int minute = mcurrentTime.get(Calendar.MINUTE);
    TimePickerDialog mTimePicker;
    mTimePicker = new TimePickerDialog(CreateTransportRequasitionActivity.this, new TimePickerDialog.OnTimeSetListener() {
      @Override
      public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
        String HH = String.valueOf(selectedHour);
        String MM = String.valueOf(selectedMinute);
        if (selectedHour < 10) {
          HH = "0" + HH;
        }
        if (selectedMinute < 10) {
          MM = "0" + MM;
        }
        endTime.setText(HH + ":" + MM + ":00");
      }
    }, hour, minute, true);//Yes 24 hour time
    mTimePicker.setTitle("Select Time");
    mTimePicker.show();
  }

  @OnClick(R.id.btnSubmit)
  public void submit() {
    strRegMaintenance = (checkboxRegMaintenance.isChecked() ? "1" : "0");
    strSurvey = (checkboxSurvey.isChecked() ? "1" : "0");
    strClientConnectivity = (checkboxClientConnectivity.isChecked() ? "1" : "0");
    strImplementation = (checkboxImplementation.isChecked() ? "1" : "0");
    strStartTime = startDate.getText().toString() + " " + startTime.getText().toString();
    strEndTime = endDate.getText().toString() + " " + endTime.getText().toString();
    strDestination = destination.getText().toString();
    strRemark = remarks.getText().toString();

    createRequasition();
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_transport_requasition);
    ButterKnife.bind(this);
    setActionBar();

    preferences = new FieldForcePreferences(this);
    apiInterface = APIClient.getApiClient().create(APIInterface.class);
    project.setAdapter(null);
    progressBar.setVisibility(View.VISIBLE);
    getProject();
  }

  @Override
  protected void onPause() {
    if(progressBar != null && progressBar.getVisibility() == View.VISIBLE) {
      progressBar.setVisibility(View.GONE);
    }
    super.onPause();
  }

  @Override
  protected void onPostResume() {
    super.onPostResume();
  }

  @Override
  public void onBackPressed() {
    super.onBackPressed();
  }

  private void setActionBar() {
    imgDrawer.setImageResource(R.drawable.ic_arrow_back);
    imgMap.setImageResource(R.drawable.ic_edit);
    toolBarTitle.setText("Transport Requasition");
    imgMap.setVisibility(View.GONE);
  }

  DatePickerDialog.OnDateSetListener startDateListener = new DatePickerDialog.OnDateSetListener() {

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear,
                          int dayOfMonth) {
      myCalendar.set(Calendar.YEAR, year);
      myCalendar.set(Calendar.MONTH, monthOfYear);
      myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

      String currentDateTime = simpleDateFormat.format(myCalendar.getTime());
      startDate.setText(currentDateTime);
    }

  };

  DatePickerDialog.OnDateSetListener endDateListener = new DatePickerDialog.OnDateSetListener() {

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear,
                          int dayOfMonth) {
      myCalendar.set(Calendar.YEAR, year);
      myCalendar.set(Calendar.MONTH, monthOfYear);
      myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

      String currentDateTime = simpleDateFormat.format(myCalendar.getTime());
      endDate.setText(currentDateTime);
    }

  };

  private void getProject() {

    String userId = preferences.getUser().getUserId();

    Call<Project> getProjectList = apiInterface.getProjectList(
        Constants.KEY,
        userId,
        String.valueOf(preferences.getLocation().getLatitude()),
        String.valueOf(preferences.getLocation().getLongitude())
    );

    getProjectList.enqueue(new Callback<Project>() {
      @Override
      public void onResponse(Call<Project> call, Response<Project> response) {
        if(progressBar != null && progressBar.getVisibility() == View.VISIBLE) {
          progressBar.setVisibility(View.GONE);
        }
        if (response.isSuccessful()) {
          projectDatas = response.body().responseData;

          String[] projectName = new String[projectDatas.size()];

          for (int i = 0; i < projectDatas.size(); i++) {
            projectName[i] = projectDatas.get(i).projectTitle;
          }

          project.setAdapter(new ArrayAdapter<>(
              getApplicationContext(),
              R.layout.dropdown_menu_popup_item,
              projectName));

          project.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
              project.showDropDown();
              return false;
            }
          });

          project.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
              strProjectId = projectDatas.get(position).getProjectId();
            }
          });

        }
      }

      @Override
      public void onFailure(Call<Project> call, Throwable t) {
        if(progressBar != null && progressBar.getVisibility() == View.VISIBLE) {
          progressBar.setVisibility(View.GONE);
        }
        Toast.makeText(CreateTransportRequasitionActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
        project.setAdapter(null);
        call.cancel();
      }
    });

  }

  private void createRequasition() {

    String userId = preferences.getUser().getUserId();

    Call<ResponseBody> postTransportRequisition = apiInterface.postTransportRequisition(
        Constants.KEY,
        userId,
        String.valueOf(preferences.getLocation().getLatitude()),
        String.valueOf(preferences.getLocation().getLongitude()),
        strProjectId,
        strRegMaintenance,
        strSurvey,
        strClientConnectivity,
        strImplementation,
        strStartTime,
        strEndTime,
        strDestination,
        strRemark
    );

    postTransportRequisition.enqueue(new Callback<ResponseBody>() {
      @Override
      public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
        if (response.isSuccessful()) {
          Toast.makeText(CreateTransportRequasitionActivity.this, "Transport Requasition Addedd Successfully", Toast.LENGTH_SHORT).show();
          back();
        }
      }

      @Override
      public void onFailure(Call<ResponseBody> call, Throwable t) {
        Toast.makeText(CreateTransportRequasitionActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
        call.cancel();
      }
    });

  }
}
