package com.suffix.fieldforce.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.suffix.fieldforce.R;
import com.suffix.fieldforce.model.TransportListRequasitionData;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TransportRequasitionListAdapter extends RecyclerView.Adapter<TransportRequasitionListAdapter.ViewHolder> {

  private Context context;
  private List<TransportListRequasitionData> transportListRequasitionData = new ArrayList<>();

  public TransportRequasitionListAdapter(Context context, List<TransportListRequasitionData> transportListRequasitionData) {
    this.context = context;
    this.transportListRequasitionData = transportListRequasitionData;
  }

  @NonNull
  @Override
  public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(context).inflate(R.layout.row_transport_requasition, parent, false);
    return new ViewHolder(view);
  }

  @Override
  public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
    final TransportListRequasitionData data = transportListRequasitionData.get(position);
  }

  @Override
  public int getItemCount() {
    return transportListRequasitionData.size();
  }

  public void setData(List<TransportListRequasitionData> transportListRequasitionData){
    this.transportListRequasitionData = transportListRequasitionData;
    notifyDataSetChanged();
  }

  public class ViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.txtTransportRequasitionStatus)
    TextView txtTransportRequasitionStatus;
    @BindView(R.id.txtId)
    TextView txtId;
    @BindView(R.id.txtStartDateTime)
    TextView txtStartDateTime;
    @BindView(R.id.txtEndDateTime)
    TextView txtEndDateTime;
    @BindView(R.id.layoutItem)
    RelativeLayout layoutItem;

    public ViewHolder(@NonNull View itemView) {
      super(itemView);
      ButterKnife.bind(this,itemView);
    }
  }
}
