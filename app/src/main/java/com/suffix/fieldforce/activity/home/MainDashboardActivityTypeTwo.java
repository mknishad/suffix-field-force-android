package com.suffix.fieldforce.activity.home;

import android.os.Bundle;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.suffix.fieldforce.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainDashboardActivityTypeTwo extends AppCompatActivity {

  @BindView(R.id.layoutAttendance)
  LinearLayout layoutAttendance;
  @BindView(R.id.layoutExit)
  LinearLayout layoutExit;
  @BindView(R.id.layoutTask)
  LinearLayout layoutTask;
  @BindView(R.id.layoutRoster)
  LinearLayout layoutRoster;
  @BindView(R.id.layoutBilling)
  LinearLayout layoutBilling;
  @BindView(R.id.layoutInventory)
  LinearLayout layoutInventory;
  @BindView(R.id.layoutChat)
  LinearLayout layoutChat;
  @BindView(R.id.layoutSiteMap)
  LinearLayout layoutSiteMap;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main_dashboard_type_two);
    ButterKnife.bind(this);
  }
}
