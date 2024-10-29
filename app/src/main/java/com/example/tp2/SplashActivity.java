/*
 * Nombre del proyecto: LucaMap
 * Autores: Leonardo Duarte, Lucas Baruja, Iván Samudio, Ezequiel Arce
 * Descripción: Pantalla inicial (Splash Screen) que redirige al usuario al menú principal.
 * Fecha de creación: 24/10/2024
 * Forma de utilizar: Se muestra al abrir la aplicación y redirige automáticamente a MainMenuActivity.
 */

package com.example.tp2;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // REDIRIGIR A MainMenuActivity DESPUÉS DE 3.5 SEGUNDOS
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            Intent intent = new Intent(SplashActivity.this, MainMenuActivity.class);
            startActivity(intent);
            finish(); // FINALIZA SplashActivity PARA NO VOLVER A ELLA
        }, 3500); // 3500 MILISEGUNDOS = 3.5 SEGUNDOS
    }
}
