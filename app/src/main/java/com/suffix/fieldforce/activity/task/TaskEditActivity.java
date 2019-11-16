package com.suffix.fieldforce.activity.task;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.suffix.fieldforce.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TaskEditActivity extends AppCompatActivity {

    @BindView(R.id.imgMap)
    ImageView imgMap;
    @BindView(R.id.imgDrawer)
    ImageView imgDrawer;
    @BindView(R.id.spinnerStatus)
    Spinner spinnerStatus;
    @BindView(R.id.txtIssueRemark)
    EditText txtIssueRemark;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_edit);
        ButterKnife.bind(this);

        setActionBar();
    }

    private void setActionBar() {
        imgMap.setImageResource(R.drawable.ic_save);
        //igetActionBar().setTitle("Edit Task");
    }

    @OnClick(R.id.imgMap)
    public void onViewClicked() {
        super.onBackPressed();
    }
}
