package eda2425a;

public class PruebaSimulador{
    public static void main(String[] args) {
        int n = 10000;  
        int iteraciones = 10;

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
        double promedioNOPER = totalNOPER / (double) iteraciones;
        double promedioTiempo = totalTime / iteraciones;

        // Resultados
        System.out.println("Promedio de operaciones (NOPER): " + promedioNOPER);
        System.out.println("Promedio de tiempo por iteraci�n (ms): " + promedioTiempo);
    }
}

