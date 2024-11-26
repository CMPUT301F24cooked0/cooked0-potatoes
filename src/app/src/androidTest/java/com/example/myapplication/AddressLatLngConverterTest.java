package com.example.myapplication;

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
        assert convertedAddress != null;
        assert convertedAddress.latitude == correctLatLng.latitude;
        assert convertedAddress.longitude == correctLatLng.longitude;

    }
    @Test
    public void testNullConversion(){
        String addressStr = "this address does not exist";
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        LatLng convertedAddress = AddressLatLngConverter.AddressStrToLatLng(addressStr, context);
        assert convertedAddress == null;
    }
}
