package vadimtk5.realm.ui.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import android.view.ViewGroup;
import android.widget.TextView;


import java.util.List;

import vadimtk5.realm.R;
import vadimtk5.realm.ui.activity.TaskInfoActivity;
import vadimtk5.realm.data.model.Task;

public class TaskListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final String TAG = getClass().getSimpleName();

    private List<Task> dataSet;
    private Activity activity;
    private OnClickListener onClickListener;

    private class MyViewHolder extends RecyclerView.ViewHolder {
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

    public TaskListAdapter(Activity activity, List<Task> dataSet) {
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
            ((MyViewHolder) holder).rootView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onClickListener != null) {
                        onClickListener.onClick(holder.getAdapterPosition());
                    }
                }
            });
            ((MyViewHolder) holder).rootView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (onClickListener != null) {
                        onClickListener.onLongClick(holder.getAdapterPosition());
                        return true;
                    }
                    return false;
                }
            });

            ((MyViewHolder) holder).title.setText(task.getName());
            ((MyViewHolder) holder).genre.setText(task.getDescription());
            ((MyViewHolder) holder).year.setText(task.getFormattedDate());
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

    public void removeTask(Task task) {
        getDataSet().remove(task);
        notifyItemRemoved(getDataSet().indexOf(task));
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public interface OnClickListener {
        void onClick(int position);

        void onLongClick(int position);
    }
}