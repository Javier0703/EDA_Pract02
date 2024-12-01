/**
 * Copyright Universidad de Valladolid 2024
 */
package eda2425a;
import java.util.*;

/**
 * Implementacion de la clase GridHash
 * Clase que maneja el tama�o de cada una de las celdas
 * @author javcalv
 */

public class GridHash {
    private final float cellSize;  // Tama�o de cada celda (puedes ajustarlo seg�n resultados)
    private final Map<Integer, List<Simulador.Goticula>> grid;  // HashMap para celdas

    public GridHash(float cellSize) {
        this.cellSize = cellSize;
        this.grid = new HashMap<>();
    }

    // Calcula el �ndice hash de una celda en base a la posici�n (x, y)
    private int hash(float x, float y) {
        int cx = (int) Math.floor(x / cellSize);
        int cy = (int) Math.floor(y / cellSize);
        return Objects.hash(cx, cy);
    }

    // Agrega una got�cula a su celda correspondiente
    public void add(Simulador.Goticula g) {
        int key = hash(g.x, g.y);
        grid.computeIfAbsent(key, k -> new ArrayList<>()).add(g);
    }

    // Elimina una got�cula de su celda
    public void remove(Simulador.Goticula g) {
        int key = hash(g.x, g.y);
        List<Simulador.Goticula> cell = grid.get(key);
        if (cell != null) {
            cell.remove(g);
            if (cell.isEmpty()) grid.remove(key);  // Limpia celdas vac�as
        }
    }

    // Obtiene las got�culas cercanas a una posici�n
    public List<Simulador.Goticula> getNeighbors(float x, float y) {
        List<Simulador.Goticula> neighbors = new ArrayList<>();
        int cx = (int) Math.floor(x / cellSize);
        int cy = (int) Math.floor(y / cellSize);

        // Recorre las celdas vecinas
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                int key = Objects.hash(cx + dx, cy + dy);
                List<Simulador.Goticula> cell = grid.get(key);
                if (cell != null) neighbors.addAll(cell);
            }
        }
        return neighbors;
    }

    // Limpia la cuadr�cula para reorganizarla
    public void clear() {
        grid.clear();
    }
}