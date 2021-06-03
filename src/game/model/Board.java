package game.model;

import java.util.*;

public class Board {

    private final int size;
    private final Cell[][] board;
    private Cell gold;
    private ArrayList<Cell> beacons;
    private final ArrayList<Cell> pits;

    public Board(int size) {
        // number of sides in board
        this.size = size;
        
        // sets size of board
        this.board = new Cell[size][size];

        // Initialize board with char 'O'
        for(int i = 0; i < size; i++) 
            for(int j = 0; j < size; j++)
                board[i][j] = new Cell(i, j, 'O');

        beacons = new ArrayList<>();
        pits = new ArrayList<>();
        gold = new Cell(size - 1, size - 1, 'G');
        setCell(gold);
    }

    // checks if coord is valid
    public boolean isValidCoord(int x, int y) {

        // checks if coord is not miner's starting coord
        // checks if coord is within bounds of size
        if((x >= 0 && x < size) && (y >= 0 && y < size)) {
            // checks if x and y are both equal to 0
            return x != 0 || y != 0;
        }
        return false;
    }

    public boolean isValidValidCoord(int x, int y) {

        // checks if coord is not miner's starting coord
        // checks if coord is within bounds of size
        if((x >= 0 && x < size) && (y >= 0 && y < size)) {
            // checks if x and y are both equal to 0
            if(x != 0 || y != 0) {
                // checks if coord is already occupied
                return board[x][y].getObj() == 'O';
            }
        }
        return false;
    }

    // Can only add one Gold as of right now
    // Cannot overwrite objects on board
    public boolean addGold(int x, int y) {
        if(isValidValidCoord(x, y)) {

            setCell(new Cell(gold.getPosX(), gold.getPosY(), 'O'));

            gold = new Cell(x, y, 'G'); //[edited] due to private Cell gold not being updated.
            setCell(gold);

            beacons = new ArrayList<>();
            return true;
        }
        else {
            return false;
        }
    }

    // Cannot overwrite objects on board
    public boolean addBeacon(int x, int y) {
        if(isValidValidCoord(x, y)) {

            Cell b = new Cell(x, y, 'B');
            beacons.add(b);
            setCell(b);

            return true;
        }
        else {
            return false;
        }
    }

    // Cannot overwrite objects on board
    public boolean addPit(int x, int y) {
        if(isValidValidCoord(x, y)) {

            Cell p = new Cell(x, y, 'P');
            pits.add(p);
            setCell(p);

            return true;
        }
        else {
            return false;
        }
    }

    // returns the char representation of an object at position (x, y)
    public Cell getCell(int x, int y) {
        return board[x][y];
    }

    // returns the gold obj in the board
    public Cell getGold() {
        return gold;
    }

    // returns the list of beacons
    public ArrayList<Cell> getBeacons() {
        return beacons;
    }

    // returns the list of pits
    public ArrayList<Cell> getPits() {
        return pits;
    }

    public int getSize() {
        return size;
    }

    // sets cell on board with an object. object could be gold, beacon, pit, or miner
    public void setCell(Cell obj) {
        board[obj.getPosX()][obj.getPosY()] = obj;
    }
}