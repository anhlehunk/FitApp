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

import java.util.ArrayList;



class ExerciseMuscleAdapter extends BaseAdapter {
    private ArrayList<String> namelist;
    private ArrayList<String> idlist;
    private Context context;

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
        // Textview with the title of the art.
        title.setText(namelist.get(position));
        TextView art_id = (TextView) view.findViewById(R.id.muscle_id);
        //textview with invisible ID
        art_id.setText(idlist.get(position));


        return view;
    }
}


