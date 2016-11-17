package co.edu.unal.reto9;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

public class MainActivity extends AppCompatActivity {

    protected static final String TAG = "Reto9-MainActivity";

    private GoogleApiClient mGoogleApiClient;
    private Location mCurrentLocation;
    private int PLACE_PICKER_REQUEST = 1;
    private PlacePicker.IntentBuilder builder;
    private TextView mSelectedSite;
    private EditText mSelectedRadius;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.createAPIClient();
        queryCurrentLocation();
        builder = new PlacePicker.IntentBuilder();

        Button viewMapButton = (Button)findViewById(R.id.view_map_button);
        mSelectedSite = (TextView)findViewById(R.id.place_information);
        mSelectedRadius = (EditText)findViewById(R.id.radius_edittext);

        viewMapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    queryCurrentLocation();
                    LatLng center = new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
                    int radius = Integer.parseInt(mSelectedRadius.getText().toString());
                    Intent intent = builder.setLatLngBounds(new LatLngBounds(
                            addMetersToLatLng(radius * -1, center),
                            addMetersToLatLng(radius, center)))
                            .build(MainActivity.this);
                    startActivityForResult(intent, PLACE_PICKER_REQUEST);
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(this, data);
                String description = String.format("Lugar: %s \nDirección: %s \nTeléfono: %s \nWebsite: %s \nRating: %s",
                        place.getName(),
                        place.getAddress(),
                        place.getPhoneNumber(),
                        place.getWebsiteUri(),
                        place.getRating());
                mSelectedSite.setText(description);
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }


    // MÉTODOS PRIVADOS ///
    private synchronized void createAPIClient() {
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(LocationServices.API)
                    .addApi(Places.GEO_DATA_API)
                    .addApi(Places.PLACE_DETECTION_API)
                    .build();
        }
    }

    private LatLng addMetersToLatLng(float meters, LatLng latLng) {
        float r_earth = 6371000;

        double newLatitude  = latLng.latitude + (meters / r_earth) * (180 / Math.PI);
        double newLongitude = latLng.longitude + (meters/ r_earth) * (180 / Math.PI) / Math.cos(latLng.latitude * Math.PI/180);

        return new LatLng(newLatitude, newLongitude);
    }

    private void queryCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
    }
}
