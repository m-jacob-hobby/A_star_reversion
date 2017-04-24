/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package a_star_revert;

import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.util.PriorityQueue;
import java.util.ArrayList;

/**
 *
 * @author MJHobby
 */
public class A_Star_App extends JFrame {
// for heuristic cost calculations
    public static final int DIAGONAL_COST = 14;
    public static final int VERT_HORIZ_COST = 10;
    public static final int WIDTH = 10;
    public static final int HEIGHT = 10;

    
    public static void setOpen(Settings appSettings){
        int[] startCoords = appSettings.getStart();
        int[] endCoords = appSettings.getEnd();
        Node current;
        appSettings.getOpenSet().add(appSettings.getGrid()[startCoords[0]][startCoords[1]]);
        
        while(true){ 
            current = appSettings.getOpenSet().poll();
            if(current == null)break;
            appSettings.getClosedSet()[current.getX()][current.getY()]=true; 

            if(current.equals(appSettings.getGrid()[endCoords[0]][endCoords[1]])){
                return; 
            } 

            Node n;  
            if(current.getX() - 1 >= 0){
                n = appSettings.getGrid()[current.getX() - 1][current.getY()];
                findAndSetCosts(current, n, current.finalCost+VERT_HORIZ_COST, appSettings); 

                if(current.getY() - 1 >= 0){ //current.y-1 >= 0){                      
                    n = appSettings.getGrid()[current.getX() - 1][current.getY() - 1];
                    findAndSetCosts(current, n, current.finalCost+DIAGONAL_COST, appSettings); 
                }

                if(current.getY() + 1 < WIDTH){ //current.y+1 < grid[0].length){
                    n =appSettings.getGrid()[current.getX() - 1][current.getY() + 1];
                    findAndSetCosts(current, n, current.finalCost+DIAGONAL_COST, appSettings); 
                }
            } 

            if(current.getY() - 1 >= 0){//current.y-1 >= 0){
                n = appSettings.getGrid()[current.getX()][current.getY() - 1];
                findAndSetCosts(current, n, current.finalCost+VERT_HORIZ_COST, appSettings); 
            }

            if(current.getY() + 1 < WIDTH){//current.y+1 < grid[0].length){
                n = appSettings.getGrid()[current.getX()][current.getY() + 1];
                findAndSetCosts(current, n, current.finalCost+VERT_HORIZ_COST, appSettings); 
            }

            if(current.getX() + 1 < WIDTH){ //current.x+1 < grid.length){
                n = appSettings.getGrid()[current.getX() + 1][current.getY()];
                findAndSetCosts(current, n, current.finalCost+VERT_HORIZ_COST, appSettings); 

                if(current.getY() - 1 >= 0){//current.y-1 >= 0){
                    n = appSettings.getGrid()[current.getX() + 1][current.getY() - 1];
                    findAndSetCosts(current, n, current.finalCost+DIAGONAL_COST, appSettings); 
                }
                
                if(current.getY() + 1 < WIDTH){ //current.y+1 < grid[0].length){
                   n = appSettings.getGrid()[current.getX() + 1][current.getY() + 1];
                    findAndSetCosts(current, n, current.finalCost+DIAGONAL_COST, appSettings); 
                }  
            }
        } 
    }
    
    /*
        @params:
            current: current node being tested
            t: the next node of the path
            cost: the cost of total travel to current
        
    */
    static void findAndSetCosts(Node current, Node t, int cost, Settings appSettings){
        // Add to closed if null node
        if(t == null || appSettings.getClosedSet()[t.getX()][t.getY()])return;
        
        // Get cost and find if node in open set
        int tFinalCost = t.heuristicCost + cost;
        boolean inOpen = appSettings.getOpenSet().contains(t);
        
        if(!inOpen || tFinalCost < t.finalCost){
            t.finalCost = tFinalCost;
            t.parent = current;
            if(!inOpen)appSettings.getOpenSet().add(t);
        }
    }
    
    /*
        params:
            @width = width of board
            @height = height of board
            @walls = array of x and y coordinates of walls to be placed on board
        Output debug map of grid
    */
    public static void run(Settings appSettings){ // int[] walls
            int[] startCoords = appSettings.getStart();
            int[] endCoords = appSettings.getEnd();
           
           for(int x=0 ; x < WIDTH ; ++x){
              for(int y=0 ; y < HEIGHT ; ++y){
                  appSettings.getGrid()[x][y] = new Node(x, y, false);
                  appSettings.getGrid()[x][y].setHeuristic(endCoords[0], endCoords[1]);
              }
           }
           appSettings.getGrid()[startCoords[0]][startCoords[1]].finalCost = 0;
           
           /*
             Set walls cells. Simply set the cell values to null
             for walls cells.
           */
           for(int i=0; i < appSettings.getWallList().size() ; i+=2){
               int x = appSettings.getWallList().get(i);                                            //walls[i];
               int y = appSettings.getWallList().get(i + 1);                                        //walls[i+1];
               appSettings.getGrid()[x][y] = null;
           }
           
           setOpen(appSettings);
           
           // For DEBUGGING: view map with values, start, end, and walls in console
           System.out.println("Key: ");
           System.out.println(" O  ... Computer start position");
           System.out.println(" X  ... Goal position");
           System.out.println("/// ... Wall");
           System.out.println(" #  ... Node cost");
           //Display initial map
           System.out.println("\nGrid: ");
            for(int x=0; x < WIDTH ;++x){
                for(int y=0; y < HEIGHT ;++y){
                   if(x == startCoords[0] && y == startCoords[1])System.out.print(" O  ");        //Start point
                   else if(x == endCoords[0] && y == endCoords[1])System.out.print(" X  ");     //Goal
                   else if(appSettings.getGrid()[x][y]!=null)System.out.printf("%-3d ", appSettings.getGrid()[x][y].finalCost);
                   else System.out.print("/// "); 
                }
                System.out.println();
            } 
            System.out.println();
            
            // For DEBUGGING: Outputs the path from end to start that is the shortest traversable path
            int pathCounter = 1;
            if(appSettings.getClosedSet()[endCoords[0]][endCoords[1]]){
               //Trace back the path 
                System.out.println("Computer's Path: ");
                Node current = appSettings.getGrid()[endCoords[0]][endCoords[1]];
                //path.push(current);
                appSettings.getNavigation().addCoordinates(endCoords[0], endCoords[1]);
                System.out.println("1. " + current);
                while(current.parent!=null){
                    pathCounter++;
                    System.out.println(pathCounter + ". " +current.parent);
                    current = current.parent;
                    appSettings.getNavigation().addCoordinates(current.getX(), current.getY());
                } 
                appSettings.getNavigation().close();
                System.out.println();
            }else System.out.println("No possible path!");
           
    }


    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        StartupPanel initialize = new StartupPanel(HEIGHT, WIDTH);              // Builds the initial panel. This panel will prompt
        initialize.createFrame();                                               // the user to determine start, goal and all wall blocks        
        while(!initialize.getIsFinished()){                                     // that will be utilized. Runs until finished and won't        
            if (!initialize.getWait())                                          // setFrame until after user selects next block.
                initialize.setFrame();
        }

        // After the initial setup panel is complete, we transfer and set the values gathered from setup into an Settings object
        Settings appSettings = new Settings(WIDTH, HEIGHT, initialize.getStartX(), initialize.getStartY(), initialize.getGoalX(), initialize.getGoalY(), initialize.getWalls());
        
        run(appSettings);
        
        PathDisplayPanel displayPath = new PathDisplayPanel(WIDTH, HEIGHT, appSettings);
        
        displayPath.buildPanel(initialize);
        //A_Star_App d = new A_Star_App(appSettings);
        
    }
    
}
