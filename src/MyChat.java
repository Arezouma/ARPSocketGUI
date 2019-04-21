
import java.awt.*;
import java.awt.event.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.net.DatagramPacket;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.util.Base64;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.swing.*;
 
/** ARP Socket with GUI. 
 * 
 * Author: Arezouma Solly N.
 * 
 * GUI is set up to use Address Resolution Protocol to provide connection
 * between computers on the LAN. Each computer that requests the address of the 
 * other computer will create a window for it and an internal table to keep
 * track of all created window in order to append the message to the respective
 * window.
 * */
public class MyChat extends JFrame implements ActionListener {
    
	/**
     * Create the GUI for the connection chat. the window has two text fields: Ip and Port,
     *  a chat button to start communication, an exit button to close it and a clear button to reset the fields.
     *  The constructor takes a socket as parameter  
     
     */
	private static Socket mySocket; // Create socket object 
	private JFrame frame;
	private JTextField nameTextField; // The ip number place holder
	private final JLabel nameLabel; 
	// the start, exit and clear buttons
	private final JButton chatButton; 
	private final JButton exitButton;
	private final JButton clearButton;
	private InetAddress address;
	//private InetAddress broadcast;
	private int port;

	
	//Initialize hashmap to store connection window
	private static HashMap<String, WinFrame> winTable = new HashMap<>();
	
	
	public MyChat(Socket socket) throws UnknownHostException {
        //Create and set up the window.
        super("Connect Frame");
		mySocket = socket;
		port = 64000;
		this.address = InetAddress.getByName("255.255.255.255");
		setLayout(new FlowLayout());
		// construct  name TextField
		nameTextField = new JTextField(20); 
		nameLabel = new JLabel("Name        : "); // label for ip
		nameLabel.setHorizontalAlignment(JLabel.LEFT);
		add(nameLabel);
		add(nameTextField); // add ip TextField to JFrame
		
		
		// create new Buttons and ButtonHandler for button event handling 
		
		chatButton = new JButton("Chat");
		exitButton = new JButton("Exit");
		clearButton = new JButton("Clear");
		
		chatButton.addActionListener(this);
		exitButton.addActionListener(this);
		clearButton.addActionListener(this);
		
		add(clearButton);
		add(exitButton);
	    add(chatButton);
		
		
		
		
	}
	 
	// handle button event
	@Override
	public void actionPerformed(ActionEvent event)
    {   
		// Get input name from JTextField

		String name = nameTextField.getText();
		
		String key = name + ":" + port;
		
    	
	 	if (event.getActionCommand()=="Chat")
	 	{	
	 	// Check if the name and corresponding port exist in the table 
	 		if (!name.isEmpty() && !winTable.containsKey(key))  
	 		{	
	 			String msg = "????? " + name + " ##### " + "Arezouma";
	 			System.out.println("Address = " + address.getHostAddress());
	 			System.out.println("Port    = " + port);
	 			mySocket.send(msg, address, port); 
	 			System.out.println("Message send");
			   		 
			 }	
	 	 }
	 	 if(event.getActionCommand()=="Exit") {
	 		 
	 		   exitButton.addActionListener(new CloseListener());
		}

	 	  
	 	if (event.getActionCommand() == "Clear") 
	 	{
	 		nameTextField.setText(" ");
	 	    
	 	}
	 	  
		
	}
	// Getters
	
	// get name from ipTextfield as a String 
	public String getName(){
		return nameTextField.getText();
	}
	
	
		
	// get port number from portTextfield as a String
	public int getPort(){
		return port;
	}
	
	public static Socket getSocket() {
		return mySocket;
	}
	
	public static void recievePackets() throws UnknownHostException {
		DatagramPacket inPacket = null;
		
		do {
			try {
				inPacket = mySocket.receive();
				// Get message, ip, and port to set hash key	
					if (inPacket != null) {
						String message = new String(inPacket.getData());
						System.out.println("Received Message = " + message);
						InetAddress address= inPacket.getAddress();
						int port = inPacket.getPort();
						String key = address + ":" + port;
						WinFrame chat = null;
					// Check if the received message requests an address of the local computer
						if (message.startsWith("?") && 
								(message.substring(5, message.lastIndexOf("#")-4).contains("Arezouma"))) {
							String msg = "##### " + "Arezouma " + "##### " + mySocket.getAddress().getHostAddress();
							mySocket.send(msg, address, port);
							String destName = message.substring(message.lastIndexOf("#") + 1, message.length());
							try {
				 				chat = new WinFrame(mySocket, destName, address, port);
							} catch (UnknownHostException e) {
								e.printStackTrace();
							}
					 	
				             chat.win();
					         winTable.put(key, chat);
						}
						
						// Check if the received message is a response of an address request of the local computer
						else if (message.startsWith("#")) {
							String destName = message.substring(5, message.lastIndexOf("#") -4);
							String ipAdd = message.substring(message.lastIndexOf("#") + 1, message.length());
					
						
							key =  address + ":" + port;
		
							// Check if key in wintable, then create WinFrame and append message
				 			try {
				 				chat = new WinFrame(mySocket, destName, address, port);
							} catch (UnknownHostException e) {
								e.printStackTrace();
							}
					 	
				             chat.win();
					         winTable.put(key, chat);
								// Otherwise create WinFrame and add to the wintable
					    } else {
					    	
					    	//if (winTable.containsKey(key)) 
							chat = winTable.get(key);
							if (!message.startsWith("?") && (!message.startsWith("#"))) {
								
								chat.getTextarea().append(address + ":" + message + "\n");
								chat.win();
							}
					    }
					}
			   } catch (NullPointerException ne) {
						// Nothing to do
			   }
			} while (true); 
		} 
    
    
	public void mainWin()
	{ 
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(350, 200);
		setVisible(true);
	
	}
	
	// Inner class to close window when exit button clicked
	private class CloseListener implements ActionListener{
	    @Override
	    public void actionPerformed(ActionEvent event) {
	    	if (event.getSource() == exitButton) {
	    		
	    		MyChat.getSocket().close();
				 System.exit(0);
	    	}
	              
	    }
	}
	
}
