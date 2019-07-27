package scrabble.gui.inGameComponents;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import scrabble.Player;

public class PlayerPanel extends JPanel{
	private Vector<PlayerBoxPanel> players;
	private Vector<Player> playerList;
	private Vector<PlayerBoxPanel> panelList;
	Box box = Box.createHorizontalBox();
	
	public PlayerPanel(Vector<Player> _playerList)
	{
		playerList = _playerList;
		//System.out.println(playerList.size());
		setLayout(new BorderLayout());
		setOpaque(false);
		panelList = new Vector<PlayerBoxPanel>();
		for (int i = 0; i < 4; i++){
			PlayerBoxPanel p = new PlayerBoxPanel(i);
			panelList.add(p);
			box.add(p);
			box.add(Box.createHorizontalStrut(10));
		}
		add(box, BorderLayout.WEST);
	}
	
	public void redisplay()
	{
		System.out.println("So luong player:" + playerList.size());
		for (PlayerBoxPanel i: panelList){
			i.setVisible(false);
		}
		int count = 0;
		for (Player i : playerList){
			panelList.get(count).setPlayer(i);
			panelList.get(count).setVisible(true);
			count++;
		}
	}
}

class PlayerBoxPanel extends JPanel{
	private static int WIDTH = 150;
	private static int HEIGHT = 75;
	String username;
	JLabel nameLabel, scoreLabel;
	boolean inTurn, isResigned;
	int score;
	Image masterImg, img;
	StatusPanel sttPanel;
	
	public PlayerBoxPanel(int t)
	{
		this.setVisible(false);
		
		// nameLabel
		nameLabel = new JLabel("");
		nameLabel.setBounds(10, 5, 150, 30);
		nameLabel.setFont(new Font("Arial", Font.BOLD, 18));
		nameLabel.setForeground(Color.black);
		add(nameLabel);
		// score label
		score = 0;
		scoreLabel = new JLabel("");
		scoreLabel.setBounds(100, 40, 50, 30);
		scoreLabel.setFont(new Font("Courier New", Font.BOLD, 23));
		scoreLabel.setForeground(Color.white);
		add(scoreLabel);
		// status
		sttPanel = new StatusPanel("");
		sttPanel.setVisible(false);
		sttPanel.setBounds(10, 42, 50, 25);
		add(sttPanel);
	
		try{
			if (t > 0)
				img = ImageIO.read(new File("images/PLAYER.png"));
			else
				img = ImageIO.read(new File("images/CURRENTPLAYER.png"));
		}catch (IOException e){
			e.printStackTrace();
		}
		Dimension size = new Dimension(WIDTH, HEIGHT);
	    setPreferredSize(size);
	    setMinimumSize(size);
	    setMaximumSize(size);
	    setSize(size);
	    setLayout(null);
	}
	
	public void setPlayer(Player p)
	{
		// nameLabel
		nameLabel.setText(p.getUsername());
		// score label
		score = 0;
		scoreLabel.setText(p.getScore()+"");
		// status
		if (p.resigned()){
			sttPanel.setText("RESIGN");
		}else if (p.isInTurn()){
			sttPanel.setText("TURN");
		}else{
			sttPanel.setVisible(false);
		}
		//isMaster = p.isMaster();
	}
	
	public void paintComponent(Graphics g) 
	{
	    super.paintComponent(g);
    	g.drawImage(img, 0, 0, WIDTH, HEIGHT, null);
	}
	
	class StatusPanel extends JPanel{
		String text;
		Image img;
		JLabel stt;
		
		public StatusPanel(String t)
		{
			text = t;
			try{
				img = ImageIO.read(new File("images/STATUS.png"));
			}catch (IOException e){
				e.printStackTrace();
			}
			stt = new JLabel(text);
			int w = 50;
			int h = 30;
			stt.setBounds(5, 5, w, h);
			img.getScaledInstance(w, h, Image.SCALE_AREA_AVERAGING);
			add(stt);
		}
		
		public void setText(String s)
		{
			setVisible(true);
			stt.setText(s);
		}
		
		public void paintComponent(Graphics g) 
		{
		    super.paintComponent(g);
		    g.drawImage(img, 0, 0, null);
		}
	}
	
}
