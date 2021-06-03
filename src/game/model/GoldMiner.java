package game.model;

import javafx.util.Pair;

import java.util.ArrayList;


public class GoldMiner {

    private Board board; //the board
    private Miner miner; //the miner
    private int boardSize; //the NxN size of the board
    private boolean rationality; //Smart Miner or Random Miner (Smart -> true)
    private boolean win; //finished the game by finding gold or falling in a pit.
    private boolean impossibility;

    public GoldMiner() {

        //set default values
        this.boardSize = 8;
        impossibility = false;

        board = new Board(boardSize);
        miner = new Miner(boardSize);
    }

    public ArrayList<Integer> calculate(){

        //SMART MINER
        if (rationality)
            miner.smartAlgo(board); //checks the result of each move.
        //RANDOM MINER
        else
            miner.randAlgo(board); //checks the result of each move.

        win = miner.getIsAlive();

        impossibility = miner.getImpoExit();

        return miner.getQueueAnimation();
    }

    public void setRationality(boolean index){
        rationality = index;
    }

    public void setGridSize(int size){
        if(size >= 8 && size <= 64) {
            this.boardSize = size;
            reset();
        }
    }

    public boolean setGold(int x, int y){
        return board.addGold(x, y);
    }

    public boolean addBeacon(int x, int y){
        return board.addBeacon(x, y);
    }

    public boolean addPit(int x, int y){
        return board.addPit(x, y);
    }

    public void reset(){
        board = new Board(boardSize);
        miner = new Miner(boardSize);
    }

    public int getBoardSize(){
        return boardSize;
    }

    public Board getBoard(){
        return board;
    }

    public ArrayList<Integer> getStats(){
        ArrayList<Integer> stats = new ArrayList<>();

        //backup:
        stats.add(miner.getTotalMoves()); //1. Moves (Index: 0)
        stats.add(miner.getTotalScans()); //2. Scans (Index: 1)
        stats.add(miner.getTotalRotates()); //3. Rotations (Index: 2)

        return stats;
    }

    public boolean getWinResult(){
        return win;
    }

    public boolean getImpoExit(){
        return impossibility;
    }

    public Pair<Integer, Integer> getFinalCoordinates(){
        return miner.getFinalCoordinates();
    }
}