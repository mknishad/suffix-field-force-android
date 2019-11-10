package com.suffix.fieldforce.activity.home;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.suffix.fieldforce.R;
import com.suffix.fieldforce.activity.task.TaskDashboard;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class MainDashboard extends AppCompatActivity {

    @BindView(R.id.imgDrawer)
    ImageView imgDrawer;
    @BindView(R.id.imgActivation)
    ImageView imgActivation;
    @BindView(R.id.txtUserName)
    TextView txtUserName;
    @BindView(R.id.txtUserStatus)
    TextView txtUserStatus;
    @BindView(R.id.txtUserAddress)
    TextView txtUserAddress;
    @BindView(R.id.cardTask)
    CardView cardTask;
    @BindView(R.id.cardBills)
    CardView cardBills;
    @BindView(R.id.cardActivityLog)
    CardView cardActivityLog;
    @BindView(R.id.cardHistory)
    CardView cardHistory;
    @BindView(R.id.cardInventory)
    CardView cardInventory;
    @BindView(R.id.cardSiteMap)
    CardView cardSiteMap;
    @BindView(R.id.imgUserProfile)
    CircleImageView imgUserProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_dashboard);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.cardTask)
    public void openTask(){
        Intent intent = new Intent(MainDashboard.this, TaskDashboard.class);
        startActivity(intent);
    }

}
