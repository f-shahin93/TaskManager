package com.example.taskmanager;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.taskmanager.model.Task;
import com.example.taskmanager.model.User;
import com.example.taskmanager.repository.TasksRepository;
import com.example.taskmanager.repository.UserRepository;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class UserFragment extends Fragment {

    private List<User> mUsersList = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private MyAdapter mAdapter;
    private TasksRepository mTasksRepository;

    public UserFragment() {
        // Required empty public constructor
    }


    public static UserFragment newInstance() {
        UserFragment fragment = new UserFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user, container, false);

        mUsersList = UserRepository.getInstance(getContext()).getUsersList();
        mTasksRepository = TasksRepository.getInstance(getContext());

        mAdapter = new MyAdapter(mUsersList);
        mRecyclerView = view.findViewById(R.id.recycler_user_item);
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        if (mAdapter != null) {
            updateUI();
            /*mAdapter.setTaskListAdapter(mTasksListFragments);
            mAdapter.notifyDataSetChanged();*/
        }


        return view;
    }

    private class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {
        List<User> usersList;

        MyAdapter(List userList) {
            this.usersList = userList;
        }

        public void setUserListAdapter(List<User> users) {
            usersList = users;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getContext());
            View itemView = layoutInflater.inflate(R.layout.user_list_item, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

            SimpleDateFormat localDateFormat1 = new SimpleDateFormat("yyyy/MM/dd" + "  " + "HH:mm");
            holder.mTVDateUser.setText(localDateFormat1.format(usersList.get(position).getDate()));

            holder.mTVUsernameUser.setText(usersList.get(position).getUserName());
            holder.mTVcountTaskUser.setText("Number of Tasks is : " + mTasksRepository.getTasks(usersList.get(position).getUserName()).size());
            holder.bind(usersList.get(position));

        }

        @Override
        public int getItemCount() {
            return usersList.size();
        }
    }

    private class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView mTVUsernameUser;
        private TextView mTVDateUser;
        private TextView mTVcountTaskUser;
        private ImageButton mImageBDeleteUser;
        private TextView mTVitemUser;
        private User mUserVH;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mTVUsernameUser = itemView.findViewById(R.id.tv_username_itemList);
            mTVDateUser = itemView.findViewById(R.id.tv_dateUser_itemList);
            mTVcountTaskUser = itemView.findViewById(R.id.tv_countTaskUser_itemList);
            mImageBDeleteUser = itemView.findViewById(R.id.ImageB_delete_user);
            mTVitemUser = itemView.findViewById(R.id.tv_itemUser);

            mImageBDeleteUser.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    UserRepository.getInstance(getContext()).deleteUser(mUserVH);
                }
            });

        }

        public void bind(User user) {
            mUserVH = user;
            if (!mUserVH.getUserName().equals("")) {
                mTVitemUser.setText(String.valueOf(mUserVH.getUserName().charAt(0)));
            }
        }
    }

    public void updateUI() {

        mUsersList = UserRepository.getInstance(getContext()).getUsersList();

        mAdapter.setUserListAdapter(mUsersList);
        mAdapter.notifyDataSetChanged();

    }


}
