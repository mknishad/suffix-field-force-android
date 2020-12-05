package com.suffix.fieldforce.akg.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.suffix.fieldforce.fragment.ActivityFragment;

public class StockPagerAdapter extends FragmentStatePagerAdapter {
  public StockPagerAdapter(FragmentManager fragmentManager) {
    super(fragmentManager, FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
  }

  @NonNull
  @Override
  public Fragment getItem(int position) {
    if (position == 0){
      return new ActivityFragment();
    } else {
      return new ActivityFragment();
    }
  }

  @Override
  public int getCount() {
    return 2;
  }

  @Nullable
  @Override
  public CharSequence getPageTitle(int position) {
    if (position == 0) {
      return "স্টক";
    }
    return "সেল";
  }
}
