package com.suffix.fieldforce.activity.chat;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import com.google.android.material.tabs.TabLayout;
import com.suffix.fieldforce.R;
import com.suffix.fieldforce.adapter.ChatDashboardViewPagerAdapter;
import com.suffix.fieldforce.fragment.ChatListFragment;
import com.suffix.fieldforce.fragment.MultipleChatFragment;
import com.suffix.fieldforce.fragment.UserListFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChatDashboardActivity extends AppCompatActivity {

  @BindView(R.id.tab)
  TabLayout tab;

  @BindView(R.id.viewpager)
  ViewPager viewpager;

  private int[] tabIcons = {
      R.drawable.singlechat,
      R.drawable.group,
      R.drawable.member
  };

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_chat_dashboard);
    ButterKnife.bind(this);

    setupViewPager(viewpager);
    tab.setupWithViewPager(viewpager);
    setTabIcon(tab);
  }

  private void setTabIcon(TabLayout tab) {
    tab.getTabAt(0).setIcon(tabIcons[0]);
    tab.getTabAt(1).setIcon(tabIcons[1]);
    tab.getTabAt(2).setIcon(tabIcons[2]);
  }

  private void setupViewPager(ViewPager viewpager) {
    ChatDashboardViewPagerAdapter adapter = new ChatDashboardViewPagerAdapter(getSupportFragmentManager());
    adapter.addFragment(new ChatListFragment(), "Chat");
    adapter.addFragment(new MultipleChatFragment(), "Group Chat");
    adapter.addFragment(new UserListFragment(), "All Users");
    viewpager.setAdapter(adapter);
    viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
      @Override
      public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

      }

      @Override
      public void onPageSelected(int position) {

      }

      @Override
      public void onPageScrollStateChanged(int state) {

      }
    });
  }
}
