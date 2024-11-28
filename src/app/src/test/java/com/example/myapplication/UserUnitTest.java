package com.example.myapplication;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import android.graphics.Bitmap;

import com.google.android.gms.maps.model.LatLng;

import org.junit.Test;

public class UserUnitTest {
    @Test
    public void firstConstructorTest() throws Exception {
        User user = new UserMock(null, "name", "email@email.ca", (Bitmap) null);
    }

    @Test
    public void secondConstructorTest() throws Exception {
        User user = new UserMock(null, "name", "email@email.ca", 7801234567L, (Bitmap) null);
    }

    @Test
    public void setGetNameTest() throws Exception {
        User user = new UserMock(null, "name", "email@email.ca", (Bitmap) null);
        String newName = "newName";
        assertNotEquals(user.getName(), newName);
        user.setName(newName);
        assertEquals(user.getName(), newName);
        // TODO finish statement coverage
    }

    @Test
    public void setGetEmailTest() throws Exception {
        User user = new UserMock(null, "name", "email@email.ca", (Bitmap) null);
        String newEmail = "newEmail@email.ca";
        assertNotEquals(user.getEmail(), newEmail);
        user.setEmail(newEmail);
        assertEquals(user.getEmail(), newEmail);
        // TODO finish statement coverage
    }

    @Test
    public void setGetPhoneNumber() throws Exception {
        User user = new UserMock(null, "name", "email@email.ca", (Bitmap) null);
        Long phoneNumber = 7801234567L;
        assertNotEquals(user.getPhoneNumber(), phoneNumber);
        user.setPhoneNumber(phoneNumber);
        assertEquals(user.getPhoneNumber(), phoneNumber);
        // TODO finish statement coverage
    }

    @Test
    public void deleteProfilePictureTest() throws Exception {
        User user = new UserMock(null, "name", "email@email.ca", (Bitmap) null);
        assertNull(user.getProfilePicture());
        user.deleteProfilePicture();
        assertNull(user.getProfilePicture());
    }

    @Test
    public void setGetFacilityTest() throws Exception {
        User user = new UserMock(null, "name", "email@email.ca", (Bitmap) null);
        assertNull(user.getFacility());
        LatLng location = new LatLng(42.69, 69.42);
        String address = "address";
        Facility facility = new Facility("name", location, address);
        user.setFacility(facility);
        assertEquals(user.getFacility(), facility);
    }

    @Test
    public void deleteFacilityTest() throws Exception {
        User user = new UserMock(null, "name", "email@email.ca", (Bitmap) null);
        assertNull(user.getFacility());
        LatLng location = new LatLng(42.69, 69.42);
        String address = "address";
        Facility facility = new Facility("name", location, address);
        user.setFacility(facility);
        assertEquals(user.getFacility(), facility);
        user.deleteFacility();
        assertNull(user.getFacility());
    }

    @Test
    public void setGetReceivesOrgAdmNotificationsTest() throws Exception {
        User user = new UserMock(null, "name", "email@email.ca", (Bitmap) null);
        assertTrue(user.getReceivesOrgAdmNotifications());
        user.setReceivesOrgAdmNotifications(false);
        assertFalse(user.getReceivesOrgAdmNotifications());
    }

    @Test
    public void defaultIsAdminTest() throws Exception {
        User user = new UserMock(null, "name", "email@email.ca", (Bitmap) null);
        assertFalse(user.isAdmin());
    }

    @Test
    public void isOrganizerTest() throws Exception {
        User user = new UserMock(null, "name", "email@email.ca", (Bitmap) null);
        assertFalse(user.isOrganizer());
        LatLng location = new LatLng(42.69, 69.42);
        String address = "address";
        Facility facility = new Facility("name", location, address);
        user.setFacility(facility);
        assertTrue(user.isOrganizer());
    }
}
