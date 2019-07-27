package scrabble.game;

public class LetterMove {
	public int x, y;
	Tile tile;
	
	public LetterMove(int _x, int _y, Tile t)
	{
            x = _x;
            y = _y;
            tile = t;
	}
        public LetterMove(int _x, int _y, int tileID)
        {
            x = _x;
            y = _y;
            tile = new Tile(tileID);
        }
        public LetterMove (String lt, String _x, String _y)
        {
            tile = new Tile(Integer.parseInt(lt));
            x = Integer.parseInt(_x);
            y = Integer.parseInt(_y);
        }
        
        public Tile getTile()
        {
        	return tile;
        }

}
