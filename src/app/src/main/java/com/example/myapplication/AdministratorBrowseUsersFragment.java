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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class AdministratorBrowseUsersFragment extends Fragment {
    ListView userList;
    ArrayList<User> userDataList;
    ArrayAdapter<User> userArrayAdapter;

    private FirebaseFirestore db;
    private CollectionReference userRef;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.administrator_browse_users, container, false);
        db = FirebaseFirestore.getInstance();
        userRef = db.collection("Users");

        userList = view.findViewById(R.id.administrator_browse_users_recyclerview);
        userDataList = new ArrayList<>();
        userArrayAdapter = new UserArrayAdapter(requireContext(), userDataList);
        userList.setAdapter(userArrayAdapter);

        userRef.addSnapshotListener((querySnapshots, error) -> {
            userDataList.clear();
            for (QueryDocumentSnapshot doc : querySnapshots) {
                String name = doc.getString("name");
                String email = doc.getString("email");
                Long phoneNumber -doc.getLong("phoneNumber");
                User user = new User(Name, email, phoneNumber);
                userDataList.add(user);
            }
            userArrayAdapter.notifyDataSetChanged();
        });

        userList.setOnItemClickListener(((adapterView, view1, position, id) -> showDeletePage(position)));
        return view;
    }


    private void showDeletePage(LayoutInflater inflater, final int position) {
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
            deleteUser(user, position);
            dialog.dismiss();
        });
        dialog.show();
    }


    private void deleteUser(final User user, final int position) {
        userRef.document(user.getName()).delete().addOnSuccessListener(aVoid -> {
            Log.d("Firestore", "User Deleted");
            userDataList.remove(position);
            userArrayAdapter.notifyDataSetChanged();
        }).addOnFailureListener(e -> Log.w("Firestore", "Error Removing User"));
    }
}

