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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        ButterKnife.bind(this,view);
        setupViewPager(viewpager);
        tab.setupWithViewPager(viewpager);
        return view;
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getChildFragmentManager());
        viewPagerAdapter.addFragment(new Issue_Assigned_Fragment(apiInterface.assignTicketList("BLA0010")), "Assigned");
        viewPagerAdapter.addFragment(new Issue_Assigned_Fragment(apiInterface.inprogressTicketList("BLA0010")), "In Progress");
        viewPagerAdapter.addFragment(new Issue_Assigned_Fragment(apiInterface.acceptedTicketList("BLA0010")), "Accepted");
        viewPagerAdapter.addFragment(new Issue_Assigned_Fragment(apiInterface.completedTicketList("BLA0010")), "Completed");
        viewPager.setAdapter(viewPagerAdapter);
    }

}
