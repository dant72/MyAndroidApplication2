package com.example.myapplication;

import android.content.Context;

public class MyButton extends androidx.appcompat.widget.AppCompatButton {
    public int row;
    public int column;
    public MyButton(Context context) {
        super(context);
    }

    public MyButton(Context context, int row, int column) {
        super(context);
        this.row = row;
        this.column = column;
    }
}
