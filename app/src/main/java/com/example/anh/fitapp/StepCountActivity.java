package com.example.anh.fitapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.icu.text.DecimalFormat;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.Objects;

import static android.view.View.GONE;
import static com.example.anh.fitapp.R.id.editStep;


public class StepCountActivity extends ActionBarActivity implements SensorEventListener{
    ProgressBar prg;
    Button button;
    private SensorManager sensorManager;
    private TextView count;
    private TextView countStep;
    boolean activityRunning;
    private int stepsInSensor = 0;
    private int stepsAtReset;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    public static final String myPreference = "mypreference";
    String enteredSteps;
    int enteredStepsInt;
    EditText editTextStep;




    @Override
    protected void onCreate(Bundle savedInstanceState) {

        SharedPreferences prefs = getSharedPreferences(myPreference, Context.MODE_PRIVATE);
        editor = prefs.edit();
        stepsAtReset = prefs.getInt("stepsAtReset", 0);


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_count);
        editTextStep = (EditText) findViewById(R.id.editStep);
        button = (Button) findViewById(R.id.enterSteps);
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {   enteredSteps = editTextStep.getText().toString();
                if (Objects.equals(enteredSteps, "")){
                    editTextStep.setVisibility(View.VISIBLE);
                    Toast.makeText(StepCountActivity.this, "Please enter your goal!", Toast.LENGTH_SHORT)
                            .show();}

                else{
                if (editTextStep.getVisibility() == View.VISIBLE){
                    enteredStepsInt = Integer.parseInt(enteredSteps);
                    editTextStep.setText("");
                    editTextStep.setVisibility(View.GONE);
                }
                else {
                    editTextStep.setVisibility(View.VISIBLE);
                }}





                }});


        prg =(ProgressBar) findViewById(R.id.progressBar) ;
        count = (TextView) findViewById(R.id.count);
        countStep = (TextView) findViewById(R.id.countStep);
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
    public void onSensorChanged(SensorEvent event)   {
        prg =(ProgressBar) findViewById(R.id.progressBar) ;
        button = (Button) findViewById(R.id.enterSteps);

        if(activityRunning){
            stepsInSensor = Integer.valueOf((int) event.values[0]);
            int stepsSinceReset = stepsInSensor - stepsAtReset;

            double num = stepsSinceReset;
            double sum = enteredStepsInt;

            String str = String.format("%.1f", (num/sum) * 100.0);
            countStep.setText(String.valueOf(stepsSinceReset));
            float percentage = ((float) stepsSinceReset / enteredStepsInt) * 100;
            count.setText(str + "%");
            Log.d("check", String.valueOf(percentage));
            prg.setProgress((int) percentage);
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


