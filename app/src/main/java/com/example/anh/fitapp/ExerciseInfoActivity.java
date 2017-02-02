package com.example.anh.fitapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.util.concurrent.ExecutionException;

import static com.example.anh.fitapp.R.id.count;
import static com.example.anh.fitapp.R.id.countStep;
import static com.example.anh.fitapp.StepCountActivity.myPreference;

public class ExerciseInfoActivity extends AppCompatActivity {

    String searched_id;
    String searched_name;
    TextView exerciseTitle;
    TextView exerciseDescription;
    TextView exerciseID;
    ImageView exerciseImage1;
    ImageView exerciseImage2;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_info);
        //Declarations
        exerciseTitle = (TextView) findViewById(R.id.exercise_title);
        exerciseDescription = (TextView) findViewById(R.id.exercise_description);
        exerciseID = (TextView) findViewById(R.id.exercise_id);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child(mAuth.getCurrentUser().getUid()).child("Exercises");

        //Get extras from previous activity
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            searched_id = extras.getString("searched_exercise_id");
            searched_name = extras.getString("searched_exercise");
        }
        exerciseImage1 = (ImageView) findViewById(R.id.image1);
        exerciseImage2 = (ImageView) findViewById(R.id.image2);
        executeInFo();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.exercise_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {switch (item.getItemId()){
            case R.id.home:
                startActivity(new Intent(this, MainActivity.class));
                return true;
            case R.id.run:
                startActivity(new Intent(this, SaveActivity.class));
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

    public class ExercisesInfo extends AsyncTask<String, Object, String> {
        @Override
        protected String doInBackground(String... params) {
            try {
                //the url is built with the input of the user
                InputStream input = new URL("https://wger.de/api/v2/exercise/?format=json&language=2&name="
                        + URLEncoder.encode(searched_name, "UTF-8")).openStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                StringBuilder result = new StringBuilder();
                String line;
                while((line = reader.readLine()) != null) {
                    result.append(line);
                }
                return String.valueOf(result);

            } catch (IOException e) {
                Log.d("MainActivity", "Error" + e);
                return null;
            }
        }

        public void onPostExecute(String result) {
            try {
                //pick out the needed data out of the query
                JSONObject jsonObject = new JSONObject(result);

                if (jsonObject.has("Error")) {
                    Log.d("oops", "foutje");
                }
                else{
                    try {
                        //loops through all the results and add the title, image link, and unique id to two different lists
                        JSONArray jsonArray = jsonObject.getJSONArray("results");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jObj = jsonArray.getJSONObject(i);
                            String name = jObj.getString("name");
                            String id = jObj.getString("id");
                            String description = jObj.getString("description");

                            //Replace unnecassary stuff
                            description = description.replace("<p>", "");
                            description = description.replace("</p>", "\n");
                            description = description.replace("</ol>", "\n");
                            description = description.replace("<ol>", "\n");
                            description = description.replace("<li>", "-");
                            description = description.replace("</li>", "");

                            //Put the results in the right place
                            exerciseTitle.setText(name);
                            exerciseID.setText(id);
                            exerciseDescription.setText(description);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public class ExercisesImage extends AsyncTask<String, Object, String> {

        @Override
        protected String doInBackground(String... params) {
            try {
                //the url is built with the input of the user
                InputStream input2 = new URL("https://wger.de/api/v2/exerciseimage/?exercise="
                        + URLEncoder.encode(searched_id, "UTF-8")).openStream();
                BufferedReader reader2 = new BufferedReader(new InputStreamReader(input2));
                StringBuilder result2 = new StringBuilder();
                String line2;
                while((line2 = reader2.readLine()) != null) {
                    result2.append(line2);
                }
                return String.valueOf(result2);
            } catch (IOException e) {
                Log.d("MainActivity", "Error" + e);
                return null;
            }
        }

        public void onPostExecute(String result) {
            try {
                //pick out the needed data out of the query
                JSONObject jsonObject = new JSONObject(result);
                if (jsonObject.has("Error")) {
                } else{
                    try {
                        //loops through all the results and add the title, image link, and unique id to two different lists
                        JSONArray jsonArray = jsonObject.getJSONArray("results");
                        if (jsonArray.length() == 0){
                        } else{

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jObj = jsonArray.getJSONObject(i);
                            String parseID = jObj.getString("id");
                            int foo= Integer.parseInt(parseID);
                            //If there are 2 images, one always has an even digit and the other an uneven digit
                            if ( (foo % 2) == 0 ){
                            String image = jObj.getString("image");
                            Picasso.with(ExerciseInfoActivity.this).load(image.toString()).resize(300, 400).into(exerciseImage1);
                            } else{
                            String image = jObj.getString("image");
                            Picasso.with(ExerciseInfoActivity.this).load(image.toString()).resize(300, 400).into(exerciseImage2);}
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void executeInFo() {
        try {
            new ExercisesInfo().execute().get();
            new ExercisesImage().execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    public void saveExercise(View view) {
        String name =  exerciseTitle.getText().toString();
        String ID = exerciseID.getText().toString();
        mDatabase.child(name).setValue(ID);
        Snackbar.make(findViewById(R.id.saveButton),
                "Saved!",
                Snackbar.LENGTH_LONG)
                .setAction("Go to list", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(ExerciseInfoActivity.this, SaveActivity.class));
                    }
                })
                .setActionTextColor(Color.WHITE)
                .show();
            }
        }
