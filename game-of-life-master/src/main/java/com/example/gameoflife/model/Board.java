package com.example.gameoflife.model;

/**
 * Board class that represents the Game of Life grid.
 * Holds a 2D array of Cell objects and manages the logic for evolution.
 */
public class Board {
    private final int width;
    private final int height;
    private final Cell[][] grid;

    /**
     * Constructor initializes the board with given dimensions and dead cells.
     */
    public Board(int width, int height) {
        this.width = width;
        this.height = height;
        this.grid = new Cell[height][width];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                grid[y][x] = new Cell(x,y,false); // All cells initially dead
            }
        }
    }

    /**
     * Set a specific cell to be alive or dead manually (used to initialize a pattern).
     */
    public void setCellState(int x, int y, boolean isAlive) {
        if (isInBounds(x, y)) {
            grid[y][x].setCurrent(isAlive);
        }
    }

    /**
     * Calculates the number of live neighbors for each cell and stores it in the cell.
     */
    public void calculateNeighbors() {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int count = countLiveNeighbors(x, y);
                grid[y][x].setLiveNeighbors(count);
            }
        }
    }

    /**
     * Count how many live neighbors a cell has.
     */
    private int countLiveNeighbors(int x, int y) {
        int count = 0;
        for (int dy = -1; dy <= 1; dy++) {
            for (int dx = -1; dx <= 1; dx++) {
                if (dx == 0 && dy == 0) continue; // Skip the cell itself

                int nx = x + dx;
                int ny = y + dy;

                if (isInBounds(nx, ny) && grid[ny][nx].isCurrent()) {
                    count++;
                }
            }
        }
        return count;
    }

    /**
     * Apply Game of Life rules to compute the next state for each cell.
     */
    public void calculateNextGeneration() {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Cell cell = grid[y][x];
                calculateNeighbors();
                int neighbors = cell.getLiveNeighbors();

                if (cell.isCurrent()) {
                    cell.setNext(neighbors == 2 || neighbors == 3);
                } else {
                    cell.setNext(neighbors == 3);
                }
            }
        }
    }

    /**
     * Advance the board to the next generation.
     */
    public void stepForward() {
        calculateNextGeneration();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Cell c=grid[x][y];
                c.setCurrent(c.isNext());
            }
        }

    }

    /**
     * Get the current board state.
     */
    public Cell[][] getGrid() {

        return grid;
    }

    /**
     * Utility: check if a coordinate is within board bounds.
     */
    private boolean isInBounds(int x, int y) {
        return x >= 0 && x < width && y >= 0 && y < height;
    }
}
