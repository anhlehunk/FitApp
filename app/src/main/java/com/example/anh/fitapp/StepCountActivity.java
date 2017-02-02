package com.example.anh.fitapp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.icu.text.DecimalFormat;
import android.support.design.widget.Snackbar;
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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.util.Calendar;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import static android.view.View.GONE;
import static com.example.anh.fitapp.R.id.editStep;


public class StepCountActivity extends ActionBarActivity implements SensorEventListener{
    ProgressBar prg;
    Button button;
    private SensorManager sensorManager;
    private TextView count;
    private TextView countStep;
    private TextView totalText;
    boolean activityRunning;
    private int stepsInSensor = 0;
    private int stepsAtReset;
    private int goalSave;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    public static final String myPreference = "mypreference";
    String enteredSteps;
    int enteredStepsInt;
    EditText editTextStep;
    TextView setGoal;
    int stringNum;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    int stepsSinceReset;
    int stepsTotalReset;
    int stepsTotal;
    String saveData;
    SharedPreferences prefs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        prefs = getSharedPreferences(myPreference, Context.MODE_PRIVATE);
        editor = prefs.edit();
        stepsAtReset = prefs.getInt("stepsAtReset", 0);
        stepsTotalReset = prefs.getInt("stepsTotalReset", 0);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_count);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child(mAuth.getCurrentUser().getUid());


        totalText = (TextView) findViewById(R.id.total);
        goalSave = prefs.getInt("stepsEntered", enteredStepsInt);
        editTextStep = (EditText) findViewById(R.id.editStep);
        setGoal = (TextView) findViewById(R.id.setGoal);
        setGoal.setText(String.valueOf(goalSave));
        stringNum = Integer.parseInt(String.valueOf(setGoal.getText()));
        Log.d("kijken2", String.valueOf(stepsTotal));
        totalText.setText(String.valueOf(stepsTotal));
        button = (Button) findViewById(R.id.enterSteps);
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {   enteredSteps = editTextStep.getText().toString();
                if (Objects.equals(enteredSteps, "")){
                    editTextStep.setVisibility(View.VISIBLE);
                    Toast.makeText(StepCountActivity.this, "Please enter your goal!", Toast.LENGTH_SHORT)
                            .show();
                } else{

                if (editTextStep.getVisibility() == View.VISIBLE){
                    enteredStepsInt = Integer.parseInt(enteredSteps);
                    editTextStep.setText("");
                    count.setVisibility(View.VISIBLE);
                    editor.remove("enteredSteps");
                    editor.putInt("stepsEntered", enteredStepsInt);
                    editor.commit();
                    setGoal.setText(enteredSteps);
                    setGoal.setVisibility(View.VISIBLE);
                    editTextStep.setVisibility(View.GONE);


                } else {
                    editTextStep.setVisibility(View.GONE);
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
                StepCountActivity.this.finish();

                return true;
            case R.id.exercise:
                startActivity(new Intent(this, ExerciseActivity.class));
                StepCountActivity.this.finish();

                return true;
            case R.id.stat:
                startActivity(new Intent(this, StatActivity.class));
                StepCountActivity.this.finish();
                return true;

            case R.id.run:
                startActivity(new Intent(this, SaveActivity.class));
                StepCountActivity.this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void home(View view) {
        Toast.makeText(this, "Sensor not available", Toast.LENGTH_LONG).show();
        stepsTotalReset = stepsInSensor;
        SharedPreferences.Editor editor =
                getSharedPreferences(myPreference, MODE_PRIVATE).edit();
        editor.putInt("stepsTotalReset", stepsTotalReset);
        editor.commit();

        totalText.setText(String.valueOf(0));
    }

    public void reset(View v) {
        Snackbar.make(findViewById(R.id.reset),
                "Are you sure you want to reset?",
                Snackbar.LENGTH_LONG)
                .setAction("Yes!", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        stepsAtReset = stepsInSensor;
                        SharedPreferences.Editor editor =
                                getSharedPreferences(myPreference, MODE_PRIVATE).edit();
                        editor.putInt("stepsAtReset", stepsAtReset);
                        editor.commit();

                        // you can now display 0:
                        countStep.setText(String.valueOf(0));
                        count.setText(String.valueOf(0));
                        prg.setProgress(0);

                    }
                })
                .setActionTextColor(Color.WHITE)
                .show();



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
        saveData = totalText.getText().toString();
        mDatabase.child("Steps").setValue(saveData);
    }


    @Override
    public void onSensorChanged(SensorEvent event)   {


        prg =(ProgressBar) findViewById(R.id.progressBar) ;
        button = (Button) findViewById(R.id.enterSteps);

        if(activityRunning){
            stepsInSensor = Integer.valueOf((int) event.values[0]);
            stepsSinceReset = stepsInSensor - stepsAtReset;
            stepsTotal = stepsInSensor - stepsTotalReset;
            saveData = totalText.getText().toString();



            double num = stepsSinceReset;
            double sum = stringNum;

            String str = String.format("%.1f", (num/sum) * 100.0);
            countStep.setText(String.valueOf(stepsSinceReset));
            totalText.setText(String.valueOf(stepsTotal));
            stringNum = Integer.parseInt(String.valueOf(setGoal.getText()));
            float percentage = ((float) stepsSinceReset / stringNum) * 100;
            count.setText(str + "%");
            Log.d("check", String.valueOf(percentage));
            prg.setProgress((int) percentage);
            Log.d("kijken", saveData);
            mDatabase.child("Steps").setValue(saveData);
        }else{
            event.values[0] = 0;
        }}

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }





}


