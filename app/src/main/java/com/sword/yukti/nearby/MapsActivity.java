package com.sword.yukti.nearby;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener,LocationListener,View.OnClickListener
{
    private GoogleMap mMap;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    LocationRequest mLocationRequest;
    double mLatitude=0;
    double mLongitude=0;
    Marker marker;
    ArrayList<Getter_setter>mapsList;
    HashMap<String, String> mMarkerPlaceLink = new HashMap<String, String>();
    Button button,button1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission();
        }
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
         button=(Button)findViewById(R.id.search_button);
         button1=(Button)findViewById(R.id.list_map);
         button.setOnClickListener(this);
        button1.setOnClickListener(this);
    }
   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.tools, menu);
        return true;
    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search_button:
                Toast.makeText(this, "Nearby Restaurant", Toast.LENGTH_SHORT).show();
                mLatitude = mLastLocation.getLatitude();
                mLongitude = mLastLocation.getLongitude();
                LatLng latLng = new LatLng(mLatitude, mLongitude);
                marker = mMap.addMarker(new MarkerOptions().position(latLng).title("My location").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(12));
                StringBuilder sb = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
                sb.append("location=" + mLatitude + "," + mLongitude);
                sb.append("&radius=3000");
                sb.append("&types=" +"restaurant");
                sb.append("&sensor=true");
                sb.append("&key=AIzaSyBFjK8UInAeNGfhx8attCH8UNY6xzNjuwU");
                PlacesTask placesTask = new PlacesTask();
                placesTask.execute(sb.toString());
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

        //Initialize Google Play Services
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
            }
        } else {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onConnected(Bundle bundle) {

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {

        mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }

        //Place current location marker
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Position");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
        mCurrLocationMarker = mMap.addMarker(markerOptions);

        //move map camera
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(11));

        //stop location updates
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient,MapsActivity.this);
        }

    }


    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Asking user if explanation is needed
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                //Prompt the user once explanation has been shown
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted. Do the
                    // contacts-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            android.Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }

                } else {

                    // Permission denied, Disable the functionality that depends on this permission.
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }

            // other 'case' lines to check for other permissions this app might request.
            // You can add here other case statements according to your requirement.
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.search_button:
                Toast.makeText(this, "Nearby hospital", Toast.LENGTH_SHORT).show();
                if(mLastLocation!= null) {
                    mLatitude = mLastLocation.getLatitude();
                    mLongitude = mLastLocation.getLongitude();

                    LatLng latLng = new LatLng(mLatitude, mLongitude);
                    marker = mMap.addMarker(new MarkerOptions().position(latLng).title("My location").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                    mMap.animateCamera(CameraUpdateFactory.zoomTo(12));
                    StringBuilder sb = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
                    sb.append("location=" + mLatitude + "," + mLongitude);
                    sb.append("&radius=4000");
                    sb.append("&types=" + "hospital");
                    sb.append("&sensor=true");
                    sb.append("&key=AIzaSyBFjK8UInAeNGfhx8attCH8UNY6xzNjuwU");
                    Log.d("sb:", sb.toString());
                    PlacesTask placesTask = new PlacesTask();
                    placesTask.execute(sb.toString());
                    button1.setVisibility(View.VISIBLE);
                }else
                {
                    Toast.makeText(this, "last location not found!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(intent);
                }



                break;
            case R.id.list_map:
                Intent intent=new Intent(MapsActivity.this,ListActivity.class);
                intent.putExtra("Maps_list",mapsList);

                startActivity(intent);
                break;

        }
    }

    private class PlacesTask extends AsyncTask<String, Integer, String>
    {
        String data = null;
        @Override
        protected String doInBackground(String... url)
        {
            try
            {
                data = downloadUrl(url[0]);
                Log.d("data:",data);
            }
            catch (Exception e)
            {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result)
        {
            ParserTask parserTask = new ParserTask();
            parserTask.execute(result);
            Log.d("result",result);
        }

    }
    private class ParserTask extends AsyncTask<String, Integer, List<HashMap<String, String>>>
    {
        JSONObject jObject;
        @Override
        protected List<HashMap<String, String>> doInBackground(String... jsonData)
        {
            List<HashMap<String, String>> places = null;
            PlaceJsonparser placeJsonParser = new PlaceJsonparser();
            try
            {
                jObject = new JSONObject(jsonData[0]);
                places = placeJsonParser.parse(jObject);
            }
            catch (Exception e)
            {
                Log.d("Exception", e.toString());
            }
            return places;
        }
        @Override
        protected void onPostExecute(List<HashMap<String, String>> list)
        {


            mMap.clear();
            mapsList=new ArrayList<Getter_setter>();
            mapsList.clear();


            for (int i = 0; i < list.size(); i++)

            {   Getter_setter getter_setter=new Getter_setter();
                MarkerOptions markerOptions = new MarkerOptions();
                HashMap<String, String> hmPlace = list.get(i);
                double lat = Double.parseDouble(hmPlace.get("lat"));
                double lng = Double.parseDouble(hmPlace.get("lng"));
                String name = hmPlace.get("place_name");
                String vicinity = hmPlace.get("vicinity");
                String rating=hmPlace.get("rating");
                String refernce=hmPlace.get("reference");
                Log.d(rating,"rating");
                LatLng latLng = new LatLng(lat, lng);
                markerOptions.position(latLng);
                markerOptions.title(name + " : " + vicinity);
                getter_setter.setPlaceName(name);
                getter_setter.setVicinity(vicinity);
                getter_setter.setRating(rating);
                getter_setter.setReference(refernce);
                mapsList.add(getter_setter);
                Marker m = mMap.addMarker(markerOptions);
                Log.d(hmPlace.get("reference"),"reference");
                mMarkerPlaceLink.put(m.getId(), hmPlace.get("reference"));
            }

        }
    }    @SuppressLint("LongLogTag")
    private String downloadUrl(String strUrl) throws IOException
    {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try
        {
            URL url = new URL(strUrl);

            urlConnection = (HttpURLConnection) url.openConnection();


            urlConnection.connect();


            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null)
            {
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        }
        catch (Exception e)
        {
            Log.d("Exception while downloading url", e.toString());
        } finally
        {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }



}