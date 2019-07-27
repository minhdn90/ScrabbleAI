package scrabble.gui.inGameComponents;

import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import scrabble.chat.ChatClient;

public class ChatPanel extends JPanel{
	private JTextArea chatBox;
	private JScrollPane scrollPane;
	private JTextField chatField;
	private ChatClient cc;
	
	public ChatPanel(ChatClient _cc)
	{
		cc = _cc;
		setLayout(null);
		setOpaque(false);
		scrollPane = new JScrollPane();
		chatBox = new JTextArea();
		chatBox.setColumns(18);
		chatBox.setRows(10);
		chatBox.setEditable(false);
		scrollPane.setViewportView(chatBox);
		scrollPane.setBounds(0, 0, 250, 330);
		add(scrollPane);

		chatField = new JTextField(20);
		chatField.setBounds(60, 330, 190, 30);
		chatField.addKeyListener(new MyKeyListener());
		add(chatField);
		JButton b = new JButton("Chat");
		b.setBounds(0, 330, 60, 30);
		add(b);
	}
	/**
	 * Scrolldown the chat area
	 */
	private void scrollDown()
	{
		chatBox.selectAll();
		int x = chatBox.getSelectionEnd();
		chatBox.select(x,x);
	}
	
	/**
	 * Display a message to user
	 */
	public void displayMessage(String s)
	{
		s = s.toUpperCase();
		chatBox.append("\n" + s + "\n\n");
		scrollDown();
	}
	
	public void addChat(String s)
	{
		chatBox.append(s + "\n");
		scrollDown();
	}
	
	class MyKeyListener extends KeyAdapter{
		public void keyPressed(KeyEvent e) {
		     int key = e.getKeyCode();
		     try{
			     if (key == KeyEvent.VK_ENTER) {
			    	 String s = chatField.getText();
			    	 cc.sendMessage(s);
			    	 chatField.setText("");
			     }
		     }catch (NullPointerException npe){
		    	 System.out.println("WTF");
		     }
		}
	}
}


