package com.suffix.fieldforce.akg.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.suffix.fieldforce.BuildConfig;
import com.suffix.fieldforce.R;
import com.suffix.fieldforce.akg.activity.QuantityActivity;
import com.suffix.fieldforce.akg.model.product.GiftModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GiftListAdapter extends RecyclerView.Adapter<GiftListAdapter.ViewHolder> {

  private Context context;
  private List<GiftModel> giftModelList = new ArrayList<>();
  private ProductCategoryListInterface productCategoryListInterface;
  private GiftListAdapterInterface giftListAdapterInterface;

  public GiftListAdapter(Context context, List<GiftModel> giftModelList, GiftListAdapterInterface giftListAdapterInterface) {
    this.context = context;
    this.giftModelList = giftModelList;
    this.giftListAdapterInterface = giftListAdapterInterface;
  }

  public void setProductCategoryListInterface(ProductCategoryListInterface productCategoryListInterface) {
    this.productCategoryListInterface = productCategoryListInterface;
  }

  @NonNull
  @Override
  public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(context).inflate(R.layout.row_gift, parent, false);
    return new ViewHolder(view);
  }

  @Override
  public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
    final GiftModel model = giftModelList.get(position);

    String imageURL = BuildConfig.IMAGE_BASE_URL + model.getProductImage();
    Log.d("imageURL", imageURL);
    holder.txtQuantity.setText(model.getOrderQuantity());
    holder.imgGift.setImageURI(Uri.parse(imageURL));
    holder.imgMinus.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if(Integer.parseInt(holder.txtQuantity.getText().toString()) >0){
          holder.txtQuantity.setText(String.valueOf(Integer.parseInt(holder.txtQuantity.getText().toString())-1));
          if(giftListAdapterInterface != null){
            giftListAdapterInterface.onPlusClicked(Integer.parseInt(holder.txtQuantity.getText().toString()));
          }
        }
      }
    });

    holder.imgPlus.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {

      }
    });

  }

  @Override
  public int getItemCount() {
    return giftModelList.size();
  }

  public void setData(List<GiftModel> giftModelList) {
    this.giftModelList = giftModelList;
    notifyDataSetChanged();
  }

  public class ViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.imgGift)
    SimpleDraweeView imgGift;
    @BindView(R.id.imgPlus)
    ImageView imgPlus;
    @BindView(R.id.imgMinus)
    TextView imgMinus;
    @BindView(R.id.txtQuantity)
    TextView txtQuantity;

    public ViewHolder(@NonNull View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
    }
  }
}
