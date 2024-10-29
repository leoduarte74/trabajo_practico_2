/*
 * Nombre del proyecto: SensoresApp
 * Autores: LucasLab
 * Descripción: Esta clase gestiona las estadísticas de la actividad, como la distancia recorrida y los movimientos bruscos.
 * Fecha de creación: 24/10/2024
 * Forma de utilizar: Instancia esta clase en MainActivity para registrar datos durante la actividad.
 */

package com.example.tp2;

public class ActivityStats {
    private double totalDistance; // DISTANCIA TOTAL RECORRIDA
    private double maxSpeed; // VELOCIDAD MÁXIMA
    private int fastMovements; // NÚMERO DE MOVIMIENTOS BRUSCOS

    private double lastLatitude; // ÚLTIMA LATITUD
    private double lastLongitude; // ÚLTIMA LONGITUD

    public ActivityStats() {
        this.totalDistance = 0.0;
        this.maxSpeed = 0.0;
        this.fastMovements = 0;
    }

    // MÉTODO PARA ACTUALIZAR LA DISTANCIA BASADO EN LAS NUEVAS COORDENADAS GPS
    public void updateDistance(double latitude, double longitude) {
        if (lastLatitude != 0 && lastLongitude != 0) {
            double distance = calculateDistance(lastLatitude, lastLongitude, latitude, longitude);
            totalDistance += distance;
        }
        lastLatitude = latitude;
        lastLongitude = longitude;
    }

    // CALCULAR DISTANCIA ENTRE DOS PUNTOS (LATITUD Y LONGITUD)
    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        double R = 6371e3; // RADIO DE LA TIERRA EN METROS
        double phi1 = Math.toRadians(lat1);
        double phi2 = Math.toRadians(lat2);
        double deltaPhi = Math.toRadians(lat2 - lat1);
        double deltaLambda = Math.toRadians(lon2 - lon1);

        double a = Math.sin(deltaPhi / 2) * Math.sin(deltaPhi / 2) +
                Math.cos(phi1) * Math.cos(phi2) *
                        Math.sin(deltaLambda / 2) * Math.sin(deltaLambda / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return R * c;
    }

    // MÉTODO PARA REGISTRAR MOVIMIENTOS BRUSCOS DETECTADOS POR EL ACELERÓMETRO
    public void registerFastMovement() {
        fastMovements++;
    }

    // GETTERS PARA OBTENER LAS ESTADÍSTICAS DE LA ACTIVIDAD
    public double getTotalDistance() {
        return totalDistance;
    }

    public double getMaxSpeed() {
        return maxSpeed;
    }

    public int getFastMovements() {
        return fastMovements;
    }
}
