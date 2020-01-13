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
import com.applandeo.materialcalendarview.listeners.OnCalendarPageChangeListener;
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

  private APIInterface apiInterface;
  private List<EventDay> events;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_roster_management);
    ButterKnife.bind(this);

    events = new ArrayList<>();
    apiInterface = APIClient.getApiClient().create(APIInterface.class);
    prepareRosterCalender(calendarView.getCurrentPageDate());

    calendarView.setOnPreviousPageChangeListener(new OnCalendarPageChangeListener() {
      @Override
      public void onChange() {
        prepareRosterCalender(calendarView.getCurrentPageDate());
      }
    });

    calendarView.setOnForwardPageChangeListener(new OnCalendarPageChangeListener() {
      @Override
      public void onChange() {
        prepareRosterCalender(calendarView.getCurrentPageDate());
      }
    });
  }

  private void prepareRosterCalender(Calendar currentDate) {

    String year = String.valueOf(currentDate.get(Calendar.YEAR));
    String month = String.valueOf(currentDate.get(Calendar.MONTH)+1);

    Call<RosterScheduleModel> call = apiInterface.getUserRosterSchedule(Constants.KEY,
        new FieldForcePreferences(this).getUser().getUserId(),
        "23.789847",
        "90.402321",
        year,
        month);

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
                events.clear();
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
        call.cancel();
      }
    });
  }
}
