package com.suffix.fieldforce.adapter

import android.content.Context

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

import com.suffix.fieldforce.R
import com.suffix.fieldforce.fragment.AdvanceBillFragment
import com.suffix.fieldforce.fragment.ExpenseBillFragment

/**
 * Created by Nishad on 5/8/2017.
 */

class BillCategoryAdapter(
  private val mContext: Context, fm: FragmentManager
) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

  override fun getItem(position: Int): Fragment {
    return if (position == 0) {
      ExpenseBillFragment()
    } else {
      AdvanceBillFragment()
    }
  }

  override fun getCount(): Int {
    return 2
  }

  override fun getPageTitle(position: Int): CharSequence? {
    return if (position == 0) {
      mContext.getString(R.string.expense)
    } else {
      mContext.getString(R.string.advance)
    }
  }
}
