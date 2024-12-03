package com.example.myapplication.ui.admin.facilities;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.DatabaseManager;
import com.example.myapplication.Facility;
import com.example.myapplication.R;

public class AdminFacilityDetails extends AppCompatActivity {
    private Facility selectedFacility;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_facility_details);

        TextView facilityName=findViewById(R.id.facility_details_name);
        TextView facilityAddress=findViewById(R.id.facility_details_address);
        Button removeFacilityButton=findViewById(R.id.remove_facility_button);
        Button returnButton=findViewById(R.id.return_button);

        String facilityRefPath=getIntent().getStringExtra("facilityRef");
        if(facilityRefPath==null || facilityRefPath.isEmpty()){
            Toast.makeText(this,"Invalid Facility Reference",Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        new Thread(()->{
            DatabaseManager databaseManager=new DatabaseManager();
           try {
               selectedFacility = databaseManager.fetchFacilityByRefPath(facilityRefPath);
               if (selectedFacility != null) {
                   runOnUiThread(() -> {
                       facilityName.setText(selectedFacility.getName());
                       facilityAddress.setText(selectedFacility.getAddress());
                   });
               } else {
                   runOnUiThread(() -> {
                       Toast.makeText(this, "Facility Not Found", Toast.LENGTH_SHORT).show();
                       finish();
                   });
               }
           }
           catch (Exception e){
               runOnUiThread(()->{
                   Toast.makeText(this,"Error Fetching Facility Data",Toast.LENGTH_SHORT).show();
                   finish();
               });
           }
        }).start();

        removeFacilityButton.setOnClickListener(view -> {
            if(selectedFacility==null){
                Toast.makeText(this,"Facility data not loaded",Toast.LENGTH_SHORT).show();
                return;
            }
            showDeletePage(selectedFacility);
        });
        returnButton.setOnClickListener(view -> finish());
    }

    public void showDeletePage(Facility facility) {
        LayoutInflater inflater= LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.delete_dialog,null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);

        TextView dialogTitle = dialogView.findViewById(R.id.dialog_title);
        TextView dialogMessage=dialogView.findViewById(R.id.dialog_message);
        dialogTitle.setText("Remove Facility");
        dialogMessage.setText("Are you sure you want to remove this facility?");
        Button cancelButton = dialogView.findViewById(R.id.dialog_cancel_button);
        Button removeButton = dialogView.findViewById(R.id.dialog_remove_button);
        AlertDialog dialog = builder.create();

        cancelButton.setOnClickListener(view -> dialog.dismiss());
        removeButton.setOnClickListener(view -> {
            if(facility==null){
                Toast.makeText(this,"No Facility",Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                return;
            }
            new Thread(()->{
                DatabaseManager databaseManager=new DatabaseManager();
                boolean result=databaseManager.deleteFacility(facility);
                runOnUiThread(()->{
                    if(result){
                        Toast.makeText(this,"Success",Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                        finish();
                    }
                    else{
                        Toast.makeText(this,"Fail",Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });
            }).start();
        });

        dialog.show();
    }
}
