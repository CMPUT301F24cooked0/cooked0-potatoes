package com.example.myapplication;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Text;

public class AdministratorUserProfilePage extends AppCompatActivity {
    private User selectedUser;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile_summary);

        Intent intent=getIntent();
        selectedUser=(User) intent.getSerializableExtra("user");

        if(selectedUser==null){
            Toast.makeText(this,"User Data Unavailable",Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        ImageView profilePicture=findViewById(R.id.user_profile_image);
        TextView profileName=findViewById(R.id.user_profile_name);
        TextView profileEmail=findViewById(R.id.user_profile_email);
        TextView profileNumber=findViewById(R.id.user_profile_phonenumber);
        Button removeButton=findViewById(R.id.remove_user_button);
        Button returnButton=findViewById(R.id.return_user_button);

        profileName.setText(selectedUser.getName());
        profileEmail.setText(selectedUser.getEmail());
        profileNumber.setText(String.valueOf(selectedUser.getPhoneNumber()));
        //TODO profile image
    }
}


/***
 * Method to show the dialog page to confirm or cancel delete action.
 * @param position
 */
public void showDeletePage(final int position) {
    LayoutInflater inflater= LayoutInflater.from(requireContext());
    View dialogView = inflater.inflate(R.layout.delete_dialog,null);
    AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
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
        User user = userDataList.get(position);
        user.deleteUser(unused -> {
                    userDataList.remove(position);
                    userArrayAdapter.notifyDataSetChanged();
                    dialog.dismiss();
                    Log.d("DeleteUser","Successfully removed user");
                    Toast.makeText(requireContext(),"Succesfully removed user",Toast.LENGTH_SHORT).show();},
                e -> {
                    Log.w("DeleteUser","Failed to delete user");
                    Toast.makeText(requireContext(),"Failed to delete user",Toast.LENGTH_SHORT).show();
                });
        dialog.dismiss();
    });
    dialog.show();
}