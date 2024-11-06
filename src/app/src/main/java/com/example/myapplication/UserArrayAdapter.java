package com.example.myapplication;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class UserArrayAdapter extends ArrayAdapter<User> {
    private Context context;
    private ArrayList<User> users;

    public UserArrayAdapter(Context context, ArrayList<User> users){
        super(context,0,users);
        this.context=context;
        this.users=users;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        if(convertView==null){
            convertView= LayoutInflater.from(context).inflate(R.layout.user_profile_summary,parent,false);
        }
        User currentUser=users.get(position);
        TextView userName=convertView.findViewById(R.id.user_profile_username);
        userName.setText(currentUser.getName());
        return convertView;
    }
}

