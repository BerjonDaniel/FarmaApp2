package com.example.farmaapp2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.location.Criteria;
import android.location.LocationListener;
import android.os.Bundle;

import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.util.JsonReader;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.Marker;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

class PlacesDecoder extends Activity {
    Intent intentThatCalled;
    public double latitude;
    public double longitude;
    public LocationManager locationManager;
    public Criteria criteria;
    public String bestProvider;
}

class GooglePlace3 {
    private String name;
    private String latitude;
    private String longitude;

    public GooglePlace3() {
        this.name = "";
        this.latitude = "";
        this.longitude = "";
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLatitude(String latitude) {

        this.latitude = latitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

}

public class MapsActivity3 extends AppCompatActivity implements LocationListener, OnMapReadyCallback {
    GoogleMap mMap;
    private final int REQUEST_PERMISSION_ACCESS_FINE_LOCATION = 1;

    public double latitude;
    public double longitude;
    public LocationManager locationManager;
    public Criteria criteria;
    public String bestProvider;
    private TextView locationTextView;

    String voice2text; //added
    // Radio de búsqueda
    final String radius = "4000";
    // Tipo de establecimiento (ver API Google Places)
    final String type = "pharmacy";
    private final GoogleMap.OnMapClickListener mClickListener = new GoogleMap.OnMapClickListener() {

        @Override
        public void onMapClick(LatLng position) {
            // Recibimos las coordenadas del punto del mapa que pulsó el usuario.
            // Para realizar la concatenación de texto forma eficiente, usaremos un objeto de la clase StringBuffer:
            StringBuffer text = new StringBuffer();
            text.append("GPS: ");
            text.append(position.latitude).append(", ").append(position.longitude);

        }
    }; //Fin de la definición e inicialización del atributo mClickListener

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapfragment);

        locationTextView = findViewById(R.id.locationTextView);

        // Obtenemos el objeto SupportMapFragment y solicitamos una notificación cuando el mapa esté listo para ser utilizado
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapsView);
        assert mapFragment != null;
        mapFragment.getMapAsync((OnMapReadyCallback) this);

    }

    // Único método de la interfaz OnMapReadyCallback, que implementa nuestra Activity,
    // que debemos rellenar para hacer uso del mapa una vez que está listo
    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

        // Obtenemos el mapa, que nunca será NULL,
        // y ya podemos hacer lo que sea con él
        mMap = googleMap;

        // En primer lugar, registramos los escuchadores de eventos del mapa que hemos creado
        mMap.setOnMapClickListener(mClickListener); // para clicks sobre el mapa

        // Activamos algunos controles en el mapa:
        // (https://developers.google.com/maps/documentation/android/interactivity)

        // Primero pedimos el objeto que nos permite modificar la UI.
        // Todos los cambios efectuados sobre este objeto se reflejan inmediatamente en el mapa
        UiSettings settings = mMap.getUiSettings();

        settings.setZoomControlsEnabled(true); // botones para hacer zoom

        settings.setMapToolbarEnabled(true);

        settings.setCompassEnabled(true); // brújula (SOLO se muestra el icono si se rota el mapa con los dedos)
        getLocation();

        // Comprobamos si tenemos permiso para acceder a la localización
        if (checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true); // botón "My Location"
            // Obtener la ubicación actualizada aquí
            LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            @SuppressLint("MissingPermission")
            Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (location != null) {
                locationTextView.setText(latitude + "," + longitude);
                centerMap(latitude, longitude);

                /*
                int locationAux = (int) (longitude + latitude);
                locationTextView = findViewById(R.id.locationTextView);
                locationTextView.setText(locationAux);

                 */

                /*
                LatLng sydney = new LatLng(latitude, longitude);
                mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
                 */
            } else {

            }

        } else {
            // no tiene permiso, solicitarlo
            requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_PERMISSION_ACCESS_FINE_LOCATION);
            // cuando se nos conceda el permiso se llamará a onRequestPermissionsResult()
        }

        // Configura el Listener para el botón "Mi ubicación"
        mMap.setOnMyLocationButtonClickListener(() -> {
            // Aquí puedes realizar las acciones que desees cuando se toca el botón
            // "Mi ubicación"
            // Por ejemplo, puedes centrar el mapa en la ubicación actual del usuario
            // o realizar alguna otra operación relacionada con la ubicación
            locationTextView.setText(latitude + "," + longitude);
            centerMap(latitude, longitude);


            // Devuelve "true" si quieres que el comportamiento predeterminado también se ejecute
            // (centrar el mapa en la ubicación del usuario). Si devuelves "false", solo se ejecutará
            // tu código personalizado.
            return true;
        });

    }

    public boolean isLocationEnabled(Context context) {
        //...............
        return true;
    }

    protected void getLocation() {
        if (isLocationEnabled(MapsActivity3.this)) {
            locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
            criteria = new Criteria();
            bestProvider = String.valueOf(locationManager.getBestProvider(criteria, true));

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            Location location = locationManager.getLastKnownLocation(bestProvider);
            locationManager.requestLocationUpdates(bestProvider, 1000, 0, this);
            latitude = location.getLatitude();
            longitude = location.getLongitude();
            locationTextView.setText(latitude + "," + longitude);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                centerMap(latitude, longitude);
            }
        }

    }

        @Override
        protected void onPause() {
            super.onPause();
            locationManager.removeUpdates((LocationListener) this);

        }


        public void onLocationChanged(Location location) {
            //Hey, a non null location! Sweet!

            //remove location callback:
            locationManager.removeUpdates((LocationListener) this);

            //open the map:
            latitude = location.getLatitude();
            longitude = location.getLongitude();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                centerMap(latitude, longitude);
            }
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
    @RequiresApi(api = Build.VERSION_CODES.R)
    public void centerMap(double latitude, double longitude){

        // A partir de una pareja de coordenadas (tipo double) creamos un objeto LatLng,
        // que es el tipo de dato que debemos usar al tratar con mapas
        LatLng position = new LatLng(latitude, longitude);

        // Obtenemos un objeto CameraUpdate que indique el movimiento de cámara que queremos;
        // en este caso, centrar el mapa en unas coordenadas con el método newLatLng()
        CameraUpdate update;

        // Alternativamente, se puede hacer lo mismo a la vez que se cambia el nivel de zoom
        // (comentar si se desea evitar el zoom)
        float zoom = 13;
        update = CameraUpdateFactory.newLatLngZoom(position, zoom);

        // Más información sobre distintos movimientos de cámara aquí:
        // http://developer.android.com/reference/com/google/android/gms/maps/CameraUpdateFactory.html
        new MapsActivity3.GooglePlaces().execute();


        // Pasamos el tipo de actualización configurada al método del mapa que mueve la cámara
        mMap.moveCamera(update);

    }
    private class GooglePlaces extends AsyncTask<View, Void, ArrayList<GooglePlace>> {

        @Override
        protected ArrayList<GooglePlace> doInBackground(View... urls) {
            ArrayList<GooglePlace> temp;
            //print the call in the console
            System.out.println("https://maps.googleapis.com/maps/api/place/nearbysearch/json?location="
                    + latitude + "," + longitude + "&radius=" + radius + "&type=" + type + "&sensor=true&key=" + "AIzaSyBLOTxH3QWbX6x34KvAOjkjzS_GodPC0v8");

            // make Call to the url
            temp = makeCall("https://maps.googleapis.com/maps/api/place/nearbysearch/json?location="
                    + latitude + "," + longitude + "&radius=" + radius + "&type=" + type + "&sensor=true&key=" + "AIzaSyBLOTxH3QWbX6x34KvAOjkjzS_GodPC0v8");

            return temp;
        }



        @Override
        protected void onPostExecute(ArrayList<GooglePlace> result) {

            // Aquí se actualiza el interfaz de usuario
            List<String> listTitle = new ArrayList<>();

            for (int i = 0; i < result.size(); i++) {
                // make a list of the venus that are loaded in the list.
                // show the name, the category and the city
                listTitle.add(i, "Place name: " +result.get(i).getName() + "\nLatitude: " + result.get(i).getLatitude() + "\nLongitude:" + result.get(i).getLongitude());
                MarkerOptions markerOpts = new MarkerOptions();
                double latitud = Double.parseDouble(result.get(i).getLatitude() );
                double longitud = Double.parseDouble(result.get(i).getLongitude() );
                LatLng location = new LatLng(latitud, longitud);

                markerOpts.position(location); // ubicación en el mapa (único requisito imprescindible)
                markerOpts.draggable(true); // se le permite ser arrastrado (¡preconfiguración!
                // para hacerlo a posteriori, utilizar Marker.setDraggable(boolean))
                // Se arrastra con una pulsación larga + movimiento sin levantar el dedo
                markerOpts.title(result.get(i).getName()); // título

                Marker marker = mMap.addMarker(markerOpts);

            }


        }
    }

    public static ArrayList<GooglePlace> makeCall(String stringURL) {

        URL url = null;
        BufferedInputStream is = null;
        JsonReader jsonReader;
        ArrayList<GooglePlace> temp = new ArrayList<>();

        try {
            url = new URL(stringURL);
        } catch (Exception ex) {
            System.out.println("Malformed URL");
        }

        try {
            if (url != null) {
                HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
                is = new BufferedInputStream(urlConnection.getInputStream());
            }
        } catch (IOException ioe) {
            System.out.println("IOException");
        }

        if (is != null) {
            try {
                jsonReader = new JsonReader(new InputStreamReader(is, "UTF-8"));
                jsonReader.beginObject();
                while (jsonReader.hasNext()) {
                    String name = jsonReader.nextName();
                    // Busca la cadena "results"
                    if (name.equals("results")) {
                        // comienza un array de objetos
                        jsonReader.beginArray();
                        while (jsonReader.hasNext()) {
                            GooglePlace poi = new GooglePlace();
                            jsonReader.beginObject();
                            // comienza un objeto
                            while (jsonReader.hasNext()) {
                                name = jsonReader.nextName();
                                if (name.equals("name")) {
                                    // si clave "name" guarda el valor
                                    poi.setName(jsonReader.nextString());
                                    System.out.println("PLACE NAME:" + poi.getName());
                                } else if (name.equals("geometry")) {
                                    // Si clave "geometry" empieza un objeto
                                    jsonReader.beginObject();
                                    while (jsonReader.hasNext()) {
                                        name = jsonReader.nextName();
                                        if (name.equals("location")) {
                                            // dentro de "geometry", si clave "location" empieza un objeto
                                            jsonReader.beginObject();
                                            while (jsonReader.hasNext()) {
                                                name = jsonReader.nextName();
                                                // se queda con los valores de "lat" y "long" de ese objeto
                                                if (name.equals("lat")) {
                                                    poi.setLatitude(jsonReader.nextString());
                                                    System.out.println("PLACE LATITUDE:" + poi.getLatitude());
                                                } else if (name.equals("lng")) {
                                                    poi.setLongitude(jsonReader.nextString());
                                                    System.out.println("PLACE LONGITUDE:" + poi.getLongitude());
                                                } else {
                                                    jsonReader.skipValue();
                                                }
                                            }
                                            jsonReader.endObject();
                                        } else {
                                            jsonReader.skipValue();
                                        }
                                    }
                                    jsonReader.endObject();
                                } else{
                                    jsonReader.skipValue();
                                }
                            }
                            jsonReader.endObject();
                            temp.add(poi);
                        }
                        jsonReader.endArray();
                    } else {
                        jsonReader.skipValue();
                    }
                }
                jsonReader.endObject();
            } catch (Exception e) {
                System.out.println("Exception");
                return new ArrayList<>();
            }
        }

        return temp;
    }
}
