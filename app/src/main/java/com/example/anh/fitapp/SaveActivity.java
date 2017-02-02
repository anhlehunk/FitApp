package com.example.anh.fitapp;

import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import static android.widget.Toast.makeText;

public class SaveActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    ListView lv;
    ArrayList<String> list = new ArrayList<>();
    ArrayList<String> listID = new ArrayList<>();

    SaveAdapter arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save);
        lv = (ListView) findViewById(R.id.listSaved);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child(mAuth.getCurrentUser().getUid()).child("Exercises");

        //set adapter on the listview
        arrayAdapter = new SaveAdapter(SaveActivity.this, list, listID);
        SaveActivity.this.lv.setAdapter(arrayAdapter);
        searchExercise();
        deleteExercise();


        mDatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String value = dataSnapshot.getKey();
                String valueID = (String) dataSnapshot.getValue();
                Log.d("check", valueID);

                list.add(value);
                listID.add(valueID);

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.save_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.home:
                startActivity(new Intent(this, MainActivity.class));
                return true;
            case R.id.run:
                startActivity(new Intent(this,ExerciseActivity.class));
                return true;
            case R.id.stat:
                startActivity(new Intent(this, StatActivity.class));
                return true;
            case R.id.step:
                startActivity(new Intent(this, StepCountActivity.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void deleteExercise(){
        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                TextView nameView = (TextView) view.findViewById(R.id.exercise_name);
                final String exerciseName = nameView.getText().toString();



                mDatabase.child(exerciseName).setValue(null);
                finish();
                overridePendingTransition(0, 0);
                Intent intent = getIntent();
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                overridePendingTransition(0, 0);
                Toast succesful = makeText(SaveActivity.this, exerciseName + " is deleted!" , Toast.LENGTH_SHORT);
                succesful.show();









                return false;
            }
            });
        }

    public void searchExercise(){
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView nameView = (TextView) view.findViewById(R.id.exercise_name);
                String exerciseName = nameView.getText().toString();
                TextView idView = (TextView) view.findViewById(R.id.exercise_id);
                String exerciseID= idView.getText().toString();

                Intent searchExercise = new Intent(SaveActivity.this, ExerciseInfoActivity.class);

                searchExercise.putExtra("searched_exercise", exerciseName);
                searchExercise.putExtra("searched_exercise_id", exerciseID);
                startActivity(searchExercise);


            }
        });
    }

    public void Help(View view) {
        /* Show a small help snackbar to user */
        final Snackbar snackBar = Snackbar.make(findViewById(R.id.help),
                "Click to show info, long-click to delete.",
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

