package com.suffix.fieldforce.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.suffix.fieldforce.R;
import com.suffix.fieldforce.activity.chat.MessageActivity;
import com.suffix.fieldforce.model.ModelUserList;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.VideoViewHolder> {

  Context mcontext;
  List<ModelUserList> modelUserLists = new ArrayList<>();

  public UserAdapter(Context context, List<ModelUserList> modelUserLists) {
    this.mcontext = context;
    this.modelUserLists = modelUserLists;
  }

  @Override
  public VideoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_chat_list, parent, false);
    return new VideoViewHolder(view);

  }

  @Override
  public void onBindViewHolder(final VideoViewHolder holder, int position) {

    ModelUserList model = modelUserLists.get(position);
    holder.txtUserName.setText(model.getEmpName());

    holder.itemView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Intent intent = new Intent(mcontext, MessageActivity.class);
        intent.putExtra("name", holder.txtUserName.getText());
        mcontext.startActivity(intent);
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
    CircleImageView imgUserLogo;

    @BindView(R.id.txtUserName)
    TextView txtUserName;

    @BindView(R.id.txtUserLastMessage)
    TextView txtUserLastMessage;

    public VideoViewHolder(View itemView) {
      super(itemView);
      txtUserName = (TextView) itemView.findViewById(R.id.txtUserName);
    }
  }
}