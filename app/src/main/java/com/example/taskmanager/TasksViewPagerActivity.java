package com.example.taskmanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.taskmanager.repository.TasksRepository;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;


public class TasksViewPagerActivity extends AppCompatActivity {

    public static final String EXTRA_USERNAME = "Extra username";
    static int orientation;
    private ViewPager mViewPager;
    TasksRepository mTasksRepository;
    private int mNumOfTabs;
    private TabLayout mTabLayout;
    private TabItem mTabTodo;
    private TabItem mTabDoing;
    private TabItem mTabDone;
    private String username;
    private FragmentStatePagerAdapter mAdapter;
    private List<TasksListFragment> mListFragments = new ArrayList<>();
    private HashMap<String, Fragment> mListHashMap = new HashMap<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks_view_pager);

        mTabLayout = findViewById(R.id.tablayout);
        mTabTodo = findViewById(R.id.tab_TODO);
        mTabDoing = findViewById(R.id.tab_DOING);
        mTabDone = findViewById(R.id.tab_DONE);
        mViewPager = findViewById(R.id.activity_containerViewPager);

        mTasksRepository = TasksRepository.getInstance(getApplicationContext());

        mNumOfTabs = mTabLayout.getTabCount();
        username = getIntent().getStringExtra(EXTRA_USERNAME);


        //mTabLayout.setupWithViewPager(mViewPager);
        mViewPager.setOffscreenPageLimit(0);

        mAdapter = new FragmentStatePagerAdapter(getSupportFragmentManager()) {

            @Override
            public Fragment getItem(int position) {

                switch (position) {
                    case 0: {
                        return TasksListFragment.newInstance(mTasksRepository.getTasksList(0, username), position, username);
                    }
                    case 1: {
                        return TasksListFragment.newInstance(mTasksRepository.getTasksList(1, username), position, username);
                    }
                    case 2: {
                        return TasksListFragment.newInstance(mTasksRepository.getTasksList(2, username), position, username);
                    }
                }


                /*switch (position) {
                    case 0: {
                        TasksListFragment fragment0 = TasksListFragment.newInstance(mTasksRepository.getTasksList(0, username), position, username);
                        mListFragments.add(fragment0);
                        mListHashMap.put("TODO", fragment0);
                        return fragment0;
                    }
                    case 1: {
                        TasksListFragment fragment1 = TasksListFragment.newInstance(mTasksRepository.getTasksList(1, username), position, username);
                        mListFragments.add(fragment1);
                        mListHashMap.put("DOING", fragment1);
                        return fragment1;
                    }
                    case 2: {
                        TasksListFragment fragment2 = TasksListFragment.newInstance(mTasksRepository.getTasksList(2, username), position, username);
                        mListFragments.add(fragment2);
                        mListHashMap.put("DONE", fragment2);
                        return fragment2;
                    }
                }*/
                return null;
            }

            @Override
            public int getCount() {
                return mNumOfTabs;
            }

        };
        mViewPager.setAdapter(mAdapter);


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


        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mAdapter.notifyDataSetChanged();

                //((TasksListFragment) (mViewPager.getAdapter()).getItem(position)).updateUI();

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

/*
 @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Fragment createdFragment = (Fragment) super.instantiateItem(container, position);
            // save the appropriate reference depending on position
            switch (position) {
                case 0:
                    m1stFragment = (FragmentA) createdFragment;
                    break;
                case 1:
                    m2ndFragment = (FragmentB) createdFragment;
                    break;
            }
            return createdFragment;
        }
 */


    public static Intent newIntent(Context context, String username) {
        Intent intent = new Intent(context, TasksViewPagerActivity.class);
        intent.putExtra(EXTRA_USERNAME, username);
        return intent;
    }

    /*public void updateAllFragment() {

        //mViewPager.getAdapter().notifyDataSetChanged();
        *//*FragmentManager fragmentManager = getSupportFragmentManager();

        for (int i = 0; i < fragmentManager.getFragments().size(); i++) {
            if (fragmentManager.getFragments().get(i).getTag() == TasksListFragment.TAG) {
                TasksListFragment tasksListFragment = (TasksListFragment) fragmentManager.getFragments().get(i);

                //tasksListFragment.updateUI();
                tasksListFragment.myUpdate( mTasksRepository.getTasksList(i, username));
            }
        }*//*

        for (int i = 0; i < mAdapter.getCount(); i++) {
//            TasksListFragment fragment = (TasksListFragment) mAdapter.getItem(i);
            Fragment fragment = getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.activity_containerViewPager + ":" + i);
            if (fragment instanceof TasksListFragment) {
                TasksListFragment tasksListFragment = (TasksListFragment) fragment;
                tasksListFragment.updateUI();
            }
        }
    }*/

}