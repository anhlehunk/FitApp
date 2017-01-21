package com.example.anh.fitapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;



public class StepCountActivity extends ActionBarActivity implements SensorEventListener{
    ProgressBar prg;
    Button button;
    private SensorManager sensorManager;
    private TextView count;
    boolean activityRunning;
    private int stepsInSensor = 0;
    private int stepsAtReset;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    public static final String myPreference = "mypreference";


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        SharedPreferences prefs = getSharedPreferences(myPreference, Context.MODE_PRIVATE);
        stepsAtReset = prefs.getInt("stepsAtReset", 0);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_count);

        prg =(ProgressBar) findViewById(R.id.progressBar) ;
        count = (TextView) findViewById(R.id.count);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);


    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.home:
                startActivity(new Intent(this, MainActivity.class));
                return true;
            case R.id.exercise:
                startActivity(new Intent(this, ExerciseActivity.class));
                return true;
            case R.id.stat:
                startActivity(new Intent(this, StatActivity.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void reset(View v) {
        stepsAtReset = stepsInSensor;

        SharedPreferences.Editor editor =
        getSharedPreferences(myPreference, MODE_PRIVATE).edit();
        editor.putInt("stepsAtReset", stepsAtReset);
        editor.commit();

        // you can now display 0:
        count.setText(String.valueOf(0));

        prg.setProgress(0);
    }

    @Override
    protected void onResume() {
        super.onResume();
        activityRunning = true;
        Sensor countSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
            if (countSensor != null) {
                sensorManager.registerListener(this, countSensor, SensorManager.SENSOR_DELAY_UI);
            }

            else{
                Toast.makeText(this, "Sensor not available", Toast.LENGTH_LONG).show();
            }
    }

    @Override
    protected void onPause() {
        super.onPause();
        activityRunning = false;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        prg =(ProgressBar) findViewById(R.id.progressBar) ;
        if(activityRunning){
            stepsInSensor = Integer.valueOf((int) event.values[0]);
            int stepsSinceReset = stepsInSensor - stepsAtReset;
            count.setText(String.valueOf(stepsSinceReset));
            prg.setProgress(stepsSinceReset);
        }else{
            event.values[0] = 0;
        }}

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    public void home(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        this.finish();
    }

}


