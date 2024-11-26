package com.example.myapplication;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;

import com.google.android.gms.maps.model.LatLng;

import org.junit.Test;

public class AddressLatLngConverterTest {
    @Test
    public void testCompareConversion() {
        String addressStr = "725 5th Ave, New York, NY 10022, United States";
        LatLng correctLatLng = new LatLng(40.7627197, -73.97391309999999);
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        LatLng convertedAddress = AddressLatLngConverter.AddressStrToLatLng(addressStr, context);
        assertEquals(correctLatLng.latitude, convertedAddress.latitude, 0.0001);
        assertEquals(correctLatLng.longitude, convertedAddress.longitude, 0.0001);

    }
    @Test
    public void testNullConversion(){
        String addressStr = "this address does not exist";
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        LatLng convertedAddress = AddressLatLngConverter.AddressStrToLatLng(addressStr, context);
        assertEquals(convertedAddress, null);
    }
}
