package scrabble.game;

import scrabble.*;

public class Tile {


    char letter;
    int point;
    int id;
    
    public Tile(int _id)
    {
    	id = _id;
        letter = Constants.tileLetter[id];
        point = Constants.tilePoint[id];
        
    }
    
    public Tile(Tile t)
    {
    	letter = t.letter;
        id = t.id;
        point = t.point;
    }
    
    // set letter
    public void setLetter(char l)
    {
    	letter = l;
    }
    
    // get letter of the tile
    public char getLetter()
    {
    	return letter;
    }
    
    public String toString()
    {
    	return "" + letter;
    }
    public int getID ()
    {
        return id;
    }
}
