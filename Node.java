/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package a_star_revert;

/**
 *
 * @author MJHobby
 * @date 04/27/2017
 * @title Node: Stores variables associated with grid node properties and calculates 
 *         the heuristic values for the node
 */
public class Node {
 
    private int heuristicCost = 0; //Heuristic cost (H(n))
    private int finalCost = 0; //G+H
    private int movementCost = 0;
    private int x, y;
    private boolean isGoal;
    private Node parent; 
        
    Node(int x, int y, boolean goal){
        this.x = x;
        this.y = y;
        this.isGoal = goal;
    }
        
    @Override
    public String toString(){
        return "(" + this.x + ", " + this.y + ")";
    }
    
    public int getX(){
        return this.x;
    }
    
    public int getY(){
        return this.y;
    }
    
    public boolean isGoal(){
        return this.isGoal;
    }
    
    public void setParent(Node node){
        parent = node;
    }
    
    public Node getParent(){
        return parent;
    }
    
    public void setFinalCost(int cost){
        finalCost = cost;
    }
    
    public int getFinalCost(){
        return finalCost;
    }
    
    public void setMovementCost(int cost){
        movementCost = cost;
    }
    
    public int getMovementCost(){
        return movementCost;
    }
    
    public void setHeuristic(int endX, int endY){
        this.heuristicCost = (Math.abs(x - endX) + Math.abs(y - endY)) * 10;
    }
    
    public int getHeuristic(){
        return heuristicCost;
    }
}