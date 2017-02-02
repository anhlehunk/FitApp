package com.example.anh.fitapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.ads.formats.NativeAd;
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
    ProgressBar prg1;
    ProgressBar prg2;
    ProgressBar prg3;
    ProgressBar prg4;
    String steps;
    String stepStat;
    TextView percentage1;
    TextView percentage2;
    TextView percentage3;
    TextView percentage4;
    ImageView done1;
    ImageView done2;
    ImageView done3;
    ImageView done4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stat);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child(mAuth.getCurrentUser().getUid()).child("Steps");
        stepView = (TextView) findViewById(R.id.totalSteps);
        prg1 =(ProgressBar) findViewById(R.id.progressBar1) ;
        prg2 =(ProgressBar) findViewById(R.id.progressBar2) ;
        prg3 =(ProgressBar) findViewById(R.id.progressBar3) ;
        prg4 =(ProgressBar) findViewById(R.id.progressBar4) ;
        percentage1 = (TextView) findViewById(R.id.percentage1);
        percentage2 = (TextView) findViewById(R.id.percentage2);
        percentage3 = (TextView) findViewById(R.id.percentage3);
        percentage4 = (TextView) findViewById(R.id.percentage4);
        done1= (ImageView) findViewById(R.id.done1);
        done2= (ImageView) findViewById(R.id.done2);
        done3= (ImageView) findViewById(R.id.done3);
        done4= (ImageView) findViewById(R.id.done4);


        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                steps = dataSnapshot.getValue().toString();
                stepView.setText(steps);
                stepStat = stepView.getText().toString();
                if((int) ((float) Integer.parseInt(stepStat)/1000 * 100) < 100){
                String str = String.format("%.1f", ((float) Integer.parseInt(stepStat)/1000) * 100.0);
                prg1.setProgress((int) ((float) Integer.parseInt(stepStat)/1000 * 100));
                percentage1.setText(str + "%");
                } else {
                    done1.setVisibility(View.VISIBLE);
                }

                if((int) ((float) Integer.parseInt(stepStat)/5000 * 100) < 100){
                String str2 = String.format("%.1f", ((float) Integer.parseInt(stepStat)/5000) * 100.0);
                percentage2.setText(str2 + "%");
                prg2.setProgress((int) ((float) Integer.parseInt(stepStat)/5000 * 100));
                } else {
                    done2.setVisibility(View.VISIBLE);
                }


                if((int) ((float) Integer.parseInt(stepStat)/10000 * 100) < 100){
                    String str3 = String.format("%.1f", ((float) Integer.parseInt(stepStat)/10000) * 100.0);
                    percentage3.setText(str3 + "%");
                    prg3.setProgress((int) ((float) Integer.parseInt(stepStat)/10000 * 100));
                } else {
                    done3.setVisibility(View.VISIBLE);
                }


                if((int) ((float) Integer.parseInt(stepStat)/24000 * 100) < 100){
                    String str2 = String.format("%.1f", ((float) Integer.parseInt(stepStat)/24000) * 100.0);
                    percentage4.setText(str2 + "%");
                    prg4.setProgress((int) ((float) Integer.parseInt(stepStat)/24000 * 100));
                } else {
                    done4.setVisibility(View.VISIBLE);
                }
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
            case R.id.run:
                startActivity(new Intent(this, SaveActivity.class));
                finish();
                return true;
            case R.id.step:
                startActivity(new Intent(this, StepCountActivity.class));
                finish();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    public void Help(View view) {
        /* Show a small help snackbar to user */
        final Snackbar snackBar = Snackbar.make(findViewById(R.id.help),
                "Achievements for 1000, 5000, 10000, and 24000 steps.",
                Snackbar.LENGTH_INDEFINITE);

        snackBar.setAction("Got it!", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackBar.dismiss();
            }
        })
                .setActionTextColor(Color.WHITE);
        snackBar.show();
    }

}
