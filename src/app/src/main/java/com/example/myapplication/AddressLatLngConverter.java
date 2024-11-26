package com.example.myapplication;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;

public class AddressLatLngConverter {
    /**
     * Converts an address String to a LatLng.
     * @param addressStr - String representing the address to convert
     * @return LatLng representing the location
     */
    public static LatLng AddressStrToLatLng(String addressStr, Context context) throws IOException {
        Geocoder geocoder = new Geocoder(context);
        List<Address> addresses = geocoder.getFromLocationName(addressStr, 1);
        if (addresses != null && !addresses.isEmpty()) {
            Address location = addresses.get(0);
            return new LatLng(location.getLatitude(), location.getLongitude());
        } else {
            return null;
        }
    }
}

