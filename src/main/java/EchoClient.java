import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.*;
import java.net.*;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

public class EchoClient {

    /**
     * main method accepts a connection, receives a message from client then
     * sends an echo to the client
     *
     */
    public static Socket echoSocket = null;
    public static String userName = null;
    public static ChatFrame frame = null;
    public ZooKeeper zkClient = null;

    public void run(String ip, String port, ZooKeeper zkClient) throws IOException {

        EchoClient c = new EchoClient();
        c.zkClient = zkClient;

        //GUI
        JFrame.setDefaultLookAndFeelDecorated(true);
        JDialog.setDefaultLookAndFeelDecorated(true);

        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (ClassNotFoundException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        } catch (InstantiationException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        } catch (IllegalAccessException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        } catch (UnsupportedLookAndFeelException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    frame = new ChatFrame();
                    frame.setVisible(true);
                    frame.textArea_2.setText("Please tape your name:\r\n");
                    frame.button_2.addActionListener(new ActionListener() {
                        //@Override
                        public void actionPerformed(ActionEvent e) {

                            String line = frame.textArea_1.getText();
                            if (line == null || line.equals("")) {
                                frame.textArea_2.append("invalid name...\r\n");
                            } else {
                                userName = line;
                                frame.textArea_1.setText("");
                                frame.textArea_2.setText("");
                                frame.button_2.removeActionListener(this);
                            }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        try {
            // creation socket ==> connexion
            while (true) {
                if (userName != null) {
                    break;
                }
                System.out.flush();
            }

            echoSocket = new Socket(ip, new Integer(port).intValue());

            // creation thread reader & writer
            ClientWriter cw = new ClientWriter(c);
            ClientReader cr = new ClientReader(c);

            cw.start();
            cr.start();

            frame.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent we) {
                    try {
                        PrintWriter ps = new PrintWriter(echoSocket.getOutputStream());
                        ps.println("exit");
                        ps.flush();

                        cr.interrupt();
                        cw.interrupt();

                        //echoSocket.close();

                        //frame.removeWindowListener(this);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    System.exit(0);
                }
            });

        } catch (UnknownHostException e) {
            System.err.println("Don't know about host:" + ip);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for "
                    + "the connection to:" + ip);
            System.exit(1);
        }

    }
    
    public void stop() {
    	frame.dispose();
    }
}
