package com.suffix.fieldforce.akg.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.suffix.fieldforce.R;
import com.suffix.fieldforce.akg.model.product.CartModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StockBodyListAdapter extends RecyclerView.Adapter<StockBodyListAdapter.ViewHolder> {

  private Context context;
  private List<CartModel> bodyData = new ArrayList<>();

  public StockBodyListAdapter(Context context, List<CartModel> bodyData) {
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
    final CartModel row = bodyData.get(position);
    holder.colOne.setText(String.valueOf(row.getProductCode()));
    holder.colTwo.setText(String.valueOf(row.getInHandQty()));
    holder.colThree.setText(String.valueOf(row.getSalesQty()));
  }

  @Override
  public int getItemCount() {
    return bodyData.size();
  }

  public void setData(List<CartModel> bodyData) {
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
