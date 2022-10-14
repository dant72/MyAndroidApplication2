package com.example.myapplication.backend.models;

import android.text.Editable;

import java.util.Date;

public class ToDo {
    public int id;
    public Editable time;
    public Editable name;
    public Editable description;

    public ToDo(Editable time, Editable name, Editable description)
    {
        this.time = time;
        this.name = name;
        this.description = description;
        id = currentId++;
    }

    private static int currentId = 0;
}
