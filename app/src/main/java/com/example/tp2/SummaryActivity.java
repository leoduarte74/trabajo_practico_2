/*
 * Nombre del proyecto: LucasMap
 * Autores: Leonardo Duarte, Lucas Baruja, Iván Samudio, Ezequiel Arce
 * Descripción: Actividad que muestra el resumen de la actividad física con estadísticas como distancia y movimientos.
 * Fecha de creación: 24/10/2024
 * Forma de utilizar: Se invoca al finalizar la actividad física para mostrar el resumen de los datos capturados.
 */

package com.example.tp2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.text.DecimalFormat;

public class SummaryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);

        // FORMATO PARA REDONDEAR DECIMALES
        DecimalFormat df = new DecimalFormat("#.##");

        // OBTENER DATOS PASADOS DESDE MainActivity
        Intent intent = getIntent();
        double totalDistance = intent.getDoubleExtra("totalDistance", 0.0);
        int fastMovements = intent.getIntExtra("fastMovements", 0);
        double averageSpeed = intent.getDoubleExtra("averageSpeed", 0.0);

        // MOSTRAR LOS DATOS EN TEXTVIEWS
        TextView textViewDistance = findViewById(R.id.textViewDistance);
        TextView textViewMovements = findViewById(R.id.textViewMovements);
        TextView textViewSpeed = findViewById(R.id.textViewSpeed);

        // ACTUALIZAR TEXTVIEWS CON FORMATO REDONDEADO
        textViewDistance.setText("Distancia recorrida: " + df.format(totalDistance) + " metros");
        textViewMovements.setText("Movimientos bruscos: " + fastMovements);
        textViewSpeed.setText("Velocidad promedio: " + df.format(averageSpeed) + " m/s");

        // CONFIGURAR BOTÓN PARA VOLVER AL MAPA
        Button buttonBackToMap = findViewById(R.id.buttonBackToMap);
        buttonBackToMap.setOnClickListener(v -> {
            Intent backIntent = new Intent(SummaryActivity.this, MainActivity.class);
            startActivity(backIntent);
            finish();
        });

        // CONFIGURAR BOTÓN PARA VOLVER AL MENÚ PRINCIPAL
        Button buttonBackToMenu = findViewById(R.id.buttonBackToMenu);
        buttonBackToMenu.setOnClickListener(v -> {
            Intent menuIntent = new Intent(SummaryActivity.this, MainMenuActivity.class);
            startActivity(menuIntent);
            finish();
        });
    }
}
