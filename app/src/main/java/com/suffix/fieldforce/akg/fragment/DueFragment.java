package com.suffix.fieldforce.akg.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.suffix.fieldforce.R;
import com.suffix.fieldforce.akg.activity.MemoDetailsActivity;
import com.suffix.fieldforce.akg.activity.MemoListActivity;
import com.suffix.fieldforce.akg.adapter.MemoListAdapter;
import com.suffix.fieldforce.akg.adapter.MemoListInterface;
import com.suffix.fieldforce.akg.api.AkgApiInterface;
import com.suffix.fieldforce.akg.database.manager.RealMDatabaseManager;
import com.suffix.fieldforce.akg.model.InvoiceRequest;
import com.suffix.fieldforce.akg.util.AkgConstants;
import com.suffix.fieldforce.preference.FieldForcePreferences;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DueFragment extends Fragment {

  @BindView(R.id.recyclerView)
  RecyclerView recyclerView;

  @BindView(R.id.txtTotalMemo)
  TextView txtTotalMemo;

  @BindView(R.id.txtResponse)
  TextView txtResponse;

  private MemoListAdapter memoListAdapter;
  private List<InvoiceRequest> memoListResponse;


  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {


    // Inflate the layout for this fragment
    View view = inflater.inflate(R.layout.fragment_all_memo, container, false);
    ButterKnife.bind(this, view);


    LinearLayoutManager manager = new LinearLayoutManager(getActivity());
    recyclerView.setLayoutManager(manager);
    memoListAdapter = new MemoListAdapter(getActivity(), memoListResponse);
    recyclerView.setAdapter(memoListAdapter);

    generateView();

    memoListAdapter.setMemoListInterface(new MemoListInterface() {
      @Override
      public void onItemClick(int position) {
        InvoiceRequest response = memoListResponse.get(position);

        Intent intent = new Intent(getActivity(), MemoDetailsActivity.class);
        intent.putExtra(AkgConstants.MEMO_DETAIL, response);
        startActivity(intent);
      }
    });

    return view;
  }

  private void generateView() {

    memoListResponse = ((MemoListActivity) getActivity()).getMemoList();
    if (memoListResponse.size() > 0) {
      txtResponse.setVisibility(View.GONE);
      recyclerView.setVisibility(View.VISIBLE);
      memoListAdapter.setData(prepareList(memoListResponse));
    }
    txtTotalMemo.setText("মোট : " + memoListResponse.size());
  }

  private List<InvoiceRequest> prepareList(List<InvoiceRequest> memoListResponse) {
    List<InvoiceRequest> invoiceRequests = new ArrayList<>();

    for (InvoiceRequest invoiceRequest : memoListResponse) {
      if ((invoiceRequest.getTotalAmount() - invoiceRequest.getRecievedAmount()) > 0) {
        invoiceRequests.add(invoiceRequest);
      }
    }

    return invoiceRequests;
  }

}