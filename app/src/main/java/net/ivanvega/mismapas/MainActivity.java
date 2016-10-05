package net.ivanvega.mismapas;

import android.*;
import android.Manifest;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.server.converter.StringToIntConverter;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;

import static com.google.ads.AdRequest.LOGTAG;

public class MainActivity extends AppCompatActivity
        implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener , LocationListener {

    private static final int PETICION_CONFIG_UBICACION = 1002;
    private LocationRequest locRequest;

    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    private static final int
            PETICION_PERMISO_LOCALIZACION
            = 1001;

    TextView txtLat, txtLng;
    Switch swActualizaciones;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inicializarUI();

        if (mGoogleApiClient == null) {
            mGoogleApiClient =
                    new GoogleApiClient.Builder(this)
                            .addConnectionCallbacks(this)
                            .addOnConnectionFailedListener(this)
                            .addApi(LocationServices.API)
                            .build();

        }

    }

    private void enableLocationUpdates(){
        locRequest = new LocationRequest();
        locRequest.setInterval(2000);
        locRequest.setFastestInterval(1000);
        locRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest locationSettingsRequest
                = new LocationSettingsRequest.Builder()
                .addLocationRequest(locRequest)
                .build();

       PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(
                        mGoogleApiClient, locationSettingsRequest);

        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult locationSettingsResult) {
                final Status status = locationSettingsResult.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:

                        Log.i(LOGTAG, "Configuración correcta");
                        startLocationUpdates();
                        break;

                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        try {
                            Log.i(LOGTAG, "Se requiere actuación del usuario");
                            status.startResolutionForResult(MainActivity.this, PETICION_CONFIG_UBICACION);
                        } catch (IntentSender.SendIntentException e) {
                            swActualizaciones.setChecked(false);
                            Log.i(LOGTAG, "Error al intentar solucionar configuración de ubicación");
                        }
                        break;

                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        Log.i(LOGTAG, "No se puede cumplir la configuración de ubicación necesaria");
                        swActualizaciones.setChecked(false);
                        break;
                }
            }
        });

    }


    private void inicializarUI() {
        txtLat = (TextView) findViewById(R.id.txtLat);
        txtLng = (TextView) findViewById(R.id.txtLon);
        swActualizaciones = (Switch)
                findViewById(R.id.swtActualizaciones);
        swActualizaciones.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                if (isChecked){
                    showLocation();
                }
                toogleLocationUpdates(isChecked);
            }
        });

    }

    private void toogleLocationUpdates(boolean isChecked) {
        if (isChecked){
            enableLocationUpdates();
        }else{
            disableLocationUpdates();
        }
    }

    private void disableLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient,this );
    }

    @Override
    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED

                &&

                ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED) {


            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PETICION_PERMISO_LOCALIZACION);

        }else{
            showLocation();
        }

    }

    private void showLocation() {


        //noinspection MissingPermission
        mLastLocation =
                LocationServices.FusedLocationApi
                        .getLastLocation(mGoogleApiClient);

        if(mLastLocation != null){

            Toast.makeText(this,
                    "Latitud: " + String.valueOf(mLastLocation.getLatitude()) +
                            ", Lomgituf: " + String.valueOf(mLastLocation.getLongitude()),
                    Toast.LENGTH_LONG).show();

            txtLat.setText(String.valueOf(mLastLocation.getLatitude()));
            txtLng.setText(String.valueOf(mLastLocation.getLongitude()));

        }
        else{
            Toast.makeText(this,"No hay localizacion",Toast.LENGTH_LONG
                    ).show();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        if (requestCode == 1001){
            if(grantResults.length>0 &&
                    grantResults[0]==PackageManager.PERMISSION_GRANTED){
                showLocation();
            }
        }

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case PETICION_CONFIG_UBICACION:
                switch (resultCode) {
                    case AppCompatActivity.RESULT_OK:
                        startLocationUpdates();
                        break;
                    case AppCompatActivity.RESULT_CANCELED:
                        Log.i(LOGTAG, "El usuario no ha realizado los cambios de configuración necesarios");
                        swActualizaciones.setChecked(false);
                        break;
                }
                break;
        }

    }

    private void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            //Ojo: estamos suponiendo que ya tenemos concedido el permiso.
            //Sería recomendable implementar la posible petición en caso de no tenerlo.

            Log.i(LOGTAG, "Inicio de recepción de ubicaciones");

            LocationServices.FusedLocationApi.requestLocationUpdates(
                    mGoogleApiClient, locRequest, this);

        }
    }

    private void updateLocation(Location location) {
        txtLng.setText(String.valueOf(location.getLongitude()));
        txtLat.setText(String.valueOf(location.getLatitude()));
    }

    @Override
    public void onLocationChanged(Location location) {
        updateLocation(location);
    }
}
