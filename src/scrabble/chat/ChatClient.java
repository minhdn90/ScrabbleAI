package scrabble.chat;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.Map.*;

import scrabble.gui.InGamePanel;
import scrabble.gui.MainFrame;
import scrabble.gui.inGameComponents.ChatPanel;

public class ChatClient
    extends Thread {
  InetAddress chatGroup;
  MulticastSocket chatSocket;

  private boolean done = false;

  private DatagramPacket chatDatagram;
  private static final int CHAT_PORT = 10987;
  String name;
  private int length;
  private InGamePanel inGamePanel;
  
  public ChatClient(String _name, int _length, InGamePanel f) {
    try {
      chatGroup = InetAddress.getByName("228.9.8.7");
      chatSocket = new MulticastSocket(CHAT_PORT);
      chatSocket.joinGroup(chatGroup);
      chatSocket.setTimeToLive(1); // run on local only
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }

    name = _name;
    length = _length;
    inGamePanel = f;
  }

  public void finish()
  {
      done = true;
  }

  public void reset()
  {
      done = false;
      this.start();
  }

  byte[] convert(String message)
  {
      byte[] buf = new byte[length];
      for (int i=0; i<message.length(); i++)
      {
        buf[i] = (byte)message.charAt(i);
      }
      return buf;
  }

    String convert(byte[] buf)
    {
        String ans = "";
        for (int i = 0; i < buf.length; i++) {
            if (buf[i] == 0) break;
            ans += (char) buf[i];
        }
        return ans;
    }

    public void sendMessage(String message)
    {
    	message = name + ": " + message;
        byte[] buf = convert(message);

        chatDatagram = new DatagramPacket(buf, buf.length,
                                    chatGroup, CHAT_PORT);
        try
        {
            chatSocket.send(chatDatagram);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
  public void run() 
  {
      byte[] buf = new byte[1000];
      DatagramPacket recv = new DatagramPacket(buf, buf.length);
      while (!done)
      {
        try
        {
        	Thread.sleep(200);
            chatSocket.receive(recv);
            inGamePanel.displayChat(convert(recv.getData()));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
  }
}
