package com.suffix.fieldforce.activity.chat;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.suffix.fieldforce.R;
import com.suffix.fieldforce.adapter.ChatDashboardViewPagerAdapter;
import com.suffix.fieldforce.fragment.MultipleChatFragment;
import com.suffix.fieldforce.fragment.SingleChatFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChatDashboardActivity extends AppCompatActivity {

  @BindView(R.id.tab)
  TabLayout tab;

  @BindView(R.id.viewpager)
  ViewPager viewpager;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_chat_dashboard);
    ButterKnife.bind(this);

    setupViewPager(viewpager);
    tab.setupWithViewPager(viewpager);
  }

  private void setupViewPager(ViewPager viewpager) {
    ChatDashboardViewPagerAdapter adapter = new ChatDashboardViewPagerAdapter(getSupportFragmentManager());
    adapter.addFragment(new SingleChatFragment(), "Chat");
    adapter.addFragment(new MultipleChatFragment(), "Group Chat");
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
