package com.suffix.fieldforce.activity.roster;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.EventDay;
import com.suffix.fieldforce.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RosterManagementActivity extends AppCompatActivity {

  @BindView(R.id.calendarView)
  CalendarView calendarView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_roster_management);
    ButterKnife.bind(this);

    List<EventDay> events = new ArrayList<>();

    Calendar calendar = Calendar.getInstance();
    events.add(new EventDay(calendar, R.drawable.ic_chat));
//or
    //events.add(new EventDay(calendar, new Drawable()));
//or if you want to specify event label color
   // events.add(new EventDay(calendar, R.drawable.sample_icon, Color.parseColor("#228B22")));

    CalendarView calendarView = (CalendarView) findViewById(R.id.calendarView);
    calendarView.setEvents(events);
  }
}
