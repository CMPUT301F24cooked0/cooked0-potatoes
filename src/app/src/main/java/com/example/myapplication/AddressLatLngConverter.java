package com.example.myapplication;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;
import java.util.Locale;

public class AddressLatLngConverter {
    /**
     * Converts an address String to a LatLng.
     * @param addressStr - String representing the address to convert
     * @param context - Context of the application
     * @return LatLng representing the location of the address, or null if the address could not be converted
     */
    public static LatLng AddressStrToLatLng(String addressStr, Context context)  {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocationName(addressStr, 1);
            if (addresses != null && !addresses.isEmpty()) {
                Address location = addresses.get(0);
                return new LatLng(location.getLatitude(), location.getLongitude());
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }
}

