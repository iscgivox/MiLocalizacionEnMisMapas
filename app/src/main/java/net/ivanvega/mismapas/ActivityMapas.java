package net.ivanvega.mismapas;

import android.app.FragmentTransaction;
import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class ActivityMapas extends AppCompatActivity
implements OnMapReadyCallback, View.OnClickListener, GoogleMap.OnMapClickListener
{
    SupportMapFragment mapFragment ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapas);

//        mMapFragment = MapFragment.newInstance();
//        FragmentTransaction fragmentTransaction =
//                getFragmentManager().beginTransaction();
//        fragmentTransaction.add(R.id.my_container, mMapFragment);
//        fragmentTransaction.commit();

        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        inicializarUI();


    }

    GoogleMap map;

    @Override
    public void onMapReady(GoogleMap googleMap) {

        map = googleMap;
    }

    private Button btnOpcion, btnAnimar, btnMOver, btnPOsicion;

    private void inicializarUI(){
        btnOpcion = (Button) findViewById(R.id.btnOpcion); btnOpcion.setOnClickListener(this);
        btnAnimar = (Button) findViewById(R.id.btnAnimar); btnAnimar.setOnClickListener(this);
        btnMOver = (Button)findViewById(R.id.btnMover); btnMOver.setOnClickListener(this);
        btnPOsicion = (Button) findViewById(R.id.btnPosc); btnPOsicion.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        if(v.equals(btnOpcion)){
            map.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
            map.getUiSettings().setZoomControlsEnabled(true);
        }

        if(v.equals(btnMOver)){
            CameraUpdate camera = CameraUpdateFactory.
                    newLatLngZoom(new LatLng(19.427304,-99.137822),5);
            map.moveCamera(camera);
        }

        if(v.equals(btnAnimar)){
            LatLng angelInd = new LatLng(19.426978, -99.167775);
            CameraPosition position = new CameraPosition.Builder()
                    .target(angelInd)
                    .bearing(45)
                    .zoom(19)
                    .tilt(70)
                    .build();
            CameraUpdate campos = CameraUpdateFactory.newCameraPosition(position);
            map.animateCamera(campos);
        }

        if(v.equals(btnPOsicion)){
             CameraPosition camPosition = map.getCameraPosition();
            Toast.makeText(this, "Lat: " + String.valueOf(camPosition.target.latitude) +
            "\nLon: " + String.valueOf(camPosition.target.longitude)
            , Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onMapClick(LatLng latLng) {
        Projection proj = map.getProjection();
        Point coord = proj.toScreenLocation(latLng);

        Toast.makeText(
                this,
                "Click\n" +
                        "Lat: " + latLng.latitude + "\n" +
                        "Lng: " + latLng.longitude + "\n" +
                        "X: " + coord.x + " - Y: " + coord.y,
                Toast.LENGTH_SHORT).show();
    }
}

