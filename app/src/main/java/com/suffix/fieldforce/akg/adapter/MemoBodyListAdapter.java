package com.suffix.fieldforce.akg.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.suffix.fieldforce.R;
import com.suffix.fieldforce.akg.model.InvoiceDetail;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MemoBodyListAdapter extends RecyclerView.Adapter<MemoBodyListAdapter.ViewHolder> {

  private Context context;
  private List<InvoiceDetail> bodyData = new ArrayList<>();

  public MemoBodyListAdapter(Context context, List<InvoiceDetail> bodyData) {
    this.context = context;
    this.bodyData = bodyData;
  }

  @NonNull
  @Override
  public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(context).inflate(R.layout.row_table_item, parent, false);
    return new ViewHolder(view);
  }

  @Override
  public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
    final InvoiceDetail row = bodyData.get(position);
    holder.colOne.setText(row.getProductCode());
    holder.colTwo.setText(row.getQuantity().toString());
    holder.colThree.setText(row.getAmount().toString());
  }

  @Override
  public int getItemCount() {
    return bodyData.size();
  }

  public void setData(List<InvoiceDetail> bodyData) {
    this.bodyData = bodyData;
    notifyDataSetChanged();
  }

  public class ViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.colOne)
    TextView colOne;
    @BindView(R.id.colTwo)
    TextView colTwo;
    @BindView(R.id.colThree)
    TextView colThree;

    public ViewHolder(@NonNull View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
    }
  }
}
