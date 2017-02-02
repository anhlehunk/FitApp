package com.example.anh.fitapp;

/**
 * Created by Anh on 21-1-2017.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import static android.widget.Toast.makeText;


class ExerciseMuscleAdapter extends BaseAdapter {
    private ArrayList<String> namelist;
    private ArrayList<String> idlist;
    private Context context;
    TextView translatedMuscleView;

    ExerciseMuscleAdapter(Context context, ArrayList<String> namelist, ArrayList<String> idlist){
        this.namelist = namelist;
        this.context = context;
        this.idlist = idlist;
    }

    @Override
    public int getCount() {
        return namelist.size();
    }

    @Override
    public Object getItem(int position) {
        return namelist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    // Sets adapter view for the search result listview.
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = convertView;
        if(view == null){
            view = inflater.inflate(R.layout.item_exercise_muscle, null);
        }
        TextView title = (TextView) view.findViewById(R.id.muscle_name);
        // Textview with the title
        title.setText(namelist.get(position));
        TextView art_id = (TextView) view.findViewById(R.id.muscle_id);
        //textview with invisible ID
        art_id.setText(idlist.get(position));

        String muscleID = art_id.getText().toString();

        translatedMuscleView = (TextView) view.findViewById(R.id.translated_muscle_name);
        switch(muscleID){
            case "1": ;
               translatedMuscleView.setText("Biceps");
                break;
            case "2": ;
                translatedMuscleView.setText("Schouderspieren");
                break;
            case "3": ;
                translatedMuscleView.setText("Schoudergordel");
                break;
            case "4": ;
                translatedMuscleView.setText("Borstspieren");
                break;
            case "5": ;
                translatedMuscleView.setText("Triceps");
                break;
            case "6": ;
                translatedMuscleView.setText("Buikspieren");
                break;
            case "7": ;
                translatedMuscleView.setText("Kuitspieren");
                break;
            case "8": ;
                translatedMuscleView.setText("Bilspieren");
                break;
            case "9": ;
                translatedMuscleView.setText("Trapezius (Nekspieren)");
                break;
            case "10": ;
                translatedMuscleView.setText("Dijspieren");
                break;
            case "11": ;
                translatedMuscleView.setText("Achterste beenspieren");
                break;
            case "12": ;
                translatedMuscleView.setText("Brede rugspieren");
                break;
            case "13": ;
                translatedMuscleView.setText("Elleboogspieren");
                break;
            case "14": ;
                translatedMuscleView.setText("Schuine buikspieren");
                break;
            case "15": ;
                translatedMuscleView.setText("Scholspieren");
                break;
        }
        return view;
    }
}


