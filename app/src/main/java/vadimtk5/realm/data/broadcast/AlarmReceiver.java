package vadimtk5.realm.data.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("rersdf", "onReceive: ");
        Toast.makeText(context, intent.getExtras().getInt("action"), Toast.LENGTH_SHORT).show();
    }
}
