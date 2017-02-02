package com.example.anh.fitapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Anh on 31-1-2017.
 */

public class SaveAdapter extends BaseAdapter {
    private ArrayList<String> namelist;
    private ArrayList<String> idlist;
    private Context context;

    SaveAdapter(Context context, ArrayList<String> namelist, ArrayList<String> idlist){
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
            view = inflater.inflate(R.layout.item_saved, null);
        }
        TextView title = (TextView) view.findViewById(R.id.exercise_name);
        // Textview with the title of the exercise
        title.setText(namelist.get(position));

        TextView titleID = (TextView) view.findViewById(R.id.exercise_id);
        titleID.setText(idlist.get(position));
        return view;
    }
}
