import java.io.*;

/**
 *
 * @author tliu
 */
public class ClientReader extends Thread{
    EchoClient ec ;
    public ClientReader (EchoClient ec){
        this.ec = ec;
    }
    @Override
    public void run() {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(ec.echoSocket.getInputStream()));
            while (true) {
                if(isInterrupted()){
                    br.close();
                    ec.echoSocket.close();
                    break;
                }
                
                String str = br.readLine();
                if (str != null && !str.equals("")) {
                    ec.frame.textArea_2.append(str + "\r\n");
                    ec.frame.textArea_2.setCaretPosition(ec.frame.textArea_2.getText().length());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        }
    
}
