package com.suffix.fieldforce.akg.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.suffix.fieldforce.akg.fragment.AllMemoFragment;
import com.suffix.fieldforce.akg.fragment.DueFragment;

public class MemoPagerAdapter extends FragmentStatePagerAdapter {

  public MemoPagerAdapter(@NonNull FragmentManager fm) {
    super(fm, FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
  }

  @NonNull
  @Override
  public Fragment getItem(int position) {
    if (position == 0) {
      return new AllMemoFragment();
    } else {
      return new DueFragment();
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
      return "সকল মেমো";
    } else {
      return "বাঁকী মেমো";
    }
  }
}
