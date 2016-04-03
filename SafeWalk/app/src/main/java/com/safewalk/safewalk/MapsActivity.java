package com.safewalk.safewalk;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import im.delight.android.location.SimpleLocation;

package com.example.felix.princetonsafewalk;



public class MapsActivity extends FragmentActivity /*implements OnMapReadyCallback */ {
    private String s_lat = "0";
    private String s_long = "0";
    private String d_lat = "0";
    private String d_long = "0";
    private String m_time = "0";
    //private GoogleMap mMap;

    private SimpleLocation location;

    int PLACE_PICKER_REQUEST = 1;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

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
        s_lat = Double.toString(latitude);
        s_long = Double.toString(longitude);


        try {
            PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
            startActivityForResult(builder.build(this), PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, this);
                String toastMsg = String.format("Place: %s", place.getLatLng());
                Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show();
                System.out.println(toastMsg);
                toastMsg = toastMsg.replaceAll(".*\\(", "");
                toastMsg = toastMsg.replaceAll("[() ]", "");
                String[] marr = toastMsg.trim().split(",");
                d_lat = marr[0];
                d_long = marr[1];//Double.parseDouble(
                System.out.println("DEST" + d_lat + "," + d_long);
                new MyDownloadTask().execute();
            }
        }

    }

    class MyDownloadTask extends AsyncTask<Void,Void,Void> {
        @Override
        protected Void doInBackground(Void... params) {
            System.out.println("we stopped here");
            System.out.println("lat,long,start check" + s_lat + "," + s_long);


            StringBuilder urlString = new StringBuilder();
            urlString.append("https://maps.googleapis.com/maps/api/distancematrix/json?");
            urlString.append("&units=").append("imperial");
            urlString.append("&mode=").append("walking");
            urlString.append("&origins=").append(s_lat + "," + s_long);
            urlString.append("&destinations=").append(d_lat + "," + d_long);
            urlString.append("&key=").append("AIzaSyAzjrtTZCi2fW0XhVthMmSgQ_vUezAzb2A");
            HttpURLConnection con = null;
            URL url = null;
            JSONObject object = null;
            try {
                url = new URL(urlString.toString());
                System.out.println(url);

                con = (HttpURLConnection) url.openConnection();
                System.out.println("we stopped here 3");
                con.connect();
                System.out.println("we stopped here 4");
                int status = con.getResponseCode();
                System.out.println("we stopped here 5");

                if (status >= 200 && status < 300) {
                    BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));

                    String inputLine;
                    StringBuffer response = new StringBuffer();

                    while ((inputLine = br.readLine()) != null) {
                        response.append(inputLine);
                    }
                    br.close();
                    System.out.println("we stopped here 6");
                    //print result
                    String ares = response.toString();
                    System.out.println(ares);
                    Pattern pattern = Pattern.compile("([0-9]+) mins");
                    Matcher matcher = pattern.matcher(ares);
                    if (matcher.find())
                    {
                        System.out.println(matcher.group(0));
                        m_time = matcher.group(1);
                    }

                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("TEST of IOException error");
            }
            return null; //HISDFPSDPFHPSDP
        }
    }

    @Override
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
    }


    @Override
    public void onDestroy() {
        System.out.println("we destroyed here");
        super.onDestroy();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Maps Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.example.felix.princetonsafewalk/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Maps Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.example.felix.princetonsafewalk/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
}