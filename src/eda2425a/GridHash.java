/**
 * Copyright Universidad de Valladolid 2024
 */
package eda2425a;
import java.util.*;

/**
 * Implementación de la clase GridHash
 * Clase que maneja el tamaño de cada una de las celdas
 * ahora incluye filtrado de vecinos y mayor flexibilidad
 * @author javcalv
 */

public class GridHash {
    // Tamaño de cada celda (puedes ajustarlo según resultados)
    private final float cellSize;  
    // HashMap para celdas
    private final Map<Integer, List<Simulador.Goticula>> grid;

    public GridHash(float cellSize) {
        this.cellSize = cellSize;
        this.grid = new HashMap<>();
    }

    // Calcula el índice hash de una celda en base a la posición (x, y)
    private int hash(float x, float y) {
        int cx = (int) Math.floor(x / cellSize);
        int cy = (int) Math.floor(y / cellSize);
        return Objects.hash(cx, cy);
    }

    // Agrega una gotícula a su celda correspondiente
    public void add(Simulador.Goticula g) {
        int key = hash(g.x, g.y);
        grid.computeIfAbsent(key, k -> new ArrayList<>()).add(g);
    }

    // Elimina una gotícula de su celda
    public void remove(Simulador.Goticula g) {
        int key = hash(g.x, g.y);
        List<Simulador.Goticula> cell = grid.get(key);
        if (cell != null) {
            cell.remove(g);
            if (cell.isEmpty()) grid.remove(key);  // Limpia celdas vacías
        }
    }

    // Obtiene las gotículas cercanas a una posición
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

    // Obtiene los vecinos filtrados que están dentro del radio de influencia
    public List<Simulador.Goticula> getFilteredNeighbors(float x, float y) {
        List<Simulador.Goticula> neighbors = getNeighbors(x, y);  // Vecinos iniciales
        // Filtrar por distancia euclidiana <= 1
        neighbors.removeIf(g -> (g.x - x) * (g.x - x) + (g.y - y) * (g.y - y) > 1);
        return neighbors;
    }

    // Limpia la cuadrícula para reorganizarla
    public void clear() {
        grid.clear();
    }
}