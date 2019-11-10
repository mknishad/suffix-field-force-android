package com.suffix.fieldforce.activity.task.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.suffix.fieldforce.R;
import com.suffix.fieldforce.activity.task.adapters.TaskDetailsAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Issue_Assigned_Fragment extends Fragment {

    @BindView(R.id.filter_issue)
    LinearLayout filterIssue;
    @BindView(R.id.issue_item)
    RecyclerView recyclerViewList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_issue__assigned_, container, false);
        ButterKnife.bind(this,view);
//        final SweetAlertDialog pDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.PROGRESS_TYPE);
//        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
//        pDialog.setTitleText("please wait...");
//        pDialog.setCancelable(false);
//        pDialog.show();
        ArrayList<String> modelTasks = new ArrayList<String>();
        for (int i = 0; i < 10; i++) {
            modelTasks.add("name");
        }
        recyclerViewList.setLayoutManager(new GridLayoutManager(getContext(), 1));
        TaskDetailsAdapter adapter = new TaskDetailsAdapter(getContext(), modelTasks);
        recyclerViewList.setAdapter(adapter);
        return view;
    }

}
