package com.example.taskmanager;


import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.taskmanager.model.Task;
import com.example.taskmanager.repository.TasksRepository;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.Serializable;
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
    private List mTasksListFragments = new ArrayList();
    private RecyclerView mRecyclerView;
    private MyAdapter mAdapter;
    private FloatingActionButton mFloatingActionButton;
    private TasksRepository mTasksRepository = TasksRepository.getInstance();
    private int mNumCurentPage;


    public static TasksListFragment newInstance(List list, int numCurentPage) {
        TasksListFragment fragment = new TasksListFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_LIST, (Serializable) list);
        args.putInt(ARG_NUM_CURENT_PAGE, numCurentPage);
        fragment.setArguments(args);
        return fragment;
    }

    public TasksListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mTasksListFragments = (List) getArguments().getSerializable(ARG_LIST);
            mNumCurentPage = getArguments().getInt(ARG_NUM_CURENT_PAGE);
        }
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tasks_list, container, false);

        /*if(mTasksListFragments.size()==0){
            view.setBackgroundResource(R.drawable.ic_action_add);
        }*/

        mFloatingActionButton = view.findViewById(R.id.floatingaction_add);

        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AddInfoTaskFragment addInfoTaskFragment = AddInfoTaskFragment.newInstance(mNumCurentPage);
                addInfoTaskFragment.setTargetFragment(TasksListFragment.this, REQUEST_CODE_AddInfo);
                addInfoTaskFragment.show(getFragmentManager(), TAG_ADD_INFO);

            }
        });

        mAdapter = new MyAdapter(mTasksListFragments);
        mRecyclerView = view.findViewById(R.id.recycler_item);
        mRecyclerView.setAdapter(mAdapter);

        if (TasksViewPagerActivity.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        } else if (TasksViewPagerActivity.orientation == Configuration.ORIENTATION_PORTRAIT)
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

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

            holder.mTVTitleTask.setText(taskList.get(position).getTaskTitle());
            holder.mTVDateTask.setText(taskList.get(position).getDate().toString());
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
                    EditInfoTaskFragment editInfoTaskFragment = EditInfoTaskFragment.newInstance(mNumCurentPage ,mTaskVH.getID());
                    editInfoTaskFragment.setTargetFragment(TasksListFragment.this, REQUEST_CODE_EditInfo);
                    editInfoTaskFragment.show(getFragmentManager(), TAG_EDIT_INFO);
                }
            });

        }
         public void bind(Task task){
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

    private void updateUI() {

        switch (mNumCurentPage) {
            case 0:
                mTasksListFragments = mTasksRepository.getTaskListTodo();
                break;
            case 1:
                mTasksListFragments = mTasksRepository.getTaskListDoing();
                break;
            case 2:
                mTasksListFragments = mTasksRepository.getTaskListDone();
                break;
        }

        mAdapter.setTaskListAdapter(mTasksListFragments);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu , MenuInflater inflater) {
        super.onCreateOptionsMenu(menu ,inflater);
        inflater.inflate(R.menu.item_menu ,menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.id_deleteAccount: {
                TasksRepository.getInstance().deleteAll();
                mTasksListFragments = TasksRepository.getInstance().getTaskListDoing();
                mAdapter.notifyDataSetChanged();
                //getActivity().finish();
            }
            default: {
                return super.onOptionsItemSelected(item);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        mAdapter.notifyDataSetChanged();
        Log.d(TAG, "onResume()");
        Bundle bundle = new Bundle();
        bundle.putSerializable("Bundle list", (Serializable) mTasksListFragments);
        onSaveInstanceState(bundle);
    }
}