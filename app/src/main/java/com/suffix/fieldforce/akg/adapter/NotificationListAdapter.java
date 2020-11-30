package com.suffix.fieldforce.akg.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.suffix.fieldforce.R;
import com.suffix.fieldforce.akg.model.CustomerData;
import com.suffix.fieldforce.akg.model.MemoListResponse;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NotificationListAdapter extends RecyclerView.Adapter<NotificationListAdapter.ViewHolder> {

  private Context context;
  private List<CustomerData> customerDataList = new ArrayList<>();
  private NotificationListInterface notificationListInterface;

  public NotificationListAdapter(Context context, List<CustomerData> customerData) {
    this.context = context;
    this.customerDataList = customerData;
  }

  public void setMemoListInterface(NotificationListInterface listener) {
    this.notificationListInterface = listener;
  }

  @NonNull
  @Override
  public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(context).inflate(R.layout.row_user_notification, parent, false);
    return new ViewHolder(view);
  }

  @Override
  public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
    final CustomerData customerData = customerDataList.get(position);

    holder.txtUsername.setText(customerData.getCustomerName());
    holder.txtStorename.setText(customerData.getConsumerCode());
    holder.txtDateTime.setText(customerData.getMobileNo());
    holder.txtAddress.setText(customerData.getStatus());
    holder.txtUsername.setText(customerData.getTradeLicenseNo());

    holder.btnActive.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (notificationListInterface != null) {
          notificationListInterface.onItemClick(position);
        }
      }
    });
  }

  @Override
  public int getItemCount() {
    return customerDataList.size();
  }

  public void setData(List<CustomerData> customerData) {
    this.customerDataList = customerData;
    notifyDataSetChanged();
  }

  public class ViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.txtUsername)
    TextView txtUsername;
    @BindView(R.id.txtStorename)
    TextView txtStorename;
    @BindView(R.id.txtDateTime)
    TextView txtDateTime;
    @BindView(R.id.txtAddress)
    TextView txtAddress;
    @BindView(R.id.btnActive)
    Button btnActive;
    @BindView(R.id.imgCigar)
    SimpleDraweeView imgCigar;


    public ViewHolder(@NonNull View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
    }
  }
}