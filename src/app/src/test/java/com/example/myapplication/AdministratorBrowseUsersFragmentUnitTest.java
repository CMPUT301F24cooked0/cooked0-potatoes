package com.example.myapplication;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.test.core.app.ApplicationProvider;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class AdministratorBrowseUsersFragmentUnitTest {
    private AdministratorBrowseUsersFragment fragment;
    private ListView listView;
    private Context context;
    private List<User> userList;

    @Test
    public void testDeleteSuccess(){
        context = ApplicationProvider.getApplicationContext();
        fragment=new AdministratorBrowseUsersFragment();
        fragment.userDataList=new ArrayList<>();
        fragment.userArrayAdapter=new ArrayAdapter<>(context, android.R.layout.simple_list_item_1,fragment.userDataList);
        listView=new ListView(context);
        fragment.userList=listView;

        User testUser=new User("name","name@email.com", 1234567890L);
        fragment.userDataList.add(testUser);
        fragment.userArrayAdapter.notifyDataSetChanged();
        fragment.deleteUser(testUser,0);
        assertEquals(0,fragment.userDataList.size());

    }

    @Test
    public void testDeleteFailure(){
        context = ApplicationProvider.getApplicationContext();
        fragment=new AdministratorBrowseUsersFragment();
        fragment.userDataList=new ArrayList<>();
        fragment.userArrayAdapter=new ArrayAdapter<>(context, android.R.layout.simple_list_item_1,fragment.userDataList);
        listView=new ListView(context);
        fragment.userList=listView;

        User testUser=new User("name","name@email.com", 1234567890L);
        fragment.userDataList.add(testUser);
        fragment.userArrayAdapter.notifyDataSetChanged();

        fragment.deleteUser(testUser,0);
        assertEquals(1,fragment.userDataList.size());
    }

    @Test
    public void testDeleteDialog(){
        context = ApplicationProvider.getApplicationContext();
        fragment=new AdministratorBrowseUsersFragment();
        fragment.userDataList=new ArrayList<>();
        fragment.userArrayAdapter=new ArrayAdapter<>(context, android.R.layout.simple_list_item_1,fragment.userDataList);
        listView=new ListView(context);
        fragment.userList=listView;

        User testUser=new User("name","name@email.com", 1234567890L);
        fragment.userDataList.add(testUser);
        fragment.userArrayAdapter.notifyDataSetChanged();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                fragment.showDeletePage(position);
            }
        });
        listView.performItemClick(listView.getChildAt(0),0,listView.getItemIdAtPosition(0));
    }
}
