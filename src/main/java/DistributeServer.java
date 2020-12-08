import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Collections;
import java.util.List;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;

public class DistributeServer {
	//TODO ip:port of zookeeper cluster
	private static String connectString = "192.168.64.133:2181,192.168.64.134:2181,192.168.64.135:2181";
	//TODO ip and port of chat system server
	private static String chatSystemServer = "192.168.64.135:12005";
	private static int sessionTimeout = 2000;
	private ZooKeeper zkClient = null;

	private String parentNode = "/servers";
	
	private EchoServerMultiThreaded echoServer = null;

	public void getConnect() throws IOException {
		zkClient = new ZooKeeper(connectString, sessionTimeout, new Watcher() {
			@Override
			public void process(WatchedEvent event) {
				//if(event.getType()==Event.EventType.NodeChildrenChanged) {
					try {
						getLastRecord();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				//}
			}
		});
	}

	public void registServer(String hostname) throws Exception {
		String create = zkClient.create(parentNode + "/" + hostname, "0".getBytes(), Ids.OPEN_ACL_UNSAFE,
				CreateMode.EPHEMERAL);
		System.out.println(hostname + " is online " + create);
	}
	
	public void getLastRecord() throws Exception {
		List<String> records = zkClient.getChildren("/history", true);
		if(records.size()>0) {
			Collections.sort(records);
			String lastRecord = records.get(records.size()-1);
			byte[] data = zkClient.getData("/history" + "/" + lastRecord, false, null);
			String msg = new String(data);
			if(echoServer!=null) {
				for(Socket sk : EchoServerMultiThreaded.list){
					if(!sk.isClosed()) {
						PrintStream ps = new PrintStream(sk.getOutputStream());
		                ps.println(msg);
		                ps.flush();
					}
				}	
			}
		}
	}

	public void business(String hostname) throws Exception {
		System.out.println(hostname + " is working ...");
		echoServer = new EchoServerMultiThreaded();
		echoServer.run();
		//Thread.sleep(Long.MAX_VALUE);
	}

	public static void main(String[] args) throws Exception {
		DistributeServer server = new DistributeServer();
		server.getConnect();
		server.registServer(chatSystemServer);
		server.business(chatSystemServer);
	}

}
