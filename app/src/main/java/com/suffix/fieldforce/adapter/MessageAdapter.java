package com.suffix.fieldforce.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.suffix.fieldforce.R;
import com.suffix.fieldforce.model.Chats;
import com.suffix.fieldforce.preference.FieldForcePreferences;
import java.util.ArrayList;
import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

  private Context mContext;
  private List<Chats> mChats = new ArrayList<>();
  private FieldForcePreferences preferences;

  public static final int MSG_TYPE_LEFT = 0;
  public static final int MSG_TYPE_RIGHT = 1;

  public MessageAdapter(Context context, List<Chats> chats) {
    this.mContext = context;
    this.mChats = chats;
    preferences = new FieldForcePreferences(context);
  }

  @Override
  public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

    if (viewType == MSG_TYPE_LEFT) {

      View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item_left, parent, false);

      return new MessageAdapter.ViewHolder(view);
    } else {
      View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item_right, parent, false);

      return new MessageAdapter.ViewHolder(view);

    }

  }

  @Override
  public int getItemViewType(int position) {
    if (mChats.get(position).getSender_id().equals(preferences.getUser().getUserId())) {
      return MSG_TYPE_RIGHT;
    } else {
      return MSG_TYPE_LEFT;
    }
  }

  @Override
  public void onBindViewHolder(final ViewHolder holder, int position) {

    Chats chats = mChats.get(position);
    holder.text.setText(chats.getMessage());

  }

  @Override
  public int getItemCount() {
    return mChats.size();
  }

  public class ViewHolder extends RecyclerView.ViewHolder {

    TextView text;

    public ViewHolder(View itemView) {
      super(itemView);
      text = itemView.findViewById(R.id.msg);
    }
  }
}
