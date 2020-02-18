package com.example.taskmanager.controller;


import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;


import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.taskmanager.R;
import com.example.taskmanager.model.Task;
import com.example.taskmanager.model.User;
import com.example.taskmanager.repository.TasksRepository;
import com.example.taskmanager.repository.UserRepository;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import static android.provider.MediaStore.ACTION_IMAGE_CAPTURE;


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
    public static final int REQUEST_CODE_CAPTURE_IMAGE = 3;
    public static final int REQUEST_IMAGE_GET = 4;
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
    private File mPhotoFile;
    private Uri mPhotoUri;
    private String photoPath = "";
    private Bitmap mBitmap;
    private ImageButton mIbPhotoTask;
    private boolean mIsAdmin;


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
        mIbPhotoTask = new ImageButton(getContext());

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


        User user = UserRepository.getInstance(getContext()).getUser(mUsername);
        if (user.equals("admin") && user.getPassword().equals("123456")) {
            mIsAdmin = true;
        } else {
            mIsAdmin = false;
        }

        if (mIsAdmin) {
            mTasksListFragments = mTasksRepository.getTasksList(mNumCurentPage, mUsername);
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
        public static final String AUTHORITY_FILE_PROVIDER = "com.example.taskmanager.fileProvider";
        private TextView mTVTitleTask;
        private TextView mTVDateTask;
        private ImageView mIbPhotoTask;
        private Task mTaskVH;
        private ImageButton mIBshare;
        private ImageButton mIBedit;
        private ImageButton mIBtakePhoto;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mTVTitleTask = itemView.findViewById(R.id.tv_title_itemList);
            mTVDateTask = itemView.findViewById(R.id.tv_date_itemList);
            mIbPhotoTask = itemView.findViewById(R.id.ImageV_itemTask);
            mIBshare = itemView.findViewById(R.id.ImageB_share);
            mIBedit = itemView.findViewById(R.id.ImageB_edit);
            mIBtakePhoto = itemView.findViewById(R.id.ImageB_take_photo);


            mIBedit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    EditInfoTaskFragment editInfoTaskFragment = EditInfoTaskFragment.newInstance(mNumCurentPage, mTaskVH.getUUID());
                    editInfoTaskFragment.setTargetFragment(TasksListFragment.this, REQUEST_CODE_EditInfo);
                    editInfoTaskFragment.show(getFragmentManager(), TAG_EDIT_INFO);
                    mAdapter.notifyDataSetChanged();
                }
            });

            mIBshare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    /*Intent shareIntent = ShareCompat.IntentBuilder.from(getActivity())
                            .setType("text/plain")
                            .setText(getTaskShare(mTaskVH))
                            .setSubject(getString(R.string.task_share_subject))
                            .setChooserTitle(R.string.send_task)
                            .createChooserIntent();

                    if (shareIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                        startActivity(shareIntent);
                    }*/

                    //implicit intent to send text to other apps
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("text/plain");
                    intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.task_share_subject));
                    intent.putExtra(Intent.EXTRA_TEXT, getTaskShare(mTaskVH));
                    intent = Intent.createChooser(intent, getString(R.string.send_task));
                    startActivity(intent);

                }
            });

            mIBtakePhoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                   /* if (mTaskVH != null)
                        mPhotoFile = TasksRepository.getInstance(getContext()).getPhotoFile(mTaskVH);

                   /* showPictureDialog(mTaskVH);
                    mTaskVH.setPhotoPath(photoPath);
                    mIbPhotoTask.setImageBitmap(mBitmap);*/

                   /* if (mPhotoFile == null)
                        return;

                    mPhotoUri = FileProvider.getUriForFile(getContext(),
                            AUTHORITY_FILE_PROVIDER,
                            mPhotoFile);

                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, mPhotoUri);*/

                    /*List<ResolveInfo> cameraActivities = getActivity().getPackageManager()
                            .queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
                    for (ResolveInfo resolveInfo : cameraActivities) {
                        getActivity().grantUriPermission(resolveInfo.activityInfo.packageName,
                                mPhotoUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                    }*/

                  //  startActivityForResult(intent, REQUEST_CODE_CAPTURE_IMAGE);

                    //updatePhotoView();
                    //mTaskVH.setPhotoPath(photoPath);
                    //mIbPhotoTask.setImageBitmap(mBitmap);


                }
            });
        }

        public void bind(Task task) {
            mTaskVH = task;
            if(photoPath != null && mBitmap!= null ){
                mTaskVH.setPhotoPath(photoPath);
                mIbPhotoTask.setImageBitmap(mBitmap);
            }

            /*if (!mTaskVH.getTaskTitle().equals("")) {
                mTask = mTaskVH;
                mIbPhotoTask.setBackgroundResource(mTask.getTaskTitle().charAt(0));
                //mIbPhotoTask.setText(String.valueOf(mTaskVH.getTaskTitle().charAt(0)));
            }*/
            /*if (mPhotoFile == null || !mPhotoFile.exists()) {
               // mIbPhotoTask.setBackgroundResource(mTaskVH.getTaskTitle().charAt(0));
            } else {
                Bitmap bitmap = PictureUtils.getScaledBitmap(mPhotoFile.getAbsolutePath(), getActivity());
                mIbPhotoTask.setImageBitmap(bitmap);
            }*/
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_CAPTURE_IMAGE) {
            Log.d("MyLog","jhkhj");

            updatePhotoView();

            getActivity().revokeUriPermission(
                    mPhotoUri,
                    Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        }


        if (resultCode == Activity.RESULT_CANCELED || data == null)
            return;

        if (requestCode == REQUEST_CODE_AddInfo) {
            updateUI();
        }

        if (requestCode == REQUEST_CODE_EditInfo) {
            updateUI();

        }

        if (requestCode == REQUEST_IMAGE_GET) {
            Uri fullPhotoUri = data.getData();
            //mIbPhotoTask.setImageBitmap(BitmapFactory.decodeFile(setPathForGallery(fullPhotoUri)));
            //mTask.setPhotoPath(setPathForGallery(fullPhotoUri));
            mBitmap = BitmapFactory.decodeFile(setPathForGallery(fullPhotoUri));
            photoPath = setPathForGallery(fullPhotoUri);
        }

    }

    private void updatePhotoView() {
        if (mPhotoFile == null || !mPhotoFile.exists()) {
            //mIbPhotoTask.setBackgroundResource(mTask.getTaskTitle().charAt(0));
        } else {
            Bitmap bitmap = PictureUtils.getScaledBitmap(mPhotoFile.getAbsolutePath(), getActivity());
            //mIbPhotoTask.setImageBitmap(bitmap);
            //mTask.setPhotoPath(mPhotoFile.getAbsolutePath());
            mBitmap = bitmap;
            photoPath = mPhotoFile.getAbsolutePath();
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

        if (mIsAdmin) {
            mTasksListFragments = mTasksRepository.getTasksList(mNumCurentPage, mUsername);
        }

        if (mTasksListFragments.size() > 0)
            mIVbackEmptyList.setVisibility(View.GONE);
        else if (mTasksListFragments.size() == 0) {
            mIVbackEmptyList.setVisibility(View.VISIBLE);
        }

        mAdapter.setTaskListAdapter(mTasksListFragments);
        mAdapter.notifyDataSetChanged();
    }


    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
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

                    if (mTask != null) {
                        EditInfoTaskFragment editInfoTaskFragment = EditInfoTaskFragment.newInstance(mNumCurentPage, mTask.getUUID());
                        editInfoTaskFragment.setTargetFragment(TasksListFragment.this, REQUEST_CODE_EditInfo);
                        editInfoTaskFragment.show(getFragmentManager(), TAG_EDIT_INFO);
                    } else if (task != null) {
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
                return true;
            }
            case R.id.search_menu_item: {

                return true;
            }
            case R.id.logOut_menu_item: {
                getActivity().finish();
                return true;
            }
            case R.id.admin_menu_item: {
                if (mUsername.equals("admin") && UserRepository.getInstance(getContext()).getUser(mUsername).getPassword().equals("123456")) {
                    startActivity(UserActivity.newIntent(getContext()));
                }
                Toast.makeText(getActivity(), "only Admin see this feature!", Toast.LENGTH_SHORT).show();
            }
            default: {
                return super.onOptionsItemSelected(item);
            }
        }
    }

    private String getTaskShare(Task task) {
        String dateString = new SimpleDateFormat("yyyy/MM/dd").format(task.getDate());
        String timeString = new SimpleDateFormat("HH:mm").format(task.getTime());

        return getString(R.string.task_share,
                task.getTaskTitle(),
                task.getDescription(),
                dateString,
                timeString,
                task.getState().name());
    }

    private void showPictureDialog(final Task task) {
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(getActivity());
        pictureDialog.setTitle("Select Action");
        String[] pictureDialogItems = {
                "From Gallery",
                "From Camera"};
        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                choosePhotoFromGallery();
                                break;
                            case 1:
                                takePhotoFromCamera(task);
                                break;
                        }
                    }
                });
        pictureDialog.show();
    }

    public void choosePhotoFromGallery() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            intent = Intent.createChooser(intent, "Take picture");
            startActivityForResult(intent, REQUEST_IMAGE_GET);
        }
    }

    private void takePhotoFromCamera(Task task) {
        Intent intent = new Intent(ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            // Create the File where the photo should go
            mPhotoFile = TasksRepository.getInstance(getContext()).getPhotoFile(task);
            // Continue only if the File was successfully created
            if (mPhotoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(getContext(),
                        "com.example.taskmanager",
                        mPhotoFile);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                intent = Intent.createChooser(intent, "Capture photo");
                startActivityForResult(intent, REQUEST_CODE_CAPTURE_IMAGE);
            }
        }
    }

    private String setPathForGallery(Uri uri) {
        String path = null;
        if (Build.VERSION.SDK_INT < 11)
            path = RealPathUtils.getRealPathFromURI_BelowAPI11(getContext(), uri);

            // SDK >= 11 && SDK < 19
        else if (Build.VERSION.SDK_INT < 19)
            path = RealPathUtils.getRealPathFromURI_API11to18(getContext(), uri);

            // SDK > 19 (Android 4.4)
        else
            path = RealPathUtils.getRealPathFromURI_API19(getContext(), uri);

        Log.e(TAG, "setPathForGallery: PATH= " + path);
        return path;
    }


    /*  @Override
      public void onSaveInstanceState(@NonNull Bundle outState) {
          super.onSaveInstanceState(outState);
          Log.d(TAG, "onSave()" + " " + mNumCurentPage);
          if (mAdapter != null) {
              updateUI();
          }
      }
  */
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