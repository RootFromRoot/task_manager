package vadimtk5.realm.ui.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import vadimtk5.realm.R;
import vadimtk5.realm.data.model.Task;
import vadimtk5.realm.ui.adapter.TaskListAdapter;

public class TaskInfoActivity extends AppCompatActivity {
    private final String TAG = getClass().getSimpleName();

    private Toolbar toolbar;
    private Realm realm;
    private TaskListAdapter adapter;
    private TextView TVName;
    private TextView TVDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_info);

        initView();
        setupToolbar();
        setupLayout();
        setupRealm();
    }

    private void setupLayout() {
        if (getIntent() == null || getIntent().getExtras() == null) {
            Log.e(TAG, "setupLayout()-> Intent is null");
            return;
        }

        String name = getIntent().getExtras().getString("name");
        String description = getIntent().getExtras().getString("description");


        TVName.setText(name);
        TVDescription.setText(description);
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        TVName = (TextView) findViewById(R.id.tv_name);
        TVDescription = (TextView) findViewById(R.id.tv_description);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.look_task_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_delete:
                int position = getIntent().getExtras().getInt("position");
                final Task task = realm.where(Task.class).equalTo("id", adapter.getDataSet().get(position).getId()).findFirst();

                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                       task.deleteFromRealm();

                    }
                });
                return true;


            case R.id.action_edit:
                Intent intent = new Intent(TaskInfoActivity.this,TaskCreateActivity.class);
                startActivity(intent);
                String name = TVName.getText().toString();
                String description = TVDescription.getText().toString();
               intent.putExtra("name",name);//transfer to TaskInfo.activity
                intent.putExtra("description",description);//transfer to TaskInfo.activity
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    private void deleteTask(int position){




        // adapter.removeTask(adapter.getDataSet().get(position));
    }
}
