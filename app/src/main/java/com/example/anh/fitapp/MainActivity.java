package com.example.anh.fitapp;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
    }



    public void step(View view) {
        Intent intent = new Intent(this, StepCountActivity.class);
        startActivity(intent);
        this.finish();
    }

    public void run(View view) {
        Intent intent = new Intent(this, RunningActivity.class);
        startActivity(intent);
        this.finish();
    }

    public void exercise (View view) {
        Intent intent = new Intent(this, ExerciseActivity.class);
        startActivity(intent);
        this.finish();
    }

    public void stat (View view) {
        Intent intent = new Intent(this, StatActivity.class);
        startActivity(intent);
        this.finish();
    }
}
