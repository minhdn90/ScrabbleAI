package scrabble.gui;

import java.awt.*; 
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.ColorModel;
import java.io.*;
import java.net.Socket;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;

import scrabble.chat.ChatClient;
import scrabble.dataservice.*;
import scrabble.game.Board;
import scrabble.Player;
import scrabble.gui.inGameComponents.*;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InGamePanel extends JPanel{
	private MainFrame mainFrame;
	private PlayerPanel playerPanel;
	private ChatPanel chatPanel;
	private GamePanel gamePanel;
	private Board board;
	private Player player;
	private Vector<Player> playerList;
	private GameClient client;
	Image bgimage = null;
	private ChatClient cc;
	
	public InGamePanel(){}
	
	public InGamePanel(MainFrame f, GameClient _client)
	{
		setLayout(null);
		mainFrame = f;
		set(_client);
		
		addPlayerPanel();
		addGamePanel();
		addChatPanel();
		addStartGameButton();
		addResignButton();
		addQuitGameButton();
		try{
			bgimage = ImageIO.read(new File("images/background.png"));
			bgimage = bgimage.getScaledInstance(800, 600, Image.SCALE_AREA_AVERAGING);
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	public void set(GameClient _client)
	{
		client = _client;
		board = client.getBoard();
		player = client.getPlayer();
		playerList = client.getPlayerList();
		cc = new ChatClient(player.getUsername(), 200, this);
		cc.start();
	}
	
			
	protected void paintComponent(Graphics g) 
	{
		super.paintComponent(g);
	    g.drawImage(bgimage, 0, 0, null);
	}
	
	// add panel contains players
	private void addPlayerPanel()
	{
		playerPanel = new PlayerPanel(playerList);
		playerPanel.setBounds(20, 5, 800, 80);
		add(playerPanel);
	}
	
	// add chat panel
	private void addChatPanel()
	{
		chatPanel = new ChatPanel(cc);
		chatPanel.setBounds( 530, 200, 250, 360);
		add(chatPanel);
	}
	
	// add panel contains game stuffs
	private void addGamePanel()
	{
		gamePanel = new GamePanel(client);
		gamePanel.setBounds(0, 80, 550, 500);
		add(gamePanel);
	}
	
	/**
	 * Button to start game
	 */
	private void addStartGameButton()
	{
		if (client.isMaster()){
			final JButton b = new MyButton("Start game", 120, 30, 600, 100, null);
			b.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e)
				{
					if (!client.hasStarted() && client.canStart()){
						client.callStartGame();
                                                b.setVisible(false);
					}
				}
			});
			add(b);
		}
	}
	
	/**
	 * Button to resign
	 */
	private void addResignButton()
	{
		JButton b = new MyButton("Resign", 120, 30, 530, 150, null);
		b.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e)
			{
				if (client.hasStarted()){
					player.setResign(true);
					client.resign();
					mainFrame.redisplay();
					displayMessage("You have resigned.");
				}
			}
		});
		add(b);
	}
	
	/**
	 * Button to quit game
	 */
	private void addQuitGameButton()
	{
		JButton b = new MyButton("Quit", 120, 30, 660, 150, null);
		b.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e)
			{
				client.quit();
				mainFrame.setStartGameScreen();
			}
		});
		add(b);
	}
	/**
	 * Display a message to user
	 */
	public void displayMessage(String s)
	{
		chatPanel.displayMessage(s);
	}
	
	public void displayChat(String s)
	{
		chatPanel.addChat(s);
	}
	
	public void close()
	{
		cc.finish();
		// TODO: close socket, send quit (quit game dong thoi close socket)
	}
	
	/**
	 * Call when this player ends turn
	 */
	public void endTurn()
	{
		
	}
	
	public void redisplay()
	{
		chatPanel.repaint();
		gamePanel.redisplay();
		playerPanel.redisplay();
	}

}


