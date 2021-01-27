package com.maximgorshkov.reversi;

import java.util.HashSet;
import java.util.Set;

public class Action
{
    public int          row;
    public int          col;
    public int          player_value;
    
    Set<Coordinate> coordinates_to_flip;
    
    public Action(int row, int col, int current_player)
    {
        this.row                 = row;
        this.col                 = col;
        this.player_value        = current_player;
        this.coordinates_to_flip = new HashSet<>();
    }
    
    @Override
    public boolean equals(Object o) { 
        // Source: https://www.geeksforgeeks.org/overriding-equals-method-in-java/

        // If the object is compared with itself then return true   
        if (o == this) { 
            return true; 
        } 
  
        /* Check if o is an instance of Action or not 
          "null instanceof [type]" also returns false */
        if (!(o instanceof Action)) { 
            return false; 
        } 
          
        // typecast o to Action so that we can compare data members  
        Action c = (Action) o; 
          
        // Compare the data members and return accordingly  
        return Integer.compare(row, c.row) == 0
                && Integer.compare(col, c.col) == 0
                && Integer.compare(player_value, c.player_value) == 0; 
    }

    @Override
    public String toString() {
        return "Action{" +
                "row=" + row +
                ", col=" + col +
                ", player_value=" + player_value +
                '}';
    }
}
