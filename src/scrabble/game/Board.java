package scrabble.game;

import java.util.Vector;
import java.lang.Math;
import scrabble.Constants;

class Position{
    public int x;
    public int y;
    public Position (int _x, int _y)
    {
        x = _x;
        y = _y;
    }
    public Position (Position pos)
    {
        x = pos.x;
        y = pos.y;
    }
}

public class Board {
    private Square board[][];
    private static final int size = 15;
    private Vector <String> wordsToCheck = new Vector <String>();
    private Vector <Position> initPos = new Vector <Position>();
    private static final int BOARD[][] = 
    	{{4, 0, 0, 1, 0, 0, 0, 4, 0, 0, 0, 1, 0, 0, 4,},
    	 {0, 3, 0, 0, 0, 2, 0, 0, 0, 2, 0, 0, 0, 3, 0,},
    	 {0, 0, 3, 0, 0, 0, 1, 0, 1, 0, 0, 0, 3, 0, 0,},
    	 {1, 0, 0, 3, 0, 0, 0, 1, 0, 0, 0, 3, 0, 0, 1,},
    	 {0, 0, 0, 0, 3, 0, 0, 0, 0, 0, 3, 0, 0, 0, 0,},
    	 {0, 2, 0, 0, 0, 2, 0, 0, 0, 2, 0, 0, 0, 2, 0,},
    	 {0, 0, 1, 0, 0, 0, 1, 0, 1, 0, 0, 0, 1, 0, 0,},
    	 {4, 0, 0, 1, 0, 0, 0, 3, 0, 0, 0, 1, 0, 0, 4,},
    	 {0, 0, 1, 0, 0, 0, 1, 0, 1, 0, 0, 0, 1, 0, 0,},
    	 {0, 2, 0, 0, 0, 2, 0, 0, 0, 2, 0, 0, 0, 2, 0,},
    	 {0, 0, 0, 0, 3, 0, 0, 0, 0, 0, 3, 0, 0, 0, 0,},
    	 {1, 0, 0, 3, 0, 0, 0, 1, 0, 0, 0, 3, 0, 0, 1,},
    	 {0, 0, 3, 0, 0, 0, 1, 0, 1, 0, 0, 0, 3, 0, 0,},
    	 {0, 3, 0, 0, 0, 2, 0, 0, 0, 2, 0, 0, 0, 3, 0,},
    	 {4, 0, 0, 1, 0, 0, 0, 4, 0, 0, 0, 1, 0, 0, 4,}
    	};
    
    
    public Board()
    {
    	board = new Square[size][size];
    	for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++)
                board[i][j] = new Square(BOARD[i][j]);
    }

    // get a square
    public Square getSquare(int i, int j)
    {
            return board[i][j];
    }

    // update a move to the board
    public void update(Vector<LetterMove> move)
    {
        for (int i=0; i<move.size(); i++)
        {
            board[move.elementAt(i).x][move.elementAt(i).y].tile = move.elementAt(i).tile;
        }
    }
    //find the min X if word is vertical, min Y if word is horizontal
    private int findMin (Vector <LetterMove> currentMove)
    {
        int min = 0;
        if (currentMove.size() >= 2)
        {
            if (currentMove.elementAt(0).x == currentMove.elementAt(1).x)
            {
                min = currentMove.elementAt(0).y;
                for (int i=1; i< currentMove.size(); i++)
                {
                    if (currentMove.elementAt(i).y < min) min = currentMove.elementAt(i).y;
                }
            }
            else
            {
                min = currentMove.elementAt(0).x;
                for (int i=1; i< currentMove.size(); i++)
                {
                    if (currentMove.elementAt(i).x < min) min = currentMove.elementAt(i).x;
                }
            }
        }
        return min;
    }
    //find max min X if word is vertical, max Y if word is horizontal
    private int findMax (Vector <LetterMove> currentMove)
    {
        int max = 0;
        if (currentMove.size() >= 2)
        {
            if (currentMove.elementAt(0).x == currentMove.elementAt(1).x)
            {
                max = currentMove.elementAt(0).y;
                for (int i=1; i< currentMove.size(); i++)
                {
                    if (currentMove.elementAt(i).y > max) max = currentMove.elementAt(i).y;
                }
            }
            else
            {
                max = currentMove.elementAt(0).x;
                for (int i=1; i< currentMove.size(); i++)
                {
                    if (currentMove.elementAt(i).x > max) max = currentMove.elementAt(i).x;
                }
            }
        }
        return max;
    }
    //check if the NEW tiles is in one row or one column and connect with each others
    private int isOneLine(Vector<LetterMove> currentMove)
    {
        if (currentMove.size() == 1)
        {
            int x = currentMove.elementAt(0).x;
            int y = currentMove.elementAt(0).y;

            if (x > 0 && board[x-1][y].isOccupied()) return 1;
            if (x < 14 && board[x+1][y].isOccupied()) return 1;
            return 2;
        }
        //more than 2 tiles in a move
        else
        {
            if (currentMove.elementAt(0).x == currentMove.elementAt(1).x)
            {
                //System.out.println("board: check ngang");
                for (int i=2; i<currentMove.size(); i++)
                {
                    if (currentMove.elementAt(i).x != currentMove.elementAt(0).x) 
                    {       
                        return 0;
                    }
                }
                //System.out.println("board: ngang");
                return 1;
            }
            else if (currentMove.elementAt(0).y == currentMove.elementAt(1).y)
            {
                for (int i=2; i<currentMove.size(); i++)
                {
                    if (currentMove.elementAt(i).y != currentMove.elementAt(0).y) 
                    {
                        return 0;
                    }
                }
                return 2;
            }
        }
        return 0;
    }
    private boolean preOccupied (Position p, Vector <LetterMove> m)
    {
        for (int i=0; i<m.size(); i++)
        {
            if (m.elementAt(i).x == p.x && m.elementAt(i).y == p.y) 
            {
                return true;
            }
        }
        return false;
    }
    private boolean firstWord ()
    {
        for (int i=0; i < size; i++)
        {
            for (int j=0; j<size; j++)
            {
                if (board[i][j].isOccupied()) return false;
            }
        }
        return true;
    }
    //check if the new tiles connect with old tiles
    public boolean checkConnected (Vector <LetterMove> m)
    {
        if (!board[7][7].isOccupied() && !preOccupied(new Position (7, 7), m)) return false;
        if (m.size() == 1)
        {
            if (m.elementAt(0).x > 0)
            {
                if (board[m.elementAt(0).x-1][m.elementAt(0).y].isOccupied()) return true;
            }
            if (m.elementAt(0).x < 14)
            {
                if (board[m.elementAt(0).x+1][m.elementAt(0).y].isOccupied()) return true;
            }
            if (m.elementAt(0).y > 0)
            {
                if (board[m.elementAt(0).x-1][m.elementAt(0).y-1].isOccupied()) return true;
            }
            if (m.elementAt(0).y < 14)
            {
                if (board[m.elementAt(0).x-1][m.elementAt(0).y+1].isOccupied()) return true;
            }
        }
        else
        {
            if (isOneLine(m) == 1)
            {
                int start, end, x = m.elementAt(0).x;
                start = findMin(m);
                end = findMax(m);

                boolean ans = false;
                if (start>0)
                    if (board[x][start-1].isOccupied()) ans = true;
                if (end<14)
                    if (board[x][end+1].isOccupied()) ans = true;
                
                //System.out.println("board: start - " + start + " end - " + end);

                while (start <= end)
                {
                    //System.out.println ("vao while ngang");
                    if (board[x][start].isOccupied()) ans = true;
                    if (x>0)
                        if (board[x-1][start].isOccupied()) ans = true;
                    if (x<14)
                        if (board[x+1][start].isOccupied()) ans = true;

                    if (!board[x][start].isOccupied()
                        && !preOccupied(new Position (x, start), m))
                        {
                            //System.out.println ("board: false x " + x + "-" +start);
                            return false;
                        }
                    start ++;
                }
                if (ans) return true;
            }
            else
            {
               int start, end, y = m.elementAt(0).y;
                start = findMin(m);
                end = findMax(m);

                boolean ans = false;
                if (start>0)
                    if (board[start-1][y].isOccupied()) ans = true;
                if (end<14)
                    if (board[end+1][y].isOccupied()) ans = true;

                //System.out.println("board: start - " + start + " end - " + end);

                while (start <= end)
                {
                    //System.out.println ("vao while ngang");
                    if (board[start][y].isOccupied()) ans = true;
                    if (y>0)
                        if (board[start][y-1].isOccupied()) ans = true;
                    if (y<14)
                        if (board[start][y+1].isOccupied()) ans = true;

                    if (!board[start][y].isOccupied()
                        && !preOccupied(new Position (start,y), m))
                        {
                            //System.out.println ("board: false y " + start + "-" + y);
                            return false;
                        }
                    start ++;
                }
                if (ans) return true;
            }
        }
        return true;
    }
    
    public int isLine (Vector <LetterMove> currentMove)
    {
        if (isOneLine(currentMove) == 0) 
        {
            //System.out.println ("one line bang 0");
            return 0;
        }     
        else
        {
            //System.out.println ("one line khac 0");
            if (checkConnected(currentMove)) 
            {
                int x = isOneLine(currentMove);
                return x;
            }//if connect
            else 
            {
                //System.out.println ("not connected");
                return 0;
            }//not connect
        }
    }

    private void makeHorizontalWord(int _x, int _y, Vector<LetterMove> currentMove )
    {
        String tmp = "";
        int it = _y;
        int x  = _x;
        // horizontal
        while (it > 0 && board[x][it-1].isOccupied())
        {
            it--;
        }
        Position pos = new Position (x, it);
        while (it < 15 && board[x][it].isOccupied() || preOccupied(new Position(x, it), currentMove))
        {
            if (board[x][it].isOccupied()) tmp += board[x][it].getTile().letter;
            else
            {
                for (int j=0; j<currentMove.size(); j++)
                {
                    if (currentMove.elementAt(j).x == x && currentMove.elementAt(j).y == it)
                    {
                        tmp += currentMove.elementAt(j).tile.letter;
                    }
                }
            }
            it++;
        }

        if (tmp.length()>1)
        {
            wordsToCheck.add(tmp.toLowerCase());
            initPos.add(pos);
        }
    }

    public void makeVerticalWord(int _x, int _y, Vector<LetterMove> currentMove)
    {
        int it = _x;
        int y  = _y;
        String tmp = "";
        while (it > 0 && board[it-1][y].isOccupied())
        {
            it--;
        }
        Position pos = new Position (it, y);
        while (it < 15 && board[it][y].isOccupied() || preOccupied(new Position(it, y), currentMove))
        {
            if (it < 15 && board[it][y].isOccupied()) tmp += board[it][y].getTile().letter;
            else
            {
                for (int j=0; j<currentMove.size(); j++)
                {
                    if (currentMove.elementAt(j).y ==y && currentMove.elementAt(j).x == it)
                    {
                        tmp += currentMove.elementAt(j).tile.letter;
                    }
                }
            }
            it++;
        }
        if (tmp.length()>1)
        {
            wordsToCheck.add(tmp.toLowerCase());
            initPos.add(pos);
        }
    }
    
    //make the word correspoding to the main direction 
    private void makeMainWord (Vector<LetterMove> currentMove)
    {
        if (currentMove.size() == 1)
        {
            makeHorizontalWord(currentMove.elementAt(0).x, currentMove.elementAt(0).y, currentMove);

            // vertical
            makeVerticalWord(currentMove.elementAt(0).x, currentMove.elementAt(0).y, currentMove);
        }
        else
        {
            if (isLine (currentMove) == 1)
            {
                int it = findMin(currentMove);
                int x = currentMove.elementAt(0).x;
                makeHorizontalWord(x, it, currentMove);
            }
            else if (isLine(currentMove) == 2)
            {
                int it = findMin(currentMove);
                int y = currentMove.elementAt(0).y;
                makeVerticalWord(it, y, currentMove);
            }
        }
    }
    //add the secondary word to the vector
    private void makeSecondaryWord (Vector<LetterMove> m)
    {
        String s="";
        //horizontal
        if (isLine(m) == 1)
        {
            for (int i=0; i<m.size(); i++)
            {
                makeVerticalWord(m.elementAt(i).x, m.elementAt(i).y, m);
            }
        }
        //vertical
        else if (isLine(m) == 2)
        {
            for (int i=0; i<m.size(); i++)
            {
                makeHorizontalWord(m.elementAt(i).x, m.elementAt(i).y, m);
            }
        }
    }
    public Vector<String> getWords (Vector <LetterMove> currentMove)
    {
        wordsToCheck.clear();
        makeMainWord(currentMove); 
        if (currentMove.size()>1)
            makeSecondaryWord(currentMove);
        return wordsToCheck;
    }
    public Vector <Position> getPos (Vector<LetterMove> currentMove)
    {
        Vector <Position> res = new Vector(initPos);
        //System.out.println("board: initPos's size " + initPos.size());
        initPos.clear();
        return res;
    }
    public boolean checkNewLetter (Position p)
    {
        if (board[p.x][p.y].isOccupied()) return false;
        return true;
    }
    
}
