package com.suffix.fieldforce.activity.task;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.suffix.fieldforce.R;
import com.suffix.fieldforce.fragment.DashboardFragment;
import com.suffix.fieldforce.fragment.NotificationsFragment;
import com.suffix.fieldforce.fragment.ProjectsFragment;
import butterknife.BindView;
import butterknife.ButterKnife;

public class TaskDashboard extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.frame_container)
    FrameLayout frameContainer;
    @BindView(R.id.bottomNavigation)
    BottomNavigationView bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_dashboard_type_two);
        ButterKnife.bind(this);

        bottomNavigation.setOnNavigationItemSelectedListener(this);
        replaceDummyFragment(new DashboardFragment());
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
}
