package com.figure.anothertest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

public class NewPost extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;

    private TabItem tab1,tab2,tab3;
    private PageAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);

        tabLayout = findViewById(R.id.post_tab_layout);
         tab1 = findViewById(R.id.tab1);
         //tab2 = findViewById(R.id.tab2);
         tab3 = findViewById(R.id.tab3);

         viewPager = findViewById(R.id.tab_viewpager);

         pagerAdapter = new PageAdapter(getSupportFragmentManager(),tabLayout.getTabCount());
         viewPager.setAdapter(pagerAdapter);

         tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
             @Override
             public void onTabSelected(TabLayout.Tab tab) {
                 viewPager.setCurrentItem(tab.getPosition());

                 if(tab.getPosition() == 0){
                     pagerAdapter.notifyDataSetChanged();
                 }
                 else if(tab.getPosition() == 1){
                     pagerAdapter.notifyDataSetChanged();
                 }
                 else if(tab.getPosition() == 3){
                     pagerAdapter.notifyDataSetChanged();
                 }
             }

             @Override
             public void onTabUnselected(TabLayout.Tab tab) {

             }

             @Override
             public void onTabReselected(TabLayout.Tab tab) {

             }
         });

         viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
    }
}
