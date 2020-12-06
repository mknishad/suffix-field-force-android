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
  private List<InvoiceRequest> filteredInvoiceRequests;


  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {


    // Inflate the layout for this fragment
    View view = inflater.inflate(R.layout.fragment_due, container, false);
    ButterKnife.bind(this, view);

    filteredInvoiceRequests = new ArrayList<>();

    LinearLayoutManager manager = new LinearLayoutManager(getActivity());
    recyclerView.setLayoutManager(manager);
    memoListAdapter = new MemoListAdapter(getActivity(), filteredInvoiceRequests);
    recyclerView.setAdapter(memoListAdapter);

    //generateView();

    memoListAdapter.setMemoListInterface(new MemoListInterface() {
      @Override
      public void onItemClick(int position) {
        InvoiceRequest response = filteredInvoiceRequests.get(position);

        Intent intent = new Intent(getActivity(), MemoDetailsActivity.class);
        intent.putExtra(AkgConstants.MEMO_DETAIL, response);
        startActivity(intent);
      }
    });

    return view;
  }

  @Override
  public void onResume() {
    super.onResume();
    generateView();
  }

  private void generateView() {

    memoListResponse = ((MemoListActivity) getActivity()).getMemoList();
    if (memoListResponse.size() > 0) {
      txtResponse.setVisibility(View.GONE);
      recyclerView.setVisibility(View.VISIBLE);
      memoListAdapter.setData(prepareList(memoListResponse));
    }

  }

  private List<InvoiceRequest> prepareList(List<InvoiceRequest> memoListResponse) {
    filteredInvoiceRequests.clear();
    for (InvoiceRequest invoiceRequest : memoListResponse) {
      if ((invoiceRequest.getTotalAmount() - invoiceRequest.getRecievedAmount()) > 0) {
        filteredInvoiceRequests.add(invoiceRequest);
      }
    }
    txtTotalMemo.setText("মোট : " + filteredInvoiceRequests.size());
    return filteredInvoiceRequests;
  }

}