package Tetris1;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

public class TetrisServer extends Thread {
	private Vector<ServerHandler> handlers;
	private int port;
	public TetrisServer(int port) {
		this.port = port;
	}
	
	public void run () {
		ServerSocket server = null;
		try {
			server = new ServerSocket (port);
			handlers = new Vector<ServerHandler>();
			System.out.println("Server is ready.");
			while (true) {
				Socket client = server.accept();
				System.out.println("Connected from " + client.getInetAddress() + ":" + client.getPort());
				ServerHandler c = new ServerHandler(this, client);
				c.start();
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	public void register(ServerHandler c) {
		handlers.addElement(c);
	}
	
	public void unregister(Object o) {
		handlers.removeElement(o);
	}

	public void broadcast (String message) {
		synchronized (handlers) {
			int n = handlers.size();
			for(int i=0; i < n; i++) {
				ServerHandler c = handlers.elementAt(i);
				try {
					c.println(message);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
}
