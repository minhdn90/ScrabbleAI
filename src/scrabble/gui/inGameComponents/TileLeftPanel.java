package scrabble.gui.inGameComponents;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JLabel;
import javax.swing.JPanel;

import scrabble.dataservice.GameClient;
import scrabble.game.Board;

public class TileLeftPanel extends JPanel{
	private GameClient client;
	Image bag;
	JLabel num;
	
	public TileLeftPanel(GameClient _client, int w, int h)
	{
		client = _client;
		setOpaque(false);
		setLayout(null);

		try{
			bag = ImageIO.read(new File("images/BAG.png"));
			bag = bag.getScaledInstance(w, h, Image.SCALE_AREA_AVERAGING);
		}catch(IOException e){
			e.printStackTrace();
		}
		num = new JLabel("100");
		num.setFont(new Font("Serif", Font.BOLD, 22));
		num.setForeground(Color.white);
		num.setBounds(w/4, h/3+5, 40, 30);
		add(num);
	}
	
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		g.drawImage(bag, 0, 0, null);
		if (client.hasStarted())
			num.setText("" + client.countTile());
		else
			num.setText("100");
	}
	
}
