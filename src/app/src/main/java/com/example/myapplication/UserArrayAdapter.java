package com.example.myapplication;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
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
            convertView= LayoutInflater.from(context).inflate(R.layout.admin_user_list,parent,false);
        }
        User currentUser=users.get(position);
        ImageView userImage=convertView.findViewById(R.id.userlistimage);
        TextView userName=convertView.findViewById(R.id.userlistname);
        userImage.setImageBitmap(currentUser.getProfilePicture());
        userName.setText(currentUser.getName());
        return convertView;
    }
}

