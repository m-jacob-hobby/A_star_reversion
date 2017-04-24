/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package a_star_revert;

import java.util.ArrayList;
import java.util.PriorityQueue;

/**
 *
 * @author MJHobby
 */
public class Settings {
    
    private PriorityQueue<Node> open;
    private boolean closed[][];
    private Node[][] grid;
    private ArrayList<Integer> wallList;
    
    private Path navigation;
    private Mover computer;
    
    private int width;
    private int height; // Game board width and height
    private int startX;
    private int startY; // Computer start position x and y coordinates
    private int endX, endY;
    private boolean play ,pause;
    
    public Settings(int boardWidth, int boardHeight, int startX, int startY, int goalX, int goalY, ArrayList<Integer> walls){
        this.width = boardWidth;
        this.height = boardHeight;
        setStart(startX, startY);
        setEnd(goalX, goalY);
        setWallList(walls);
        navigation = new Path();
        grid = new Node[width][height];
        setOpenSet();
        closed = new boolean[width][height];
    }
    
    public int getWidth(){
        return this.width;
    }
    
    public int getHeight(){
        return this.height;
    }
    
    public void setStart(int x, int y){
        this.startX = x;
        this.startY = y;
        this.computer = new Mover(this.startX, this.startY);
    }
    
    public int[] getStart(){
        int[] startCoords = {this.startX, this.startY};
        return startCoords;
    }
    
    public Mover getComputer(){
        return this.computer;
    }
    
    public void setEnd(int x, int y){
        this.endX = x;
        this.endY = y;
    }
    
    public int[] getEnd(){
        int[] endCoords = {this.endX, this.endY};
        return endCoords;
    }
    
    public void setWallList(ArrayList<Integer> walls){
        this.wallList = walls;
    }
    
    public ArrayList<Integer> getWallList(){
        return this.wallList;
    }
    
    public Node[][] getGrid(){
        return this.grid;
    }
    
    public void setOpenSet(){
        open = new PriorityQueue<>((Object o1, Object o2) -> {
                Node c1 = (Node)o1;
                Node c2 = (Node)o2;

                return c1.finalCost < c2.finalCost?-1:
                        c1.finalCost > c2.finalCost?1:0;
            }); 
    }
    
    public PriorityQueue<Node> getOpenSet(){
        return this.open;
    }
    
    public boolean[][] getClosedSet(){
        return this.closed;
    }
    
    public Path getNavigation(){
        return this.navigation;
    }
    
    
}
