package com.example.anh.fitapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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
    String steps;
    String stepStat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stat);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child(mAuth.getCurrentUser().getUid()).child("Steps");
        stepView = (TextView) findViewById(R.id.totalSteps);
        prg =(ProgressBar) findViewById(R.id.progressBar1) ;

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                steps = dataSnapshot.getValue().toString();
                stepView.setText(steps);

                stepStat = stepView.getText().toString();

                prg.setProgress((int) ((float) Integer.parseInt(stepStat)/1000 * 100));
                Log.d("wtf", String.valueOf((int) ((float) Integer.parseInt(stepStat)/1000 * 100)));
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });



        //prg.setProgress((int) (((float)Integer.parseInt(steps) / 1000) *100));

    }
}
