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
        int n = 10000; 
        SimuladorExt sim = new SimuladorExt(n); 
        for(int i = 0; i < 600; i++) {sim.PasoSimulacion();	}
        javax.swing.SwingUtilities.invokeLater(() -> { new GUIPtos(sim);});
    }
}