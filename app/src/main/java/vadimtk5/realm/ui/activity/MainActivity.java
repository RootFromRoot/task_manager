package vadimtk5.realm.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import vadimtk5.realm.data.broadcast.AlarmManagerBroadcastReceiver;
import vadimtk5.realm.R;
import vadimtk5.realm.data.model.Task;
import vadimtk5.realm.ui.adapter.TaskListAdapter;
import vadimtk5.realm.utils.RequestCodes;

public class MainActivity extends AppCompatActivity {
    private final String TAG = getClass().getSimpleName();

    private AlarmManagerBroadcastReceiver alarm;

    private Toolbar toolbar;
    private List<Task> dataSet = new ArrayList<>();
    private RecyclerView recyclerView;
    private TaskListAdapter adapter;
    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupViews();
        setupToolbar();
        setupRealm();
        setupRecyclerView();
        alarm = new AlarmManagerBroadcastReceiver();
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
    }

    private void setupRealm() {
        Realm.setDefaultConfiguration(new RealmConfiguration.Builder(this)
                .name(Realm.DEFAULT_REALM_NAME)
                .deleteRealmIfMigrationNeeded()
                .schemaVersion(1)
                .build()
        );
        realm = Realm.getDefaultInstance();
    }

    private void setupViews() {
        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
    }


    private List<Task> fetchTasksFromDb() {
        return realm.where(Task.class).findAll();
    }

    private void addTaskToDb(Task task) {
        realm.beginTransaction();
        realm.insert(task);
        realm.commitTransaction();
    }

    private void setupRecyclerView() {
        dataSet.addAll(fetchTasksFromDb());
        adapter = new TaskListAdapter(this, dataSet);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        adapter.setOnClickListener(new TaskListAdapter.OnClickListener() {
            @Override
            public void onClick(int position) {
                Task task = adapter.getDataSet().get(position);

                startActivity(
                        new Intent(MainActivity.this, TaskInfoActivity.class)
                                .putExtra("name", task.getName())
                                .putExtra("description", task.getDescription())
                );
            }

            @Override
            public void onLongClick(int position) {
                Toast.makeText(MainActivity.this, "Long click...", Toast.LENGTH_SHORT).show(); // TODO implement method logic
            }
        });
    }

    public void addNewTask(View view) {
        Intent intent = new Intent(MainActivity.this, TaskSettingActivity.class);
        startActivityForResult(intent, RequestCodes.REQUEST_ADD_TASK);

        recyclerView.scrollToPosition(adapter.getDataSet().size() - 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case RequestCodes.REQUEST_ADD_TASK:
                if (resultCode == RESULT_OK) {
                    try {
                        String name = data.getExtras().getString("name");
                        String description = data.getExtras().getString("description");
                        Date date = ((Date) data.getExtras().getSerializable("date"));

                        Task task = new Task(
                                name,
                                description,
                                date
                        );

                        adapter.addTask(task);
                        addTaskToDb(task);
                    } catch (Exception ex) {
                        Log.e(TAG, "onActivityResult: ", ex);
                        Toast.makeText(MainActivity.this, "Error occurred.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Error occurred.", Toast.LENGTH_SHORT).show();
                    return;
                }
                break;
            default:
                break;
        }
    }
}