import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import ch.aplu.util.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;



public class EV3ClientConsole {

	


	  private String[] responseMsg =
	  {
	    "OK", "SEND_FAILED", "ILLEGAL_METHOD",
	    "ILLEGAL_INSTANCE", "CMD_ERROR", "ILLEGAL_PORT", "CREATION_FAILED"
	  };

	  private String ipAddress = "10.0.1.1";
	  private int port = 1299;
	  private OutputStream os = null;
	  private InputStream is = null;

	  public EV3ClientConsole()
	  {
	    ch.aplu.util.Console c = new ch.aplu.util.Console();
	    HiResTimer timer = new HiResTimer();

	    try
	    {
	      System.out.println("Trying to connect to " + ipAddress);
	      Socket s = new Socket(ipAddress, port);
	      System.out.println("Connection established.");
	      System.out.println("Enter command<cr>");
	      os = s.getOutputStream();
	      is = s.getInputStream();
	      String command = "";
	      while (true)
	      {
	        command = c.readLine();
	        if (command.length() == 0)
	        {
	          System.out.println("Illegal command");
	          continue;
	        }
	        timer.start();
	        sendCommand(command);
	        String response = readResponse();
	        int rc = 0;
	        try
	        {
	          rc = Integer.parseInt(response);
	        }
	        catch (NumberFormatException ex)
	        {
	        }
	        if (rc >= 0)
	          System.out.print("Response: " + response);
	        else
	          System.out.print("Response (error): " + responseMsg[-rc]);

	        System.out.println(" in " + timer.getTime() / 1000 + " ms");
	      }
	    }
	    catch (Exception ex)
	    {
	      ex.printStackTrace();
	    }
	  }

	  private void sendCommand(String cmd) throws IOException
	  {
	    if (cmd == null || cmd.length() == 0 || os == null)
	      throw new IOException("sendCommand failed.");
	    cmd += "\n";  // Append \n
	    byte[] ary = cmd.getBytes(Charset.forName("UTF-8"));
	    os.write(ary);
	    os.flush();
	  }

	  private String readResponse() throws IOException
	  {
	    if (is == null)
	      return "";
	    byte[] buf = new byte[4096];
	    ByteArrayOutputStream baos = new ByteArrayOutputStream();
	    boolean done = false;
	    while (!done)
	    {  
	       int len = is.read(buf);
	       if (len == -1)
	         throw new IOException("Stream closed");
	       baos.write(buf, 0, len);
	       if (buf[len - 1] == 10)  // \n
	         done = true;
	    }
	    String s = baos.toString("UTF-8");
	    return s.substring(0, s.length() - 1);  // Remove \n
	  }

	  public static void main(String[] args)
	  {
	    new EV3ClientConsole();
	  }	
}
