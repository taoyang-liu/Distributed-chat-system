import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

public class DistributeClient {
	//TODO ip:port of zookeeper cluster
	private static String connectString = "192.168.64.133:2181,192.168.64.134:2181,192.168.64.135:2181";
	private static int sessionTimeout = 2000;
	private ZooKeeper zkClient = null;

	private String parentNode = "/servers";
	
	private String currentIp = null;
	private String currentPort = null;
	private EchoClient ec = null;
	
	private static int counter = 0;

	public void getConnect() throws IOException {
		zkClient = new ZooKeeper(connectString, sessionTimeout, new Watcher() {
			@Override
			public void process(WatchedEvent event) {
				try {
					getServerList();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public void getServerList() throws Exception {
		List<String> children = zkClient.getChildren(parentNode, true);
		Boolean currentInList = false;
		int minNbConn = 100;
		String server = null;
		for (String child : children) {
			byte[] data = zkClient.getData(parentNode + "/" + child, false, null);
			int nbConn = Integer.parseInt(new String(data));
			if(currentIp!=null&&new String(child).equals(currentIp+":"+currentPort))
				currentInList = true;
			if(nbConn < minNbConn) {
				minNbConn = nbConn;
				server = child;
			}
		}
		
		System.out.println(children);
		
		if((currentIp==null&&currentPort==null)||!currentInList) {
			if(ec!=null)
				ec.stop();
			ec = null;
			currentIp = server.split(":")[0];
			currentPort = server.split(":")[1];
			counter++;
		
			if(counter!=2)
				business();
			}
	}

	public void business() throws Exception {
		System.out.println("Chat client connecting to "+currentIp+":"+currentPort);
		
		String curServer = parentNode+"/"+currentIp+":"+currentPort;
		byte[] dataNb = zkClient.getData(parentNode+"/"+currentIp+":"+currentPort, false, null);
		int nb = Integer.parseInt(new String(dataNb));
		Stat stat = zkClient.setData(curServer,Integer.toString(nb+1).getBytes(),-1);
		
		ec = new EchoClient();
		ec.run(currentIp, currentPort,zkClient);
		//Thread.sleep(Long.MAX_VALUE);
	}

	public static void main(String[] args) throws Exception {
		DistributeClient client = new DistributeClient();
		client.getConnect();
		client.getServerList();
		//client.business();
	}

}
