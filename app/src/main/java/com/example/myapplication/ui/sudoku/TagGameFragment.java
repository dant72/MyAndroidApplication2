package com.example.myapplication.ui.sudoku;


import androidx.lifecycle.ViewModelProvider;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.Toast;

import com.example.myapplication.MyButton;
import com.example.myapplication.R;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class TagGameFragment extends Fragment {

    private TagGameViewModel mViewModel;
    private final int rows = 4, columns = 4;
    private Button grid[][] = new Button[rows][columns];
    public static TagGameFragment newInstance() {
        return new TagGameFragment();
    }
    private MyButton EmptyButton = null;
    private MyButton lastMove = null;
    private final Lock lock = new ReentrantLock();
    private  GridLayout gridLayout = null;

    Timer timer = new Timer();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.fragment_tag_game, container, false);

        gridLayout = view.findViewById(R.id.table);
       // List<ToDo> toDoList = ToDo.listAll(ToDo.class);
        for (int i = 0; i < rows; i++)
            for (int j = 0; j < columns; j++)
                createElement(view, i, j);

        DrawView();

        Button b = (Button)view.findViewById(R.id.startGameButton);
        b.setText("start");

        Handler h = new Handler();

        TimerTask timerTask  = new TimerTask() {
            @Override
            public void run() {

                h.post(new Runnable() {

                    public void run() {
                        MyButton b = (MyButton) getNextRandomButton();

                        b.callOnClick();

                    }
                });
            }
        };
        b.setOnClickListener(new View.OnClickListener() {



            @Override
            public void onClick(View v) {
                Button b = (Button) v;
                if (b.getText() == "start") {
                    timer.schedule(timerTask, 100, 500);
                    b.setText("stop");
                }
                else
                {
                    timer.purge();
                    b.setText("start");
                }
            }
        });



       return view;
    }

    private void DrawView()
    {
        gridLayout.removeAllViews();
        for (int i = 0; i < rows; i++)
            for (int j = 0; j < columns; j++) {
                GridLayout.LayoutParams lp = new GridLayout.LayoutParams(
                        GridLayout.spec(i, GridLayout.CENTER),
                        GridLayout.spec(j, GridLayout.CENTER));
                gridLayout.addView(grid[i][j], lp);
            }
    }


    @Override
    public void onDestroyView() {

        super.onDestroyView();

        //timer.cancel();
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(TagGameViewModel.class);
        // TODO: Use the ViewModel
    }

    private int findButton(Button button)
    {
        for (int i = 0;  i < rows; i++)
            for (int j = 0; j < columns; j++)
                if (grid[i][j] == button)
                    return i * rows + j;
                return -1;
    }

    public void createElement(View view, int i, int j)
    {
        MyButton myButton = new MyButton(getContext(), i, j);

        grid[i][j] = myButton;
        if (i == 0 && j == 0) {
            myButton.getBackground().setAlpha(0);
            myButton.setTextColor(Color.TRANSPARENT);
            EmptyButton = myButton;
        }

        myButton.setOnClickListener(new View.OnClickListener() {

        @Override
        public void onClick(View view) {
            MyButton button = (MyButton) view;
            int buttonIndex = findButton(button);
            int EmptyButtonIndex = findButton(EmptyButton);

            int iButton = buttonIndex / rows;
            int jButton = buttonIndex % columns;

            int iEmptyButton = EmptyButtonIndex / rows;
            int jEmptyButton = EmptyButtonIndex % columns;



            if (Math.abs(iButton - iEmptyButton) == 1 && jButton == jEmptyButton
                    || Math.abs(jButton - jEmptyButton) == 1 && iButton == iEmptyButton)
            {
                grid[iButton][jButton] = EmptyButton;
                grid[iEmptyButton][jEmptyButton] = button;
                lastMove = button;

                DrawView();
            }
        }
    });

        myButton.setText((i * 9 + j) + " ");
        myButton.setId(i * 9 + j);

    }

    private synchronized Button getNextRandomButton()
    {
        ArrayList<Button> possibleMove = new ArrayList();
        int EmptyButtonIndex = findButton(EmptyButton);

        int iEmptyButton = EmptyButtonIndex / rows;
        int jEmptyButton = EmptyButtonIndex % columns;

        if (iEmptyButton > 0)
            possibleMove.add(grid[iEmptyButton - 1][jEmptyButton]);
        if (iEmptyButton < rows - 2)
            possibleMove.add(grid[iEmptyButton + 1][jEmptyButton]);
        if (jEmptyButton > 0)
            possibleMove.add(grid[iEmptyButton][jEmptyButton - 1]);
        if (jEmptyButton < columns - 2)
            possibleMove.add(grid[iEmptyButton][jEmptyButton + 1]);

        possibleMove.remove(lastMove);
        int index = rnd(possibleMove.size() - 1);

        return possibleMove.get(index);
    }

    public static int rnd(int max)
    {
        return (int) (Math.random() * ++max);
    }

}