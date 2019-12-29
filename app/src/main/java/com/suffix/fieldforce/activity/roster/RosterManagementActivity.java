package com.suffix.fieldforce.activity.roster;

import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.applandeo.materialcalendarview.CalendarUtils;
import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.EventDay;
import com.suffix.fieldforce.R;
import com.suffix.fieldforce.model.RosterScheduleDateModel;
import com.suffix.fieldforce.model.RosterScheduleModel;
import com.suffix.fieldforce.preference.FieldForcePreferences;
import com.suffix.fieldforce.retrofitapi.APIClient;
import com.suffix.fieldforce.retrofitapi.APIInterface;
import com.suffix.fieldforce.util.Constants;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RosterManagementActivity extends AppCompatActivity {

  @BindView(R.id.calendarView)
  CalendarView calendarView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_roster_management);
    ButterKnife.bind(this);

    APIInterface apiInterface = APIClient.getApiClient().create(APIInterface.class);
    Call<RosterScheduleModel> call = apiInterface.getUserRosterSchedule(Constants.KEY,
        new FieldForcePreferences(this).getUser().getUserId(),
        String.valueOf(new FieldForcePreferences(this).getLocation().getLatitude()),
        String.valueOf(new FieldForcePreferences(this).getLocation().getLongitude()),
        "2019",
        "11");
    call.enqueue(new Callback<RosterScheduleModel>() {
      @RequiresApi(api = Build.VERSION_CODES.O)
      @Override
      public void onResponse(Call<RosterScheduleModel> call, Response<RosterScheduleModel> response) {
        try {
          if (response.isSuccessful()) {
            RosterScheduleModel r = response.body();
            if (r.getResponseCode().equals("1")) {
              List<RosterScheduleDateModel> data = r.getResponseData();
              if (data.size() > 0) {
                List<EventDay> events = new ArrayList<>();
                for (RosterScheduleDateModel d : data) {
                  String stringDate = d.getCalendarDate();
                  String status = d.getStatus();

                  Drawable drawable = null;
                  Typeface typeface = Typeface.createFromAsset(RosterManagementActivity.this.getAssets(), "font/circulate.ttf");
                  if (status.toUpperCase().contains("R")) {
                    drawable = CalendarUtils.getDrawableText(RosterManagementActivity.this, status, typeface, R.color.color_green, 20);
                  } else if (status.toUpperCase().contains("D")) {
                    drawable = CalendarUtils.getDrawableText(RosterManagementActivity.this, status, typeface, R.color.colorFlowerDark, 20);
                  } else if (status.toUpperCase().contains("OFF")) {
                    drawable = CalendarUtils.getDrawableText(RosterManagementActivity.this, "O", typeface, R.color.dot_light_screen2, 20);
                  } else if (status.toUpperCase().contains("N")) {
                    drawable = CalendarUtils.getDrawableText(RosterManagementActivity.this, status, typeface, R.color.colorLavanderDark, 20);
                  }

                  SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                  Date date = null;
                  try {
                    date = formatter.parse(stringDate);
                  } catch (ParseException e) {
                    e.printStackTrace();
                  }
                  Calendar calender = Calendar.getInstance();
                  calender.setTime(date);

                  events.add(new EventDay(calender, drawable));
                }
                CalendarView calendarView = (CalendarView) findViewById(R.id.calendarView);
                calendarView.setEvents(events);
              }
            }
          }
        } catch (Exception e) {
          e.printStackTrace();
        }
      }

      @Override
      public void onFailure(Call<RosterScheduleModel> call, Throwable t) {

      }
    });

//    Calendar calendar = Calendar.getInstance();
//    events.add(new EventDay(calendar, R.drawable.ic_chat));
//or
    //events.add(new EventDay(calendar, new Drawable()));
//or if you want to specify event label color
    // events.add(new EventDay(calendar, R.drawable.sample_icon, Color.parseColor("#228B22")));


  }
}
