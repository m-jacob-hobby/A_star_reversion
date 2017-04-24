/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package a_star_revert;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

/**
 *
 * @author MJHobby
 */
public class PathDisplayPanel {
    
    private int height, width;
    private Settings appSettings;
    
    PathDisplayPanel(int boardWidth, int boardHeight, Settings settings){
        width = boardWidth;
        height = boardHeight;
        appSettings = settings;
    }
    
    public void buildPanel(StartupPanel initialPane){
        //JFrame pane = new JFrame();
        //pane.setDefaultCloseOperation(pane.EXIT_ON_CLOSE);
        //pane.getContentPane();
        
        // Panels for the board grid, and panel for buttons
        /*
        JPanel boardPanel = new JPanel(new GridLayout(height, width));
        JPanel buttonPanel = new JPanel(new GridLayout(1, 4));
        JPanel initialPanel = new JPanel();
        boardPanel.setBorder(BorderFactory.createEmptyBorder(2,2,2,2));
        initialPanel.setBorder(BorderFactory.createEmptyBorder());
        
        JLabel[][] board = new JLabel[height][width];
        
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
                boolean stillEnabled = move(appSettings, board);
                if(!stillEnabled) nextButton.setEnabled(false);
            }
        });
        nextButton.setEnabled(true);
        buttonPanel.add(nextButton);

        
        // Add contents to the main panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        
        mainPanel.add(initialPanel, BorderLayout.NORTH);
        mainPanel.add(boardPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        //pane.add(mainPanel);
        mainPanel.setVisible(true);
        */
        //drawBoard(appSettings, board);
        //initialPane.addJFrameComponent(mainPanel);

    }
    
    public boolean move(Settings appSettings, JLabel[][] board){
        boolean stillEnabled = true;
        
        if(!appSettings.getNavigation().isEnd()){
            appSettings.getNavigation().getNext();
            appSettings.getComputer().move(appSettings.getNavigation().getX(), appSettings.getNavigation().getY());
        } else {
            stillEnabled = false;
        }
        drawBoard(appSettings, board);
        
        return stillEnabled;
    }
    
    public void drawBoard(Settings appSettings, JLabel[][] board){
        int[] startCoords = appSettings.getStart();
        int[] endCoords = appSettings.getEnd();

        for (int x = 0 ; x < width ; x++){
            for (int y = 0 ; y < height ; y++){
                board[x][y].setBorder(BorderFactory.createLineBorder(Color.BLACK));
                if (x == startCoords[0] && y == startCoords[1]){
                    board[x][y].setText("START");
                    board[x][y].setBackground(Color.YELLOW);
                }
                else if(appSettings.getGrid()[x][y] == null) board[x][y].setBackground(Color.BLACK); //.setText("#");
                else if(x == endCoords[0] && y == endCoords[1] ) {
                    board[x][y].setText("GOAL");
                    board[x][y].setBackground(Color.RED);
                }else if (appSettings.getComputer().getX() == x && appSettings.getComputer().getY() == y) {
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
    
}
