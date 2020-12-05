package com.suffix.fieldforce.akg.fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.suffix.fieldforce.R;
import com.suffix.fieldforce.akg.activity.StockActivity;
import com.suffix.fieldforce.akg.adapter.PrintingInterface;
import com.suffix.fieldforce.akg.adapter.StockSaleAdapter;
import com.suffix.fieldforce.akg.model.AkgLoginResponse;
import com.suffix.fieldforce.akg.model.Distributor;
import com.suffix.fieldforce.akg.model.product.CategoryModel;
import com.suffix.fieldforce.akg.util.AkgPrintingService;
import com.suffix.fieldforce.preference.FieldForcePreferences;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SaleFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SaleFragment extends Fragment {

  @BindView(R.id.layoutScroll)
  NestedScrollView layoutScroll;
  @BindView(R.id.srNameTextView)
  TextView srNameTextView;
  @BindView(R.id.timeTextView)
  TextView timeTextView;
  @BindView(R.id.recyclerView)
  RecyclerView recyclerView;
  @BindView(R.id.totalAmountTextView)
  TextView totalAmountTextView;
  @BindView(R.id.txtResponse)
  TextView txtResponse;

  @OnClick(R.id.btnPrint)
  public void printSale() {
    progressDialog.show();
    Distributor distributor = new Gson().fromJson(preferences.getDistributor(), Distributor.class);
    AkgLoginResponse loginResponse = new Gson().fromJson(preferences.getLoginResponse(),
        AkgLoginResponse.class);
    new AkgPrintingService().printSale(distributor.getData().getDistributorName(),
        distributor.getData().getMobile(), loginResponse, products, new PrintingInterface() {
          @Override
          public void onPrintSuccess(String message) {
            progressDialog.dismiss();
            builder.setMessage(message);
            builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
              @Override
              public void onClick(DialogInterface dialog, int which) {
                //onBackPressed();
              }
            });

            AlertDialog alertDialog = builder.create();
            alertDialog.setCancelable(false);
            alertDialog.show();
          }

          @Override
          public void onPrintFail(String message) {
            progressDialog.dismiss();
            builder.setMessage(message);
            builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
              @Override
              public void onClick(DialogInterface dialog, int which) {
                //alertDialog.dismiss();
              }
            });

            AlertDialog alertDialog = builder.create();
            alertDialog.setCancelable(false);
            alertDialog.show();
          }
        });
  }

  private static final String TAG = "SaleFragment";

  private FieldForcePreferences preferences;
  private AkgLoginResponse loginResponse;
  private StockSaleAdapter stockSaleAdapter;
  private List<CategoryModel> products;

  private ProgressDialog progressDialog;
  private AlertDialog.Builder builder;

  public SaleFragment() {
    // Required empty public constructor
  }

  public static SaleFragment newInstance() {
    SaleFragment fragment = new SaleFragment();
    return fragment;
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_stock_sale, container, false);
    ButterKnife.bind(this, view);
    return view;
  }

  @Override
  public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);

    preferences = new FieldForcePreferences(getActivity());
    loginResponse = new Gson().fromJson(preferences.getLoginResponse(), AkgLoginResponse.class);

    progressDialog = new ProgressDialog(getActivity());
    progressDialog.setMessage("Printing...");

    builder = new AlertDialog.Builder(getActivity());

    products = new ArrayList<>();
    stockSaleAdapter = new StockSaleAdapter(getActivity(), products);

    stockSaleAdapter = new StockSaleAdapter(getActivity(), products);
    recyclerView.setAdapter(stockSaleAdapter);

    products = ((StockActivity) getActivity()).getProducts();
    populateView();
  }

  private void populateView() {
    if (products.size() > 0) {
      txtResponse.setVisibility(View.GONE);
      layoutScroll.setVisibility(View.VISIBLE);
      stockSaleAdapter.setData(products);

      srNameTextView.setText(String.format(Locale.getDefault(),
          "SR Name: %s", loginResponse.getData().getUserName()));
      String time = android.text.format.DateFormat.format("hh:mm a", new java.util.Date()).toString();
      timeTextView.setText(time);
      double totalSaleAmount = 0.0;
      for (CategoryModel categoryModel : products) {
        totalSaleAmount += categoryModel.getSalesQty() * categoryModel.getSellingRate();
      }
      totalAmountTextView.setText(String.format(Locale.getDefault(), "%.2f", totalSaleAmount));
    } else {
      txtResponse.setVisibility(View.VISIBLE);
      layoutScroll.setVisibility(View.GONE);
    }
  }
}