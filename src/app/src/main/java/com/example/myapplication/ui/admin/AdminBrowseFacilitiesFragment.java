package com.example.myapplication.ui.admin;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.myapplication.DatabaseManager;
import com.example.myapplication.Facility;
import com.example.myapplication.FacilityArrayAdapter;
import com.example.myapplication.R;

import java.util.ArrayList;

public class AdminBrowseFacilitiesFragment extends Fragment {
    private ListView facilitiesList;
    private ArrayList<Facility> facilitiesDataList;
    private FacilityArrayAdapter facilityArrayAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View view=inflater.inflate(R.layout.admin_browse_facilities,container,false);
        facilitiesList=view.findViewById(R.id.facilities_list);
        facilitiesDataList=new ArrayList<>();
        facilityArrayAdapter=new FacilityArrayAdapter(requireContext(),facilitiesDataList);
        facilitiesList.setAdapter(facilityArrayAdapter);
        DatabaseManager databaseManager=new DatabaseManager();
        databaseManager.getAllFacilities(facilities -> {
            if(facilities!=null && !facilities.isEmpty()){
                requireActivity().runOnUiThread(()->{
                    facilitiesDataList.clear();
                    facilitiesDataList.addAll(facilities);
                    facilityArrayAdapter.notifyDataSetChanged();
                    Log.d("Fetch Facilities","Success");
                });
            }
            else {
                requireActivity().runOnUiThread(()->{
                    Log.w("Fetch Facilities","Failed");
                    Toast.makeText(requireContext(),"No Facilities Found",Toast.LENGTH_SHORT).show();
                });
            }
        });

        facilitiesList.setOnItemClickListener((parent,view1,position,id)->{
            Facility selectedFacility=facilitiesDataList.get(position);
            Intent intent=new Intent(getContext(), AdminFacilityDetails.class);
            intent.putExtra("facilityRef",selectedFacility.getFacilityReference().getPath());
            startActivity(intent);
        });
        return view;
    }
}
