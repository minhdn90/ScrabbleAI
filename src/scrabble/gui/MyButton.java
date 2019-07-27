package scrabble.gui;

import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JButton;

public class MyButton extends JButton{
	Image img;

	public MyButton(){}
	
	public MyButton(String value, int w, int h, int locX, int locY, Image _img)
	{
		super(value);
		this.setBounds(locX, locY, w, h);
		img = _img;
		if (img != null)
			img = img.getScaledInstance(w, h, Image.SCALE_AREA_AVERAGING);
	}
	
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		g.drawImage(img, 0, 0, null);
	}
}
