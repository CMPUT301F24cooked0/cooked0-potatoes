package com.example.myapplication;


import static java.security.AccessController.getContext;

import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;

public class AdministratorBrowseUsersFragment extends Fragment {
    ListView userList;
    ArrayList<User> userDataList;
    ArrayAdapter<User> userArrayAdapter;

    //private FirebaseFirestore  TODO database
    //private CollectionReference TODO database reference to users

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //TODO initialise database and user reference
       /*
        db=FirebaseFirestore.getInstance();
        userRef=db.collection("users");

        userListView=view.findViewById(R.id.administrator_browse_users_recyclerview);
        userDataList=new ArrayList<>();
        userListInit();
        */

        userArrayAdapter=new UserArrayAdapter(this,userDataList);
        userList.setAdapter(userArrayAdapter);

        userRef.addSnapshotListener(new EventListener<QuerySnapshot>(){
           @Override
           public void onEvent(@Nullable QuerySnapshot querySnapshots, @Nullable FirebaseFirestoreException error) {
               if (error != null) {
                   Log.e("Firestore", error.toString());
                   return;
               }
               if (querySnapshots != null) {
                   userDataList.clear();
                   for (QueryDocumentSnapshot doc : querySnapshots) {
                       String name = doc.getString("name");
                       String email = doc.getString("email");
                       Long phoneNumber = doc.getLong("phoneNumber");
                       User user = new User(name, email, phoneNumber);

                       userDataList.add(user);
                   }
                   userArrayAdapter.notifyDataSetChanged();
               }
           }
        });

    }
}
