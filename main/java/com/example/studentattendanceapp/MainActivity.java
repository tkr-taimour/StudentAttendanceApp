

package com.example.studentattendanceapp;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studentattendanceapp.adapter.SubjectAdapter;
import com.example.studentattendanceapp.model.Subject;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private DBHelper dbHelper;
    private RecyclerView recyclerView;
    private SubjectAdapter adapter;
    private List<Subject> subjectList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        dbHelper = new DBHelper(this);


        recyclerView = findViewById(R.id.recyclerViewSubjects);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        subjectList = new ArrayList<>();


        adapter = new SubjectAdapter(this, subjectList, dbHelper);
        recyclerView.setAdapter(adapter);


        FloatingActionButton fab = findViewById(R.id.fabAddSubject);
        fab.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddSubjectActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        subjectList.clear();
        Cursor cursor = dbHelper.getSubjects();
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
            String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
            subjectList.add(new Subject(id, name));
        }
        cursor.close();
        adapter.notifyDataSetChanged();
    }
}
