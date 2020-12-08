import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.io.File;

public class EchoServerMultiThreaded  {
  
 	/**
  	* main method
	* @param EchoServer port
  	* 
  	**/
        public static List<Socket> list = new ArrayList<Socket>();
        public static String historyName = "E:\\Helsinki\\distributed system\\history.txt";
        public static File history = null;
        public static String verifyCode = "noe";
        
        public static String port = "12005";
        
    public void run(){ 
        ServerSocket listenSocket;
        
        try {
            listenSocket = new ServerSocket(Integer.parseInt(port)); //port
//            history = new File(historyName);
//            if(!history.exists()){
//                System.out.println("File not found, creating new file...");
//                if(history.createNewFile()){
//                    System.out.println("New file created");
//                }else{
//                    System.out.println("Failed to create new file");
//                }
//            }
            System.out.println("Server ready..."); 
                
            while (true) {
                Socket clientSocket = listenSocket.accept();
                list.add(clientSocket);
                System.out.println("Connexion from:" + clientSocket.getInetAddress());
                ClientThread ct = new ClientThread(clientSocket);
                for (Socket sk : list) {
                    if(!sk.isClosed()){
                        if(sk==clientSocket) continue;
                        PrintStream ps = new PrintStream(sk.getOutputStream());
                        ps.println("A new user joined in this group chat");
                    }
                }
                
                //Chargement d'histoire
//                InputStreamReader read = new InputStreamReader(new FileInputStream(history), "UTF-8");
//                BufferedReader bufferedReader = new BufferedReader(read);
//                PrintStream ps = new PrintStream(clientSocket.getOutputStream(),true);
//                String lineTxt = null;
//                while((lineTxt = bufferedReader.readLine()) != null){
//                    System.out.println(lineTxt);
//                    ps.println(lineTxt);
//                }
                
                ct.start();
            }
        } catch (Exception e) {
            System.err.println("Error in EchoServer:" + e);
            e.printStackTrace();
        }
    }
}
