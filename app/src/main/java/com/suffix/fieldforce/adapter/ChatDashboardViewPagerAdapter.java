package com.suffix.fieldforce.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class ChatDashboardViewPagerAdapter extends FragmentPagerAdapter {

  List <Fragment> fragments = new ArrayList<>();
  List <String> titles = new ArrayList<>();

  public ChatDashboardViewPagerAdapter(@NonNull FragmentManager fm) {
    super(fm);
  }

  @NonNull
  @Override
  public Fragment getItem(int position) {
    return fragments.get(position);
  }

  @Override
  public int getCount() {
    return fragments.size();
  }

  @Nullable
  @Override
  public CharSequence getPageTitle(int position) {
    return titles.get(position);
  }

  public void addFragment(Fragment fragment, String title){
    fragments.add(fragment);
    titles.add(title);
  }
}
