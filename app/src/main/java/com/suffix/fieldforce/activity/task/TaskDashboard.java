package com.suffix.fieldforce.activity.task;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.suffix.fieldforce.R;
import com.suffix.fieldforce.activity.home.MainDashboard;
import com.suffix.fieldforce.fragment.DashboardFragment;
import com.suffix.fieldforce.fragment.NotificationsFragment;
import com.suffix.fieldforce.fragment.ProjectsFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TaskDashboard extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.frame_container)
    FrameLayout frameContainer;
    @BindView(R.id.bottomNavigation)
    BottomNavigationView bottomNavigation;
    @BindView(R.id.imgMap)
    ImageView imgMap;
    @BindView(R.id.imgDrawer)
    ImageView imgDrawer;
    @BindView(R.id.toolBarTitle)
    TextView toolBarTitle;
    @BindView(R.id.buttonCreateTask)
    FloatingActionButton buttonCreateTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_dashboard_type_two);
        ButterKnife.bind(this);

        setActionBar();

        bottomNavigation.setOnNavigationItemSelectedListener(this);
        replaceDummyFragment(new DashboardFragment());
    }

    private void setActionBar() {
        imgDrawer.setImageResource(R.drawable.ic_arrow_back);
        imgDrawer.setImageResource(R.drawable.ic_arrow_back);
        imgMap.setVisibility(View.GONE);
        imgDrawer.setVisibility(View.VISIBLE);
        toolBarTitle.setText("TASK LIST");
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        Fragment fragment;

        if (menuItem.isChecked())
            menuItem.setChecked(false);
        switch (menuItem.getItemId()) {
            case R.id.action_home:
                menuItem.setChecked(true);
                fragment = new DashboardFragment();
                replaceDummyFragment(fragment);
                break;
            case R.id.action_projects:
                menuItem.setChecked(true);
                fragment = new ProjectsFragment();
                replaceDummyFragment(fragment);
                break;
            case R.id.action_notifications:
                menuItem.setChecked(true);
                fragment = new NotificationsFragment();
                replaceDummyFragment(fragment);
                break;
            case R.id.action_activity:
                menuItem.setChecked(true);
                //fragment = new ActivityFragment();
                //replaceDummyFragment(fragment);
                break;
            default:
                break;

        }
        return true;
    }

    private void replaceDummyFragment(@NonNull Fragment fragment) {
        Bundle b = new Bundle();
        fragment.setArguments(b);
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, fragment).commit();
    }

    @OnClick(R.id.imgDrawer)
    public void onBackClicked() {
        super.onBackPressed();
    }

    @OnClick(R.id.buttonCreateTask)
    public void onCreateTaskClicked() {
        startActivity(new Intent(TaskDashboard.this,CreateTaskActivity.class));
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(TaskDashboard.this, MainDashboard.class));
        finish();
    }
}
