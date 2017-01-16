package com.example.anh.fitapp;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
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

public class ExerciseActivity extends AppCompatActivity {

    //Assign variables
    public ListView lv;
    public ArrayList<String> idlist;
    public ArrayList<String> namelist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise);
        new Task().execute();

        //declarations
        idlist = new ArrayList<>();
        namelist = new ArrayList<>();
        lv = (ListView)findViewById(R.id.searchlist);

    }

    public class Task extends AsyncTask<String, Object, String> {
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
                    }
                }


                } catch (JSONException e) {
                e.printStackTrace();
            }


        }

}}


