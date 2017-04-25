/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package a_star_revert;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

/**
 *
 * @author MJHobby
 */
public class StartupPanel {
    
    // <editor-fold desc="VARIABLES">
    
    // Our constants to calculate heuristic costs
    public static final int DIAGONAL_COST = 14;
    public static final int VERT_HORIZ_COST = 10;
    
    private int height, width, wallCount, startX, startY, goalX, goalY;
    private boolean noPath, setStart, setGoal, finished, wait;
    private String actionMessage;
    private ArrayList<Integer> walls = new ArrayList<Integer>();
    private JLabel currentAction = new JLabel();
    private JLabel wallCounter = new JLabel();
    private JFrame pane = new JFrame();
    
    private Settings appSettings;
    
    // </editor-fold>

    // CONSTRUCTOR
    StartupPanel(int h, int w){
        height = h;
        width = w;
        wallCount = h + w +(h+w)/2;
        setStart = false;
        setGoal = false;
        finished = false;
        wait = false;
        noPath = false;
    }
    
    // <editor-fold desc="JFRAME METHODS">
    
    // Sets up the initial JFrame and adds all of its components
    public void createFrame(){
        pane.setDefaultCloseOperation(pane.EXIT_ON_CLOSE);
        pane.getContentPane();
        pane.setLayout(new GridLayout(1, 2));
    }
    
    // Adds all of the initial components of the JFrame
    public void setFrame(){

        // <editor-fold desc="Declaring panels, buttons, and other JFrame components">
        // WE START BY DECLAREING PANELS
        JPanel grid = new JPanel(new GridLayout(height, width));                // will hold our grid of buttons to build walls
        JPanel updates = new JPanel(new GridLayout(2, 1));                      // will display our wall count and action commands
        JPanel boardPanel = new JPanel(new GridLayout(height, width));          // will draw our final board
        JPanel buttonPanel = new JPanel(new GridLayout(1, 4));                  // will hold all of our buttons
        JPanel mainPanel = new JPanel(new BorderLayout());                      // will contain initial, board, and button panels
        
        // DECLARE BUTTONS
        JButton nextButton = new JButton("Next");
        JButton buildButton = new JButton("BUILD");
        JButton resetButton = new JButton("RESET");
        
        // THIS WILL BUILD A HEIGHT X WIDTH GRID OF BUTTONS USER WILL USE TO SET WALLS
        for(int i = 0; i < height*width ; i++){
            JButton button = new JButton(Integer.toString(i%10) + ", " + Integer.toString(i/10));
            button.setBackground(null);
            button.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e) {
                    setWalls(button, buildButton);
                }
            });
            grid.add(button);
        }
        
        // SET OTHER COMPONENTS
        JLabel[][] board = new JLabel[height][width];
        wallCounter.setText("Walls left to build: " + wallCount);
        wallCounter.setHorizontalAlignment(SwingConstants.CENTER);
        currentAction.setText(actionMessage);
        currentAction.setFont(new Font("Serif", Font.BOLD, 16));
        currentAction.setHorizontalAlignment(SwingConstants.CENTER);
        
        // SET BUTTON PROPERTIES
        // Build button properties
        buildButton.setEnabled(false);
        buildButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                setIsFinished(true);
                pane.getContentPane().remove(grid);
                pane.getContentPane().remove(updates);
                pane.getContentPane().invalidate();
                pane.getContentPane().validate();
                pane.setExtendedState(JFrame.MAXIMIZED_BOTH);
                appSettings = new Settings(width, height, startX, startY, goalX, goalY, walls);
                run();
                if(!noPath){
                    nextButton.setEnabled(true);
                    drawBoard(board);
                }else {
                    nextButton.setText("NO PATH");
                    nextButton.setEnabled(false);
                }
            }
        });
        // Next Button properties
        nextButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                boolean stillEnabled = move(board);
                if(!stillEnabled) nextButton.setEnabled(false);
            }
        });
        nextButton.setEnabled(false);
        // Reset Button properties
        resetButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                resetFrame();
            }
        });
        // </editor-fold>

        // ADD PROPERTIES TO JPANELS
        // Add command content to the updates panel
        updates.add(wallCounter);
        updates.add(currentAction);
        updates.add(buildButton);
        // loop through y coord of board to initialize board spaces
        boardPanel.setBorder(BorderFactory.createEmptyBorder(2,2,2,2));
        for(int x =0 ; x < width ; x++){
            for(int y = 0; y < height ; y++){
                board[x][y] = new JLabel(" ", JLabel.CENTER);   // 'blank' space filled in for each piece of the board
                board[x][y].setOpaque(true);
                boardPanel.add(board[x][y]);                    // add the new empty space to our boardPanel
            }
        }
        // Add buttons to buttonPanel
        buttonPanel.add(nextButton);
        buttonPanel.add(resetButton);
        // Add contents to the main panel
        mainPanel.add(boardPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        mainPanel.setVisible(true);

        // Add all the primary components to the JFrame
        addJFrameComponent(updates);
        addJFrameComponent(grid);
        addJFrameComponent(mainPanel);
        
        refreshFrame();
        
        this.wait = true;
    }
    
    public void refreshFrame(){
        pane.pack();
        pane.setVisible(true);
        pane.getContentPane().invalidate();
        pane.getContentPane().validate();
    }
    
    public void addJFrameComponent(JPanel newPanel){
        pane.getContentPane().add(newPanel);
        refreshFrame();
    }
    
    public boolean removeJFrameComponent(JPanel panel){
        boolean result = false;
        for(int i = 0; i < pane.getComponents().length; i++){
            if(pane.getComponent(i).equals(panel)){
                result = true;
                pane.getContentPane().remove(panel);
            }
        }
        return result;
    }
    // </editor-fold>
    
    // <editor-fold desc="BUTTON ACTION LISTENER METHODS">
    
    // Outputs debug map of grid, and calls the setOpen method
    public void run(){ // int[] walls
            int[] startCoords = appSettings.getStart();
            int[] endCoords = appSettings.getEnd();
           
           for(int x=0 ; x < appSettings.getWidth() ; ++x){
              for(int y=0 ; y < appSettings.getHeight() ; ++y){
                  appSettings.getGrid()[x][y] = new Node(x, y, false);
                  appSettings.getGrid()[x][y].setHeuristic(endCoords[0], endCoords[1]);
              }
           }
           appSettings.getGrid()[startCoords[0]][startCoords[1]].finalCost = 0;
           
           // Set walls cells. Simply set the cell values to null
           for(int i=0; i < appSettings.getWallList().size() ; i+=2){
               int x = appSettings.getWallList().get(i);
               int y = appSettings.getWallList().get(i + 1);
               appSettings.getGrid()[x][y] = null;
           }
           
           setOpen();
           
           // <editor-fold desc="DEBUG CODE">
           // For DEBUGGING: view map with values, start, end, and walls in console
           System.out.println("Key: ");
           System.out.println(" O  ... Computer start position");
           System.out.println(" X  ... Goal position");
           System.out.println("/// ... Wall");
           System.out.println(" #  ... Node cost");
           //Display initial map
           System.out.println("\nGrid: ");
            for(int x=0; x < appSettings.getWidth() ;++x){
                for(int y=0; y < appSettings.getHeight() ;++y){
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
            }else {
                System.out.println("No possible path!");
                noPath = true;
            }
            // </editor-fold>
    }
    
    public void setOpen(){
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
                findAndSetCosts(current, n, current.finalCost+VERT_HORIZ_COST); 

                if(current.getY() - 1 >= 0){ //current.y-1 >= 0){                      
                    n = appSettings.getGrid()[current.getX() - 1][current.getY() - 1];
                    findAndSetCosts(current, n, current.finalCost+DIAGONAL_COST); 
                }

                if(current.getY() + 1 < appSettings.getWidth()){ //current.y+1 < grid[0].length){
                    n =appSettings.getGrid()[current.getX() - 1][current.getY() + 1];
                    findAndSetCosts(current, n, current.finalCost+DIAGONAL_COST); 
                }
            } 

            if(current.getY() - 1 >= 0){//current.y-1 >= 0){
                n = appSettings.getGrid()[current.getX()][current.getY() - 1];
                findAndSetCosts(current, n, current.finalCost+VERT_HORIZ_COST); 
            }

            if(current.getY() + 1 < appSettings.getWidth()){//current.y+1 < grid[0].length){
                n = appSettings.getGrid()[current.getX()][current.getY() + 1];
                findAndSetCosts(current, n, current.finalCost+VERT_HORIZ_COST); 
            }

            if(current.getX() + 1 < appSettings.getWidth()){ //current.x+1 < grid.length){
                n = appSettings.getGrid()[current.getX() + 1][current.getY()];
                findAndSetCosts(current, n, current.finalCost+VERT_HORIZ_COST); 

                if(current.getY() - 1 >= 0){//current.y-1 >= 0){
                    n = appSettings.getGrid()[current.getX() + 1][current.getY() - 1];
                    findAndSetCosts(current, n, current.finalCost+DIAGONAL_COST); 
                }
                
                if(current.getY() + 1 < appSettings.getWidth()){ //current.y+1 < grid[0].length){
                   n = appSettings.getGrid()[current.getX() + 1][current.getY() + 1];
                    findAndSetCosts(current, n, current.finalCost+DIAGONAL_COST); 
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
    public void findAndSetCosts(Node current, Node t, int cost){
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
    
    // When next button pressed, update the mover position to their next destination
    public boolean move(JLabel[][] board){
        boolean stillEnabled = true;
        
        if(!appSettings.getNavigation().isEnd()){
            appSettings.getNavigation().getNext();
            appSettings.getComputer().move(appSettings.getNavigation().getX(), appSettings.getNavigation().getY());
        } else {
            stillEnabled = false;
        }
        drawBoard(board);
        
        return stillEnabled;
    }
   
    public void drawBoard(JLabel[][] board){
        for (int x = 0 ; x < width ; x++){
            for (int y = 0 ; y < height ; y++){
                board[x][y].setBorder(BorderFactory.createLineBorder(Color.BLACK));
                if (x == startX && y == startY){
                    board[x][y].setText("START");
                    board[x][y].setBackground(Color.YELLOW);
                }
                else if(x == goalX && y == goalY ) {
                    board[x][y].setText("GOAL");
                    board[x][y].setBackground(Color.RED);
                } else {
                    board[x][y].setBackground(Color.BLUE);
                    board[x][y].setText("");
                }
                if(x == appSettings.getComputer().getX() && y == appSettings.getComputer().getY()){
                    board[x][y].setText("COMP");
                    board[x][y].setBackground(Color.CYAN);
                }
            }
        }
        
        if(!appSettings.equals(null)){
            for(int i = 0; i < appSettings.getWallList().size() ; i+= 2){
                board[appSettings.getWallList().get(i)][appSettings.getWallList().get(i+1)].setBackground(Color.BLACK);
            }
        }
    }

    // Select button associated with grids that will set 
    // values to grid for traversing
    public void setWalls(JButton button, JButton build){
        actionMessage = "";                                                     // Informs the user what current block types they are selecting
        
        if(wallCount > 0){
            actionMessage = "Build more walls!";
            if(!button.isBackgroundSet()){                                      // User selected grid to be a new wall space
                button.setBackground(Color.BLACK);
                wallCount--;
                walls.add(getXCoordinate(button.getText()));                    // add next x coordinate
                walls.add(getYCoordinate(button.getText()));                    // add next y coordinate
                if(wallCount == 0) {
                    setStart = true;
                    actionMessage = "Set the start position!";
                }
            } else {                                                            // User wants to change wall back to traversable grid
                button.setBackground(null);
                walls.remove(walls.size() - 1);                                 // most recent y
                walls.remove(walls.size() - 2);                                 // most recent x
                wallCount++;
            }
        }else if(setStart){                                                     // User selects start position
            actionMessage = "Set the start position!";
            if(!button.isBackgroundSet()){
                button.setBackground(Color.GREEN);
                setStart = false;
                setGoal = true;
                actionMessage = "Set the goal position!";
                startX = getXCoordinate(button.getText());                      // Assign starting x coord
                startY = getYCoordinate(button.getText());                      // Assign starting y coord
            }
        }else if(setGoal){                                                      // User selects goal position
            actionMessage = "Set the goal position!";
            if(!button.isBackgroundSet()){
                button.setBackground(Color.BLUE);
                setGoal = false;
                goalX = getXCoordinate(button.getText());                       // Assign goal x coord
                goalY = getYCoordinate(button.getText());                       // Assign goal y coord
                build.setEnabled(true);
                //setIsFinished(true);
            }
        }
        wallCounter.setText("Walls left to build: " + wallCount);
        currentAction.setText(actionMessage);
        
        wait = false;                                                           // When over, end wait flag
        
    }
    
    public int getXCoordinate(String coordinates){
        String value = "" + coordinates.charAt(0);
        return Integer.parseInt(value);
    }
    
    public int getYCoordinate(String coordinates){
        String value = "" + coordinates.charAt(coordinates.length()-1);
        return Integer.parseInt(value);
    }
    
    public void resetFrame(){
        wallCount = height + width +(height + width)/2;
        setStart = false;
        setGoal = false;
        finished = false;
        wait = false;
        noPath = false;
        setFrame();
    }
    
    // </editor-fold>
    
    // <editor-fold desc="GETTER AND SETTERS FOR PROPERTIES">
    
    public boolean getIsFinished(){
        return this.finished;
    }
    
    public void setIsFinished(boolean value){
        this.finished = value;
    }
   
    public boolean getWait(){
        return this.wait;
    }
    
    public int getStartX(){
        return startX;
    }
    
    public int getStartY(){
        return startY;
    }
    
    public int getGoalX(){
        return goalX;
    }
    
    public int getGoalY(){
        return goalY;
    }
    
    public ArrayList<Integer> getWalls(){
        return walls;
    }
    
    // </editor-fold>
    
}