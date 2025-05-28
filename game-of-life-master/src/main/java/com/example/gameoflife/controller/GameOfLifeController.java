package com.example.gameoflife.controller;

import com.example.gameoflife.model.Board;
import com.example.gameoflife.model.Cell;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.sql.Connection;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import com.example.gameoflife.controller.database;
@RestController
@RequestMapping("/api/life")
public class GameOfLifeController {
    database d=new database();
    Connection c=d.getConnection();
    static int id=-1;
    int step=1;
    @GetMapping("/time")
    public String getCurrentTime() {
        LocalDateTime now = LocalDateTime.now();
        return now.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }

    @PostMapping("/next")
    public boolean[][] getNextGeneration(@RequestBody boolean[][] inputGrid) {
        if (inputGrid == null || inputGrid.length == 0 || inputGrid[0].length == 0) {
            throw new InvalidBoardException("Grid must be a non-empty 2D boolean array");
        }

        int height = inputGrid.length;
        int width = inputGrid[0].length;

        // Create and initialize board
        Board board = new Board(width, height);
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                board.setCellState(x, y, inputGrid[y][x]);
            }
        }
        if(id==-1){
            id=database.startInsert(c,inputGrid);
        }
        database.matrixInsert(c,id,step++,inputGrid);
        board.stepForward();

        // Return a new generation as boolean[][]
        boolean[][] result = new boolean[height][width];
        Cell[][] newGrid = board.getGrid();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                result[y][x] = newGrid[y][x].isCurrent();
            }
        }

        return result;
    }
    @GetMapping("/grid")
    public boolean[][] getGrid(@RequestBody Map<String, Integer> request) {
        return database.selectStep(c,request.get("id"),request.get("step"));
    }
    // Handle invalid input
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InvalidBoardException.class)
    public String handleInvalidBoard(InvalidBoardException ex) {
        return ex.getMessage();
    }

    // Handle any unexpected exceptions
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public String handleGeneralError(Exception ex) {
        return "An unexpected error occurred: " + ex.getMessage();
    }

    // Custom exception
    static class InvalidBoardException extends RuntimeException {
        public InvalidBoardException(String message) {
            super(message);
        }
    }
}
