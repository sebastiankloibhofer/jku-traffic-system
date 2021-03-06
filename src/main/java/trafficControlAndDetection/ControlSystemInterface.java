package trafficControlAndDetection;

import java.util.HashMap;

import controlsystem.ControlSystem;
import controlsystem.model.GraphPart;

public class ControlSystemInterface {
	TrafficControlAndDetection tcontrol;
	ControlSystem commInterface;
	
	
	public ControlSystemInterface(ControlSystem commInterface, TrafficControlAndDetection tcontrol) {
		this.commInterface = commInterface;
		this.tcontrol = tcontrol;
	}
	
	public void receiveCommand(Command c){
		tcontrol.receiveCommand(c);
	}
	
	protected void sendData(HashMap<GraphPart, Integer> map) {
		map.keySet().forEach(x -> commInterface.updateParticipantCount(x.getId(), map.get(x)));
	}
	
	protected void sendErrorReport() {
		//TODO (not a selected feature)
	}
}
