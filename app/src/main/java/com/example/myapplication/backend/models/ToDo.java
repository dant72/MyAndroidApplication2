package com.example.myapplication.backend.models;

import java.util.Date;

public class ToDo {
    public Date date;
    public String name;
    public String description;

    public ToDo(Date date, String name, String description)
    {
        this.date = date;
        this.name = name;
        this.description = description;
    }
}
