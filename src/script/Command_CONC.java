package script;

import wisen_simulation.SimLog;
import device.SensorNode;

public class Command_CONC extends Command {

	protected String arg1 = "";
	protected String arg2 = "";
	protected String arg3 = "";
	
	public Command_CONC(SensorNode sensor, String arg1, String arg2, String arg3) {
		this.sensor = sensor ;
		this.arg1 = arg1 ;
		this.arg2 = arg2 ;
		this.arg3 = arg3 ;
	}

	@Override
	public double execute() {
		String v1 = sensor.getScript().getVariableValue(arg2);
		String v2 = sensor.getScript().getVariableValue(arg3);
		String z = v1+v2;
		SimLog.add("S" + sensor.getId() + " " + arg1 + " = (" + Double.valueOf(v1) + ") + (" + Double.valueOf(v2) + ") -> " + z);
		sensor.getScript().addVariable(arg1, "" + z);
		return 0 ;
	}

	@Override
	public String toString() {
		return "PLUS";
	}
}