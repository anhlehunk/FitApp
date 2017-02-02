package com.example.anh.fitapp;

import android.content.Intent;
import android.content.res.Resources;
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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
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
import java.util.ArrayList;

import static android.R.attr.id;
import static android.widget.Toast.makeText;

public class ExerciseActivity extends AppCompatActivity {

    //Assign variables
    ImageView image;
    public Spinner spinner;
    public ArrayList<String> idlist;
    public ArrayList<String> namelist;
    Button b1;
    Button button1;
    String muscleID;
    TextView translatedMuscleView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise);
        new Task().execute();

        //declarations
        final Resources res = getResources();
        image = (ImageView) findViewById(R.id.muscle_image);
        idlist = new ArrayList<>();
        namelist = new ArrayList<>();
        b1 = (Button) findViewById(R.id.search_id);
        translatedMuscleView = (TextView) findViewById(R.id.translated_muscle_name);
        spinner = (Spinner)findViewById(R.id.searchlist);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView idView = (TextView) view.findViewById(R.id.muscle_id);
                muscleID = idView.getText().toString();
                switch(muscleID){
                    case "1": image.setImageDrawable(res.getDrawable(R.drawable.id1));
                        break;
                    case "2": image.setImageDrawable(res.getDrawable(R.drawable.id2));
                        break;
                    case "3": image.setImageDrawable(res.getDrawable(R.drawable.id3));
                        break;
                    case "4": image.setImageDrawable(res.getDrawable(R.drawable.id4));
                        break;
                    case "5": image.setImageDrawable(res.getDrawable(R.drawable.id5));
                        break;
                    case "6": image.setImageDrawable(res.getDrawable(R.drawable.id6));
                        break;
                    case "7": image.setImageDrawable(res.getDrawable(R.drawable.id7));
                        break;
                    case "8": image.setImageDrawable(res.getDrawable(R.drawable.id8));
                        break;
                    case "9": image.setImageDrawable(res.getDrawable(R.drawable.id9));
                        break;
                    case "10": image.setImageDrawable(res.getDrawable(R.drawable.id10));
                        break;
                    case "11": image.setImageDrawable(res.getDrawable(R.drawable.id11));
                        break;
                    case "12": image.setImageDrawable(res.getDrawable(R.drawable.id12));
                        break;
                    case "13": image.setImageDrawable(res.getDrawable(R.drawable.id13));
                        break;
                    case "14": image.setImageDrawable(res.getDrawable(R.drawable.id14));
                        break;
                    case "15": image.setImageDrawable(res.getDrawable(R.drawable.id15));
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
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
            case R.id.stat:
                startActivity(new Intent(this, StatActivity.class));
                return true;
            case R.id.step:
                startActivity(new Intent(this, StepCountActivity.class));
                return true;
            case R.id.home:
                startActivity(new Intent(this, MainActivity.class));
                return true;
            case R.id.run:
                startActivity(new Intent(this, SaveActivity.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public class Task extends AsyncTask<String, Object, String> {


        protected void onPreExecute() {
            button1 = (Button) findViewById(R.id.search_id);
            button1.setVisibility(View.INVISIBLE);
            Toast succesful = makeText(ExerciseActivity.this, "Loading..." , Toast.LENGTH_SHORT);
            succesful.show();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                InputStream input = new URL("https://wger.de/api/v2/muscle/?format=json").openStream();
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
            b1.setVisibility(View.VISIBLE);
            Toast succesful = makeText(ExerciseActivity.this, "Loaded" , Toast.LENGTH_SHORT);
            succesful.show();
            try {
                //pick out the needed data out of the query
                JSONObject jsonObject = new JSONObject(result);
                if (jsonObject.has("Error")) {
                } else{
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
                    }
                }

                //set adapter on the listview
                ExerciseMuscleAdapter arrayAdapter = new ExerciseMuscleAdapter(ExerciseActivity.this, namelist, idlist);
                ExerciseActivity.this.spinner.setAdapter(arrayAdapter);
                } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    public void searchExercise(View view) {
        TextView idView = (TextView) findViewById(R.id.muscle_id);
        TextView muscleView = (TextView) findViewById(R.id.muscle_name);
        String muscleID = idView.getText().toString();
        String muscleName = muscleView.getText().toString();
        Intent searchExercise = new Intent(this, ExerciseFoundActivity.class);
        searchExercise.putExtra("searched_id", muscleID);
        searchExercise.putExtra("searched_name", muscleName);
        startActivity(searchExercise);
        }
    }


