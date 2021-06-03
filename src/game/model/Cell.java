package game.model;

public class Cell {
    private final int x, y;
    private final char obj;
    private boolean visited, scanned;
    private boolean path, goal;
    private Cell parent;
    

    public Cell(int x, int y, char obj) {
        this.x = x;
        this.y = y;
        this.obj = obj;
        visited = false;
        scanned = false;
        path = false;
        parent = null;
        goal = false;
    }

    public int getPosX() {
        return x;
    }

    public int getPosY() {
        return y;
    }

    public char getObj() {
        return obj;
    }

    public boolean isVisited() {
        return !visited;
    }

    public boolean isScanned() {
        return scanned;
    }

    public boolean isPath() {
        return path;
    }

    public boolean isGoal () {
        return !goal;
    }

    public Cell getParent() {
        return parent;
    }

    public void setVisited(boolean b) {
        visited = b;
    }

    public void setScanned(boolean b) {
        scanned = b;
    }

    public void setPath(boolean b) {
        path = b;
    }

    public void setParent(Cell p) {
        parent = p;
    }

    public void setGoal(boolean b) {
        goal = b;
    }
}