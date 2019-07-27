package scrabble.gui;

import java.awt.*; 
import java.awt.event.*;
import java.io.*;
import java.net.Socket;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;

import scrabble.dataservice.*;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class StartGamePanel extends JPanel{
	private MainFrame mainFrame;
	private PlayerNamePanel playerNamePanel;
	private HostPanel hostPanel;
	private JButton joinGameButton,
					createGameButton,
					cancelButton,
					showGameButton;
	private MulticastDiscoverer md = null;
	private MulticastAnnouncer ma = null;
	private GameServer server = null;
	Image bgimage;
	
	String hostName = null;
	
	public StartGamePanel(MainFrame f)
	{
		mainFrame = f;
		setLayout(null);
		md = new MulticastDiscoverer();
		md.start();

		addPlayerNameBox();
		addHostBox();
		addJoinGameButton();
		addCancelButton();
		addCreateGameButton();
		addShowGameButton();	
		try{
			bgimage = ImageIO.read(new File("images/background.png"));
			bgimage = bgimage.getScaledInstance(800, 600, Image.SCALE_AREA_AVERAGING);
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	private void addPlayerNameBox()
	{
		playerNamePanel = new PlayerNamePanel();
		playerNamePanel.setBounds(15, 15, 400, 75);
		playerNamePanel.setOpaque(false);
		add(playerNamePanel);
	}
	
	private void addHostBox()
	{
		JLabel hostLabel = new JLabel("Games");
		hostLabel.setFont(new Font("Serif", Font.BOLD, 18));
		hostLabel.setBounds(50, 100, 100, 50);
		add(hostLabel);
		hostPanel = new HostPanel(this);
		hostPanel.setBounds(45, 150, 500, 300);
	//	hostPanel.setOpaque(false);
		add(hostPanel);
	}
	
	private JButton newButton(String name)
	{
		JButton b = new JButton(name);
		b.setPreferredSize(new Dimension(150, 30));
		return b;
	}
	
	// button to join game
	private void addJoinGameButton()
	{
		joinGameButton = newButton("Join game");
		joinGameButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent event)
			{
				String name = playerNamePanel.textField.getText();
				if (isValidName(name)){
					createGameClient(name, false);
					InGamePanel scr = new InGamePanel();
				}
				else{
					displayMessage("Your username is invalid.");
				}
			}
		});
		joinGameButton.setBounds(600, 60, 150, 30);
		add(joinGameButton);
	}
	
	// button to quit game
	private void addCancelButton()
	{
		cancelButton = newButton("Cancel");
		cancelButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent event)
			{
				System.exit(0);
			}
		});
		cancelButton.setBounds(600, 100, 150, 30);
		add(cancelButton);
	}
	
	/**
	 * Button to create a new game
	 */
	private void addCreateGameButton()
	{
		createGameButton = newButton("Create game");
		createGameButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent event)
			{
				String name = playerNamePanel.textField.getText();
				if (isValidName(name)){
					createServer(name);
				}
				else{
					displayMessage("Your host's name is invalid.");
				}
			};
		});
		createGameButton.setBounds(100, 480, 150, 30);
		add(createGameButton);
	}
	
	/**
	 * Button to show available games
	 */
	private void addShowGameButton()
	{
		showGameButton = newButton("Show games");
		showGameButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent event)
			{
				hostPanel.updateGameList(md.getGameList());
				hostName = null;
			}

		});
		showGameButton.setBounds(350, 480, 150, 30);
		add(showGameButton);
	}
	
	// check whether the entered name is valid or not
	private boolean isValidName(String name)
	{
		Pattern pattern = Pattern.compile("^[a-zA-Z0-9_-]{3,15}$");
		Matcher matcher = pattern.matcher(name);
		return matcher.matches();
	}
	
	// create a new server
	private void createServer(String name)
	{
		hostName = name;
		try{
			server = new GameServer(6789);
			server.start();
			ma = new MulticastAnnouncer(hostName);
			ma.start();
			pause(500);
			createGameClient(name, true);
			setVisible(false);
			
		}catch (Exception e){
			e.printStackTrace();
		}
	}
	
	// create a new client
	private void createGameClient(String username, boolean isMaster)
	{
		try{
			Socket skt = md.joinGame(hostName);
			GameClient client = new GameClient(skt, username, isMaster, mainFrame);
            client.start();
		}catch (IOException e){
			e.printStackTrace();
			if (hostName == null){
				displayMessage("You have not selected host.");
			} else{
				displayMessage("The selected host is not exist.");
			}
		}
	}
	
	//pause
	private void pause(int miliseconds)
	{
		try{
		    Thread.sleep(miliseconds);
		}catch (InterruptedException io){}
	}
	
	/**
	 * Display a message to user
	 */
	public void displayMessage(String s)
	{
		JOptionPane.showMessageDialog(null, s);
	}
	
	public void closeServer()
	{
		if (ma != null){
			ma.finish();
			ma = null;
		}
		if (server != null){
			System.out.println("Server finish");
			server.finish();
		}
	}
	
	protected void paintComponent(Graphics g) 
	{
		super.paintComponent(g);
	    g.drawImage(bgimage, 0, 0, null);
	}
	
	// destructor
	/*protected void finalize() throws Throwable
	{
		if (md != null){
			md.finish();
		}
		if (ma != null){
			ma.finish();
		}
	  super.finalize(); //not necessary if extending Object.
	} */
}

class PlayerNamePanel extends JPanel{
	Box playerNameBox;
	JTextField textField;
	
	public PlayerNamePanel()
	{
		int strut = 10;
		playerNameBox = Box.createVerticalBox();
		JLabel nameLabel = new JLabel("Player name");
		nameLabel.setFont(new Font("Serif", Font.BOLD, 18));
		playerNameBox.add(nameLabel);
		playerNameBox.add(Box.createVerticalStrut(strut));
		// name box
		textField = new JTextField(30);
		playerNameBox.add(textField);
		add(playerNameBox);
	}
}

/**
 * Panel containing hosts
 * @author tienthanh411
 *
 */
class HostPanel extends JPanel{
	Box hostBox;
	Set<String> gameList = new HashSet();
	StartGamePanel f;
	
	public HostPanel(){}
	
	public HostPanel(StartGamePanel _f)
	{
		f = _f;
		setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		hostBox = Box.createVerticalBox();
	    add(hostBox);
	}
	
	public void updateGameList(Set<String> ls)
	{
		gameList = ls;
		//this.removeAll();
		hostBox.removeAll();
		
		Iterator it = gameList.iterator();
	    while(it.hasNext()){
	    	String value=(String)it.next();
	    	//System.out.println(value);
	    	JButton b = new JButton(value);
	    	b.setContentAreaFilled(false);
	    	//b.setPreferredSize(new Dimension(400, 30));
	    	b.setBorderPainted(false);
	    	b.addActionListener(new HostActionListener(value, f));
	    	b.setVisible(true);
	    	hostBox.add(b);
	    } 
	    //add(hostBox);
	    this.paintAll(this.getGraphics());
	}
	
}

class HostActionListener implements ActionListener{
	String value;
	StartGamePanel f;
	
	public HostActionListener(String s, StartGamePanel _f)
	{
		value = s;
		f = _f;
	}
	
	public void actionPerformed(ActionEvent e) 
	{ 
		f.hostName = value;
    }
}


/*class TransparentButton extends JButton {
	private float a = 0.5f;
	public TransparentButton(String text) { 
	    super(text);
	    setOpaque(false); 
	} 
	    
	public void paintComponent(Graphics g) {
		//super.paintComponent(g);
	    Graphics2D g2 = (Graphics2D) g.create(); 
	    g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, a)); 
	    super.paintComponent(g2); 
	    g2.dispose(); 
	} 
}*/
