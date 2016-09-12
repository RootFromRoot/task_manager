package vadimtk5.realm;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CalendarView;
import android.widget.EditText;

import java.util.Date;

public class TaskSetting extends AppCompatActivity {
    private final String TAG = getClass().getSimpleName();

    private Toolbar toolbar;
    private EditText ETName;
    private EditText ETDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_setting);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        ETName = (EditText) findViewById(R.id.editText);
        ETDescription = (EditText) findViewById(R.id.editText2);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                finish();
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void save(View view) {
        if (ETName == null || ETDescription == null) {
            Log.e(TAG, "save: EditText is null");
            return;
        }

        Intent data = new Intent();
        data
                .putExtra("name", ETName.getText().toString())
                .putExtra("description", ETDescription.getText().toString())
                .putExtra("date", new Date()); // TODO Change this, mazafaka

        setResult(RESULT_OK, data);
        finish();
    }
}