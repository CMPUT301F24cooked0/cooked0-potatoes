package com.example.myapplication;

import android.app.AlertDialog;
import android.app.AppComponentFactory;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class AdministratorEventDetails extends AppCompatActivity {
    private Event event;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.administrator_event_details);

        event=(Event) getIntent().getSerializableExtra("event");

        ImageView eventPoster=findViewById(R.id.event_poster);
        TextView eventName=findViewById(R.id.event_name);
        Button removeEventButton=findViewById(R.id.remove_event_button);
        Button removeQRButton=findViewById(R.id.remove_qr_button);
        Button returnButton=findViewById(R.id.event_detail_return_button);

        if(event!=null){
            if(event.getEventPoster()!=null){
                eventPoster.setImageBitmap(event.getEventPoster());
            }
            eventName.setText(event.getName());

            removeEventButton.setOnClickListener(v-> showDeleteDialog());
            removeQRButton.setOnClickListener(v -> {
                event.invalidateQRCode();
                //regenerate QR code probably make a new method in Events
            });

            returnButton.setOnClickListener(v -> finish());
        }
    }

    private void showDeleteDialog(){
        new AlertDialog.Builder(this)
                .setTitle("Remove Event")
                .setMessage("Are you sure you want to remove this event?")
                .setPositiveButton("Confirm",(dialog, which) -> {
                    event.deleteEvent(
                            aVoid->{
                                Toast.makeText(this,"Succesfully removed event",Toast.LENGTH_SHORT).show();
                                finish();
                            },
                            e -> {
                                Toast.makeText(this, "Failed to remove event", Toast.LENGTH_SHORT).show();
                            }
                    );
                })
                .setNegativeButton("Cancel",null).show();
    }
}
