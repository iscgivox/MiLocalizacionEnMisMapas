package net.ivanvega.mismapas;

import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.PolylineOptions;

public class MapsActivityMarkers extends FragmentActivity implements OnMapReadyCallback,
        GoogleMap.OnMarkerClickListener,
        View.OnClickListener{

    private GoogleMap mMap;
    Button btnMarcador, btnLinea, btnPoligono;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps_markers);
//         Obtain the SupportMapFragment and get notified when the map is ready to be used.
        inicializarUI();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map_markers );
        mapFragment.getMapAsync(this);
    }

    private void inicializarUI() {
        btnMarcador = (Button)findViewById(R.id.btnMarcador); btnMarcador.setOnClickListener(this);
        btnLinea = (Button)findViewById(R.id.btnLineas); btnLinea.setOnClickListener(this);
        btnPoligono = (Button)findViewById(R.id.btnPoligono); btnPoligono.setOnClickListener(this);
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



//        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
//            @Override
//            public boolean onMarkerClick(Marker marker) {
//                Toast.makeText(MapsActivityMarkers.this, "El marcador es pulsado desde objeto anonimo: " + marker.getTitle(), Toast.LENGTH_LONG).show();
//                return true;
//            }
//        });

        mMap.setOnMarkerClickListener(this);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {

        Toast.makeText(this, "El marcador es pulsado: " + marker.getTitle(), Toast.LENGTH_LONG).show();

        //Dibujo con Lineas

//        PolylineOptions lineas = new PolylineOptions()
////                .add(new LatLng(45.0, -12.0))
////                .add(new LatLng(45.0, 5.0))
////                .add(new LatLng(34.5, 5.0))
////                .add(new LatLng(34.5, -12.0))
//                .add(new LatLng(45.0, -12.0));
//
//        lineas.width(8);
//        lineas.color(Color.RED);
//
//        mMap.addPolyline(lineas);

        return true;
    }

    @Override
    public void onClick(View v) {
        if (v.equals(btnPoligono)){
// Add a marker in Sydney and move the camera
            mostrarPoligono();
        }
        if (v.equals(btnLinea)){
            mostrarLineas();
        }
        if (v.equals(btnMarcador)){
            LatLng sydney = new LatLng(-34, 151);
            mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        }

    }

    private void mostrarLineas()
    {
        //Dibujo con Lineas

        PolylineOptions lineas = new PolylineOptions()
                .add(new LatLng(45.0, -12.0))
                .add(new LatLng(45.0, 5.0))
                .add(new LatLng(34.5, 5.0));
//                .add(new LatLng(34.5, -12.0))
//                .add(new LatLng(45.0, -12.0));

        lineas.width(8);
        lineas.color(Color.RED);

        mMap.addPolyline(lineas);
    }

    private void mostrarPoligono()
    {
        //Dibujo con Poligonos

        PolygonOptions rectangulo = new PolygonOptions()
                .add(new LatLng(45.0, -12.0),
                        new LatLng(45.0, 5.0),
                        new LatLng(34.5, 5.0),
                        new LatLng(34.5, -12.0),
                        new LatLng(45.0, -12.0));

        rectangulo.strokeWidth(8);
        rectangulo.strokeColor(Color.RED);

        mMap.addPolygon(rectangulo);
    }

}
