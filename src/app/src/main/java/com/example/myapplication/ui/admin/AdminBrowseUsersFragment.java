package com.example.myapplication.ui.admin;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.myapplication.DatabaseManager;
import com.example.myapplication.R;
import com.example.myapplication.User;
import com.example.myapplication.UserArrayAdapter;

import java.util.ArrayList;



public class AdminBrowseUsersFragment extends Fragment {
    ListView userList;
    ArrayList<User> userDataList;
    ArrayAdapter<User> userArrayAdapter;

    //@Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.admin_browse_users, container, false);

        userList.setAdapter(userArrayAdapter);
        DatabaseManager databaseManager=new DatabaseManager();
        Log.d("test","test");
        databaseManager.getAllUsers(users -> {
            Log.d("test","before test");
            if(users!=null && !users.isEmpty()){
                Log.d("test","after test");
                requireActivity().runOnUiThread(()->{
                    userDataList.clear();
                    userDataList.addAll(users);
                    userArrayAdapter.notifyDataSetChanged();
                    Log.d("Fetch Users","User List Updated:" + users.size()+"users");
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
            Intent intent=new Intent(requireContext(), AdminUserProfile.class);
            intent.putExtra("uniqueID",selectedUser.getUniqueID());
            startActivity(intent);
        });
        return view;
    }




}

