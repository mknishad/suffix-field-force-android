package com.suffix.fieldforce.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.suffix.fieldforce.R;
import com.suffix.fieldforce.adapter.TaskDetailsAdapter;
import com.suffix.fieldforce.model.AssignTaskItem;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Issue_Created_Fragment extends Fragment {

    @BindView(R.id.filter_issue)
    LinearLayout filterIssue;
    @BindView(R.id.issue_item)
    RecyclerView recyclerViewList;

    TaskDetailsAdapter adapter;
    ArrayList<AssignTaskItem> assignTaskItems;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_issue__created, container, false);
        ButterKnife.bind(this,view);

        recyclerViewList.setLayoutManager(new GridLayoutManager(getContext(), 1));
        adapter = new TaskDetailsAdapter(getContext(), new ArrayList<AssignTaskItem>());
        recyclerViewList.setAdapter(adapter);

        return view;
    }

}
