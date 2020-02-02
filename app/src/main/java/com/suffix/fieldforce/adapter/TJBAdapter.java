package com.suffix.fieldforce.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.suffix.fieldforce.R;
import com.suffix.fieldforce.model.TJBInfoData;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TJBAdapter extends RecyclerView.Adapter<TJBAdapter.ViewHolder> {

  private Context context;
  private List<TJBInfoData> tjbInfoDataObj = new ArrayList<TJBInfoData>();

  public TJBAdapter(Context context, List<TJBInfoData> tjbInfoDataObj) {
    this.context = context;
    this.tjbInfoDataObj = tjbInfoDataObj;
  }

  @NonNull
  @Override
  public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(context).inflate(R.layout.row_tjb, parent, false);
    return new ViewHolder(view);
  }

  @Override
  public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
    final TJBInfoData data = tjbInfoDataObj.get(position);

    holder.txtAddress.setText(data.getLocationName());
    holder.txtFCoreId.setText(data.getFromCoreId());
    holder.txtFCore.setText(data.getFromCore());
    holder.txtTCoreId.setText(data.getToCoreId());
    holder.txtTCore.setText(data.getToCore());

  }

  @Override
  public int getItemCount() {
    return tjbInfoDataObj.size();
  }

  public void setData(List<TJBInfoData> tjbInfoDataObj) {
    this.tjbInfoDataObj = tjbInfoDataObj;
    notifyDataSetChanged();
  }

  public class ViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.txtAddress)
    TextView txtAddress;
    @BindView(R.id.txtFCoreId)
    TextView txtFCoreId;
    @BindView(R.id.txtFCore)
    TextView txtFCore;
    @BindView(R.id.txtTCoreId)
    TextView txtTCoreId;
    @BindView(R.id.txtTCore)
    TextView txtTCore;

    public ViewHolder(@NonNull View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
    }
  }
}
