package com.suffix.fieldforce.activity.inventory

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.suffix.fieldforce.R

/**
 * Created by Nishad on 5/8/2017.
 */

class InventoryCategoryAdapter(
  private val mContext: Context, fm: FragmentManager
) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

  override fun getItem(position: Int): Fragment {
    return if (position == 0) {
      InventoryListFragment()
    } else {
      RequisitionListFragment()
    }
  }

  override fun getCount(): Int {
    return 2
  }

  override fun getPageTitle(position: Int): CharSequence? {
    return if (position == 0) {
      mContext.getString(R.string.inventory)
    } else {
      mContext.getString(R.string.requisition)
    }
  }
}
