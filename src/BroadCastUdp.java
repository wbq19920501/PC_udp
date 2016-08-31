import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;


public class BroadCastUdp extends Thread {
	private String dataString;
	DatagramPacket dataPacket = null;
	DatagramSocket udpSocket = null;
	
	public BroadCastUdp(String dataString) {
		this.dataString = dataString;
	}

	public void run() {
		try {
			udpSocket = new DatagramSocket();
			byte[] data = dataString.getBytes();
			dataPacket = new DatagramPacket(data, data.length,
					InetAddress.getByName("255.255.255.255"), 43708);
			udpSocket.send(dataPacket);
		}catch (Exception e) {
			
		}finally{
			if(null != udpSocket){
				udpSocket.close();
			}
		}
	}
	public static void main(String[] args) {
		new Thread(new TcpReceive()).start();
		new BroadCastUdp("PC request：").start();
	}
}

class TcpReceive implements Runnable {
	public void run() {
		while (true) {
			Socket socket = null;
			ServerSocket ss = null;
			BufferedReader in = null;
			try {
				ss = new ServerSocket(8080);

				socket = ss.accept();

				if (socket != null) {
					in = new BufferedReader(new InputStreamReader(
							socket.getInputStream()));

					StringBuilder sb = new StringBuilder();
					sb.append(socket.getInetAddress().getHostAddress());
					String line = null;
					while ((line = in.readLine()) != null) {
						sb.append(line);
					}

					final String ipString = sb.toString().trim();// "192.168.0.104:8731";
					System.out.println("收到：" + ipString + "\n");

				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					if (in != null)
						in.close();
					if (socket != null)
						socket.close();
					if (ss != null)
						ss.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}