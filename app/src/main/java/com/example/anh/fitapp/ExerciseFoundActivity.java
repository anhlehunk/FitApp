package com.example.anh.fitapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import static android.widget.Toast.makeText;

public class ExerciseFoundActivity extends AppCompatActivity {

    String searched_id;
    String searched_name;
    TextView lookedFor;
    public ArrayList<String> idlist;
    public ArrayList<String> namelist;
    public ListView lv;
    String exerciseName;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_found);


        //Get extras from previous activity
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            searched_id = extras.getString("searched_id");
            searched_name = extras.getString("searched_name");
        }

        idlist = new ArrayList<>();
        namelist = new ArrayList<>();
        lv = (ListView) findViewById(R.id.exercises_list);
        lookedFor = (TextView) findViewById(R.id.muscle_title);
        lookedFor.setText("Looked for: " + searched_name);
        executeTask();

        // Handles clicks items of the list
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            // takes the ID and give it to an intent, because the id is required to search for one specific item
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView nameView = (TextView) view.findViewById(R.id.muscle_name);
                TextView idView = (TextView) view.findViewById(R.id.muscle_id);
                String exerciseName = nameView.getText().toString();
                String exerciseID = idView.getText().toString();

                Intent searchExercise = new Intent(ExerciseFoundActivity.this, ExerciseInfoActivity.class);

                searchExercise.putExtra("searched_exercise", exerciseName);
                searchExercise.putExtra("searched_exercise_id", exerciseID);
                startActivity(searchExercise);
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.exercise_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.home:
                startActivity(new Intent(this, MainActivity.class));
                return true;
            case R.id.run:
                startActivity(new Intent(this, RunningActivity.class));
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


    public class ExercisesTask extends AsyncTask<String, Object, String> {

        @Override
        protected String doInBackground(String... params) {


            try {
                //the url is built with the input of the user
                InputStream input = new URL("https://wger.de/api/v2/exercise/?format=json&language=2&muscles=" + searched_id).openStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                StringBuilder result = new StringBuilder();
                String line;
                while((line = reader.readLine()) != null) {
                    result.append(line);
                }
                return String.valueOf(result);
            }
            catch (IOException e) {
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
                            namelist.add(name);
                            idlist.add(id);
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }}

                //set adapter on the listview
                ExerciseAdapter arrayAdapter = new ExerciseAdapter(ExerciseFoundActivity.this, namelist, idlist);
                ExerciseFoundActivity.this.lv.setAdapter(arrayAdapter);

            } catch (JSONException e) {
                e.printStackTrace();
            }}
    }





    public void executeTask() {

        try {
            new ExercisesTask().execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }





}
