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
 * @title Mover: Stores position and updates position of object traversing Path
 */
public class Mover {
    
    private int x, y;
    
    public Mover(int startX, int startY){
        this.x = startX;
        this.y = startY;
    }
    
    public void move(int destX, int destY){
        this.x = destX;
        this.y = destY;
    }
    
    public int getX(){
        return this.x;
    }
    
    public int getY(){
        return this.y;
    }
    
}
