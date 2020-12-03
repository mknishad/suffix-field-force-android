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

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.RealmResults;

public class CategoryListAdapter extends RecyclerView.Adapter<CategoryListAdapter.ViewHolder> {

  private Context context;
  private RealmResults<CartModel> bodyData;

  public CategoryListAdapter(Context context, RealmResults<CartModel> bodyData) {
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
    holder.colOne.setText(row.getProductCode());
    holder.colTwo.setText(row.getOrderQuantity());
    holder.colThree.setText(String.format(Locale.getDefault(), "%.2f",
        Integer.parseInt(row.getOrderQuantity()) * row.getSellingRate()));
  }

  @Override
  public int getItemCount() {
    return bodyData.size();
  }

  public void setData(RealmResults<CartModel> bodyData) {
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
