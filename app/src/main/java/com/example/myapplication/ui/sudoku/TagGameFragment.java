package com.example.myapplication.ui.sudoku;


import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.Toast;

import com.example.myapplication.MyButton;
import com.example.myapplication.R;

public class TagGameFragment extends Fragment {

    private TagGameViewModel mViewModel;
    private final int rows = 4, columns = 4;
    private Button grid[][] = new Button[rows][columns];
    public static TagGameFragment newInstance() {
        return new TagGameFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.fragment_tag_game, container, false);

       // List<ToDo> toDoList = ToDo.listAll(ToDo.class);
        for (int i = 0; i < rows; i++)
            for (int j = 0; j < columns; j++)
                createElement(view, i, j);

       return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(TagGameViewModel.class);
        // TODO: Use the ViewModel
    }

    public void createElement(View view, int i, int j)
    {
        if (i == 0 && j == 0)
            return;
        Button myButton = new MyButton(getContext(), i, j);
        grid[i][j] = myButton;

        myButton.setOnClickListener(new View.OnClickListener() {

        @Override
        public void onClick(View view) {

            MyButton button = (MyButton) view;

            if (button.row  > 0 && grid[button.row - 1][button.column] == null) {
                moveTo(button, button.row - 1, button.column);
            }
            else if (button.row < rows - 1 && grid[button.row + 1][button.column] == null)
            {
                moveTo(button, button.row + 1, button.column);
            }
            else if (button.column < columns - 1 && grid[button.row][button.column + 1] == null)
            {
                moveTo(button, button.row, button.column + 1);
            }
            else if (button.column > 0 && grid[button.row][button.column - 1] == null)
            {
                moveTo(button, button.row, button.column - 1);
            }

            Toast.makeText(getContext(), view.getId() + "",Toast.LENGTH_SHORT).show();
        }
    });


        myButton.setText((i * 9 + j) + " ");
        myButton.setId(i * 9 + j);

        GridLayout ll = view.findViewById(R.id.table);

        GridLayout.LayoutParams lp = new GridLayout.LayoutParams(
                                      GridLayout.spec(i, GridLayout.CENTER),
                                      GridLayout.spec(j, GridLayout.CENTER));
        ll.addView(myButton, lp);

    }

    public void onToDoItemClick(View view) {
        //ToDo item = ToDo.findById(ToDo.class, view.getId());
        Toast.makeText(getContext(), view.getId() + "",Toast.LENGTH_LONG).show();
    }

    private void moveTo(MyButton button, int toRow, int toColumn)
    {
        GridLayout.LayoutParams lp = new GridLayout.LayoutParams(
                GridLayout.spec(toRow, GridLayout.CENTER),
                GridLayout.spec(toColumn, GridLayout.CENTER));

        grid[toRow][toColumn] = button;
        grid[button.row][button.column] = null;

        button.setLayoutParams(lp);
    }

}