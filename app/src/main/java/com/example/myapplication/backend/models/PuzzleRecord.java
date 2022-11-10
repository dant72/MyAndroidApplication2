package com.example.myapplication.backend.models;

import com.orm.SugarRecord;

import java.sql.Date;
import java.sql.Time;

public class PuzzleRecord extends SugarRecord {

    String time;
    int count;
    String date;

    public PuzzleRecord()
    {
    }

    public  PuzzleRecord(String time, int count, String date)
    {
        this.time = time;
        this.count = count;
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public int getCount() {
        return count;
    }

    public String getTime() {
        return time;
    }
}
