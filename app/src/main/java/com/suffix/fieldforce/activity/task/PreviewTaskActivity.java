package com.suffix.fieldforce.activity.task;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.suffix.fieldforce.R;
import com.suffix.fieldforce.activity.bill.AddBillActivity;
import com.suffix.fieldforce.model.AssignTaskItem;
import com.suffix.fieldforce.util.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
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
    @BindView(R.id.txtTicketStatus)
    TextView txtTicketStatus;
    @BindView(R.id.txtStatusTitle)
    TextView txtStatusTitle;
    @BindView(R.id.btnStartBill)
    Button btnStartBill;

    private String ticketId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview_task);
        ButterKnife.bind(this);

        setActionBar();

        if (getIntent().hasExtra("MODEL")) {
            AssignTaskItem assignTaskItem = getIntent().getParcelableExtra("MODEL");
            ticketTitle.setText(assignTaskItem.getTicketTitle());
            ticketCateroryTitle.setText(assignTaskItem.getTicketCateroryTitle());
            consumerName.setText(assignTaskItem.getConsumerName());
            deviceName.setText(assignTaskItem.getDeviceName());
            consumerAddress.setText(assignTaskItem.getConsumerAddress());
            consumerDistrict.setText(assignTaskItem.getConsumerDistrict());
            consumerThana.setText(assignTaskItem.getConsumerThana());
            consumerMobile.setText(assignTaskItem.getConsumerMobile());
            ticketStatusText.setText(assignTaskItem.getTicketStatusText());
            txtTicketStatus.setText(assignTaskItem.getTicketStatusText());
            ticketId = assignTaskItem.getTicketId();
        }

//        APIInterface apiInterface = APIClient.getApiClient().create(APIInterface.class);
//        SpannableString spannableString = new SpannableString(" START  " + startAddress);
//        Object bgGreenSpan = new BackgroundColorSpan(Color.parseColor("#2e7d32"));
//        Object clearSpan = new BackgroundColorSpan(Color.TRANSPARENT);
//        spannableString.setSpan(bgGreenSpan, 0, 7, 0);
//        spannableString.setSpan(clearSpan, 7, spannableString.length(), 0);
//        spannableString.setSpan(new ForegroundColorSpan(Color.WHITE), 0, 7, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//        holder.txtStartAddress.setText(spannableString);


    }

    private void setActionBar() {
        imgDrawer.setImageResource(R.drawable.ic_arrow_back);
        imgMap.setImageResource(R.drawable.ic_edit);
        toolBarTitle.setText("Preview Task");

    }

    @OnClick(R.id.imgMap)
    public void editStatus() {
        Intent intent = new Intent(PreviewTaskActivity.this, TaskEditActivity.class);
        intent.putExtra(Constants.TASK_ID, ticketId);
        startActivity(intent);
    }

    @OnClick(R.id.imgDrawer)
    public void back() {
        super.onBackPressed();
        finish();
    }

    @OnClick(R.id.btnStartBill)
    public void startBill() {
        if(ticketId != null) {
            Intent intent = new Intent(PreviewTaskActivity.this, AddBillActivity.class);
            intent.putExtra(Constants.TASK_ID, ticketId);
            startActivity(intent);
        }
    }
}
