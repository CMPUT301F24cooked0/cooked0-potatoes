package com.example.myapplication;


import static java.security.AccessController.getContext;

import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

public class AdministratorBrowseUsersFragment extends Fragment {
    private ListView userListView;
    private ArrayList<User> userDataList;
    private ArrayAdapter<User> userArrayAdapter;
    //private FirebaseFirestore  TODO database
    //private CollectionReference TODO database reference to users

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view= inflater.inflate(R.layout.administrator_browse_users,container, false);

        //TODO initialise database and user reference

        userListView=view.findViewById(R.id.administrator_browse_users_recyclerview);
        userDataList=new ArrayList<>();

        initialiseUserList();

        userArrayAdapter=new UserArrayAdapter(getContext(), userDataList);
        userListView.setAdapter(userArrayAdapter);



    }
}
