package scrabble.dataservice;

/**
 *
 * @author Peral
 */
import java.io.IOException;
import java.net.*;
import java.util.*;
import java.util.Map.*;

public class MulticastDiscoverer extends Thread
        implements Runnable {

    InetAddress discoveryGroup;
    MulticastSocket discoverySocket;
    DatagramPacket recv;
    Map<String, InetAddress> game_host = new HashMap();
    Map<InetAddress, Long> timeStamp = new HashMap();
    private boolean done = false;
// half a second btw them
    private static final long HEARTBEAT_TIME = 2000;
    private static final int DISCOVERY_PORT = 8976;
    private static final int TCP_PORT = 6789;

    public MulticastDiscoverer()
    {
        try {
            discoveryGroup = InetAddress.getByName("228.9.8.7");
            discoverySocket = new MulticastSocket(DISCOVERY_PORT);
            discoverySocket.joinGroup(discoveryGroup);
// just run on our local sub-net
            discoverySocket.setTimeToLive(1);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        // create and start listen thread  on discovery socket
        new Thread(this).start();
    }

    public Socket joinGame(String hostName)
            throws IOException
    {
        InetAddress address = game_host.get(hostName);
        long time = timeStamp.get(address);
        Socket tcpSocket = new Socket();
        tcpSocket.connect(new InetSocketAddress(address, TCP_PORT));
        return tcpSocket;
    }

    public Set getGameList()
    {
        return game_host.keySet();
    }

    String convert(byte[] buf)
    {
        String ans ="";
        for (int i=0; i<buf.length; i++)
        {
        	if (buf[i] == 0)
        		break;
            ans += (char)buf[i];
        }
        return ans;
    }

    public void finish()
    {
        done = true;
        game_host.clear();
        timeStamp.clear();
    }

    public void reset()
    {
        done = false;
        this.run();
    }

    @Override
    public void run()
    {
        byte[] buf = new byte[1000];
        DatagramPacket recv = new DatagramPacket(buf, buf.length);
        while (!done) {
            try {
                discoverySocket.receive(recv);
                String hostName = convert(recv.getData());
                if (!game_host.containsValue(recv.getAddress())) {
                    if (game_host.containsKey(hostName))
                    {
                        hostName = hostName + System.currentTimeMillis();
                    }
                    game_host.put(hostName, recv.getAddress());
                    timeStamp.put(recv.getAddress(), System.currentTimeMillis());
                } else
                {
                    timeStamp.remove(recv.getAddress());
                    timeStamp.put(recv.getAddress(), System.currentTimeMillis());
                }
                Thread.sleep(1000);
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }
}
