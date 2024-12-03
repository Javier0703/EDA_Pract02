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
        long totalNOPER = 0;
        double totalTime = 0;

        for (int i = 0; i < iteraciones; i++) {
            sim.PasoSimulacion();  // Ejecuta un paso
            totalNOPER += Simulador.NOPER;  // Suma el n�mero de operaciones
            totalTime += sim.tpo;  // Suma el tiempo de ejecuci�n de este paso
        }

        // Calcula promedios
        long promedioNOPER = totalNOPER / iteraciones;
        double promedioTiempo = totalTime / iteraciones;

        // Resultados
        System.out.println("Promedio de operaciones (NOPER): " + promedioNOPER);
        System.out.println("Promedio de tiempo por iteraci�n (ms): " + promedioTiempo);
    }
}