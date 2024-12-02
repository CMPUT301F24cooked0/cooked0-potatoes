package com.example.myapplication;

import android.app.AlertDialog;
import android.app.AppComponentFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class AdministratorEventDetails extends AppCompatActivity {
    private Event selectedEvent;
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm").withLocale(Locale.getDefault());

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.administrator_event_details);

        ImageView eventPoster=findViewById(R.id.event_detail_poster);
        TextView eventName=findViewById(R.id.event_detail_name);
        TextView eventDescription=findViewById(R.id.event_detail_description);
        TextView eventDateTime=findViewById(R.id.event_detail_datetime);
        TextView eventRegStart=findViewById(R.id.event_detail_regstart);
        TextView eventRegEnd=findViewById(R.id.event_detail_regend);
        Button removeEventButton=findViewById(R.id.remove_event_button);
        Button removeQRButton=findViewById(R.id.remove_qr_button);
        Button returnButton=findViewById(R.id.event_detail_return_button);

        ImageButton deletePosterButton=findViewById(R.id.delete_poster_button);
        String eventRefPath=getIntent().getStringExtra("eventRef");
        if(eventRefPath==null || eventRefPath.isEmpty()){
            Toast.makeText(this,"Event reference invalid",Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        new Thread(()->{
            try{
                DatabaseManager databaseManager=new DatabaseManager();
                selectedEvent=databaseManager.fetchEventByRefPath(eventRefPath);
                runOnUiThread(()->{
                    if(selectedEvent!=null) {
                        eventName.setText(selectedEvent.getName());
                        eventDescription.setText(selectedEvent.getDescription() != null ? selectedEvent.getDescription() : "");
                        eventPoster.setImageBitmap(selectedEvent.getEventPoster());

                        String formattedStartDateTime = formatDateTime(selectedEvent.getStartInstant());
                        String formattedEndDateTime = formatDateTime(selectedEvent.getEndInstant());
                        String formattedRegStartDateTime = formatDateTime(selectedEvent.getRegistrationStartInstant());
                        String formattedRegEndDateTime = formatDateTime(selectedEvent.getRegistrationEndInstant());

                        eventDateTime.setText(formattedStartDateTime != null && formattedEndDateTime != null ? formattedStartDateTime + " - " + formattedEndDateTime : "No event date and time available");
                        eventRegStart.setText(formattedRegStartDateTime != null ? formattedRegStartDateTime : "No reg start time");
                        eventRegEnd.setText(formattedRegEndDateTime != null ? formattedRegEndDateTime : "No reg end time");
                    }else {
                        Toast.makeText(this, "Event not found", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
            }
            catch (Exception e){
                runOnUiThread(()->{
                    Toast.makeText(this,"Error fetching event",Toast.LENGTH_SHORT).show();
                    finish();
                });
            }
        }).start();

        deletePosterButton.setOnClickListener(view -> showImageDeletePage());
        removeEventButton.setOnClickListener(view -> showDeletePage());
        removeQRButton.setOnClickListener(v -> {
            selectedEvent.invalidateQRCode();
            Toast.makeText(this,"QR code invalidated",Toast.LENGTH_SHORT).show();
            //TODO generate and set new qrcode
            //Toast.makeText(this,"New QR code generated",Toast.LENGTH_SHORT).show();
        });

        returnButton.setOnClickListener(v -> finish());

    }

    /**
     * Formats an Instant into a string in "dd/MM/yyyy HH:mm" format.
     *
     * @param instant the Instant to format.
     * @return the formatted string.
     */
    private String formatDateTime(Instant instant) {
        try {
            return instant.atZone(ZoneId.systemDefault()).toLocalDateTime().format(dateFormatter);
        } catch (Exception e) {
            return null;
        }
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

    public void showImageDeletePage() {
        LayoutInflater inflater= LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.delete_dialog,null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);

        TextView dialogTitle = dialogView.findViewById(R.id.dialog_title);
        TextView dialogMessage=dialogView.findViewById(R.id.dialog_message);
        dialogTitle.setText("Remove Event Poster");
        dialogMessage.setText("Are you sure you want to remove this photo");
        Button cancelButton = dialogView.findViewById(R.id.dialog_cancel_button);
        Button removeButton = dialogView.findViewById(R.id.dialog_remove_button);
        AlertDialog dialog = builder.create();

        cancelButton.setOnClickListener(view -> dialog.dismiss());
        removeButton.setOnClickListener(view -> {
            if(selectedEvent==null){
                Toast.makeText(this,"No Profile Picture to delete",Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                return;
            }
            try {
                selectedEvent.setEventPoster(null);
                ImageView eventPoster=findViewById(R.id.event_detail_poster);
                eventPoster.setImageDrawable(null);
                eventPoster.setVisibility(View.INVISIBLE);

                new Thread(()->{
                   try{
                       DatabaseManager databaseManager=new DatabaseManager();
                       databaseManager.updateEvent(selectedEvent);
                       runOnUiThread(()->{
                           Toast.makeText(this,"Event poster removed successfully",Toast.LENGTH_SHORT).show();
                           dialog.dismiss();
                       });
                   }
                   catch (Exception e){
                       runOnUiThread(()->{
                           Toast.makeText(this,"Error removing event poster",Toast.LENGTH_SHORT).show();
                       });
                   }
                }).start();
            }
            catch (Exception e) {
                Toast.makeText(this,"Error setting event poster",Toast.LENGTH_SHORT).show();
            }
        });
        dialog.show();
    }
}
