package com.example.myapplication.ui;

import androidx.lifecycle.ViewModelProvider;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.backend.models.PuzzleRecord;
import com.example.myapplication.backend.models.ToDo;

import java.util.ArrayList;
import java.util.List;

public class PuzzleRecordsFragment extends Fragment {

    private PuzzleRecordsViewModel mViewModel;
    private ArrayList<PuzzleRecord> records = new ArrayList<PuzzleRecord>();

    public static PuzzleRecordsFragment newInstance() {
        return new PuzzleRecordsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_puzzle_records, container, false);

        TableLayout table = (TableLayout)view.findViewById(R.id.records);

        List<PuzzleRecord> recordsList = ToDo.listAll(PuzzleRecord.class);
        if (!recordsList.isEmpty()) {

            for (PuzzleRecord item : recordsList){
                final TableRow tr_head = new TableRow(getContext());
                tr_head.setLayoutParams(new TableRow.LayoutParams(
                        TableRow.LayoutParams.FILL_PARENT,
                        TableRow.LayoutParams.WRAP_CONTENT));

                TextView label_date = new TextView(getContext());
                label_date.setGravity(View.TEXT_ALIGNMENT_CENTER);
                label_date.setText(item.getTime() + "");
                tr_head.addView(label_date);// add the column to the table row here

                TextView label_weight_kg = new TextView(getContext());
                label_weight_kg.setText("1"); // set the text for the header
                tr_head.addView(label_weight_kg); // add the column to the table row here
                label_weight_kg.setGravity(View.TEXT_ALIGNMENT_CENTER);

                TextView label_weight_kg2 = new TextView(getContext());
                label_weight_kg2.setText(item.getDate()); // set the text for the header
                tr_head.addView(label_weight_kg2);
                label_weight_kg2.setGravity(View.TEXT_ALIGNMENT_CENTER);// add the column to the table row here

                table.addView(tr_head, new TableLayout.LayoutParams(
                        TableLayout.LayoutParams.FILL_PARENT,
                        TableLayout.LayoutParams.WRAP_CONTENT));
            }
        }
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(PuzzleRecordsViewModel.class);
        // TODO: Use the ViewModel
    }

}