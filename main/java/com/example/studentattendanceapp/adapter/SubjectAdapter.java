package com.example.studentattendanceapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studentattendanceapp.DBHelper;
import com.example.studentattendanceapp.R;
import com.example.studentattendanceapp.SubjectDetailActivity;
import com.example.studentattendanceapp.model.Subject;

import java.util.List;

public class SubjectAdapter extends RecyclerView.Adapter<SubjectAdapter.ViewHolder> {
    private final Context context;
    private final List<Subject> subjects;
    private final DBHelper dbHelper;

    public SubjectAdapter(Context context, List<Subject> subjects, DBHelper dbHelper) {
        this.context = context;
        this.subjects = subjects;
        this.dbHelper = dbHelper;
    }

    @NonNull @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_subject, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Subject s = subjects.get(position);
        holder.name.setText(s.name);

        int present = dbHelper.getPresentCount(s.id);
        int total   = dbHelper.getTotalCount(s.id);
        int pct     = total == 0 ? 0 : (present * 100 / total);
        String info = "Present: " + present + " / " + total + " (" + pct + "%)";
        if (pct < 75 && total > 0) {
            holder.info.setTextColor(
                    ContextCompat.getColor(context, android.R.color.holo_red_dark)
            );
            info += " SA";
        } else {
            holder.info.setTextColor(
                    ContextCompat.getColor(context, R.color.black)
            );
        }
        holder.info.setText(info);

        holder.progressBar.setMax(total);
        holder.progressBar.setProgress(present);

        holder.itemView.setOnClickListener(v -> {
            Intent i = new Intent(context, SubjectDetailActivity.class);
            i.putExtra("subjectId", s.id);
            i.putExtra("subjectName", s.name);
            context.startActivity(i);
        });
    }

    @Override
    public int getItemCount() {
        return subjects.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, info;
        ProgressBar progressBar;
        ViewHolder(@NonNull View v) {
            super(v);
            name = v.findViewById(R.id.textViewSubjectName);
            info = v.findViewById(R.id.textViewAttendanceInfo);
            progressBar = v.findViewById(R.id.progressBar);
        }
    }
}
