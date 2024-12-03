package eda2425a;
import java.util.*;

public class SimuladorExt extends Simulador {

    // Grid dimensions
    private int numCellsX, numCellsY;
    private float cellSizeX, cellSizeY;
    private List<Goticula>[][] grid;

    public SimuladorExt(int n) {
        super(n);
        // Initialize grid based on lx and ly
        cellSizeX = 1.0f; // Adjust based on lx and n
        cellSizeY = 1.0f; // Adjust based on ly and n
        numCellsX = (int) Math.ceil(lx / cellSizeX);
        numCellsY = (int) Math.ceil(ly / cellSizeY);
        grid = new List[numCellsX][numCellsY];
        for (int i = 0; i < numCellsX; i++) {
            for (int j = 0; j < numCellsY; j++) {
                grid[i][j] = new ArrayList<>();
            }
        }
    }

    @Override
    protected void ReestructuraED() {
        // Clear the grid
        for (int i = 0; i < numCellsX; i++) {
            for (int j = 0; j < numCellsY; j++) {
                grid[i][j].clear();
            }
        }
        // Assign each droplet to its cell
        for (Goticula g : gotas) {
            int cellX = (int) (g.x / cellSizeX);
            int cellY = (int) (g.y / cellSizeY);
            if (cellX < 0) cellX = 0;
            if (cellX >= numCellsX) cellX = numCellsX - 1;
            if (cellY < 0) cellY = 0;
            if (cellY >= numCellsY) cellY = numCellsY - 1;
            grid[cellX][cellY].add(g);
        }
    }

    @Override
    protected float CalcDensidad(float x, float y) {
        float densidad = 0;
        // Determine which cells to consider
        int cellX = (int) (x / cellSizeX);
        int cellY = (int) (y / cellSizeY);
        // Iterate over neighboring cells
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                int neighborX = cellX + dx;
                int neighborY = cellY + dy;
                if (neighborX >= 0 && neighborX < numCellsX && neighborY >= 0 && neighborY < numCellsY) {
                    for (Goticula g : grid[neighborX][neighborY]) {
                        densidad += CalcDensidadIter(x, y, g);
                    }
                }
            }
        }
        return densidad;
    }

    @Override
    protected VecXY CalcPresion(Goticula gi) {
        VecXY f = new VecXY(0, 0);
        // Determine which cells to consider
        int cellX = (int) (gi.xa / cellSizeX);
        int cellY = (int) (gi.ya / cellSizeY);
        // Iterate over neighboring cells
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                int neighborX = cellX + dx;
                int neighborY = cellY + dy;
                if (neighborX >= 0 && neighborX < numCellsX && neighborY >= 0 && neighborY < numCellsY) {
                    for (Goticula gj : grid[neighborX][neighborY]) {
                        if (gi != gj) {
                            f.Add(CalcPresionIter(gi, gj));
                        }
                    }
                }
            }
        }
        f.Scale(1 / gi.d);
        return f;
    }

    @Override
    protected VecXY CalcViscosidad(Goticula gi) {
        VecXY f = new VecXY(0, 0);
        // Determine which cells to consider
        int cellX = (int) (gi.xa / cellSizeX);
        int cellY = (int) (gi.ya / cellSizeY);
        // Iterate over neighboring cells
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                int neighborX = cellX + dx;
                int neighborY = cellY + dy;
                if (neighborX >= 0 && neighborX < numCellsX && neighborY >= 0 && neighborY < numCellsY) {
                    for (Goticula gj : grid[neighborX][neighborY]) {
                        if (gi != gj) {
                            f.Add(CalcViscosidadIter(gi, gj));
                        }
                    }
                }
            }
        }
        return f;
    }
}