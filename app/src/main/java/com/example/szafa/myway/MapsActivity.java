package com.example.szafa.myway;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Pair;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.maps.DirectionsApi;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.DistanceMatrixApi;
import com.google.maps.DistanceMatrixApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.model.DirectionsLeg;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsRoute;
import com.google.maps.model.DirectionsStep;
import com.google.maps.model.DistanceMatrix;
import com.google.maps.model.Duration;
import com.google.maps.model.EncodedPolyline;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationManager locationManager;
    private DrawerLayout mDrawerLayout;
    private LatLng ClientLocation = new LatLng(40,40);
    private ArrayList<Client> clientsToVisit;
    private Options options = new Options(TransportMean.Car, true, true);

    public static final int LOCATIONS_REQUEST = 2;
    public static final int OPTIONS_REQUEST = 3;
    public String[] RouteAddresses = new String[] {"Aleje Jerozolimskie", "Obozowa Warszawa", "Koszykowa Warszawa", "Stadion Narodowy"};
    public String StartAddress = "Miejski Ogrod zoologiczny warszawa"; // if StartAddress is null then StartAdress is the current client location
    public String EndAddress = null; // if EndAddress is null then EndAddress is the current client location

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        setNavBar();

        ClientsDbHelper db = ClientsDbHelper.initDbHelper(getApplicationContext());
        clientsToVisit = new ArrayList<>();
    }
    private void SwitchToRouteLocationsActivity()
    {
        Intent intent = new Intent(this, RouteLocationsActivity.class);
        intent.putExtra(RouteLocationsActivity.LIST_ID, clientsToVisit);
        startActivityForResult(intent, LOCATIONS_REQUEST);
    }
    private void SwitchToOptions()
    {
        Intent intent = new Intent(this, OptionsActivity.class);
        intent.putExtra(OptionsActivity.OPTIONS_ID, options);
        startActivityForResult(intent, OPTIONS_REQUEST);
    }
    private void a(String aa)
    {
        Toast.makeText(this, aa, Toast.LENGTH_LONG).show();
    }
    private void setNavBar()
    {

        mDrawerLayout = findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        // set item as selected to persist highlight
                        menuItem.setChecked(true);
                        // close drawer when item is tapped
                        mDrawerLayout.closeDrawers();

                        // Add code here to update the UI based on the item selected
                        // For example, swap UI fragments here

                        a(menuItem.getTitle().toString());
                        // MENU ITEMS CLICKS

                        if(menuItem.getTitle().toString().contains("Client list"))
                        {
                            SwitchToRouteLocationsActivity();
                        }
                        if(menuItem.getTitle().toString().contains("Options"))
                        {
                            SwitchToOptions();
                        }


                        return true;
                    }
                });
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
        setTitle("MyWay");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);

                return true;


        }
        Intent intent = new Intent(this, RouteLocationsActivity.class);
        startActivity(intent);
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
        while (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }

        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            ClientLocation = new LatLng(location.getLatitude(), location.getLongitude());
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 10));
                        }
                    }
                });

        mMap.setMyLocationEnabled(true);

    }

    public void onClickBtn(View v) //Set route button click
    {
        DisplayShortestRoute();

        //Intent intent = new Intent(this, RouteLocationsActivity.class);
        //startActivity(intent);
    }

    public void DisplayShortestRoute()
    {
        GeoApiContext context = new GeoApiContext.Builder().apiKey("").build();

        List<LatLng> path = new ArrayList();
        List<Pair<LatLng, String>> markers = new ArrayList<>();

        long JourneyTime = 0; //in sec
        int JourneyLength = 0; //in meters

        try {
            mFusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {
                                ClientLocation = new LatLng(location.getLatitude(), location.getLongitude());
                                mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(), location.getLongitude())));
                            }
                        }
                    });


            String start = Double.toString(ClientLocation.latitude) + ',' + Double.toString(ClientLocation.longitude);
            String end = start;

            if(StartAddress != null)
                start = StartAddress;
            if(EndAddress != null)
                end=EndAddress;

            DirectionsApiRequest req = DirectionsApi.getDirections(context, start, end);
            req.optimizeWaypoints(true);
            req.waypoints(RouteAddresses);

            DirectionsResult res = req.await();

            if (res.routes != null && res.routes.length > 0) {
                DirectionsRoute route = res.routes[0];

                if (route.legs !=null) {
                    markers.add(new Pair<>(new LatLng(route.legs[0].startLocation.lat, route.legs[0].startLocation.lng), route.legs[0].startAddress));

                    for(int i=0; i<route.legs.length; i++) {
                        DirectionsLeg leg = route.legs[i];
                        markers.add(new Pair<>(new LatLng(route.legs[i].endLocation.lat, route.legs[i].endLocation.lng), route.legs[i].endAddress));
                        JourneyTime += route.legs[i].duration.inSeconds;
                        JourneyLength += route.legs[i].distance.inMeters;

                        if (leg.steps != null) {
                            for (int j=0; j<leg.steps.length;j++){
                                DirectionsStep step = leg.steps[j];
                                if (step.steps != null && step.steps.length >0) {
                                    for (int k=0; k<step.steps.length;k++){
                                        DirectionsStep step1 = step.steps[k];
                                        EncodedPolyline points1 = step1.polyline;
                                        if (points1 != null) {
                                            //Decode polyline and add points to list of route coordinates
                                            List<com.google.maps.model.LatLng> coords1 = points1.decodePath();
                                            for (com.google.maps.model.LatLng coord1 : coords1) {
                                                path.add(new LatLng(coord1.lat, coord1.lng));
                                            }
                                        }
                                    }
                                } else {
                                    EncodedPolyline points = step.polyline;
                                    if (points != null) {
                                        //Decode polyline and add points to list of route coordinates
                                        List<com.google.maps.model.LatLng> coords = points.decodePath();
                                        for (com.google.maps.model.LatLng coord : coords) {
                                            path.add(new LatLng(coord.lat, coord.lng));
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch(Exception ex) {
            Toast.makeText(this, ex.toString(), Toast.LENGTH_LONG).show();
        }
        if (path.size() > 0) {
            if(EndAddress==null && StartAddress==null) markers.remove(markers.size()-1);
            for (int i=0;i<markers.size();i++) {
                LatLng l = markers.get(i).first;
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(l);
                markerOptions.title("Pozycja w trasie : " +  Integer.toString(i));
                markerOptions.snippet(markers.get(i).second);

                mMap.addMarker(markerOptions);
            }
            PolylineOptions opts = new PolylineOptions().addAll(path).color(Color.BLUE).width(5);
            mMap.addPolyline(opts);

            //Toast for journey time and length
            Toast.makeText(this,"Estimated time : " + FormatTimeFromSeconds(JourneyTime) + "\nDistance : " + FormatDistanceFromMeters(JourneyLength) , Toast.LENGTH_LONG).show();
        }
    }

    public void onClientListClick(View view){
        Intent intent = new Intent(this, ClientListActivity.class);
        startActivity(intent);
    }
    private static String FormatTimeFromSeconds(long longVal)
    {
        int hours = (int) longVal / 3600;
        int remainder = (int) longVal - hours * 3600;
        int mins = remainder / 60;
        remainder = remainder - mins * 60;
        int secs = remainder;
        return  String.format("%02d:%02d:%02d", hours, mins, secs);
    }
    private static String FormatDistanceFromMeters(int meters)
    {
        int km = meters/1000;
        meters = meters - km*1000;
        return km+","+meters + " km";
    }
    private final LocationListener locationListener = new LocationListener() {
        public void onLocationChanged(Location location) {
            ClientLocation = new LatLng(location.getLatitude(), location.getLongitude());
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };

    @Override
    protected void onDestroy() {
        ClientsDbHelper.getDbHelper().close();
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == LOCATIONS_REQUEST) {
            if(resultCode == RESULT_OK){
                clientsToVisit = (ArrayList<Client>) data.getSerializableExtra(RouteLocationsActivity.LIST_ID);
            }
        }
        else if(requestCode == OPTIONS_REQUEST) {
            if(resultCode == RESULT_OK){
                options = (Options) data.getSerializableExtra(OptionsActivity.OPTIONS_ID);
            }
        }
    }
}
