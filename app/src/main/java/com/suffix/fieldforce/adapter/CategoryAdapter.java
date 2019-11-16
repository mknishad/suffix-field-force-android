package com.suffix.fieldforce.adapter;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.suffix.fieldforce.R;
import com.suffix.fieldforce.fragment.StandardBillFragment;

/**
 * Created by Nishad on 5/8/2017.
 */

public class CategoryAdapter extends FragmentPagerAdapter {

    /**
     * Context of the app
     */
    private Context mContext;

    public CategoryAdapter(Context context, FragmentManager fm) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new StandardBillFragment();
        } else {
            return new StandardBillFragment();
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0) {
            return mContext.getString(R.string.standard);
        } else {
            return mContext.getString(R.string.standard);
        }
    }
}
