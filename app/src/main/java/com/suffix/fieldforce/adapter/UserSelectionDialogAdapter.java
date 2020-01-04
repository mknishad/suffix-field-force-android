package com.suffix.fieldforce.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.suffix.fieldforce.R;
import com.suffix.fieldforce.activity.chat.MessageActivity;
import com.suffix.fieldforce.model.ModelUserList;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UserSelectionDialogAdapter extends RecyclerView.Adapter<UserSelectionDialogAdapter.VideoViewHolder> {

  private Context mcontext;
  private List<ModelUserList> modelUserLists = new ArrayList<>();
  private Uri uri;
  private UserSelectionDialogAdapterListener userSelectionDialogAdapterListener;

  public void setUserSelectionDialogAdapterListener(UserSelectionDialogAdapterListener userSelectionDialogAdapterListener) {
    this.userSelectionDialogAdapterListener = userSelectionDialogAdapterListener;
  }

  public UserSelectionDialogAdapter(Context context, List<ModelUserList> modelUserLists) {
    this.mcontext = context;
    this.modelUserLists = modelUserLists;
  }

  @Override
  public VideoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_user_selection, parent, false);
    return new VideoViewHolder(view);
  }

  @Override
  public void onBindViewHolder(final VideoViewHolder holder, int position) {

    ModelUserList model = modelUserLists.get(position);
    if (!TextUtils.isEmpty(model.getPictureLink().trim())) {
      uri = Uri.parse(model.getPictureLink());
      holder.txtUserName.setText(model.getEmpName());
      try {
        //holder.imgUserLogo.setImageURI(uri);
      } catch (Exception e) {
        Log.d("USER LOGO ----> ", uri.getPath());
      }
    }

    holder.checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
      @Override
      public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (holder.checkbox.isChecked()){
          if(userSelectionDialogAdapterListener != null){
            userSelectionDialogAdapterListener.onCheckboxSelect(position);
          }
        }else {
          //do something else
        }
      }
    });

  }

  public void setData(List<ModelUserList> modelUserLists) {
    this.modelUserLists = modelUserLists;
    notifyDataSetChanged();
  }

  @Override
  public int getItemCount() {
    return modelUserLists.size();
  }

  public class VideoViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.imgUserLogo)
    SimpleDraweeView imgUserLogo;

    @BindView(R.id.txtUserName)
    TextView txtUserName;

    @BindView(R.id.checkbox)
    CheckBox checkbox;

    public VideoViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this,itemView);
    }
  }
}