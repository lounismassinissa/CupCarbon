package real_node_emulator;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;
import java.util.regex.Pattern;

import project.Project;


public class Emulator_Node extends Thread {


	private Socket s;
	public Socket dataSocket;
	private String ID; // identifier of the client
	private PrintStream out;
	private BufferedReader in;
	private String server_ip;
	private int count = 0;

	public static String path;

	public Emulator_Node(String ID){
		this.ID = ID;
		createFolder();
		System.out.println("Node creation my id:"+ID);
		this.server_ip = "127.0.0.1";
	}

	private void createFolder(){
		path = Project.getProjectVirtualNodesPath()+File.separator+ID;
		File file  = new File(path);
		file.mkdir();

		System.out.println("folder path:"+path);

	}

	public void run(){
		connect();
	}

	public String getID(){
		return this.ID;
	}

	public void connect(){
		try {

		    s = new Socket(server_ip,8888);
		    out = new PrintStream(s.getOutputStream());
		    in = new BufferedReader(new InputStreamReader(s.getInputStream()));
		    System.out.println("Connection OK");
		    initCom();
			new Receiver(this, in, out);

		} catch (IOException e) {
			System.out.println("Server not found !");
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e1) {
				System.out.println("wait reconnecting error ");
			}
			count++;
			if(count < 10) connect();
		}
	}

	public void send(String msg){
			out.println(msg);
			System.out.println(msg);
	}


	public void initCom(){
		// initialize the communication between the client and server
		// Send get identifier
		out.println(COMMAND.ASK_CONNECTION+" 127.0.0.1 "+this.ID);
	}

}
