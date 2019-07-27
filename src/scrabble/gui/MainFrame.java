package scrabble.gui;

import javax.swing.JFrame;

import scrabble.chat.ChatClient;
import scrabble.dataservice.GameClient;

enum STATUS {LOGIN, IN_ROOM};

public final class MainFrame extends JFrame {
	// default sizes
	private static int WIDTH = 800;
	private static int HEIGHT = 600;
	// starting location
	private static int LOC_X = 100;
	private static int LOC_Y = 100;
	
	private StartGamePanel startGamePanel = null;
	private InGamePanel inGamePanel = null;
	GameClient client;
	private STATUS status;
	
	
	public MainFrame()
	{
		setTitle("Scrabble game");
		setSize(WIDTH, HEIGHT);
		setLocation(LOC_X, LOC_Y);
		setResizable(false);
		
		// set start game screen
		setStartGameScreen();
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);	
		
	}
	
	// change the screen into start game screen
	public void setStartGameScreen()
	{
		if (inGamePanel != null){
			inGamePanel.close();
			inGamePanel.setVisible(false);
		}
		
		if (startGamePanel == null){
			startGamePanel = new StartGamePanel(this);
			add(startGamePanel);
		}else{
			startGamePanel.closeServer();
			startGamePanel.setVisible(true);
		}
		status = STATUS.LOGIN;
		redisplay();
	}
	
	// change the screen into in game screen
	public void setInGameScreen(GameClient _client)
	{
		if (startGamePanel != null){
			startGamePanel.setVisible(false);
		}
		client = _client;
	
		if (inGamePanel == null){
                    inGamePanel = new InGamePanel(this, client);
                    add(inGamePanel);
		}else{
                    this.remove(inGamePanel);
                    inGamePanel = new InGamePanel(this, client);
                    add(inGamePanel);
		}
		inGamePanel.setVisible(true);
		status = STATUS.IN_ROOM;
		redisplay();
	}
	
	// redisplay the screen
	public void redisplay()
	{
		if (status == STATUS.IN_ROOM){
			inGamePanel.redisplay();
		}
		else{
			startGamePanel.repaint();
		}
	//	this.paintAll(this.getGraphics());
	}
	
	/**
	 * End game
	 */
	public void endGame()
	{
		
	}
	/**
	 * Start game
	 */
	public void startGame()
	{
		
	}
	
	public void startTurn(String username)
	{
		
	}
	/**
	 * Call when this user has ended his turn
	 */
	public void endTurn()
	{
		inGamePanel.endTurn();
	}
	
	/**
	 * Display a system message
	 */
	public void displayMessage(String s)
	{
		if (status == STATUS.LOGIN)
			startGamePanel.displayMessage(s);
		else
			inGamePanel.displayMessage(s);
	}
}
