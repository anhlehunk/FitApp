package com.example.anh.fitapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class StatActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private TextView stepView;
    private FirebaseAuth mAuth;
    ProgressBar prg;
    ProgressBar prg1;
    String steps;
    String stepStat;
    TextView percentage1;
    TextView percentage2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stat);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child(mAuth.getCurrentUser().getUid()).child("Steps");
        stepView = (TextView) findViewById(R.id.totalSteps);
        prg =(ProgressBar) findViewById(R.id.progressBar1) ;
        prg1 =(ProgressBar) findViewById(R.id.progressBar2) ;
        percentage1 = (TextView) findViewById(R.id.percentage1);
        percentage2 = (TextView) findViewById(R.id.percentage2);


        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                steps = dataSnapshot.getValue().toString();
                stepView.setText(steps);

                stepStat = stepView.getText().toString();
                String str = String.format("%.1f", ((float) Integer.parseInt(stepStat)/1000) * 100.0);
                String str2 = String.format("%.1f", ((float) Integer.parseInt(stepStat)/5000) * 100.0);

                prg.setProgress((int) ((float) Integer.parseInt(stepStat)/1000 * 100));
                percentage1.setText(str + "%");
                prg1.setProgress((int) ((float) Integer.parseInt(stepStat)/5000 * 100));
                percentage2.setText(str2 + "%");

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.stat_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.home:
                startActivity(new Intent(this, MainActivity.class));
                finish();
                return true;
            case R.id.exercise:
                startActivity(new Intent(this, ExerciseActivity.class));
                finish();
                return true;
            case R.id.step:
                startActivity(new Intent(this, StepCountActivity.class));
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
