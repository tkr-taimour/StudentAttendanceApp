package com.example.studentattendanceapp;

import android.app.DatePickerDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studentattendanceapp.adapter.AttendanceAdapter;
import com.example.studentattendanceapp.model.Attendance;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class SubjectDetailActivity extends AppCompatActivity {
    private DBHelper dbHelper;
    private int subjectId;
    private List<Attendance> attendanceList;
    private AttendanceAdapter attendanceAdapter;
    private TextView textViewStats;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject_detail);


        MaterialToolbar toolbar = findViewById(R.id.toolbarDetail);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> finish());


        subjectId = getIntent().getIntExtra("subjectId", -1);
        String subjectName = getIntent().getStringExtra("subjectName");
        getSupportActionBar().setTitle(subjectName);


        textViewStats = findViewById(R.id.textViewStats);


        RecyclerView rv = findViewById(R.id.recyclerViewAttendance);
        rv.setLayoutManager(new LinearLayoutManager(this));
        attendanceList = new ArrayList<>();
        attendanceAdapter = new AttendanceAdapter(attendanceList);
        rv.setAdapter(attendanceAdapter);


        dbHelper = new DBHelper(this);


        FloatingActionButton fab = findViewById(R.id.fabAddAttendance);
        fab.setOnClickListener(v -> showDatePickerDialog());


        loadAttendance();
    }

    private void loadAttendance() {
        attendanceList.clear();
        Cursor c = dbHelper.getAttendance(subjectId);
        while (c.moveToNext()) {
            int id       = c.getInt(c.getColumnIndexOrThrow("id"));
            String date  = c.getString(c.getColumnIndexOrThrow("date"));
            boolean pres = c.getInt(c.getColumnIndexOrThrow("present")) == 1;
            attendanceList.add(new Attendance(id, date, pres));
        }
        c.close();
        attendanceAdapter.notifyDataSetChanged();
        updateStats();
    }

    private void updateStats() {
        int total = attendanceList.size(), present = 0;
        for (Attendance a : attendanceList) if (a.present) present++;
        int pct = (total == 0 ? 0 : (present * 100 / total));
        String status = pct < 75 ? "SA (Short-Attendance)" : "Good";
        textViewStats.setText(String.format(
                Locale.getDefault(),
                "Total: %d\nPresent: %d\n%%: %d%%\nStatus: %s",
                total, present, pct, status
        ));
    }

    private void showDatePickerDialog() {
        Calendar cal = Calendar.getInstance();
        new DatePickerDialog(
                this,
                (view, y, m, d) -> {
                    String date = String.format(Locale.getDefault(), "%04d-%02d-%02d", y, m + 1, d);
                    new AlertDialog.Builder(this)
                            .setTitle("Mark Attendance")
                            .setMessage(date)
                            .setPositiveButton("Present", (dlg, w) -> {
                                dbHelper.addAttendance(subjectId, date, true);
                                loadAttendance();
                            })
                            .setNegativeButton("Absent", (dlg, w) -> {
                                dbHelper.addAttendance(subjectId, date, false);
                                loadAttendance();
                            })
                            .show();
                },
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
        ).show();
    }
}
