package com.suffix.fieldforce.activity.task.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.suffix.fieldforce.R;
import com.suffix.fieldforce.activity.task.adapters.TaskDetailsAdapter;
import com.suffix.fieldforce.model.task.AssignTaskItem;
import com.suffix.fieldforce.model.task.AssignedTask;
import com.suffix.fieldforce.retrofitapi.APIClient;
import com.suffix.fieldforce.retrofitapi.APIInterface;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Issue_Assigned_Fragment extends Fragment {

    @BindView(R.id.filter_issue)
    LinearLayout filterIssue;
    @BindView(R.id.issue_item)
    RecyclerView recyclerViewList;

    private final String RESPONSE_OK = "1";
    @BindView(R.id.txtError)
    TextView txtError;

    private ArrayList<AssignTaskItem> assignTaskItems;
    TaskDetailsAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_issue__assigned_, container, false);
        ButterKnife.bind(this, view);

//        final SweetAlertDialog pDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.PROGRESS_TYPE);
//        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
//        pDialog.setTitleText("please wait...");
//        pDialog.setCancelable(false);
//        pDialog.show();

        recyclerViewList.setLayoutManager(new GridLayoutManager(getContext(), 1));

        APIInterface apiInterface = APIClient.getApiClient().create(APIInterface.class);

        Call<AssignedTask> getAssignTicketList = apiInterface.getAssignTicketList("BLA0010");
        getAssignTicketList.enqueue(new Callback<AssignedTask>() {
            @Override
            public void onResponse(Call<AssignedTask> call, Response<AssignedTask> response) {
                AssignedTask assignedTask = response.body();
                if (assignedTask.getResponseCode().contains(RESPONSE_OK)) {
                    assignTaskItems = (ArrayList<AssignTaskItem>) assignedTask.getResponseData();
                    adapter = new TaskDetailsAdapter(getContext(), assignTaskItems);
                    recyclerViewList.setAdapter(adapter);
                } else {
                    recyclerViewList.setVisibility(View.GONE);
                    txtError.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<AssignedTask> call, Throwable t) {

            }
        });
        return view;
    }

}
