/**
 * Copyright Universidad de Valladolid 2024
 */
package eda2425a;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementacion de la clase SimuladorExt
 * En ella habra modificaciones de las clases de Simulador para una mejor estructuracion
 * de los datos
 * @author javcalv
 */
public class SimuladorExt extends Simulador {
	
	//Generamos un tam de celda de 1
    private static final float TAM_GRID = 1f;
    //Lista bidimensional de objetos Goticula que representa la cuadr�cula.
    private List<List<Goticula>> grid;
    //N�mero de filas y columnas en la cuadr�cula
    private int gridFilas;
    private int gridColumnas; 
    
    /**
     * Constructor del SimuladorExt. Inicializa la cuadricula como una lista de listas,
     * donde cada lista es una celda vacia
     * @param n numero de goticulas
     */
    public SimuladorExt(int n) {
        super(n);
        gridFilas = (int) Math.ceil(lx / TAM_GRID);
        gridColumnas = (int) Math.ceil(ly / TAM_GRID);
        grid = new ArrayList<>(gridFilas * gridColumnas);
        for (int i = 0; i < gridFilas * gridColumnas; i++) {
            grid.add(new ArrayList<>());
        }
    }
    
    @Override
    /**
     * Metodo para reestructurar los datos. Para ello vacia cada celda de la cuadricula, e inserta las 
     * got�culas en la cuadr�cula seg�n sus nuevas posiciones (x, y). 
     * Utiliza el m�todo getGridIndex(x, y) para determinar la celda correspondiente para cada got�cula.
     */
    protected void ReestructuraED() {
        for (List<Goticula> celda : grid) {
            celda.clear();
        }
        for (Goticula g : gotas) {
            int[] indices = getGridIndex(g.x, g.y);
            int fila = indices[0];
            int columna = indices[1];
            int indice = fila * gridColumnas + columna;
            if (fila >= 0 && fila < gridFilas && columna >= 0 && columna < gridColumnas) {
                grid.get(indice).add(g);
            }
        }
    }

    @Override
    /**
     * Metodo para calcular la densidad en un punto (x,y)
     * Obtiene los �ndices de las celdas vecinas utilizando getCeldasVecinas(x, y). 
     * Itera sobre las got�culas en esas celdas vecinas y realiza el c�lculo de densidad.
     */
    protected float CalcDensidad(float x, float y) {
        float densidad = 0;
        List<Integer> indicesVecinos = getCeldasVecinas(x, y);
        for (int indice : indicesVecinos) {
            for (Goticula g : grid.get(indice)) {
                densidad += CalcDensidadIter(x, y, g);
            }
        }
        return densidad;
    }

    @Override
    /**
     * Calcula la presi�n sobre la got�cula gi.
     * Obtiene los �ndices de las celdas vecinas de la posici�n actual de gi. 
     * Itera sobre las got�culas en esas celdas vecinas y realiza el c�lculo de presi�n.
     */
    protected VecXY CalcPresion(Goticula gi) {
        VecXY f = new VecXY(0, 0);
        List<Integer> indicesVecinos = getCeldasVecinas(gi.xa, gi.ya);
        for (int indice : indicesVecinos) {
            for (Goticula gj : grid.get(indice)) {
                if (gj != gi) {
                    f.Add(CalcPresionIter(gi, gj));
                }
            }
        }
        f.Scale(1 / gi.d);
        return f;
    }

    @Override
    /**
     * Calcula la viscosidad sobre la got�cula gi.
     * Obtiene los �ndices de las celdas vecinas de la posici�n actual de gi.
     * Itera sobre las got�culas en esas celdas vecinas y realiza el c�lculo de viscosidad.
     */
    protected VecXY CalcViscosidad(Goticula gi) {
        VecXY f = new VecXY(0, 0);
        List<Integer> indicesVecinos = getCeldasVecinas(gi.xa, gi.ya);
        for (int indice : indicesVecinos) {
            for (Goticula gj : grid.get(indice)) {
                if (gj != gi) {
                    f.Add(CalcViscosidadIter(gi, gj));
                }
            }
        }
        return f;
    }

    // Helper method to convert coordinates to grid indices
    private int[] getGridIndex(float x, float y) {
        int fila = (int) (x / TAM_GRID);
        int columna = (int) (y / TAM_GRID);
        return new int[]{fila, columna};
    }

    // Helper method to get neighboring celda indices
    private List<Integer> getCeldasVecinas(float x, float y) {
        List<Integer> vecinos = new ArrayList<>();
        int fila = (int) (x / TAM_GRID);
        int columna = (int) (y / TAM_GRID);
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                int vecinosFila = fila + dx;
                int vecinosColumna = columna + dy;
                if (vecinosFila >= 0 && vecinosFila < gridFilas &&
                    vecinosColumna >= 0 && vecinosColumna < gridColumnas) {
                    int indice = vecinosFila * gridColumnas + vecinosColumna;
                    vecinos.add(indice);
                }
            }
        }
        return vecinos;
    }
}