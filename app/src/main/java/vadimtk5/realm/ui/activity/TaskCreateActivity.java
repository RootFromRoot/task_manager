package vadimtk5.realm.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerController;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import vadimtk5.realm.data.broadcast.AlarmManagerBroadcastReceiver;
import vadimtk5.realm.R;

public class TaskCreateActivity extends AppCompatActivity {
    private final String TAG = getClass().getSimpleName();

    private Toolbar toolbar;

    private EditText ETName;
    private EditText ETDescription;
    private LinearLayout LLDateTimeContainer;
    private TextView TVDate;
    private TextView TVTime;

    private Calendar alarmCalendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_create);

        initView();
        getExtras();
        setupToolbar();
        setupLayout();
    }

    private void getExtras() {
        if (getIntent() == null || getIntent().getExtras() == null) {
            Log.e(TAG, "getExtras: Extras is null");
            return;
        }

        if (ETName != null) {
            ETName.setText(getIntent().getExtras().getString("name"));
        }//transfer from TaskInfo.activity
        if (ETDescription != null) {
            ETName.setText(getIntent().getExtras().getString("description"));
        }//transfer from TaskInfo.activity
    }

    private void setupLayout() {
        alarmCalendar = Calendar.getInstance();
        updateDrawingDateTime();

        LLDateTimeContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar now = Calendar.getInstance();

                final TimePickerDialog timePickerDialog = TimePickerDialog.newInstance(
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {
                                alarmCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                alarmCalendar.set(Calendar.MINUTE, minute);
                                alarmCalendar.set(Calendar.SECOND, second);

                                updateDrawingDateTime();
                            }
                        },
                        now.get(Calendar.HOUR_OF_DAY),
                        now.get(Calendar.MINUTE),
                        true
                );

                final DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                                alarmCalendar.set(Calendar.YEAR, year);
                                alarmCalendar.set(Calendar.MONTH, monthOfYear);
                                alarmCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                                timePickerDialog.show(getFragmentManager(), "TimePickerDialog");
                            }
                        },
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );

                datePickerDialog.setAccentColor(ContextCompat.getColor(TaskCreateActivity.this, R.color.colorPrimary));
                timePickerDialog.setAccentColor(ContextCompat.getColor(TaskCreateActivity.this, R.color.colorPrimary));

                datePickerDialog.show(getFragmentManager(), "DatePickerDialog");
            }
        });

    }

    private void updateDrawingDateTime() {
        try {
            Date dateTime = alarmCalendar.getTime();

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM", Locale.getDefault());
            String formattedDateTime = dateFormat.format(dateTime);
            TVDate.setText(formattedDateTime);

            dateFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
            formattedDateTime = dateFormat.format(dateTime);
            TVTime.setText(formattedDateTime);
        } catch (Exception ex) {
            Log.e(TAG, "updateDrawingDateTime()-> ", ex);
        }
    }

    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        ETName = (EditText) findViewById(R.id.editText);
        ETDescription = (EditText) findViewById(R.id.editText2);
        LLDateTimeContainer = (LinearLayout) findViewById(R.id.ll_date_time_container);
        TVDate = (TextView) findViewById(R.id.tv_date);
        TVTime = (TextView) findViewById(R.id.tv_time);
    }

    private void setupToolbar() {
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
                .putExtra("date", alarmCalendar.getTime());

        AlarmManagerBroadcastReceiver.setAlarm(this, alarmCalendar.getTimeInMillis());

        setResult(RESULT_OK, data);
        finish();
    }
}