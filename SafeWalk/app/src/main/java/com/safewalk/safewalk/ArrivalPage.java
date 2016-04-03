package com.safewalk.safewalk;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class ArrivalPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arrival_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    public void SwitchFinalPage(View view){
        Intent intent = new Intent (ArrivalPage.this, FinalPage.class);
        startActivity(intent);
    }

    private String makeJSon(String email) {
        String jsonString = "";
        try {
            JSONObject data = new JSONObject();
            data.put("email", email);
            data.put("arrived", true);
            jsonString = data.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonString;
    }

    //I HAVE TWO SWITCHFINALPAGES HERE, ONE WITH DATA SENDING AND ONE WITHOUT. IT DOESN"T WORK NOW
    //SO I JUST GOT RIG OF IT.
    /*
    public void SwitchFinalPage(View view){


        try {

            String message = makeJSon("abc@abc.com");
            Log.w("JSON Object", message);

            URL url = new URL("http://safewalk-web.herokuapp.com/api/on_arrival/");
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setDoOutput(true);
            urlConnection.setRequestMethod("PUT");

            OutputStreamWriter writer = new OutputStreamWriter(urlConnection.getOutputStream());
            writer.write(message);
            writer.close();

            int response = urlConnection.getResponseCode();
            Log.w("RESPONSE-LOGIN", Integer.toString(response));


        } catch (IOException e) {
            e.printStackTrace();
        }


    }
    */
}
