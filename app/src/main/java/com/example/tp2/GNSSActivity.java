/*
 * Nombre del proyecto: LucasMap
 * Autores: Leonardo Duarte, Lucas Baruja, Iván Samudio, Ezequiel Arce
 * Descripción: Actividad que muestra información de ubicación GNSS, incluyendo altitud.
 * Fecha de creación: 24/10/2024
 * Forma de utilizar: Muestra la latitud, longitud y altitud del usuario en tiempo real.
 */

package com.example.tp2;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import java.text.DecimalFormat;

public class GNSSActivity extends AppCompatActivity {

    private LocationManager locationManager;
    private LocationListener locationListener;
    private DecimalFormat decimalFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gnssactivity);

        // Inicializar DecimalFormat para redondear a 2 cifras
        decimalFormat = new DecimalFormat("#.##");

        // TextView para mostrar la información de la ubicación
        TextView locationTextView = findViewById(R.id.locationTextView);

        // Verificar permisos de ubicación y solicitarlos si es necesario
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            setupLocationUpdates(locationTextView);
        }

        // Configurar el botón para volver al menú principal
        Button buttonBackToMenu = findViewById(R.id.buttonBackToMenu);
        buttonBackToMenu.setOnClickListener(v -> finish());
    }

    private void setupLocationUpdates(TextView locationTextView) {
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onLocationChanged(Location location) {
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();
                double altitude = location.getAltitude();

                // Formatear los números a 2 decimales antes de mostrarlos
                String formattedLatitude = decimalFormat.format(latitude);
                String formattedLongitude = decimalFormat.format(longitude);
                String formattedAltitude = decimalFormat.format(altitude);

                // Mostrar la información en el TextView
                locationTextView.setText("Latitud: " + formattedLatitude +
                        "\nLongitud: " + formattedLongitude +
                        "\nAltitud: " + formattedAltitude + " m");
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {}

            @Override
            public void onProviderEnabled(String provider) {}

            @Override
            public void onProviderDisabled(String provider) {}
        };

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5, locationListener);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (locationManager != null && locationListener != null) {
            locationManager.removeUpdates(locationListener);
        }
    }
}
