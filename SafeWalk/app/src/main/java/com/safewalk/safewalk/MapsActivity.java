package com.safewalk.safewalk;

//import android.app.Activity;
//import android.app.Fragment;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;

import im.delight.android.location.SimpleLocation;


public class MapsActivity extends FragmentActivity /*implements OnMapReadyCallback */{

    //private GoogleMap mMap;

    private SimpleLocation location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("uniquewords");
        super.onCreate(savedInstanceState);

        // ...

        // construct a new instance of SimpleLocation
        location = new SimpleLocation(this);

        // if we can't access the location yet
        if (!location.hasLocationEnabled()) {
            // ask the user to enable location access
            SimpleLocation.openSettings(this);
        }
        /*
        //findViewById(R.id.someView).setOnClickListener(new View.OnClickListener() {
        */
        //@Override
        //public void onClick(View v) {
        final double latitude = location.getLatitude();
        final double longitude = location.getLongitude();
        System.out.println("lat" + latitude);
        /*
        int PLACE_PICKER_REQUEST = 1;
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

        try {
            startActivityForResult(builder.build(this), PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }*/

        // TODO
        //    }

        //});
        int PLACE_PICKER_REQUEST = 1;

        try {
            PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
            startActivityForResult(builder.build(this), PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }


    /*@Override
    protected void onResume() {


        // make the device update its location
        location.beginUpdates();
        super.onResume();


        // ...
    }

    @Override
    protected void onPause() {
        // stop location updates (saves battery)
        location.endUpdates();

        // ...

        super.onPause();
    }*/

    //@Override

    /*public void onMapReady(GoogleMap googleMap) {

    }*/

    // TESTING
    @Override
    public void onStop() {
        System.out.println("we stopped here");
        super.onStop();
    }
    @Override
    public void onDestroy() {
        System.out.println("we destroyed here");
        super.onDestroy();
    }

}

