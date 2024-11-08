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

public class AdministratorEventDetails extends AppCompatActivity {
    private Event event;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.administrator_event_details);

        event=(Event) getIntent().getSerializableExtra("event");

        ImageView eventPoster=findViewById(R.id.event_detail_poster);
        TextView eventName=findViewById(R.id.event_detail_name);
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
        LayoutInflater inflater= LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.delete_dialog, null);

        TextView dialogTitle=dialogView.findViewById(R.id.dialog_title);
        TextView dialogMessage=dialogView.findViewById(R.id.dialog_message);
        Button cancelButton=dialogView.findViewById(R.id.dialog_cancel_button);
        Button removeButton=dialogView.findViewById(R.id.dialog_remove_button);
        dialogTitle.setText("Remove Event");
        dialogMessage.setText("Are you sure you want to remove this event?");
        AlertDialog dialog=new AlertDialog.Builder(this).setView(dialogView).create();

        cancelButton.setOnClickListener(view -> dialog.dismiss());
        removeButton.setOnClickListener(view -> {
            event.deleteEvent(
                    aVoid->{
                        Toast.makeText(this,"Succesfully removed event",Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                        finish();
                    },e -> {
                        Toast.makeText(this,"Failed to remove event",Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
            );
        });
        dialog.show();
    }
}
