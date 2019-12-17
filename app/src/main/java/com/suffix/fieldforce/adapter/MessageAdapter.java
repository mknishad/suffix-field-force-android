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

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.VideoViewHolder> {

    Context mcontext;
    List<Chats> mChats = new ArrayList<>();
    private FieldForcePreferences preferences;

    public static final int MSG_TYPE_LEFT =0;
    public static final int MSG_TYPE_RIGHT =1;

    public MessageAdapter(Context context, List<Chats> chats) {
        this.mcontext = context;
        this.mChats = chats;
        preferences = new FieldForcePreferences(context);
    }

    @Override
    public VideoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if(viewType==MSG_TYPE_LEFT){

        View view = LayoutInflater.from( parent.getContext()).inflate(R.layout.chat_item_left, parent, false);

        return new MessageAdapter.VideoViewHolder(view);
        }else {
            View view = LayoutInflater.from( parent.getContext()).inflate(R.layout.chat_item_right, parent, false);

            return new MessageAdapter.VideoViewHolder(view);

        }

    }

    @Override
    public int getItemViewType(int position) {
        if (mChats.get(position).getSender().equals("admin")||mChats.get(position).getSender().equals(preferences.getUser().getUserName())){
            return MSG_TYPE_RIGHT;
        }else{
            return MSG_TYPE_LEFT;
        }
    }

    @Override
    public void onBindViewHolder(final VideoViewHolder holder, int position) {

        Chats chats = mChats.get(position);
        holder.text.setText(chats.getMessage());

    }

    @Override
    public int getItemCount() {
        return mChats.size();
    }

    public class VideoViewHolder extends RecyclerView.ViewHolder{

        TextView text;

        public VideoViewHolder(View itemView) {
            super(itemView);
            text = (TextView) itemView.findViewById(R.id.msg);
        }
    }
}