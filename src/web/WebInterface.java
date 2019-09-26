package web;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.Enumeration;

public class WebInterface implements Runnable
{
	int port = 8080;
	//the web requests to be processed by the client
	ArrayList<WebRequest> queue = new ArrayList<WebRequest>();
	public String path = null;
	
	public WebInterface(int port)
	{
		this.port = port;
		Thread t = new Thread( this );
		t.setDaemon( true );
		t.start();
	}
	
	public synchronized void getQueue(ArrayList<WebRequest> q)
	{
	    q.addAll(queue);
	    queue.clear();
	}
	public synchronized void queue(WebRequest e)
	{
		queue.add(e);
	}
	
	public void run()
	{
		try
		{
			final ServerSocket ss = new ServerSocket( port );
			while( true )
				new WebRequest( this, ss.accept() );
		}
		catch ( IOException ioe )
		{}
	}
}