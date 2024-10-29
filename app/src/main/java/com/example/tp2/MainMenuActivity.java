/*
 * Nombre del proyecto: LucasMap
 * Autores: Leonardo Duarte, Lucas Baruja, Iván Samudio, Ezequiel Arce
 * Descripción: Actividad que funciona como menú principal para navegar entre las diferentes pantallas de la aplicación.
 * Fecha de creación: 24/10/2024
 * Forma de utilizar: Muestra opciones para acceder al mapa, información de sensores, y pantalla de "Acerca de", con música de fondo.
 */

package com.example.tp2;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class MainMenuActivity extends AppCompatActivity {

    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        // INICIALIZAR Y REPRODUCIR MÚSICA DE FONDO
        mediaPlayer = MediaPlayer.create(this, R.raw.menu_music);
        mediaPlayer.setLooping(true); // Hace que la música se repita
        mediaPlayer.start();

        // VERIFICAR LA DISPONIBILIDAD DE LOS SENSORES IMU Y GNSS
        verificarSensores();

        // BOTÓN PARA IR A LA PANTALLA DE MAPA
        Button buttonMap = findViewById(R.id.buttonMap);
        buttonMap.setOnClickListener(v -> startActivity(new Intent(MainMenuActivity.this, MainActivity.class)));

        // BOTÓN PARA IR A LA PANTALLA DE INFORMACIÓN IMU
        Button buttonIMU = findViewById(R.id.buttonIMU);
        buttonIMU.setOnClickListener(v -> startActivity(new Intent(MainMenuActivity.this, IMUActivity.class)));

        // BOTÓN PARA IR A LA PANTALLA DE INFORMACIÓN GNSS
        Button buttonGNSS = findViewById(R.id.buttonGNSS);
        buttonGNSS.setOnClickListener(v -> startActivity(new Intent(MainMenuActivity.this, GNSSActivity.class)));

        // BOTÓN PARA IR A LA PANTALLA "ACERCA DE"
        Button buttonAbout = findViewById(R.id.buttonAbout);
        buttonAbout.setOnClickListener(v -> startActivity(new Intent(MainMenuActivity.this, AboutActivity.class)));
    }

    // MÉTODO PARA VERIFICAR LA EXISTENCIA DE LOS SENSORES IMU Y GNSS
    private void verificarSensores() {
        boolean imuDisponible = verificarIMU();
        boolean gnssDisponible = verificarGNSS();

        // Configuración del mensaje en función de la disponibilidad de los sensores
        String mensaje;
        if (imuDisponible && gnssDisponible) {
            mensaje = "¡Tienes el sensor IMU y GNSS funcionando correctamente!";
        } else {
            mensaje = "Uno o ambos sensores no están disponibles. IMU: " +
                    (imuDisponible ? "Disponible" : "No disponible") +
                    ", GNSS: " + (gnssDisponible ? "Disponible" : "No disponible");
        }

        // Mostrar el mensaje en una ventana emergente (AlertDialog)
        mostrarDialogo(mensaje);
    }

    // MÉTODO PARA MOSTRAR EL DIALOGO CON EL MENSAJE DE DISPONIBILIDAD
    private void mostrarDialogo(String mensaje) {
        new AlertDialog.Builder(this)
                .setTitle("Estado de Sensores")
                .setMessage(mensaje)
                .setPositiveButton("Cerrar", (dialog, which) -> dialog.dismiss())
                .setCancelable(false) // Evita que se cierre al tocar fuera del diálogo
                .show();
    }

    // MÉTODO PARA VERIFICAR SI HAY UN ACELERÓMETRO (IMU) DISPONIBLE
    private boolean verificarIMU() {
        SensorManager sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager != null) {
            Sensor accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            return accelerometer != null;
        }
        return false;
    }

    // MÉTODO PARA VERIFICAR SI HAY GPS (GNSS) DISPONIBLE
    private boolean verificarGNSS() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager != null && locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // PAUSA LA MÚSICA SI LA ACTIVIDAD SE PAUSA
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // REANUDA LA MÚSICA AL VOLVER A LA ACTIVIDAD
        if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
            mediaPlayer.start();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // LIBERA RECURSOS CUANDO LA ACTIVIDAD SE DESTRUYE
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}
