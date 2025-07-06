package com.example.studentattendanceapp;

import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

public class AddSubjectActivity extends AppCompatActivity {
    EditText et;
    Button btn;
    DBHelper db;

    @Override
    protected void onCreate(Bundle s) {
        super.onCreate(s);
        setContentView(R.layout.activity_add_subject);

        et = findViewById(R.id.editTextSubjectName);
        btn = findViewById(R.id.buttonSaveSubject);
        db = new DBHelper(this);

        btn.setOnClickListener(v -> {
            String name = et.getText().toString().trim();
            if (!name.isEmpty() && db.addSubject(name)) {
                finish();
            } else {
                Toast.makeText(this, "Enter valid name", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
