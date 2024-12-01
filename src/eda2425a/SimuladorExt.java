/**
 * Copyright Universidad de Valladolid 2024
 */

package eda2425a;
import java.util.*;

/**
 * Implementacion de SimuladorExt
 * @author javcalv
 *
 */

public class SimuladorExt extends Simulador {
    private GridHash grid;  // Instancia de la cuadrícula hash

    public SimuladorExt(int n) {
        super(n);
        this.grid = new GridHash(1.0f);  // Tamaño de celda ajustable (1.0f es un buen inicio)
        ReestructuraED();
    }

    // Sobrescribimos ReestructuraED para reorganizar las gotículas en la cuadrícula (a nuestro tamaño)
    @Override
    protected void ReestructuraED() {
        grid.clear();
        for (Goticula g : gotas) {
            grid.add(g);
        }
    }

    // Optimización de CalcDensidad para usar vecinos cercanos
    @Override
    protected float CalcDensidad(float x, float y) {
        float densidad = 0;
        List<Goticula> vecinos = grid.getNeighbors(x, y);  // Solo gotículas cercanas
        for (Goticula g : vecinos) {
            densidad += CalcDensidadIter(x, y, g);
        }
        return densidad;
    }

    // Optimización de CalcPresion
    @Override
    protected VecXY CalcPresion(Goticula gi) {
        VecXY f = new VecXY(0, 0);
        List<Goticula> vecinos = grid.getNeighbors(gi.x, gi.y);  // Solo vecinos cercanos
        for (Goticula gj : vecinos) {
            f.Add(CalcPresionIter(gi, gj));
        }
        f.Scale(1 / gi.d);
        return f;
    }

    // Optimización de CalcViscosidad
    @Override
    protected VecXY CalcViscosidad(Goticula gi) {
        VecXY f = new VecXY(0, 0);
        List<Goticula> vecinos = grid.getNeighbors(gi.x, gi.y);  // Solo vecinos cercanos
        for (Goticula gj : vecinos) {
            f.Add(CalcViscosidadIter(gi, gj));
        }
        return f;
    }
}
