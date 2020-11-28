package com.suffix.fieldforce.activity.abul.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.suffix.fieldforce.R;

import java.util.List;

public class SalesAdapter extends RecyclerView.Adapter<SalesAdapter.ViewHolder> {

  public Context mContext;
  public List<String> salesData;

  public SalesAdapter(Context mContext, List<String> salesData) {
    this.mContext = mContext;
    this.salesData = salesData;
  }

  @NonNull
  @Override
  public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(mContext).inflate(R.layout.row_user_notification,parent,false);
    return new ViewHolder(view);
  }

  @Override
  public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
      final String singleData = salesData.get(position);
  }

  @Override
  public int getItemCount() {
    return salesData.size();
  }

  public class ViewHolder extends RecyclerView.ViewHolder{

    public ViewHolder(@NonNull View itemView) {
      super(itemView);
    }
  }
}
