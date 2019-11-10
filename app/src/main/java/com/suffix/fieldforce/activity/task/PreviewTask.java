package com.suffix.fieldforce.activity.task;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.suffix.fieldforce.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PreviewTask extends AppCompatActivity {

    @BindView(R.id.imgMap)
    ImageView imgMap;
    @BindView(R.id.imgDrawer)
    ImageView imgDrawer;
    @BindView(R.id.txtCompanyName)
    TextView txtCompanyName;
    @BindView(R.id.txtIssueTitle)
    TextView txtIssueTitle;
    @BindView(R.id.txtHint)
    TextView txtHint;
    @BindView(R.id.imageProfile)
    ImageView imageProfile;
    @BindView(R.id.textName)
    TextView textName;
    @BindView(R.id.textDateTime)
    TextView textDateTime;
    @BindView(R.id.txtShortTitle)
    TextView txtShortTitle;
    @BindView(R.id.txtComments)
    TextView txtComments;
    @BindView(R.id.textView7)
    TextView textView7;
    @BindView(R.id.txtIssueType)
    TextView txtIssueType;
    @BindView(R.id.textView9)
    TextView textView9;
    @BindView(R.id.textView10)
    TextView textView10;
    @BindView(R.id.textView11)
    TextView textView11;
    @BindView(R.id.txtIssueStatus)
    TextView txtIssueStatus;
    @BindView(R.id.txtPriority)
    TextView txtPriority;
    @BindView(R.id.txtCategory)
    TextView txtCategory;
    @BindView(R.id.toolBarTitle)
    TextView toolBarTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview_task);
        ButterKnife.bind(this);

        setActionBar();
    }

    private void setActionBar() {
        imgDrawer.setImageResource(R.drawable.ic_arrow_back);
        imgMap.setImageResource(R.drawable.ic_edit);
        toolBarTitle.setText("Preview Task");
    }
}
