package com.example.studentattendanceapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studentattendanceapp.R;
import com.example.studentattendanceapp.model.Attendance;

import java.util.List;

public class AttendanceAdapter extends RecyclerView.Adapter<AttendanceAdapter.ViewHolder> {
    private final List<Attendance> list;

    public AttendanceAdapter(List<Attendance> list) {
        this.list = list;
    }

    @NonNull @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_attendance, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder h, int pos) {
        Attendance a = list.get(pos);
        h.date.setText(a.date);
        h.status.setText(a.present ? "Present" : "Absent");
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView date, status;
        ViewHolder(@NonNull View v) {
            super(v);
            date   = v.findViewById(R.id.textViewDate);
            status = v.findViewById(R.id.textViewStatus);
        }
    }
}
