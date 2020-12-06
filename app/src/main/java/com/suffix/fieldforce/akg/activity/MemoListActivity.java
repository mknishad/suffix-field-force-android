package com.suffix.fieldforce.akg.activity;

import android.content.Intent;
import android.graphics.BlendMode;
import android.graphics.BlendModeColorFilter;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.suffix.fieldforce.R;
import com.suffix.fieldforce.akg.adapter.MemoListAdapter;
import com.suffix.fieldforce.akg.adapter.MemoListInterface;
import com.suffix.fieldforce.akg.adapter.MemoPagerAdapter;
import com.suffix.fieldforce.akg.adapter.StockPagerAdapter;
import com.suffix.fieldforce.akg.api.AkgApiClient;
import com.suffix.fieldforce.akg.api.AkgApiInterface;
import com.suffix.fieldforce.akg.database.manager.RealMDatabaseManager;
import com.suffix.fieldforce.akg.model.InvoiceRequest;
import com.suffix.fieldforce.akg.util.AkgConstants;
import com.suffix.fieldforce.preference.FieldForcePreferences;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MemoListActivity extends AppCompatActivity {

  @BindView(R.id.toolbar)
  Toolbar toolbar;

  @BindView(R.id.tabLayout)
  TabLayout tabLayout;
  @BindView(R.id.viewPager)
  ViewPager viewPager;

  private FieldForcePreferences preferences;
  private AkgApiInterface apiInterface;
  private MemoListAdapter memoListAdapter;
  private List<InvoiceRequest> memoListResponse;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_memo);
    ButterKnife.bind(this);

    setupToolbar();

    viewPager.setAdapter(new MemoPagerAdapter(getSupportFragmentManager()));
    tabLayout.setupWithViewPager(viewPager);

    memoListResponse = new ArrayList<>();

    preferences = new FieldForcePreferences(this);
    apiInterface = AkgApiClient.getApiClient().create(AkgApiInterface.class);

    getMemoList();
  }

  private void setupToolbar() {
    setSupportActionBar(toolbar);
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      toolbar.setTitleTextColor(getResources().getColor(android.R.color.white, null));
    } else {
      toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
    }

    if (getSupportActionBar() != null) {
      getSupportActionBar().setDisplayShowTitleEnabled(true);
      getSupportActionBar().setDisplayHomeAsUpEnabled(true);
      getSupportActionBar().setTitle("মেমোলিস্ট");
    }

    toolbar.setNavigationOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        onBackPressed();
      }
    });

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
      toolbar.getNavigationIcon().setColorFilter(new BlendModeColorFilter(Color.WHITE,
          BlendMode.SRC_ATOP));
    } else {
      toolbar.getNavigationIcon().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
    }
  }

  public List<InvoiceRequest> getMemoList() {
    memoListResponse = new RealMDatabaseManager().prepareInvoiceRequest();
    return memoListResponse;
  }
}