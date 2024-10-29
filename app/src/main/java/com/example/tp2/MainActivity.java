/*
 * Nombre del proyecto: LucasMap
 * Autores: Leonardo Duarte, Lucas Baruja, Iván Samudio, Ezequiel Arce
 * Descripción: Actividad principal que gestiona el rastreo GPS, el mapa y los sensores de IMU.
 * Fecha de creación: 24/10/2024
 * Forma de utilizar: Muestra la ubicación del usuario en un mapa y registra datos de movimiento y distancia.
 */

package com.example.tp2;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    private LocationManager locationManager;
    private LocationListener locationListener;
    private SensorManager sensorManager;
    private Sensor accelerometer;
    private SensorEventListener sensorEventListener;
    private ActivityStats activityStats;
    private long startTime;
    private GoogleMap mMap;
    private List<LatLng> routePoints = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // INICIALIZAR ActivityStats PARA REGISTRAR DATOS DE LA ACTIVIDAD
        activityStats = new ActivityStats();

        // INICIAR EL TIEMPO DE LA ACTIVIDAD
        startTime = System.currentTimeMillis();

        // CONFIGURAR EL MAPA
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        // VERIFICAR Y SOLICITAR PERMISO DE GPS
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            iniciarGPS();
        }

        // INICIAR SENSORES DE IMU
        iniciarIMU();

        // CONFIGURAR EL BOTÓN PARA FINALIZAR LA ACTIVIDAD Y MOSTRAR EL RESUMEN
        Button buttonFinish = findViewById(R.id.buttonFinish);
        buttonFinish.setOnClickListener(v -> {
            // CALCULAR TIEMPO TOTAL DE LA ACTIVIDAD
            long endTime = System.currentTimeMillis();
            long totalTime = endTime - startTime; // Tiempo total en milisegundos
            double totalTimeSeconds = totalTime / 1000.0; // Convertir a segundos

            // CALCULAR VELOCIDAD PROMEDIO
            double averageSpeed = activityStats.getTotalDistance() / totalTimeSeconds;

            // CREAR INTENT PARA ENVIAR DATOS A SummaryActivity
            Intent intent = new Intent(MainActivity.this, SummaryActivity.class);
            intent.putExtra("totalDistance", activityStats.getTotalDistance());
            intent.putExtra("fastMovements", activityStats.getFastMovements());
            intent.putExtra("averageSpeed", averageSpeed); // ENVIAR VELOCIDAD PROMEDIO

            // INICIAR SummaryActivity Y FINALIZAR MainActivity
            startActivity(intent);
            finish();
        });
    }

    // INICIALIZAR EL GPS Y ESCUCHAR CAMBIOS EN LA UBICACIÓN
    private void iniciarGPS() {
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                double latitud = location.getLatitude();
                double longitud = location.getLongitude();

                // ACTUALIZAR DISTANCIA EN ActivityStats
                activityStats.updateDistance(latitud, longitud);

                // AÑADIR PUNTO AL MAPA Y DIBUJAR RECORRIDO
                LatLng newPoint = new LatLng(latitud, longitud);
                routePoints.add(newPoint);
                drawRoute();

                // MOVER LA CÁMARA A LA NUEVA UBICACIÓN Y AGREGAR UN MARCADOR
                if (mMap != null) {
                    mMap.clear(); // Limpiar marcador anterior
                    mMap.addMarker(new MarkerOptions().position(newPoint).title("Ubicación Actual"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(newPoint, 15)); // Mover y hacer zoom en la ubicación actual
                }

                // MOSTRAR DISTANCIA TOTAL EN UN TOAST
                Toast.makeText(MainActivity.this, "Distancia: " + activityStats.getTotalDistance() + " m", Toast.LENGTH_SHORT).show();
            }
        };

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 6, locationListener);
        }
    }

    // MÉTODO PARA DIBUJAR EL RECORRIDO EN EL MAPA
    private void drawRoute() {
        if (mMap != null && routePoints.size() > 1) {
            PolylineOptions polylineOptions = new PolylineOptions()
                    .addAll(routePoints)
                    .width(5)
                    .color(ContextCompat.getColor(this, R.color.route_color));
            mMap.addPolyline(polylineOptions);
        }
    }

    // INICIALIZAR SENSORES DE IMU (ACELERÓMETRO)
    private void iniciarIMU() {
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        sensorEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                float x = event.values[0];
                float y = event.values[1];
                float z = event.values[2];
                float acceleration = (float) Math.sqrt(x * x + y * y + z * z);

                if (acceleration > 35) {
                    // REGISTRAR MOVIMIENTO BRUSCO EN ActivityStats
                    activityStats.registerFastMovement();
                    Toast.makeText(MainActivity.this, "¡Movimiento rápido detectado! Total: " + activityStats.getFastMovements(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {}
        };

        sensorManager.registerListener(sensorEventListener, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    // MÉTODO QUE SE EJECUTA CUANDO EL MAPA ESTÁ LISTO
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Intentar obtener la última ubicación conocida y centrar la cámara en ella
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (lastKnownLocation != null) {
                LatLng lastLocation = new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lastLocation, 15)); // Centrar y hacer zoom en la última ubicación conocida
                mMap.addMarker(new MarkerOptions().position(lastLocation).title("Ubicación Actual"));
            }
        }
    }
}
