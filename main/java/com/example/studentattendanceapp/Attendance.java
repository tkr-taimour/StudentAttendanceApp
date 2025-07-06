package com.example.studentattendanceapp.model;

public class Attendance {
    public int id;
    public String date;
    public boolean present;

    public Attendance(int id, String date, boolean present) {
        this.id = id;
        this.date = date;
        this.present = present;
    }
}
