package gr.greekbusses.activities;

import android.Manifest;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import gr.greekbusses.R;
import gr.greekbusses.misc.EmptyTokenizer;
import gr.greekbusses.misc.Strings;

public class MainActivity extends AppCompatActivity
{
    private static final int REQUEST_LOCATION = 1;

    LocationManager locationManager;

    MultiAutoCompleteTextView regionTextView;
    ImageButton locationButton;
    Button confirmButton;
    Button contactButton;

    ArrayList<String> regions = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Objects.requireNonNull(getSupportActionBar()).hide();
        initializeViewObjects();
        getRegionsFromDatabase();

        NotificationChannel channel = new NotificationChannel("main", "Main", NotificationManager.IMPORTANCE_DEFAULT);
        NotificationManager manager = getSystemService(NotificationManager.class);
        manager.createNotificationChannel(channel);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, regions);
        regionTextView.setAdapter(adapter);
        regionTextView.setTokenizer(new EmptyTokenizer());

        locationButton.setOnClickListener(view ->
        {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

            if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
            {
                enableLocationServices();
            }
            else
            {
                getCurrentUserLocation();
            }
        });
        confirmButton.setOnClickListener(view ->
        {
            if (isValidRegion(regionTextView.getText().toString()))
            {
                createNotification();

                Intent intent = new Intent(MainActivity.this, MenuActivity.class);
                intent.putExtra("region", regionTextView.getText().toString());
                startActivity(intent);
            }
        });
        contactButton.setOnClickListener(view ->
        {
            Intent intent = new Intent(MainActivity.this, ContactActivity.class);
            startActivity(intent);
        });
    }
    private void initializeViewObjects()
    {
        regionTextView = findViewById(R.id.regionTextView);
        locationButton = findViewById(R.id.locationButton);
        confirmButton = findViewById(R.id.confirmButton);
        contactButton = findViewById(R.id.contactButton);
    }
    public void createNotification()
    {
        String title = "Greek Busses";
        String message = "Did you forget about us?";

        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(MainActivity.this, "main");
        builder.setContentTitle(title);
        builder.setContentText(message);
        builder.setSmallIcon(R.drawable.ic_bus_icon);
        builder.setAutoCancel(true);
        builder.setContentIntent(pendingIntent);
        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(MainActivity.this);
        managerCompat.notify(1, builder.build());
    }
    private void getRegionsFromDatabase()
    {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();

        firestore.collection(Strings.REG).get().addOnCompleteListener(task ->
        {
            if (task.isSuccessful())
            {
                for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult()))
                {
                    Log.d(Strings.TAG, document.getId() + " => " + document.getData());
                    regions.add(Objects.requireNonNull(document.get("region_name")).toString());
                }
            }
            else
            {
                Log.d(Strings.TAG, "Error getting documents: ", task.getException());
            }
        });
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
        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        }
        else
        {
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            // Check if location is not null, if it is popup an error
            if (location != null)
            {
                double lat = location.getLatitude();
                double lon = location.getLongitude();
                regionTextView.setText(getLocationName(lat, lon));
            }
            else
            {
                Toast.makeText(this, "Unable to get location.", Toast.LENGTH_SHORT).show();
            }
        }
    }
    public String getLocationName(double latitude, double longitude)
    {
        String cityName = "Not Found";

        Geocoder gcd = new Geocoder(getBaseContext(), Locale.getDefault());

        try
        {
            List<Address> addresses = gcd.getFromLocation(latitude, longitude, 10);

            for (Address address : addresses)
            {
                if (address != null)
                {
                    String city = address.getLocality();

                    if (city != null && !city.equals(""))
                    {
                        cityName = city;
                        System.out.println("city ::  " + cityName);
                    }
                }
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return cityName;
    }
    public boolean isValidRegion(String string)
    {
        for (String region : regions)
        {
            if (Objects.equals(string, region))
            {
                return Objects.equals(string, region);
            }
        }
        return false;
    }
}