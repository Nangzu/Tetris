package Tetris1;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class ServerHandler extends Thread {
	private Socket           s;
    private BufferedReader   i;
    private PrintWriter       o;
    private TetrisServer       server;
    public ServerHandler(TetrisServer server, Socket s) throws IOException {
    	   this.s = s;
    	   this.server = server;
    	   InputStream ins = s.getInputStream();
    	   OutputStream os = s.getOutputStream();
    	   i = new BufferedReader(new InputStreamReader(ins));
    	   o = new PrintWriter(new OutputStreamWriter(os), true);
    }
    public void run () {
	    try {
		    server.register(this);
		    while (true) {
			    String msg = i.readLine();
			    broadcast (msg);
		    }
	    } catch (IOException e) {
	    	e.printStackTrace();
	    }
	    server.unregister(this);
	    try {
	    	i.close();
	    	o.close();
	    	s.close();
	    } catch (IOException e) {
	    	e.printStackTrace();
	    }
    }
    protected void println(String message) {
    	o.println(message);
    }
    protected void broadcast (String message) {
    	server.broadcast(message);
    }
}