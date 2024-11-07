package com.example.myapplication;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import android.graphics.Bitmap;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.firestore.FirebaseFirestore;

import org.junit.Test;
import org.mockito.Mockito;

public class UserUnitTest {
    FirebaseFirestore db = Mockito.mock(FirebaseFirestore.class);

    @Test
    public void firstConstructorTest() throws Exception {
        User user = new User("name", "email@email.ca", db);
    }

    @Test
    public void secondConstructorTest() throws Exception {
        User user = new User("name", "email@email.ca", 7801234567L, db);
    }

    @Test
    public void thirdConstructorTest() throws Exception {
        User user = new User("name", "email@email.ca", (Bitmap) null, db);
    }

    @Test
    public void fourthConstructorTest() throws Exception {
        User user = new User("name", "email@email.ca", 7801234567L, (Bitmap) null, db);
    }

    @Test
    public void setGetNameTest() throws Exception {
        User user = new User("name", "email@email.ca", db);
        String newName = "newName";
        assertNotEquals(user.getName(), newName);
        user.setName(newName);
        assertEquals(user.getName(), newName);
        // TODO finish statement coverage
    }

    @Test
    public void setGetEmailTest() throws Exception {
        User user = new User("name", "email@email.ca", db);
        String newEmail = "newEmail@email.ca";
        assertNotEquals(user.getEmail(), newEmail);
        user.setEmail(newEmail);
        assertEquals(user.getEmail(), newEmail);
        // TODO finish statement coverage
    }

    @Test
    public void setGetPhoneNumber() throws Exception {
        User user = new User("name", "email@email.ca", db);
        Long phoneNumber = 7801234567L;
        assertNotEquals(user.getPhoneNumber(), phoneNumber);
        user.setPhoneNumber(phoneNumber);
        assertEquals(user.getPhoneNumber(), phoneNumber);
        // TODO finish statement coverage
    }

    @Test
    public void setGetProfilePicture() {
        // TODO test? not sure how since Bitmap can't be used in testing...
    }

    @Test
    public void deleteProfilePictureTest() throws Exception {
        User user = new User("name", "email@email.ca", db);
        assertNull(user.getProfilePicture());
        user.deleteProfilePicture();
        assertNull(user.getProfilePicture());
    }

    @Test
    public void setGetFacilityTest() throws Exception {
        User user = new User("name", "email@email.ca", db);
        assertNull(user.getFacility());
        LatLng location = new LatLng(42.69, 69.42);
        Facility facility = new Facility("name", location, user, db);
        user.setFacility(facility);
        assertEquals(user.getFacility(), facility);
    }

    @Test
    public void deleteFacilityTest() throws Exception {
        User user = new User("name", "email@email.ca", db);
        assertNull(user.getFacility());
        LatLng location = new LatLng(42.69, 69.42);
        Facility facility = new Facility("name", location, user, db);
        user.setFacility(facility);
        assertEquals(user.getFacility(), facility);
        user.deleteFacility();
        assertNull(user.getFacility());
    }

    @Test
    public void setGetReceivesOrgAdmNotificationsTest() throws Exception {
        User user = new User("name", "email@email.ca", db);
        assertTrue(user.getReceivesOrgAdmNotifications());
        user.setReceivesOrgAdmNotifications(false);
        assertFalse(user.getReceivesOrgAdmNotifications());
    }

    @Test
    public void defaultIsAdminTest() throws Exception {
        User user = new User("name", "email@email.ca", db);
        assertFalse(user.isAdmin());
    }

    @Test
    public void isOrganizerTest() throws Exception {
        User user = new User("name", "email@email.ca", db);
        assertFalse(user.isOrganizer());
        LatLng location = new LatLng(42.69, 69.42);
        Facility facility = new Facility("name", location, user, db);
        user.setFacility(facility);
        assertTrue(user.isOrganizer());
    }
}
