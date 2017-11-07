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
		 CupCarbon.server.synchroRealAndVirtualNode(id, id);
		 CupCarbon.server.runScript(String.valueOf(id));
	}

}
