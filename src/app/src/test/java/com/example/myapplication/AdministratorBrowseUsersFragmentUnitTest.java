package com.example.myapplication;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.test.core.app.ApplicationProvider;

import org.junit.AssumptionViolatedException;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class AdministratorBrowseUsersFragmentUnitTest {
    private AdministratorBrowseUsersFragment fragment;
    private ListView listView;

    @Before
    public void setUp(){
        Context context = ApplicationProvider.getApplicationContext();
        fragment=new AdministratorBrowseUsersFragment();
        ArrayList<User> userList = new ArrayList<>();
        fragment.userDataList= userList;
        fragment.userArrayAdapter=new ArrayAdapter<>(context, R.layout.user_profile_summary,fragment.userDataList);
        listView=new ListView(context);
        fragment.userList=listView;
    }

    @Test
    public void testDeleteSuccess() throws Exception{
        User testUser=new User("name","name@email.com", 1234567890L);
        fragment.userDataList.add(testUser);
        fragment.userArrayAdapter.notifyDataSetChanged();
        testUser.deleteUser(unused -> {
            fragment.userDataList.remove(testUser);
            fragment.userArrayAdapter.notifyDataSetChanged();
            assertEquals(0, fragment.userDataList.size());
        },
                e -> {
            throw new AssertionError("Failed to delete user");
        });
        assertEquals(0,fragment.userDataList.size());

    }

    @Test
    public void testDeleteFailure() throws Exception{
        User testUser=new User("name","name@email.com", 1234567890L);
        fragment.userDataList.add(testUser);
        fragment.userArrayAdapter.notifyDataSetChanged();

        testUser.deleteUser(unused -> {
                    throw new AssertionError("Unexpected Deletion");
                },
                e -> {
                    assertEquals(1,fragment.userDataList.size());
                });
    }

}
