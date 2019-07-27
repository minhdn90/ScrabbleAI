package scrabble.game;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class Dictionary {
    private Set<String> dictionary = new HashSet();
    private static final String DICTINARY_FILE = "wordlist.txt";
    
    public Dictionary()
    {
    	String s;
    	try{
    		BufferedReader input = new BufferedReader(new FileReader(DICTINARY_FILE));
    		while (true){
    			s = input.readLine();
    			if (s.equals("-1"))
    				break;
    			dictionary.add(s);
    		}
    	}catch (IOException exception) {exception.printStackTrace();}
    }

    public boolean checkWord(String word)
    {
    	return dictionary.contains(word);
    }
}
