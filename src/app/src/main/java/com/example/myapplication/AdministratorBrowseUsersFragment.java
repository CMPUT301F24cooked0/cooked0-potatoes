package com.example.myapplication;



import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.icu.text.Transliterator;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;


public class AdministratorBrowseUsersFragment extends Fragment {
    ListView userList;
    ArrayList<User> userDataList;
    ArrayAdapter<User> userArrayAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.administrator_browse_users, container, false);

        userList = view.findViewById(R.id.administrator_browse_users_recyclerview);
        userDataList = new ArrayList<>();
        userArrayAdapter = new UserArrayAdapter(requireContext(), userDataList);
        userList.setAdapter(userArrayAdapter);
        DatabaseManager databaseManager=new DatabaseManager();
        databaseManager.getAllUsers(users -> {
            if(users!=null && !users.isEmpty()){
                requireActivity().runOnUiThread(()->{
                    userDataList.clear();
                    userDataList.addAll(users);
                    userArrayAdapter.notifyDataSetChanged();
                    Log.d("Fetch Users","Success");
                });
            }
            else{
                requireActivity().runOnUiThread(()->{
                    Log.w("Fetch Users", "No Users Found");
                    Toast.makeText(requireContext(),"No Users Found",Toast.LENGTH_SHORT).show();
                });
            }
        });
        userList.setOnItemClickListener((adapterView, view1, position, id) -> {
            User selectedUser=userDataList.get(position);
        });
        return view;
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


}

