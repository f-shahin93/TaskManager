package com.example.taskmanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;

import com.example.taskmanager.repository.TasksRepository;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;


public class TasksViewPagerActivity extends AppCompatActivity {

    static int orientation;
    private ViewPager mViewPager;
    TasksRepository mTasksRepository = TasksRepository.getInstance();
    private int mNumOfTabs;
    private TabLayout mTabLayout;
    private TabItem mTabTodo;
    private TabItem mTabDoing;
    private TabItem mTabDone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks_view_pager);

        mTabLayout = findViewById(R.id.tablayout);
        mTabTodo = findViewById(R.id.tab_TODO);
        mTabDoing = findViewById(R.id.tab_DOING);
        mTabDone = findViewById(R.id.tab_DONE);
        mViewPager = findViewById(R.id.activity_containerViewPager);
        mNumOfTabs = mTabLayout.getTabCount();

        //mTabLayout.setupWithViewPager(mViewPager);

        mViewPager.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {

            @Override
            public Fragment getItem(int position) {
                switch (position) {
                    case 0: {
                            return TasksListFragment.newInstance(mTasksRepository.getTaskListTodo(),position);
                    }
                    case 1: {
                            return TasksListFragment.newInstance(mTasksRepository.getTaskListDoing(),position);
                    }
                    case 2: {
                            return TasksListFragment.newInstance(mTasksRepository.getTaskListDone(),position);
                    }
                }
                return null;
            }

            @Override
            public int getCount() {
                return mNumOfTabs;
            }

        });

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        orientation = getResources().getConfiguration().orientation;
    }

    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, TasksViewPagerActivity.class);
        return intent;
    }
}