package vadimtk5.realm.ui.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import vadimtk5.realm.R;

public class TaskInfoActivity extends AppCompatActivity {
    private final String TAG = getClass().getSimpleName();

    private Toolbar toolbar;

    private TextView TVName;
    private TextView TVDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_info);

        initView();
        setupToolbar();
        setupLayout();
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
            case R.id.action_edit:
                Intent intent = new Intent(TaskInfoActivity.this,TaskCreateActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
