import java.io.*;
import java.net.*;

public class ClientThread
	extends Thread {
	
	private Socket clientSocket;
	
	ClientThread(Socket s) {
		this.clientSocket = s;
	}

 	/**
  	* receives a request from client then sends an echo to the client
  	* @param clientSocket the client socket
  	**/
	public void run() {
    	  try {
    		BufferedReader socIn = null;
    		socIn = new BufferedReader(
    			new InputStreamReader(clientSocket.getInputStream()));    
    		while (true) {
                    String line = socIn.readLine();
                    String code = EchoServerMultiThreaded.verifyCode;
                    if (code.length() <= line.length() && code.equals(line.substring(0, code.length()))) {

                        File history = EchoServerMultiThreaded.history;
                        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(history, true)));
                        out.write(line.substring(code.length(),line.length()) + "\r\n");
                        out.flush();
                        out.close();
                    }else if(line.length()>=4&&line.equals("exit")){
                        for (Socket sk : EchoServerMultiThreaded.list) {
                            if(!sk.isClosed()&&sk!=clientSocket){
                                PrintStream ps = new PrintStream(sk.getOutputStream());
                                ps.println("A user left this chat room");
                                ps.flush();
                            }
                        }
                        clientSocket.close();
                        break;
                    }
    		}
                
    	} catch (Exception e) {
        	System.err.println("Error in EchoServer:" + e); 
        }
       }
  
  }