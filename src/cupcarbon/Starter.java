package cupcarbon;

public class Starter extends Thread {

    private String id;
	public Starter(String id ){
		this.id = id;

	}
	public void run(){
		 try {
			this.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		 CupCarbon.cupCarbonController.real_nodes_server.synchroRealAndVirtualNode(id, id);
		 CupCarbon.cupCarbonController.real_nodes_server.runScript(String.valueOf(id));
	}

}
