package com.example.anh.fitapp;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.util.concurrent.ExecutionException;

public class ExerciseInfoActivity extends AppCompatActivity {

    String searched_id;
    String searched_name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_info);

        //Get extras from previous activity
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            searched_id = extras.getString("searched_exercise_id");
            searched_name = extras.getString("searched_exercise");
        }
        executeInFo();
    }

    public class ExercisesInfo extends AsyncTask<String, Object, String> {


        @Override
        protected String doInBackground(String... params) {



            try {
                //the url is built with the input of the user
                Log.d("proberen2", searched_name + searched_id);
                InputStream input = new URL("https://wger.de/api/v2/exercise/?format=json&language=2&name=" + URLEncoder.encode(searched_name, "UTF-8")).openStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                StringBuilder result = new StringBuilder();
                String line;
                while((line = reader.readLine()) != null) {
                    result.append(line);
                }
                Log.d("proberen2", String.valueOf(result));
                return String.valueOf(result);
            }
            catch (IOException e) {
                Log.d("MainActivity", "Error" + e);
                return null;
            }
        }

        public void onPostExecute(String result) {
           /* try {
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
            }}*/
        }}

    public void executeInFo() {

        try {
            new ExercisesInfo().execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

}
