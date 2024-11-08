package com.example.myapplication;



import android.app.AlertDialog;
import android.content.DialogInterface;
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

    /***
     * Method to show the dialog page to confirm or cancel delete action.
     * @param position
     */
    public void showDeletePage(final int position) {
        LayoutInflater inflater= LayoutInflater.from(requireContext());
        View dialogView = inflater.inflate(R.layout.delete_user_dialog, null);
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
            user.deleteUser(unused -> {
                userDataList.remove(position);
                userArrayAdapter.notifyDataSetChanged();
                dialog.dismiss();
                Log.d("DeleteUser","Successfully removed user");},
                    e -> {
                Log.w("DeleteUser","Failed to delete user");
            });
            dialog.dismiss();
        });
        dialog.show();
    }


}

