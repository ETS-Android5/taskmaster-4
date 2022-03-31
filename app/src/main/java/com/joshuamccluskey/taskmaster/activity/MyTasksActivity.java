package com.joshuamccluskey.taskmaster.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.core.model.temporal.Temporal;
import com.amplifyframework.datastore.generated.model.Task;
import com.amplifyframework.datastore.generated.model.Team;
import com.joshuamccluskey.taskmaster.R;
import com.joshuamccluskey.taskmaster.adapter.MyTasksListRecyclerViewAdapter;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;



public class MyTasksActivity extends AppCompatActivity {

    public final String TAG = "MainActivity";
    public static String TASK_DETAIL_TITLE_TAG = "TASK DETAIL TITLE";
    public static String TASK_DETAIL_BODY_TAG = "TASK BODY";
    public static String TASK_DETAIL_STATE_TAG = "TASK STATE";
    SharedPreferences userPreferences;
    MyTasksListRecyclerViewAdapter myTasksListRecyclerViewAdapter;
    List<Task> tasksList = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_tasks);
        userPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        tasksList = new ArrayList<>();
//        tasksList = taskMasterDatabase.taskDao().findAll();
//        String currentDate = com.amazonaws.util.DateUtils.formatISO8601Date(new Date());
//        com.amplifyframework.datastore.generated.model.Task tasker =
//            com.amplifyframework.datastore.generated.model.Task.builder()
//                .title("Taxes")
//                .body("Due this week!")
//                .state(com.amplifyframework.datastore.generated.model.StateEnum.New)
//                .date(new Temporal.DateTime(currentDate))
//                .build();
//        Amplify.API.mutate(
//                ModelMutation.create(tasker),
//                successResponse -> Log.i(TAG, "MyTaskActivity.onCreate: made a Task"),
//                failureResponse -> Log.i(TAG, "MyTaskActivity.onCreate: failed" + failureResponse)
//        );

//        Team codeForLife =
//            Team.builder()
//                .teamName("Code For Life")
//                .build();
//        Amplify.API.mutate(
//                ModelMutation.create(codeForLife),
//                successResponse -> Log.i(TAG, "MyTaskActivity.onCreate: made a Team"),
//                failureResponse -> Log.i(TAG, "MyTaskActivity.onCreate: failed" + failureResponse)
//        );
//        Team codeNinjas =
//                Team.builder()
//                        .teamName("Code Ninjas")
//                        .build();
//        Amplify.API.mutate(
//                ModelMutation.create(codeNinjas),
//                successResponse -> Log.i(TAG, "MyTaskActivity.onCreate: made a Team"),
//                failureResponse -> Log.i(TAG, "MyTaskActivity.onCreate: failed" + failureResponse)
//        );
//        Team codeBuilders =
//                Team.builder()
//                        .teamName("Code Builders")
//                        .build();
//        Amplify.API.mutate(
//                ModelMutation.create(codeBuilders),
//                successResponse -> Log.i(TAG, "MyTaskActivity.onCreate: made a Team"),
//                failureResponse -> Log.i(TAG, "MyTaskActivity.onCreate: failed" + failureResponse)
//        );

        addTaskButtonSetUp();
        allTasksButtonSetUp();
        settingsImageButtonSetUp();
        myTasksListRecycleViewSetUp();


    }


        // onResume needs to be outside of on Create all methods taken out of onCreate
        @Override
        public void onResume(){
            super.onResume();

            String username = userPreferences.getString(SettingsActivity.USERNAME_TAG, "No Username");
            ((TextView)findViewById(R.id.usernameTextView)).setText(getString(R.string.username_username, username));

            Amplify.API.query(
                    ModelQuery.list(Task.class),
                    success -> {
                        Log.i(TAG, "Task successfully created");
                        tasksList.clear();
                        for (Task databaseTask :success.getData()) {
                            tasksList.add(databaseTask);
                        }
                        runOnUiThread(() ->{
                            myTasksListRecyclerViewAdapter.notifyDataSetChanged();
                        });

                    },
                    failure -> Log.i(TAG, "Task creation failed ")
            );


            myTasksListRecycleViewSetUp();
        }

        public void addTaskButtonSetUp(){
            Button addTaskButton = findViewById(R.id.goToAddTaskButton);
            addTaskButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    System.out.println("Add Task Button!");
                    Log.e(TAG, "onClick: Add Task Button!");
                    Intent goToAddTaskIntent = new Intent(MyTasksActivity.this, AddTaskActivity.class);
                    MyTasksActivity.this.startActivity(goToAddTaskIntent);
                }
            });
        }

        public void allTasksButtonSetUp() {

            Button allTasksButton = findViewById(R.id.allTasksButton);
            allTasksButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    System.out.println("All Tasks Button!");
                    Log.e(TAG, "onClick: All Tasks Button!");
                    Intent goToAllTasksIntent = new Intent(MyTasksActivity.this, AllTasksActivity.class);
                    startActivity(goToAllTasksIntent);
                }
            });
        }

        public void settingsImageButtonSetUp() {
            ImageButton settingsImageButton = findViewById(R.id.settingsImageButton);
            settingsImageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    System.out.println("Settings Image Button!");
                    Log.e(TAG, "onClick: Settings Image Button!");
                    Intent goToSettingsIntent = new Intent(MyTasksActivity.this, SettingsActivity.class);
                    startActivity(goToSettingsIntent);
                }
            });
        }


        public void myTasksListRecycleViewSetUp(){
            RecyclerView myTasksListRecycleView = findViewById(R.id.tasksListRecycleView);

            RecyclerView.LayoutManager taskLayoutManager =  new LinearLayoutManager(this);

            myTasksListRecycleView.setLayoutManager(taskLayoutManager);

            myTasksListRecyclerViewAdapter = new MyTasksListRecyclerViewAdapter(tasksList, this);

            myTasksListRecycleView.setAdapter(myTasksListRecyclerViewAdapter);
        }
    }
