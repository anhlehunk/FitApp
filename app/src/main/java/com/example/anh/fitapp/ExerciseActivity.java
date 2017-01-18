package com.example.anh.fitapp;

import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
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

import static android.R.id.button1;
import static android.widget.Toast.makeText;

public class ExerciseActivity extends AppCompatActivity {

    //Assign variables
    ImageView image;
    public Spinner spinner;
    public ArrayList<String> idlist;
    public ArrayList<String> namelist;
    Button b1;
    Button button1;


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



        spinner = (Spinner)findViewById(R.id.searchlist);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView idView = (TextView) view.findViewById(R.id.muscle_id);
                String muscleID = idView.getText().toString();
                Toast succesful = makeText(ExerciseActivity.this, muscleID , Toast.LENGTH_SHORT);
                succesful.show();

                switch(muscleID){
                    case "1": image.setImageDrawable(res.getDrawable(R.drawable.runbutton));

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

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
                //the url is built with the input of the user
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
                ExerciseAdapter arrayAdapter = new ExerciseAdapter(ExerciseActivity.this, namelist, idlist);
                ExerciseActivity.this.spinner.setAdapter(arrayAdapter);

                } catch (JSONException e) {
                e.printStackTrace();
            }}

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


        }}


