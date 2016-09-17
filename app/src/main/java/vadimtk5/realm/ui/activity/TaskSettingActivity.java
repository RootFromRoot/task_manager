package vadimtk5.realm.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import java.util.Date;

import vadimtk5.realm.data.broadcast.AlarmManagerBroadcastReceiver;
import vadimtk5.realm.R;

public class TaskSettingActivity extends AppCompatActivity {
    private final String TAG = getClass().getSimpleName();

    private Toolbar toolbar;
    private EditText ETName;
    private EditText ETDescription;
    private DatePicker datePicker;
    public Date AlarmTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_setting);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        ETName = (EditText) findViewById(R.id.editText);
        ETDescription = (EditText) findViewById(R.id.editText2);
        datePicker = (DatePicker) findViewById(R.id.date_picker);

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
                .putExtra("date", new Date(datePicker.getDrawingTime())); // TODO Change this, mazafaka

        AlarmManagerBroadcastReceiver.setAlarm(this, datePicker.getDrawingTime());

        setResult(RESULT_OK, data);
        finish();
    }
}