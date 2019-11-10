package com.suffix.fieldforce.activity.task.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.suffix.fieldforce.R;
import com.suffix.fieldforce.model.task.AssignTaskItem;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class TaskDetailsAdapter extends RecyclerView.Adapter<TaskDetailsAdapter.ViewHolder> {

    private Context context;
    private ArrayList<AssignTaskItem> modelRemainderArrayList = new ArrayList<AssignTaskItem>();

    public TaskDetailsAdapter(Context context, ArrayList<AssignTaskItem> modelRemainderArrayList) {
        this.context = context;
        this.modelRemainderArrayList = modelRemainderArrayList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_issue, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final AssignTaskItem assignTaskItem = modelRemainderArrayList.get(position);
        holder.ticketCode.setText(assignTaskItem.getTicketCode());
        holder.ticketTitle.setText(assignTaskItem.getTicketTitle());
        holder.ticketStatusText.setText(assignTaskItem.getTicketStatusText());
        holder.consumerName.setText(assignTaskItem.getConsumerName());
        holder.consumerAddress.setText(assignTaskItem.getConsumerAddress());
    }

    @Override
    public int getItemCount() {
        return modelRemainderArrayList.size();
    }

    public void setList(ArrayList<AssignTaskItem> list) {
        this.modelRemainderArrayList = list;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.ticketCode)
        TextView ticketCode;
        @BindView(R.id.ticketTitle)
        TextView ticketTitle;
        @BindView(R.id.ticketStatusText)
        TextView ticketStatusText;
        @BindView(R.id.ticketStartDate)
        TextView ticketStartDate;
        @BindView(R.id.imgProjectIcon)
        CircleImageView imgProjectIcon;
        @BindView(R.id.consumerName)
        TextView consumerName;
        @BindView(R.id.consumerAddress)
        TextView consumerAddress;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
