package com.suffix.fieldforce.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.suffix.fieldforce.R;
import com.suffix.fieldforce.adapter.ViewPagerAdapter;
import com.suffix.fieldforce.preference.FieldForcePreferences;
import com.suffix.fieldforce.retrofitapi.APIClient;
import com.suffix.fieldforce.retrofitapi.APIInterface;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DashboardFragment extends Fragment {

  @BindView(R.id.viewpager)
  ViewPager viewpager;

  @BindView(R.id.tab)
  TabLayout tab;

  APIInterface apiInterface = APIClient.getApiClient().create(APIInterface.class);
  private FieldForcePreferences preferences;
  private TaskFragment assignTicket;
  private TaskFragment inprogressTicket;
  private TaskFragment acceptedTicket;
  private TaskFragment completedTicket;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_dashboard, container, false);
    preferences = new FieldForcePreferences(getContext());
    ButterKnife.bind(this, view);

    assignTicket = new TaskFragment();
    inprogressTicket = new TaskFragment();
    acceptedTicket = new TaskFragment();
    completedTicket = new TaskFragment();

    setupViewPager(viewpager);
    tab.setupWithViewPager(viewpager);
    return view;
  }

  private void setupViewPager(ViewPager viewPager) {
    ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getChildFragmentManager());
    viewPagerAdapter.addFragment(new TaskFragment(apiInterface.assignTicketList(preferences.getUser().getUserId())), "Assigned");
    viewPagerAdapter.addFragment(new TaskFragment(apiInterface.inprogressTicketList(preferences.getUser().getUserId())), "In Progress");
    viewPagerAdapter.addFragment(new TaskFragment(apiInterface.acceptedTicketList(preferences.getUser().getUserId())), "Accepted");
    viewPagerAdapter.addFragment(new TaskFragment(apiInterface.completedTicketList(preferences.getUser().getUserId())), "Completed");
    viewPager.setAdapter(viewPagerAdapter);
  }
//    private void setupViewPager(ViewPager viewPager) {
//        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getChildFragmentManager());
//        viewPagerAdapter.addFragment(assignTicket, "Assigned");
//        viewPagerAdapter.addFragment(inprogressTicket, "In Progress");
//        viewPagerAdapter.addFragment(acceptedTicket, "Accepted");
//        viewPagerAdapter.addFragment(completedTicket, "Completed");
//        viewPager.setAdapter(viewPagerAdapter);
//    }

}
