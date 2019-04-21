import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetAddress;
import java.net.UnknownHostException;
import javax.swing.border.Border;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.AncestorListener;


/**
 * Create the GUI for a connected host. the window has text area to append any send or receive message 
 *  a send button to send message, an exit button to disconnect communication
 *  The constructor takes a socket as parameter, the destination ip address
 *  and port number 
 
 */

public class WinFrame extends JFrame implements ActionListener{
	 
	private JFrame frame;
	private JButton sendButton, exitButton; // Send and Exit buttons
	private JTextField textField;
	private JTextArea textarea, textForPort;
	private JPanel panel;
	private JLabel label;
	private JScrollPane scrollpane;
	private int port;
	private String name;
	private Border borders;
	private Container content;
	private JScrollPane stackTraceScrollPane;
	// Initialize the host object
	private static Socket host;
	private InetAddress hostAddress;
	//private InetAddress sourceAddress;
	
	
	
    // Constructor to Create and set up the window
	
	public WinFrame(Socket host, String name, InetAddress ip, int port) throws UnknownHostException {
        this.host = host;
		this.name = name;
		this.port = port;
		System.out.println("Name = " + name);
		System.out.println("IP   = " + ip);
		hostAddress = ip;
		//System.out.println("Inet = " + hostAddress);
		// Host window title
        //this.sourceAddress = this.host.getAddress();
        String title = "Name:" + " " + this.name + "|| IP : " + hostAddress;
		frame = new JFrame(title);
		frame.setPreferredSize(new Dimension(300, 290));
		this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		content = frame.getContentPane();
		content.setLayout(new FlowLayout());

		textarea = new JTextArea(20, 30);
		textarea.setEditable(false);
		borders = BorderFactory.createLineBorder(Color.BLUE, 1);
		textarea.setBorder(borders);
		textarea.append("\n");

		stackTraceScrollPane = new JScrollPane(textarea, 
				ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		content.add(stackTraceScrollPane, BorderLayout.CENTER);
		textField = new JTextField(30);
		textField.setHorizontalAlignment(JTextField.CENTER);
		content.add(textField);
		// The send and exit buttons
		sendButton = new JButton("SEND");
		sendButton.setHorizontalAlignment(JTextField.CENTER);
		sendButton.setSize(new Dimension(200,200));
		content.add(sendButton);
		exitButton = new JButton("EXIT");
		exitButton.setHorizontalAlignment(JTextField.CENTER);
		content.add(exitButton);

		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.pack();
		frame.setSize(450, (int) frame.getSize().getHeight() + 180);
		sendButton.addActionListener(this);
		exitButton.addActionListener(this);

	}

	// Add sent message in the TextArea 
	public void append(String str) {
		textarea.append(str + "\n");
		
	}
	
	// Getters
		
	public  String getName() {
		return this.name;
	}
	
	public  int getPort() {
		return port;
	}
	
	public  InetAddress getIpAddress() {

		return hostAddress;
	}
	
	public  JTextArea getTextarea() {
		return textarea;
	}
 
	
	//The buttons event handling
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getActionCommand()=="SEND") {
			String message = textField.getText();
			InetAddress ip = getIpAddress();
			System.out.println("Send IP = " + ip);
			int port = getPort();
			textarea.append("Arezouma: " + message + "\n");	
			host.send(message, ip, port);
		}
		if(e.getActionCommand()=="EXIT") {
			
            frame.dispose();
			
		}
	}
	
	public void win()
	{ 
		frame.setVisible(true);
		
	}
	
	
}
