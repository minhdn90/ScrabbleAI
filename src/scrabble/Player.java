package scrabble;

import java.util.Vector;
import scrabble.game.Tile;

public class Player {
    String username;
    int score;
    public Vector<Tile> rack;
    boolean resign;
    boolean inTurn;
    boolean isMaster;
    

    public Player(String _username, boolean _isMaster)
    {
        username = _username;
        score = 0;
        rack = new Vector<Tile>();
        resign = false;
        inTurn = false;
        isMaster = _isMaster;
    }

    public String getUsername()
    {
        return username;
    }

    public void addScore(int _score) {

        score += _score;
    }

    public boolean resigned()   {

        return resign;
    }

    public void setResign(boolean _resign){

        resign = _resign;
        
    }

    public void addTile(Tile tile)   {

        rack.add(tile);
    }

    public Vector<Tile> getRack()   {

        return rack;
    }
    
    public boolean isInTurn()
    {
    	return inTurn;
    }
    
    public void setTurn(boolean b)
    {
    	inTurn = b;
    }
    
    public boolean isMaster()
    {
    	return isMaster;
    }
    
    public int getScore()
    {
    	return score;
    }


}
