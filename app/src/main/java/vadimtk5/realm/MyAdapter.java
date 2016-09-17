package vadimtk5.realm;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;


import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final String TAG = getClass().getSimpleName();

    private List<Task> dataSet;
    private Activity activity;
    private View.OnClickListener onClickListener;
  private String name;
    private String description;
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public View rootView;
        public TextView title, year, genre;

        public MyViewHolder(View view) {
            super(view);

            rootView = view.findViewById(R.id.root_view);
            title = (TextView) view.findViewById(R.id.title);
            genre = (TextView) view.findViewById(R.id.genre);
            year = (TextView) view.findViewById(R.id.year);


        }
    }

    public class ButtonViewHolder extends RecyclerView.ViewHolder {
        public ButtonViewHolder(View view) {
            super(view);
        }
    }

    public MyAdapter(Activity activity, List<Task> dataSet) {
        this.activity = activity;
        this.dataSet = dataSet;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        return new MyViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rv_task, parent, false));

    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        Task task = dataSet.get(position);

        if (task == null) {
            Log.e(TAG, "onBindViewHolder: task at position " + position + " is null");
            return;
        }

        if (holder instanceof MyViewHolder) {
            if (onClickListener != null) {
              ((MyViewHolder) holder).rootView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(activity, LookTask.class);
                        intent.putExtra("name",name);
                        intent.putExtra("description",description);
                        activity.startActivity(intent);
                    }
                });
            }

            ((MyViewHolder) holder).title.setText(task.getName());
            ((MyViewHolder) holder).genre.setText(task.getDescription());
            ((MyViewHolder) holder).year.setText(task.getFormattedDate());
        } else if (holder instanceof ButtonViewHolder) {

        }
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public List<Task> getDataSet() {
        return dataSet;
    }

    public void addTask(Task task) {
        getDataSet().add(task);
        notifyItemInserted(getDataSet().indexOf(task));
    }

    public void removeTask() {

    }


    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;


    }
}