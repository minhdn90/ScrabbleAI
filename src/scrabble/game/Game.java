package scrabble.game;
import java.lang.Math;
import java.util.Vector;
import scrabble.Constants;
import scrabble.Player;

public class Game {
    private final int size = 10;
    private Board board;
    private MyDictionary dictionary;
    private int turn;
    private Bag bag;
    private Vector<Player> playerList;
    private Vector<LetterMove> currentMove;
    private Vector<Position> startPos;
    Vector<String> words;
    private boolean isStarted;
    private int dir;
    
    public Game()
    {
    	board = new Board();
    	dictionary = new MyDictionary();
        playerList = new Vector();
        currentMove = new Vector();
        startPos = new Vector();
    	bag = new Bag();
        isStarted = false;
        turn = -1;
    }
    
    public void startGame()
    {
    	isStarted = true;
    }

    public boolean endGame()
    {
        int count = 0;
        for (int i=0; i<playerList.size(); i++)
        {
            if (!playerList.elementAt(i).resigned()) count++;
        }
        //System.out.println("game: there are " + count + " player");
        return (count<2);
    }

    public boolean checkWord()
    {
        System.out.print("game: current letters ");
        for (LetterMove i:currentMove)
        {
            System.out.print(i.x + " " + i.y + " - ");
        }
        System.out.println();
        
        dir = board.isLine(currentMove);
        System.out.println("game: in one line - " + dir);
        if (dir == 0) return false;
        words = board.getWords(currentMove);

        for (int i=0; i<words.size(); i++)
        {
            System.out.println("game: word - " + words.elementAt(i));
            if (!dictionary.checkWord(words.elementAt(i))) return false;
        }
        return true;
    }

    public boolean canStart()
    {
        
        return (playerList.size()>=1);
    }

    public boolean isStarted()
    {
        return isStarted;
    }

    public void setGameStt (boolean _isStarted)
    {
        isStarted = _isStarted;
    }

    public void addPlayer(Player p)
    {
        playerList.add(p);
    }

    public Vector<Player> getPlayerList ()
    {
        return playerList;
    }
    public Vector<Tile> getTiles(String username)
    {
        for (int i=0; i<playerList.size(); i++)
        {
            if (playerList.elementAt(i).getUsername() == username)
            {
                System.out.println("game: fill rack of player " + i);
                bag.fillRack(playerList.elementAt(i).getRack());

                Vector<Tile> newTiles = new Vector(playerList.elementAt(i).getRack());
                return newTiles;
            }
        }
        return new Vector<Tile> ();
    }
    public Vector<Tile> getNewTiles()
    {
        int size = playerList.elementAt(turn).getRack().size();
        System.out.println("game: player " + turn + " has " + size + " tiles");
        bag.fillRack(playerList.elementAt(turn).getRack());
        Vector<Tile> newTiles = new Vector();
        Vector<Tile> rack = playerList.elementAt(turn).getRack();
        for (int i=size; i<playerList.elementAt(turn).getRack().size(); i++)
        {
            newTiles.add(playerList.elementAt(turn).getRack().elementAt(i));
        }

        if (newTiles.size()>0)
            System.out.println("game: there are " + newTiles.size() + " new tiles");
        return newTiles;
    }

    public String getTurn()
    {
        if (turn == -1)
            return "null";
        return playerList.elementAt(turn).getUsername();
    }

    public String nextTurn ()
    {
        board.update(currentMove);
        currentMove.clear();
        if (turn == -1)
            turn = Math.abs((int)(System.currentTimeMillis() * 257) % playerList.size());
        else
        {
            turn++;
            turn = turn % playerList.size();
            while (playerList.elementAt(turn).resigned())
            {
                turn++;
                turn = turn % playerList.size();
            };
        }
        return playerList.elementAt(turn).getUsername();
    }

    // calculate score
    private int marking(String word, Position pos, int dir, int mod)
    {
        System.out.println("game: position " + pos.x + " " + pos.y);
        System.out.println("game: word " + word);
        int ans = 0;
        Square cur;
        Position curPos = new Position(pos);
        int modifier = 1;

        for (int i=0; i<word.length(); i++)
        {
            cur = board.getSquare(curPos.x, curPos.y);
            switch (cur.type)
            {
                case (Constants.NORMAL):
                    ans += Constants.getPoint(word.charAt(i));
                    System.out.println("game: plus " + word.charAt(i) + " " + Constants.getPoint(word.charAt(i)));
                    break;
                case (Constants.X2LETTER):
                    if (board.checkNewLetter(pos))
                        ans += Constants.getPoint(word.charAt(i));
                    ans += Constants.getPoint(word.charAt(i));
                    System.out.println("game: plus " + Constants.getPoint(word.charAt(i)));
                    break;
                case (Constants.X3LETTER):
                    if (board.checkNewLetter(pos))
                        ans += (Constants.getPoint(word.charAt(i)) * 2);
                    ans += Constants.getPoint(word.charAt(i));
                    System.out.println("game: plus " + Constants.getPoint(word.charAt(i)));
                    break;
                case (Constants.X2WORD):
                    if (board.checkNewLetter(pos))
                        modifier *= 2;
                    ans += Constants.getPoint(word.charAt(i));
                    System.out.println("game: plus " + Constants.getPoint(word.charAt(i)));
                    break;
                case (Constants.X3WORD):
                    if (board.checkNewLetter(pos))
                        modifier *= 3;
                    ans += Constants.getPoint(word.charAt(i));
                    System.out.println("game: plus " + Constants.getPoint(word.charAt(i)));
                    break;
                default: break;
            }
            if (dir == 1){
                curPos.y ++;
            }
            else curPos.x ++;
        }
        System.out.println("game: score " + ans + " " + modifier);
        if (mod == 0) return ans;
        return ans * modifier;
    }
    
    // calculate score of current move
    public int calculateScore()
    {
        int point = 0;
        startPos = new Vector(board.getPos(currentMove));
        System.out.println("game: startPos's size " + startPos.size());
        point = marking(words.elementAt(0), startPos.elementAt(0), dir, 1);
        for (int i=1; i<words.size(); i++)
        {
            point += marking(words.elementAt(i), startPos.elementAt(i), 3-dir, 0);
        }
    	return point;
    }

    public Vector<Tile> exchangeRack()
    {
        currentMove.clear();
        bag.exchangeRack(playerList.elementAt(turn).getRack());
        return playerList.elementAt(turn).getRack();
    }

    public void updateMove(LetterMove move)
    {
        for (Tile t : playerList.elementAt(turn).getRack())
        {
            if (t.letter == move.tile.letter)
            {
                playerList.elementAt(turn).getRack().remove(t);
                break;
            }
        }
        currentMove.add(move);
    }

    public void removeMove(LetterMove move)
    {
        System.out.println("game: going to remove " + move.x + " "+  move.y);
        for (int i=0; i<currentMove.size(); i++)
        {
            if (currentMove.elementAt(i).x == move.x && currentMove.elementAt(i).y == move.y)
            {
                System.out.println("game: remove " + currentMove.elementAt(i).tile.letter);
                playerList.elementAt(turn).getRack().add(currentMove.elementAt(i).tile);
                currentMove.remove(i);
            }
        }
    }

    public void clearMove()
    {
        currentMove.clear();
    }

    public void reInsertMove()
    {
        for (int i=0; i<currentMove.size(); i++)
        {
            playerList.elementAt(turn).getRack().add(currentMove.elementAt(i).tile);
            currentMove.remove(i);
        }
    }

    public void takeBack()
    {
        bag.takeBack(playerList.elementAt(turn).getRack());
    }
}
