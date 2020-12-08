import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.*;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs.Ids;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 *
 * @author tliu
 */
public class ClientWriter extends Thread {

    EchoClient ec;
    boolean histoireAffichee = false;
    String tmp = null;
    String verifyCode = "noe";

    public ClientWriter(EchoClient ec) {
        this.ec = ec;
    }
    BufferedReader stdIn = null;
    
    public void sendMsg(String Msg, PrintStream ps){
        ps.println(verifyCode+Msg);
    }

    @Override
    public void run() {
        
        try {
            OutputStream os = ec.echoSocket.getOutputStream();
            PrintStream ps = new PrintStream(os);

            ec.frame.button_2.addActionListener(new ActionListener() {
                //@Override
                public void actionPerformed(ActionEvent e) {
                    if(isInterrupted()){
                        ec.frame.button_2.removeActionListener(this);
                    }
                        
                    String line = ec.frame.textArea_1.getText();
                    if (line != null && !line.equals("")) {
                        SimpleDateFormat sdf = new SimpleDateFormat();
                        sdf.applyPattern("yyyy-MM-dd HH:mm:ss");
                        String msg = ec.userName + " (" + sdf.format(new Date()) + ")" + ": ";
                        sendMsg(msg,ps);
                        sendMsg(line,ps);
                        sendMsg("",ps);
                        
                        
                        try {
							ec.zkClient.create("/history/record", (msg+"\n"+line).getBytes(), Ids.OPEN_ACL_UNSAFE,
CreateMode.EPHEMERAL_SEQUENTIAL);
						} catch (KeeperException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						} catch (InterruptedException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
                        
                        
                        ec.frame.textArea_1.setText("");
                        ps.flush();
                    } else {
                        ps.println("Write something..");
                        ec.frame.textArea_1.setText("");
                        ps.flush();
                    }
                }
            });
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
