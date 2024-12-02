/**
 * Copyright Universidad de Valladolid 2024
 */
package eda2425a;
/**
 * Implementacion visual del estado de nuestras goticulas empleando la nueva
 * estructura de datos.
 * @author javcalv
 */
public class Visualizador {
    public static void main(String[] args) {
    	// Numero de goticulas
        int n = 10000; 
        // Simulamos el programa con nuestro simulador
        SimuladorExt sim = new SimuladorExt(n); 
        //Realizamos un paso de 600 PasoSimulacion e instanciamos la interfaz.
        for(int i = 0; i < 600; i++) {sim.PasoSimulacion();	}
        javax.swing.SwingUtilities.invokeLater(() -> { new GUIPtos(sim);});
    }
}