package com.suffix.fieldforce.akg.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.suffix.fieldforce.R;
import com.suffix.fieldforce.akg.model.CustomerData;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CustomerListAdapter extends RecyclerView.Adapter<CustomerListAdapter.ViewHolder> {

  private Context context;
  private List<CustomerData> customerDataList;
  private CustomerListInterface customerListInterface;

  public CustomerListAdapter(Context context, List<CustomerData> customerData) {
    this.context = context;
    this.customerDataList = customerData;
  }

  public void setCustomerListInterface(CustomerListInterface listener) {
    this.customerListInterface = listener;
  }

  @NonNull
  @Override
  public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(context).inflate(R.layout.spinner_item, parent, false);
    return new ViewHolder(view);
  }

  @Override
  public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
    final CustomerData customerData = customerDataList.get(position);

    holder.txtName.setText(customerData.getCustomerName());
    holder.txtAddress.setText(customerData.getAddress());
    holder.layout.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        customerListInterface.onItemClick(position, customerData);
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

    @BindView(R.id.txtName)
    TextView txtName;
    @BindView(R.id.txtAddress)
    TextView txtAddress;
    @BindView(R.id.layout)
    LinearLayout layout;


    public ViewHolder(@NonNull View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
    }
  }
}