/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package a_star_revert;

import javax.swing.*;

/**
 *
 * @author MJHobby
 */
public class A_Star_App extends JFrame {

    public static final int WIDTH = 10;
    public static final int HEIGHT = 10;


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
        
    }
    
}
