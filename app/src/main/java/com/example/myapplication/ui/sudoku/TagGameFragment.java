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
import android.view.ViewPropertyAnimator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.Toast;

import com.example.myapplication.MyButton;
import com.example.myapplication.R;

import java.util.ArrayList;
import java.util.List;
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
    private MyButton LastMove = null;
    private final Lock lock = new ReentrantLock();

    Timer timer = new Timer();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.fragment_tag_game, container, false);

       // List<ToDo> toDoList = ToDo.listAll(ToDo.class);
        for (int i = 0; i < rows; i++)
            for (int j = 0; j < columns; j++)
                createElement(view, i, j);

        Button b = (Button)view.findViewById(R.id.startGameButton);
        b.setText("start");
        b.setOnClickListener(new View.OnClickListener() {

            Handler h = new Handler();

            @Override
            public void onClick(View v) {
                Button b = (Button) v;
                if (b.getText() == "start") {
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            //use a handler to run a toast that shows the current timestamp
                            lock.lock();
                            h.post(new Runnable() {

                                public void run() {
                                    MyButton b = (MyButton) getNextRandomButton();

                                    lock.lock();
                                    if (b != null) {
                                        b.callOnClick();
                                        Log.d("Move", b.row + " : " + b.column + " , Empty: " + EmptyButton.row + " : " + EmptyButton.column);
                                    }
                                    else {
                                        Log.d("Move","Button is null");
                                    }
                                    lock.unlock();
                                }

                            });
                            lock.unlock();
                        }
                    }, 100, 500);
                    b.setText("stop");
                }
                else
                {
                    timer.cancel();
                    b.setText("start");
                }
            }
        });



       return view;
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

    public void createElement(View view, int i, int j)
    {
        MyButton myButton = new MyButton(getContext(), i, j);

        grid[i][j] = myButton;
        if (i == 0 && j == 0) {
            myButton.getBackground().setAlpha(0);
            myButton.setTextColor(Color.TRANSPARENT);
            EmptyButton = myButton;
            grid[i][j] = null;
        }

        myButton.setOnClickListener(new View.OnClickListener() {

        @Override
        public void onClick(View view) {
            //Toast.makeText(getContext(), "Empty " + EmptyButton.getX() + ":" + EmptyButton.getY(),Toast.LENGTH_SHORT).show();
            MyButton button = (MyButton) view;

            if (Math.abs(button.row - EmptyButton.row) == 1 && button.column == EmptyButton.column
                    || Math.abs(button.column - EmptyButton.column) == 1 && button.row == EmptyButton.row)
            {

                int tmpRow = button.row;
                button.row = EmptyButton.row;
                EmptyButton.row = tmpRow;

                int tmpColumn = button.column;
                button.column = EmptyButton.column;
                EmptyButton.column = tmpColumn;

                ViewGroup.LayoutParams tmp = button.getLayoutParams();
                button.setLayoutParams(EmptyButton.getLayoutParams());
                EmptyButton.setLayoutParams(tmp);


                LastMove = button;

                //button.animate().x(EmptyButton.getX()).y(EmptyButton.getY()).setDuration(200);

            }

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

    private synchronized Button getNextRandomButton()
    {

        ArrayList<Button> possibleMove = new ArrayList<Button>();
        if (EmptyButton.row > 0)
            possibleMove.add(grid[EmptyButton.row - 1][EmptyButton.column]);
        if (EmptyButton.row < rows - 2)
            possibleMove.add(grid[EmptyButton.row + 1][EmptyButton.column]);
        if (EmptyButton.column > 0)
            possibleMove.add(grid[EmptyButton.row][EmptyButton.column - 1]);
        if (EmptyButton.column < columns - 2)
            possibleMove.add(grid[EmptyButton.row][EmptyButton.column + 1]);

        //possibleMove.remove(LastMove);

        int index = rnd(possibleMove.size() - 1);

        return possibleMove.get(index);
    }

    public static int rnd(int max)
    {
        return (int) (Math.random() * ++max);
    }

}