/*
 * Nombre del proyecto: LucasMap
 * Autores: Leonardo Duarte, Lucas Baruja, Iván Samudio, Ezequiel Arce
 * Descripción: Pantalla de información de la aplicación que muestra datos sobre la versión, desarrolladores y fecha de lanzamiento.
 * Fecha de creación: 24/10/2024
 * Forma de utilizar: Muestra los datos de la aplicación y permite volver al menú principal.
 */

package com.example.tp2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        // Configurar botón para volver al menú principal
        Button buttonBackToMenu = findViewById(R.id.buttonBackToMenu);
        buttonBackToMenu.setOnClickListener(v -> {
            Intent intent = new Intent(AboutActivity.this, MainMenuActivity.class);
            startActivity(intent);
            finish();
        });
    }
}
