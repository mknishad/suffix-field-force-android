package com.suffix.fieldforce.activity.task.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.suffix.fieldforce.R;
import com.suffix.fieldforce.activity.task.adapters.NotificationDetailsAdapter;

import java.util.ArrayList;

import butterknife.BindView;

public class NotificationsFragment extends Fragment {

    NotificationDetailsAdapter adapter;
    ArrayList<String> modelTasks;
    @BindView(R.id.issue_item)
    RecyclerView recyclerViewList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notifications, container, false);
        recyclerViewList = view.findViewById(R.id.issue_item);
        modelTasks = new ArrayList<String>();

        for (int i = 0; i < 10; i++) {
            modelTasks.add("name");
        }

        recyclerViewList.setLayoutManager(new GridLayoutManager(getContext(), 1));
        adapter = new NotificationDetailsAdapter(getContext(), modelTasks);
        recyclerViewList.setAdapter(adapter);
        return view;
    }

}
