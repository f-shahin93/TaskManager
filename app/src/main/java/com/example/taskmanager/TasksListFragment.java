package com.example.taskmanager;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.databinding.DataBindingUtil;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.taskmanager.model.Task;
import com.example.taskmanager.repository.TasksRepository;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class TasksListFragment extends Fragment {

    public static final String ARG_LIST = "ARG_list";
    public static final int REQUEST_CODE_AddInfo = 1;
    public static final int REQUEST_CODE_EditInfo = 2;
    public static final String TAG_ADD_INFO = "Tag add Info";
    public static final String TAG_EDIT_INFO = "Tag edit Info";
    public static final String TAG = "ListFragment";
    public static final String ARG_NUM_CURENT_PAGE = "Arg numCurentPage";
    public static final String ARG_USERNAME = "Arg username";
    private List<Task> mTasksListFragments = new ArrayList();
    private RecyclerView mRecyclerView;
    private MyAdapter mAdapter;
    private FloatingActionButton mFloatingActionButton;
    private TasksRepository mTasksRepository = TasksRepository.getInstance(getContext());
    private int mNumCurentPage;
    private ImageView mIVbackEmptyList;
    private String mUsername;
    private Task mTask;
    private SearchView mSearchView;


    public static TasksListFragment newInstance(List list, int numCurentPage, String username) {
        TasksListFragment fragment = new TasksListFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_LIST, (Serializable) list);
        args.putInt(ARG_NUM_CURENT_PAGE, numCurentPage);
        args.putString(ARG_USERNAME, username);
        fragment.setArguments(args);
        return fragment;
    }

    public TasksListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate()" + " " + mNumCurentPage);
        if (getArguments() != null) {
            mTasksListFragments = (List) getArguments().getSerializable(ARG_LIST);
            mNumCurentPage = getArguments().getInt(ARG_NUM_CURENT_PAGE);
            mUsername = getArguments().getString(ARG_USERNAME);
        }
        setHasOptionsMenu(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tasks_list, container, false);
        Log.d(TAG, "onCreateView()" + " " + mNumCurentPage);
        mFloatingActionButton = view.findViewById(R.id.floatingaction_add);
        mIVbackEmptyList = view.findViewById(R.id.backEmptyTask);

        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AddInfoTaskFragment addInfoTaskFragment = AddInfoTaskFragment.newInstance(mNumCurentPage, mUsername);
                addInfoTaskFragment.setTargetFragment(TasksListFragment.this, REQUEST_CODE_AddInfo);
                addInfoTaskFragment.show(getFragmentManager(), TAG_ADD_INFO);
                mAdapter.notifyDataSetChanged();

            }
        });

        // set background empty list
        if (mTasksListFragments.size() > 0)
            mIVbackEmptyList.setVisibility(View.GONE);
        else if (mTasksListFragments.size() == 0) {
            mIVbackEmptyList.setVisibility(View.VISIBLE);
        }


        mAdapter = new MyAdapter(mTasksListFragments);
        mRecyclerView = view.findViewById(R.id.recycler_item);
        mRecyclerView.setAdapter(mAdapter);


        if (TasksViewPagerActivity.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        } else if (TasksViewPagerActivity.orientation == Configuration.ORIENTATION_PORTRAIT)
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        if (mAdapter != null) {
            updateUI();
            /*mAdapter.setTaskListAdapter(mTasksListFragments);
            mAdapter.notifyDataSetChanged();*/
        }

        return view;
    }

    private class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {
        List<Task> taskList;

        MyAdapter(List taskList) {
            this.taskList = taskList;
        }

        public void setTaskListAdapter(List<Task> tasks) {
            taskList = tasks;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View itemView = layoutInflater.inflate(R.layout.tasks_list_item, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

            SimpleDateFormat localDateFormat1 = new SimpleDateFormat("yyyy/MM/dd" + "  " + "HH:mm");
            holder.mTVDateTask.setText(localDateFormat1.format(taskList.get(position).getDate()));

            holder.mTVTitleTask.setText(taskList.get(position).getTaskTitle());
            // holder.mTVDateTask.setText(taskList.get(position).getDate().toString());
            holder.bind(taskList.get(position));

        }

        @Override
        public int getItemCount() {
            return taskList.size();
        }
    }

    private class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView mTVTitleTask;
        private TextView mTVDateTask;
        private TextView mTVitem;
        private Task mTaskVH;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mTVTitleTask = itemView.findViewById(R.id.tv_title_itemList);
            mTVDateTask = itemView.findViewById(R.id.tv_date_itemList);
            mTVitem = itemView.findViewById(R.id.ImageV_itemTask);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    EditInfoTaskFragment editInfoTaskFragment = EditInfoTaskFragment.newInstance(mNumCurentPage, mTaskVH.getUUID());
                    editInfoTaskFragment.setTargetFragment(TasksListFragment.this, REQUEST_CODE_EditInfo);
                    editInfoTaskFragment.show(getFragmentManager(), TAG_EDIT_INFO);
                    mAdapter.notifyDataSetChanged();
                }
            });

        }

        public void bind(Task task) {
            mTaskVH = task;
            if (!mTaskVH.getTaskTitle().equals("")) {
                mTVitem.setText(String.valueOf(mTaskVH.getTaskTitle().charAt(0)));
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_CANCELED || data == null)
            return;

        if (requestCode == REQUEST_CODE_AddInfo) {
            updateUI();
        }

        if (requestCode == REQUEST_CODE_EditInfo) {
            updateUI();

        }
    }

    public void updateUI() {

        switch (mNumCurentPage) {
            case 0:
                mTasksListFragments = mTasksRepository.getTasksList(0, mUsername);
                break;
            case 1:
                mTasksListFragments = mTasksRepository.getTasksList(1, mUsername);
                break;
            case 2:
                mTasksListFragments = mTasksRepository.getTasksList(2, mUsername);
                break;
        }
        if (mTasksListFragments.size() > 0)
            mIVbackEmptyList.setVisibility(View.GONE);
        else if (mTasksListFragments.size() == 0) {
            mIVbackEmptyList.setVisibility(View.VISIBLE);
        }

        mAdapter.setTaskListAdapter(mTasksListFragments);
        mAdapter.notifyDataSetChanged();

    }

    public void myUpdate(List list) {
        if (mTasksListFragments.size() > 0)
            mIVbackEmptyList.setVisibility(View.GONE);
        else if (mTasksListFragments.size() == 0) {
            mIVbackEmptyList.setVisibility(View.VISIBLE);
        }

        mAdapter.setTaskListAdapter(list);
        mAdapter.notifyDataSetChanged();

    }

    public void secondUpdate(int i) {

        switch (i) {
            case 0:
                mTasksListFragments = mTasksRepository.getTasksList(0, mUsername);
                break;
            case 1:
                mTasksListFragments = mTasksRepository.getTasksList(1, mUsername);
                break;
            case 2:
                mTasksListFragments = mTasksRepository.getTasksList(2, mUsername);
                break;
        }
        if (mTasksListFragments.size() > 0)
            mIVbackEmptyList.setVisibility(View.GONE);
        else if (mTasksListFragments.size() == 0) {
            mIVbackEmptyList.setVisibility(View.VISIBLE);
        }

        mAdapter.setTaskListAdapter(mTasksListFragments);
        mAdapter.notifyDataSetChanged();

    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.item_menu, menu);

        mSearchView = (SearchView) menu.findItem(R.id.search_menu_item).getActionView();

        mSearchView.setQueryHint("Search:");
        mSearchView.setSubmitButtonEnabled(true);
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                if (query != null) {

                    mTask = TasksRepository.getInstance(getContext()).searchTask(query, mUsername);
                    Task task = TasksRepository.getInstance(getContext()).getDateTask(query, mUsername);

                    if (mTask != null ) {
                        EditInfoTaskFragment editInfoTaskFragment = EditInfoTaskFragment.newInstance(mNumCurentPage, mTask.getUUID());
                        editInfoTaskFragment.setTargetFragment(TasksListFragment.this, REQUEST_CODE_EditInfo);
                        editInfoTaskFragment.show(getFragmentManager(), TAG_EDIT_INFO);
                    }else if(task != null){
                        EditInfoTaskFragment editInfoTaskFragment = EditInfoTaskFragment.newInstance(task.getUUID());
                        editInfoTaskFragment.setTargetFragment(TasksListFragment.this, REQUEST_CODE_EditInfo);
                        editInfoTaskFragment.show(getFragmentManager(), TAG_EDIT_INFO);
                    }
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //    adapter.getFilter().filter(newText);
                return true;
            }
        });

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.deleteAccount_menu_item: {
                AlertDialog alert = new AlertDialog.Builder(getActivity())
                        .setMessage("Are you sure delete all tasks ?")
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                TasksRepository.getInstance(getContext()).deleteAll();
                                if (mNumCurentPage == 1) {
                                    mTasksListFragments = new ArrayList();
                                    mIVbackEmptyList.setVisibility(View.VISIBLE);
                                    mAdapter.setTaskListAdapter(mTasksListFragments);
                                    mAdapter.notifyDataSetChanged();
                                }
                                updateUI();
                            }
                        })
                        .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        }).create();
                alert.show();
                //getActivity().recreate();
                return true;
            }
            case R.id.search_menu_item: {

                return true;
            }
            case R.id.logOut_menu_item: {
                getActivity().finish();
                return true;
            }
            default: {
                return super.onOptionsItemSelected(item);
            }
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(TAG, "onSave()" + " " + mNumCurentPage);
        if (mAdapter != null) {
            updateUI();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mAdapter != null) {
            updateUI();
        }
        Log.d(TAG, "onResume()" + " " + mNumCurentPage);
        Bundle bundle = new Bundle();
        bundle.putSerializable("Bundle list", (Serializable) mTasksListFragments);
        onSaveInstanceState(bundle);
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onStop()" + " " + mNumCurentPage);
        if (mAdapter != null) {
            updateUI();
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        Log.d(TAG, "onStart()" + " " + mNumCurentPage);
    }


    @Override
    public void onStop() {
        super.onStop();

        Log.d(TAG, "onStop()" + " " + mNumCurentPage);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        Log.d(TAG, "onDestroy()" + " " + mNumCurentPage);
    }


}