/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package a_star_revert;

import java.util.ArrayList;

/**
 *
 * @author MJHobby
 * @date 04/27/2017
 * @title Path: Stores values for the A* calculated shortest distance path
 */
public class Path {
    private ArrayList<Integer> coordinates = new ArrayList<Integer>();
    private int position, x, y;
    private boolean end;
    
    public Path(){
        position = 0;
    }
    
public void addCoordinates(int x, int y){
    coordinates.add(x);
    coordinates.add(y);
}

public void close(){
    position = coordinates.size()-1;
}

public void getNext(){

    if (position >= 1){
        y = coordinates.get(position);
        x = coordinates.get(position - 1);
    
        position -= 2;
        if (position < 1) end = true;
    } else {
        position = 0;
        end = true;
    }
    
}

public void getPrevious(){
    if(this.position < coordinates.size() && this.position >= 0){
        this.x = coordinates.get(position);
        this.y = coordinates.get(position + 1);
        
        this.position += 2;
    }
}

public boolean isEnd(){
    return end;
}

public int getX(){
    return x;
}

public int getY(){
    return y;
}
    
    
}
