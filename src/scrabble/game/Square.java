/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package scrabble.game;


public class Square {
    int type;
    Tile tile;
    
    public Square(int t)
    {
    	type = t;
    	tile = null;
    }
    
    public Square(int t, Tile _tile)
    {
    	type = t;
    	tile = _tile;
    }
    
    // check whether it is occupied
    public boolean isOccupied()
    {
    	return (tile != null);
    }
    
    // get type of the square
    public int getType()
    {
    	return type;
    }
    
    public Tile getTile()
    {
    	return tile;
    }
}
