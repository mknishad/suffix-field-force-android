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
import com.suffix.fieldforce.akg.model.product.CategoryModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProductCategoryListAdapter extends RecyclerView.Adapter<ProductCategoryListAdapter.ViewHolder> {

  private Context context;
  private List<CategoryModel> categoryModel = new ArrayList<>();
  private MemoListInterface memoListInterface;

  public ProductCategoryListAdapter(Context context, List<CategoryModel> categoryModel) {
    this.context = context;
    this.categoryModel = categoryModel;
  }

//  public void setMemoListInterface(MemoListInterface memoListInterface) {
//    this.memoListInterface = memoListInterface;
//  }

  @NonNull
  @Override
  public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(context).inflate(R.layout.row_item_sale, parent, false);
    return new ViewHolder(view);
  }

  @Override
  public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
    final CategoryModel model = categoryModel.get(position);

//    holder.layout.setOnClickListener(new View.OnClickListener() {
//      @Override
//      public void onClick(View v) {
//        if (memoListInterface != null) {
//          memoListInterface.onItemClick(position);
//        }
//      }
//    });
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

//    @BindView(R.id.layout)
//    RelativeLayout layout;
    @BindView(R.id.txtQtyOne)
    TextView txtQtyOne;
    @BindView(R.id.txtQtyTwo)
    TextView txtQtyTwo;
    @BindView(R.id.txtName)
    TextView txtName;

    public ViewHolder(@NonNull View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
    }
  }
}
