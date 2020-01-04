package com.suffix.fieldforce.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.suffix.fieldforce.R;
import com.suffix.fieldforce.activity.chat.MessageActivity;
import com.suffix.fieldforce.model.GroupChatInfo;
import com.suffix.fieldforce.model.ModelGroupChat;
import com.suffix.fieldforce.model.ModelUserList;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class GroupListAdapter extends RecyclerView.Adapter<GroupListAdapter.VideoViewHolder> {

  private Context mcontext;
  private List<GroupChatInfo> modelGroupChats = new ArrayList<>();
  private Uri uri;

  public GroupListAdapter(Context context, List<GroupChatInfo> modelGroupChats) {
    this.mcontext = context;
    this.modelGroupChats = modelGroupChats;
  }

  @Override
  public VideoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_chat_list, parent, false);
    return new VideoViewHolder(view);
  }

  @Override
  public void onBindViewHolder(final VideoViewHolder holder, int position) {

    GroupChatInfo modelGroupChat = modelGroupChats.get(position);
    holder.txtUserName.setText(modelGroupChat.getChatGroupName());

//    holder.itemView.setOnClickListener(new View.OnClickListener() {
//      @Override
//      public void onClick(View view) {
//        Intent intent = new Intent(mcontext, MessageActivity.class);
//        intent.putExtra("EMPLOYEE_NAME", model.getEmpName());
//        intent.putExtra("EMPLOYEE_IMAGE", model.getPictureLink());
//        intent.putExtra("EMPLOYEE_ID", model.getEmpOfficeId());
//        mcontext.startActivity(intent);
//      }
//    });
  }

  public void setData(List<GroupChatInfo> modelGroupChats) {
    this.modelGroupChats = modelGroupChats;
    notifyDataSetChanged();
  }

  @Override
  public int getItemCount() {
    return modelGroupChats.size();
  }

  public class VideoViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.imgUserLogo)
    SimpleDraweeView imgUserLogo;

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