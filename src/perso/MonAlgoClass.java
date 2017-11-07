package perso;

import java.util.LinkedList;
import java.util.List;

import device.DeviceList;
import device.SensorNode;
import map.MapLayer;


public class MonAlgoClass extends Thread {

	public void run() {
		DeviceList.initAll();
		MapLayer.repaint();


		List<SensorNode> capteurs = DeviceList.sensors;

		SensorNode startingNode = capteurs.get(0);
		lancer(startingNode);

	}

	public void lancer(SensorNode node) {

	}



}
