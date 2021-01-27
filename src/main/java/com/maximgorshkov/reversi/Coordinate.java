package com.maximgorshkov.reversi;

import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Coordinate
{
    public int r, c;

    @Id @GeneratedValue
    private Long id;

    public Coordinate() {}

    public Coordinate(int r, int c)
    {
        this.r = r;
        this.c = c;
    }

    @Override
    public boolean equals(Object o) { 
        // Source: https://www.geeksforgeeks.org/overriding-equals-method-in-java/

        // If the object is compared with itself then return true   
        if (o == this) { 
            return true; 
        } 
  
        /* Check if o is an instance of Coordinate or not 
          "null instanceof [type]" also returns false */
        if (!(o instanceof Coordinate)) { 
            return false; 
        } 
          
        // typecast o to Coordinate so that we can compare data members  
        Coordinate coord = (Coordinate) o;
          
        // Compare the data members and return accordingly  
        return Integer.compare(r, coord.r) == 0
                && Integer.compare(c, coord.c) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(r, c);
    }
}

