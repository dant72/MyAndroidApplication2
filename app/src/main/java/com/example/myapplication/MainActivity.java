package com.example.myapplication;

import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.view.Menu;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.myapplication.backend.models.ToDo;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    private ArrayList<ToDo> toDoList = new ArrayList<ToDo>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);
        binding.appBarMain.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public void onToDoItemClick(View view) {

        ToDo toDo = null;
        Optional<ToDo> item = toDoList.stream().filter(i -> i.getId() == view.getId()).findFirst();

        if (item.isPresent())
            Toast.makeText(this, item.get().getDescription() + "", Toast.LENGTH_LONG).show();
        else
            Toast.makeText(this, view.getId() + "", Toast.LENGTH_LONG).show();
    }


    void createElement(String time, String toDoName, String description)
    {
        ToDo item = new ToDo(time, toDoName, description);
        item.save();

        Button myButton = new Button(this);
        myButton.setId(item.getId().intValue());
        myButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                onToDoItemClick(view);
            }
        });


        myButton.setText(time + " - " + toDoName);

        LinearLayout ll = findViewById(R.id.toDoList);

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        ll.addView(myButton, lp);

        Toast.makeText(this, toDoName + " creating!", Toast.LENGTH_SHORT).show();
    }

    public void onAddClick(View view) {

        EditText time = findViewById(R.id.editTextTime);
        EditText toDoName = findViewById(R.id.editTextToDoName);
        EditText description = findViewById(R.id.editTextDescription);

        createElement(time.getText().toString(), toDoName.getText().toString(), description.getText().toString());
    }
}