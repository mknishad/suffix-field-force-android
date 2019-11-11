package com.suffix.fieldforce.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.suffix.fieldforce.R;
import com.suffix.fieldforce.adapter.TaskDetailsAdapter;
import com.suffix.fieldforce.adapter.TaskDetailsAdapterListener;
import com.suffix.fieldforce.model.AssignTaskItem;
import com.suffix.fieldforce.model.AssignedTask;
import com.suffix.fieldforce.retrofitapi.APIClient;
import com.suffix.fieldforce.retrofitapi.APIInterface;

import java.util.ArrayList;
import java.util.List;

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
    @BindView(R.id.txtError)
    TextView txtError;

    private final String RESPONSE_OK = "1";

    private ArrayList<AssignTaskItem> assignTaskItems;
    TaskDetailsAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_issue__assigned_, container, false);
        ButterKnife.bind(this, view);

        recyclerViewList.setLayoutManager(new GridLayoutManager(getContext(), 1));

        APIInterface apiInterface = APIClient.getApiClient().create(APIInterface.class);

        Call<List<AssignedTask>> getAssignTicketList = apiInterface.getAssignTicketList("BLA0010");
        getAssignTicketList.enqueue(new Callback<List<AssignedTask>>() {
            @Override
            public void onResponse(Call<List<AssignedTask>> call, Response<List<AssignedTask>> response) {
                    if(response.isSuccessful()){
                        List<AssignedTask> assignedTasks = response.body();
                        AssignedTask assignedTask = assignedTasks.get(0);

                        if(assignedTask.getResponseCode().contains(RESPONSE_OK)){
                            adapter = new TaskDetailsAdapter(getContext(),assignedTask.getResponseData());
                            recyclerViewList.setAdapter(adapter);
                            adapter.setTaskDetailsAdapterListener(new TaskDetailsAdapterListener() {
                                @Override
                                public void onItemClicked(int position) {
                                    Toast.makeText(getContext(), ""+position, Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
            }

            @Override
            public void onFailure(Call<List<AssignedTask>> call, Throwable t) {
                t.printStackTrace();
            }
        });

        return view;
    }

}
