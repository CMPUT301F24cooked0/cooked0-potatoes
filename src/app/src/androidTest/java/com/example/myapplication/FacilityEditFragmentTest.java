package com.example.myapplication;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.testing.FragmentScenario;
import androidx.lifecycle.Lifecycle;
import androidx.test.ext.junit.runners.AndroidJUnit4;


import com.google.android.gms.maps.model.LatLng;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

@RunWith(AndroidJUnit4.class)
public class FacilityEditFragmentTest {
    private FragmentScenario<FacilityEditFragment> scenario;

    @Before
    public void setup() {
        scenario = FragmentScenario.launchInContainer(FacilityEditFragment.class);
        scenario.moveToState(Lifecycle.State.CREATED);
    }
    @Test
    public void testEditFacility() {
        scenario.onFragment(fragment -> {
            fragment.existingFacility = new Facility("Old Name", new LatLng(0, 0));
            fragment.databaseManager = mock(DatabaseManager.class);
            onView(withId(R.id.editFacilityName)).perform(typeText("New Name"));
            onView(withId(R.id.editFacilityAddress)).perform(typeText("8900 114 St NW, Edmonton, AB T6G 2S4"));
            onView(withId(R.id.editFacilityButton)).perform(click());
            Mockito.verify(fragment.databaseManager).updateFacility(Mockito.any(Facility.class));
            assertEquals("New Name", fragment.existingFacility.getName());
        });

    }

}