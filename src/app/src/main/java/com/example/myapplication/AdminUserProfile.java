package com.example.myapplication;

import static com.example.myapplication.ProfilePictureGenerator.generateProfileImage;


import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class AdminUserProfile extends AppCompatActivity{ ;
    private User selectedUser;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile_summary);


        Button returnButton=findViewById(R.id.return_user_button);
        ImageButton removeImageButton=findViewById(R.id.delete_poster_button);

        String userUniqueID=getIntent().getStringExtra("uniqueID");
        if (userUniqueID == null || userUniqueID.isEmpty()) {
            Toast.makeText(this, "Invalid user ID", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        new Thread(()->{
            DatabaseManager databaseManager=new DatabaseManager();
            databaseManager.getUser(userUniqueID, user -> {
                if(user!=null){
                    selectedUser=user;
                    runOnUiThread(this::updateUserData);
                }
                else{
                    runOnUiThread(()->{
                        Toast.makeText(this,"User Not Found",Toast.LENGTH_SHORT).show();
                        finish();
                    });
                }
            });
        }).start();
        removeImageButton.setOnClickListener(view->showImageDeletePage());
        returnButton.setOnClickListener(view -> finish());
    }



    private void updateUserData() {
        if(selectedUser==null){
            Toast.makeText(this,"User data unavailable",Toast.LENGTH_SHORT).show();
        }
        ImageView profileImageView = findViewById(R.id.user_profile_image);
        TextView profileName = findViewById(R.id.user_profile_name);
        TextView profileEmail = findViewById(R.id.user_profile_email);
        TextView profileNumber = findViewById(R.id.user_profile_phonenumber);
        Button removeButton = findViewById(R.id.remove_user_button);


        profileName.setText(selectedUser.getName());
        profileEmail.setText(selectedUser.getEmail());
        profileNumber.setText(String.valueOf(selectedUser.getPhoneNumber()));

        if (selectedUser.getProfilePicture() != null) {
            profileImageView.setImageBitmap(selectedUser.getProfilePicture());
        } else {
            Bitmap generatedPicture = generateProfileImage(selectedUser.getName());
            profileImageView.setImageBitmap(generatedPicture);
        }

        profileImageView.setOnClickListener(view -> showImageDeletePage());
        removeButton.setOnClickListener(view -> showUserDeletePage());

    }

    /***
     * Method to show the dialog page to confirm or cancel the delete action
     */
    public void showUserDeletePage() {
        LayoutInflater inflater= LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.delete_dialog,null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);

        TextView dialogTitle = dialogView.findViewById(R.id.dialog_title);
        TextView dialogMessage=dialogView.findViewById(R.id.dialog_message);
        dialogTitle.setText("Remove User");
        dialogMessage.setText("Are you sure you want to remove this user?");
        Button cancelButton = dialogView.findViewById(R.id.dialog_cancel_button);
        Button removeButton = dialogView.findViewById(R.id.dialog_remove_button);
        AlertDialog dialog = builder.create();

        cancelButton.setOnClickListener(view -> dialog.dismiss());
        removeButton.setOnClickListener(view -> {
            if(selectedUser==null){
                Toast.makeText(this,"No user to delete",Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                return;
            }

            DatabaseManager databaseManager=new DatabaseManager();
            boolean result=databaseManager.deleteUser(selectedUser);
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
        dialogTitle.setText("Remove Profile Picture");
        dialogMessage.setText("Are you sure you want to remove this photo?");
        Button cancelButton = dialogView.findViewById(R.id.dialog_cancel_button);
        Button removeButton = dialogView.findViewById(R.id.dialog_remove_button);
        AlertDialog dialog = builder.create();

        cancelButton.setOnClickListener(view -> dialog.dismiss());
        removeButton.setOnClickListener(view -> {
            if(selectedUser==null){
                Toast.makeText(this,"No Profile Picture to delete",Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                return;
            }
            Bitmap generatedImage=generateProfileImage(selectedUser.getName());
            selectedUser.setProfilePicture(generatedImage);
            DatabaseManager databaseManager=new DatabaseManager();
            databaseManager.updateUser(selectedUser);
            dialog.dismiss();

        });
        dialog.show();
    }


}
