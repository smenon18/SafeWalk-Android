package com.safewalk.safewalk;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import im.delight.android.location.SimpleLocation;




public class MapsActivity extends FragmentActivity /*implements OnMapReadyCallback */ {
    private String s_lat = "0";
    private String s_long = "0";
    private String d_lat = "0";
    private String d_long = "0";
    private String m_time = "0";
    private String m_dist = "0";
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
                    Pattern pattern = Pattern.compile("([0-9]+\\.?[0-9]?) mi.*([0-9]+) mins");
                    Matcher matcher = pattern.matcher(ares);
                    if (matcher.find())
                    {
                        System.out.println("FLAG dist " + matcher.group(1) + "," + "time " + matcher.group(2));
                        m_dist = matcher.group(1);
                        m_time = matcher.group(2);
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
        protected void onPostExecute(Void res)
        {
            new SendTrip().execute();
        }
    }


    private String makeJSon(String email, String departure_time, String expected_arrival_time, String start_lat, String start_long, String end_lat, String end_long, String est_distances) {
        String jsonString = "";
        try {
            JSONObject data = new JSONObject();
            data.put("email", email);
            data.put("departure time", departure_time);
            data.put("expected_arrival_time", expected_arrival_time);
            data.put("start_pos", start_lat+","+start_long);
            data.put("end_pos", end_lat+","+end_long);
            data.put("est_distances", est_distances);
            jsonString = data.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonString;
    }

    class SendTrip extends AsyncTask<Void,Void,Void> {
        @Override
        protected Void doInBackground(Void... params) {
            try {
                final long ONE_MINUTE_IN_MILLIS=60000;//millisecs

                Calendar date = Calendar.getInstance();
                long t= date.getTimeInMillis();
                Date future=new Date(t + (Integer.parseInt(m_time) * ONE_MINUTE_IN_MILLIS));


                String message = makeJSon("abc@abc.com", (new SimpleDateFormat("dd/MM/yy HH:mm:ss")).toString(), future.toString(), s_lat, s_long, d_lat, d_long, m_dist);

                URL url = new URL("http://safewalk-web.herokuapp.com/api/notify_parent/");
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                urlConnection.setDoOutput(true);
                urlConnection.setRequestMethod("PUT");

                //OutputStream write = urlConnection.getOutputStream();

                OutputStreamWriter writer = new OutputStreamWriter(urlConnection.getOutputStream());
                writer.write(message);


                // InputStream reader = urlConnection.getInputStream();

                int response = urlConnection.getResponseCode();
                Log.w("RESPONSE-Notify", Integer.toString(response));

                if (response < 200 || response > 299) {
                    writer.write(message);
                    System.out.println("Response code" + response + " will try once more.");
                }
                else {System.out.println("WE DIDNT ERROR");}
                writer.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
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