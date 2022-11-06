package com.example.myapplication.ui.sudoku;


import androidx.lifecycle.ViewModelProvider;

import android.animation.Animator;
import android.app.AlertDialog;
import android.graphics.Color;
import android.icu.util.Calendar;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.Toast;

import com.example.myapplication.MyButton;
import com.example.myapplication.R;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class TagGameFragment extends Fragment {

    private TagGameViewModel mViewModel;
    private final int rows = 3, columns = 3;
    private Button grid[][] = new Button[rows][columns];
    public static TagGameFragment newInstance() {
        return new TagGameFragment();
    }
    private Button EmptyButton = null;
    private Button lastMove = null;
    private final Lock lock = new ReentrantLock();
    private GridLayout gridLayout = null;
    private boolean isPlayerPush = false;
    private Date beginTime = new Date();
    private Date endTime = new Date();
    int countSteps = 0;
    private boolean isUser = true;

    CountDownTimer waitTimer;

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
                    isUser = false;



                    waitTimer = new CountDownTimer(200, 100) {

                        public void onTick(long millisUntilFinished) {
                            MyButton b = (MyButton) getNextRandomButton();
                            b.callOnClick();
                        }

                        public void onFinish() {
                            //After 60000 milliseconds (60 sec) finish current
                            //if you would like to execute something when time finishes
                            beginTime = Calendar.getInstance().getTime();
                            countSteps = 0;
                            isUser = true;
                            b.setVisibility(View.GONE);

                        }
                    }.start();

                    b.setVisibility(View.GONE);
                    //timer = new Timer();
                    //timer.schedule(timerTask, 100, 100);
                    //b.setText("stop");
                    b.setVisibility(View.GONE);
                }
                else
                {
                    //timer.cancel();
                    b.setText("start");
                    b.setVisibility(View.GONE);
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
                        GridLayout.spec(j, GridLayout.CENTER)
                );
                lp.setGravity(Gravity.FILL);
                lp.setMargins(1, 1, 1, 1);
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

    /*private synchronized void swapButtons(Button button1, Button button2)
    {
        int buttonIndex = findButton(button1);
        int EmptyButtonIndex = findButton(button2);

        int iButton1 = buttonIndex / rows;
        int jButton1 = buttonIndex % columns;

        int iButton2 = EmptyButtonIndex / rows;
        int jButton2 = EmptyButtonIndex % columns;

        grid[iButton1][jButton1] = button2;
        grid[iButton2][jButton2] = button1;
        lastMove = button1;
        DrawView();
    }*/


    public void createElement(View view, int i, int j)
    {
        MyButton myButton = new MyButton(getContext(), i, j);
        myButton.setBackgroundColor(R.style.MyButtonStyle);
        myButton.setTextAppearance(R.style.MyButtonStyle);

        grid[i][j] = myButton;
        if (i == 0 && j == 0) {
            myButton.getBackground().setAlpha(0);
            myButton.setTextColor(Color.TRANSPARENT);
            EmptyButton = myButton;
        }

        myButton.setOnClickListener(new View.OnClickListener() {

        @Override
        public synchronized void onClick(View view) {
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

            if (isUser)
            {
                countSteps++;
            }

            if (isUser && checkGameOver())
            {
                endTime = Calendar.getInstance().getTime();

                long delta = endTime.getTime() - beginTime.getTime();

                Time time = new Time(delta);

                new AlertDialog.Builder(getContext())
                        .setTitle("Game Over")
                        .setMessage("You win! \nTime spent: " + time + "\nCount: " + countSteps)
                        .show();

                Button b = (Button)getView().findViewById(R.id.startGameButton);
                b.setVisibility(View.VISIBLE);
            }
        }}
    });

        myButton.setText((i * rows + j) + " ");
        myButton.setId(i * rows + j);

    }

    private boolean checkGameOver()
    {
        for (int i = 0; i < rows; i++)
            for (int j = 0; j < columns; j++)
                if (grid[i][j].getId() != i * rows + j)
                    return false;

        return true;
    }

    private synchronized Button getNextRandomButton()
    {
        ArrayList<Button> possibleMove = new ArrayList();
        int EmptyButtonIndex = findButton(EmptyButton);

        int iEmptyButton = EmptyButtonIndex / rows;
        int jEmptyButton = EmptyButtonIndex % columns;

        if (iEmptyButton > 0)
            possibleMove.add(grid[iEmptyButton - 1][jEmptyButton]);
        if (iEmptyButton < rows - 1)
            possibleMove.add(grid[iEmptyButton + 1][jEmptyButton]);
        if (jEmptyButton > 0)
            possibleMove.add(grid[iEmptyButton][jEmptyButton - 1]);
        if (jEmptyButton < columns - 1)
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