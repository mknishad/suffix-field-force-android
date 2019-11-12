package com.suffix.fieldforce.activity.task;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.suffix.fieldforce.R;
import com.suffix.fieldforce.model.AssignTaskItem;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class PreviewTaskActivity extends AppCompatActivity {

    @BindView(R.id.imgMap)
    ImageView imgMap;
    @BindView(R.id.imgDrawer)
    ImageView imgDrawer;
    @BindView(R.id.toolBarTitle)
    TextView toolBarTitle;
    @BindView(R.id.ticketTitle)
    TextView ticketTitle;
    @BindView(R.id.profileImage)
    CircleImageView profileImage;
    @BindView(R.id.consumerName)
    TextView consumerName;
    @BindView(R.id.consumerMobile)
    TextView consumerMobile;
    @BindView(R.id.consumerAddress)
    TextView consumerAddress;
    @BindView(R.id.consumerThana)
    TextView consumerThana;
    @BindView(R.id.consumerDistrict)
    TextView consumerDistrict;
    @BindView(R.id.orderId)
    TextView orderId;
    @BindView(R.id.deviceName)
    TextView deviceName;
    @BindView(R.id.ticketCateroryTitle)
    TextView ticketCateroryTitle;
    @BindView(R.id.ticketStatusText)
    TextView ticketStatusText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview_task);
        ButterKnife.bind(this);

        setActionBar();

        if (getIntent().hasExtra("MODEL")) {
            AssignTaskItem assignTaskItem = getIntent().getParcelableExtra("MODEL");
//            txtCompanyName.setText(assignTaskItem.getTicketCode());
//            txtIssueTitle.setText(assignTaskItem.getTicketTitle());
//            txtHint.setText(assignTaskItem.getTicketCateroryTitle());
//            textName.setText(assignTaskItem.getConsumerName());
//            textDateTime.setText("Mobile : " + assignTaskItem.getConsumerMobile() + "\nStart : " + assignTaskItem.getTicketStartDate() + "\n" + "End : " + assignTaskItem.getTicketEndDate());
//            txtShortTitle.setText(assignTaskItem.getDeviceName());
//            txtPriority.setText(assignTaskItem.getTicketCateroryTitle());
//            txtCategory.setText(assignTaskItem.getTicketRemark());
//            txtComments.setText("Caterory Code : " + assignTaskItem.getTicketCateroryCode() + "\n");
        }
    }

    private void setActionBar() {
        imgDrawer.setImageResource(R.drawable.ic_arrow_back);
        imgMap.setImageResource(R.drawable.ic_edit);
        toolBarTitle.setText("Preview Task");
    }
}
