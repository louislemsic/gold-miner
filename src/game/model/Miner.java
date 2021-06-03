package game.model;

import game.controller.BoardController;
import javafx.util.Pair;

import java.util.*;

public class Miner {

    private int x, y;
    private char dir;
    private int dirX, dirY;
    private char onObj;
    private final Board memory;
    private boolean isAlive, hasWon;
    private int goldDist;
    private int nMoves;
    private int nRotates;
    private int nScans;
    private int nRotateStreak;
    boolean impoExit;

    private final int SCAN = 0;
    private final int MOVE = 1;
    private final int ROTATE = 2;    

    ArrayList<Integer> queueAnimation;

    public Miner(int size) {
        this.x = 0;
        this.y = 0;

        this.dir = 'E';

        this.dirX = x + 1;

        onObj = 'O';

        memory = new Board(size);
        for(int i = 0; i < size; i++) {
            for(int j = 0; j < size; j++) {
                memory.setCell(new Cell(i, j, 'O')); 
            }
        }
        memory.setCell(new Cell(size - 1, size - 1, 'O'));

        isAlive = true;
        hasWon = false;
        impoExit = false;

        goldDist = 0;

        // TODO Test
        queueAnimation = new ArrayList<>();
        nMoves = 0;
        nRotates = 0;
        nScans = 0;
        nRotateStreak = 0;
    }

    public void updateDir() {
        // TODO Test
        switch(dir) {
            case 'N': dirX = x; dirY = y - 1; break; 
            case 'E': dirX = x + 1; dirY = y; break; 
            case 'S': dirX = x; dirY = y + 1; break; 
            case 'W': dirX = x - 1; dirY = y; break;
        }
    }

    public void checkObj(Board board) {
        if(onObj == 'P') {
            isAlive = false;
        } else if(onObj == 'G') {
            hasWon = true;
        } else if(onObj == 'B') {

            Cell g = board.getGold();
            int m = 0;

            // TODO Test
            if(x == g.getPosX()) {
                m = Math.abs(this.y - g.getPosY());
            } else if (y == g.getPosY()) {
                m = Math.abs(this.y - g.getPosY());
            }

            // TODO Test
            if(m > 0 && goldDist == 0) {
                goldDist = m;
            }
        }
            
        memory.setCell(board.getCell(x, y));
    }

    public boolean adjIsScanned() {
        // TODO Test
        if(memory.isValidCoord(x, y - 1) && !memory.getCell(x, y - 1).isScanned() && memory.getCell(x, y - 1).isVisited())
            return true;
        if(memory.isValidCoord(x + 1, y) && !memory.getCell(x + 1, y).isScanned() && memory.getCell(x + 1, y).isVisited())
            return true;
        if(memory.isValidCoord(x, y + 1) && !memory.getCell(x, y + 1).isScanned() && memory.getCell(x, y + 1).isVisited())
            return true;
        return memory.isValidCoord(x - 1, y) && !memory.getCell(x - 1, y).isScanned() && memory.getCell(x - 1, y).isVisited();
    }

    public void rotate() {
        switch(dir) {
            case 'N': dir = 'E'; break;
            case 'E': dir = 'S'; break;
            case 'S': dir = 'W'; break;
            case 'W': dir = 'N'; break;
        }
        updateDir();
        movementTracker(ROTATE);
    }

    public void scan(Board board) {
        if(board.isValidCoord(dirX, dirY)) {
            movementTracker(SCAN);
            memory.setCell(board.getCell(dirX, dirY));
            memory.getCell(dirX, dirY).setScanned(true);
        }
    }

    public void move(Board board) {

        // TODO Test
        if(board.isValidCoord(dirX, dirY)) {
            nRotateStreak = 0;
            x = dirX;
            y = dirY;
            updateDir();
            onObj = board.getCell(x, y).getObj();
            checkObj(board);
            memory.setCell(board.getCell(x, y));
            memory.getCell(x, y).setScanned(true);
            memory.getCell(x, y).setVisited(true);
            movementTracker(MOVE); //success moves noted.
        }
    }

    public void randAlgo(Board board) {
        while(isAlive && !hasWon) {
            double rng = Math.random();
            if(rng < 0.33) {
                scan(board);
            } else if(rng < 0.66) {
                rotate();
            } else {
                move(board);
            }
        }
    }

    public void smartAlgo(Board board) {
        // Optimal search until all Adj cells have been visited
        // TODO Test
        boolean possibility = true;
        int possibilityCount = 0;

        while(!hasWon && possibility) {

            // Optimal Scan Path
            while(adjIsScanned()) {
                if( memory.isValidCoord(dirX, dirY) && !memory.getCell(dirX, dirY).isScanned()) {
                    scan(board);
                    // Check for Gold or Beacon
                    // TODO Break Gold Statements
                    if(memory.getCell(dirX, dirY).getObj() == 'G') {
                        move(board);
                        break;
                    }
                    if(memory.getCell(dirX, dirY).getObj() == 'B') {
                        move(board);
                        if(goldDist > 0) {
                            beaconAlgo(board);
                            break;
                        }
                    }
                }
                while(adjIsScanned()) {
                    while(!memory.isValidCoord(dirX, dirY) || memory.getCell(dirX, dirY).isScanned()) {
                        rotate();
                        nRotateStreak++;

                        if (nRotateStreak >= 8) {
                            impoExit = true;
                            break;
                        }
                    }
                    if (impoExit) {
                        break;
                    }
                    scan(board);
                    // Check for Gold or Beacon
                    if(memory.getCell(dirX, dirY).getObj() == 'G') {
                        move(board);
                        break;
                    }
                    if(memory.getCell(dirX, dirY).getObj() == 'B') {
                        move(board);  
                        if(goldDist > 0) {
                            beaconAlgo(board);
                            break;
                        }

                    }
                }
                if(hasWon) {
                    break;
                }
                if (impoExit) {
                    break;
                }
                if(memory.isValidCoord(dirX, dirY) && memory.getCell(dirX, dirY).getObj() != 'P') {
                    move(board);
                }
            }
            if(hasWon) {
                break;
            }
            if (impoExit) {
                break;
            }
            // Sets path to unvisited cell
            // TODO might not face goal cell so must rotate

            bfs();

            while((!memory.isValidCoord(dirX, dirY) || memory.getCell(dirX, dirY).isGoal()) && possibility) {

                while ((!memory.isValidCoord(dirX, dirY) || !(memory.getCell(dirX, dirY).getParent() == memory.getCell(x, y)) || !memory.getCell(dirX, dirY).isPath())){
                    rotate();
                    nRotateStreak++;
                    if(nRotateStreak >= 8) {
                        impoExit = true;
                        break;
                    }
                }
                if (impoExit) {
                    break;
                }

                if (memory.getCell(dirX, dirY).isGoal()) {
                    move(board);
                }
                if (hasWon)
                    break;

                if(nRotateStreak >= 8) {
                    impoExit = true;
                    break;
                }

                possibilityCount++;

                if (possibilityCount >= (board.getSize() * board.getSize())/2) {
                    possibility = false;
                    impoExit = true;
                }

            }

            if (hasWon)
                break;
            if (impoExit) {
                break;
            }
            bfsReset();

            if(nRotateStreak >= 8) {
                impoExit = true;
                break;
            }
        }
    }

    public void bfs() {
        Queue<Cell> q = new LinkedList<>();
        HashSet<Cell> seen = new HashSet<>();
        Cell curr = null;
        q.add(memory.getCell(x, y));
//        System.out.println("start: "+ memory.getCell(x, y).getPosX() + " " + memory.getCell(x, y).getPosY());

//        System.out.println("TEST: BFS");

        while(!q.isEmpty()) {
            curr = q.poll();

//            System.out.println("PATH MAKING: (" + curr.getPosX() + ", " + curr.getPosY() + ") OBJ: " + curr.getObj());
            if(!seen.contains(curr)) {
//                System.out.println("contains: "+ curr.getPosX() + " " + curr.getPosY() + " VISISTED: " + curr.isVisited());

                seen.add(curr);

                if(curr.isVisited() && !curr.isScanned())
                    break;
            }
            
            int xAdj, yAdj;
            xAdj = curr.getPosX();
            yAdj = curr.getPosY();

            // Add adjacent cells and keep track of parents
            if(memory.isValidCoord(xAdj, yAdj - 1) && memory.getCell(xAdj, yAdj - 1).getObj() != 'P' && !seen.contains(memory.getCell(xAdj, yAdj - 1))) {
                q.add(memory.getCell(xAdj, yAdj - 1));

                memory.getCell(xAdj, yAdj - 1).setParent(curr);
            }
            if(memory.isValidCoord(xAdj + 1, yAdj) && memory.getCell(xAdj + 1, yAdj).getObj() != 'P' && !seen.contains(memory.getCell(xAdj + 1, yAdj))) {
                q.add(memory.getCell(xAdj + 1, yAdj));

                memory.getCell(xAdj + 1, yAdj).setParent(curr);
            }
            if(memory.isValidCoord(xAdj, yAdj + 1)&& memory.getCell(xAdj, yAdj + 1).getObj() != 'P' && !seen.contains(memory.getCell(xAdj, yAdj + 1))) {
                q.add(memory.getCell(xAdj, yAdj + 1));

                memory.getCell(xAdj, yAdj + 1).setParent(curr);
            }
            if(memory.isValidCoord(xAdj - 1, yAdj) && memory.getCell(xAdj - 1, yAdj).getObj() != 'P' && !seen.contains(memory.getCell(xAdj - 1, yAdj))) {
                q.add(memory.getCell(xAdj - 1, yAdj));

                memory.getCell(xAdj - 1, yAdj).setParent(curr);
            }
        }

        // Create path by going through setting isPath to true from curr to cell with null parent
        assert curr != null;
        curr.setGoal(true);
//        System.out.println("END: (" + curr.getPosX() + ", " + curr.getPosY() + ") OBJ: " + curr.getObj() + " DIR:" + dir);
        while(!(curr == null)) {

            curr.setPath(true);
//            System.out.println("PATH MAKING: (" + curr.getPosX() + ", " + curr.getPosY() + ") OBJ: " + curr.getObj());
            curr = curr.getParent();
        }

    }

    public void bfsReset() {
        // TODO Test
        for(int i = 0; i < memory.getSize(); i++) {
            for(int j = 0; j < memory.getSize(); j++) {
                memory.getCell(i, j).setGoal(false);
                memory.getCell(i, j).setPath(false);
                memory.getCell(i, j).setParent(null);
            }
        }
    }

    public void beaconAlgo(Board board) {
        // Move in dir goldDist cells
        boolean found = false;
        int bX, bY;
        bX = x;
        bY = y;

        for(int i = 0; i < goldDist && !found; i++) {
            move(board);
            if(onObj == 'G') {
                found = true;
            }
        }

        // rotate 2x
        for(int i = 0; i < 2 && !found; i++) {
            rotate();
        }
        // Move in dir goldDist * 2 cells
        while(!found && x != bX && y != bY) {
            move(board);
        }

        for(int i = 0; i < goldDist && !found; i++) {
            move(board);
            if(onObj == 'G') {
                found = true;
                break;
            }
        }

        // rotate 2x
        for(int i = 0; i < 2 && !found; i++) {
            rotate();
        }

        // Move in dir goldDist cells
        while(!found && x != bX && y != bY) {
            move(board);
        }

        // rotate
        rotate();
        // Move in dir goldDist cells
        for(int i = 0; i < goldDist && !found; i++) {
            move(board);
            if(onObj == 'G') {
                found = true;
                break;
            }
        }

        // rotate 2x
        for(int i = 0; i < 2 && !found; i++) {
            rotate();
        }

        while(!found && x != bX && y != bY) {
            move(board);
        }

        // Move in dir goldDist * 2 cells
        for(int i = 0; i < goldDist && !found; i++) {
            move(board);
            if(onObj == 'G') {
                found = true;
            }
        }
    }

    public boolean getIsAlive(){
        return isAlive;
    }

    private void movementTracker(int index){

        switch (index){
            case MOVE:
                switch (dir) {
                    case 'N': queueAnimation.add(BoardController.MOVE_UP);
                        break;
                    case 'E': queueAnimation.add(BoardController.MOVE_RIGHT);
                        break;
                    case 'S': queueAnimation.add(BoardController.MOVE_DOWN);
                        break;
		            case 'W': queueAnimation.add(BoardController.MOVE_LEFT);
                        break;
                }
                nMoves++;
                break;
            case ROTATE:
                queueAnimation.add(BoardController.ROTATE_RIGHT);
                nRotates++;
                break;
            case SCAN:
                queueAnimation.add(BoardController.SCAN);
                nScans++;
                break;
        }
    }

    public int getTotalMoves(){
        return nMoves;
    }

    public int getTotalScans(){
        return nScans;
    }

    public int getTotalRotates(){
        return nRotates;
    }

    public ArrayList<Integer> getQueueAnimation(){
        return queueAnimation;
    }

    public boolean getImpoExit(){
        return impoExit;
    }

    public Pair<Integer,Integer> getFinalCoordinates(){
        return new Pair<>(x, y);
    }
}