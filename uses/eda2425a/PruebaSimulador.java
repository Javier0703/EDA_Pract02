package eda2425a;

public class PruebaSimulador{
    public static void main(String[] args) {
        int n = 10000;  
        int iteraciones = 10;

        // Crea una instancia del SimuladorExt
        SimuladorExt sim = new SimuladorExt(n);
        System.out.println("Simulación con " + n + " gotículas:");
        System.out.println("Número de iteraciones: " + iteraciones);
        System.out.println("----------------------------------------");

        // Ejecuta múltiples pasos de simulación
        long totalNOPER = 0;
        double totalTime = 0;

        for (int i = 0; i < iteraciones; i++) {
            sim.PasoSimulacion();  // Ejecuta un paso
            totalNOPER += Simulador.NOPER;  // Suma el número de operaciones
            totalTime += sim.tpo;  // Suma el tiempo de ejecución de este paso
        }

        // Calcula promedios
        double promedioNOPER = totalNOPER / (double) iteraciones;
        double promedioTiempo = totalTime / iteraciones;

        // Resultados
        System.out.println("Promedio de operaciones (NOPER): " + promedioNOPER);
        System.out.println("Promedio de tiempo por iteración (ms): " + promedioTiempo);
    }
}

