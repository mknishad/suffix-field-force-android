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

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.VideoViewHolder> {

    Context mcontext;
    List<ModelUserList> modelUserLists = new ArrayList<>();

    public UserAdapter(Context context, List<ModelUserList> modelUserLists) {
        this.mcontext = context;
        this.modelUserLists = modelUserLists;
    }

    @Override
    public VideoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from( parent.getContext()).inflate(R.layout.row_issue, parent, false);
        return new UserAdapter.VideoViewHolder(view);

    }

    @Override
    public void onBindViewHolder(final VideoViewHolder holder, int position) {

        ModelUserList model = modelUserLists.get(position);
        holder.text.setText(model.getEmpName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mcontext, MessageActivity.class);
                intent.putExtra("name",holder.text.getText());
                mcontext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return modelUserLists.size();
    }

    public class VideoViewHolder extends RecyclerView.ViewHolder{

        TextView text;

        public VideoViewHolder(View itemView) {
            super(itemView);
            text = (TextView) itemView.findViewById(R.id.consumerName);
        }
    }
}