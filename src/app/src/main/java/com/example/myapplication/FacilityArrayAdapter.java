package com.example.myapplication;

import static com.example.myapplication.ProfilePictureGenerator.generateProfileImage;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.ArrayList;

public class FacilityArrayAdapter extends ArrayAdapter<Facility> {
    public FacilityArrayAdapter(Context context, ArrayList<Facility> facilitiesDataList) {
        super(context,0,facilitiesDataList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        Facility facility=getItem(position);
        if(convertView==null){
            convertView= LayoutInflater.from(getContext()).inflate(R.layout.admin_facilties_item_summary,parent,false);
        }
        ImageView facilityImage=convertView.findViewById(R.id.facility_image);
        TextView facilityName=convertView.findViewById(R.id.facility_name);

        Bitmap generatedImage=generateProfileImage(facility.getName());
        facilityImage.setImageBitmap(generatedImage);
        facilityName.setText(facility.getName());
        return convertView;
    }
}
