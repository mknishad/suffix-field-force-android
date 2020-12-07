package com.suffix.fieldforce.akg.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.suffix.fieldforce.R;
import com.suffix.fieldforce.akg.database.manager.SyncManager;
import com.suffix.fieldforce.akg.model.InvoiceProduct;
import com.suffix.fieldforce.dialog.TJBDialog;
import com.suffix.fieldforce.dialog.TJBDialogListener;

import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MemoBodyListAdapter extends RecyclerView.Adapter<MemoBodyListAdapter.ViewHolder> {

  private Context context;
  private List<InvoiceProduct> bodyData;
  private String invoiceId;
  private ProductUpdateListener updateInterface;

  public MemoBodyListAdapter(Context context, List<InvoiceProduct> bodyData, String invoiceId,
                             ProductUpdateListener updateInterface) {
    this.context = context;
    this.bodyData = bodyData;
    this.invoiceId = invoiceId;
    this.updateInterface = updateInterface;
  }

  @NonNull
  @Override
  public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(context).inflate(R.layout.row_table_item, parent, false);
    return new ViewHolder(view);
  }

  @Override
  public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
    final InvoiceProduct row = bodyData.get(position);
    holder.colOne.setText(String.valueOf(row.getProductCode()));
    holder.colTwo.setText(String.valueOf(row.getProductQty()));
    holder.colThree.setText(String.format(Locale.getDefault(), "%.2f", row.getSubToalAmount()));

    holder.layoutRow.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        TJBDialog dialog = new TJBDialog();
        dialog.setTjbDialogListener(new TJBDialogListener() {
          @Override
          public void onSubmit(String quantity) {
            if (TextUtils.isEmpty(quantity)) {
              quantity = "0";
            }
            int updateQty = Integer.parseInt(quantity) - row.getProductQty();
            new SyncManager(context).updateSingleInvoice(invoiceId, row, updateQty);
            updateInterface.onSuccess();
          }
        });
        dialog.show(((AppCompatActivity) context).getSupportFragmentManager(), "TJB");
      }
    });

  }

  @Override
  public int getItemCount() {
    return bodyData.size();
  }

  public void setData(List<InvoiceProduct> bodyData) {
    this.bodyData = bodyData;
    notifyDataSetChanged();
  }

  public class ViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.layoutRow)
    LinearLayout layoutRow;
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
