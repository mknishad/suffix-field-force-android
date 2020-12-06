package com.suffix.fieldforce.akg.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.suffix.fieldforce.R;
import com.suffix.fieldforce.akg.activity.QuantityActivity;
import com.suffix.fieldforce.akg.model.product.CategoryModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProductCategoryListAdapter extends RecyclerView.Adapter<ProductCategoryListAdapter.ViewHolder> {

  private Context context;
  private List<CategoryModel> categoryModel = new ArrayList<>();
  private ProductCategoryListInterface productCategoryListInterface;

  public ProductCategoryListAdapter(Context context, List<CategoryModel> categoryModel) {
    this.context = context;
    this.categoryModel = categoryModel;
  }

  public void setProductCategoryListInterface(ProductCategoryListInterface productCategoryListInterface) {
    this.productCategoryListInterface = productCategoryListInterface;
  }

  @NonNull
  @Override
  public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(context).inflate(R.layout.row_item_sale, parent, false);
    return new ViewHolder(view);
  }

  @Override
  public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
    final CategoryModel model = categoryModel.get(position);

    String imageURL = "http://51.79.73.162:8956/salesapi" + model.getProductImage();

    holder.txtInHandQt.setText(model.getInHandQty().toString());
    holder.txtSalesQt.setText(model.getSalesQty().toString());
    holder.txtTotalMemo.setText(model.getTotalMemo().toString());
    holder.txtProductCode.setText(model.getProductCode());
    holder.imgCigar.setImageURI(Uri.parse(imageURL));

    holder.layoutRow.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if((model.getInHandQty() - model.getSalesQty()) == 0){
          Toast.makeText(context, "স্টক শেষ হয়েছে", Toast.LENGTH_SHORT).show();
        }else {
          Intent intent = new Intent(context, QuantityActivity.class);
          intent.putExtra("CATEGORY_MODEL", model);
          context.startActivity(intent);
        }
      }
    });
  }

  @Override
  public int getItemCount() {
    return categoryModel.size();
  }

  public void setData(List<CategoryModel> categoryModel) {
    this.categoryModel = categoryModel;
    notifyDataSetChanged();
  }

  public class ViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.layoutRow)
    LinearLayout layoutRow;
    @BindView(R.id.imgCigar)
    SimpleDraweeView imgCigar;
    @BindView(R.id.txtInHandQt)
    TextView txtInHandQt;
    @BindView(R.id.txtSalesQt)
    TextView txtSalesQt;
    @BindView(R.id.txtTotalMemo)
    TextView txtTotalMemo;
    @BindView(R.id.txtProductCode)
    TextView txtProductCode;

    public ViewHolder(@NonNull View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
    }
  }
}
