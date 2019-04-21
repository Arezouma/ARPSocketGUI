
import java.net.UnknownHostException;

import javax.swing.JFrame;


/***
 * 
 * @author Arezouma S Ngobou
 *
 */
public class Driver {
	// A driver to create communication as long as the socket receives packet
	private Socket mySocket;
	public static void main(String[] args) throws UnknownHostException {
	
	//	Initialize a Socket
		Socket mySocket =new Socket(64000, Socket.SocketType.Broadcast);
		
		MyChat newChat = new MyChat(mySocket);
		newChat.mainWin();
       // Start communication whenever the socket receive a packet from a host
		do {
			MyChat.recievePackets();
		} while(true);
		}

	}



	
