package ccl;
import java.net.*;
import java.util.LinkedList;
import java.util.Scanner;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.jdesktop.swingx.HorizontalLayout;
import org.jdesktop.swingx.VerticalLayout;

import cupcarbon.CupCarbon;
import device.DeviceList;
import device.SensorNode;
import map.MapLayer;
import project.Project;
import wisen_simulation.SimLog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;


public class SessionClient extends Thread{

  private Socket _s;
  private Socket data_socket;
  private ServerSocket data_server_socket;
  private PrintStream data_out;
  private int data_socket_port = 8889;
  private PrintStream out;
  private BufferedReader in;
  private Server server;
  private String realNodeId;
  private boolean synch = false;
 // private GUI screen;
  private String fileName = "";

  private String ip;
  private String mac;


  SessionClient(Socket s, Server serv)
  {
    server = serv;
    _s = s;
    try
    {
      out = new PrintStream(_s.getOutputStream());
      in = new BufferedReader(new InputStreamReader(_s.getInputStream()));

    }
    catch (IOException e){ }

  }
  public String getID(){
	  return realNodeId;
  }

  public void setSynchro(boolean syn){
	  synch = syn;
  }
  
  public boolean isConnected() {
	 return _s.isConnected();
  }

  public String ip(){
	  return ip;
  }
  public String mac(){
	  return mac;
  }

  public void run()
  {

    String message = "";
    try
    {

      while(true)
      {
    	    message = in.readLine();
    	    //System.out.println("read data "+message);
    	  	handler(message);
      }
    }
    catch (Exception e){
    	 System.out.println("le message :"+message);
         server.delClient(realNodeId);
         try {
			_s.close();
		} catch (IOException e1) {

			System.out.println("error close socket client "+realNodeId);
		}
    }

  }

  public void send(String message){
	  out.println(message);

  }

  private void handler(String message){

	  BufferedReader fileReader = null;
	  String tab[] = message.split(" ");

	  if(tab[0].compareTo(COMMAND.ASK_CONNECTION)==0){

		  display("CMD", message);
		  ip= tab[1];
		  realNodeId = tab[2];
		  server.addClient(realNodeId+"",this);
		  System.out.println("ajout nouveau client id: "+realNodeId);
		  CupCarbon.cupCarbonController.updateRealNode(realNodeId , ip);
		  out.println(COMMAND.CONNECTION);

	  }
	  if(tab[0].compareTo(COMMAND.SEND)== 0){
		  System.out.println("---------------:"+message);
		  String cmd = tab[0];
		  String src = tab[1];
		  String dist = tab[2];
		  String msg = tab[3];
		  server.send(cmd, src, dist, msg);

	  }
	  if(tab[0].compareTo(COMMAND.OK_GET_SCRIPT)== 0){
		  display("CMD", message);

		  try {
			data_out = new PrintStream(data_socket.getOutputStream());

			String sCurrentLine;

			File scriptsFolderFile = new File(Project.getProjectScriptPath());
			String[] scripts = scriptsFolderFile.list();
			for (int i = 0; i < scripts.length; i++) {
				if (scripts[i] != null)
					if (!scripts[i].startsWith(".")){
						fileReader = new BufferedReader(new FileReader(Project.getProjectScriptPath()+File.separator+scripts[i]));
						data_out.println("file "+scripts[i]);
						while ((sCurrentLine = fileReader.readLine()) != null) {
							data_out.println(sCurrentLine);
						}
						data_out.println("EOF");
					}

			}
			data_out.println("EOD");
		  } catch (IOException e) {
			  e.printStackTrace();
		  }
	  }
	  if(tab[0].compareTo(COMMAND.GET_SCRIPT)==0){
		  display("CMD", message);
		  ask_send_Script();
	  }
	  if(tab[0].compareTo(COMMAND.ALL_DATA_RECEIVED)==0){
		  display(this.getID()+"", message);

		  String script = Adapter.getVirtualNodeByRealNodeId(realNodeId).getScriptFileName();
		  display(this.getID()+"", script);
		  String x = String.valueOf(Adapter.getVirtualNodeByRealNodeId(realNodeId).getIntPosition()[0]);
		  String y = String.valueOf(Adapter.getVirtualNodeByRealNodeId(realNodeId).getIntPosition()[1]);
		  display(this.getID()+"", x+" "+y);
		  out.println(COMMAND.RUN_SCRIPT+" "+script+" "+Adapter.getVirtualNodeByRealNodeId(realNodeId).getId()+" "+Adapter.getVirtualNodeByRealNodeId(realNodeId).getNeighbors().size()+" "+x+" "+y);

	  }
	  if(tab[0].compareTo(COMMAND.PING)==0){
		  Ping ping =  new Ping(out);
		  ping.start();
	  }
	  if(tab[0].compareTo(COMMAND.MARK)==0){

		  	//System.out.println(realNodeId+" my virtual node ID is: "+Adapter.getVirtualNodeByRealNodeId(realNodeId).getId());
		    String s = tab[1];
		  	if(s.equals("0")) {
		  		Adapter.getVirtualNodeByRealNodeId(realNodeId).setMarked(false);
			    MapLayer.repaint();
			}
			else {
				Adapter.getVirtualNodeByRealNodeId(realNodeId).setMarked(true);
				MapLayer.repaint();
			}
	  }

	  if(tab[0].compareTo(COMMAND.ADDEDGE)==0){
		  int id1 = Integer.valueOf(tab[1]);
		  int id2 = Integer.valueOf(tab[2]);
		  SensorNode sensor1 = DeviceList.getSensorNodeById(id1);
		  SensorNode sensor2 = DeviceList.getSensorNodeById(id2);

		  DeviceList.addEdge(sensor1, sensor2);
	  }
	  if(tab[0].compareTo(COMMAND.REMOVEEDGE)==0){
		  int id1 = Integer.valueOf(tab[1]);
		  int id2 = Integer.valueOf(tab[2]);
		  SensorNode sensor1 = DeviceList.getSensorNodeById(id1);
		  SensorNode sensor2 = DeviceList.getSensorNodeById(id2);

		  DeviceList.removeEdge(sensor1, sensor2);
	  }

  }

  public void ask_send_Script(){
	     try {

	    	 InetAddress ip;
	    	 ip = InetAddress.getLocalHost();

	    	 data_socket_port = Integer.valueOf(realNodeId) + 9000 ;
	    	 System.out.println(realNodeId + " sending script port: ");
	    	 data_server_socket = new ServerSocket(data_socket_port);
			 out.println(COMMAND.SCRIPT+" 10.3.141.2 "+data_socket_port);
			 System.out.println(realNodeId + " Command script sended");
			 data_socket = data_server_socket.accept();

		} catch (IOException e) {
			 System.out.println("Erreur communication node: "+realNodeId);
			 arreter();
			 ask_send_Script();
			 System.out.println("connexion to node "+realNodeId);
		}
  }

  public void arreter(){
      if(data_socket != null && data_server_socket !=null ){
		  try {
			data_socket.close();
			data_server_socket.close();
		  } catch (IOException e) {
			e.printStackTrace();
		  }
      }
	  out.println(COMMAND.STOP_SCRIPT);
  }

  public void runScript(){
	  out.println(COMMAND.RUN_SCRIPT);
	  String script = Adapter.getVirtualNodeByRealNodeId(realNodeId).getScriptFileName();
	  String x = String.valueOf(Adapter.getVirtualNodeByRealNodeId(realNodeId).getIntPosition()[0]);
	  String y = String.valueOf(Adapter.getVirtualNodeByRealNodeId(realNodeId).getIntPosition()[1]);
	  out.println(COMMAND.RUN_SCRIPT+" "+script+" "+Adapter.getVirtualNodeByRealNodeId(realNodeId).getId()+" "+Adapter.getVirtualNodeByRealNodeId(realNodeId).getNeighbors().size()+" "+x+" "+y);
  }

  public void display(String id, String msg){
	  System.out.println(id+"   "+msg);
  }

  private int nb_lines(String fileName){
	    BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader(fileName));
			String sCurrentLine;
			int i = 0;
			while ((sCurrentLine = reader.readLine()) != null) {
				i++;
			}
			return i;
		} catch (IOException e) {
			return 0;
		}
  }

  private void progress(int i, int total){
	  	int pro = (int)i * 100 / total;

  }

}