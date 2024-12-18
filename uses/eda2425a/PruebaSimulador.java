/**
 * Copyright Universidad de Valladolid 2024
 */
package eda2425a;

/**
 * Implementacion del nuevo SimuladorExt donde solo se veran
 * el numero de operaciones realizadas y el tiempo que realiza una
 * simulacion de n Goticulas (10.000, ya que es el idoneo)
 * @author Javier
 *
 */
public class PruebaSimulador{
    public static void main(String[] args) {
        int n = 10000;  
        int iteraciones = 100;

        // Crea una instancia del SimuladorExt
        SimuladorExt sim = new SimuladorExt(n);
        System.out.println("Simulaci�n con " + n + " got�culas:");
        System.out.println("N�mero de iteraciones: " + iteraciones);
        System.out.println("----------------------------------------");

        // Ejecuta m�ltiples pasos de simulaci�n
        long NOPER = 0;
        double tiempoTotal = 0;

        for (int i = 0; i < iteraciones; i++) {
            sim.PasoSimulacion();
            NOPER += Simulador.NOPER;
            tiempoTotal += sim.tpo;
        }

        // Calcula promedios
        long promedioNOPER = NOPER / iteraciones;
        double promedioTiempo = tiempoTotal / iteraciones;

        // Resultados
        System.out.println("Promedio de operaciones (NOPER): " + promedioNOPER);
        System.out.println("Promedio de tiempo por iteraci�n (ms): " + promedioTiempo);
    }
}