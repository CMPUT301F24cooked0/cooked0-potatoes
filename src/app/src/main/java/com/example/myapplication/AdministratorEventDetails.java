package com.example.myapplication;

import android.app.AlertDialog;
import android.app.AppComponentFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.time.Instant;
import java.time.format.DateTimeFormatter;

public class AdministratorEventDetails extends AppCompatActivity {
    private Event selectedEvent;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.administrator_event_details);

        selectedEvent=(Event) getIntent().getSerializableExtra("selectedEvent");
        if(selectedEvent==null){
            Toast.makeText(this,"Event Data Unavailable",Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        ImageView eventPoster=findViewById(R.id.event_detail_poster);
        TextView eventName=findViewById(R.id.event_detail_name);
        TextView eventDescription=findViewById(R.id.event_detail_description);
        TextView eventTime=findViewById(R.id.event_detail_time);
        TextView eventDate=findViewById(R.id.event_detail_date);
        TextView eventCap=findViewById(R.id.event_detail_capacity);
        TextView eventRegStart=findViewById(R.id.event_detail_regstart);
        TextView eventRegEnd=findViewById(R.id.event_detail_regend);
        Button removeEventButton=findViewById(R.id.remove_event_button);
        Button removeQRButton=findViewById(R.id.remove_qr_button);
        Button returnButton=findViewById(R.id.event_detail_return_button);


        eventName.setText(selectedEvent.getName());
        eventDescription.setText(selectedEvent.getDescription());
        eventCap.setText(selectedEvent.getCapacity());
        //TODO Time & date for the event and registration start & end date
        eventPoster.setImageBitmap(selectedEvent.getEventPoster());

        removeEventButton.setOnClickListener(v-> showDeletePage());
        removeQRButton.setOnClickListener(v -> {
            selectedEvent.invalidateQRCode();
            //TODO generate and set new qrcode
        });

        returnButton.setOnClickListener(v -> finish());

    }

    /***
     * Method to show delete dialog page to confirm or cancel deletion of event
     */
    public void showDeletePage() {
        LayoutInflater inflater= LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.delete_dialog,null);
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
        builder.setView(dialogView);

        TextView dialogTitle = dialogView.findViewById(R.id.dialog_title);
        TextView dialogMessage=dialogView.findViewById(R.id.dialog_message);
        dialogTitle.setText("Remove Event");
        dialogMessage.setText("Are you sure you want to remove this event?");
        Button cancelButton = dialogView.findViewById(R.id.dialog_cancel_button);
        Button removeButton = dialogView.findViewById(R.id.dialog_remove_button);
        androidx.appcompat.app.AlertDialog dialog = builder.create();

        cancelButton.setOnClickListener(view -> dialog.dismiss());
        removeButton.setOnClickListener(view -> {
            if(selectedEvent==null){
                Toast.makeText(this,"No event to delete",Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                return;
            }

            DatabaseManager databaseManager=new DatabaseManager();
            boolean result=databaseManager.deleteEvent(selectedEvent);
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
        dialog.show();
    }
}
