package com.example.myapplication;



import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;


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

        User.fetchUsers(users -> {
                    userDataList.clear();
                    userDataList.addAll(users);
                    userArrayAdapter.notifyDataSetChanged();
                },
                error->Log.w("Firestore","Error Fetching Users",error)
        );

        userList.setOnItemClickListener(((adapterView, view1, position, id) -> showDeletePage(position)));
        return view;
    }


    private void showDeletePage(final int position) {
        LayoutInflater inflater= LayoutInflater.from(requireContext());
        View dialogView = inflater.inflate(R.layout.delete_dialog, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        builder.setView(dialogView);
        TextView dialogTitle = dialogView.findViewById(R.id.dialog_title);
        dialogTitle.setText("Remove User");
        Button cancelButton = dialogView.findViewById(R.id.dialog_cancel_button);
        Button removeButton = dialogView.findViewById(R.id.dialog_remove_button);
        AlertDialog dialog = builder.create();

        cancelButton.setOnClickListener(view -> dialog.dismiss());
        removeButton.setOnClickListener(view -> {
            User user = userDataList.get(position);
            User.deleteUser(user,position);
            dialog.dismiss();
        });
        dialog.show();
    }


}

