package com.example.myapplication.backend.models;

import android.text.Editable;

import com.orm.SugarRecord;

public class ToDo extends SugarRecord {


    String time;
    String name;
    String description;

    public ToDo()
    {
    }

    public ToDo(String time, String name, String description)
    {
        this.time = time;
        this.name = name;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }

    public String getTime() {
        return time;
    }
}
