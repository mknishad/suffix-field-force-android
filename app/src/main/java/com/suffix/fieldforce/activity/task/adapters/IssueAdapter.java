package com.suffix.fieldforce.activity.task.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.suffix.fieldforce.R;
import com.suffix.fieldforce.model.Issue;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class IssueAdapter extends RecyclerView.Adapter<IssueAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Issue> issues = new ArrayList<>();

    public IssueAdapter(Context context, ArrayList<Issue> issues) {
        this.context = context;
        this.issues = issues;
    }

    public void setList(ArrayList<Issue> issues) {
        this.issues = issues;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(context).inflate(R.layout.row_issue, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        final Issue issue = issues.get(i);

        viewHolder.txtIssueNumber.setText(issue.getIssueName());
        viewHolder.txtIssueDescription.setText(issue.getIssueDescription());
        viewHolder.txtUserName.setText(issue.getUserName() + "\n" + issue.getUserExperties());
        viewHolder.txtIssueStatus.setText(issue.getIssueStatus());
    }

    @Override
    public int getItemCount() {
        return issues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.txtIssueNumber)
        TextView txtIssueNumber;
        @BindView(R.id.txtIssueDescription)
        TextView txtIssueDescription;
        @BindView(R.id.txtIssueStatus)
        TextView txtIssueStatus;
        @BindView(R.id.txtDateTime)
        TextView txtDateTime;
        @BindView(R.id.imgProjectIcon)
        CircleImageView imgProjectIcon;
        @BindView(R.id.txtUserName)
        TextView txtUserName;
        @BindView(R.id.txtUserAddress)
        TextView txtUserAddress;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
