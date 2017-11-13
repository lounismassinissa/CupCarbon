package ccl;

import java.io.File;

import cupcarbon.CupCarbon;
import cupcarbon.HBoxCell;
import project.Project;

public class Checker extends Thread {

	
	public Checker() {

	}
	public void run() {
		while(true) {
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			String realNodeId;
			for(HBoxCell cell:CupCarbon.cupCarbonController.myObservableList){
				realNodeId = cell.getSelectedElementCurrentID();
				String path = Project.getRealNodePath();
				String realNodeFileName = path +File.separator+ realNodeId;
				RealNodeConfig config= new RealNodeConfig(realNodeFileName+".rn");
				SessionClient session = Adapter.getSessionByRealNodeId(realNodeId);
				if(session != null) {
					if(session.isConnected()) {
						config.status = "online";
					}else {
						config.status = "offline";
					}
				}else {
					System.out.println("session null");
				}
				config.save();
				cell.updateConnection();
			}
		}
	}
}
