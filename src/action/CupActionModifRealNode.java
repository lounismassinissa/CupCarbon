package action;

import cupcarbon.CupCarbon;
import device.SensorNode;

public class CupActionModifRealNode  extends CupAction{

	private SensorNode sensorNode;
	private String realNode;
	private String cRealNode;
	
	public CupActionModifRealNode(SensorNode sensorNode, String cRealNode, String realNode) {
		super();
		this.sensorNode = sensorNode;
		this.realNode = realNode;
		this.cRealNode = cRealNode;
	}

	@Override
	public void execute() {
		if(!CupCarbon.cupCarbonController.deviceParamPane.isExpanded())
			CupCarbon.cupCarbonController.deviceParamPane.setExpanded(true);
		sensorNode.setSelected(true);
		
		sensorNode.setRealNode(realNode);
	}

	@Override
	public void antiExecute() {
		if(!CupCarbon.cupCarbonController.deviceParamPane.isExpanded())
			CupCarbon.cupCarbonController.deviceParamPane.setExpanded(true);
		sensorNode.setSelected(true);
		
		sensorNode.setRealNode(cRealNode);
	}
}
