package scrabble;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Duong Minh Nguyen
 */
public class Constants {
    //public static int NOT_READY = 0;
    //public static int READY = 1;
    //public static int RESIGN = -1;

    public static int tilePoint[] = {1, 3, 3, 2, 1, 4, 2, 4, 1, 8, 5, 1, 3, 1, 1, 3, 10, 1, 1, 1, 1, 4, 4, 8,4,10};
    public static char tileLetter[] = {'A','B',
                                'C','D','E',
                                'F','G','H','I', 'J', 'K', 'L','M','N','O','P','Q',
                                'R','S',
                                'T','U',
                                'V', 'W', 'X', 'Y','Z'};
    public static int VERTICAL =2;
    public static int HORIZONTAL=1;
    public static int NON = 0;
    public static int n = 0;
    public static int x2l = 1;
    public static int x3l = 2;
    public static int x2w = 3;
    public static int x3w = 4;

    public final static int NORMAL = 0;
    public final static int X2LETTER = 1;
    public final static int X3LETTER = 2;
    public final static int X2WORD = 3;
    public final static int X3WORD = 4;

    public static int [][]typeBoard  = 
    {
        {4, 0, 0, 1, 0, 0, 0, 4, 0, 0, 0, 1, 0, 0, 4},
        {0, 3, 0, 0, 0, 2, 0, 0, 0, 2, 0, 0, 0, 3, 0},
        {0, 0, 3, 0, 0, 0, 1, 0, 1, 0, 0, 0, 3, 0, 0},
        {1, 0, 0, 3, 0, 0, 0, 1, 0, 0, 0, 3, 0, 0, 1},
        {0, 0, 0, 0, 3, 0, 0, 0, 0, 0, 3, 0, 0, 0, 0},
        {0, 2, 0, 0, 0, 2, 0, 0, 0, 2, 0, 0, 0, 2, 0},
        {0, 0, 1, 0, 0, 0, 1, 0, 1, 0, 0, 0, 1, 0, 0},
        {4, 0, 0, 1, 0, 0, 0, 3, 0, 0, 0, 1, 0, 0, 4},
        {0, 0, 1, 0, 0, 0, 1, 0, 1, 0, 0, 0, 1, 0, 0},
        {0, 2, 0, 0, 0, 2, 0, 0, 0, 2, 0, 0, 0, 2, 0},
        {0, 0, 0, 0, 3, 0, 0, 0, 0, 0, 3, 0, 0, 0, 0},
        {1, 0, 0, 3, 0, 0, 0, 1, 0, 0, 0, 3, 0, 0, 1},
        {0, 0, 3, 0, 0, 0, 1, 0, 1, 0, 0, 0, 3, 0, 0},
        {0, 3, 0, 0, 0, 2, 0, 0, 0, 2, 0, 0, 0, 3, 0},
        {4, 0, 0, 1, 0, 0, 0, 4, 0, 0, 0, 1, 0, 0, 4}
    };

    public static int getPoint(char c)    {
        c = (char)(c - 'a' + 'A');
        for ( int i = 0; i < tileLetter.length; i ++)   {
            if ( c == tileLetter[i] )   {
                return tilePoint[i];
            }
        }
        return 0;
    }
}
