package com.suffix.fieldforce.akg.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.suffix.fieldforce.R;
import com.suffix.fieldforce.akg.activity.MemoListInterface;
import com.suffix.fieldforce.akg.model.MemoListResponse;


import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MemoListAdapter extends RecyclerView.Adapter<MemoListAdapter.ViewHolder> {

  private Context context;
  private List<MemoListResponse> memoData = new ArrayList<>();
  private MemoListInterface memoListInterface;

  public MemoListAdapter(Context context, List<MemoListResponse> memoData) {
    this.context = context;
    this.memoData = memoData;
  }

  public void setMemoListInterface(MemoListInterface memoListInterface) {
    this.memoListInterface = memoListInterface;
  }

  @NonNull
  @Override
  public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(context).inflate(R.layout.row_memo_item, parent, false);
    return new ViewHolder(view);
  }

  @Override
  public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
    final MemoListResponse memo = memoData.get(position);
    holder.txtShopName.setText(memo.getConsumer().getCustomerName());
    holder.txtCode.setText(memo.getConsumer().getConsumerCode());
    holder.txtTotal.setText(memo.getTotalAmount().toString());

    holder.layout.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (memoListInterface != null) {
          memoListInterface.onItemClick(position);
        }
      }
    });
  }

  @Override
  public int getItemCount() {
    return memoData.size();
  }

  public void setData(List<MemoListResponse> memoData) {
    this.memoData = memoData;
    notifyDataSetChanged();
  }

  public class ViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.layout)
    RelativeLayout layout;
    @BindView(R.id.txtShopName)
    TextView txtShopName;
    @BindView(R.id.txtCode)
    TextView txtCode;
    @BindView(R.id.txtTotal)
    TextView txtTotal;

    public ViewHolder(@NonNull View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
    }
  }
}
