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
    
    private int height, width, wallCount;
    private int startX, startY, goalX, goalY;
    private boolean setStart = false;
    private boolean setGoal = false;
    private boolean finished = false;
    private boolean wait = false;
    private String actionMessage;
    private ArrayList<Integer> walls = new ArrayList<Integer>();
    private JLabel currentAction = new JLabel();
    private JLabel wallCounter = new JLabel();
    private JFrame pane = new JFrame();
    private ArrayList<JPanel> frameComponents;
    private int wallX;
    private int wallY;

    StartupPanel(int h, int w){
        this.height = h;
        this.width = w;
        this.wallCount = 1; //h + w + (h+w)/2;
    }
    
    public void createFrame(){
        pane.setDefaultCloseOperation(pane.EXIT_ON_CLOSE);
        pane.getContentPane();
        pane.setLayout(new GridLayout(1, 2));
    }

    
    public void setFrame(){
        //pane.dispatchEvent(New WindowEvent(pane, WindowEvent.WINDOW_CLOSING));

        JPanel grid = new JPanel(new GridLayout(height, width));
        JPanel updates = new JPanel(new GridLayout(3, 1));
        JLabel[][] board = new JLabel[height][width];
        
        JButton build = new JButton("BUILD");
        build.setEnabled(false);
        build.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                setIsFinished(true);
                pane.getContentPane().remove(grid);
                pane.getContentPane().remove(updates);
                pane.getContentPane().invalidate();
                pane.getContentPane().validate();
                drawBoard(board);
                //updates.hide();
                //grid.hide();
            }
        });
        
        wallCounter.setText("Walls left to build: " + wallCount);
        wallCounter.setHorizontalAlignment(SwingConstants.CENTER);
        currentAction.setText(actionMessage);
        currentAction.setFont(new Font("Serif", Font.BOLD, 16));
        currentAction.setHorizontalAlignment(SwingConstants.CENTER);
        
        updates.add(wallCounter);
        updates.add(currentAction);
        updates.add(build);
        
        for(int i = 0; i < height*width ; i++){
            JButton button = new JButton(Integer.toString(i%10) + ", " + Integer.toString(i/10));
            button.setBackground(null);
            button.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e) {
                    setWalls(button, build);
                }
            });
            grid.add(button);
        }
        
        JPanel boardPanel = new JPanel(new GridLayout(height, width));
        JPanel buttonPanel = new JPanel(new GridLayout(1, 4));
        JPanel initialPanel = new JPanel();
        boardPanel.setBorder(BorderFactory.createEmptyBorder(2,2,2,2));
        initialPanel.setBorder(BorderFactory.createEmptyBorder());
        
        
        
        // loop through y coord of board to initialize board spaces
        for(int x =0 ; x < width ; x++){
            for(int y = 0; y < height ; y++){
                board[x][y] = new JLabel(" ", JLabel.CENTER);   // 'blank' space filled in for each piece of the board
                board[x][y].setOpaque(true);
                boardPanel.add(board[x][y]);                    // add the new empty space to our boardPanel
            }
        }

        // initialize the start & stop buttons and place on the buttonPanel
        JButton nextButton = new JButton("Next");
        nextButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                //boolean stillEnabled = move(appSettings, board);
                //if(!stillEnabled) nextButton.setEnabled(false);
            }
        });
        nextButton.setEnabled(false);
        buttonPanel.add(nextButton);

        
        // Add contents to the main panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        
        mainPanel.add(initialPanel, BorderLayout.NORTH);
        mainPanel.add(boardPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        mainPanel.setVisible(true);

        addJFrameComponent(updates);
        addJFrameComponent(grid);
        addJFrameComponent(mainPanel);
        
        refreshFrame();
        
        this.wait = true;
    }
    
    
    public void addJFrameComponent(JPanel newPanel){
        pane.getContentPane().add(newPanel);
        refreshFrame();
    }
    
    public void refreshFrame(){
        pane.pack();
        pane.setVisible(true);
        pane.getContentPane().invalidate();
        pane.getContentPane().validate();
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
    
    public void drawBoard(JLabel[][] board){
        int i = 0;
        for (int x = 0 ; x < width ; x++){
            int j = 0;
            for (int y = 0 ; y < height ; y++){
                board[x][y].setBorder(BorderFactory.createLineBorder(Color.BLACK));
                if (x == startX && y == startY){
                    board[x][y].setText("START");
                    board[x][y].setBackground(Color.YELLOW);
                }
                else if(wallX == x && wallY == y) board[x][y].setBackground(Color.BLACK); //.setText("#");
                else if(x == goalX && y == goalY ) {
                    board[x][y].setText("GOAL");
                    board[x][y].setBackground(Color.RED);
                }else if (startX == x && startY == y) {
                    board[x][y].setBackground(Color.GREEN);
                    board[x][y].setText("COMP");
                }
                else {
                    board[x][y].setBackground(Color.BLUE);
                    board[x][y].setText("");
                }
            }
        }
   
    }
    
    public void setIsFinished(boolean value){
        this.finished = value;
    }
    
    public boolean getIsFinished(){
        return this.finished;
    }
   
    public boolean getWait(){
        return this.wait;
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
                wallX = getXCoordinate(button.getText());                      // Assign starting x coord
                wallY = getYCoordinate(button.getText());
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
        
        this.wait = false;
        
    }
    
    public int getXCoordinate(String coordinates){
        String value = "" + coordinates.charAt(0);
        return Integer.parseInt(value);
    }
    
    public int getYCoordinate(String coordinates){
        String value = "" + coordinates.charAt(coordinates.length()-1);
        return Integer.parseInt(value);
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
    
}