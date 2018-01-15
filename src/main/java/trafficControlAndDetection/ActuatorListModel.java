package trafficControlAndDetection;

import java.util.List;

import javax.swing.AbstractListModel;
import javax.swing.ListModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

public class ActuatorListModel extends AbstractListModel<Actuator> implements ListModel<Actuator> {
	private static final long serialVersionUID = 1L;
	private List<Actuator> actuators;
	
	public ActuatorListModel(List<Actuator> actuators) {
		this.actuators = actuators;
	}

	@Override
	public Actuator getElementAt(int index) {
		return actuators.get(index);
	}

	@Override
	public int getSize() {
		return actuators.size();
	}
	
	public void fireEvents(){
		getListDataListeners();
		for( ListDataListener l : getListDataListeners()  ){
			l.contentsChanged(new ListDataEvent(this, 0 , 0, getSize()));
		}
	}
	
	

}
