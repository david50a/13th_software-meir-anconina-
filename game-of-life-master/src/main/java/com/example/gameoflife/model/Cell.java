package com.example.gameoflife.model;

/**
 * Represents a single cell in Conway's Game of Life.
 * Each cell has a position (x, y), a current state, a neighbor count, and a calculated next state.
 */
public class Cell {

    //region Fields

    private final int x; // X-coordinate of the cell in the grid
    private final int y; // Y-coordinate of the cell in the grid
    private boolean current; // true = alive, false = dead
    private int liveNeighbors; // Number of live neighbors around the cell
    private boolean next; // Calculated state for next generation

    //endregion

    //region Constructor

    /**
     * Creates a cell with position and initial state.
     * @param x Horizontal coordinate
     * @param y Vertical coordinate
     * @param current Initial alive/dead state
     */
    public Cell(int x, int y, boolean current) {
        this.x = x;
        this.y = y;
        this.current = current;
    }

    //endregion

    //region Getters and Setters

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean isCurrent() {
        return current;
    }

    public void setCurrent(boolean current) {
        this.current = current;
    }

    public int getLiveNeighbors() {
        return liveNeighbors;
    }

    public void setLiveNeighbors(int liveNeighbors) {
        this.liveNeighbors = liveNeighbors;
    }

    public boolean isNext() {
        return next;
    }

    public void setNext(boolean next) {
        this.next = next;
    }

    //endregion

    //region Debug / Utility

    /**
     * Useful for debugging or printing the board.
     */
    @Override
    public String toString() {
        return String.format("Cell(x=%d, y=%d, current=%s, neighbors=%d, next=%s)",
                x, y,
                current ? "alive" : "dead",
                liveNeighbors,
                next ? "alive" : "dead");
    }

    //endregion
}
