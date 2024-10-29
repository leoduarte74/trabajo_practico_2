/*
 * Nombre del proyecto: LucasMap
 * Autores: Leonardo Duarte, Lucas Baruja, Iván Samudio, Ezequiel Arce
 * Descripción: Actividad que muestra información básica del sensor IMU (acelerómetro).
 * Fecha de creación: 24/10/2024
 * Forma de utilizar: Se muestra al usuario para verificar que el sensor está funcionando.
 */

package com.example.tp2;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class IMUActivity extends AppCompatActivity {

    private SensorManager sensorManager;
    private Sensor accelerometer;
    private SensorEventListener sensorEventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imuactivity);

        // Inicializar SensorManager y el acelerómetro
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        // TextView para mostrar datos del acelerómetro
        TextView sensorDataTextView = findViewById(R.id.sensorDataTextView);

        // Configurar el listener del sensor
        sensorEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                float x = event.values[0];
                float y = event.values[1];
                float z = event.values[2];
                sensorDataTextView.setText("Aceleración:\nX: " + x + "\nY: " + y + "\nZ: " + z);
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {
                // No es necesario hacer nada aquí para esta funcionalidad
            }
        };

        // Registrar el listener
        sensorManager.registerListener(sensorEventListener, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);

        // Botón para volver al menú
        Button buttonBackToMenu = findViewById(R.id.buttonBackToMenu);
        buttonBackToMenu.setOnClickListener(v -> {
            Intent intent = new Intent(IMUActivity.this, MainMenuActivity.class);
            startActivity(intent);
            finish();
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Detener el listener cuando se destruye la actividad
        sensorManager.unregisterListener(sensorEventListener);
    }
}
