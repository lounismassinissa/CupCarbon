package ccl;
import java.net.*;
import java.io.*;
import java.util.*;

import cupcarbon.CupCarbon;
import cupcarbon.CupCarbonController;
import device.DeviceList;
import device.SensorNode;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import project.Project;
import real_node_emulator.Emulator_Node;


public class Server extends Thread
{
   public static LinkedList<Emulator_Node> emulatedNodes = emulatedNodes = new LinkedList<Emulator_Node>();
   public static LinkedList<SessionClient> currentSession = new LinkedList<SessionClient>();

   public Server(){
	  System.out.println("create new server");

   }


   public void run()
   {

    try
    {
      Integer port;
      port=new Integer("8888");

      //new Commandes(server);

      ServerSocket ss = new ServerSocket(port.intValue());
      //view = new ServerView(server);
      SessionClient sc;

      while (true)
      {
    	  sc = new SessionClient(ss.accept() , this);
    	  currentSession.addLast(sc);
    	  sc.start();
      }
    }
    catch (Exception e) { }
  }

  synchronized public void delClient(String id)
  {
	System.out.println("client "+id+" deconnected");
    CupCarbon.cupCarbonController.updateRealNodeItemConnection();
  }

  synchronized public void addClient(String realNodeId, SessionClient session)
  {
	   Adapter.updateRealNodeSession(realNodeId, session);
  }


  synchronized public SessionClient getSessionByRealNodeId(String id)
  {
      return Adapter.getSessionByRealNodeId(id);
  }


  synchronized public SessionClient getSessionByVirtualNodeId(String id)
  {
     return Adapter.getSessionByVirtualNodeId(id);
  }

  synchronized public void synchroRealAndVirtualNode(String realNodeId, String virtualNodeId )
  {
	 Adapter.updateSynchronization(realNodeId, virtualNodeId);

  }

  synchronized public void send(String cmd, String src, String dist, String msg){

	  SessionClient distNodeSession;
	  System.out.println("send "+src+"---->"+dist+" ["+msg+"]");
	  if(dist.equals("*")){
		  List neighbors = Adapter.getVirtualNodeByRealNodeId(src).getNeighbors();
		  SensorNode neighbor;
		  String distSession;

		  for (int i = 0; i < neighbors.size(); i++) {
			  neighbor = (SensorNode) neighbors.get(i);
			  distSession = neighbor.getRealNodeName();
			  if(!distSession.equals("")){
				  distNodeSession = getSessionByRealNodeId(distSession);
				  distNodeSession.send(cmd+" "+src+" "+distSession+" "+msg);
			  }else{
				  System.out.println("real node not yet affected for node :"+neighbor.getId());
			  }
		  }

	  }else{
		  distNodeSession = getSessionByVirtualNodeId(dist);
		  distNodeSession.send(cmd+" "+src+" "+dist+" "+msg);
	  }
  }

  synchronized public void sendScript(String id){
	  SessionClient client = getSessionByRealNodeId(id);
	  System.out.println("id"+id+"        "+client.getId());
	  if(client == null) System.out.println("5");
	  client.ask_send_Script();
  }


  synchronized public void runScript(String id){

	  System.out.println("Script to run: "+id);

	  SessionClient client = getSessionByRealNodeId(id);
	  if(client == null) System.out.println("5");
	  client.runScript();

  }
  synchronized public void stopScript(String id){

	  SessionClient client = getSessionByRealNodeId(id);
	  if(client == null) System.out.println("5");
	  client.arreter();

  }

  synchronized public void createRealNodeEmulator(String id){

	  System.out.println("Creation of a new virtual node: "+id);
	  Emulator_Node emu_node = new Emulator_Node(id);
	  this.emulatedNodes.addLast(emu_node);
	  emu_node.start();

  }

  synchronized public boolean isEmulatedNode(String virtualNodeId){
	  Emulator_Node node;
	  for(int i = 0 ; i < emulatedNodes.size() ; i++ ){
		  node  = emulatedNodes.get(i);
		  if( virtualNodeId.compareTo(node.getID()) == 0){
			  return true;
		  }
	  }
	  return false;
  }

}
