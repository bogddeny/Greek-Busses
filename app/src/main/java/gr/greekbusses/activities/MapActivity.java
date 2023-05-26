package gr.greekbusses.activities;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import gr.greekbusses.R;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback
{
    private static final int REQUEST_LOCATION = 1;

    LocationManager locationManager;

    MapView busMapView;

    LatLng currentLatLng;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        initializeViewObjects();

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
        {
            enableLocationServices();
        }
        else
        {
            getCurrentUserLocation();
        }

        busMapView.onCreate(savedInstanceState);
        busMapView.getMapAsync(this);
    }
    public void initializeViewObjects()
    {
        busMapView = findViewById(R.id.busMapView);
    }
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap)
    {
        googleMap.addMarker(new MarkerOptions().position(currentLatLng).title("Marker"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 16F));
    }
    @Override
    protected void onStart()
    {
        busMapView.onStart();
        super.onStart();
    }
    @Override
    protected void onResume()
    {
        busMapView.onResume();
        super.onResume();
    }
    @Override
    protected void onPause()
    {
        busMapView.onPause();
        super.onPause();
    }
    @Override
    protected void onStop()
    {
        busMapView.onStop();
        super.onStop();
    }
    @Override
    protected void onDestroy()
    {
        busMapView.onDestroy();
        super.onDestroy();
    }
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState)
    {
        busMapView.onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }
    @Override
    public void onLowMemory()
    {
        busMapView.onLowMemory();
        super.onLowMemory();
    }
    private void enableLocationServices()
    {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage("Enable Location Services").setCancelable(false)
                .setPositiveButton("Enable", (dialog, which) -> startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)))
                .setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        final AlertDialog alertDialog = builder.create();

        alertDialog.show();
    }
    private void getCurrentUserLocation()
    {
        //Check if permissions are granted, if not request permissions
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        }
        else
        {
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            // Check if location is not null, if it is popup an error
            if (location != null)
            {
                double currentLat = location.getLatitude();
                double currentLon = location.getLongitude();
                currentLatLng = new LatLng(currentLat, currentLon);
            }
            else
            {
                Toast.makeText(this, "Unable to get location.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}