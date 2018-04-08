package com.sword.yukti.nearby;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.WebView;
import android.widget.RatingBar;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

public class DescriptionActivity extends AppCompatActivity {
String hospital,address,refer;
TextView nameEntry,addressEntry;
RatingBar ratingBar;
    WebView mWvPlaceDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description);
        mWvPlaceDetails = (WebView) findViewById(R.id.wv_place_details);

        mWvPlaceDetails.getSettings().setUseWideViewPort(false);

        nameEntry=(TextView)findViewById(R.id.Rest_name);
      //  addressEntry=(TextView)findViewById(R.id.hosp_add);
      //  ratingBar=(RatingBar)findViewById(R.id.ratings);
        hospital=getIntent().getStringExtra("place_name");
        address=getIntent().getStringExtra("place_address");
        refer=getIntent().getStringExtra("reference");
        Log.d(hospital,"hospital");
        Log.d(address,"address");
        Log.d(refer,"reference");
        nameEntry.setText(hospital);
       // addressEntry.setText(address);
        StringBuilder sb = new StringBuilder("https://maps.googleapis.com/maps/api/place/details/json?");
        sb.append("reference="+refer);
        sb.append("&sensor=true");
        sb.append("&key=AIzaSyDryhOjbKmDzniqZQ1iINRXZ_9HRPYL4CQ");

   /*    if (Float.parseFloat(String.valueOf(rating))>1&&Float.parseFloat(String.valueOf(rating))<2)
        {
            ratingBar.setNumStars(1);
        }
        else if (Float.parseFloat(String.valueOf(rating))>2&&Float.parseFloat(String.valueOf(rating))<3)
        {
            ratingBar.setNumStars(2);
        }
        else if (Float.parseFloat(String.valueOf(rating))>3&&Float.parseFloat(String.valueOf(rating))<4)
        {
            ratingBar.setNumStars(3);
        }
        else if (Float.parseFloat(String.valueOf(rating))>4&& Float.parseFloat(String.valueOf(rating))<5)
        {
            ratingBar.setNumStars(4);
        }
        else
            {
                ratingBar.setNumStars(5);
            }

*/
        PlacesTask placesTask = new PlacesTask();

        // Invokes the "doInBackground()" method of the class PlaceTask
        placesTask.execute(sb.toString());
        Log.d("sb:",sb.toString());
    }
    private class PlacesTask extends AsyncTask<String, Integer, String> {

        String data = null;

        // Invoked by execute() method of this object
        @Override
        protected String doInBackground(String... url) {
            try{
                data = downloadUrl(url[0]);
            }catch(Exception e){
                Log.d("Background Task",e.toString());
            }
            return data;
        }

        // Executed after the complete execution of doInBackground() method
        @Override
        protected void onPostExecute(String result){
            ParserTask parserTask = new ParserTask();

            // Start parsing the Google place details in JSON format
            // Invokes the "doInBackground()" method of the class ParseTask
            parserTask.execute(result);
        }
    }
    @SuppressLint("LongLogTag")
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try{
            URL url = new URL(strUrl);


            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb  = new StringBuffer();

            String line = "";
            while( ( line = br.readLine())  != null){
                sb.append(line);
            }

            data = sb.toString();
            br.close();

        }catch(Exception e){
            Log.d("Exception while downloading url", e.toString());
        }finally{
            iStream.close();
            urlConnection.disconnect();
        }

        return data;
    }


    /** A class to parse the Google Place Details in JSON format */
    private class ParserTask extends AsyncTask<String, Integer, HashMap<String,String>>{

        JSONObject jObject;

        // Invoked by execute() method of this object
        @Override
        protected HashMap<String,String> doInBackground(String... jsonData) {

            HashMap<String, String> hPlaceDetails = null;
            PlaceDetailsJSONParser placeDetailsJsonParser = new PlaceDetailsJSONParser();

            try{
                jObject = new JSONObject(jsonData[0]);

                // Start parsing Google place details in JSON format
                hPlaceDetails = placeDetailsJsonParser.parse(jObject);

            }catch(Exception e){
                Log.d("Exception",e.toString());
            }
            Log.d(String.valueOf(hPlaceDetails),"place_detail");
            return hPlaceDetails;
        }

        // Executed after the complete execution of doInBackground() method
        @Override
        protected void onPostExecute(HashMap<String,String> hPlaceDetails){


         //   String name = hPlaceDetails.get("name");
            String icon = hPlaceDetails.get("icon");
           // String vicinity = hPlaceDetails.get("vicinity");
            String lat = hPlaceDetails.get("lat");
            String lng = hPlaceDetails.get("lng");
            String formatted_address = hPlaceDetails.get("formatted_address");
            String formatted_phone = hPlaceDetails.get("formatted_phone");
            String website = hPlaceDetails.get("website");
            String rating = hPlaceDetails.get("rating");
            String international_phone_number = hPlaceDetails.get("international_phone_number");
            String url = hPlaceDetails.get("url");


            String mimeType = "text/html";
            String encoding = "utf-8";

            String data = 	"<html>"+
                    "<br style='clear:both' />" +
                    "<hr  />"+
                    "<p>Vicinity : " + address + "</p>" +
                    "<p>Location : " + lat + "," + lng + "</p>" +
                    "<p>Address : " + formatted_address + "</p>" +
                    "<p>Phone : " + formatted_phone + "</p>" +
                    "<p>Rating : " + rating + "</p>" +
                    "<p>International Phone  : " + international_phone_number + "</p>" +
                    "<p>Website :<a href='" + website+ "'>" + website + "</p>" +

                   // "<p>URL  : <a href='" + url + "'>" + url + "</p>" +
                    "</body></html>";

            // Setting the data in WebView
            mWvPlaceDetails.loadDataWithBaseURL("", data, mimeType, encoding, "");
        }
    }

}

