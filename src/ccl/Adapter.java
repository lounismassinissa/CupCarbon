package ccl;

import java.util.HashMap;

import cupcarbon.CupCarbon;
import device.SensorNode;

public class Adapter {

	private static HashMap<String,String> virtualRealId = new HashMap<String,String>();
	private static HashMap<String,String> realVirtualId = new HashMap<String,String>();
	private static HashMap<String,SessionClient> realNodeIdSession = new HashMap<String,SessionClient>();



	public static void updateSynchronization(String realNodeId,String virtualNodeId ){
		virtualRealId.put(realNodeId, virtualNodeId);
		realVirtualId.put(virtualNodeId, realNodeId);
	}

	public static void updateRealNodeSession(String realNodeId, SessionClient session){

		realNodeIdSession.put(realNodeId, session);
	}

	public static SessionClient getSessionByVirtualNodeId(String id){
		String realNodeId = realVirtualId.get(id);
		return realNodeIdSession.get(realNodeId);
	}

	public static SessionClient getSessionByRealNodeId(String id){
		String realNodeId = realVirtualId.get(id);
		return realNodeIdSession.get(realNodeId);
	}

	public static SensorNode getVirtualNodeByRealNodeId(String id){
	    String virtualNodeId = virtualRealId.get(id);
		return CupCarbon.cupCarbonController.getSensorNodeById(Integer.valueOf(virtualNodeId));
	}

}
