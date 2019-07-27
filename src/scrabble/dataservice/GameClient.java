/* WORKING TODAY :
    Processing error codes;
    COmplete missing functions;
    Handling exceptions;
 */

/* GUI functions
 * repaint()
 * showMessage()
 *
 */
package scrabble.dataservice;


import java.util.*;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.Vector;
import java.io.IOException;
import java.io.InputStreamReader;


import scrabble.game.*;
import scrabble.gui.MainFrame;
import scrabble.*;

public class GameClient extends Thread {

	public static final String host = "127.0.0.1";
	public static final int port = 10003;
	public static final int size = 15;

	//public int timeOut;

	private BufferedReader inFromUser;
	private BufferedReader inFromServer;
	private DataOutputStream outToServer;
	private Socket skt;
	private Player player;
	private Board board;
	private int turn;
	private Vector<Player> playerList;
	private Player currentPlayer = null;
	private Vector<LetterMove> currentMove;
        private Vector<Tile> tmpRack;
	private boolean isMaster;
	private boolean stop = false;
	private boolean hasStarted = false;

	private MainFrame GUI;

	/* Client Constructor */
	public GameClient(Socket _skt, String playerName, boolean _isMaster, MainFrame _GUI ) throws IOException
	{
		skt = _skt;
		turn = 0;
		setMaster(_isMaster);

		player = new Player(playerName, isMaster);
		board = new Board();
		playerList = new Vector<Player> ();
		currentMove = new Vector<LetterMove> ();
		playerList.add(player);
		GUI = _GUI;

		//timeOut = 0;
		inFromUser = new BufferedReader(new InputStreamReader(System.in));
		inFromServer = new BufferedReader(new InputStreamReader(skt.getInputStream()));
		outToServer = new DataOutputStream(skt.getOutputStream());
		System.out.println(skt.toString());
		String nameMessage = "NAME " + playerName + "\n";
		outToServer.writeBytes(nameMessage);
		System.out.println(nameMessage);
		GUI.setInGameScreen(this);
	}



	public void setPlayer(Player _player)    {

		player = _player;

	}

	public void setBoard(Board _board)  {

		board = _board;
	}

	public Board getBoard() {

		return board;
	}

	public boolean isMaster()  {

		return isMaster;
	}

	public void setMaster(boolean _isMaster)    {

		isMaster = _isMaster;
	}

	public void setCurrentPlayer(String usernameSentByHost)  
	{
		for (Player i : playerList){
			if ( i.getUsername().compareTo(usernameSentByHost) == 0) {
				currentPlayer = i;
				currentPlayer.setTurn(true);
			}
			else{
				i.setTurn(false);
			}
		}
	}





	public void addPlayerToList(Player newPlayer)  {

		playerList.add(newPlayer);
	}

	public void removePlayerFromList(String playerName) {

		for ( int i = 0; i < playerList.size(); i ++ )  {

			if ( playerList.get(i).getUsername().compareTo(playerName) == 0 )  {

				playerList.remove(i);
			}
		}
	}


	/* Check if "my" player is in turn or not */
	public boolean isTurn() {
		if (currentPlayer == null)
			return false;
		if ( currentPlayer.getUsername().compareTo(player.getUsername()) == 0 ) return true;
		return false;
	}


	public void sendMessage(String msg)
	{
		try
		{
			// wait 1 seconds then send a msg
			//Thread.sleep(1100L);
			outToServer.writeBytes(msg);
		} catch (Exception e){
			e.printStackTrace();
			GUI.setStartGameScreen();
			this.quit();
		}
	}


	public String waitServerMsg(String inquery) throws IOException
	{
		String serverMsg = inFromServer.readLine();
		while (!serverMsg.startsWith(inquery))
		{
			//System.out.println(serverMsg);
			serverMsg = inFromServer.readLine();
		}
		return serverMsg;
	}

	public void receiveLetter(int tileID) {

		Tile newTile = new Tile(tileID);
		player.addTile(newTile);

		// GUI.receiveLetter(int tileID);
	}

	public void requestExchange() {

		String requestMsg = "EXCHANGE" + "\n";
		sendMessage(requestMsg);
		player.getRack().clear();
		GUI.displayMessage("You have requested to exchange tiles.");
	}
	/* Move Processing : place letter, remove letter from board, submit Word */
	public void placeLetter(int tileID, int x, int y) {
		LetterMove tempMove = new LetterMove(x, y, tileID);
                currentMove.add(tempMove);
                if (this.isTurn())
                    for (Tile t: player.getRack())
                    {
                        if (t.getID() == tileID)
                        {
                            player.getRack().remove(t);
                            break;
                        }
                    }
	}
	
	public void callPlaceLetter(int tileID, int x, int y)
	{
		placeLetter(tileID, x, y);
		String message = "PLACE " + tileID + " " + x + " " + y + "\n";
		sendMessage(message);
	}

	public void removeLetter(int x, int y) {
		for (LetterMove i:currentMove){
                    if (i.x == x && i.y == y)
                    {
                        if (this.isTurn())
                            player.getRack().add(i.getTile());
                        currentMove.remove(i);
                        break;
                    }
		}
	}
	
	public void callRemoveLetter(int x, int y)
	{
		removeLetter(x, y);
		String message = "REMOVE" + " " + x + " " + y + "\n";
		sendMessage(message);
	}

	public void submitWord()   {

		String requestMsg = "SUBMIT" + "\n";
		sendMessage(requestMsg);
                pause(400);
	}

	public void checkWordResult(String getCommand)   {
		if ( getCommand.startsWith("ACCEPT"))   {
			board.update(currentMove);
			//GUI.redisplay();
			currentMove.clear();
                        if (this.isTurn())
                            GUI.displayMessage("You have submitted successfully.");
			GUI.redisplay();
		}

		else if ( getCommand.startsWith("REFUSE")) {
			GUI.displayMessage("Your submitted word is incorrect.");
		}

	}
	/* End move processing */

	public void quit()
	{
		this.sendMessage("QUIT " + player.getUsername() + "\n");
		closeSocket();
	}


	public boolean canStart()   {

		if ( isMaster() && playerList.size() >= 1 )
			return true;
		else return false;
	}

	public void callStartGame() {

		String startMessage = "START_GAME" + "\n";
		try
		{
			outToServer.writeBytes(startMessage);
		//	System.out.println(startMessage);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
	}

	public void resign()    {
		sendMessage("SURRENDER " + player.getUsername() + "\n");
		player.setResign(true);
	}

	public void requestPass()   {
                player.rack = tmpRack;
		currentMove.clear();
		GUI.redisplay();
		String passMessage = "PASS" + " " + player.getUsername() + "\n";
		sendMessage(passMessage);
		GUI.displayMessage("You have requested to pass your turn.");
	}

	public int countTile()  {

		int tileOnBoard = 0;
		for (int i = 0; i < size; i ++) {
			for ( int j = 0; j < size; j ++)    {
				Square thisSquare = board.getSquare(i, j);
				if ( thisSquare.isOccupied() ) tileOnBoard ++;
			}
		}
		int tileOnHands = 0;
		tileOnHands = 7*playerList.size();
		/*for ( int i = 0; i < playerList.size(); i ++ )  {
			tileOnHands += playerList.get(i).getRack().size();
		}*/
		//System.out.println(tileOnBoard);
	//	System.out.println(tileOnHands);
		int tileOnBag = 100 - tileOnBoard - tileOnHands;
		if (tileOnBag < 0){
			tileOnBag = 0;
		}
		return tileOnBag;

	}

	public void endGame()   {
                GUI.displayMessage("GAME END!");
                for (Player p : playerList)
                {
                    GUI.displayMessage(p.getUsername() + " scores " + p.getScore() + " points.");
                }
		GUI.endGame();
	}

	public void closeSocket() {
		stop = true;
		try{
                    skt.close();
		}catch (IOException e){
			e.printStackTrace();
		}
	}

	public boolean preparation() throws IOException   {

		//String playerName = inFromUser.readLine();
		//setPlayer(new Player(playerName));
	//	System.out.println("PREPARATION");
		String getCommand = "";
		while ( !stop ) {
			pause(100);
			getCommand = inFromServer.readLine();
			System.out.println("Command: " + getCommand);

			if ( getCommand.startsWith("START")) {

				String startGame = "GAME STARTED";
				GUI.displayMessage(startGame);
				hasStarted = true;
				return true;
			}
			else if(getCommand.startsWith("JOIN"))  {

				String playerName = getCommand.split(" ")[1];
				if (!playerName.equals(player.getUsername()))
					addPlayerToList(new Player(playerName, false));
				// GUI display joining player
				//GUI.redisplay();
				//System.out.println(playerName);
				GUI.displayMessage(playerName + " has joined game.");
				GUI.redisplay();
			}
			else if(getCommand.startsWith("LEAVE"))  {

				String playerName = getCommand.split(" ")[1];
				removePlayerFromList(playerName);
				// GUI display removing player
				GUI.redisplay();
			}
			else if(getCommand.startsWith("ERROR"))  {

				String errorMessage = "Room is already full";
				this.closeSocket();
				GUI.setStartGameScreen();
				GUI.displayMessage("The room is full.");
				return false;
			}
			else if (getCommand.startsWith("QUIT"))  {
				String username = getCommand.split(" ")[1];
				quitHandler(username);
			}

		}
		return false;

	}
	
	private void surrenderHandler(String username)
	{
		for (Player i:playerList){
			if (i.getUsername().equals(username)){
				i.setResign(true);
				break;
			}
		}
	}
	
	private void quitHandler(String username)
	{
		/* Remove the quitted player from list */
		
		for ( int i = 0; i < playerList.size(); i ++ )  {
			if ( playerList.get(i).getUsername().equals(username))   {
				playerList.remove(i);
			}
		}
		System.out.println("Trong quithandler: " + playerList.size());
		String noticeMessage = username + " has left the game";
		GUI.displayMessage(noticeMessage);
		GUI.redisplay();
	}

	private void pause(int time)
	{
		try{
			Thread.sleep(time);
		}catch (InterruptedException e){
			e.printStackTrace();
		}
	}

	public void mainGame() throws IOException {

		String getCommand = "";
		String receivedTile = "";

		while ( !stop )  {
			pause(100);
			try {
				getCommand = inFromServer.readLine();
				System.out.println("Command nhan dc: " + getCommand);
				if (getCommand.startsWith("TURN")) {
					//turn ++;
					String currentPlayerName = getCommand.split(" ")[1];
					setCurrentPlayer(currentPlayerName);
					
					GUI.startTurn(currentPlayerName);
					currentMove.clear();
					if (this.isTurn())
                                        {
                                            GUI.displayMessage("You are in turn");
                                            tmpRack = new Vector(player.getRack());
                                        }
					else
						GUI.displayMessage(currentPlayerName + " is in turn.");
					GUI.redisplay();
				}

				else if(getCommand.startsWith("PLACE"))  {
					if (!this.isTurn()){
						int tileID = Integer.parseInt(getCommand.split(" ")[1]);
						int x = Integer.parseInt(getCommand.split(" ")[2]);
						int y = Integer.parseInt(getCommand.split(" ")[3]);
						placeLetter(tileID, x, y);
						GUI.redisplay();
					}
				}
				else if(getCommand.startsWith("REMOVE"))  {
					if (!this.isTurn()){
						int x = Integer.parseInt(getCommand.split(" ")[1]);
						int y = Integer.parseInt(getCommand.split(" ")[2]);
						removeLetter(x, y);
						GUI.redisplay();
					}
				}
				else if(getCommand.startsWith("MESSAGE"))  {

					String username = getCommand.split(" ")[1];
					String message = getCommand.split(" ")[2];
					/* User Interface display the message */
				}
				else if(getCommand.startsWith("TILE"))  {

					String strTileID = getCommand.split(" ")[1];
					int tileID = Integer.parseInt(strTileID);
					receiveLetter(tileID);
					receivedTile = receivedTile + Constants.tileLetter[tileID];
					if (player.getRack().size() == 7){
						GUI.displayMessage("Received tiles: " + receivedTile);
						receivedTile = "";
						GUI.redisplay();
					}
				}
				else if(getCommand.startsWith("SET_SCORE"))  {
                                        
					String wordScore = getCommand.split(" ")[1];
					int score = Integer.parseInt(wordScore);
					currentPlayer.addScore(score);
					/* User Interface display the score accordingly  */
					GUI.redisplay();
				}
				else if(getCommand.startsWith("PASS"))  {

					String username = getCommand.split(" ")[1];
					GUI.displayMessage("Player " + username + " has passed his turn");
					currentMove.clear();
					GUI.redisplay();
				}

				else if(getCommand.startsWith("END_GAME"))
                                {
					endGame();
					break;
				}
				else if(getCommand.startsWith("QUIT"))  {
					String username = getCommand.split(" ")[1];
					quitHandler(username);
				}
				else if(getCommand.startsWith("SURRENDER"))  {
					String username = getCommand.split(" ")[1];
					surrenderHandler(username);
					GUI.displayMessage(username + " has resigned.");
				}
				else if(getCommand.startsWith("ACCEPT") || getCommand.startsWith("REFUSE"))  {
					{
						String backMessage = getCommand.split(" ")[0];
						this.checkWordResult(backMessage);
					}

				}

			}
			catch(Exception e)  {
                            if (skt.isClosed()){
                                GUI.displayMessage("Host has quit the game.");
                                //endGame();
                            }
			}

		}

	}

	public Player getPlayer()
	{
		return player;
	}

	public void run()    {
		//while ( true )  {
		try {
			if (preparation())
				mainGame();
			//else break;
		}
		catch ( IOException e)  {
			e.printStackTrace();
		}
	}

	public Vector<Player> getPlayerList()
	{
		return playerList;
	}
	
	public boolean hasStarted()
	{
		return hasStarted;
	}
	
	public Vector<LetterMove> getCurrentMove()
	{
		return currentMove;
	}

	/*
        public static void main(String[] args)
	{
            try {
            	GameClient client = new GameClient(host, port);
		client.play();
            } catch (IOException e)	{
		System.err.println(e);
            }
        }
	 */



};
