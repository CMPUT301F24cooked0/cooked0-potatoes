package com.example.myapplication.ui.profile;

import androidx.lifecycle.ViewModel;

import com.example.myapplication.User;


public class ProfileViewModel extends ViewModel {

    private User user;


    public ProfileViewModel() {
        user = null;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}